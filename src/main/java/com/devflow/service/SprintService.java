
package com.devflow.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.devflow.dto.AssignIssuesRequest;
import com.devflow.dto.CreateSprintRequest;
import com.devflow.dto.SprintDashboardDTO;
import com.devflow.dto.SprintProgressDTO;
import com.devflow.dto.UpdateSprintRequest;
import com.devflow.entity.Issue;
import com.devflow.entity.IssueStatus;
import com.devflow.entity.Project;
import com.devflow.entity.Sprint;
import com.devflow.repository.IssueRepository;
import com.devflow.repository.ProjectRepository;
import com.devflow.repository.SprintRepository;

@Service
public class SprintService {

    private final SprintRepository sprintRepository;
    private final ProjectRepository projectRepository;
    private final IssueRepository issueRepository;

    public SprintService(SprintRepository sprintRepository,
                         ProjectRepository projectRepository,
                         IssueRepository issueRepository) {
        this.sprintRepository = sprintRepository;
        this.projectRepository = projectRepository;
        this.issueRepository = issueRepository;
    }

    public Sprint createSprint(CreateSprintRequest request) {

        // Check whether the project exists
        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() ->
                        new RuntimeException("Project not found"));

        // Validate sprint dates
        if (request.getStartDate() == null ||
            request.getEndDate() == null ||
            request.getEndDate().isBefore(request.getStartDate())) {

            throw new IllegalArgumentException(
                    "Invalid sprint dates");
        }

        // Create a new Sprint object
        Sprint sprint = new Sprint();

        sprint.setName(request.getName());
        sprint.setGoal(request.getGoal());
        sprint.setStartDate(request.getStartDate());
        sprint.setEndDate(request.getEndDate());
        sprint.setProject(project);

        // Save sprint in the database
        return sprintRepository.save(sprint);
    }
    
    public List<Sprint> getAllSprints() {
        return sprintRepository.findAll();
    }
    
    public Sprint getSprintById(Long id) {

        return sprintRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Sprint not found with ID: " + id
                        ));
    }
    

public Sprint updateSprint(Long id, UpdateSprintRequest request) {

    // Find the existing sprint
    Sprint sprint = sprintRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Sprint not found with ID: " + id
            ));

    // Validate dates
    if (request.getStartDate() == null ||
        request.getEndDate() == null ||
        request.getEndDate().isBefore(request.getStartDate())) {

        throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "End date must be on or after start date"
        );
    }

    // Update sprint details
    sprint.setName(request.getName());
    sprint.setGoal(request.getGoal());
    sprint.setStartDate(request.getStartDate());
    sprint.setEndDate(request.getEndDate());

    // Save updated sprint
    return sprintRepository.save(sprint);
}


public boolean deleteSprint(Long id) {

    // Check whether the sprint exists
    Sprint sprint = sprintRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Sprint not found with ID: " + id
            ));

    // Check whether issues are assigned to this sprint
    if (issueRepository.existsBySprint_Id(id)) {
        return false;
    }

    // Delete the sprint
    sprintRepository.delete(sprint);

    return true;
}


public List<Issue> assignIssuesToSprint(
        Long sprintId, AssignIssuesRequest request) {

    // Find the sprint
    Sprint sprint = sprintRepository.findById(sprintId)
            .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Sprint not found with ID: " + sprintId
            ));

    // Prepare a list for the assigned issues
    List<Issue> assignedIssues = new ArrayList<>();

    // Process each issue ID
    for (Long issueId : request.getIssueIds()) {

        // Find the issue
        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Issue not found with ID: " + issueId
                ));

        // Check whether issue and sprint belong to same project
        if (!issue.getProject().getId()
                .equals(sprint.getProject().getId())) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Issue " + issueId
                    + " does not belong to this sprint's project"
            );
        }

        // Assign issue to sprint
        issue.setSprint(sprint);

        assignedIssues.add(issue);
    }

    // Save all updated issues
    return issueRepository.saveAll(assignedIssues);
}

public List<Issue> getSprintIssues(Long sprintId) {

    // Check whether the sprint exists
    getSprintById(sprintId);

    // Fetch all issues assigned to this sprint
    return issueRepository.findBySprint_Id(sprintId);
}

public Sprint startSprint(Long sprintId) {

    // Find the sprint
    Sprint sprint = getSprintById(sprintId);

    // Check whether sprint is PLANNED
    if (!sprint.getStatus().equals("PLANNED")) {
        throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Only a PLANNED sprint can be started"
        );
    }

    // Change status to ACTIVE
    sprint.setStatus("ACTIVE");

    // Save updated sprint
    return sprintRepository.save(sprint);
}

public Sprint completeSprint(Long sprintId) {

    // Find the sprint
    Sprint sprint = getSprintById(sprintId);

    // Only ACTIVE sprints can be completed
    if (!sprint.getStatus().equals("ACTIVE")) {
        throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Only an ACTIVE sprint can be completed"
        );
    }

    // Change status to COMPLETED
    sprint.setStatus("COMPLETED");

    // Save updated sprint
    return sprintRepository.save(sprint);
}

public SprintProgressDTO getSprintProgress(Long sprintId) {

    // Check whether sprint exists
    Sprint sprint = getSprintById(sprintId);

    // Get all issues assigned to this sprint
    List<Issue> issues = issueRepository.findBySprint_Id(sprintId);

    // Count total issues
    int totalIssues = issues.size();

    // Count completed issues
    int completedIssues = (int) issues.stream()
            .filter(issue ->
                issue.getStatus() == IssueStatus.RESOLVED ||
                issue.getStatus() == IssueStatus.CLOSED)
            .count();

    // Calculate pending issues
    int pendingIssues = totalIssues - completedIssues;

    // Calculate completion percentage
    double completionPercentage = totalIssues == 0
            ? 0.0
            : (completedIssues * 100.0) / totalIssues;

    // Return progress details
    return new SprintProgressDTO(
            sprint.getId(),
            totalIssues,
            completedIssues,
            pendingIssues,
            Math.round(completionPercentage * 100.0) / 100.0
    );
}

public SprintDashboardDTO getSprintDashboard(Long sprintId) {

    // Get sprint details
    Sprint sprint = getSprintById(sprintId);

    // Get all issues assigned to this sprint
    List<Issue> issues = issueRepository.findBySprint_Id(sprintId);

    // Reuse existing progress calculation
    SprintProgressDTO progress = getSprintProgress(sprintId);

    // Count issues grouped by status
    Map<String, Integer> statusCounts = new HashMap<>();

    for (IssueStatus status : IssueStatus.values()) {

        int count = (int) issues.stream()
                .filter(issue -> issue.getStatus() == status)
                .count();

        statusCounts.put(status.name(), count);
    }

    // Create dashboard response
    return new SprintDashboardDTO(
            sprint.getId(),
            sprint.getName(),
            sprint.getGoal(),
            sprint.getStartDate(),
            sprint.getEndDate(),
            sprint.getStatus(),
            progress.getTotalIssues(),
            progress.getCompletedIssues(),
            progress.getPendingIssues(),
            progress.getCompletionPercentage(),
            statusCounts
    );
}

}

