package com.devflow.dto;

import java.time.LocalDateTime;

public class IssueAttachmentDTO {

    private Long id;
    private Long issueId;
    private String fileName;
    private String contentType;
    private Long fileSize;
    private LocalDateTime uploadedAt;

    public IssueAttachmentDTO(
            Long id,
            Long issueId,
            String fileName,
            String contentType,
            Long fileSize,
            LocalDateTime uploadedAt) {

        this.id = id;
        this.issueId = issueId;
        this.fileName = fileName;
        this.contentType = contentType;
        this.fileSize = fileSize;
        this.uploadedAt = uploadedAt;
    }

    public Long getId() {
        return id;
    }

    public Long getIssueId() {
        return issueId;
    }

    public String getFileName() {
        return fileName;
    }

    public String getContentType() {
        return contentType;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }
}
