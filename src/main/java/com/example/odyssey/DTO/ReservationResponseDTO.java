package com.example.odyssey.DTO;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ReservationResponseDTO {
    private String firstName;
    private String lastName;
    private String route;
    private BigDecimal quotedPrice;
    private Long travelTimeHours;
    private String companyName;
    private String flightStart;
    private String flightEnd;
    private BigDecimal totalPrice;

}
