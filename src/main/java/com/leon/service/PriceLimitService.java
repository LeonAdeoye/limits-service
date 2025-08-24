package com.leon.service;

import com.leon.model.PriceLimit;
import java.util.List;
import java.util.UUID;

public interface PriceLimitService
{
    PriceLimit savePriceLimit(PriceLimit priceLimit);
    PriceLimit getPriceLimit(UUID exchangeId);
    List<PriceLimit> getAllPriceLimits();
    boolean priceLimitExists(UUID exchangeId);
    void deletePriceLimit(UUID exchangeId);
}
