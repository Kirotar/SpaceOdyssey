package com.example.odyssey.service;

import com.example.odyssey.model.BigPricelist;
import com.example.odyssey.model.Legs;
import com.example.odyssey.model.Pricelist;
import com.example.odyssey.model.Providers;
import com.example.odyssey.repository.BigpricelistRepository;
import com.example.odyssey.repository.ProvidersRepository;
import com.example.odyssey.repository.LegsRepository;
import com.example.odyssey.repository.PricelistRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class TravelService {

    private final ProvidersRepository providersRepository;
    private final LegsRepository legsRepository;
    private final PricelistRepository pricelistRepository;


    public TravelService(ProvidersRepository providersRepository, LegsRepository legsRepository, PricelistRepository pricelistRepository) {
        this.providersRepository = providersRepository;
        this.legsRepository = legsRepository;
        this.pricelistRepository = pricelistRepository;
    }


   /* public ResponseEntity<?> savePricelist(Pricelist priceListData) {
        try {
            pricelistRepository.save(priceListData);
            return ResponseEntity.ok().body("Pricelist saved successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error saving pricelist: " + e.getMessage());
        }
    }*/


    public ResponseEntity<?> savePricelist(Pricelist priceListData) {
        try {
            // 1. Validate input
            if (priceListData == null || priceListData.getId() == null) {
                return ResponseEntity.badRequest().body("Invalid pricelist data");
            }

            // 1. Save pricelist
            Pricelist pricelist = new Pricelist();
            pricelist.setId(priceListData.getId());
            pricelist.setValidUntil(priceListData.getValidUntil());
            pricelistRepository.save(pricelist);

            // 2. Save legs
            for (Legs legData : priceListData.getLegs()) {
                Legs leg = new Legs();
                leg.setId(legData.getId());
                leg.setPricelist(pricelist);

                leg.setFromId(legData.getFromId());
                leg.setFromName(legData.getFromName());
                leg.setToId(legData.getToId());
                leg.setToName(legData.getToName());
                leg.setDistance(legData.getDistance());

                legsRepository.save(leg);

                // 3. Save providers for each leg
                for (Providers providerData : legData.getProviders()) {
                    Providers provider = new Providers();
                    provider.setId(providerData.getId());
                    provider.setLeg(leg);
                    provider.setCompanyId(providerData.getCompanyId());
                    provider.setCompanyName(providerData.getCompanyName());
                    provider.setPrice(providerData.getPrice());
                    provider.setFlightStart(providerData.getFlightStart());
                    provider.setFlightEnd(providerData.getFlightEnd());
                    providersRepository.save(provider);
                }
            }

            // 4. Clean up old pricelists (keep only last 15)
            //cleanupOldPricelists();
            return ResponseEntity.ok().body("Pricelist saved successfully");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error saving pricelist: " + e.getMessage());
        }
    }
}
/*    private void cleanupOldPricelists() {
        // Get IDs of pricelists to keep (latest 15)
        List<UUID> pricelistsToKeep = pricelistRepository
                .findTop15ByOrderByValidUntilDesc()
                .stream()
                .map(Pricelist::getId)
                .collect(Collectors.toList());

        // Delete older pricelists
        pricelistRepository.deleteByIdNotIn(pricelistsToKeep);
    }*/

