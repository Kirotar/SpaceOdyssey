package com.example.odyssey.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "bigpricelist")
public class BigPricelist {
    @Id
    @Column(name = "id", length = 36, nullable = false)
    private String id;

    @Column(name = "valid_until", nullable = false)  // Changed from validUntil
    private String validUntil;

    @Column(name = "leg_id", length = 36, nullable = false)  // Changed from legId
    private String legId;

    @Column(name = "route_id", length = 36, nullable = false)  // Changed from routeId
    private String routeId;

    @Column(name = "from_location_id", length = 36, nullable = false)  // Changed from fromLocationId
    private String fromLocationId;

    @Column(name = "from_location_name", nullable = false, length = 255)  // Changed from fromLocationName
    private String fromLocationName;

    @Column(name = "to_location_id", length = 36, nullable = false)  // Changed from toLocationId
    private String toLocationId;

    @Column(name = "to_location_name", nullable = false, length = 255)  // Changed from toLocationName
    private String toLocationName;

    @Column(name = "distance", nullable = false)
    private Long distance;

    @Column(name = "provider_id", length = 36, nullable = false)  // Changed from providerId
    private String providerId;

    @Column(name = "provider_name", nullable = false, length = 255)  // Changed from providerName
    private String providerName;

    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "flight_start", nullable = false)  // Changed from flightStart
    private LocalDateTime flightStart;

    @Column(name = "flight_end", nullable = false)  // Changed from flightEnd
    private LocalDateTime flightEnd;

    @Column(name = "company_id", length = 36, nullable = false)  // Changed from companyId
    private String companyId;

    @Column(name = "company_name", nullable = false, length = 255)  // Changed from companyName
    private String companyName;
}