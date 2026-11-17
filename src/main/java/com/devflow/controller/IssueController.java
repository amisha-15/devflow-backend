
package com.devflow.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.devflow.dto.AssignLabelsRequest;
import com.devflow.dto.DashboardOverviewDTO;
import com.devflow.dto.KanbanBoardDTO;
import com.devflow.dto.UpdateIssueStatusRequest;
import com.devflow.entity.Issue;
import com.devflow.entity.IssuePriority;
import com.devflow.entity.IssueStatus;
import com.devflow.service.IssueService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/issues")
public class IssueController {

    private final IssueService issueService;

    public IssueController(IssueService issueService) {
        this.issueService = issueService;
    }

    // Create a new issue
    @PostMapping
    public ResponseEntity<Issue> createIssue(@Valid @RequestBody Issue issue) {
        Issue savedIssue = issueService.createIssue(issue);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedIssue);
    }

    // Get all issues
    @GetMapping
    public ResponseEntity<List<Issue>> getAllIssues(
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {

        List<String> allowedFields = List.of(
                "id", "title", "status", "priority",
                "createdAt", "updatedAt", "dueDate"
        );

        if (!allowedFields.contains(sortBy)) {
            throw new IllegalArgumentException(
                    "Invalid sort field. Allowed fields: " + allowedFields);
        }

        if (!direction.equalsIgnoreCase("asc")
                && !direction.equalsIgnoreCase("desc")) {
            throw new IllegalArgumentException(
                    "Direction must be 'asc' or 'desc'");
        }

        return ResponseEntity.ok(
                issueService.getAllIssuesSorted(sortBy, direction));
    }

    // Get one issue by ID
    @GetMapping("/{id}")
    public ResponseEntity<Issue> getIssueById(@PathVariable Long id) {
        Issue issue = issueService.getIssueById(id);

        if (issue == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(issue);
    }

    // Update an existing issue
    @PutMapping("/{id}")
    public ResponseEntity<Issue> updateIssue(
            @PathVariable Long id,
           @Valid @RequestBody Issue updatedIssue) {

        Issue issue = issueService.updateIssue(id, updatedIssue);

        if (issue == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(issue);
    }

    // Delete an issue
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIssue(@PathVariable Long id) {
        boolean deleted = issueService.deleteIssue(id);

        if (!deleted) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
    
 // Get issues by status
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Issue>> getIssuesByStatus(
            @PathVariable IssueStatus status) {

        List<Issue> issues = issueService.getIssuesByStatus(status);

        return ResponseEntity.ok(issues);
    }
    
 // Get issues by priority
    @GetMapping("/priority/{priority}")
    public ResponseEntity<List<Issue>> getIssuesByPriority(
            @PathVariable IssuePriority priority) {

        List<Issue> issues = issueService.getIssuesByPriority(priority);

        return ResponseEntity.ok(issues);
    }
    
 // Get issues by status and priority
    @GetMapping("/filter")
    public ResponseEntity<List<Issue>> getIssuesByStatusAndPriority(
            @RequestParam IssueStatus status,
            @RequestParam IssuePriority priority) {

        List<Issue> issues =
                issueService.getIssuesByStatusAndPriority(status, priority);

        return ResponseEntity.ok(issues);
    }
    
    @GetMapping("/assignee/{assigneeId}")
    public ResponseEntity<List<Issue>> getIssuesByAssignee(
            @PathVariable Long assigneeId) {

        return ResponseEntity.ok(
                issueService.getIssuesByAssignee(assigneeId)
        );
    }
    
    @GetMapping("/dashboard/summary")
    public ResponseEntity<Map<String, Long>> getDashboardSummary() {
        return ResponseEntity.ok(issueService.getDashboardSummary());
    }
    
    @GetMapping("/paginated")
    public ResponseEntity<Page<Issue>> getAllIssuesPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        if (page < 0) {
            throw new IllegalArgumentException("Page number cannot be negative");
        }

        if (size < 1 || size > 100) {
            throw new IllegalArgumentException(
                    "Page size must be between 1 and 100");
        }

        Pageable pageable = PageRequest.of(page, size);

        return ResponseEntity.ok(
                issueService.getAllIssuesPaginated(pageable));
    }
    
    @GetMapping("/page")
    public ResponseEntity<Page<Issue>> getIssuesWithSortingAndPagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {

        if (page < 0) {
            throw new IllegalArgumentException(
                    "Page number cannot be negative");
        }

        if (size < 1 || size > 100) {
            throw new IllegalArgumentException(
                    "Page size must be between 1 and 100");
        }

        List<String> allowedFields = List.of(
                "id", "title", "status", "priority",
                "createdAt", "updatedAt", "dueDate"
        );

        if (!allowedFields.contains(sortBy)) {
            throw new IllegalArgumentException(
                    "Invalid sort field. Allowed fields: " + allowedFields);
        }

        if (!direction.equalsIgnoreCase("asc")
                && !direction.equalsIgnoreCase("desc")) {
            throw new IllegalArgumentException(
                    "Direction must be 'asc' or 'desc'");
        }

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return ResponseEntity.ok(
                issueService.getAllIssuesPaginated(pageable));
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<Issue>> searchIssuesByTitle(
            @RequestParam String title) {

        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException(
                    "Search title cannot be empty");
        }

        return ResponseEntity.ok(
                issueService.searchIssuesByTitle(title.trim()));
    }
    
    @GetMapping("/due-date")
    public ResponseEntity<List<Issue>> getIssuesByDueDateRange(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {

        return ResponseEntity.ok(
                issueService.getIssuesByDueDateRange(
                        startDate, endDate));
    }
    
    @GetMapping("/overdue")
    public ResponseEntity<List<Issue>> getOverdueIssues() {
        return ResponseEntity.ok(
                issueService.getOverdueIssues());
    }
    
    @GetMapping("/dashboard/priority-summary")
    public ResponseEntity<Map<String, Long>> getPrioritySummary() {
        return ResponseEntity.ok(
                issueService.getPrioritySummary());
    }
    
    @GetMapping("/dashboard/assignee-summary")
    public ResponseEntity<Map<String, Long>> getAssigneeSummary() {
        return ResponseEntity.ok(
                issueService.getAssigneeSummary());
    }
    
    @GetMapping("/dashboard/overview")
    public ResponseEntity<DashboardOverviewDTO> getDashboardOverview() {
        return ResponseEntity.ok(issueService.getDashboardOverview());
    }
    
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PutMapping("/{issueId}/labels")
    public ResponseEntity<Issue> assignLabels(
            @PathVariable Long issueId,
            @RequestBody AssignLabelsRequest request) {

        Issue updatedIssue = issueService.assignLabels(
                issueId, request.getLabelIds());

        return ResponseEntity.ok(updatedIssue);
    }
    
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @DeleteMapping("/{issueId}/labels/{labelId}")
    public ResponseEntity<Issue> removeLabel(
            @PathVariable Long issueId,
            @PathVariable Long labelId) {

        Issue updatedIssue = issueService.removeLabel(issueId, labelId);

        return ResponseEntity.ok(updatedIssue);
    }
    
    @GetMapping("/label/{labelId}")
    public ResponseEntity<List<Issue>> getIssuesByLabel(
            @PathVariable Long labelId) {

        return ResponseEntity.ok(
                issueService.getIssuesByLabel(labelId));
    }
    
    @GetMapping("/projects/{projectId}/kanban")
    public ResponseEntity<KanbanBoardDTO> getKanbanBoard(
            @PathVariable Long projectId) {

        KanbanBoardDTO board = issueService.getKanbanBoard(projectId);

        return ResponseEntity.ok(board);
    }
    
    @PatchMapping("/{issueId}/status")
    public ResponseEntity<Issue> updateIssueStatus(
            @PathVariable Long issueId,
            @RequestBody UpdateIssueStatusRequest request) {

        Issue updatedIssue = issueService.updateIssueStatus(
                issueId, request);

        return ResponseEntity.ok(updatedIssue);
    }
}

