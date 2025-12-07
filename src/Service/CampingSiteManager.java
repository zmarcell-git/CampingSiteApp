package Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import Model.CampingSite;
import Model.CampingType;
import Model.ISearch;
import Model.Reservation;

public class CampingSiteManager implements ISearch {
    private ArrayList<CampingSite> campingSites;

    public CampingSiteManager() {
        this.campingSites = new ArrayList<>();
    }
    // Basic getter
    public ArrayList<CampingSite> getCampingSites() {
        return this.campingSites;
    }

    public CampingSite getCampingSiteById(String id) {
        for (CampingSite site : campingSites) {
            if (site.getId().equals(id)) {
                return site;
            }
        }
        return null;
    }

    /**
     * Creates a camping site
     * 
     * @param type
     * @param capacity
     * @param price
     */
    public void CreateCampingSite(CampingType type, int capacity, double price) {
        CampingSite campingSite = new CampingSite(type, capacity, price);
        campingSites.add(campingSite);
        System.out.println("Camping site created succesfully! " + campingSite.toString());
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
    public void DeleteCampingSite(String id, ReservationManager reservationManager) {
        CampingSite campsite = findCampingSiteById(id);
        if (campsite == null) {
            System.out.println("Nincs ilyen kempinghely!");
        }

        for (Reservation reservation : reservationManager.getReservations()) {
            if (reservation.getCampId().equals(campsite.getId())) {
                System.out.println(
                        "Can not delete this camping site because its already reserved! " + reservation.getId());
                return;
            }
        }
        campingSites.remove(campsite);
        System.out.println("Camping site successfully deleted!");
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
