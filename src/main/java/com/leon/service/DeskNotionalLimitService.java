package com.leon.service;

import com.leon.model.Desk;
import com.leon.model.DeskNotionalLimit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DeskNotionalLimitService
{
    DeskNotionalLimit saveDeskNotionalLimit(DeskNotionalLimit deskNotionalLimit);
    DeskNotionalLimit getDeskNotionalLimit(UUID deskId);
    List<DeskNotionalLimit> getAllDeskNotionalLimits();
    boolean deskNotionalLimitExists(UUID deskId);
    void deleteDeskNotionalLimit(UUID deskId);
    Desk getDeskById(UUID deskId);
    Optional<Desk> findDeskByTraderId(UUID traderId);
    DeskNotionalLimit getDeskNotionalLimitByDeskId(UUID deskId);
}
