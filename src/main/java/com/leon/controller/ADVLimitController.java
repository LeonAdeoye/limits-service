package com.leon.controller;

import com.leon.model.ADVLimit;
import com.leon.service.ADVLimitService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
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
@RequestMapping("/limits/adv")
public class ADVLimitController
{
    private static final Logger log = LoggerFactory.getLogger(ADVLimitController.class);

    @Autowired
    private ADVLimitService advLimitService;

    @CrossOrigin
    @PostMapping
    public ResponseEntity<ADVLimit> saveADVLimit(@RequestBody ADVLimit advLimit)
    {
        String errorId = UUID.randomUUID().toString();
        MDC.put("errorId", errorId);

        try
        {
            ADVLimit savedADV = advLimitService.saveADVLimit(advLimit);
            log.info("Successfully saved ADV: {}", savedADV.getAdvId());
            return ResponseEntity.status(HttpStatus.CREATED).body(savedADV);
        }
        catch (Exception e)
        {
            log.error("ERR-419: Error saving ADV", e);
            return ResponseEntity.internalServerError().build();
        }
        finally
        {
            MDC.remove("errorId");
        }
    }

    @CrossOrigin
    @GetMapping("/{id}")
    public ResponseEntity<ADVLimit> getADVLimit(@NotNull @PathVariable UUID id)
    {
        String errorId = UUID.randomUUID().toString();
        MDC.put("errorId", errorId);
        
        try
        {
            ADVLimit advLimit = advLimitService.getADVLimit(id);
            if (advLimit == null)
            {
                log.error("ERR-420: ADV limit not found with ID: {}", id);
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(advLimit);
        }
        catch (Exception e)
        {
            log.error("ERR-421: Error retrieving ADV limit with ID: {}", id, e);
            return ResponseEntity.internalServerError().build();
        }
        finally
        {
            MDC.remove("errorId");
        }
    }

    @CrossOrigin
    @GetMapping
    public ResponseEntity<List<ADVLimit>> getAllADVLimits()
    {
        String errorId = UUID.randomUUID().toString();
        MDC.put("errorId", errorId);
        
        try
        {
            return ResponseEntity.ok(advLimitService.getAllADVLimits());
        }
        catch (Exception e)
        {
            log.error("ERR-422: Error retrieving all ADV limits", e);
            return ResponseEntity.internalServerError().build();
        }
        finally
        {
            MDC.remove("errorId");
        }
    }

    @CrossOrigin
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteADVLimit(@NotNull @PathVariable UUID id)
    {
        String errorId = UUID.randomUUID().toString();
        MDC.put("errorId", errorId);
        
        try
        {
            if (!advLimitService.advLimitExists(id))
            {
                log.error("ERR-423: ADV limit not found for deletion: {}", id);
                return ResponseEntity.notFound().build();
            }
            advLimitService.deleteADVLimit(id);
            log.info("Successfully deleted ADV limit with: {}", id);
            return ResponseEntity.ok().build();
        }
        catch (Exception e)
        {
            log.error("ERR-425: Error deleting ADV limit: {}", id, e);
            return ResponseEntity.internalServerError().build();
        }
        finally
        {
            MDC.remove("errorId");
        }
    }
}
