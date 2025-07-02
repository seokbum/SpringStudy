package com.myproject.schedulemanagementsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleDto {
    private Integer id;
    private String title;
    private LocalDateTime start; // FullCalendar의 필드명 'start'
    private LocalDateTime end;   // FullCalendar의 필드명 'end'
    private boolean allDay;
    private String description;
    // String color; // FullCalendar에서 이벤트 색상 지정을 위해 추가 가능
}
