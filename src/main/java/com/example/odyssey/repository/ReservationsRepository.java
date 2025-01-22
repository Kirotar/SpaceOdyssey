package com.example.odyssey.repository;

import com.example.odyssey.model.Reservations;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationsRepository extends JpaRepository<Reservations, Long> {
}
