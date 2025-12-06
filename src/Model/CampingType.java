package Model;

/**
 * The CampingType enum represents the type of a camping site.
 * 
 * Each type has a user-friendly description.
 * Possible values:
 * - TENT: Tent site
 * - CARAVAN: Caravan site
 * - CABIN: Cabin / bungalow
 */
public enum CampingType {
    TENT("Sátor"),
    CARAVAN("Lakókocsi"),
    CABIN("Faház");

    private final String description;

    /**
     * Constructor for the CampingType enum.
     * 
     * @param description A human-readable description of the camping type
     */
    CampingType(String description) {
        this.description = description;
    }

    /**
     * Returns the description of the camping type.
     * 
     * @return the description string
     */
    public String getDescription() {
        return description;
    }
}
