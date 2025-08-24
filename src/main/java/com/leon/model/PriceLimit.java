package com.leon.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PriceLimit
{
    @Id
    private UUID exchangeId;
    private Double stockPriceDifferenceLimit;
    private Double etfPriceDifferenceLimit;
    private Double futurePriceDifferenceLimit;
    private Double optionPriceDifferenceLimit;
    private Double cryptoPriceDifferenceLimit;
}
