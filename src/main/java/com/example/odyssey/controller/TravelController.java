package com.example.odyssey.controller;

import com.example.odyssey.DTO.PricelistDTO;
import com.example.odyssey.DTO.ReservationDTO;
import com.example.odyssey.DTO.ReservationResponseDTO;
import com.example.odyssey.service.TravelService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000") // Your Vue app URL
public class TravelController {


    private final TravelService travelService;

    public TravelController(TravelService travelService) {
        this.travelService = travelService;
    }

    @GetMapping("/api/travel-prices")
    public ResponseEntity<?> getTravelPrices() {
      return travelService.getTravelPrices();
    }

    @PostMapping("/api/save-pricelist")
    public ResponseEntity<?> savePricelist(){
        return travelService.savePricelist();
    }

    @PostMapping("/api/make-reservation")
    public ResponseEntity<?> makeReservation(@RequestBody ReservationDTO reservationData){
        return travelService.makeReservation(reservationData);
    }

    @GetMapping("/api/get-reservation")
    public ResponseEntity<?>  getReservation(@RequestParam String firstName, @RequestParam String lastName){
        return travelService.getReservation(firstName, lastName);
    }
}