package Model;
import Service.CampingSiteManager;

public class Admin extends User {
    public CampingSiteManager campingSiteManager;

    public Admin(String name) {
        super(name);
    }
}
