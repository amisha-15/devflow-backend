package com.devflow.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.devflow.entity.Project;
import com.devflow.repository.IssueRepository;
import com.devflow.repository.ProjectRepository;

@Service
public class ProjectService {
	
	private final ProjectRepository projectRepository;
	private final IssueRepository issueRepository;

	public ProjectService(ProjectRepository projectRepository,
	                      IssueRepository issueRepository) {
	    this.projectRepository = projectRepository;
	    this.issueRepository = issueRepository;
	}
	
	public Project createProject(Project project) {
		return projectRepository.save(project);
	
	}
	
	public List<Project> getAllProjects(){
		return projectRepository.findAll();
	}
	
	public Project getProjectById(Long id) {
		return projectRepository.findById(id).orElse(null);
	}
	
	public Project updateProject(Long id,Project updatedProject) {
		return projectRepository.findById(id)
				.map(existingProject -> {
					existingProject.setName(updatedProject.getName());
					existingProject.setDescription(updatedProject.getDescription());
					
					return projectRepository.save(existingProject);
				})
				.orElse(null); 
	}
	
	public boolean deleteProject(Long id) {

	    if (!projectRepository.existsById(id)) {
	        return false;
	    }

	    // Prevent deletion if the project has linked issues
	    if (issueRepository.existsByProject_Id(id)) {
	        return false;
	    }

	    projectRepository.deleteById(id);
	    return true;
	}

}
