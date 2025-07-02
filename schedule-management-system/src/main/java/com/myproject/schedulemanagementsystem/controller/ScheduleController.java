package com.myproject.schedulemanagementsystem.controller;

import com.myproject.schedulemanagementsystem.dto.ScheduleDto;
import com.myproject.schedulemanagementsystem.model.Event;
import com.myproject.schedulemanagementsystem.service.ScheduleService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController // RESTful API를 제공하는 컨트롤러임을 나타냄
@RequestMapping("/api/schedules") // 기본 URL 경로 설정
public class ScheduleController {

    private final ScheduleService scheduleService;

    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    // FullCalendar가 이벤트를 가져올 때 호출하는 GET 요청
    // http://localhost:8080/api/schedules?start=YYYY-MM-DDTHH:MM:SS&end=YYYY-MM-DDTHH:MM:SS
    @GetMapping
    public ResponseEntity<List<ScheduleDto>> getSchedules(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {

        List<Event> events;
        if (start != null && end != null) {
            events = scheduleService.getEventsByTimeRange(start, end);
        } else {
            // 기간 정보가 없으면 모든 이벤트 조회 (FullCalendar 초기 로딩에는 보통 기간 정보가 있음)
            // 실제 구현에서는 이 경우가 발생하지 않도록 프론트엔드에서 항상 기간을 보내는 것이 좋음
            events = scheduleService.getEventsByTimeRange(LocalDateTime.MIN, LocalDateTime.MAX);
        }

        // FullCalendar 형식에 맞게 DTO로 변환
        List<ScheduleDto> scheduleDtos = events.stream().map(event -> {
            ScheduleDto dto = new ScheduleDto();
            dto.setId(event.getId());
            dto.setTitle(event.getTitle());
            dto.setStart(event.getStartTime()); // FullCalendar는 start, end 필드명을 사용
            dto.setEnd(event.getEndTime());
            dto.setAllDay(event.isAllDay());
            // 필요한 경우 color 등 추가 속성 여기에 매핑
            return dto;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(scheduleDtos);
    }

    // 새 일정을 추가하는 POST 요청
    @PostMapping
    public ResponseEntity<Event> addSchedule(@RequestBody Event event) {
        scheduleService.addEvent(event);
        return new ResponseEntity<>(event, HttpStatus.CREATED); // 201 Created 응답
    }

    // 일정 수정 PUT 요청 (FullCalendar 드래그앤드롭/리사이즈 또는 수정 모달에서 사용)
    @PutMapping("/{id}")
    public ResponseEntity<Event> updateSchedule(@PathVariable int id, @RequestBody Event event) {
        event.setId(id); // URL 경로의 ID를 Event 객체에 설정
        scheduleService.updateEvent(event);
        return ResponseEntity.ok(event);
    }

    // 일정 삭제 DELETE 요청
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable int id) {
        scheduleService.deleteEvent(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 No Content 응답
    }
    // 특정 ID의 일정 상세 정보를 가져오는 GET 요청
    // http://localhost:8080/api/schedules/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Event> getScheduleById(@PathVariable int id) {
        Event event = scheduleService.getEventById(id);
        if (event == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 이벤트가 없으면 404 응답
        }
        return ResponseEntity.ok(event); // 200 OK와 함께 이벤트 반환
    }
    // FullCalendar에서 필요한 이벤트 DTO (내부 클래스로 정의)

}