document.addEventListener('DOMContentLoaded', function() {
    var calendarEl = document.getElementById('calendar');
    var calendar;

    var eventModal = document.getElementById('eventModal');
    var modalTitle = document.getElementById('modalTitle');
    var closeButton = document.getElementsByClassName('close-button')[0];
    var eventForm = document.getElementById('eventForm');
    var eventIdInput = document.getElementById('eventId');
    var eventTitleInput = document.getElementById('eventTitle');
    var eventStartDateInput = document.getElementById('eventStartDate');
    var eventEndDateInput = document.getElementById('eventEndDate');
    var allDayEventCheckbox = document.getElementById('allDayEvent');
    var eventDescriptionInput = document.getElementById('eventDescription');
    var saveEventBtn = document.getElementById('saveEventBtn');
    var deleteEventBtn = document.getElementById('deleteEventBtn');
    var cancelBtn = document.getElementById('cancelBtn');

    // 유틸리티 함수: LocalDateTime 포맷팅
    const formatLocalDateTime = (date) => {
        if (!date) return '';
        const year = date.getFullYear();
        const month = (date.getMonth() + 1).toString().padStart(2, '0');
        const day = date.getDate().toString().padStart(2, '0');
        const hours = date.getHours().toString().padStart(2, '0');
        const minutes = date.getMinutes().toString().padStart(2, '0');
        return `${year}-${month}-${day}T${hours}:${minutes}`;
    };

    // 모달 닫기 함수
    function closeModal() {
        eventModal.style.display = 'none';
        eventForm.reset(); // 폼 초기화
        deleteEventBtn.style.display = 'none'; // 삭제 버튼 숨김
        eventIdInput.value = ''; // ID 초기화
    }

    closeButton.onclick = closeModal;
    cancelBtn.onclick = closeModal;
    window.onclick = function(event) {
        if (event.target == eventModal) {
            closeModal();
        }
    };

    // FullCalendar 초기화
    function initializeCalendar() {
        calendar = new FullCalendar.Calendar(calendarEl, {
            initialView: 'dayGridMonth',
            headerToolbar: {
                left: 'prev,next today',
                center: 'title',
                right: 'dayGridMonth,timeGridWeek,timeGridDay'
            },
            locale: 'ko',
            editable: true, // 드래그앤드롭, 리사이즈 가능
            selectable: true, // 날짜 범위 선택 가능

            // 이벤트 데이터 로드 (백엔드 API 호출)
            events: function(fetchInfo, successCallback, failureCallback) {
                $.ajax({
                    url: '/api/schedules',
                    method: 'GET',
                    data: {
                        start: fetchInfo.startStr,
                        end: fetchInfo.endStr
                    },
                    success: function(response) {
                        successCallback(response);
                    },
                    error: function(xhr, status, error) {
                        console.error("일정 로드 실패:", error);
                        failureCallback(error);
                        alert('일정을 불러오는 데 실패했습니다.');
                    }
                });
            },

            // 날짜 클릭 시 새 일정 추가 모달 띄우기
            dateClick: function(info) {
                modalTitle.textContent = '새 일정 추가';
                eventIdInput.value = '';
                eventTitleInput.value = '';
                eventDescriptionInput.value = '';
                allDayEventCheckbox.checked = false;
                deleteEventBtn.style.display = 'none'; // 새 일정 추가 시 삭제 버튼 숨김

                const startDateTime = new Date(info.dateStr);
                const endDateTime = new Date(startDateTime.getTime() + 60 * 60 * 1000); // 1시간 후로 기본 설정

                eventStartDateInput.value = formatLocalDateTime(startDateTime);
                eventEndDateInput.value = formatLocalDateTime(endDateTime);

                eventModal.style.display = 'block';
            },

            // 이벤트 클릭 시 상세 보기/수정 모달 띄우기
            eventClick: function(info) {
                modalTitle.textContent = '일정 수정';
                deleteEventBtn.style.display = 'inline-block'; // 수정 시 삭제 버튼 표시

                // 백엔드에서 해당 이벤트의 상세 정보를 가져옴
                $.ajax({
                    url: `/api/schedules/${info.event.id}`,
                    method: 'GET',
                    success: function(eventData) {
                        eventIdInput.value = eventData.id;
                        eventTitleInput.value = eventData.title;
                        eventStartDateInput.value = formatLocalDateTime(new Date(eventData.start));
                        eventEndDateInput.value = formatLocalDateTime(new Date(eventData.end));
                        allDayEventCheckbox.checked = eventData.allDay;
                        eventDescriptionInput.value = eventData.description || ''; // description이 없을 수도 있음

                        eventModal.style.display = 'block';
                    },
                    error: function(xhr, status, error) {
                        console.error("일정 상세 로드 실패:", error);
                        alert('일정 상세 정보를 불러오는 데 실패했습니다.');
                    }
                });
            },

            // 이벤트 드래그 드롭 후 (일정 이동)
            eventDrop: function(info) {
                const updatedEvent = {
                    id: info.event.id,
                    title: info.event.title,
                    startTime: formatLocalDateTime(info.event.start),
                    endTime: formatLocalDateTime(info.event.end),
                    allDay: info.event.allDay,
                    description: info.event.extendedProps.description // 기존 설명 유지
                };

                $.ajax({
                    url: `/api/schedules/${info.event.id}`,
                    method: 'PUT',
                    contentType: 'application/json',
                    data: JSON.stringify(updatedEvent),
                    success: function() {
                        alert('일정이 성공적으로 이동되었습니다.');
                        calendar.refetchEvents(); // 캘린더 이벤트 다시 로드
                    },
                    error: function(xhr, status, error) {
                        console.error("일정 이동 실패:", error);
                        alert('일정 이동에 실패했습니다.');
                        info.revert(); // 오류 시 이전 상태로 되돌림
                    }
                });
            },

            // 이벤트 리사이즈 후 (일정 기간 변경)
            eventResize: function(info) {
                const updatedEvent = {
                    id: info.event.id,
                    title: info.event.title,
                    startTime: formatLocalDateTime(info.event.start),
                    endTime: formatLocalDateTime(info.event.end),
                    allDay: info.event.allDay,
                    description: info.event.extendedProps.description // 기존 설명 유지
                };

                $.ajax({
                    url: `/api/schedules/${info.event.id}`,
                    method: 'PUT',
                    contentType: 'application/json',
                    data: JSON.stringify(updatedEvent),
                    success: function() {
                        alert('일정 기간이 성공적으로 변경되었습니다.');
                        calendar.refetchEvents(); // 캘린더 이벤트 다시 로드
                    },
                    error: function(xhr, status, error) {
                        console.error("일정 리사이즈 실패:", error);
                        alert('일정 기간 변경에 실패했습니다.');
                        info.revert(); // 오류 시 이전 상태로 되돌림
                    }
                });
            }
        });
        calendar.render();
    }

    initializeCalendar(); // 캘린더 초기화 호출

    // 폼 제출 (새 일정 추가 또는 기존 일정 수정)
    eventForm.addEventListener('submit', function(e) {
        e.preventDefault();

        const id = eventIdInput.value;
        const title = eventTitleInput.value;
        const startTime = eventStartDateInput.value;
        const endTime = eventEndDateInput.value;
        const allDay = allDayEventCheckbox.checked;
        const description = eventDescriptionInput.value;

        const eventData = {
            title: title,
            startTime: startTime,
            endTime: endTime,
            allDay: allDay,
            description: description
        };

        if (id) { // ID가 있으면 수정
            $.ajax({
                url: `/api/schedules/${id}`,
                method: 'PUT',
                contentType: 'application/json',
                data: JSON.stringify(eventData),
                success: function(response) {
                    alert('일정이 성공적으로 수정되었습니다!');
                    closeModal();
                    calendar.refetchEvents();
                },
                error: function(xhr, status, error) {
                    console.error("일정 수정 실패:", xhr.responseText);
                    alert('일정 수정에 실패했습니다: ' + xhr.responseText);
                }
            });
        } else { // ID가 없으면 추가
            $.ajax({
                url: '/api/schedules',
                method: 'POST',
                contentType: 'application/json',
                data: JSON.stringify(eventData),
                success: function(response) {
                    alert('일정이 성공적으로 추가되었습니다!');
                    closeModal();
                    calendar.refetchEvents();
                },
                error: function(xhr, status, error) {
                    console.error("일정 추가 실패:", xhr.responseText);
                    alert('일정 추가에 실패했습니다: ' + xhr.responseText);
                }
            });
        }
    });

    // 삭제 버튼 클릭 시
    deleteEventBtn.addEventListener('click', function() {
        const id = eventIdInput.value;
        if (id && confirm('이 일정을 정말 삭제하시겠습니까?')) {
            $.ajax({
                url: `/api/schedules/${id}`,
                method: 'DELETE',
                success: function() {
                    alert('일정이 성공적으로 삭제되었습니다.');
                    closeModal();
                    calendar.refetchEvents();
                },
                error: function(xhr, status, error) {
                    console.error("일정 삭제 실패:", error);
                    alert('일정 삭제에 실패했습니다.');
                }
            });
        }
    });
});