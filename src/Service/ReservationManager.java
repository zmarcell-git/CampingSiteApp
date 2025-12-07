package Service;

import java.lang.reflect.Array;
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

    public void modifyReservation(String reservationId, LocalDate newArrival, LocalDate newDeparture, int newGuestNumber) {
        try {
            for (Reservation reservation : reservations) {
                if (reservation.getId().equals(reservationId)) {
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

    public void deleteReservation() {
        // Implementation for deleting a reservation
    }

    public void datesOverlap() {
        // Implementation for checking date overlaps
    }

    public void Search() {
        // Implementation for searching reservations
    }

    public void ReservationList() {
        // Implementation for listing reservations
        for (Reservation reservation : reservations) {
            System.out.println("Reservation ID: " + reservation.getId());
            System.out.println("Camping Site ID: "); // + reservation.getCampingSite().getId()
            System.out.println("Guest name: " + reservation.getGuest().getName());
            System.out.println("Guest Number: " + reservation.getGuestsNumber());
            System.out.println("Arrival Date: " + reservation.getArrival());
            System.out.println("Departure Date: " + reservation.getDeparture() + "\n");
            System.out.println("Status: "); // + reservation.getCampingSite().getStatus()    
        }
    }
}
