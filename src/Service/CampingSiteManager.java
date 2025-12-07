package Service;

import java.util.ArrayList;
import java.util.Scanner;

import Model.CampingSite;
import Model.CampingType;
import Model.User;

public class CampingSiteManager {
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

    public void ModifyCampingSiteById(String id) {
        if (findCampingSiteById(id) == null) {
            System.out.println("Nincs ilyen kempinghely!");
            return;
        }

        // TODO: implement

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
    public void ModifyStatus(User curretnUser, String id) {
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
    public CampingSite findCampingSiteById(String campId) {
        for (CampingSite campingSite : campingSites) {
            if (campingSite.getId().equals(campId)) {
                return campingSite;
            }
        }
        return null;
    }
}
