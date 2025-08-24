package com.leon.repository;

import com.leon.model.ADVLimit;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface ADVLimitRepository extends MongoRepository<ADVLimit, UUID>
{
}
