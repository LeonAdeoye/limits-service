package com.leon.service;

import com.leon.model.QuantityLimit;
import java.util.List;
import java.util.UUID;

public interface QuantityLimitService
{
    QuantityLimit saveQuantityLimit(QuantityLimit quantityLimit);
    QuantityLimit getQuantityLimit(UUID exchangeId);
    List<QuantityLimit> getAllQuantityLimits();
    boolean quantityLimitExists(UUID exchangeId);
    void deleteQuantityLimit(UUID exchangeId);
}
