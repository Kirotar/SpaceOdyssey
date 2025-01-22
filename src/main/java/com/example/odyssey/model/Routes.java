package com.example.odyssey.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "routes")
public class Routes {
    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "from_id", nullable = false)
    private UUID fromId;

    @Column(name = "from_name", nullable = false, length = 100)
    private String fromName;

    @Column(name = "to_id", nullable = false)
    private UUID toId;

    @Column(name = "to_name", nullable = false, length = 100)
    private String toName;

    @Column(name = "distance", nullable = false)
    private Long distance;

    @Column(name = "leg_id", nullable = false)
    private UUID legId;

}
