
package com.devflow.service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import org.springframework.stereotype.Service;

import com.devflow.dto.DashboardOverviewDTO;
import com.devflow.dto.KanbanBoardDTO;
import com.devflow.dto.KanbanIssueDTO;
import com.devflow.dto.UpdateIssueStatusRequest;
import com.devflow.entity.Issue;
import com.devflow.entity.IssuePriority;
import com.devflow.entity.IssueStatus;
import com.devflow.entity.Label;
import com.devflow.entity.Project;
import com.devflow.entity.User;
import com.devflow.repository.IssueRepository;
import com.devflow.repository.ProjectRepository;
import com.devflow.repository.UserRepository;

import jakarta.transaction.Transactional;

import com.devflow.repository.LabelRepository;


@Service
public class IssueService {
	
	
    private final IssueRepository issueRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final IssueActivityService issueActivityService;
    private final LabelRepository labelRepository;

    
    public IssueService(
    		IssueRepository issueRepository,
    		ProjectRepository projectRepository,
    		UserRepository userRepository,
    		IssueActivityService issueActivityService,
    		LabelRepository labelRepository) {
    	
        this.issueRepository = issueRepository;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.issueActivityService = issueActivityService;
        this.labelRepository = labelRepository;
    }


public Issue createIssue(Issue issue) {

	// Validate and assign the project

	if (issue.getProject() == null || issue.getProject().getId() == null) {
	    throw new IllegalArgumentException("Project ID is required");
	}

	Long projectId = issue.getProject().getId();

	Project project = projectRepository.findById(projectId)
	        .orElseThrow(() ->
	                new org.springframework.web.server.ResponseStatusException(
	                        org.springframework.http.HttpStatus.NOT_FOUND,
	                        "Project not found"
	                ));

	issue.setProject(project);

    // Assign the user if an assignee ID is provided
    if (issue.getAssignee() != null
            && issue.getAssignee().getId() != null) {

        Long userId = issue.getAssignee().getId();

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new IllegalArgumentException("User not found"));

        issue.setAssignee(user);
    }

    Issue savedIssue = issueRepository.save(issue);

    issueActivityService.recordActivity(
            savedIssue.getId(),
            "CREATED",
            "Issue created: " + savedIssue.getTitle()
    );

    return savedIssue;
}

public List<Issue> getAllIssues() {
    return issueRepository.findAll();
}

public Page<Issue> getAllIssuesPaginated(Pageable pageable) {
    return issueRepository.findAll(pageable);
}
public List<Issue> getAllIssuesSorted(String sortBy, String direction) {

    Sort sort = direction.equalsIgnoreCase("desc")
            ? Sort.by(sortBy).descending()
            : Sort.by(sortBy).ascending();

    return issueRepository.findAll(sort);
}


    public Issue getIssueById(Long id) {
        return issueRepository.findById(id).orElse(null);
    }


