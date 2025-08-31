package com.elokencia.demo.dto;

import jakarta.validation.constraints.Size;

public record UpdateProjectDto(
    @Size(max = 255, message = "name must be at most 255 characters")
    String name,

    @Size(max = 2000, message = "description must be at most 2000 characters")
    String description
) { }
