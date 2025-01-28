package com.example.odyssey.repository;

import com.example.odyssey.model.Reservations;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationsRepository extends JpaRepository<Reservations, Long> {
    List<Reservations> findByFirstNameIgnoreCaseAndLastNameIgnoreCase(String firstName, String lastName);

}
