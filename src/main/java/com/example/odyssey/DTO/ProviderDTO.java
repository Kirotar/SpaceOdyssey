package com.example.odyssey.DTO;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProviderDTO {
    private String id;
    private CompanyDTO company;
    private BigDecimal price;
    private String flightStart;
    private String flightEnd;
}
