package com.leon.repository;

import com.leon.model.PriceLimit;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface PriceLimitRepository extends MongoRepository<PriceLimit, UUID>
{
}
