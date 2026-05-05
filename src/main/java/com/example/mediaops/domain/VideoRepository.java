package com.example.mediaops.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Spring Data JPA repository for Video.
 *
 * No implementation file is needed — Spring generates one at runtime.
 * - JpaRepository<Video, Integer> gives us: findAll, findById, save, delete, count, etc.
 * - Custom finders below are derived from the method name
 *   (Spring parses "findByCategory" -> "WHERE category = ?").
 */
public interface VideoRepository extends JpaRepository<Video, Integer> {

    List<Video> findByCategory(String category);

    List<Video> findByCategoryOrderByCreatedAtDesc(String category);
}
