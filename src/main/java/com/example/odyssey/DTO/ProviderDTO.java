package com.example.odyssey.DTO;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class ProviderDTO {
    private String id;
    private CompanyDTO company;
    private BigDecimal price;
    private String flightStart;
    private String flightEnd;
}
