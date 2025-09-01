package com.elokencia.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

import com.elokencia.demo.domain.User;
import com.elokencia.demo.dto.UserDto;
import com.elokencia.demo.service.AuthService;
import com.elokencia.demo.repository.TaskRepository;
import com.elokencia.demo.dto.UserProgressDto;
import com.elokencia.demo.domain.Task;
import java.util.List;

@RestController
@RequestMapping("/api")
@PreAuthorize("isAuthenticated()")
public class UserController {


    private final AuthService currentUserService;
    private final TaskRepository taskRepository;
    
    public UserController(AuthService currentUserService, TaskRepository taskRepository) {
        this.currentUserService = currentUserService;
        this.taskRepository = taskRepository;
    }


    @GetMapping("/users/me")
    public ResponseEntity<UserDto> me(Authentication authentication) {
        User user = currentUserService.getOrCreateUser(authentication);
        return ResponseEntity.ok(new UserDto(user.getId(), user.getName(), user.getEmail()));
    }

    @GetMapping("/users/{id}/progress")
    public ResponseEntity<UserProgressDto> getUserProgress(@PathVariable Long id) {
        
        List<Task> tasks = taskRepository.findByAssigneeId(id);
        if (tasks.isEmpty()) {
            return ResponseEntity.ok(new UserProgressDto(id, 0.0));
        }

        long done = tasks.stream().filter(t -> t.getStatus() == Task.Status.DONE).count();
        double pct = (done * 100.0) / tasks.size();
        return ResponseEntity.ok(new UserProgressDto(id, pct));
    }
}
