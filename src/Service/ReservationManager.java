package Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import Model.CampingSite;
import Model.Guest;
import Model.Reservation;

public class ReservationManager {
    private List<Reservation> reservations;

    public ReservationManager() {
        this.reservations = new ArrayList<>();
    }
    
    public void createReservation(LocalDate arrival, LocalDate departure, int guestNumber, String Id, Guest guest,CampingSite campingSite) {
        Reservation reservation = new Reservation(arrival, departure, guestNumber, Id, guest, campingSite);
        reservations.add(reservation);
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

    public void datesOverlap(LocalDate start, LocalDate end) {
        for (Reservation reservation : reservations) {
            if (start.isBefore(reservation.getDeparture()) && end.isAfter(reservation.getArrival())) {
                System.out.println("The given dates overlap with an existing reservation.");
                return;
            }
        }
        System.out.println("The given dates do not overlap with any existing reservations.");        
    }

    public void Search(String reservationId, Guest guest) {
        for (Reservation reservation : reservations) {
            if (reservation.getId().equals(reservationId) && reservation.getGuest().equals(guest)) {
                System.out.println("Reservation found: " + reservationId);
                return;
            }
        }
        System.out.println("Reservation not found: " + reservationId);
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
