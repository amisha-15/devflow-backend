
package com.devflow.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.devflow.dto.IssueActivityDTO;
import com.devflow.entity.IssueActivity;
import com.devflow.service.IssueActivityService;

@RestController
@RequestMapping("/api/issues")
public class IssueActivityController {

    private final IssueActivityService activityService;

    public IssueActivityController(IssueActivityService activityService) {
        this.activityService = activityService;
    }

    @GetMapping("/{issueId}/activities")
    public ResponseEntity<Page<IssueActivityDTO>> getActivities(
            @PathVariable Long issueId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

    	if (page < 0) {
    	    throw new IllegalArgumentException(
    	            "Page number cannot be negative");
    	}

    	if (size < 1 || size > 100) {
    	    throw new IllegalArgumentException(
    	            "Page size must be between 1 and 100");
    	}

    	Pageable pageable = PageRequest.of(page, size);
        Page<IssueActivity> activities =
                activityService.getActivities(issueId, pageable);

        Page<IssueActivityDTO> response = activities.map(activity ->
                new IssueActivityDTO(
                        activity.getId(),
                        activity.getIssue().getId(),
                        activity.getAction(),
                        activity.getDetails(),
                        activity.getCreatedAt()));

        return ResponseEntity.ok(response);
    }
}
