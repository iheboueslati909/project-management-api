package com.elokencia.demo.service;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.elokencia.demo.domain.ActivityLog;
import com.elokencia.demo.domain.Project;
import com.elokencia.demo.domain.User;
import com.elokencia.demo.exceptions.ResourceNotFoundException;
import com.elokencia.demo.repository.ActivityLogRepository;
import com.elokencia.demo.repository.ProjectRepository;
import com.elokencia.demo.repository.UserRepository;

@Service
@Transactional
public class ActivityLogService {
    private final ActivityLogRepository activityLogRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;

    public ActivityLogService(ActivityLogRepository activityLogRepository,
                              UserRepository userRepository,
                              ProjectRepository projectRepository) {
        this.activityLogRepository = activityLogRepository;
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
    }

    public ActivityLog createLog(Long userId, Long projectId, ActivityLog log) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + userId));

        Project project = projectRepository.findById(projectId)
            .orElseThrow(() -> new ResourceNotFoundException("Project not found with id " + projectId));

        log.setUser(user);
        log.setProject(project);
        log.setCreatedAt(OffsetDateTime.now());

        return activityLogRepository.save(log);
    }

    public List<ActivityLog> getLogsByUser(Long userId) {
        return activityLogRepository.findByUserId(userId);
    }

    public List<ActivityLog> getLogsByProject(Long projectId) {
        return activityLogRepository.findByProjectId(projectId);
    }
}
