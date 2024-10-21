package com.main.service;

import com.main.model.Meeting;
import com.main.repository.MeetingRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class MeetingService {
    private final MeetingRepository meetingRepository;

    public MeetingService(MeetingRepository meetingRepository) {
        this.meetingRepository = meetingRepository;
    }

    public void bookMeeting(Meeting meeting) {
        // Check for conflicts before booking the meeting
        List<String> conflictingParticipants = checkConflicts(meeting);
        if (conflictingParticipants.isEmpty()) {
            meetingRepository.save(meeting); // Save the meeting to the database
        } else {
            throw new RuntimeException("Conflict detected with participants: " + conflictingParticipants);
        }
    }

    public List<TimeSlot> findFreeSlots(List<Meeting> employee1Meetings, List<Meeting> employee2Meetings, int duration) {
        List<TimeSlot> freeSlots = new ArrayList<>();
        // Combine meetings of both employees
        List<Meeting> allMeetings = new ArrayList<>();
        allMeetings.addAll(employee1Meetings);
        allMeetings.addAll(employee2Meetings);

        // Sort meetings by start time
        allMeetings.sort((m1, m2) -> m1.getStartTime().compareTo(m2.getStartTime()));

        LocalDateTime currentTime = LocalDateTime.now();
        for (Meeting meeting : allMeetings) {
            if (currentTime.plusMinutes(duration).isBefore(meeting.getStartTime())) {
                freeSlots.add(new TimeSlot(currentTime, meeting.getStartTime()));
            }
            currentTime = meeting.getEndTime().isAfter(currentTime) ? meeting.getEndTime() : currentTime;
        }
        return freeSlots;
    }

    public List<String> checkConflicts(Meeting meeting) {
        List<Meeting> existingMeetings = meetingRepository.findAll(); // Fetch all meetings from the database
        List<String> conflictingParticipants = new ArrayList<>();

        for (Meeting existingMeeting : existingMeetings) {
            if (isConflict(meeting, existingMeeting)) {
                conflictingParticipants.addAll(existingMeeting.getParticipants());
            }
        }
        return conflictingParticipants;
    }

    private boolean isConflict(Meeting newMeeting, Meeting existingMeeting) {
        // Check if the time slots overlap
        return newMeeting.getParticipants().stream()
                .anyMatch(participant -> existingMeeting.getParticipants().contains(participant)) &&
                (newMeeting.getStartTime().isBefore(existingMeeting.getEndTime()) &&
                        newMeeting.getEndTime().isAfter(existingMeeting.getStartTime()));
    }

    // Inner class to represent time slots
    @Data
    @AllArgsConstructor
    public static class TimeSlot {
        private LocalDateTime start;
        private LocalDateTime end;
    }
}
