package com.devflow.dto;

import java.time.LocalDateTime;

public class IssueCommentDTO {

    private Long id;
    private Long issueId;
    private Long userId;
    private String userName;
    private String content;
    private LocalDateTime createdAt;

    public IssueCommentDTO(Long id, Long issueId, Long userId,
                           String userName, String content,
                           LocalDateTime createdAt) {

        this.id = id;
        this.issueId = issueId;
        this.userId = userId;
        this.userName = userName;
        this.content = content;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public Long getIssueId() {
        return issueId;
    }

    public Long getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}