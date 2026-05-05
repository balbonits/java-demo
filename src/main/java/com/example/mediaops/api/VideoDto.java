package com.example.mediaops.api;

import com.example.mediaops.domain.Video;

import java.time.Instant;

/**
 * Wire-format DTO for a Video. Decouples the JSON response shape from the
 * entity, so Hibernate-managed fields (proxies, lazy collections) never
 * accidentally serialize. Records are immutable by default — perfect for DTOs.
 */
public record VideoDto(
    Integer id,
    String title,
    String description,
    String videoUrl,
    String thumbnailUrl,
    Integer duration,
    String category,
    String[] tags,
    Integer viewCount,
    Instant createdAt,
    Instant updatedAt
) {
    public static VideoDto from(Video v) {
        return new VideoDto(
            v.getId(),
            v.getTitle(),
            v.getDescription(),
            v.getVideoUrl(),
            v.getThumbnailUrl(),
            v.getDuration(),
            v.getCategory(),
            v.getTags(),
            v.getViewCount(),
            v.getCreatedAt(),
            v.getUpdatedAt()
        );
    }
}
