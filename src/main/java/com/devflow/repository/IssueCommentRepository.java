package com.devflow.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.devflow.entity.IssueComment;

public interface IssueCommentRepository extends JpaRepository<IssueComment, Long> {
	List<IssueComment> findByIssueIdOrderByCreatedAtDesc(Long issueId);
	
	Page<IssueComment> findByIssueIdOrderByCreatedAtDesc(
	        Long issueId, Pageable pageable);
	
	Optional<IssueComment> findByIdAndUserId(Long commentId, Long userId);
	
	Optional<IssueComment> findByIdAndIssueIdAndUserId(
	        Long commentId, Long issueId, Long userId);

}
