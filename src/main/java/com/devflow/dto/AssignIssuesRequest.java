
package com.devflow.dto;

import java.util.List;

public class AssignIssuesRequest {

    private List<Long> issueIds;

    public List<Long> getIssueIds() {
        return issueIds;
    }

    public void setIssueIds(List<Long> issueIds) {
        this.issueIds = issueIds;
    }
}
