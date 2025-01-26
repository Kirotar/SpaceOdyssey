package com.example.odyssey.service;

import com.example.odyssey.DTO.*;
import com.example.odyssey.model.*;
import com.example.odyssey.repository.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;


@Service
@Transactional
public class TravelService {

    private final ProvidersRepository providersRepository;
    private final LegsRepository legsRepository;
    private final PricelistRepository pricelistRepository;
    private final RoutesRepository routesRepository;
    private final ReservationsRepository reservationsRepository;
    private static final String API_URL = "https://cosmosodyssey.azurewebsites.net/api/v1.0/TravelPrices";

    public TravelService(ProvidersRepository providersRepository, LegsRepository legsRepository, PricelistRepository pricelistRepository, RoutesRepository routesRepository, ReservationsRepository reservationsRepository) {
        this.providersRepository = providersRepository;
        this.legsRepository = legsRepository;
        this.pricelistRepository = pricelistRepository;
        this.routesRepository = routesRepository;
        this.reservationsRepository = reservationsRepository;
    }

    public ResponseEntity<?> getTravelPrices() {
        RestTemplate restTemplate = new RestTemplate();

        try {
            ResponseEntity<String> response = restTemplate.getForEntity(API_URL, String.class);
            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching travel prices: " + e.getMessage());
        }
    }

    @Scheduled(fixedRate = 900000)
    public ResponseEntity<?> savePricelist() {
        RestTemplate restTemplate = new RestTemplate();

        try {
            ResponseEntity<PricelistDTO> response = restTemplate.getForEntity(API_URL, PricelistDTO.class);
            PricelistDTO priceListData = response.getBody();


            if (priceListData == null || priceListData.getId() == null) {
                return ResponseEntity.badRequest().body("Invalid pricelist data");
            }

            // 1. Save pricelist
            Pricelist pricelist = new Pricelist();
            pricelist.setId(UUID.fromString(priceListData.getId()));
            pricelist.setValidUntil(priceListData.getValidUntil());
            pricelistRepository.save(pricelist);

            // 2. Prepare collections for batch save
            List<Legs> legsToSave = new ArrayList<>();
            List<Routes> routesToSave = new ArrayList<>();
            List<Providers> providersToSave = new ArrayList<>();

            for (LegDTO legData : priceListData.getLegs()) {
                // Create leg
                Legs leg = new Legs();
                leg.setId(UUID.fromString(legData.getId()));
                leg.setPricelist(pricelist);
                legsToSave.add(leg);

                // Create route
                RouteInfoDTO routeInfo = legData.getRouteInfo();
                Routes route = new Routes();
                route.setId(UUID.fromString(routeInfo.getId()));
                route.setFromId(UUID.fromString(routeInfo.getFrom().getId()));
                route.setFromName(routeInfo.getFrom().getName());
                route.setToId(UUID.fromString(routeInfo.getTo().getId()));
                route.setToName(routeInfo.getTo().getName());
                route.setDistance(routeInfo.getDistance());
                route.setLegId(UUID.fromString(legData.getId()));
                routesToSave.add(route);

                // Create providers
                for (ProviderDTO providerData : legData.getProviders()) {
                    Providers provider = new Providers();
                    provider.setId(UUID.fromString(providerData.getId()));
                    provider.setLegId(leg.getId());
                    provider.setCompanyId(UUID.fromString(providerData.getCompany().getId()));
                    provider.setCompanyName(providerData.getCompany().getName());
                    provider.setPrice(providerData.getPrice());
                    provider.setFlightStart(providerData.getFlightStart());
                    provider.setFlightEnd(providerData.getFlightEnd());
                    providersToSave.add(provider);
                }
            }

            // 3. Batch save
            legsRepository.saveAll(legsToSave);
            routesRepository.saveAll(routesToSave);
            providersRepository.saveAll(providersToSave);


            // 4. Clean up old pricelists (keep only last 15)
            cleanupOldPricelists();

            return ResponseEntity.ok().body("Pricelist saved successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error saving pricelist: " + e.getMessage());
        }
    }

    private void cleanupOldPricelists() {
        // Get IDs of pricelists to keep (latest 15)
        List<UUID> pricelistsToKeep = pricelistRepository
                .findTop15ByOrderByValidUntilDesc()
                .stream()
                .map(Pricelist::getId)
                .collect(Collectors.toList());

        // Delete older pricelists
        pricelistRepository.deleteByIdNotIn(pricelistsToKeep);
    }

    public ResponseEntity<?> makeReservation(ReservationDTO reservationData) {


        try {
            // Find provider
            Providers provider = providersRepository.findById(UUID.fromString(reservationData.getProviderId()))
                    .orElse(null);

            if (provider == null) {
                savePricelist();  // Update pricelist if provider not found
                provider = providersRepository.findById(UUID.fromString(reservationData.getProviderId()))
                        .orElseThrow(() -> new EntityNotFoundException("Provider not found"));
            }

            // Find route
            Routes route = routesRepository.findById(UUID.fromString(reservationData.getRouteId()))
                    .orElseThrow(() -> new EntityNotFoundException("Route not found"));

            // Create reservation
            Reservations reservation = new Reservations();
            reservation.setFirstName(reservationData.getFirstName());
            reservation.setLastName(reservationData.getLastName());
            reservation.setProvider(provider);
            reservation.setRoute(route);

            reservationsRepository.save(reservation);

            return ResponseEntity.ok().body("Reservation made successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error making reservation: " + e.getMessage());
        }

    }

   public ResponseEntity<?> getReservation(String firstName, String lastName) {

       try {
           List<Reservations> reservations = reservationsRepository.findByFirstNameAndLastName(firstName, lastName);
           List<Object[]> travelTimes = providersRepository.travelTime();

           Map<UUID, Long> travelTimeMap = new HashMap<>();
           for (Object[] result : travelTimes) {
               UUID id = (UUID) result[0];
               Long hours = ((Number) result[1]).longValue();
               travelTimeMap.put(id, hours);
           }

           if (reservations.isEmpty()) {
               return ResponseEntity.notFound().build();
           }
           List<ReservationResponseDTO> responseList = reservations.stream()
                   .map(reservation -> {
                       ReservationResponseDTO dto = new ReservationResponseDTO();
                       dto.setFirstName(reservation.getFirstName());
                       dto.setLastName(reservation.getLastName());
                       dto.setRoute(reservation.getRoute().getFromName() + " to " + reservation.getRoute().getToName());
                       dto.setFlightStart(reservation.getProvider().getFlightStart());
                       dto.setFlightEnd(reservation.getProvider().getFlightEnd());
                       dto.setQuotedPrice(reservation.getProvider().getPrice());
                       dto.setTravelTimeHours(travelTimeMap.get(reservation.getProvider().getId()));
                       dto.setCompanyName(reservation.getProvider().getCompanyName());
                       return dto;
                   })
                   .collect(Collectors.toList());

           return ResponseEntity.ok(responseList);
       } catch (Exception e) {
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                   .body("Error retrieving reservation: " + e.getMessage());
       }
   }

}
