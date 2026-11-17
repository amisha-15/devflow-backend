package com.devflow.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.devflow.dto.IssueAttachmentDTO;
import com.devflow.entity.IssueAttachment;
import com.devflow.service.IssueAttachmentService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import com.devflow.entity.User;


@RestController
@RequestMapping("/api/issues/{issueId}/attachments")
public class IssueAttachmentController {

    private final IssueAttachmentService attachmentService;

    public IssueAttachmentController(IssueAttachmentService attachmentService) {
        this.attachmentService = attachmentService;
    }

    @PostMapping
    public ResponseEntity<IssueAttachment> uploadAttachment(
            @PathVariable Long issueId,
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal User user) throws IOException {

        IssueAttachment attachment =
                attachmentService.uploadAttachment(issueId, file,user);

        return ResponseEntity.status(HttpStatus.CREATED).body(attachment);
    }

    @GetMapping
    public ResponseEntity<List<IssueAttachmentDTO>> getAttachments(
            @PathVariable Long issueId) {

        List<IssueAttachmentDTO> attachments =
                attachmentService.getAttachmentsByIssue(issueId)
                        .stream()
                        .map(attachment -> new IssueAttachmentDTO(
                                attachment.getId(),
                                attachment.getIssue().getId(),
                                attachment.getFileName(),
                                attachment.getContentType(),
                                attachment.getFileSize(),
                                attachment.getUploadedAt()))
                        .toList();

        return ResponseEntity.ok(attachments);
    }
    
    @GetMapping("/{attachmentId}/download")
    public ResponseEntity<Resource> downloadAttachment(
            @PathVariable Long attachmentId) throws IOException {

        Resource resource =
                attachmentService.downloadAttachment(attachmentId);

        return ResponseEntity.ok()
        		.contentType(MediaType.parseMediaType(
        		        attachmentService.getContentType(attachmentId)))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
    
    @DeleteMapping("/{attachmentId}")
    public ResponseEntity<Void> deleteAttachment(
            @PathVariable Long attachmentId,
            @AuthenticationPrincipal User user) throws IOException {

        attachmentService.deleteAttachment(attachmentId, user);

        return ResponseEntity.noContent().build();
    }
}
