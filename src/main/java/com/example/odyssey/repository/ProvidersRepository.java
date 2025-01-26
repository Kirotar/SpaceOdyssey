package com.example.odyssey.repository;

import com.example.odyssey.model.Providers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ProvidersRepository extends JpaRepository<Providers, UUID> {
    @Query(value = "SELECT id, EXTRACT(EPOCH FROM (flight_end::timestamp - flight_start::timestamp))/3600 AS travel_time_hours FROM providers", nativeQuery = true)    List<Object[]> travelTime();
}
