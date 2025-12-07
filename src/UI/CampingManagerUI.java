package UI;

import java.util.Scanner;

import Model.CampingSite;
import Service.CampingSiteManager;

public class CampingManagerUI {

    CampingSiteManager campingSiteManager = new CampingSiteManager();

    Scanner sc = new Scanner(System.in);

    public void ModifyCampingSiteUI() {
        campingSiteManager.printCampingSites(); // show all campingsites so its easier to type the id
        System.out.print("Adja meg a kempinghely azonosítóját(ID): ");
        String inputSiteId = sc.nextLine().trim();

        // check if the input was even in the list
        if (campingSiteManager.findCampingSiteById(inputSiteId) == null) {
            System.out.println("Nincs ilyen azonosítóval kempinghely az adatbázisban!: " + inputSiteId);
            return;
        }
        CampingSite campingSite = campingSiteManager.findCampingSiteById(inputSiteId);

        System.out.println("\nVálasszon az alábbiak közül, hogy mit szeretne módosítani a kempinghelyen: ");
        System.out.println(
                "1. Kapacitás változtatása\n2. Ár változtatása\n3. Státusz változtatása\n4. Extrák változtatása");
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
                System.out.println("Nincs ilyen menüopció: " + input);
                break;
        }
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
}
