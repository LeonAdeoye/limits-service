package com.leon.repository;

import com.leon.model.QuantityLimit;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface QuantityLimitRepository extends MongoRepository<QuantityLimit, UUID>
{
}
