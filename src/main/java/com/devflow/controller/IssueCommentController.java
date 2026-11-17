package com.devflow.controller;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.devflow.dto.AddCommentRequest;
import com.devflow.dto.IssueCommentDTO;
import com.devflow.dto.UpdateCommentRequest;
import com.devflow.entity.IssueComment;
import com.devflow.service.IssueCommentService;

import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.devflow.entity.User;

@RestController
@RequestMapping("/api/issues/{issueId}/comments")
public class IssueCommentController {

    private final IssueCommentService commentService;

    public IssueCommentController(IssueCommentService commentService) {
        this.commentService = commentService;
    }

    // Add a comment 

@PostMapping
public ResponseEntity<IssueCommentDTO> addComment(
        @PathVariable Long issueId,
        @Valid @RequestBody AddCommentRequest request) {

    Authentication authentication =
            SecurityContextHolder.getContext().getAuthentication();

    User loggedInUser = (User) authentication.getPrincipal();

    IssueComment comment = commentService.addComment(
            issueId,
            loggedInUser.getId(),
            request.getContent()
    );

    return ResponseEntity.status(HttpStatus.CREATED)
            .body(convertToDTO(comment));
}


    // Get all comments for an issue
    @GetMapping
    public ResponseEntity<Page<IssueCommentDTO>> getComments(
            @PathVariable Long issueId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        if (page < 0) {
            throw new IllegalArgumentException("Page cannot be negative");
        }

        if (size < 1 || size > 100) {
            throw new IllegalArgumentException(
                    "Size must be between 1 and 100");
        }

        Pageable pageable = PageRequest.of(page, size);

        Page<IssueCommentDTO> comments = commentService
                .getCommentsByIssue(issueId, pageable)
                .map(this::convertToDTO);

        return ResponseEntity.ok(comments);
    }
    

@PutMapping("/{commentId}")
public ResponseEntity<IssueCommentDTO> updateComment(
        @PathVariable Long issueId,
        @PathVariable Long commentId,
        @Valid @RequestBody UpdateCommentRequest request) {

    Authentication authentication =
            SecurityContextHolder.getContext().getAuthentication();

    User loggedInUser = (User) authentication.getPrincipal();

    IssueComment comment = commentService.updateComment(
            commentId,
            issueId,
            loggedInUser.getId(),
            request.getContent()
    );

    return ResponseEntity.ok(convertToDTO(comment));
}

    

@DeleteMapping("/{commentId}")
public ResponseEntity<Void> deleteComment(
        @PathVariable Long issueId,
        @PathVariable Long commentId) {

    Authentication authentication =
            SecurityContextHolder.getContext().getAuthentication();

    User loggedInUser = (User) authentication.getPrincipal();

    commentService.deleteComment(
            commentId,
            issueId,
            loggedInUser.getId()
    );

    return ResponseEntity.noContent().build();
}

   
    // Convert entity into DTO
    private IssueCommentDTO convertToDTO(IssueComment comment) {

        return new IssueCommentDTO(
                comment.getId(),
                comment.getIssue().getId(),
                comment.getUser().getId(),
                comment.getUser().getName(),
                comment.getContent(),
                comment.getCreatedAt()
        );
    }
}
