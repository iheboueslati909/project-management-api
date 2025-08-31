package com.elokencia.demo.dto;

import com.elokencia.demo.domain.Task;

public record TaskDto(Long id, String title, Task.Status status, Long projectId, Long assigneeId) {}

