package Service;

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

    public void ModifyCampingSite(String id) {
        if (findCampingSiteById(id) == null) {
            System.out.println("Nincs ilyen kempinghely!");
            return;
        }

        // TODO: implement
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
                .filter(campingSite -> searchCriteria.getCampingType() == null || searchCriteria.getCampingType().equals(campingSite.getCampingType()))
                .filter(campingSite -> searchCriteria.getCapacity() == 0 || searchCriteria.getCapacity() == campingSite.getCapacity())
                .filter(campingSite -> searchCriteria.getPrice() == 0.0 || searchCriteria.getPrice() == campingSite.getPrice())
                .collect(Collectors.toList());
        return results;
    }
}
