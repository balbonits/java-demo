package com.example.mediaops.api;

import com.example.mediaops.domain.Play;
import com.example.mediaops.service.CatalogService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v2/plays")
public class PlayController {

    private final CatalogService catalog;

    public PlayController(CatalogService catalog) {
        this.catalog = catalog;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, Object> record(@Valid @RequestBody RecordPlayRequest request) {
        Play p = catalog.recordPlay(request.videoId(), request.userAgent());
        return Map.of(
            "id", p.getId(),
            "videoId", p.getVideo().getId(),
            "playedAt", p.getPlayedAt()
        );
    }
}
