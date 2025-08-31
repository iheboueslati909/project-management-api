package com.elokencia.demo.dto;

import java.time.OffsetDateTime;

public record ActivityLogDto(Long id, String action, OffsetDateTime timestamp, Long userId, Long projectId) { }
