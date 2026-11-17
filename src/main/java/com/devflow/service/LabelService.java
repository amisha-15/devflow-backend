package com.devflow.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.devflow.entity.Label;
import com.devflow.repository.LabelRepository;

@Service
public class LabelService {

    private final LabelRepository labelRepository;

    public LabelService(LabelRepository labelRepository) {
        this.labelRepository = labelRepository;
    }

    // Create a new label
    public Label createLabel(String name) {

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Label name cannot be empty");
        }

        String trimmedName = name.trim();

        if (labelRepository.existsByNameIgnoreCase(trimmedName)) {
            throw new IllegalArgumentException("Label already exists");
        }

        Label label = new Label();
        label.setName(trimmedName);

        return labelRepository.save(label);
    }

    // Get all labels
    public List<Label> getAllLabels() {
        return labelRepository.findAll();
    }
}
