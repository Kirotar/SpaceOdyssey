package com.example.odyssey.repository;

import com.example.odyssey.model.Routes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RoutesRepository extends JpaRepository<Routes, UUID> {
}
