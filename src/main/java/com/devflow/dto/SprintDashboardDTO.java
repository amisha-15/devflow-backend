package com.devflow.dto;

import java.time.LocalDate;
import java.util.Map;

public class SprintDashboardDTO {

    private Long sprintId;
    private String name;
    private String goal;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;

    private int totalIssues;
    private int completedIssues;
    private int pendingIssues;
    private double completionPercentage;

    private Map<String, Integer> issueStatusCounts;

    public SprintDashboardDTO(Long sprintId, String name, String goal,
            LocalDate startDate, LocalDate endDate, String status,
            int totalIssues, int completedIssues, int pendingIssues,
            double completionPercentage,
            Map<String, Integer> issueStatusCounts) {

        this.sprintId = sprintId;
        this.name = name;
        this.goal = goal;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.totalIssues = totalIssues;
        this.completedIssues = completedIssues;
        this.pendingIssues = pendingIssues;
        this.completionPercentage = completionPercentage;
        this.issueStatusCounts = issueStatusCounts;
    }

    public Long getSprintId() { return sprintId; }
    public String getName() { return name; }
    public String getGoal() { return goal; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }
    public String getStatus() { return status; }
    public int getTotalIssues() { return totalIssues; }
    public int getCompletedIssues() { return completedIssues; }
    public int getPendingIssues() { return pendingIssues; }
    public double getCompletionPercentage() { return completionPercentage; }
    public Map<String, Integer> getIssueStatusCounts() {
        return issueStatusCounts;
    }
}