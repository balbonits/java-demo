package com.example.mediaops.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

/**
 * Tracks the pipeline state of a Video's underlying media asset.
 * Maps the Apple JD's "asset state tracking" concept.
 * Hibernate creates this table on startup (ddl-auto=update).
 *
 * State machine:
 *   INGESTING --> ENCODING --> READY
 *   *          --> FAILED  (terminal)
 */
@Entity
@Table(
    name = "asset_states",
    indexes = { @Index(name = "idx_asset_state_video", columnList = "video_id", unique = true) }
)
public class AssetState {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "video_id", nullable = false, unique = true)
    private Video video;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Status status;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    public enum Status { INGESTING, ENCODING, READY, FAILED }

    private static final Map<Status, Set<Status>> ALLOWED = Map.of(
        Status.INGESTING, EnumSet.of(Status.ENCODING, Status.FAILED),
        Status.ENCODING,  EnumSet.of(Status.READY,    Status.FAILED),
        Status.READY,     EnumSet.noneOf(Status.class),
        Status.FAILED,    EnumSet.noneOf(Status.class)
    );

    protected AssetState() {}

    public AssetState(Video video) {
        this.video = video;
        this.status = Status.INGESTING;
        this.updatedAt = Instant.now();
    }

    public void transitionTo(Status next) {
        if (!ALLOWED.get(this.status).contains(next)) {
            throw new IllegalStateException(
                "Invalid asset state transition: " + this.status + " -> " + next
            );
        }
        this.status = next;
        this.updatedAt = Instant.now();
    }

    public Long getId() { return id; }
    public Video getVideo() { return video; }
    public Status getStatus() { return status; }
    public Instant getUpdatedAt() { return updatedAt; }
}
