package com.leon.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leon.messaging.AmpsMessageOutboundProcessor;
import com.leon.model.Desk;
import com.leon.model.DeskNotionalLimit;
import com.leon.model.Trader;
import com.leon.model.TraderNotionalLimit;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class InitializationService
{
    private static final Logger log = LoggerFactory.getLogger(InitializationService.class);
    @Autowired
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private final DeskNotionalLimitService deskNotionalLimitService;
    @Autowired
    private final TraderNotionalLimitService traderNotionalLimitService;
    @Autowired
    private final ADVLimitService advLimitService;
    @Autowired
    private final PriceLimitService priceLimitService;
    @Autowired
    private final QuantityLimitService quantityLimitService;
    @Autowired
    AmpsMessageOutboundProcessor ampsMessageOutboundProcessor;

    @PostConstruct
    public void initialize()
    {
        deskNotionalLimitService.getAllDeskNotionalLimits().forEach(desk -> ampsMessageOutboundProcessor.publishDeskNotionalUpdate(createDeskInitialMessage(objectMapper, desk)));
        traderNotionalLimitService.getAllTraderNotionalLimits().forEach(trader -> ampsMessageOutboundProcessor.publishTraderNotionalUpdate(createTraderInitialMessage(objectMapper, trader)));
    }

    private String createDeskInitialMessage(ObjectMapper objectMapper, DeskNotionalLimit deskNotionalLimit)
    {
        try
        {
            Map<String, Object> initialDetails = new HashMap<>();
            initialDetails.put("deskId", deskNotionalLimit.getDeskId());
            String deskName = deskNotionalLimitService.getDeskById(deskNotionalLimit.getDeskId()).getDeskName();
            initialDetails.put("deskName", deskName);

            initialDetails.put("currentBuyNotional", 0);
            initialDetails.put("currentSellNotional", 0);
            initialDetails.put("currentGrossNotional", 0);

            initialDetails.put("buyUtilizationPercentage", 0);
            initialDetails.put("sellUtilizationPercentage", 0);
            initialDetails.put("grossUtilizationPercentage", 0);

            initialDetails.put("buyNotionalLimit", deskNotionalLimit.getBuyNotionalLimit());
            initialDetails.put("sellNotionalLimit", deskNotionalLimit.getSellNotionalLimit());
            initialDetails.put("grossNotionalLimit", deskNotionalLimit.getGrossNotionalLimit());
            return objectMapper.writeValueAsString(initialDetails);
        }
        catch (Exception e)
        {
            log.error("Failed to create breach message desk: {}", deskNotionalLimit, e);
            return "";
        }
    }

    private String createTraderInitialMessage(ObjectMapper objectMapper, TraderNotionalLimit traderNotionalLimit)
    {
        try
        {
            Map<String, Object> initialDetails = new HashMap<>();
            initialDetails.put("traderId", traderNotionalLimit.getTraderId());
            Trader trader = traderNotionalLimitService.getTraderById(traderNotionalLimit.getTraderId());
            initialDetails.put("traderName", trader.getFirstName() + " " + trader.getLastName());
            Desk desk = deskNotionalLimitService.findDeskByTraderId(traderNotionalLimit.getTraderId()).orElse(new Desk());
            initialDetails.put("deskId", desk.getDeskId());

            initialDetails.put("currentBuyNotional", 0);
            initialDetails.put("currentSellNotional", 0);
            initialDetails.put("currentGrossNotional", 0);

            initialDetails.put("buyUtilizationPercentage", 0);
            initialDetails.put("sellUtilizationPercentage", 0);
            initialDetails.put("grossUtilizationPercentage", 0);

            DeskNotionalLimit deskNotionalLimit = deskNotionalLimitService.getDeskNotionalLimitByDeskId(desk.getDeskId());
            initialDetails.put("deskName", desk.getDeskName());
            initialDetails.put("buyNotionalLimit", deskNotionalLimit.getBuyNotionalLimit());
            initialDetails.put("sellNotionalLimit", deskNotionalLimit.getSellNotionalLimit());
            initialDetails.put("grossNotionalLimit", deskNotionalLimit.getGrossNotionalLimit());

            return objectMapper.writeValueAsString(initialDetails);
        }
        catch (Exception e)
        {
            log.error("Failed to create initial message for trader: {}", traderNotionalLimit, e);
            return "";
        }
    }
}
