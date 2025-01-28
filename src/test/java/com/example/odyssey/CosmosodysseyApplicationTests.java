package com.example.odyssey;

import com.example.odyssey.DTO.*;
import com.example.odyssey.model.*;
import com.example.odyssey.repository.*;
import com.example.odyssey.service.TravelService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class TravelServiceTest {

    @Mock
    private ProvidersRepository providersRepository;

    @Mock
    private LegsRepository legsRepository;

    @Mock
    private PricelistRepository pricelistRepository;

    @Mock
    private RoutesRepository routesRepository;

    @Mock
    private ReservationsRepository reservationsRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private TravelService travelService;

    @BeforeEach
    void setUp() throws Exception {
        AutoCloseable autoCloseable = MockitoAnnotations.openMocks(this);
    }
    @Test
    void getTravelPrices_Success() {
        // Arrange
        // Arrange
        when(restTemplate.getForEntity(anyString(), eq(String.class)))
                .thenReturn(new ResponseEntity<>("any response", HttpStatus.OK));

        // Act
        ResponseEntity<?> response = travelService.getTravelPrices();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void getTravelPrices_Error() {
        // Arrange
        when(restTemplate.getForEntity(anyString(), eq(String.class)))
                .thenThrow(new RuntimeException("API Error"));

        // Act
        ResponseEntity<?> response = travelService.getTravelPrices();

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        String errorMessage = response.getBody().toString();
        assertTrue(errorMessage.contains("Error fetching travel prices"));
    }

    @Test
    void savePricelist_Success() {
        // Arrange
        PricelistDTO mockPricelistDTO = createMockPricelistDTO();
        when(restTemplate.getForEntity(anyString(), eq(PricelistDTO.class)))
                .thenReturn(new ResponseEntity<>(mockPricelistDTO, HttpStatus.OK));

        // Act
        ResponseEntity<?> response = travelService.savePricelist();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(pricelistRepository).save(any(Pricelist.class));
        verify(legsRepository).saveAll(anyList());
        verify(routesRepository).saveAll(anyList());
        verify(providersRepository).saveAll(anyList());
    }

    @Test
    void makeReservation_Success() {
        // Arrange
        ReservationDTO reservationDTO = new ReservationDTO();
        reservationDTO.setProviderId(UUID.randomUUID().toString());
        reservationDTO.setRouteId(UUID.randomUUID().toString());
        reservationDTO.setFirstName("John");
        reservationDTO.setLastName("Doe");

        Providers mockProvider = new Providers();
        Routes mockRoute = new Routes();

        when(providersRepository.findById(any(UUID.class)))
                .thenReturn(Optional.of(mockProvider));
        when(routesRepository.findById(any(UUID.class)))
                .thenReturn(Optional.of(mockRoute));

        // Act
        ResponseEntity<?> response = travelService.makeReservation(reservationDTO);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(reservationsRepository).save(any(Reservations.class));
    }

    @Test
    void getReservation_Success() {
        // Arrange
        String firstName = "John";
        String lastName = "Doe";

        Reservations mockReservation = createMockReservation();
        List<Reservations> mockReservations = Collections.singletonList(mockReservation);

        when(reservationsRepository.findByFirstNameIgnoreCaseAndLastNameIgnoreCase(firstName, lastName))
                .thenReturn(mockReservations);
        when(providersRepository.travelTime())
                .thenReturn(Collections.singletonList(new Object[]{UUID.randomUUID(), 10L}));

        // Act
        ResponseEntity<?> response = travelService.getReservation(firstName, lastName);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    private PricelistDTO createMockPricelistDTO() {
        PricelistDTO dto = new PricelistDTO();
        dto.setId(UUID.randomUUID().toString());
        dto.setValidUntil(LocalDateTime.now().plusDays(1).toString());
        dto.setLegs(Collections.singletonList(createMockLegDTO()));
        return dto;
    }

    private LegDTO createMockLegDTO() {
        LegDTO legDTO = new LegDTO();
        legDTO.setId(UUID.randomUUID().toString());
        legDTO.setRouteInfo(createMockRouteInfoDTO());
        legDTO.setProviders(Collections.singletonList(createMockProviderDTO()));
        return legDTO;
    }

    private RouteInfoDTO createMockRouteInfoDTO() {
        RouteInfoDTO routeInfoDTO = new RouteInfoDTO();
        routeInfoDTO.setId(UUID.randomUUID().toString());

        LocationDTO fromLocation = new LocationDTO();
        fromLocation.setId(UUID.randomUUID().toString());
        fromLocation.setName("City A");

        LocationDTO toLocation = new LocationDTO();
        toLocation.setId(UUID.randomUUID().toString());
        toLocation.setName("City B");

        routeInfoDTO.setFrom(fromLocation);
        routeInfoDTO.setTo(toLocation);
        routeInfoDTO.setDistance(100L);
        return routeInfoDTO;
    }

    private ProviderDTO createMockProviderDTO() {
        ProviderDTO providerDTO = new ProviderDTO();
        providerDTO.setId(UUID.randomUUID().toString());

        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setId(UUID.randomUUID().toString());
        companyDTO.setName("Test Company");

        providerDTO.setCompany(companyDTO);
        providerDTO.setPrice(new BigDecimal("100.00"));
        providerDTO.setFlightStart(LocalDateTime.now().toString());
        providerDTO.setFlightEnd(LocalDateTime.now().plusHours(2).toString());
        return providerDTO;
    }

    private Reservations createMockReservation() {
        Reservations reservation = new Reservations();
        reservation.setFirstName("John");
        reservation.setLastName("Doe");

        Providers provider = new Providers();
        provider.setId(UUID.randomUUID());
        provider.setCompanyName("Test Company");
        provider.setPrice(new BigDecimal("100.00"));
        provider.setFlightStart(LocalDateTime.now().toString());
        provider.setFlightEnd(LocalDateTime.now().plusHours(2).toString());

        Routes route = new Routes();
        route.setFromName("City A");
        route.setToName("City B");

        reservation.setProvider(provider);
        reservation.setRoute(route);

        return reservation;
    }
}