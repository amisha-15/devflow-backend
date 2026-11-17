package com.devflow.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.devflow.entity.Project;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    

}
