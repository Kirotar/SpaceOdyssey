package com.example.odyssey.DTO;

import lombok.Data;

import java.util.UUID;

@Data
public class RouteInfoDTO {
    private String id;
    private LocationDTO from;
    private LocationDTO to;
    private Long distance;
}
