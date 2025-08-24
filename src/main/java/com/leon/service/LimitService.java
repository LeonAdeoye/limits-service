package com.leon.service;

import com.leon.model.Order;
import com.leon.service.disruptor.DisruptorService;
import com.leon.service.disruptor.OrderEventHandler;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LimitService
{
    private static final Logger log = LoggerFactory.getLogger(LimitService.class);
    private static int countOfOrders = 0;
    @Autowired
    private final OrderEventHandler orderEventHandler;
    @Autowired
    private DisruptorService disruptorService;
    @Autowired
    private final InitializationService initializationService;
    @Value("${server.port}")
    private int serverPort;

    @PostConstruct
    public void initialize()
    {
        log.info("Server is starting on port: {}", serverPort);
        disruptorService.start("LimitService", orderEventHandler);
    }
    @PreDestroy
    public void shutdown()
    {
        log.info("Shutting down LimitService. Total orders processed: {}", countOfOrders);
        disruptorService.stop();
    }

    public void processOrder(Order order)
    {
        String errorId = UUID.randomUUID().toString();
        MDC.put("errorId", errorId);
        try
        {
            countOfOrders++;
            if(!isValidOrder(order))
            {
                log.error("Invalid order: {}", order);
                return;
            }
            disruptorService.push(order);
        }
        finally
        {
            MDC.remove("errorId");
        }
    }
    
    private static boolean isValidOrder(Order order)
    {
        if (order.getQuantity() <= 0)
            return false;
        if (order.getPrice() <= 0)
            return false;
        return true;
    }
} 