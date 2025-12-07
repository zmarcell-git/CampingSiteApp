package Model;

public abstract class User {
    private String id;
    private String name;
    private Reservation reservation;

    public User(String name) {
        this.name = name;
        this.id = "USER" + System.currentTimeMillis();
    }

    public String getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }

    public Reservation getReservation() {
        return reservation;
    }
    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }
}
