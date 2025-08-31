package com.elokencia.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateProjectDto(
	@NotBlank(message = "name is required")
	@Size(max = 255, message = "name must be at most 255 characters")
	String name,

	@Size(max = 2000, message = "description must be at most 2000 characters")
	String description
) { }
