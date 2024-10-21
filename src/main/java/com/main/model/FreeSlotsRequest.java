package com.main.model;

import lombok.Data;

import java.util.List;

@Data
public class FreeSlotsRequest {
    private List<Meeting> employee1Meetings;
    private List<Meeting> employee2Meetings;
    private int duration;
}
