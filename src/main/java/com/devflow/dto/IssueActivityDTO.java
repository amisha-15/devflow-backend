
package com.devflow.dto;

import java.time.LocalDateTime;

public class IssueActivityDTO {

    private Long id;
    private Long issueId;
    private String action;
    private String details;
    private LocalDateTime createdAt;

    public IssueActivityDTO(Long id, Long issueId, String action,
                            String details, LocalDateTime createdAt) {
        this.id = id;
        this.issueId = issueId;
        this.action = action;
        this.details = details;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public Long getIssueId() {
        return issueId;
    }

    public String getAction() {
        return action;
    }

    public String getDetails() {
        return details;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}

