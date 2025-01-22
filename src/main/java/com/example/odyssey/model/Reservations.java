package com.example.odyssey.model;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.Duration;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
@ToString
@Table(name = "reservations")
public class Reservations {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @ManyToOne
    @JoinColumn(name = "provider_id", nullable = false)
    private Providers provider;

    @ManyToOne
    @JoinColumn(name = "leg_id", nullable = false)
    private Legs leg;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

    @Column(name = "travel_time", nullable = false)
    private Duration travelTime;

    @Column(name = "company_name", nullable = false)
    private String companyName;


}
