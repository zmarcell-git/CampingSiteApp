package Service;

import java.util.ArrayList;

import Model.CampingSite;

public class CampingSiteManager {
    private ArrayList<CampingSite> campingSites = new ArrayList<CampingSite>();

    public String CreateCampingSite() {
        CampingSite newCampingSite = new CampingSite(null, 0, 0, null, null);
        campingSites.add(newCampingSite);
        return "Kempinghely sikeresen létrehozva!";
    }

    public String ModifyCampingSite(CampingSite campingSite) {
        return "ModifyCampingSite not implemented";
    }

    public String DeleteCampingSite(CampingSite campingSite) {
        return "DeleteCampingSite not implemented";
    }
}
