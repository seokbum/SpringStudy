package com.myproject.schedulemanagementsystem.mapper;

import com.myproject.schedulemanagementsystem.model.Event;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface ScheduleMapper {
    List<Event> findAllEvents();
    List<Event> findEventsByTimeRange(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
    void insertEvent(Event event);
    void updateEvent(Event event);
    void deleteEvent(int id);
    Event findEventById(int id);
}