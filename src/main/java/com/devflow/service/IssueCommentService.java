package com.devflow.service;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import com.devflow.entity.Issue;
import com.devflow.entity.IssueComment;
import com.devflow.entity.User;
import com.devflow.repository.IssueCommentRepository;
import com.devflow.repository.IssueRepository;
import com.devflow.repository.UserRepository;

@Service
public class IssueCommentService {

    private final IssueCommentRepository commentRepository;
    private final IssueRepository issueRepository;
    private final UserRepository userRepository;

    public IssueCommentService(
            IssueCommentRepository commentRepository,
            IssueRepository issueRepository,
            UserRepository userRepository) {

        this.commentRepository = commentRepository;
        this.issueRepository = issueRepository;
        this.userRepository = userRepository;
    }

    // Add a comment to an issue
    public IssueComment addComment(Long issueId, Long userId, String content) {

        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(() -> new IllegalArgumentException("Issue not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        IssueComment comment = new IssueComment();
        comment.setIssue(issue);
        comment.setUser(user);
        comment.setContent(content);

        return commentRepository.save(comment);
    }

    // Get all comments for an issue
    public List<IssueComment> getCommentsByIssue(Long issueId) {

        return commentRepository.findByIssueIdOrderByCreatedAtDesc(issueId);
    }
    
    public Page<IssueComment> getCommentsByIssue(
            Long issueId, Pageable pageable) {

        return commentRepository
                .findByIssueIdOrderByCreatedAtDesc(issueId, pageable);
    }
    
    public IssueComment updateComment(
            Long commentId, Long issueId, Long userId, String content) {

        IssueComment comment = commentRepository
                .findByIdAndIssueIdAndUserId(commentId, issueId, userId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Comment not found or you are not the owner"));

        comment.setContent(content);

        return commentRepository.save(comment);
    }

    public void deleteComment(Long commentId, Long issueId, Long userId) {

        IssueComment comment = commentRepository
                .findByIdAndIssueIdAndUserId(commentId, issueId, userId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Comment not found or you are not the owner"));

        commentRepository.delete(comment);
    }

	

	
}
