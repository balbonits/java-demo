package com.example.mediaops.service;

import com.example.mediaops.domain.Play;
import com.example.mediaops.domain.PlayRepository;
import com.example.mediaops.domain.Video;
import com.example.mediaops.domain.VideoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * Business logic over the catalog.
 *
 * - @Service marks the class as a Spring-managed bean (DI target).
 * - Constructor injection: dependencies are final and required at construction.
 * - @Transactional: each public method runs in a DB transaction. Rollback on
 *   any RuntimeException; commit otherwise.
 * - Read-only methods declare readOnly=true; lets Hibernate skip dirty-check
 *   bookkeeping and Postgres optimize the txn.
 */
@Service
public class CatalogService {

    private final VideoRepository videos;
    private final PlayRepository plays;

    public CatalogService(VideoRepository videos, PlayRepository plays) {
        this.videos = videos;
        this.plays = plays;
    }

    @Transactional(readOnly = true)
    public List<Video> listVideos(String categoryFilter) {
        if (categoryFilter == null || categoryFilter.isBlank()) {
            return videos.findAll();
        }
        return videos.findByCategoryOrderByCreatedAtDesc(categoryFilter);
    }

    @Transactional(readOnly = true)
    public Video getVideo(Integer id) {
        return videos.findById(id)
            .orElseThrow(() -> new NoSuchElementException("Video " + id + " not found"));
    }

    @Transactional
    public Play recordPlay(Integer videoId, String userAgent) {
        Video video = getVideo(videoId);
        video.incrementViewCount();   // dirty-checked, auto-UPDATEd at commit
        return plays.save(new Play(video, userAgent));
    }
}
