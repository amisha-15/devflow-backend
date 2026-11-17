package com.devflow.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.devflow.entity.IssueAttachment;
import com.devflow.entity.Role;
import com.devflow.entity.User;
import com.devflow.repository.IssueAttachmentRepository;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.web.multipart.MultipartFile;

import com.devflow.entity.Issue;
import com.devflow.repository.IssueRepository;

@Service
public class IssueAttachmentService {
	
	private final IssueRepository issueRepository;

	@Value("${devflow.upload-dir}")
	private String uploadDir;

    private final IssueAttachmentRepository attachmentRepository;

    public IssueAttachmentService(
            IssueAttachmentRepository attachmentRepository,
            IssueRepository issueRepository) {

        this.attachmentRepository = attachmentRepository;
        this.issueRepository = issueRepository;
    }
    public List<IssueAttachment> getAttachmentsByIssue(Long issueId) {
        return attachmentRepository.findByIssueId(issueId);
    }
    
    public IssueAttachment uploadAttachment(
            Long issueId, MultipartFile file, User user) throws IOException {

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Please select a file");
        }

        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Issue not found"));

        String originalFileName = Paths.get(
                file.getOriginalFilename() == null
                        ? "uploaded-file"
                        : file.getOriginalFilename()
        ).getFileName().toString();

        String storedFileName = UUID.randomUUID() + "_" 
                + Paths.get(originalFileName).getFileName();

        Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();

        Files.createDirectories(uploadPath);

        Path filePath = uploadPath.resolve(storedFileName).normalize();

        if (!filePath.startsWith(uploadPath)) {
            throw new IllegalArgumentException("Invalid file path");
        }

        Files.copy(file.getInputStream(), filePath);

        IssueAttachment attachment = new IssueAttachment();
        attachment.setFileName(originalFileName);
        attachment.setStoredFileName(storedFileName);
        attachment.setContentType(file.getContentType());
        attachment.setFileSize(file.getSize());
        attachment.setIssue(issue);
        attachment.setUploadedBy(user);

        return attachmentRepository.save(attachment);
    }
    
    public Resource downloadAttachment(Long attachmentId) throws IOException {

        IssueAttachment attachment = attachmentRepository.findById(attachmentId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Attachment not found"));

        Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();

        Path filePath = uploadPath.resolve(attachment.getStoredFileName()).normalize();

        if (!filePath.startsWith(uploadPath)) {
            throw new IllegalArgumentException("Invalid file path");
        }

        Resource resource = new UrlResource(filePath.toUri());

        if (!resource.exists() || !resource.isReadable()) {
            throw new IllegalArgumentException("File not found");
        }

        return resource;
    }
    
    public String getContentType(Long attachmentId) {

        IssueAttachment attachment = attachmentRepository.findById(attachmentId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Attachment not found"));

        return attachment.getContentType();
    }
    
    public void deleteAttachment(Long attachmentId,User user) throws IOException {

        IssueAttachment attachment = attachmentRepository.findById(attachmentId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Attachment not found"));
        if (user.getRole() != Role.ADMIN &&
                (attachment.getUploadedBy() == null ||
                 !attachment.getUploadedBy().getId().equals(user.getId()))) {

            throw new org.springframework.security.access.AccessDeniedException(
                    "You are not allowed to delete this attachment");
        }

        Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();

        Path filePath = uploadPath.resolve(attachment.getStoredFileName()).normalize();

        if (!filePath.startsWith(uploadPath)) {
            throw new IllegalArgumentException("Invalid file path");
        }

        Files.deleteIfExists(filePath);

        attachmentRepository.delete(attachment);
    }
}