package com.devflow.dto;

import java.util.List;

public class AssignLabelsRequest {

    private List<Long> labelIds;

    public List<Long> getLabelIds() {
        return labelIds;
    }

    public void setLabelIds(List<Long> labelIds) {
        this.labelIds = labelIds;
    }
}
