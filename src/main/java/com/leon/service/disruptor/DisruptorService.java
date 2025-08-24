package com.leon.service.disruptor;

import com.lmax.disruptor.EventHandler;
import com.leon.model.Order;

public interface DisruptorService
{
    void start(String name, EventHandler<OrderEvent> actionEventHandler);
    void stop();
    void push(Order order);
}