package com.example.odyssey.service;

import com.example.odyssey.DTO.LegDTO;
import com.example.odyssey.DTO.PricelistDTO;
import com.example.odyssey.DTO.ProviderDTO;
import com.example.odyssey.DTO.RouteInfoDTO;
import com.example.odyssey.model.Legs;
import com.example.odyssey.model.Pricelist;
import com.example.odyssey.model.Providers;
import com.example.odyssey.model.Routes;
import com.example.odyssey.repository.ProvidersRepository;
import com.example.odyssey.repository.LegsRepository;
import com.example.odyssey.repository.PricelistRepository;
import com.example.odyssey.repository.RoutesRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Transactional
public class TravelService {

    private final ProvidersRepository providersRepository;
    private final LegsRepository legsRepository;
    private final PricelistRepository pricelistRepository;
private final RoutesRepository routesRepository;

    public TravelService(ProvidersRepository providersRepository, LegsRepository legsRepository, PricelistRepository pricelistRepository, RoutesRepository routesRepository) {
        this.providersRepository = providersRepository;
        this.legsRepository = legsRepository;
        this.pricelistRepository = pricelistRepository;
        this.routesRepository = routesRepository;
    }

    public ResponseEntity<?> savePricelist(PricelistDTO priceListData) {
        try {
            // 1. Save pricelist
            Pricelist pricelist = new Pricelist();
            pricelist.setId(UUID.fromString(priceListData.getId()));
            pricelist.setValidUntil(priceListData.getValidUntil());
            pricelistRepository.save(pricelist);

            // 2. Save legs
            for (LegDTO legData : priceListData.getLegs()) {
                Legs leg = new Legs();
                leg.setId(UUID.fromString(legData.getId())); // Convert string ID to UUID
                leg.setPricelist(pricelist); // Set pricelist relationship
                legsRepository.save(leg);

                // Save route for the leg
                RouteInfoDTO routeInfo = legData.getRouteInfo();
                Routes route = new Routes();
                route.setId(UUID.fromString(routeInfo.getId())); // Convert string ID to UUID
                route.setFromId(UUID.fromString(routeInfo.getFrom().getId())); // Convert string ID to UUID
                route.setFromName(routeInfo.getFrom().getName());
                route.setToId(UUID.fromString(routeInfo.getTo().getId())); // Convert string ID to UUID
                route.setToName(routeInfo.getTo().getName());
                route.setDistance(routeInfo.getDistance());
                route.setLegId(UUID.fromString(legData.getId())); // Associate with leg ID
                routesRepository.save(route);

                // 3. Save providers for each leg
                for (ProviderDTO providerData : legData.getProviders()) {
                    Providers provider = new Providers();
                    provider.setId(UUID.fromString(providerData.getId()));
                    provider.setLegId(leg.getId());
                    provider.setCompanyId(UUID.fromString(providerData.getCompany().getId()));
                    provider.setCompanyName(providerData.getCompany().getName());
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

    /*private void cleanupOldPricelists() {
        // Get IDs of pricelists to keep (latest 15)
        List<UUID> pricelistsToKeep = pricelistRepository
                .findTop15ByOrderByValidUntilDesc()
                .stream()
                .map(Pricelist::getId)
                .collect(Collectors.toList());

        // Delete older pricelists
        pricelistRepository.deleteByIdNotIn(pricelistsToKeep);*/
    }
