package Model;

import java.time.LocalDate;

public class Reservation {
    private LocalDate arrival;
    private LocalDate departure;
    private int guestNumber;
    private String Id;
    private Guest guest;
    private CampingSite campingSite;
    

    public Reservation(LocalDate arrival, LocalDate departure, int guestNumber, String Id, Guest guest, CampingSite campingSite) {
        this.arrival = arrival;
        this.departure = departure;
        this.guestNumber = guestNumber;
        this.Id = Id;
        this.guest = guest;
        this.campingSite = campingSite;
    }

    public double calculatePrice() {
        int capacity = 0; // Need a getCapacity() method in CampingSite class
        int intervallum = departure.getDayOfYear() - arrival.getDayOfYear();
        double price = capacity * guestNumber * intervallum;
        return price;
    }

    // Getters és Setters
    public String getId() { return Id; }
    public void setId(String id) { this.Id = id; }

    public LocalDate getArrival() { return arrival; }
    public void setArrival(LocalDate arrival) { this.arrival = arrival; }

    public LocalDate getDeparture() { return departure; }
    public void setDeparture(LocalDate departure) { this.departure = departure; }

    public int getGuestsNumber() { return guestNumber; }
    public void setGuestsNumber(int guestsNumber) { this.guestNumber = guestsNumber; }

    public Guest getGuest() { return guest; }
    public void setGuest(Guest guest) { this.guest = guest; }

    public CampingSite getCampingSite() { return campingSite; }
    public void setCampingSite(CampingSite campingSite) { this.campingSite = campingSite; }    
}