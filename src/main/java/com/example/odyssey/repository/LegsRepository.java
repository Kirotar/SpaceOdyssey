package com.example.odyssey.repository;

import com.example.odyssey.model.Legs;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LegsRepository extends JpaRepository<Legs, UUID> {
}
