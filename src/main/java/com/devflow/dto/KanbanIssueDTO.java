package com.devflow.dto;

public class KanbanIssueDTO {

    private Long id;
    private String title;
    private String priority;
    private String assigneeName;

    // Constructor
    public KanbanIssueDTO(Long id, String title,
                          String priority, String assigneeName) {
        this.id = id;
        this.title = title;
        this.priority = priority;
        this.assigneeName = assigneeName;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getPriority() {
        return priority;
    }

    public String getAssigneeName() {
        return assigneeName;
    }
}
