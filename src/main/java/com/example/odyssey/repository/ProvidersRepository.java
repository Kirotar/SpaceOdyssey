package com.example.odyssey.repository;

import com.example.odyssey.model.Providers;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProvidersRepository extends JpaRepository<Providers, UUID> {
}
