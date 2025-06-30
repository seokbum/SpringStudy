package com.myproject.schedulemanagementsystem.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Room {
    private Integer id;
    private String name;
    private Integer capacity;
    private String location;
    private String description;
}