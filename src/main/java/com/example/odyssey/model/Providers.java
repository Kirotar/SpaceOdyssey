package com.example.odyssey.model;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
@ToString
@Table(name = "providers")
public class Providers {
    @Id
    private UUID id;

    @Column(name = "leg_id", nullable = false)
    private UUID legId;

    @Column(name = "company_id", nullable = false)
    private UUID companyId;

    @Column(name = "company_name", nullable = false)
    private String companyName;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

    @Column(name = "flight_start", nullable = false)
    private String flightStart;

    @Column(name = "flight_end", nullable = false)
    private String flightEnd;
}