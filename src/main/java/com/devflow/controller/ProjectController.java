package com.devflow.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devflow.entity.Project;
import com.devflow.service.ProjectService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {
	
	private final ProjectService projectService;
	
	public ProjectController(ProjectService projectService) {
		this.projectService = projectService;
	}
	
	@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
	@PostMapping
	public ResponseEntity<Project> createProject(@Valid @RequestBody Project project){
		Project savedProject = projectService.createProject(project);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(savedProject);
	}
	
	@GetMapping
	public ResponseEntity<List<Project>> getAllProjects(){
		return ResponseEntity.ok(projectService.getAllProjects());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Project> getProjectById(@PathVariable Long id){
		Project project = projectService.getProjectById(id);
		
		if(project == null) {
			return ResponseEntity.notFound().build();
			
		}
		
		return ResponseEntity.ok(project);
	}
	
	@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
	@PutMapping("/{id}")
	public ResponseEntity<Project> updateProject(
			@PathVariable Long id,
			@Valid @RequestBody Project updatedProject){
		Project project = projectService.updateProject(id, updatedProject);
		
		if(project == null) {
			return ResponseEntity.notFound().build();
		}
		
		return ResponseEntity.ok(project);
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteProject(@PathVariable Long id) {

	    // First check whether the project exists
	    if (projectService.getProjectById(id) == null) {
	        return ResponseEntity.notFound().build();
	    }

	    // Try to delete the project
	    boolean deleted = projectService.deleteProject(id);

	    if (!deleted) {
	        return ResponseEntity.status(HttpStatus.CONFLICT)
	                .body("Cannot delete project because issues are linked to it.");
	    }

	    return ResponseEntity.noContent().build();
	}
	
}