public Issue updateIssue(Long id, Issue updatedIssue) {

    return issueRepository.findById(id)
            .map(existingIssue -> {
            	
            	IssueStatus oldStatus = existingIssue.getStatus();
            	IssuePriority oldPriority = existingIssue.getPriority();
            	String oldTitle = existingIssue.getTitle();
            	LocalDate oldDueDate = existingIssue.getDueDate();
                existingIssue.setTitle(updatedIssue.getTitle());
                existingIssue.setDescription(updatedIssue.getDescription());
                existingIssue.setStatus(updatedIssue.getStatus());
                existingIssue.setPriority(updatedIssue.getPriority());
                existingIssue.setDueDate(updatedIssue.getDueDate());
                Long oldAssigneeId = existingIssue.getAssignee() == null
                        ? null
                        : existingIssue.getAssignee().getId();

                // Update project if a project ID is provided
                if (updatedIssue.getProject() != null
                        && updatedIssue.getProject().getId() != null) {

                    Long projectId = updatedIssue.getProject().getId();

                    Project project = projectRepository.findById(projectId)
                            .orElseThrow(() ->
                                    new IllegalArgumentException("Project not found"));

                    existingIssue.setProject(project);
                }

                // Update assignee if a user ID is provided
                if (updatedIssue.getAssignee() != null
                        && updatedIssue.getAssignee().getId() != null) {

                    Long userId = updatedIssue.getAssignee().getId();

                    User user = userRepository.findById(userId)
                            .orElseThrow(() ->
                                    new IllegalArgumentException("User not found"));

                    existingIssue.setAssignee(user);
                }

                Issue savedIssue = issueRepository.save(existingIssue);
                
                if (oldStatus != savedIssue.getStatus()) {
                    issueActivityService.recordActivity(
                            savedIssue.getId(),
                            "STATUS_CHANGED",
                            "Status changed from " + oldStatus
                                    + " to " + savedIssue.getStatus()
                    );
                }

                if (oldPriority != savedIssue.getPriority()) {
                    issueActivityService.recordActivity(
                            savedIssue.getId(),
                            "PRIORITY_CHANGED",
                            "Priority changed from " + oldPriority
                                    + " to " + savedIssue.getPriority()
                    );
                }
                
                if (!oldTitle.equals(savedIssue.getTitle())) {
                    issueActivityService.recordActivity(
                            savedIssue.getId(),
                            "TITLE_CHANGED",
                            "Title changed from " + oldTitle
                                    + " to " + savedIssue.getTitle()
                    );
                }

                if (!java.util.Objects.equals(oldDueDate, savedIssue.getDueDate())) {
                    issueActivityService.recordActivity(
                            savedIssue.getId(),
                            "DUE_DATE_CHANGED",
                            "Due date changed from " + oldDueDate
                                    + " to " + savedIssue.getDueDate()
                    );
                }
                
                Long newAssigneeId = savedIssue.getAssignee() == null
                        ? null
                        : savedIssue.getAssignee().getId();

                if (!java.util.Objects.equals(oldAssigneeId, newAssigneeId)) {
                    issueActivityService.recordActivity(
                            savedIssue.getId(),
                            "ASSIGNEE_CHANGED",
                            "Assignee changed from user ID " + oldAssigneeId
                                    + " to user ID " + newAssigneeId
                    );
                }
                return savedIssue;
            })
            .orElse(null);
}


