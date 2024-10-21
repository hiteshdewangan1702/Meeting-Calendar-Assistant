package com.main;

import com.main.controller.MeetingController;
import com.main.model.FreeSlotsRequest;
import com.main.model.Meeting;
import com.main.service.MeetingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MeetingControllerTest {
    @Mock
    private MeetingService meetingService;

    private MeetingController meetingController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        meetingController = new MeetingController(meetingService);
    }

    @Test
    void testBookMeeting() {
        // Arrange
        Meeting meeting = new Meeting();
        meeting.setStartTime(LocalDateTime.of(2024, 10, 21, 10, 15));
        meeting.setEndTime(LocalDateTime.of(2024, 10, 21, 10, 45));
        meeting.setParticipants(Arrays.asList("employee1@example.com", "employee2@example.com"));

        // Act
        ResponseEntity<Void> response = meetingController.bookMeeting(meeting);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(meetingService, times(1)).bookMeeting(meeting);
    }

    @Test
    void testFindFreeSlots() {
        // Arrange
        FreeSlotsRequest request = new FreeSlotsRequest();
        request.setEmployee1Meetings(Arrays.asList(
                new Meeting(LocalDateTime.of(2024, 10, 21, 9, 0), LocalDateTime.of(2024, 10, 21, 10, 0), List.of("employee1@example.com")),
                new Meeting(LocalDateTime.of(2024, 10, 21, 11, 0), LocalDateTime.of(2024, 10, 21, 12, 0), List.of("employee1@example.com"))
        ));
        request.setEmployee2Meetings(List.of(
                new Meeting(LocalDateTime.of(2024, 10, 21, 10, 30), LocalDateTime.of(2024, 10, 21, 11, 30), List.of("employee2@example.com"))
        ));
        request.setDuration(30);

        List<MeetingService.TimeSlot> freeSlots = List.of(
                new MeetingService.TimeSlot(LocalDateTime.of(2024, 10, 21, 10, 0), LocalDateTime.of(2024, 10, 21, 10, 30))
        );

        when(meetingService.findFreeSlots(request.getEmployee1Meetings(), request.getEmployee2Meetings(), request.getDuration())).thenReturn(freeSlots);

        // Act
        ResponseEntity<List<MeetingService.TimeSlot>> response = meetingController.findFreeSlots(request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(freeSlots, response.getBody());
        verify(meetingService, times(1)).findFreeSlots(request.getEmployee1Meetings(), request.getEmployee2Meetings(), request.getDuration());
    }

    @Test
    void testCheckConflicts_NoConflicts() {
        // Arrange
        Meeting meeting = new Meeting();
        meeting.setStartTime(LocalDateTime.of(2024, 10, 21, 10, 15));
        meeting.setEndTime(LocalDateTime.of(2024, 10, 21, 10, 45));
        meeting.setParticipants(Arrays.asList("employee1@example.com", "employee2@example.com"));

        when(meetingService.checkConflicts(meeting)).thenReturn(List.of());

        // Act
        ResponseEntity<List<String>> response = meetingController.checkConflicts(meeting);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isEmpty());
        verify(meetingService, times(1)).checkConflicts(meeting);
    }

    @Test
    void testCheckConflicts_WithConflicts() {
        // Arrange
        Meeting meeting = new Meeting();
        meeting.setStartTime(LocalDateTime.of(2024, 10, 21, 10, 15));
        meeting.setEndTime(LocalDateTime.of(2024, 10, 21, 10, 45));
        meeting.setParticipants(Arrays.asList("employee1@example.com", "employee2@example.com"));

        List<String> conflicts = List.of("employee1@example.com");

        when(meetingService.checkConflicts(meeting)).thenReturn(conflicts);

        // Act
        ResponseEntity<List<String>> response = meetingController.checkConflicts(meeting);

        // Assert
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals(conflicts, response.getBody());
        verify(meetingService, times(1)).checkConflicts(meeting);
    }
}
