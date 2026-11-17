package com.devflow.dto;

import com.devflow.entity.IssueStatus;

public class UpdateIssueStatusRequest {

    private IssueStatus status;

    public IssueStatus getStatus() {
        return status;
    }

    public void setStatus(IssueStatus status) {
        this.status = status;
    }
}
