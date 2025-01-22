package com.example.odyssey.DTO;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class PricelistDTO {
    private String id;
    private String validUntil;
    private List<LegDTO> legs;

}
