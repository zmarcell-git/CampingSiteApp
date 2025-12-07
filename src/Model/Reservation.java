package Model;

import java.time.LocalDate;

public class Reservation {
    private LocalDate arrival;
    private LocalDate departure;
    private int guestNumber;
    private String Id;
    private Guest guest;
    private CampingSite campingSite;

    public Reservation(LocalDate arrival, LocalDate departure, int guestNumber, String Id, Guest guest,
            CampingSite campingSite) {
        this.arrival = arrival;
        this.departure = departure;
        this.guestNumber = guestNumber;
        this.Id = Id;
        this.guest = guest;
        this.campingSite = campingSite;
    }

    public double calculatePrice() {
        // Placeholder for price calculation logic
        return 0.0;
    }

    // Getters és Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDate getArrival() {
        return arrival;
    }

    public void setArrival(LocalDate arrival) {
        this.arrival = arrival;
    }

    public LocalDate getDeparture() {
        return departure;
    }

    public void setDeparture(LocalDate departure) {
        this.departure = departure;
    }

    public int getGuestsNumber() {
        return guestsNumber;
    }

    public void setGuestsNumber(int guestsNumber) {
        this.guestsNumber = guestsNumber;
    }

    public Guest getGuest() {
        return guest;
    }

    public void setGuest(Guest guest) {
        this.guest = guest;
    }

    public CampingSite getCampingSite() {
        return campingSite;
    }

    public void setCampingSite(CampingSite campingSite) {
        this.campingSite = campingSite;
    }
}
