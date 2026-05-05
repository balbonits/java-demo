package com.example.mediaops.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;

@Entity
@Table(name = "videos")
public class Video {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank
    @Column(nullable = false, length = 255)
    private String title;

    @Column(columnDefinition = "text")
    private String description;

    @NotBlank
    @Column(name = "video_url", nullable = false, columnDefinition = "text")
    private String videoUrl;

    @Column(name = "thumbnail_url", columnDefinition = "text")
    private String thumbnailUrl;

    private Integer duration;

    @Column(length = 100)
    private String category;

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(columnDefinition = "text[]")
    private String[] tags;

    @Column(name = "view_count", nullable = false)
    private Integer viewCount = 0;

    @Column(name = "created_at", insertable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private Instant updatedAt;

    protected Video() {}

    public Video(String title, String videoUrl, String category) {
        this.title = title;
        this.videoUrl = videoUrl;
        this.category = category;
    }

    public Integer getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getVideoUrl() { return videoUrl; }
    public String getThumbnailUrl() { return thumbnailUrl; }
    public Integer getDuration() { return duration; }
    public String getCategory() { return category; }
    public String[] getTags() { return tags; }
    public Integer getViewCount() { return viewCount; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }

    public void setDescription(String description) { this.description = description; }
    public void setThumbnailUrl(String thumbnailUrl) { this.thumbnailUrl = thumbnailUrl; }
    public void setDuration(Integer duration) { this.duration = duration; }
    public void setCategory(String category) { this.category = category; }
    public void setTags(String[] tags) { this.tags = tags; }
    public void incrementViewCount() { this.viewCount = (this.viewCount == null ? 1 : this.viewCount + 1); }
}
