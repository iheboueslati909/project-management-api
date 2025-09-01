package com.elokencia.demo.controller;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;
import com.elokencia.demo.domain.Project;
import com.elokencia.demo.dto.CreateProjectDto;
import com.elokencia.demo.dto.ProjectDto;
import com.elokencia.demo.dto.UpdateProjectDto;
import com.elokencia.demo.service.ProjectService;

@RestController
@RequestMapping("/api/projects")
@PreAuthorize("isAuthenticated()")
public class ProjectController {
    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping
    public ResponseEntity<ProjectDto> createProject(@Valid @RequestBody CreateProjectDto dto, Authentication auth) {
        Project project = new Project(dto.name(), dto.description());
        Project saved = projectService.createProject(project, auth);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ProjectDto(saved.getId(), saved.getName(), saved.getDescription(), saved.getOwner().getId()));
    }

    @GetMapping("/user/{ownerId}")
    public List<ProjectDto> getProjectsByUser(@PathVariable Long ownerId) {
        return projectService.getProjectsByOwner(ownerId)
                .stream()
                .map(p -> new ProjectDto(p.getId(), p.getName(), p.getDescription(), p.getOwner().getId()))
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectDto> getProjectById(@PathVariable Long id) {
        Project p = projectService.getProjectById(id);
        return ResponseEntity.ok(new ProjectDto(p.getId(), p.getName(), p.getDescription(), p.getOwner().getId()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjectDto> updateProject(@PathVariable Long id,
                                                    @Valid @RequestBody UpdateProjectDto dto,
                                                    Authentication auth) {
        Project updated = projectService.updateProject(id, dto, auth);
        return ResponseEntity.ok(new ProjectDto(updated.getId(), updated.getName(), updated.getDescription(), updated.getOwner().getId()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id, Authentication auth) {
        projectService.deleteProject(id, auth);
        return ResponseEntity.noContent().build();
    }
}
