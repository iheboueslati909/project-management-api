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

import com.elokencia.demo.domain.Task;
import com.elokencia.demo.dto.CreateTaskDto;
import com.elokencia.demo.dto.TaskDto;
import com.elokencia.demo.service.TaskService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("/project/{projectId}")
    public ResponseEntity<TaskDto> createTask(@PathVariable Long projectId,
                                            @Valid @RequestBody CreateTaskDto dto) {
        Task task = new Task(dto.title(), Task.Status.valueOf(dto.status()));

        Task saved = taskService.createTask(projectId, dto.assigneeId(), task);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new TaskDto(
                        saved.getId(),
                        saved.getTitle(),
                        saved.getStatus(),
                        saved.getProject().getId(),
                        saved.getAssignee() != null ? saved.getAssignee().getId() : null
                ));
    }

    @GetMapping("/project/{projectId}")
    public List<TaskDto> getTasksByProject(@PathVariable Long projectId) {
        return taskService.getTasksByProject(projectId)
                .stream()
                .map(t -> new TaskDto(t.getId(), t.getTitle(), t.getStatus(), t.getProject().getId(), t.getAssignee() != null ? t.getAssignee().getId() : null))
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDto> getTaskById(@PathVariable Long id) {
        Task t = taskService.getTaskById(id);
        return ResponseEntity.ok(new TaskDto(t.getId(), t.getTitle(), t.getStatus(), t.getProject().getId(), t.getAssignee() != null ? t.getAssignee().getId() : null));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskDto> updateTask(@PathVariable Long id,
                                              @Valid @RequestBody com.elokencia.demo.dto.UpdateTaskDto dto) {
        Task updated = taskService.updateTask(id, dto);
        return ResponseEntity.ok(new TaskDto(updated.getId(), updated.getTitle(), updated.getStatus(), updated.getProject().getId(), updated.getAssignee() != null ? updated.getAssignee().getId() : null));
    }

    //TODO AFTER AUTH IMPLEMENTATION
    // @DeleteMapping("/{id}")
    // public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
    //     taskService.deleteTask(id);
    //     return ResponseEntity.noContent().build();
    // }

}
