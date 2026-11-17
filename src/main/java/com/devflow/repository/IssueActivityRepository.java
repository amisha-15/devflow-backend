package com.devflow.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.devflow.entity.IssueActivity;

public interface IssueActivityRepository
        extends JpaRepository<IssueActivity, Long>{
	
	List<IssueActivity> findByIssueIdOrderByCreatedAtDesc(Long issueId);
	
	 org.springframework.data.domain.Page<IssueActivity>
	    findByIssueIdOrderByCreatedAtDesc(Long issueId, Pageable pageable);
	 
	 void deleteByIssueId(Long issueId);
}