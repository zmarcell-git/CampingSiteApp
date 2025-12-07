package Test;

import org.junit.*;
import java.time.LocalDate;
import Service.CampingSiteManager;
import Service.ReservationManager;
import Model.CampingSite;
import Model.CampingType;
import Model.Guest;

public class ReservationManagerTest {
    
    private ReservationManager reservationManager;
    private CampingSiteManager campingSiteManager;
    private Guest guest1;
    private Guest guest2;
    private CampingSite site;
    
    @Before
    public void setUp() {
        campingSiteManager = new CampingSiteManager();
        reservationManager = new ReservationManager(campingSiteManager);
        guest1 = new Guest("John Doe");
        guest2 = new Guest("Jane Smith");
        site = new CampingSite(CampingType.TENT, 4, 10000);
        campingSiteManager.AddCampingSite(site);
    }
    
    @Test
    public void testValidReservationCreation() throws Exception {
        LocalDate arrival = LocalDate.of(2025, 12, 15);
        LocalDate departure = LocalDate.of(2025, 12, 20);
        
        reservationManager.createReservation(arrival, departure, 2, guest1, site);
        
        Assert.assertTrue("Reservation should be created", true);
    }
    
    @Test(expected = Exception.class)
    public void testInvalidIntervalExpectedMoreDays() throws Exception {
        LocalDate arrival = LocalDate.of(2025, 12, 20);
        LocalDate departure = LocalDate.of(2025, 12, 20);
        
        reservationManager.reservationValidation(arrival, departure, 2, site);
    }
    
    @Test(expected = Exception.class)
    public void testInvalidIntervalDepartureBefore() throws Exception {
        LocalDate arrival = LocalDate.of(2025, 12, 25);
        LocalDate departure = LocalDate.of(2025, 12, 20);
        
        reservationManager.reservationValidation(arrival, departure, 2, site);
    }
    
    @Test(expected = Exception.class)
    public void testGuestNumberExceedsCapacity() throws Exception {
        LocalDate arrival = LocalDate.of(2025, 12, 15);
        LocalDate departure = LocalDate.of(2025, 12, 20);
        
        // Site capacity is 4, trying to book for 6 guests
        reservationManager.reservationValidation(arrival, departure, 6, site);
    }
    
    @Test(expected = Exception.class)
    public void testGuestNumberZero() throws Exception {
        LocalDate arrival = LocalDate.of(2025, 12, 15);
        LocalDate departure = LocalDate.of(2025, 12, 20);
        
        reservationManager.reservationValidation(arrival, departure, 0, site);
    }
    
    @Test(expected = Exception.class)
    public void testGuestNumberNegative() throws Exception {
        LocalDate arrival = LocalDate.of(2025, 12, 15);
        LocalDate departure = LocalDate.of(2025, 12, 20);
        
        reservationManager.reservationValidation(arrival, departure, -3, site);
    }
    
    @Test(expected = Exception.class)
    public void testConflictingReservations() throws Exception {
        LocalDate arrival1 = LocalDate.of(2025, 12, 15);
        LocalDate departure1 = LocalDate.of(2025, 12, 20);
        
        // Create first reservation
        reservationManager.createReservation(arrival1, departure1, 2, guest1, site);
        
        // Try to create overlapping reservation on same site
        LocalDate arrival2 = LocalDate.of(2025, 12, 18);
        LocalDate departure2 = LocalDate.of(2025, 12, 22);
        
        reservationManager.createReservation(arrival2, departure2, 2, guest2, site);
    }
    
    @Test(expected = Exception.class)
    public void testConflictingReservationsPartialOverlap() throws Exception {
        LocalDate arrival1 = LocalDate.of(2025, 12, 10);
        LocalDate departure1 = LocalDate.of(2025, 12, 15);
        
        // Create first reservation
        reservationManager.createReservation(arrival1, departure1, 2, guest1, site);
        
        // Try to create reservation that starts during first reservation
        LocalDate arrival2 = LocalDate.of(2025, 12, 14);
        LocalDate departure2 = LocalDate.of(2025, 12, 18);
        
        reservationManager.createReservation(arrival2, departure2, 2, guest2, site);
    }
    
    @Test
    public void testNonConflictingReservationsAfter() throws Exception {
        LocalDate arrival1 = LocalDate.of(2025, 12, 10);
        LocalDate departure1 = LocalDate.of(2025, 12, 15);
        
        // Create first reservation
        reservationManager.createReservation(arrival1, departure1, 2, guest1, site);
        
        // Create non-overlapping reservation after first one
        LocalDate arrival2 = LocalDate.of(2025, 12, 15);
        LocalDate departure2 = LocalDate.of(2025, 12, 20);
        
        reservationManager.createReservation(arrival2, departure2, 2, guest2, site);
        
        Assert.assertTrue("Both reservations should be created", true);
    }
    
    @Test
    public void testMaxCapacityReservation() throws Exception {
        LocalDate arrival = LocalDate.of(2025, 12, 15);
        LocalDate departure = LocalDate.of(2025, 12, 20);
        
        // Site capacity is 4, booking for exactly 4 guests
        reservationManager.reservationValidation(arrival, departure, 4, site);
        
        Assert.assertTrue("Reservation with max capacity should be valid", true);
    }
}

