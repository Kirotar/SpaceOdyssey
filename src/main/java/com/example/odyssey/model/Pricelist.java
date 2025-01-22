package com.example.odyssey.model;
import jakarta.persistence.*;
import lombok.*;

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
@Table(name = "pricelist")
public class Pricelist {

    @Id
    private UUID id;

    @Column(name = "valid_until", nullable = false)
    private String validUntil;
}