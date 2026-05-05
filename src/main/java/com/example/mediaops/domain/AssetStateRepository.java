package com.example.mediaops.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AssetStateRepository extends JpaRepository<AssetState, Long> {

    Optional<AssetState> findByVideoId(Integer videoId);
}
