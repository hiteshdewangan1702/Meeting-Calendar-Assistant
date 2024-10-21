package com.main.controller;

import com.main.model.FreeSlotsRequest;
import com.main.model.Meeting;
import com.main.service.MeetingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/meetings")
public class MeetingController {
    private final MeetingService meetingService;

    public MeetingController(MeetingService meetingService) {
        this.meetingService = meetingService;
    }

    @PostMapping("/book")
    public ResponseEntity<Void> bookMeeting(@RequestBody Meeting meeting) {
        meetingService.bookMeeting(meeting);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @PostMapping("/free-slots")
    public ResponseEntity<List<MeetingService.TimeSlot>> findFreeSlots(
            @RequestBody FreeSlotsRequest request) {
        List<MeetingService.TimeSlot> freeSlots = meetingService.findFreeSlots(request.getEmployee1Meetings(), request.getEmployee2Meetings(), request.getDuration());
        return ResponseEntity.ok(freeSlots);
    }


    @PostMapping("/check-conflicts")
    public ResponseEntity<List<String>> checkConflicts(@RequestBody Meeting meeting) {
        List<String> conflicts = meetingService.checkConflicts(meeting);

        if (conflicts.isEmpty()) {
            // No conflicts found
            return ResponseEntity.ok(conflicts); // 200 OK with an empty list
        } else {
            // Conflicts found
            return ResponseEntity.status(HttpStatus.CONFLICT).body(conflicts); // 409 Conflict with the list of conflicts
        }
    }

}
