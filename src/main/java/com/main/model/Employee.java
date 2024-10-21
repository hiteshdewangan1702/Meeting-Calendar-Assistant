package com.main.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Employee {
    private final String id;
    private final List<Meeting> meetings;

    public Employee(String id) {
        this.id = id;
        this.meetings = new ArrayList<>();
    }
}
