package Service;

import java.util.ArrayList;
import java.util.List;
import Model.Reservation;

public class ReservationManager {
    private List<Reservation> reservations;

    public ReservationManager() {
        this.reservations = new ArrayList<>();
    }
    
    public void createReservation() {
        // Implementation for creating a reservation
    }

    public void modifyReservation() {
        // Implementation for modifying a reservation
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
        }
    }
}
