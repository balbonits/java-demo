package com.example.mediaops.api;

import jakarta.validation.constraints.NotNull;

/**
 * Request body for POST /api/v2/plays.
 * Bean Validation guards: videoId required.
 */
public record RecordPlayRequest(
    @NotNull Integer videoId,
    String userAgent
) {}
