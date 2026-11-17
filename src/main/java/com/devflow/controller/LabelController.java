package com.devflow.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.devflow.entity.Label;
import com.devflow.service.LabelService;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api/labels")
public class LabelController {

    private final LabelService labelService;

    public LabelController(LabelService labelService) {
        this.labelService = labelService;
    }

    // Create a label
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PostMapping
    public ResponseEntity<Label> createLabel(@RequestBody Label label) {
        Label createdLabel = labelService.createLabel(label.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdLabel);
    }

    // Get all labels
    @GetMapping
    public ResponseEntity<List<Label>> getAllLabels() {
        return ResponseEntity.ok(labelService.getAllLabels());
    }
}
