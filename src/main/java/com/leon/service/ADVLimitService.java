package com.leon.service;

import com.leon.model.ADVLimit;
import java.util.List;
import java.util.UUID;

public interface ADVLimitService
{
    ADVLimit saveADVLimit(ADVLimit advLimit);
    ADVLimit getADVLimit(UUID advId);
    List<ADVLimit> getAllADVLimits();
    boolean advLimitExists(UUID advId);
    void deleteADVLimit(UUID advId);
}
