package com.devflow.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.devflow.entity.IssueAttachment;

public interface IssueAttachmentRepository
        extends JpaRepository<IssueAttachment, Long> {

    List<IssueAttachment> findByIssueId(Long issueId);
    
    Optional<IssueAttachment> findById(Long id);
}
