package com.example.odyssey.DTO;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class LegDTO {
    private String id;
    private RouteInfoDTO routeInfo;
    private List<ProviderDTO> providers;

}
