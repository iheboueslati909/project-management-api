package com.elokencia.demo.dto;

import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;

public record UpdateTaskDto(
    @Size(max = 255, message = "title must be at most 255 characters")
    String title,

    @Pattern(regexp = "TODO|IN_PROGRESS|DONE", message = "status must be TODO, IN_PROGRESS or DONE")
    String status,

    Long assigneeId
) { }
