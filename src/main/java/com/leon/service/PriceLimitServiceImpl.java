package com.leon.service;

import com.leon.model.PriceLimit;
import com.leon.repository.PriceLimitRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PriceLimitServiceImpl implements PriceLimitService
{
    private static final Logger log = LoggerFactory.getLogger(PriceLimitServiceImpl.class);
    
    @Autowired
    private final PriceLimitRepository priceLimitRepository;

    @Override
    @Transactional
    public PriceLimit savePriceLimit(PriceLimit priceLimit)
    {
        try
        {
            PriceLimit savedPriceLimit = priceLimitRepository.save(priceLimit);
            log.info("Saved price limit with ID: {} to MongoDB", savedPriceLimit.getExchangeId());
            return savedPriceLimit;
        }
        catch (Exception e)
        {
            log.error("ERR-228: Failed to save price limit: {}", priceLimit.getExchangeId(), e);
            throw e;
        }
    }

    @Override
    public PriceLimit getPriceLimit(UUID exchangeId)
    {
        return priceLimitRepository.findById(exchangeId).orElse(null);
    }

    @Override
    public List<PriceLimit> getAllPriceLimits()
    {
        return priceLimitRepository.findAll();
    }

    @Override
    public boolean priceLimitExists(UUID exchangeId)
    {
        return priceLimitRepository.existsById(exchangeId);
    }

    @Override
    @Transactional
    public void deletePriceLimit(UUID exchangeId)
    {
        try
        {
            priceLimitRepository.deleteById(exchangeId);
            log.info("Deleted price limit with ID: {} from MongoDB", exchangeId);
        }
        catch (Exception e)
        {
            log.error("ERR-229: Failed to delete price limit: {}", exchangeId, e);
            throw e;
        }
    }
}
