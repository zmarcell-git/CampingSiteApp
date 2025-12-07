package Model;

import java.util.ArrayList;

/**
 * The CampingSite class represents a camping spot.
 * 
 * Each camping site has a unique ID, type, capacity, price, amenities, and
 * status.
 */
public class CampingSite {
    private static int idCounter = 1;

    private String id;
    private CampingType type;
    private int capacity;
    private double price;
    private ArrayList<String> amenities = new ArrayList<String>(); // arraylast so it can grow
    private String status;

    /**
     * Constructor for creating a new CampingSite object.
     * 
     * @param type      The type of the camping site (e.g., TENT, CARAVAN, CABIN)
     * @param capacity  Maximum number of people the site can accommodate
     * @param price     Price of the site (e.g., per night)
     * @param amenities Array of amenities available at the site
     * @param status    Current status of the site (e.g., "available", "occupied")
     */
    public CampingSite(CampingType type, int capacity, double price, ArrayList<String> inAmenities) {
        this.id = "CampingSite" + idCounter++;
        this.type = type;
        this.capacity = capacity;
        this.price = price;
        for (String string : inAmenities) {
            amenities.add(string);
        }
        this.status = "Aktív";
    }

    // Basic getters
    public String getId() {
        return this.id;
    }

    public CampingType getCampingType() {
        return this.type;
    }

    public int getCapacity() {
        return this.capacity;
    }

    public double getPrice() {
        return this.price;
    }

    public ArrayList<String> getAmenities() {
        return this.amenities;
    }

    public String getStatus() {
        return this.status;
    }

    // Basic setters
    public void setType(String newType) {
        this.type = CampingType.valueOf(newType.toUpperCase());
    }

    public void setCapacity(int newCapacity) {
        this.capacity = newCapacity;
    }

    public void setPrice(double newPrice) {
        this.price = newPrice;
    }

    public void addAmenities(String newAmenitie) {
        // checks for input
        if (newAmenitie == null) {
            System.out.println("Adjon meg új extrát");
            return;
        }

        // checks for already existing amenitie in arraylist amenities
        for (String s : amenities) {
            if (newAmenitie.toLowerCase().equals(s)) {
                System.out.println("Ezt az extrát már tartalmazza az extrák listája: " + s);
                return; // we return if its already exists
            }
        }
        this.amenities.add(newAmenitie);
    }

    /**
     * Basic setter for status. Only admin users can change this.
     * 
     * @param user      User to check for the permission
     * @param newStatus new status String to change current status for.
     * @throws Exception method throws exception if the user don't have permission
     *                   to change status.
     */
    public void setStatus(User user, String newStatus) throws Exception {
        // checks for admin permission so only Admin users can change the status
        if (user instanceof Admin) {
            this.status = newStatus;
            return;
        }
        throw new Exception("A statust csak adminisztrátor tudja módosítani!");
    }
}
