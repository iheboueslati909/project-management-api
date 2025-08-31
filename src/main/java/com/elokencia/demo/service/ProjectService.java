package com.elokencia.demo.service;

import java.nio.file.AccessDeniedException;
import java.util.List;

import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.elokencia.demo.domain.Project;
import com.elokencia.demo.domain.User;
import com.elokencia.demo.exceptions.ResourceNotFoundException;
import com.elokencia.demo.repository.ProjectRepository;
import com.elokencia.demo.repository.UserRepository;

@Service
@Transactional
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public ProjectService(ProjectRepository projectRepository, UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    public Project createProject(Long ownerId, Project project) {
        User user = userRepository.findById(ownerId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + ownerId));

        project.setOwner(user);
        return projectRepository.save(project);
    }

    public List<Project> getProjectsByOwner(Long ownerId) {
        return projectRepository.findByOwnerId(ownerId);
    }

    public Project getProjectById(Long id) {
        return projectRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Project not found with id " + id));
    }

    public Project updateProject(Long id, com.elokencia.demo.dto.UpdateProjectDto dto) {
        Project project = getProjectById(id);

        if (dto.name() != null) {
            project.setName(dto.name());
        }
        if (dto.description() != null) {
            project.setDescription(dto.description());
        }

        return projectRepository.save(project);
    }

    //TODO AFTER AUTH IMPLEMENTATION
    // public void deleteProject(Long id, Authentication auth) {
    //     Project project = projectRepository.findById(id)
    //         .orElseThrow(() -> new ResourceNotFoundException("Project not found with id " + id));

    //     // Get current user (from JWT claims, already validated by Spring Security)
    //     Jwt jwt = (Jwt) auth.getPrincipal();
    //     String currentUserEmail = jwt.getClaim("preferred_username");

    //     // Check ownership
    //     if (!project.getOwner().getEmail().equalsIgnoreCase(currentUserEmail)) {
    //         throw new AccessDeniedException("You are not allowed to delete this project");
    //     }

    //     projectRepository.delete(project);
    // }
}
