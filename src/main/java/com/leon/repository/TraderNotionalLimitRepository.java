package com.leon.repository;

import com.leon.model.TraderNotionalLimit;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TraderNotionalLimitRepository extends MongoRepository<TraderNotionalLimit, UUID> {
} 