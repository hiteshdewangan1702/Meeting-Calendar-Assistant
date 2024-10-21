package com.main.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TimeSlot {
    private LocalDateTime start;
    private LocalDateTime end;
}
