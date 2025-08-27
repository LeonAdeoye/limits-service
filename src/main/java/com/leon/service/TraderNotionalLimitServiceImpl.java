package com.leon.service;

import com.leon.model.Trader;
import com.leon.model.TraderNotionalLimit;
import com.leon.repository.TraderNotionalLimitRepository;
import com.leon.repository.TraderRepository;
import jakarta.annotation.PostConstruct;
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
import java.util.stream.Collectors;

@Service
public class TraderNotionalLimitServiceImpl implements TraderNotionalLimitService
{
    private static final Logger log = LoggerFactory.getLogger(TraderNotionalLimitServiceImpl.class);
    
    @Autowired
    private TraderNotionalLimitRepository traderNotionalLimitRepository;
    @Autowired
    private TraderRepository traderRepository;
    
    private Map<UUID, Trader> tradersCache = new ConcurrentHashMap<>();
    private final Map<UUID, TraderNotionalLimit> traderNotionalLimitCache = new ConcurrentHashMap<>();
    private final Map<UUID, List<TraderNotionalLimit>> deskTradersCache = new ConcurrentHashMap<>();

    @PostConstruct
    public void initializeTraderCache()
    {
        log.info("Initializing trader cache from MongoDB");
        try
        {
            List<Trader> traders = traderRepository.findAll();
            traders.forEach(trader -> tradersCache.put(trader.getTraderId(), trader));
            log.info("Loaded {} traders from MongoDB", traders.size());

            List<TraderNotionalLimit> tradersNotionalLimits = traderNotionalLimitRepository.findAll();
            tradersNotionalLimits.forEach(traderNotionalLimit -> traderNotionalLimitCache.put(traderNotionalLimit.getTraderId(), traderNotionalLimit));
            log.info("Loaded {} trader notional limits from MongoDB", tradersNotionalLimits.size());

            deskTradersCache.putAll(tradersNotionalLimits.stream().collect(Collectors.groupingBy(TraderNotionalLimit::getTraderId)));
            log.info("Initialized desk traders cache with {} entries", deskTradersCache.size());
        }
        catch (Exception e)
        {
            log.error("ERR-201: Failed to initialize caches from MongoDB", e);
            throw new RuntimeException("Failed to initialize caches", e);
        }
    }

    @Override
    @Transactional
    public TraderNotionalLimit saveTraderNotionalLimit(TraderNotionalLimit traderNotionalLimit)
    {
        try
        {
            TraderNotionalLimit savedTraderNotionalLimit = traderNotionalLimitRepository.save(traderNotionalLimit);
            traderNotionalLimitCache.put(savedTraderNotionalLimit.getTraderId(), savedTraderNotionalLimit);
            log.info("Saved trader notional with ID: {} to MongoDB", savedTraderNotionalLimit.getTraderId());
            return savedTraderNotionalLimit;
        }
        catch (Exception e)
        {
            log.error("ERR-203: Failed to save trader: {}", traderNotionalLimit.getTraderId(), e);
            throw e;
        }
    }

    @Override
    public TraderNotionalLimit getTraderNotionalLimit(UUID traderId)
    {
        return traderNotionalLimitCache.get(traderId);
    }

    @Override
    public List<TraderNotionalLimit> getAllTraderNotionalLimits()
    {
        return traderNotionalLimitCache.values().stream().toList();
    }

    @Override
    public boolean traderNotionalLimitExists(UUID traderId)
    {
        return traderNotionalLimitCache.containsKey(traderId) || traderNotionalLimitRepository.existsById(traderId);
    }

    @Override
    @Transactional
    public void deleteTraderNotionalLimit(UUID traderId)
    {
        try
        {
            traderNotionalLimitRepository.deleteById(traderId);
            traderNotionalLimitCache.remove(traderId);
            log.info("Deleted trader notional limit with ID: {} from MongoDB", traderId);
        }
        catch (Exception e)
        {
            log.error("ERR-205: Failed to delete trader: {}", traderId, e);
            throw e;
        }
    }

    public Trader getTraderById(UUID traderId)
    {
        return tradersCache.get(traderId);
    }

    public String findTraderFullNameByUserId(String userId)
    {
        for (Trader trader : tradersCache.values())
        {
            if (trader.getUserId().equals(userId))
                return trader.getFirstName() + " " + trader.getLastName();
        }

        return userId;
    }

    public Optional<Trader> findTraderByUserId(String userId)
    {
        return tradersCache.values().stream().filter(trader -> trader.getUserId().equals(userId)).findFirst();
    }

    @Override
    public List<TraderNotionalLimit> getDeskTraderNotionalLimits(UUID deskId)
    {
        return deskTradersCache.getOrDefault(deskId, List.of());
    }
}
