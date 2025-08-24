package com.leon.messaging;

import com.crankuptheamps.client.Client;
import com.crankuptheamps.client.Message;
import com.crankuptheamps.client.MessageHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.leon.model.Order;
import com.leon.service.LimitService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AmpsMessageInboundProcessor implements MessageHandler
{
    private static final Logger log = LoggerFactory.getLogger(AmpsMessageInboundProcessor.class);
    @Value("${amps.server.url}")
    private String ampsServerUrl;
    @Value("${amps.client.name}")
    private String ampsClientName;
    @Value("${amps.topic.orders}")
    private String ordersTopic;
    @Autowired
    private final LimitService limitService;
    private ObjectMapper objectMapper;
    private Client ampsClient;
    
    @PostConstruct
    public void initialize() throws Exception
    {
        try
        {
            ampsClient = new Client(ampsClientName);
            ampsClient.connect(ampsServerUrl);
            ampsClient.logon();
            objectMapper = new ObjectMapper();
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("h:mm:ss a", Locale.ENGLISH);
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("M/d/yyyy", Locale.ENGLISH);
            JavaTimeModule javaTimeModule = new JavaTimeModule();
            javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(dateFormatter));
            javaTimeModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(timeFormatter));
            objectMapper.registerModule(javaTimeModule);
            for(Message message : (ampsClient.subscribe(ordersTopic, "/actionEvent = 'SUBMIT_TO_EXCH' AND /state = 'ACCEPTED_BY_DESK'")))
                invoke(message);

        }
        catch (Exception e)
        {
            log.error("ERR-007: Failed to initialize AMPS client", e);
            throw e;
        }
    }
    
    @Override
    public void invoke(Message message)
    {
        String errorId = UUID.randomUUID().toString();
        MDC.put("errorId", errorId);
        
        try
        {
            Order order = objectMapper.readValue(message.getData(), Order.class);
            log.info("Received valid order message: {}", order);
            limitService.processOrder(order);
            
        }
        catch (Exception e)
        {
            log.error("ERR-009: Failed to process message", e);
        }
        finally
        {
            MDC.remove("errorId");
        }
    }
} 