package com.devflow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.devflow.entity.Label;

import java.util.Optional;

public interface LabelRepository extends JpaRepository<Label, Long> {

    Optional<Label> findByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCase(String name);
}