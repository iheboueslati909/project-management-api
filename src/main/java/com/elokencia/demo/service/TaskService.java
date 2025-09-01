package com.elokencia.demo.service;

import java.util.List;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.elokencia.demo.domain.Project;
import com.elokencia.demo.domain.Task;
import com.elokencia.demo.domain.User;
import com.elokencia.demo.exceptions.ResourceNotFoundException;
import com.elokencia.demo.repository.ProjectRepository;
import com.elokencia.demo.repository.TaskRepository;

@Service
@Transactional
public class TaskService {
    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final com.elokencia.demo.repository.UserRepository userRepository;
    private final AuthService authService;
    private final ActivityLogService activityLogService;

    public TaskService(TaskRepository taskRepository, ProjectRepository projectRepository, com.elokencia.demo.repository.UserRepository userRepository, AuthService authService, ActivityLogService activityLogService) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.authService = authService;
        this.activityLogService = activityLogService;
    }

    public Task createTask(Long projectId, Long assigneeId, Task task, Authentication auth) {
        Project project = projectRepository.findById(projectId)
            .orElseThrow(() -> new ResourceNotFoundException("Project not found with id " + projectId));

        User current = authService.getUserIfExists(auth);
        if (current == null) {
            throw new AccessDeniedException("Unauthenticated");
        }

        if (project.getOwner() == null || !project.getOwner().getId().equals(current.getId())) {
            throw new AccessDeniedException("Only the project owner can create tasks for this project");
        }

        task.setProject(project);

        if (assigneeId != null) {
            User assignee = userRepository.findById(assigneeId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + assigneeId));
            task.setAssignee(assignee);
        }

    Task saved = taskRepository.save(task);
    
    activityLogService.createLog(current.getId(), project.getId(), new com.elokencia.demo.domain.ActivityLog("TASK_CREATED:" + saved.getId()));
    return saved;
    }

    public List<Task> getTasksByProject(Long projectId) {
        return taskRepository.findByProjectId(projectId);
    }

    public Task getTaskById(Long id) {
        return taskRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Task not found with id " + id));
    }

    public Task updateTask(Long id, com.elokencia.demo.dto.UpdateTaskDto dto, Authentication auth) {
        Task task = getTaskById(id);

        User current = authService.getUserIfExists(auth);
        if (current == null) {
            throw new AccessDeniedException("Unauthenticated");
        }

        boolean isOwner = task.getProject().getOwner() != null && task.getProject().getOwner().getId().equals(current.getId());
        boolean isAssignee = task.getAssignee() != null && task.getAssignee().getId().equals(current.getId());
        if (!isOwner && !isAssignee) {
            throw new AccessDeniedException("You are not allowed to update this task");
        }

        if (dto.title() != null) {
            task.setTitle(dto.title());
        }

        if (dto.status() != null) {
            task.setStatus(Task.Status.valueOf(dto.status()));
        }

        if (dto.assigneeId() != null) {
            com.elokencia.demo.domain.User user = userRepository.findById(dto.assigneeId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + dto.assigneeId()));
            task.setAssignee(user);
        }

    Task saved = taskRepository.save(task);
    activityLogService.createLog(current.getId(), task.getProject().getId(), new com.elokencia.demo.domain.ActivityLog("TASK_UPDATED:" + saved.getId()));
    return saved;
    }

    public void deleteTask(Long id, Authentication auth) {
        Task task = getTaskById(id);

        User current = authService.getUserIfExists(auth);
        if (current == null) {
            throw new AccessDeniedException("Unauthenticated");
        }

        boolean isOwner = task.getProject().getOwner() != null && task.getProject().getOwner().getId().equals(current.getId());
        boolean isAssignee = task.getAssignee() != null && task.getAssignee().getId().equals(current.getId());
        if (!isOwner && !isAssignee) {
            throw new AccessDeniedException("You are not allowed to delete this task");
        }

    taskRepository.deleteById(id);
    }
}
