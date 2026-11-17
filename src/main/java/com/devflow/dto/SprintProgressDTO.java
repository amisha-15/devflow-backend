package com.devflow.dto;

public class SprintProgressDTO {

    private Long sprintId;
    private int totalIssues;
    private int completedIssues;
    private int pendingIssues;
    private double completionPercentage;

    public SprintProgressDTO(Long sprintId, int totalIssues,
            int completedIssues, int pendingIssues,
            double completionPercentage) {

        this.sprintId = sprintId;
        this.totalIssues = totalIssues;
        this.completedIssues = completedIssues;
        this.pendingIssues = pendingIssues;
        this.completionPercentage = completionPercentage;
    }

    public Long getSprintId() {
        return sprintId;
    }

    public int getTotalIssues() {
        return totalIssues;
    }

    public int getCompletedIssues() {
        return completedIssues;
    }

    public int getPendingIssues() {
        return pendingIssues;
    }

    public double getCompletionPercentage() {
        return completionPercentage;
    }
}
