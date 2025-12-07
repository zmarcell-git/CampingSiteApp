package Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import Model.CampingSite;
import Model.Guest;
import Model.ISearch;
import Model.Reservation;
import Service.CampingSiteManager;

public class ReservationManager implements ISearch {
    private List<Reservation> reservations;
    private CampingSiteManager campingSiteManager;

    public ReservationManager(CampingSiteManager campingSiteManager) {
        this.reservations = new ArrayList<>();
        this.campingSiteManager = campingSiteManager;
    }
    
    public void createReservation(LocalDate arrival, LocalDate departure, int guestNumber, Guest guest, CampingSite campingSite) {
        if (campingSite == null) {
            System.out.println("Finding available camping site...");
            campingSite = findAvailableCampingSite(arrival, departure, guestNumber);
        }
        
        if (departure.isBefore(arrival) || departure.isEqual(arrival)) {
            System.out.println("Error: Departure date must be after arrival date.");
            return;
        }

        if (campingSite == null) {
            System.out.println("No available camping site found for the given criteria.");
            return;
        }

        // The availability check is now correctly handled by findAvailableCampingSite
        Reservation reservation = new Reservation(arrival, departure, guestNumber, guest, campingSite, "Confirmed");
        reservations.add(reservation);
        System.out.println("Reservation created successfully for site: " + campingSite.getId());
    }

    public void modifyReservation(String reservationId, LocalDate newArrival, LocalDate newDeparture, int newGuestNumber, Guest guest) {
        try {
            for (Reservation reservation : reservations) {
                if (reservation.getId().equals(reservationId) && reservation.getGuest().equals(guest)) {
                    reservation.setArrival(newArrival);
                    // TODO: Add validation to ensure the new dates don't conflict with other reservations.
                    reservation.setDeparture(newDeparture);
                    reservation.setGuestsNumber(newGuestNumber);
                    System.out.println("Reservation modified successfully.");
                    break;
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Reservation not found: " + e.getMessage());
        }
    }

    public void deleteReservation(String reservationId, Guest guest) {
        boolean removed = reservations.removeIf(reservation -> reservation.getId().equals(reservationId) && reservation.getGuest().equals(guest));
        if (!removed) {
            System.out.println("Reservation not found for deletion.");
        } else {
            System.out.println("Reservation deleted successfully.");
        }
    }

    private boolean datesOverlap(String siteId, LocalDate start, LocalDate end) {
        return reservations.stream()
            .filter(res -> res.getCampingSite() != null && res.getCampingSite().getId().equals(siteId))
            .anyMatch(res -> start.isBefore(res.getDeparture()) && end.isAfter(res.getArrival()));
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
