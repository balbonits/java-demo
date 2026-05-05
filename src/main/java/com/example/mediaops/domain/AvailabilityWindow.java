package com.example.mediaops.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

/**
 * When and where a Video is available to stream.
 * Maps the Apple JD's "availability windows" concept.
 * Hibernate creates this table on startup (ddl-auto=update).
 */
@Entity
@Table(
    name = "availability_windows",
    indexes = {
        @Index(name = "idx_availability_video", columnList = "video_id"),
        @Index(name = "idx_availability_window", columnList = "starts_at,ends_at")
    }
)
public class AvailabilityWindow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "video_id", nullable = false)
    private Video video;

    @NotBlank
    @Column(length = 8, nullable = false)
    private String region;  // e.g., "US", "GB", "JP"

    @NotNull
    @Column(name = "starts_at", nullable = false)
    private Instant startsAt;

    @NotNull
    @Column(name = "ends_at", nullable = false)
    private Instant endsAt;

    protected AvailabilityWindow() {}

    public AvailabilityWindow(Video video, String region, Instant startsAt, Instant endsAt) {
        this.video = video;
        this.region = region;
        this.startsAt = startsAt;
        this.endsAt = endsAt;
    }

    public Long getId() { return id; }
    public Video getVideo() { return video; }
    public String getRegion() { return region; }
    public Instant getStartsAt() { return startsAt; }
    public Instant getEndsAt() { return endsAt; }
}
