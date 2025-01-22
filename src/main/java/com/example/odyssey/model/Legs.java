package com.example.odyssey.model;
import jakarta.persistence.*;
import lombok.*;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
@ToString
@Table(name = "legs")
public class Legs {

    @Id
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "pricelist_id", nullable = false)
    private Pricelist pricelist;

}
