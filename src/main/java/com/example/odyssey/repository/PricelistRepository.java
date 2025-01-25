package com.example.odyssey.repository;

import com.example.odyssey.model.Pricelist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PricelistRepository extends JpaRepository<Pricelist, UUID> {
    List<Pricelist> findTop15ByOrderByValidUntilDesc();
    void deleteByIdNotIn(List<UUID> ids);
}
