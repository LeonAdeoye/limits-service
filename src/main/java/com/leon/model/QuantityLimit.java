package com.leon.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuantityLimit
{
    @Id
    private UUID exchangeId;
    private Long stockQuantityLimit;
    private Long etfQuantityLimit;
    private Long futureQuantityLimit;
    private Long optionQuantityLimit;
    private Long cryptoQuantityLimit;
}
