
package com.devflow.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.devflow.entity.Issue;
import com.devflow.entity.IssueActivity;
import com.devflow.repository.IssueActivityRepository;
import com.devflow.repository.IssueRepository;


@Service
public class IssueActivityService {

    private final IssueActivityRepository activityRepository;
    private final IssueRepository issueRepository;

    public IssueActivityService(
            IssueActivityRepository activityRepository,
            IssueRepository issueRepository) {
        this.activityRepository = activityRepository;
        this.issueRepository = issueRepository;
    }

    public IssueActivity recordActivity(
            Long issueId, String action, String details) {

        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Issue not found"));

        IssueActivity activity = new IssueActivity();
        activity.setIssue(issue);
        activity.setAction(action);
        activity.setDetails(details);

        return activityRepository.save(activity);
    }

    public List<IssueActivity> getActivities(Long issueId) {
        return activityRepository
                .findByIssueIdOrderByCreatedAtDesc(issueId);
    }
    

public Page<IssueActivity> getActivities(Long issueId, Pageable pageable) {
    return activityRepository.findByIssueIdOrderByCreatedAtDesc(
            issueId, pageable);
}

public void deleteActivitiesByIssueId(Long issueId) {
    activityRepository.deleteByIssueId(issueId);
}
}
