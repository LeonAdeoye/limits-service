package com.leon.service;

import com.leon.model.ADVLimit;
import com.leon.repository.ADVLimitRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;

@Service
public class ADVLimitServiceImpl implements ADVLimitService
{
    private static final Logger log = LoggerFactory.getLogger(ADVLimitServiceImpl.class);
    
    @Autowired
    private ADVLimitRepository advLimitRepository;

    @Override
    @Transactional
    public ADVLimit saveADVLimit(ADVLimit advLimit)
    {
        try
        {
            ADVLimit savedADVLimit = advLimitRepository.save(advLimit);
            log.info("Saved ADV limit with ID: {} to MongoDB", savedADVLimit.getAdvId());
            return savedADVLimit;
        }
        catch (Exception e)
        {
            log.error("ERR-226: Failed to save ADV limit: {}", advLimit.getAdvId(), e);
            throw e;
        }
    }

    @Override
    public ADVLimit getADVLimit(UUID advId)
    {
        return advLimitRepository.findById(advId).orElse(null);
    }

    @Override
    public List<ADVLimit> getAllADVLimits()
    {
        return advLimitRepository.findAll();
    }

    @Override
    public boolean advLimitExists(UUID advId)
    {
        return advLimitRepository.existsById(advId);
    }

    @Override
    @Transactional
    public void deleteADVLimit(UUID advId)
    {
        try
        {
            advLimitRepository.deleteById(advId);
            log.info("Deleted ADV limit with ID: {} from MongoDB", advId);
        }
        catch (Exception e)
        {
            log.error("ERR-227: Failed to delete ADV limit: {}", advId, e);
            throw e;
        }
    }
}
