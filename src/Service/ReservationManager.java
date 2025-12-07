package Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import Model.CampingSite;
import Model.Guest;
import Model.ISearch;
import Model.Reservation;


public class ReservationManager implements ISearch {
    private List<Reservation> reservations;
    private CampingSiteManager campingSiteManager;

    public ReservationManager(CampingSiteManager campingSiteManager) {
        this.reservations = new ArrayList<>();
        this.campingSiteManager = campingSiteManager;
    }
    
    public void createReservation(LocalDate arrival, LocalDate departure, int guestNumber, Guest guest, CampingSite campingSite) throws Exception {
        if (campingSite == null) {
            System.out.println("Finding available camping site...");
            campingSite = findAvailableCampingSite(arrival, departure, guestNumber);
        }
        if (campingSite == null) {
            throw new Exception("No available camping site found for the given criteria.");
        }
        if (reservationValidation(arrival, departure, guestNumber, campingSite)){
            Reservation reservation = new Reservation(arrival, departure, guestNumber, guest, campingSite, "Confirmed");
            reservations.add(reservation);
            System.out.println("Reservation created successfully for site: " + campingSite.getId());
            System.out.printf("Total Price: %, .2f Ft\n", reservation.calculatePrice());
        }
    }

    public boolean reservationValidation(LocalDate arrival, LocalDate departure, int guestNumber, CampingSite campingSite) throws Exception { 
        if (departure.isBefore(arrival) || departure.isEqual(arrival)) {
            throw new Exception("Error: Departure date must be after arrival date.");
        }
        if (guestNumber <= 0) {
            throw new Exception("Error: Number of guests must be greater than zero.");
        }
        if (guestNumber > campingSite.getCapacity()) {
            throw new Exception("Error: Number of guests exceeds camping site capacity.");
        }
        return true;
    }

    public void modifyReservation(String reservationId, LocalDate newArrival, LocalDate newDeparture, int newGuestNumber, Guest guest) throws Exception {
        Reservation reservationToModify = reservations.stream()
                .filter(r -> r.getId().equals(reservationId) && r.getGuest().equals(guest))
                .findFirst()
                .orElseThrow(() -> new Exception("Reservation not found or you do not have permission to modify it."));

        CampingSite originalSite = reservationToModify.getCampingSite();
        reservationValidation(newArrival, newDeparture, newGuestNumber, originalSite);

        // Check for availability on the new dates, excluding the current reservation from the check.
        boolean isOverlapping = reservations.stream()
            .filter(res -> !res.getId().equals(reservationId)) // Exclude the reservation being modified
            .filter(res -> res.getCampingSite() != null && res.getCampingSite().getId().equals(originalSite.getId()))
            .anyMatch(res -> newArrival.isBefore(res.getDeparture()) && newDeparture.isAfter(res.getArrival()));

        if (isOverlapping) {
            System.out.println("\nModification failed: The camping site #" + originalSite.getId() + " is not available for the selected dates.");
            System.out.println("Here are some suggestions:");

            // Suggestion 1: Alternative camping place for the same dates.
            CampingSite alternativeSite = findAvailableCampingSite(newArrival, newDeparture, newGuestNumber);
            if (alternativeSite != null) {
                System.out.println("- Suggestion 1: Book a different site for the same dates.");
                System.out.println("  An alternative available site is: #" + alternativeSite.getId());
            } else {
                System.out.println("- No other camping sites are available for your selected dates.");
            }

            // Suggestion 2: Alternative interval for the same camping place.
            long duration = java.time.temporal.ChronoUnit.DAYS.between(newArrival, newDeparture);
            LocalDate nextAvailableArrival = findNextAvailableDate(originalSite, newArrival, duration, reservationId);
            if (nextAvailableArrival != null) {
                LocalDate nextAvailableDeparture = nextAvailableArrival.plusDays(duration);
                System.out.println("- Suggestion 2: Book the same site for a different time.");
                System.out.println("  The next available slot for site #" + originalSite.getId() + " is from " + nextAvailableArrival + " to " + nextAvailableDeparture);
            }
            
            throw new Exception("Booking conflict. Please see suggestions above and try again.");
        } else {
            reservationToModify.setArrival(newArrival);
            reservationToModify.setDeparture(newDeparture);
            reservationToModify.setGuestsNumber(newGuestNumber);
            System.out.println("Reservation modified successfully.");
            System.out.printf("New Total Price: %, .2f Ft\n", reservationToModify.calculatePrice());
        }
    }

