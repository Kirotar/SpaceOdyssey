package com.example.odyssey.controller;

import com.example.odyssey.model.BigPricelist;
import com.example.odyssey.model.Pricelist;
import com.example.odyssey.service.TravelService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@CrossOrigin(origins = "http://localhost:3000") // Your Vue app URL
public class TravelController {


    private final TravelService travelService;

    public TravelController(TravelService travelService) {
        this.travelService = travelService;
    }

    @GetMapping("/api/travel-prices")
    public ResponseEntity<?> getTravelPrices() {
        RestTemplate restTemplate = new RestTemplate();

        try {
            String apiUrl = "https://cosmosodyssey.azurewebsites.net/api/v1.0/TravelPrices";
            ResponseEntity<String> response = restTemplate.getForEntity(apiUrl, String.class);
            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching travel prices: " + e.getMessage());
        }
    }

    @PostMapping("/api/save-pricelist")
    public ResponseEntity<?> savePricelist(@RequestBody Pricelist priceListData){
        return travelService.savePricelist(priceListData);
    }

}