package com.leon.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ADVLimit
{
    @Id
    private UUID advId;
    private Double buyADVLimit;
    private Double sellADVLimit;
}
