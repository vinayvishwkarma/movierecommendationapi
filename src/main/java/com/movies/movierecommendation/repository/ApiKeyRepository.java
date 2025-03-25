package com.movies.movierecommendation.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.movies.movierecommendation.model.ApiKey;

@Repository
public interface ApiKeyRepository extends JpaRepository<ApiKey, Integer> {

    @Query("SELECT s FROM ApiKey s WHERE s.apiKey = :apiKey")
    Optional<ApiKey> findByApiKey(String apiKey);

    @Query("SELECT s FROM ApiKey s WHERE s.serviceName = :serviceName")
    Optional<ApiKey> findByServiceName(String serviceName);
}
