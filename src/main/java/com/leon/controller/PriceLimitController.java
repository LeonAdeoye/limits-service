package com.leon.controller;

import com.leon.model.PriceLimit;
import com.leon.service.PriceLimitService;
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
@RequestMapping("/limits/price")
public class PriceLimitController
{
    private static final Logger log = LoggerFactory.getLogger(PriceLimitController.class);

    @Autowired
    private PriceLimitService priceLimitService;

    @CrossOrigin
    @RequestMapping("/heartbeat")
    String heartbeat()
    {
        return "Here I am";
    }

    @CrossOrigin
    @PostMapping
    public ResponseEntity<PriceLimit> savePriceLimit(@RequestBody PriceLimit priceLimit)
    {
        String errorId = UUID.randomUUID().toString();
        MDC.put("errorId", errorId);

        try
        {
            PriceLimit savedPriceLimit = priceLimitService.savePriceLimit(priceLimit);
            log.info("Successfully saved price limit: {}", savedPriceLimit.getExchangeId());
            return ResponseEntity.status(HttpStatus.CREATED).body(savedPriceLimit);
        }
        catch (Exception e)
        {
            log.error("ERR-426: Error saving price limit", e);
            return ResponseEntity.internalServerError().build();
        }
        finally
        {
            MDC.remove("errorId");
        }
    }

    @CrossOrigin
    @GetMapping("/{id}")
    public ResponseEntity<PriceLimit> getPriceLimit(@NotNull @PathVariable UUID id)
    {
        String errorId = UUID.randomUUID().toString();
        MDC.put("errorId", errorId);
        
        try
        {
            PriceLimit priceLimit = priceLimitService.getPriceLimit(id);
            if (priceLimit == null)
            {
                log.error("ERR-427: Price limit not found with ID: {}", id);
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(priceLimit);
        }
        catch (Exception e)
        {
            log.error("ERR-428: Error retrieving price limit with ID: {}", id, e);
            return ResponseEntity.internalServerError().build();
        }
        finally
        {
            MDC.remove("errorId");
        }
    }

    @CrossOrigin
    @GetMapping
    public ResponseEntity<List<PriceLimit>> getAllPriceLimits()
    {
        String errorId = UUID.randomUUID().toString();
        MDC.put("errorId", errorId);
        
        try
        {
            return ResponseEntity.ok(priceLimitService.getAllPriceLimits());
        }
        catch (Exception e)
        {
            log.error("ERR-429: Error retrieving all price limits", e);
            return ResponseEntity.internalServerError().build();
        }
        finally
        {
            MDC.remove("errorId");
        }
    }

    @CrossOrigin
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePriceLimit(@NotNull @PathVariable UUID id)
    {
        String errorId = UUID.randomUUID().toString();
        MDC.put("errorId", errorId);
        
        try
        {
            if (!priceLimitService.priceLimitExists(id))
            {
                log.error("ERR-430: Price limit not found for deletion: {}", id);
                return ResponseEntity.notFound().build();
            }
            priceLimitService.deletePriceLimit(id);
            log.info("Successfully deleted price limit with: {}", id);
            return ResponseEntity.ok().build();
        }
        catch (Exception e)
        {
            log.error("ERR-432: Error deleting price limit: {}", id, e);
            return ResponseEntity.internalServerError().build();
        }
        finally
        {
            MDC.remove("errorId");
        }
    }
}
