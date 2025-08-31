package com.elokencia.demo.controller;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.elokencia.demo.domain.Project;
import com.elokencia.demo.dto.CreateProjectDto;
import com.elokencia.demo.dto.ProjectDto;
import com.elokencia.demo.service.ProjectService;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {
    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping("/user/{userId}")
    public ResponseEntity<ProjectDto> createProject(@PathVariable Long userId,
                                                    @RequestBody CreateProjectDto dto) {
        Project project = new Project(dto.name(), dto.description());
        Project saved = projectService.createProject(userId, project);

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
                                                    @jakarta.validation.Valid @org.springframework.web.bind.annotation.RequestBody com.elokencia.demo.dto.UpdateProjectDto dto) {
        Project updated = projectService.updateProject(id, dto);
        return ResponseEntity.ok(new ProjectDto(updated.getId(), updated.getName(), updated.getDescription(), updated.getOwner().getId()));
    }

    // @DeleteMapping("/{id}")
    // public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
    //     projectService.deleteProject(id, auth);
    //     return ResponseEntity.noContent().build();
    // }
}
