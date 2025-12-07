package UI;

import java.util.ArrayList;
import java.util.Scanner;

import Model.CampingSite;
import Model.CampingType;
import Model.Reservation;
import Service.CampingSiteManager;
import Service.ReservationManager;

public class CampingManagerUI {

    CampingSiteManager campingSiteManager = new CampingSiteManager();

    Scanner sc = new Scanner(System.in);

    public void ModifyCampingSiteUI() {
        campingSiteManager.printCampingSites(); // show all campingsites so its easier to type the id
        System.out.print("Enter the camping site ID: ");
        String inputSiteId = sc.nextLine().trim();

        // check if the input was even in the list
        if (campingSiteManager.findCampingSiteById(inputSiteId) == null) {
            System.out.println("There is no such camping site with this ID!: " + inputSiteId);
            return;
        }
        CampingSite campingSite = campingSiteManager.findCampingSiteById(inputSiteId);

        System.out.println("\nChoose what do you want modify in the camping site: ");
        System.out.println(
                "1. Modify Capacity\n2. Modify Price\n3. Modify Status\n4. Modify Amenities");
        String input = sc.nextLine().trim();

        switch (input) {
            case "1":
                showCapacityChangeUI(campingSite);
                break;
            case "2":
                showPriceChangeUI(campingSite);
                break;
            case "3":
                showStatusChangeUI(campingSite);
                break;
            case "4":
                showAmenitiesUI(campingSite);
                break;
            default:
                System.out.println("No such menu option: " + input);
                break;
        }
    }

    public void CreateCampingSite() {
        CampingType type = null;
        System.out.println("Create new Camping Site");
        System.out.print("Site type: \n1. Tent\n2. Caravan\n3. Cabin\n");
        String typeInput = sc.nextLine().trim();
        switch (typeInput) {
            case "1":
                type = CampingType.TENT;
                break;
            case "2":
                type = CampingType.CARAVAN;
                break;
            case "3":
                type = CampingType.CABIN;
                break;
            default:
                System.out.println("Invalid site type, choose from 1-3+");
                break;
        }
        System.out.print("\nCapacity: ");
        int capacity = Integer.parseInt(sc.nextLine().trim());
        System.out.print("\nPrice: ");
        double price = Double.parseDouble(sc.nextLine().trim());

        campingSiteManager.CreateCampingSite(type, capacity, price);
        System.out.println("Press Enter to continue...");
        sc.nextLine();
    }

    private void showAmenitiesUI(CampingSite site) {
        System.out.println(
                site.getId() + "azonosítóval ellátott kempinghelyen jelenleg ezek az extrák találhatóak: "
                        + site.getAmenities());
        System.out.println("1. Extra hozzáadása\n2. Extra eltávolítása\n3. Összes extra törlése");
        String menuInput = sc.nextLine().trim();
        switch (menuInput) {
            case "1.":
                System.out.print("\nAdja meg a hozzáadandó extra nevét: ");
                String newExtra = sc.nextLine().trim().toLowerCase();
                campingSiteManager.addAmenitieForSite(site, newExtra);
                break;
            case "2":
                System.out.print("\nAdja meg a törölni kívánt extra nevét: ");
                String deleteExtra = sc.nextLine().trim().toLowerCase();
                campingSiteManager.deleteAmenitiesFromSite(site, deleteExtra);
                break;
            case "3":
                campingSiteManager.deleteAllAmenitiesFromSite(site);
                break;
            default:
                break;
        }
    }

    private void showStatusChangeUI(CampingSite site) {
        System.out.println("1. Aktív\n2. Inaktív\n3. Lefoglalt");
        String input = sc.nextLine().trim();
        switch (input) {
            case "1":
                campingSiteManager.ModifyStatus(site, "Aktív");
                break;
            case "2":
                campingSiteManager.ModifyStatus(site, "Inaktív");
                break;
            case "3":
                campingSiteManager.ModifyStatus(site, "Lefoglalt");
                break;
            default:
                System.out.println("Nincs ilyen menüopció: " + input);
                break;
        }
    }

    private void showCapacityChangeUI(CampingSite campSite) {
        System.out.print("\nAdja meg az új kapacitást: ");
        String newCapacity = sc.nextLine().trim();
        campingSiteManager.ModifyCapacity(campSite, newCapacity);
    }

    private void showPriceChangeUI(CampingSite campSite) {
        System.out.println("1. Fix ár\n2. Dinamikus ár");
        String input = sc.nextLine().trim();

        switch (input) {
            case "1":
                System.out.print("Adja meg az új árát a kempinghelynek: ");
                String newPrice = sc.nextLine().trim();
                campingSiteManager.SiteFixPricing(campSite, newPrice);
                break;
            case "2":
                System.out.print("Adja meg a szállás alap árát: ");
                String basicPrice = sc.nextLine().trim();
                System.out.print("\nAdja meg hogy átlagba mennyivel emeljék az extrák az árat? ");
                String priceGrowByExtras = sc.nextLine().trim();
                campingSiteManager.SitePricingToGrowWithAmenities(campSite, basicPrice, priceGrowByExtras);
                break;
            default:
                System.out.println("Nincs ilyen menüopció: " + input);
                break;
        }
    }

    public void showCampingSites() {
        ArrayList<CampingSite> sites = campingSiteManager.getCampingSites();
        System.out.println("All Camping sites: ");
        for (CampingSite campingSite : sites) {
            System.out.println(campingSite.toString());
        }
        System.out.println("\nPress enter to continue...");
        sc.nextLine();
    }

    public void deleteCampingSiteById(ReservationManager reservationManager) {
        System.out.print("Give a camping site ID to delete camping site: ");
        String campId = sc.nextLine().trim();
        campingSiteManager.DeleteCampingSite(campId, reservationManager);
    }
}
