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

/*    @Id
    private UUID id;

    @Column(name = "valid_until", nullable = false)
    private LocalDateTime validUntil;

    @OneToMany(mappedBy = "pricelist", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<Legs> legs;
}*/
@Id
private String id;  // Changed from UUID to String

    @Column(name = "valid_until", nullable = false)
    private OffsetDateTime validUntil;  // Changed from LocalDateTime to OffsetDateTime

    @OneToMany(mappedBy = "pricelist", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Legs> legs;
}