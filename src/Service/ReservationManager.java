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

    public ReservationManager() {
        this.reservations = new ArrayList<>();
    }
    
    public void createReservation(LocalDate arrival, LocalDate departure, int guestNumber, String Id, Guest guest,CampingSite campingSite) {
        if (!datesOverlap(arrival, departure)) {
            Reservation reservation = new Reservation(arrival, departure, guestNumber, Id, guest, campingSite);
            reservations.add(reservation);
        } else {
            System.out.println("The selected dates overlap with an existing reservation.");
        }
    }

    public void modifyReservation(String reservationId, LocalDate newArrival, LocalDate newDeparture, int newGuestNumber, Guest guest) {
        try {
            for (Reservation reservation : reservations) {
                if (reservation.getId().equals(reservationId) && reservation.getGuest().equals(guest)) {
                    reservation.setArrival(newArrival);
                    reservation.setDeparture(newDeparture);
                    reservation.setGuestsNumber(newGuestNumber);
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

    public boolean datesOverlap(LocalDate start, LocalDate end) {
        for (Reservation reservation : reservations) {
            if (start.isBefore(reservation.getDeparture()) && end.isAfter(reservation.getArrival())) {
                return true;
            }
        }     
        return false;
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
            System.out.println("Status: " + reservation.getCampingSite().getStatus() + "\n");
        }
    }
}
