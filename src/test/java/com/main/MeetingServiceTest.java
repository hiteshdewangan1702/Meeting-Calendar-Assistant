package com.main;

import com.main.model.Meeting;
import com.main.service.MeetingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MeetingServiceTest {
    private MeetingService meetingService;
    private MeetingRepository meetingRepository;

    @BeforeEach
    void setUp() {
        meetingRepository = Mockito.mock(MeetingRepository.class); // Create a mock repository
        meetingService = new MeetingService(meetingRepository); // Inject the mock repository
    }

    @Test
    void testBookMeeting() {
        // Arrange
        Meeting meeting = new Meeting();
        meeting.setStartTime(LocalDateTime.of(2024, 10, 21, 10, 0));
        meeting.setEndTime(LocalDateTime.of(2024, 10, 21, 10, 30));
        meeting.setParticipants(List.of("employee1@example.com", "employee2@example.com"));

        // Act
        meetingService.bookMeeting(meeting);

        // Assert
        // Verify that the meeting was saved in the repository
        verify(meetingRepository, times(1)).addMeeting(meeting); // Adjust according to your method names
    }

    @Test
    void testFindFreeSlots() {
        // Arrange
        List<Meeting> employee1Meetings = new ArrayList<>();
        List<Meeting> employee2Meetings = new ArrayList<>();

        // Sample meetings for employee 1
        Meeting meeting1 = new Meeting();
        meeting1.setStartTime(LocalDateTime.of(2024, 10, 21, 9, 0));
        meeting1.setEndTime(LocalDateTime.of(2024, 10, 21, 10, 0));
        employee1Meetings.add(meeting1);

        // Sample meetings for employee 2
        Meeting meeting2 = new Meeting();
        meeting2.setStartTime(LocalDateTime.of(2024, 10, 21, 10, 30));
        meeting2.setEndTime(LocalDateTime.of(2024, 10, 21, 11, 30));
        employee2Meetings.add(meeting2);

        int duration = 30; // Duration in minutes

        // Act
        List<MeetingService.TimeSlot> freeSlots = meetingService.findFreeSlots(employee1Meetings, employee2Meetings, duration);

        // Assert
        assertNotNull(freeSlots);
        assertFalse(freeSlots.isEmpty()); // Expecting at least one free slot
    }

    @Test
    void testCheckConflicts() {
        // Arrange
        Meeting meeting = new Meeting();
        meeting.setStartTime(LocalDateTime.of(2024, 10, 21, 10, 15));
        meeting.setEndTime(LocalDateTime.of(2024, 10, 21, 10, 45));
        meeting.setParticipants(List.of("employee1@example.com", "employee2@example.com"));

        // Sample existing meetings
        List<Meeting> existingMeetings = new ArrayList<>();

        Meeting existingMeeting = new Meeting();
        existingMeeting.setStartTime(LocalDateTime.of(2024, 10, 21, 10, 0));
        existingMeeting.setEndTime(LocalDateTime.of(2024, 10, 21, 10, 30));
        existingMeeting.setParticipants(List.of("employee1@example.com"));
        existingMeetings.add(existingMeeting);

        // Mock the repository to return existing meetings
        when(meetingRepository.getMeetings()).thenReturn(existingMeetings);

        // Act
        List<String> conflicts = meetingService.checkConflicts(meeting);

        // Assert
        assertNotNull(conflicts);
        assertFalse(conflicts.isEmpty()); // Expecting a conflict due to the overlapping meeting
    }
}
