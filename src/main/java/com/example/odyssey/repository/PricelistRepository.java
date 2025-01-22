package com.example.odyssey.repository;

import com.example.odyssey.model.Pricelist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PricelistRepository extends JpaRepository<Pricelist, String> {
}
