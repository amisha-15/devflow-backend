package com.devflow.dto;

import java.util.List;
import java.util.Map;

public class KanbanBoardDTO {

    private Map<String, List<KanbanIssueDTO>> columns;

    public KanbanBoardDTO(Map<String, List<KanbanIssueDTO>> columns) {
        this.columns = columns;
    }

    public Map<String, List<KanbanIssueDTO>> getColumns() {
        return columns;
    }
}
