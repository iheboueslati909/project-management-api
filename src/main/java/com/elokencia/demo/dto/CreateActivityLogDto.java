package com.elokencia.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateActivityLogDto(
	@NotBlank(message = "action is required")
	@Size(max = 1000, message = "action must be at most 1000 characters")
	String action
) { }
