package Model;

import java.time.LocalDate;

public class Reservation {
    private LocalDate arrival;
    private LocalDate departure;
    private int guestNumber;
    private String id;
    private Guest guest;
    private CampingSite campingSite;
    private String status;
    

    public Reservation(LocalDate arrival, LocalDate departure, int guestNumber, String id, Guest guest, CampingSite campingSite, String status) {
        this.arrival = arrival;
        this.departure = departure;
        this.guestNumber = guestNumber;
        this.id = id;
        this.guest = guest;
        this.campingSite = campingSite;
        this.status = status;
    }

    public double calculatePrice() {
        int capacity = campingSite.getCapacity();
        int intervallum = departure.getDayOfYear() - arrival.getDayOfYear();
        double price = capacity * guestNumber * intervallum;
        return price;
    }

    // Getters és Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public LocalDate getArrival() { return arrival; }
    public void setArrival(LocalDate arrival) { this.arrival = arrival; }

    public LocalDate getDeparture() { return departure; }
    public void setDeparture(LocalDate departure) { this.departure = departure; }

    public int getGuestsNumber() { return guestNumber; }
    public void setGuestsNumber(int guestNumber) { this.guestNumber = guestNumber; }

    public Guest getGuest() { return guest; }
    public void setGuest(Guest guest) { this.guest = guest; }

    public CampingSite getCampingSite() { return campingSite; }
    public void setCampingSite(CampingSite campingSite) { this.campingSite = campingSite; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}