package com.leon.controller;

import com.leon.model.DeskNotionalLimit;
import com.leon.service.DeskNotionalLimitService;
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

@RestController()
@RequestMapping("/limits/desk")
@RequiredArgsConstructor
public class DeskNotionalLimitController
{
    private static final Logger log = LoggerFactory.getLogger(DeskNotionalLimitController.class);

    @Autowired
    private final DeskNotionalLimitService deskNotionalLimitService;

    @CrossOrigin
    @PostMapping
    public ResponseEntity<DeskNotionalLimit> saveDeskNotionalLimit(@RequestBody DeskNotionalLimit deskNotionalLimit)
    {
        String errorId = UUID.randomUUID().toString();
        MDC.put("errorId", errorId);

        try
        {
            DeskNotionalLimit savedDesk = deskNotionalLimitService.saveDeskNotionalLimit(deskNotionalLimit);
            log.info("Successfully saved desk: {}", savedDesk.getDeskId());
            return ResponseEntity.status(HttpStatus.CREATED).body(savedDesk);
        }
        catch (Exception e)
        {
            log.error("ERR-412: Error saving desk", e);
            return ResponseEntity.internalServerError().build();
        }
        finally
        {
            MDC.remove("errorId");
        }
    }

    @CrossOrigin
    @GetMapping("/{id}")
    public ResponseEntity<DeskNotionalLimit> getDeskNotionalLimit(@NotNull @PathVariable UUID id)
    {
        String errorId = UUID.randomUUID().toString();
        MDC.put("errorId", errorId);
        
        try
        {
            DeskNotionalLimit deskNotionalLimit = deskNotionalLimitService.getDeskNotionalLimit(id);
            if (deskNotionalLimit == null)
            {
                log.error("ERR-413: Desk notional limit not found with ID: {}", id);
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(deskNotionalLimit);
        }
        catch (Exception e)
        {
            log.error("ERR-414: Error retrieving desk notional limit with ID: {}", id, e);
            return ResponseEntity.internalServerError().build();
        }
        finally
        {
            MDC.remove("errorId");
        }
    }

    @CrossOrigin
    @GetMapping
    public ResponseEntity<List<DeskNotionalLimit>> getAllDesks()
    {
        String errorId = UUID.randomUUID().toString();
        MDC.put("errorId", errorId);
        
        try
        {
            return ResponseEntity.ok(deskNotionalLimitService.getAllDeskNotionalLimits());
        }
        catch (Exception e)
        {
            log.error("ERR-415: Error retrieving all desk notional limits", e);
            return ResponseEntity.internalServerError().build();
        }
        finally
        {
            MDC.remove("errorId");
        }
    }

    @CrossOrigin
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDesk(@NotNull @PathVariable UUID id)
    {
        String errorId = UUID.randomUUID().toString();
        MDC.put("errorId", errorId);
        
        try
        {
            if (!deskNotionalLimitService.deskNotionalLimitExists(id))
            {
                log.error("ERR-416: Desk notional limit not found for deletion: {}", id);
                return ResponseEntity.notFound().build();
            }
            deskNotionalLimitService.deleteDeskNotionalLimit(id);
            log.info("Successfully deleted desk notional limit with: {}", id);
            return ResponseEntity.ok().build();
        }
        catch (Exception e)
        {
            log.error("ERR-418: Error deleting desk notional limit: {}", id, e);
            return ResponseEntity.internalServerError().build();
        }
        finally
        {
            MDC.remove("errorId");
        }
    }
} 