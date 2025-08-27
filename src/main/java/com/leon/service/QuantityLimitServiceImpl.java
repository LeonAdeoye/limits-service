package com.leon.service;

import com.leon.model.QuantityLimit;
import com.leon.repository.QuantityLimitRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;

@Service
public class QuantityLimitServiceImpl implements QuantityLimitService
{
    private static final Logger log = LoggerFactory.getLogger(QuantityLimitServiceImpl.class);
    
    @Autowired
    private QuantityLimitRepository quantityLimitRepository;

    @Override
    @Transactional
    public QuantityLimit saveQuantityLimit(QuantityLimit quantityLimit)
    {
        try
        {
            QuantityLimit savedQuantityLimit = quantityLimitRepository.save(quantityLimit);
            log.info("Saved quantity limit with ID: {} to MongoDB", savedQuantityLimit.getExchangeId());
            return savedQuantityLimit;
        }
        catch (Exception e)
        {
            log.error("ERR-230: Failed to save quantity limit: {}", quantityLimit.getExchangeId(), e);
            throw e;
        }
    }

    @Override
    public QuantityLimit getQuantityLimit(UUID exchangeId)
    {
        return quantityLimitRepository.findById(exchangeId).orElse(null);
    }

    @Override
    public List<QuantityLimit> getAllQuantityLimits()
    {
        return quantityLimitRepository.findAll();
    }

    @Override
    public boolean quantityLimitExists(UUID exchangeId)
    {
        return quantityLimitRepository.existsById(exchangeId);
    }

    @Override
    @Transactional
    public void deleteQuantityLimit(UUID exchangeId)
    {
        try
        {
            quantityLimitRepository.deleteById(exchangeId);
            log.info("Deleted quantity limit with ID: {} from MongoDB", exchangeId);
        }
        catch (Exception e)
        {
            log.error("ERR-231: Failed to delete quantity limit: {}", exchangeId, e);
            throw e;
        }
    }
}
