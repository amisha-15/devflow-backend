
package com.devflow.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devflow.dto.AssignIssuesRequest;
import com.devflow.dto.CreateSprintRequest;
import com.devflow.dto.SprintDashboardDTO;
import com.devflow.dto.SprintProgressDTO;
import com.devflow.dto.UpdateSprintRequest;
import com.devflow.entity.Issue;
import com.devflow.entity.Sprint;
import com.devflow.service.SprintService;

@RestController
@RequestMapping("/api/sprints")
public class SprintController {

    private final SprintService sprintService;

    public SprintController(SprintService sprintService) {
        this.sprintService = sprintService;
    }
    
    @GetMapping
    public ResponseEntity<List<Sprint>> getAllSprints() {

        List<Sprint> sprints = sprintService.getAllSprints();

        return ResponseEntity.ok(sprints);
    }
    
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PostMapping
    public ResponseEntity<Sprint> createSprint(
            @RequestBody CreateSprintRequest request) {

        Sprint sprint = sprintService.createSprint(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(sprint);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Sprint> getSprintById(@PathVariable Long id) {

        Sprint sprint = sprintService.getSprintById(id);

        return ResponseEntity.ok(sprint);
    }
    
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PutMapping("/{id}")
    public ResponseEntity<Sprint> updateSprint(
        @PathVariable Long id,
        @RequestBody UpdateSprintRequest request) {

    Sprint updatedSprint = sprintService.updateSprint(id, request);

    return ResponseEntity.ok(updatedSprint);
}

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSprint(@PathVariable Long id) {

    boolean deleted = sprintService.deleteSprint(id);

    if (!deleted) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body("Cannot delete sprint because it has assigned issues");
    }

    return ResponseEntity.noContent().build();
}

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
@PutMapping("/{sprintId}/issues")
public ResponseEntity<List<Issue>> assignIssuesToSprint(
        @PathVariable Long sprintId,
        @RequestBody AssignIssuesRequest request) {

    List<Issue> assignedIssues =
            sprintService.assignIssuesToSprint(sprintId, request);

    return ResponseEntity.ok(assignedIssues);
}

@GetMapping("/{sprintId}/issues")
public ResponseEntity<List<Issue>> getSprintIssues(
        @PathVariable Long sprintId) {

    List<Issue> issues = sprintService.getSprintIssues(sprintId);

    return ResponseEntity.ok(issues);
}

@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
@PutMapping("/{sprintId}/start")
public ResponseEntity<Sprint> startSprint(
        @PathVariable Long sprintId) {

    Sprint sprint = sprintService.startSprint(sprintId);

    return ResponseEntity.ok(sprint);
}

@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
@PutMapping("/{sprintId}/complete")
public ResponseEntity<Sprint> completeSprint(
        @PathVariable Long sprintId) {

    Sprint sprint = sprintService.completeSprint(sprintId);

    return ResponseEntity.ok(sprint);
}

@GetMapping("/{sprintId}/progress")
public ResponseEntity<SprintProgressDTO> getSprintProgress(
        @PathVariable Long sprintId) {

    SprintProgressDTO progress =
            sprintService.getSprintProgress(sprintId);

    return ResponseEntity.ok(progress);
}

@GetMapping("/{sprintId}/dashboard")
public ResponseEntity<SprintDashboardDTO> getSprintDashboard(
        @PathVariable Long sprintId) {

    SprintDashboardDTO dashboard =
            sprintService.getSprintDashboard(sprintId);

    return ResponseEntity.ok(dashboard);
}
}

