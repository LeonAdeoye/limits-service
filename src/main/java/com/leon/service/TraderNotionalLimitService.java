package com.leon.service;

import com.leon.model.Trader;
import com.leon.model.TraderNotionalLimit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TraderNotionalLimitService
{
    TraderNotionalLimit saveTraderNotionalLimit(TraderNotionalLimit traderNotionalLimit);
    TraderNotionalLimit getTraderNotionalLimit(UUID traderId);
    List<TraderNotionalLimit> getAllTraderNotionalLimits();
    boolean traderNotionalLimitExists(UUID traderId);
    void deleteTraderNotionalLimit(UUID traderId);
    Trader getTraderById(UUID traderId);
    String findTraderFullNameByUserId(String userId);
    Optional<Trader> findTraderByUserId(String userId);
    List<TraderNotionalLimit> getDeskTraderNotionalLimits(UUID deskId);
}
