import Model.CampingSite;
import Model.Guest;
import Model.Reservation;
import Service.CampingSiteManager;
import Service.ReservationManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// This version of the test does not use Mockito.
// It tests the integration between ReservationManager and a real CampingSiteManager.
public class ReservationManagerTest {

    // We will use real instances of the managers instead of mocks.
    private CampingSiteManager campingSiteManager;
    private ReservationManager reservationManager;

    // For capturing console output from ReservationList()
    private final PrintStream originalSystemOut = System.out;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    // Common test data
    private Guest guest;
    private CampingSite site;
    private LocalDate arrival;
    private LocalDate departure;

    @BeforeEach
    void setUp() {
        // Redirect System.out to capture console output
        System.setOut(new PrintStream(outContent));

        // Instantiate the real managers for each test. This ensures a clean state.
        campingSiteManager = new CampingSiteManager();
        reservationManager = new ReservationManager(campingSiteManager);

        // Setup common test data.
        guest = new Guest("Test Guest");
        site = new CampingSite("TENT", 4, 50.0);
        // Add a default site to the manager. This assumes CampingSiteManager has a method to add sites.
        // If not, you might need to add: public void addCampingSite(CampingSite site) { this.campingSites.add(site); }
        campingSiteManager.addCampingSite(site);
        arrival = LocalDate.now().plusDays(10);
        departure = LocalDate.now().plusDays(15);
    }

    @AfterEach
    void tearDown() {
        // Restore original System.out
        System.setOut(originalSystemOut);
    }

    // --- createReservation Tests ---

    @Test
    void createReservation_Success_WithSpecificSite() throws Exception {
        // Arrange: The 'site' is added to the campingSiteManager in the setUp() method,
        // so it is available by default for a new reservation.

        // Act: Create the reservation
        Reservation reservation = reservationManager.createReservation(arrival, departure, 2, guest, site);

        // Assert: Check that the reservation was created correctly
        assertNotNull(reservation);
        assertEquals(guest, reservation.getGuest());
        assertEquals(site, reservation.getCampingSite());
        assertEquals(arrival, reservation.getArrival());
        assertEquals(departure, reservation.getDeparture());
        assertEquals(2, reservation.getGuestsNumber());
    }

    @Test
    void createReservation_Success_WithAutomaticSiteAssignment() throws Exception {
        // Arrange: The 'site' is added in setUp(), so findAvailableSite should find it
        // for the given criteria.

        // Act: Create reservation without specifying a site (pass null)
        Reservation reservation = reservationManager.createReservation(arrival, departure, 2, guest, null);

        // Assert: Check that a site was found and assigned
        assertNotNull(reservation);
        assertEquals(site, reservation.getCampingSite());
        assertEquals(guest, reservation.getGuest());
    }

    @Test
    void createReservation_Failure_WhenNoSiteAvailable() {
        // Arrange: For this test, we need a manager with no sites.
        // We create new, empty managers, overriding the ones from setUp().
        campingSiteManager = new CampingSiteManager();
        reservationManager = new ReservationManager(campingSiteManager);

        // Act & Assert: Expect an exception when no site can be automatically assigned
        Exception exception = assertThrows(Exception.class, () -> {
            reservationManager.createReservation(arrival, departure, 2, guest, null);
        });

        assertEquals("No available camping sites for the given criteria.", exception.getMessage());
    }

    @Test
    void createReservation_Failure_WhenSpecifiedSiteNotAvailable() {
        // Arrange: Make the site unavailable by creating a conflicting reservation first.
        assertDoesNotThrow(() -> reservationManager.createReservation(arrival, departure, 2, guest, site));

        // Act & Assert: Expect an exception for booking conflict
        Exception exception = assertThrows(Exception.class, () -> {
            // Try to book the same site for the same dates with another guest.
            reservationManager.createReservation(arrival, departure, 2, new Guest("Another Guest"), site);
        });

        assertTrue(exception.getMessage().startsWith("Booking conflict: The selected site is not available"));
    }

    @Test
    void createReservation_Failure_WithInvalidDates() {
        // Arrange: Departure date is before arrival date
        LocalDate invalidDeparture = arrival.minusDays(1);

        // Act & Assert: Expect an exception for invalid dates
        Exception exception = assertThrows(Exception.class, () -> {
            reservationManager.createReservation(arrival, invalidDeparture, 2, guest, site);
        });

        assertEquals("Departure date must be after arrival date.", exception.getMessage());
    }

    // --- modifyReservation Tests ---

