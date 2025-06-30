package com.myproject.schedulemanagementsystem.service;

import com.myproject.schedulemanagementsystem.mapper.ScheduleMapper;
import com.myproject.schedulemanagementsystem.model.Event;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ScheduleService {

    private final ScheduleMapper scheduleMapper;

    public ScheduleService(ScheduleMapper scheduleMapper) {
        this.scheduleMapper = scheduleMapper;
    }

    // 모든 이벤트 또는 특정 기간의 이벤트 조회 (FullCalendar가 호출)
    public List<Event> getEventsByTimeRange(LocalDateTime start, LocalDateTime end) {
        // FullCalendar는 start/end를 캘린더 뷰 범위로 보냅니다.
        // 해당 범위에 걸치는 모든 이벤트를 조회합니다.
        return scheduleMapper.findEventsByTimeRange(start, end);
    }

    // 새로운 이벤트 추가
    public void addEvent(Event event) {
        scheduleMapper.insertEvent(event);
    }

    // 이벤트 업데이트 (제목, 시간 등)
    public void updateEvent(Event event) {
        scheduleMapper.updateEvent(event);
    }

    // 이벤트 삭제
    public void deleteEvent(int id) {
        scheduleMapper.deleteEvent(id);
    }

    // ID로 이벤트 조회
    public Event getEventById(int id) {
        return scheduleMapper.findEventById(id);
    }
}