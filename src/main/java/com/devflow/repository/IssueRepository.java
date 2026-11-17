package com.devflow.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.devflow.entity.Issue;
import com.devflow.entity.IssuePriority;
import com.devflow.entity.IssueStatus;

public interface IssueRepository extends JpaRepository<Issue, Long> {
	List<Issue> findByStatus(IssueStatus status);
	
	// Find issues by priority
	List<Issue> findByPriority(IssuePriority priority);
	
	// Find issues by both status and priority
	List<Issue> findByStatusAndPriority(
	        IssueStatus status,
	        IssuePriority priority);
	
	List<Issue> findByAssigneeId(Long assigneeId);
	
	long countByStatus(IssueStatus status);
	
	List<Issue> findByTitleContainingIgnoreCase(String title);
	
	List<Issue> findByDueDateBetween(
	        LocalDate startDate,
	        LocalDate endDate);
	
	List<Issue> findByDueDateBeforeAndStatusNotIn(
	        LocalDate today,
	        List<IssueStatus> statuses);
	
	long countByPriority(IssuePriority priority);
	
	long countByAssigneeId(Long assigneeId);
	
	List<Issue> findDistinctByLabelsId(Long labelId);
	
	// Check whether a project has any linked issues
	boolean existsByProject_Id(Long projectId);
	
	boolean existsBySprint_Id(Long sprintId);
	
	List<Issue> findBySprint_Id(Long sprintId);
	
	// Find all issues belonging to a project
	List<Issue> findByProject_Id(Long projectId);

}
