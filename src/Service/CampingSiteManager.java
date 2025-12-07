package Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import Model.CampingSite;
import Model.CampingType;
import Model.ISearch;

public class CampingSiteManager implements ISearch {
    private ArrayList<CampingSite> campingSites = new ArrayList<CampingSite>();

    public void CreateCampingSite(String[] data) throws Exception {
        campingSites.add(CreateCampingSiteObject(data));
    }

    // Basic getter
    public ArrayList<CampingSite> getCampingSites() {
        return this.campingSites;
    }

    // TODO: needs refactoring
    private CampingSite CreateCampingSiteObject(String[] data) throws Exception {
        CampingType type = DataToType(data[0]);
        int capacity = Integer.parseInt(data[1]);
        double price = Double.parseDouble(data[2]);

        ArrayList<String> amenities = new ArrayList<String>();

        if (!data[3].isEmpty()) {
            for (String s : data[3].split(",")) {
                amenities.add(s.trim());
            }
        }
        return new CampingSite(type, capacity, price, amenities);
    }

    /**
     * Prints the site list into the UI
     */
    public void printCampingSites() {
        for (CampingSite site : campingSites) {
            System.out.println(site.toString());
        }
    }

    /**
     * Converts a given string to the corresponding CampingType enum value.
     *
     * <p>
     * This method attempts to match the provided string (case-insensitively)
     * to one of the defined values in the CampingType enum. If the input is null,
     * the method returns null. If the input does not match any enum constant,
     * an Exception is thrown with an informative error message.
     * </p>
     *
     * @param data the string representing a camping type
     * @return the corresponding CampingType value, or null if the input is null
     * @throws Exception if the given string does not match any CampingType enum
     *                   constant
     */
    private CampingType DataToType(String data) throws Exception {
        if (data == null) {
            return null;
        }

        try {
            return CampingType.valueOf(data.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new Exception("No such a type in enum CampingSiteType: " + e.getMessage());
        }
    }

    /**
     * Modifies the capacity of a camping site identified by the given ID.
     * <p>
     * The method retrieves the camping site and displays its current capacity.
     * It then reads user input and attempts to parse it as an integer.
     * If the input is valid, the site's capacity is updated.
     * If the input is not a valid integer, an error message is displayed.
     *
     * @param id The ID of the camping site whose capacity will be modified.
     */
    public void ModifyCapacity(CampingSite campingSite, String newCapacity) {
        try {
            int capacityToInteger = Integer.parseInt(newCapacity);
            campingSite.setCapacity(capacityToInteger);
        } catch (NumberFormatException e) {
            System.out.println("Kérem egész számot adjon meg a kapacitás változtatásához! " + e.getMessage());
        }
    }

    /**
     * Decide from the local date wether is in the summer season or not
     * 
     * @param date the date to found out if its in the season
     * @return returns true if the date in the season; otherwise false
     */
    private boolean isInSeason(LocalDate date) {
        int month = date.getMonthValue();

        if (month >= 6 && month <= 8) {
            return true;
        }
        return false;
    }

    /**
     * Sets a basic fix pricing for the given site object
     * 
     * @param site the object which has to be changed
     */
    public void SiteFixPricing(CampingSite site, String newPrice) {
        try {
            double newprice = Double.parseDouble(newPrice);
            site.setPrice(newprice);
        } catch (NumberFormatException e) {
            System.err.println("Kérem számmal adja meg az új árat! " + e.getMessage());
        }
        System.out.println("Ár sikeresen változtatva: " + site.getId() + " kempinghely ára: " + site.getPrice());
    }

    /**
     * Sets the pricing grow with the amount of amenities does the site has
     * 
     * @param site site to be changed
     */
    public void SitePricingToGrowWithAmenities(CampingSite site, String basePrice, String extrasPrice) {
        double convertedBasePrice = Double.parseDouble(basePrice);
        convertedBasePrice += (Double.parseDouble(extrasPrice) * site.getAmenities().size());
        site.setPrice(convertedBasePrice);
        System.out.println(site.getId() + " kempinghely új árazása: " + site.getPrice()
                + "/éj az extrák mennyisége miatt: " + site.getAmenities().size());
    }

    /**
     * Modifies the status of a camping site identified by the given ID.
     * <p>
     * The method retrieves the camping site, displays its current status,
     * then asks the user to enter a new status. It attempts to update the status
     * using the provided user object for permission checking. If an error occurs
     * during the update, the exception message is displayed.
     *
     * @param id The ID of the camping site whose status will be modified.
     */
    public void ModifyStatus(CampingSite site, String status) {
        site.setStatus(status);
        System.out.println("Kempinghely státusza sikeresen módosítva! " + site.getId() + " kempinghely új státusza: "
                + site.getStatus());
    }

    public void addAmenitieForSite(CampingSite site, String amenitie) {
        for (String s : site.getAmenities()) {
            if (s.equals(amenitie)) {
                System.out.println("Ez az extra már megtalálható ezen a kempinghelyen: " + amenitie);
                return;
            }
        }
        site.addAmenitie(amenitie);
        System.out.println("Extra sikeresen hozzáadva a kempinghelyhez! Jelenlegi extrák: " + site.getAmenities());
    }

    public void deleteAmenitiesFromSite(CampingSite site, String delExtra) {
        for (String s : site.getAmenities()) {
            if (s.equals(delExtra)) {
                site.deleteAmenities(delExtra);
                System.out.println("Sikeresen törölte az extrák közül: " + delExtra);
                return;
            }
        }
        System.out.println("Nincs ilyen extra a kempinghely extrái között: " + delExtra);
    }

    public void deleteAllAmenitiesFromSite(CampingSite site) {
        if (site.getAmenities().size() == 0) {
            System.out.println("Ezen a kempinghelyen még nincs extra!");
        }
        site.deleteAllAmenities();
        System.out.println("Sikeresen törölte a kempinghely összes extráját!");
    }

    /**
     * Removes a camping site by the given ID from the camping sites.
     * 
     * @param id (String) CampingSite.id
     */
    public void DeleteCampingSite(String id) {
        CampingSite campsite = findCampingSiteById(id);
        if (campsite == null) {
            System.out.println("Nincs ilyen kempinghely!");
        }

        campingSites.remove(campsite);
    }

    /**
     * Finds CampingSite by id and returns it.
     * If the list does not contains the Site it retorns null;
     * 
     * @param campId Strign Campsite.id
     * @return Returns Campsite.
     */
    public CampingSite findCampingSiteById(String campId) {
        for (CampingSite campingSite : campingSites) {
            if (campingSite.getId().equals(campId)) {
                return campingSite;
            }
        }
        return null;
    }

    @Override
    public <T> List<T> search(Object criteria) {
        CampingSite searchCriteria = (CampingSite) criteria;
        @SuppressWarnings("unchecked")
        List<T> results = (List<T>) campingSites.stream()
                .filter(campingSite -> searchCriteria.getCampingType() == null
                        || searchCriteria.getCampingType().equals(campingSite.getCampingType()))
                .filter(campingSite -> searchCriteria.getCapacity() == 0
                        || searchCriteria.getCapacity() == campingSite.getCapacity())
                .filter(campingSite -> searchCriteria.getPrice() == 0.0
                        || searchCriteria.getPrice() == campingSite.getPrice())
                .collect(Collectors.toList());
        return results;
    }
}
