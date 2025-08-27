package com.leon.controller;

import com.leon.model.QuantityLimit;
import com.leon.service.QuantityLimitService;
import jakarta.annotation.PostConstruct;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/limits/qty")
public class QuantityLimitController
{
    private static final Logger log = LoggerFactory.getLogger(QuantityLimitController.class);
    @PostConstruct
    public void init() {
        log.info("✅ QuantityLimitController ===> initialized");
    }


    @Autowired
    private QuantityLimitService quantityLimitService;

    @CrossOrigin
    @PostMapping
    public ResponseEntity<QuantityLimit> saveQuantityLimit(@RequestBody QuantityLimit quantityLimit)
    {
        String errorId = UUID.randomUUID().toString();
        MDC.put("errorId", errorId);

        try
        {
            QuantityLimit savedQuantityLimit = quantityLimitService.saveQuantityLimit(quantityLimit);
            log.info("Successfully saved quantity limit: {}", savedQuantityLimit.getExchangeId());
            return ResponseEntity.status(HttpStatus.CREATED).body(savedQuantityLimit);
        }
        catch (Exception e)
        {
            log.error("ERR-433: Error saving quantity limit", e);
            return ResponseEntity.internalServerError().build();
        }
        finally
        {
            MDC.remove("errorId");
        }
    }

    @CrossOrigin
    @GetMapping("/{id}")
    public ResponseEntity<QuantityLimit> getQuantityLimit(@NotNull @PathVariable UUID id)
    {
        String errorId = UUID.randomUUID().toString();
        MDC.put("errorId", errorId);
        
        try
        {
            QuantityLimit quantityLimit = quantityLimitService.getQuantityLimit(id);
            if (quantityLimit == null)
            {
                log.error("ERR-434: Quantity limit not found with ID: {}", id);
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(quantityLimit);
        }
        catch (Exception e)
        {
            log.error("ERR-435: Error retrieving quantity limit with ID: {}", id, e);
            return ResponseEntity.internalServerError().build();
        }
        finally
        {
            MDC.remove("errorId");
        }
    }

    @CrossOrigin
    @GetMapping
    public ResponseEntity<List<QuantityLimit>> getAllQuantityLimits()
    {
        String errorId = UUID.randomUUID().toString();
        MDC.put("errorId", errorId);
        
        try
        {
            return ResponseEntity.ok(quantityLimitService.getAllQuantityLimits());
        }
        catch (Exception e)
        {
            log.error("ERR-436: Error retrieving all quantity limits", e);
            return ResponseEntity.internalServerError().build();
        }
        finally
        {
            MDC.remove("errorId");
        }
    }

    @CrossOrigin
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuantityLimit(@NotNull @PathVariable UUID id)
    {
        String errorId = UUID.randomUUID().toString();
        MDC.put("errorId", errorId);
        
        try
        {
            if (!quantityLimitService.quantityLimitExists(id))
            {
                log.error("ERR-437: Quantity limit not found for deletion: {}", id);
                return ResponseEntity.notFound().build();
            }
            quantityLimitService.deleteQuantityLimit(id);
            log.info("Successfully deleted quantity limit with: {}", id);
            return ResponseEntity.ok().build();
        }
        catch (Exception e)
        {
            log.error("ERR-439: Error deleting quantity limit: {}", id, e);
            return ResponseEntity.internalServerError().build();
        }
        finally
        {
            MDC.remove("errorId");
        }
    }
}