@Transactional
public boolean deleteIssue(Long id) {

    if (!issueRepository.existsById(id)) {
        return false;
    }

    // Delete activity history linked to this issue first
    issueActivityService.deleteActivitiesByIssueId(id);

    // Delete the issue
    issueRepository.deleteById(id);

    return true;
}
    
 // Get issues by status
    public List<Issue> getIssuesByStatus(IssueStatus status) {
        return issueRepository.findByStatus(status);
    }
    
 // Get issues by priority
    public List<Issue> getIssuesByPriority(IssuePriority priority) {
        return issueRepository.findByPriority(priority);
    }
    
 // Get issues by status and priority
    public List<Issue> getIssuesByStatusAndPriority(
            IssueStatus status,
            IssuePriority priority) {

        return issueRepository.findByStatusAndPriority(status, priority);
    }
    
    public List<Issue> getIssuesByAssignee(Long assigneeId) {
	    return issueRepository.findByAssigneeId(assigneeId);
	}
    
    public Map<String, Long> getDashboardSummary() {

        return Map.of(
                "totalIssues", issueRepository.count(),
                "openIssues", issueRepository.countByStatus(IssueStatus.OPEN),
                "inProgressIssues", issueRepository.countByStatus(IssueStatus.IN_PROGRESS),
                "resolvedIssues", issueRepository.countByStatus(IssueStatus.RESOLVED),
                "closedIssues", issueRepository.countByStatus(IssueStatus.CLOSED)
        ); 
    }
    
    public List<Issue> searchIssuesByTitle(String title) {
        return issueRepository.findByTitleContainingIgnoreCase(title);
    }
    
    public List<Issue> getIssuesByDueDateRange(
            LocalDate startDate, LocalDate endDate) {

        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException(
                    "Start date cannot be after end date");
        }

        return issueRepository.findByDueDateBetween(
                startDate, endDate);
    }
    
    public List<Issue> getOverdueIssues() {

        LocalDate today = LocalDate.now();

        List<IssueStatus> excludedStatuses = List.of(
                IssueStatus.RESOLVED,
                IssueStatus.CLOSED
        );

        return issueRepository.findByDueDateBeforeAndStatusNotIn(
                today, excludedStatuses);
    }
    
    public Map<String, Long> getPrioritySummary() {

        Map<String, Long> summary = new HashMap<>();

        summary.put("lowIssues",
                issueRepository.countByPriority(IssuePriority.LOW));

        summary.put("mediumIssues",
                issueRepository.countByPriority(IssuePriority.MEDIUM));

        summary.put("highIssues",
                issueRepository.countByPriority(IssuePriority.HIGH));

        summary.put("criticalIssues",
                issueRepository.countByPriority(IssuePriority.CRITICAL));

        return summary;
    }
    
    
    public Map<String, Long> getAssigneeSummary() {

        Map<String, Long> summary = new HashMap<>();

        List<Issue> issues = issueRepository.findAll();

        for (Issue issue : issues) {

            String assigneeName;

            if (issue.getAssignee() == null) {
                assigneeName = "Unassigned";
            } else {
                assigneeName = issue.getAssignee().getName();
            }

            summary.put(
                    assigneeName,
                    summary.getOrDefault(assigneeName, 0L) + 1
            );
        }

        return summary;
    }
    
    public DashboardOverviewDTO getDashboardOverview() {

        return new DashboardOverviewDTO(
                getDashboardSummary(),
                getPrioritySummary(),
                getAssigneeSummary()
        );
    }
    
    public Issue assignLabels(Long issueId, List<Long> labelIds) {

        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Issue not found"));

        List<Label> labels = labelRepository.findAllById(labelIds);

        if (labels.size() != labelIds.size()) {
            throw new IllegalArgumentException("One or more labels not found");
        }

        issue.setLabels(labels);

        return issueRepository.save(issue);
        
        
    }
    
    public Issue removeLabel(Long issueId, Long labelId) {

        // Find the issue
        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Issue not found"));

        // Find the label
        Label label = labelRepository.findById(labelId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Label not found"));

        // Remove only the association from this issue
        boolean removed = issue.getLabels().removeIf(
                existingLabel -> existingLabel.getId().equals(label.getId())
        );

        // Check whether the label was actually assigned
        if (!removed) {
            throw new IllegalArgumentException("Label is not assigned to this issue");
        }

        // Save the updated issue
        return issueRepository.save(issue);
    }
    public List<Issue> getIssuesByLabel(Long labelId) {
        return issueRepository.findDistinctByLabelsId(labelId);
    }
    
    public Issue updateIssueStatus(
            Long issueId,
            UpdateIssueStatusRequest request) {

        // 1. Find the existing issue
        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Issue not found"));

        // 2. Store the old status
        IssueStatus oldStatus = issue.getStatus();

        // 3. Get the new status from the request
        IssueStatus newStatus = request.getStatus();

        // 4. Update only if the status has changed
        if (oldStatus != newStatus) {

            issue.setStatus(newStatus);

            // 5. Save the updated issue
            Issue savedIssue = issueRepository.save(issue);

            // 6. Record the status change
            issueActivityService.recordActivity(
                    savedIssue.getId(),
                    "STATUS_CHANGED",
                    "Status changed from " + oldStatus
                            + " to " + newStatus
            );

            return savedIssue;
        }

        // No change: return the existing issue
        return issue;
    }
    
    
    public KanbanBoardDTO getKanbanBoard(Long projectId) {

        // 1. Check whether the project exists
        projectRepository.findById(projectId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Project not found"));

        // 2. Create all four Kanban columns
        Map<String, List<KanbanIssueDTO>> columns = new HashMap<>();

        for (IssueStatus status : IssueStatus.values()) {
            columns.put(status.name(), new java.util.ArrayList<>());
        }

        // 3. Fetch issues belonging to this project
        List<Issue> issues = issueRepository.findByProject_Id(projectId);

        // 4. Convert each issue into a DTO and group by status
        for (Issue issue : issues) {

            String status = issue.getStatus().name();

            String assigneeName = issue.getAssignee() == null
                    ? "Unassigned"
                    : issue.getAssignee().getName();

            KanbanIssueDTO dto = new KanbanIssueDTO(
                    issue.getId(),
                    issue.getTitle(),
                    issue.getPriority().name(),
                    assigneeName
            );

            columns.get(status).add(dto);
        }

        // 5. Return the completed Kanban board
        return new KanbanBoardDTO(columns);
    }
    
    
}
