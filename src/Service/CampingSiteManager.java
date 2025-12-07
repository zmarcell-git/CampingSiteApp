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

    Scanner sc = new Scanner(System.in);

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
     * Provides a menu for modifying different attributes of a camping site
     * identified by the given ID.
     * <p>
     * The method checks whether the camping site exists. If it does not, an error
     * message is displayed
     * and the method exits. Otherwise, a menu is shown where the user can choose to
     * modify:
     * <ul>
     * <li>Capacity</li>
     * <li>Price</li>
     * <li>Status</li>
     * <li>Amenities</li>
     * </ul>
     * Based on the selected option, the corresponding modification method is
     * called.
     *
     * @param id The ID of the camping site to modify.
     */
    public void ModifyCampingSiteById(String id) {
        if (findCampingSiteById(id) == null) {
            System.out.println("Nincs ilyen kempinghely!");
            return;
        }

        System.out.println("Válasszon az alábbiak közül, hogy mit szeretne módosítani: ");
        System.out.println(
                "1. Kapacitás változtatása\n2. Ár változtatása\n3. Státusz változtatása\n4. Extrák változtatása");

        String menuOption = sc.nextLine().trim().toLowerCase();

        switch (menuOption) {
            case "1":
                ModifyCapacity(id);
                break;
            case "2":
                ModifyPrice(id);
                break;
            case "3":
                ModifyStatus(null, id); // TODO: implement user permission
                break;
            case "4":
                ModifyAmenities(id);
                break;
            default:
                System.out.println(
                        "Nincs ilyen menü opció: " + menuOption + "\nKérem válasszon egy érvénye opciót!(1,2,3,4)");
                break;
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
    private void ModifyCapacity(String id) {
        CampingSite site = findCampingSiteById(id);
        System.out.println(site.getId() + " kempinghely jelenlegi kapacitása: " + site.getCapacity());
        String input = sc.nextLine().trim();
        try {
            int newCapacity = Integer.parseInt(input);
            site.setCapacity(newCapacity);
        } catch (NumberFormatException e) {
            System.out.println("Kérem egész számot adjon meg a kapacitás változtatásához! " + e.getMessage());
        }
    }

    /**
     * Modifies the price of a camping site identified by the given ID.
     * <p>
     * The method retrieves the camping site and displays its current price.
     * It then reads user input and attempts to convert it to a double value.
     * If the conversion is successful, the site's price is updated.
     * If the input is not a valid number, an error message is shown.
     *
     * @param id The ID of the camping site whose price will be modified.
     */
    private void ModifyPrice(String id) {
        CampingSite site = findCampingSiteById(id);
        System.out.println(site.getId() + " kempinghely jelenlegi ára: " + site.getPrice());
        String inputPrice = sc.nextLine().trim();
        try {
            double newPrice = Double.parseDouble(inputPrice);
            site.setPrice(newPrice);
        } catch (NumberFormatException e) {
            System.err.println("Nem megfelelő formátumba megadott ár! " + e.getMessage());
        }
    }

    /**
     * Sets the site pricing dynamic with helper methods. Find the site by id.
     * 
     * @param id given id to find the site
     */
    public void setPricingToDynamic(String id) {
        CampingSite site = findCampingSiteById(id);
        System.out.println("Adja meg milyenre akarja változtatni az árazást: ");
        System.out.println("1. Fix árazás\n2. Extráktól függő árazás\n3. Szezonális árazás");
        String input = sc.nextLine();
        switch (input) {
            case "1":
                SiteFixPricing(site);
                break;
            case "2":
                SitePricingToGrowWithAmenities(site);
                break;
            case "3":
                SitePricingSeasonal(site);
                break;

            default:
                break;
        }
    }

    /**
     * Sets pricing of the given site seasonal
     * 
     * @param site the site which needs to be changed
     */
    private void SitePricingSeasonal(CampingSite site) {
        // TODO: implement
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
    private void SiteFixPricing(CampingSite site) {
        System.out.print("Mennyi legyen az új ára a kempinghelynek? ");
        String input = sc.nextLine().trim();
        double newprice = Integer.parseInt(input);
        site.setPrice(newprice);
        System.out.println("Ár sikeresen változtatva: " + site.getId() + " kempinghely ára: " + site.getPrice());
    }

    /**
     * Sets the pricing grow with the amount of amenities does the site has
     * 
     * @param site site to be changed
     */
    private void SitePricingToGrowWithAmenities(CampingSite site) {
        double price = 500;
        price = price + (100 * site.getAmenities().size());
        site.setPrice(price);
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
     * @param curretnUser The user who is attempting to change the site's status.
     * @param id          The ID of the camping site whose status will be modified.
     */
    private void ModifyStatus(User curretnUser, String id) {
        CampingSite site = findCampingSiteById(id);
        System.out.println("Kempinghely jelenlegi státusza: " + site.getStatus());
        System.out.print("Mire szeretné változtatni a státuszát a kempinghelynek?: ");
        String newStatus = sc.nextLine().trim().toLowerCase();
        try {
            site.setStatus(curretnUser, newStatus);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Modifies the amenities of a camping site identified by the given ID.
     * <p>
     * The method first attempts to find the camping site by its ID.
     * If no site is found, an error message is displayed and the method returns.
     * Otherwise, the current list of amenities is shown and the user can choose
     * from
     * three modification options:
     * <ul>
     * <li>Add a new amenity to the site</li>
     * <li>Remove an existing amenity</li>
     * <li>Clear all amenities from the site</li>
     * </ul>
     * Based on the user's input, the method performs the corresponding update
     * on the site's list of amenities.
     *
     * @param id The ID of the camping site whose amenities should be
     *           modified.
     */
    private void ModifyAmenities(String id) {
        CampingSite site = findCampingSiteById(id);

        // checks if the site is even exists
        if (site == null) {
            System.out.println("Nincs ilyen id-val rendelkező kempinghely!");
            return;
        }
        System.out.println("Jelenlegi extrák: " + site.getAmenities());

        // print the menu options
        System.out.println("1. Új extra felvétele\n2. Meglévő extra törlése\n3. Összes extra törlése");
        String input = sc.nextLine().trim();
        switch (input) {
            case "1":
                String newAmenite = sc.nextLine().trim().toLowerCase();
                site.addAmenitie(newAmenite);
                break;
            case "2":
                String delExtra = sc.nextLine().trim().toLowerCase();
                ArrayList<String> siteAmenities = site.getAmenities();
                if (siteAmenities.size() == 0) {
                    System.out.println("Ez a hely nem tartalmaz extrákat!");
                    break;
                }
                if (siteAmenities.contains(delExtra)) {
                    siteAmenities.remove(delExtra);
                    System.out.println(delExtra + " extra sikeresen törölve!");
                    break;
                }
                System.out.println("Nem található ilyen extra ezen a kempinghelyen!");
                break;

            case "3":
                site.getAmenities().clear();
                break;

            default:
                // if the user does not choose from an available menu option
                System.out.println("Nincs ilyen lehetőség: " + input);
                break;
        }
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
    private CampingSite findCampingSiteById(String campId) {
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
                .filter(campingSite -> searchCriteria.getCampingType() == null || searchCriteria.getCampingType().equals(campingSite.getCampingType()))
                .filter(campingSite -> searchCriteria.getCapacity() == 0 || searchCriteria.getCapacity() == campingSite.getCapacity())
                .filter(campingSite -> searchCriteria.getPrice() == 0.0 || searchCriteria.getPrice() == campingSite.getPrice())
                .collect(Collectors.toList());
        return results;
    }
}
