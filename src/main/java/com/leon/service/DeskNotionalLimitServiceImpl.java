package com.leon.service;

import com.leon.model.Desk;
import com.leon.model.DeskNotionalLimit;
import com.leon.repository.DeskNotionalLimitRepository;
import com.leon.repository.DeskRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class DeskNotionalLimitServiceImpl implements DeskNotionalLimitService
{
    private static final Logger log = LoggerFactory.getLogger(DeskNotionalLimitServiceImpl.class);
    private final Map<UUID, DeskNotionalLimit> deskNotionalLimitCache = new ConcurrentHashMap<>();
    @Autowired
    private final DeskNotionalLimitRepository deskNotionalLimitRepository;
    @Autowired
    private final DeskRepository deskRepository;
    private Map<UUID, Desk> desksCache = new ConcurrentHashMap<>();

    @PostConstruct
    public void initializeDeskCache()
    {
        log.info("Initializing desk cache from MongoDB");
        try
        {
            List<Desk> desks = deskRepository.findAll();
            desks.forEach(desk -> desksCache.put(desk.getDeskId(), desk));
            log.info("Loaded {} desks from MongoDB", desks.size());

            List<DeskNotionalLimit> deskNotionalLimits = deskNotionalLimitRepository.findAll();
            deskNotionalLimits.forEach(deskNotionalLimit -> deskNotionalLimitCache.put(deskNotionalLimit.getDeskId(), deskNotionalLimit));
            log.info("Loaded {} desk notional limits from MongoDB", deskNotionalLimits.size());
        }
        catch (Exception e)
        {
            log.error("ERR-201: Failed to initialize caches from MongoDB", e);
            throw new RuntimeException("Failed to initialize caches", e);
        }
    }

    @Override
    @Transactional
    public DeskNotionalLimit saveDeskNotionalLimit(DeskNotionalLimit deskNotionalLimit)
    {
        try
        {
            DeskNotionalLimit savedDeskNotionalLimit = deskNotionalLimitRepository.save(deskNotionalLimit);
            deskNotionalLimitCache.put(savedDeskNotionalLimit.getDeskId(), savedDeskNotionalLimit);
            log.info("Saved desk notional with ID: {} to MongoDB", savedDeskNotionalLimit.getDeskId());
            return savedDeskNotionalLimit;
        }
        catch (Exception e)
        {
            log.error("ERR-202: Failed to save desk: {}", deskNotionalLimit.getDeskId(), e);
            throw e;
        }
    }

    @Override
    public DeskNotionalLimit getDeskNotionalLimit(UUID deskId)
    {
        return deskNotionalLimitCache.get(deskId);
    }

    @Override
    public List<DeskNotionalLimit> getAllDeskNotionalLimits()
    {
        return deskNotionalLimitCache.values().stream().toList();
    }

    @Override
    public boolean deskNotionalLimitExists(UUID deskId)
    {
        return deskNotionalLimitCache.containsKey(deskId) || deskNotionalLimitRepository.existsById(deskId);
    }

    @Override
    @Transactional
    public void deleteDeskNotionalLimit(UUID deskId)
    {
        try
        {
            deskNotionalLimitCache.remove(deskId);
            deskNotionalLimitRepository.deleteById(deskId);
            log.info("Deleted desk notional limit with ID: {} from MongoDB", deskId);
        }
        catch (Exception e)
        {
            log.error("ERR-204: Failed to delete desk: {}", deskId, e);
            throw e;
        }
    }

    public Desk getDeskById(UUID deskId)
    {
        return desksCache.get(deskId);
    }

    public Optional<Desk> findDeskByTraderId(UUID traderId)
    {
        return desksCache.values().stream().filter(desk -> desk.getTraders().contains(traderId)).findFirst();
    }

    @Override
    public DeskNotionalLimit getDeskNotionalLimitByDeskId(UUID deskId)
    {
        return deskNotionalLimitCache.get(deskId);
    }
}
