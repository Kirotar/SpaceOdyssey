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
    private String id;  // Changed from UUID to String

    @Column(name = "from_id", nullable = false)
    private String fromId;  // Changed from UUID to String

    @Column(name = "from_name", nullable = false)
    private String fromName;

    @Column(name = "to_id", nullable = false)
    private String toId;  // Changed from UUID to String

    @Column(name = "to_name", nullable = false)
    private String toName;

    @Column(nullable = false)
    private Long distance;

    @ManyToOne
    @JoinColumn(name = "pricelist_id", nullable = false)
    private Pricelist pricelist;

    @OneToMany(mappedBy = "leg", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Providers> providers;
}
/*
    @Id
    private UUID id;

    @Column(name = "from_id", nullable = false)
    private UUID fromId;

    @Column(name = "from_name", nullable = false)
    private String fromName;

    @Column(name = "to_id", nullable = false)
    private UUID toId;

    @Column(name = "to_name", nullable = false)
    private String toName;

    @Column(nullable = false)
    private Long distance;

    @ManyToOne
    @JoinColumn(name = "pricelist_id", nullable = false)
    private Pricelist pricelist;

    @OneToMany(mappedBy = "leg", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<Providers> providers;
}
*/