    @Test
    void modifyReservation_Success() throws Exception {
        // Arrange: First, create a reservation to modify
        Reservation originalReservation = reservationManager.createReservation(arrival, departure, 2, guest, site);
        String reservationId = originalReservation.getId();

        LocalDate newArrival = arrival.plusDays(1);
        LocalDate newDeparture = departure.plusDays(1);

        // Act: Modify the reservation
        Reservation modifiedReservation = reservationManager.modifyReservation(reservationId, newArrival, newDeparture, 3, guest);

        // Assert: Check that the details were updated
        assertNotNull(modifiedReservation);
        assertEquals(reservationId, modifiedReservation.getId());
        assertEquals(newArrival, modifiedReservation.getArrival());
        assertEquals(newDeparture, modifiedReservation.getDeparture());
        assertEquals(3, modifiedReservation.getGuestsNumber());
    }

    @Test
    void modifyReservation_Failure_ReservationNotFound() {
        // Act & Assert: Expect an exception when trying to modify a non-existent reservation
        Exception exception = assertThrows(Exception.class, () -> {
            reservationManager.modifyReservation("non-existent-id", arrival, departure, 2, guest);
        });

        assertEquals("Reservation with ID 'non-existent-id' not found.", exception.getMessage());
    }

    @Test
    void modifyReservation_Failure_UserNotAuthorized() throws Exception {
        // Arrange: Create a reservation with 'guest'
        Reservation originalReservation = reservationManager.createReservation(arrival, departure, 2, guest, site);
        String reservationId = originalReservation.getId();

        // Create another guest who will try to modify it
        Guest anotherGuest = new Guest("Hacker");

        // Act & Assert: Expect an exception because 'anotherGuest' is not the owner
        Exception exception = assertThrows(Exception.class, () -> {
            reservationManager.modifyReservation(reservationId, arrival, departure, 2, anotherGuest);
        });

        assertEquals("You are not authorized to modify this reservation.", exception.getMessage());
    }

    // --- deleteReservation Tests ---

    @Test
    void deleteReservation_Success() throws Exception {
        // Arrange: Create a reservation to delete
        Reservation reservation = reservationManager.createReservation(arrival, departure, 2, guest, site);
        String reservationId = reservation.getId();

        // Act: Delete the reservation
        reservationManager.deleteReservation(reservationId, guest);

        // Assert: The reservation should no longer be found (e.g., trying to modify it fails)
        Exception exception = assertThrows(Exception.class, () -> {
            reservationManager.modifyReservation(reservationId, arrival, departure, 2, guest);
        });
        assertEquals("Reservation with ID '" + reservationId + "' not found.", exception.getMessage());
    }

    // --- search Tests ---

    @Test
    void search_ReturnsMatchingReservations() throws Exception {
        // Arrange: Create a couple of reservations with different criteria

        // Add a second site to avoid booking conflicts on the same dates
        CampingSite anotherSite = new CampingSite("RV", 6, 100.0);
        campingSiteManager.addCampingSite(anotherSite);

        reservationManager.createReservation(arrival, departure, 2, guest, site); // Matches guests=2
        reservationManager.createReservation(arrival.plusDays(20), departure.plusDays(25), 4, guest, anotherSite); // Matches guests=4

        // Create search criteria for reservations with 2 guests (assumes a constructor for searching)
        Reservation searchCriteria = new Reservation(null, null, 2, null, null, null);

        // Act: Perform the search
        List<Object> results = reservationManager.search(searchCriteria);

        // Assert: Should find exactly one reservation
        assertEquals(1, results.size());
        Reservation found = (Reservation) results.get(0);
        assertEquals(2, found.getGuestsNumber());
    }

    // --- ReservationList Tests ---

    @Test
    void reservationList_PrintsReservations_WhenListIsNotEmpty() throws Exception {
        // Arrange
        Reservation res1 = reservationManager.createReservation(arrival, departure, 2, guest, site);

        // Act
        reservationManager.ReservationList();

        // Assert
        String output = outContent.toString().replace(System.lineSeparator(), "\n");
        assertTrue(output.contains("--- All Reservations ---"), "Should contain the list header.");
        assertTrue(output.contains("Reservation ID: " + res1.getId()), "Should contain the reservation ID.");
        assertTrue(output.contains("Guest: Test Guest"), "Should contain the guest's name.");
    }

    @Test
    void reservationList_PrintsMessage_WhenNoReservations() {
        // Arrange: No reservations have been created

        // Act
        reservationManager.ReservationList();

        // Assert
        String output = outContent.toString();
        assertTrue(output.contains("No reservations found."), "Should print a 'not found' message.");
    }
}