    public void deleteReservation(String reservationId, Guest guest) throws Exception {
        boolean removed = reservations.removeIf(reservation -> reservation.getId().equals(reservationId) && reservation.getGuest().equals(guest));
        if (!removed) {
            throw new Exception("Reservation not found for deletion, or you do not have permission.");
        } else {
            System.out.println("Reservation deleted successfully.");
        }
    }

    private boolean datesOverlap(String siteId, LocalDate start, LocalDate end) {
        return reservations.stream()
            .filter(res -> res.getCampingSite() != null && res.getCampingSite().getId().equals(siteId))
            .anyMatch(res -> start.isBefore(res.getDeparture()) && end.isAfter(res.getArrival()));
    }

    private LocalDate findNextAvailableDate(CampingSite site, LocalDate afterDate, long durationInDays, String reservationIdToIgnore) {
        LocalDate potentialArrival = afterDate;
        LocalDate searchLimit = afterDate.plusYears(1); // Search up to one year in the future
    
        while (potentialArrival.isBefore(searchLimit)) {
            LocalDate potentialDeparture = potentialArrival.plusDays(durationInDays);
            
            // Check for overlaps, ignoring the reservation we are modifying
            final LocalDate finalPotentialArrival = potentialArrival;
            boolean overlaps = reservations.stream()
                    .filter(res -> !res.getId().equals(reservationIdToIgnore))
                    .filter(res -> res.getCampingSite() != null && res.getCampingSite().getId().equals(site.getId()))
                    .anyMatch(res -> finalPotentialArrival.isBefore(res.getDeparture()) && potentialDeparture.isAfter(res.getArrival()));
    
            if (!overlaps) {
                return potentialArrival; // Found an available slot
            }
    
            // If there's an overlap, jump to the end of the conflicting reservation to optimize the search
            potentialArrival = reservations.stream()
                    .filter(res -> !res.getId().equals(reservationIdToIgnore))
                    .filter(res -> res.getCampingSite() != null && res.getCampingSite().getId().equals(site.getId()))
                    .filter(res -> finalPotentialArrival.isBefore(res.getDeparture()) && potentialDeparture.isAfter(res.getArrival()))
                    .map(Reservation::getDeparture)
                    .max(LocalDate::compareTo)
                    .orElse(potentialArrival.plusDays(1)); // Fallback
        }
        return null; // No available date found within the next year
    }

    public CampingSite findAvailableCampingSite(LocalDate arrival, LocalDate departure, int guestNumber) {
        List<CampingSite> allSites = campingSiteManager.getCampingSites();
        if (allSites == null) {
            return null;
        }

        for (CampingSite site : allSites) {
            if (site.getCapacity() >= guestNumber) {
                // Check if this site is available for the given dates.
                if (!datesOverlap(site.getId(), arrival, departure)) {
                    // This site is available!
                    return site;
                }
            }
        }
        return null; // No suitable site found
    }

    @Override
    public <T> List<T> search(Object criteria) {
        Reservation searchCriteria = (Reservation) criteria;

        @SuppressWarnings("unchecked")
        List<T> results = (List<T>) reservations.stream()
                .filter(reservation -> searchCriteria.getArrival() == null || searchCriteria.getArrival().equals(reservation.getArrival()))
                .filter(reservation -> searchCriteria.getDeparture() == null || searchCriteria.getDeparture().equals(reservation.getDeparture()))
                .filter(reservation -> searchCriteria.getGuestsNumber() == 0 || searchCriteria.getGuestsNumber() == reservation.getGuestsNumber())
                .filter(reservation -> searchCriteria.getId() == null || searchCriteria.getId().equals(reservation.getId()))
                .filter(reservation -> searchCriteria.getGuest() == null || searchCriteria.getGuest().equals(reservation.getGuest()))
                .filter(reservation -> searchCriteria.getCampingSite() == null || searchCriteria.getCampingSite().equals(reservation.getCampingSite()))
                .filter(reservation -> searchCriteria.getStatus() == null || searchCriteria.getStatus().equals(reservation.getStatus()))
                .collect(Collectors.toList());
         return results;
    }

    public void ReservationList() {
        // Implementation for listing reservations
        for (Reservation reservation : reservations) {
            System.out.println("Reservation ID: " + reservation.getId());
            System.out.println("Camping Site ID: " + reservation.getCampingSite().getId());
            System.out.println("Guest name: " + reservation.getGuest().getName());
            System.out.println("Guest Number: " + reservation.getGuestsNumber());
            System.out.println("Arrival Date: " + reservation.getArrival());
            System.out.println("Departure Date: " + reservation.getDeparture());
            System.out.println("Status: " + reservation.getStatus() + "\n");
        }
    }
}
