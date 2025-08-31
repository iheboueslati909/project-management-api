package com.elokencia.demo.service;

import java.util.List;
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

    public TaskService(TaskRepository taskRepository, ProjectRepository projectRepository, com.elokencia.demo.repository.UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    public Task createTask(Long projectId, Long assigneeId, Task task) {
        Project project = projectRepository.findById(projectId)
            .orElseThrow(() -> new ResourceNotFoundException("Project not found with id " + projectId));

        task.setProject(project);

        if (assigneeId != null) {
            User assignee = userRepository.findById(assigneeId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + assigneeId));
            task.setAssignee(assignee);
        }

        return taskRepository.save(task);
    }

    public List<Task> getTasksByProject(Long projectId) {
        return taskRepository.findByProjectId(projectId);
    }

    public Task getTaskById(Long id) {
        return taskRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Task not found with id " + id));
    }

    public Task updateTask(Long id, com.elokencia.demo.dto.UpdateTaskDto dto) {
        Task task = getTaskById(id);

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

        return taskRepository.save(task);
    }

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }
}
