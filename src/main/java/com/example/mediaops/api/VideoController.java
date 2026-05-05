package com.example.mediaops.api;

import com.example.mediaops.service.CatalogService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST endpoints for the video catalog.
 *
 * - @RestController = @Controller + @ResponseBody (every method returns
 *   JSON, not a view name).
 * - @RequestMapping at the class level prefixes all endpoint paths.
 * - Constructor injection (no @Autowired needed since Spring 4.3 when the
 *   class has a single constructor).
 */
@RestController
@RequestMapping("/api/v2/videos")
public class VideoController {

    private final CatalogService catalog;

    public VideoController(CatalogService catalog) {
        this.catalog = catalog;
    }

    @GetMapping
    public List<VideoDto> list(@RequestParam(required = false) String category) {
        return catalog.listVideos(category).stream()
            .map(VideoDto::from)
            .toList();
    }

    @GetMapping("/{id}")
    public VideoDto getOne(@PathVariable Integer id) {
        return VideoDto.from(catalog.getVideo(id));
    }
}
