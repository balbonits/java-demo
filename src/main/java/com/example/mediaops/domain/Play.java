package com.example.mediaops.domain;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "plays")
public class Play {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "video_id", nullable = false)
    private Video video;

    @Column(name = "played_at", insertable = false, updatable = false)
    private Instant playedAt;

    @Column(name = "user_agent", columnDefinition = "text")
    private String userAgent;

    protected Play() {}

    public Play(Video video, String userAgent) {
        this.video = video;
        this.userAgent = userAgent;
    }

    public Integer getId() { return id; }
    public Video getVideo() { return video; }
    public Instant getPlayedAt() { return playedAt; }
    public String getUserAgent() { return userAgent; }
}
