package com.elokencia.demo.service;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;

import com.elokencia.demo.service.AuthService;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.elokencia.demo.domain.Project;
import com.elokencia.demo.domain.User;
import com.elokencia.demo.exceptions.ResourceNotFoundException;
import com.elokencia.demo.repository.ProjectRepository;

@Service
@Transactional
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final AuthService authService;
    private final ActivityLogService activityLogService;
    
    public ProjectService(ProjectRepository projectRepository, AuthService authService, ActivityLogService activityLogService) {
        this.projectRepository = projectRepository;
        this.authService = authService;
        this.activityLogService = activityLogService;
    }

    public Project createProject(Project project, Authentication auth) {
        User user = authService.getUserIfExists(auth);

        if (user == null) {
            throw new AccessDeniedException("Unauthenticated");
        }

    project.setOwner(user);
    Project saved = projectRepository.save(project);
    activityLogService.createLog(user.getId(), saved.getId(), new com.elokencia.demo.domain.ActivityLog("PROJECT_CREATED:" + saved.getId()));
    return saved;
    }

    public List<Project> getProjectsByOwner(Long ownerId) {
        return projectRepository.findByOwnerId(ownerId);
    }

    public Project getProjectById(Long id) {
        return projectRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Project not found with id " + id));
    }

    public Project updateProject(Long id, com.elokencia.demo.dto.UpdateProjectDto dto, Authentication auth) {
        Project project = getProjectById(id);

        User currentUser = authService.getUserIfExists(auth);
        if (currentUser == null) {
            throw new AccessDeniedException("Unauthenticated");
        }

        if (project.getOwner() == null || !project.getOwner().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("Only the project owner can update this project");
        }

        if (dto.name() != null) {
            project.setName(dto.name());
        }
        if (dto.description() != null) {
            project.setDescription(dto.description());
        }

        Project saved = projectRepository.save(project);
        activityLogService.createLog(currentUser.getId(), saved.getId(), new com.elokencia.demo.domain.ActivityLog("PROJECT_UPDATED:" + saved.getId()));
        return saved;
    }

    public void deleteProject(Long id, Authentication auth) {
        Project project = projectRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Project not found with id " + id));

        User currentUser = authService.getUserIfExists(auth);
        if (currentUser == null) {
            throw new AccessDeniedException("Unauthenticated");
        }

        if (!project.getOwner().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You are not allowed to delete this project");
        }    
        
        projectRepository.delete(project);
    }

}
