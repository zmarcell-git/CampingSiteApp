
import java.util.Scanner;
import java.time.LocalDate;

import Model.CampingSite;
import Model.Guest;
import Model.User;
import Service.CampingSiteManager;
import Service.ReservationManager;
import Service.UserManager;

public class Main {

    // Managers setup
    private static CampingSiteManager campingSiteManager = new CampingSiteManager();
    private static UserManager userManager = new UserManager();
    private static ReservationManager reservationManager = new ReservationManager(campingSiteManager);

    public static void main(String[] args) {
        Main app = new Main();
        app.runApp();
    }

    private void runApp() {
        Scanner sc = new Scanner(System.in);
        String userType;

        while (true) {
            clearConsole();
            welcomeInterface();
            userType = sc.nextLine().trim();

            if (userType.equalsIgnoreCase("exit")) {
                System.out.println("Exiting application. Goodbye!");
                break;
            }

            switch (userType) {
                case "1":
                    showGuestInterface(sc);
                    break; // This break exits the switch, not the while loop
                case "2":
                    showAdminInterface(sc);
                    break; // This break exits the switch, not the while loop
                default:
                    System.out.println("\nInvalid input. Please enter 1, 2, or 'exit'.");
                    System.out.println("Press Enter to try again...");
                    sc.nextLine(); // Wait for user to press Enter
                    break;
            }
        }
        sc.close();
    }

    private void welcomeInterface() {
        System.out.println("Welcome to the Camping Reservation System!");
        System.out.println("Please select an option:");
        System.out.println("1. Guest");
        System.out.println("2. Admin");
    }

    private void clearConsole() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    // ---- User Interfaces -- //
    private void showGuestInterface(Scanner sc) {
        System.out.println("\n--- Welcome Guest! ---");
        System.out.println("Username: ");
        String username = sc.nextLine().trim();
        Guest guest = new Guest(username);
        userManager.addUser(guest);
        System.out.println("Welcome, " + guest.getName() + "!");
        System.out.println("Press Enter to continue...");

        sc.nextLine();
        userMenu(sc, guest.getId());
    }

    private void userMenu(Scanner sc, String userId) {
        System.out.println("\nGuest Menu:");
        System.out.println("1. Create Reservation");
        System.out.println("2. Modify Reservation");
        System.out.println("3. Delete Reservation");
        System.out.println("4. View Reservations");
        System.out.println("5. Back to Main Menu");
        System.out.print("Please select an option: ");
        String choice = sc.nextLine().trim();

        switch (choice) {
            case "1":
                // Logic for creating a reservation
                System.out.println("Create Reservation selected.");
                startReservationProcess(sc, userId);
                break;
            case "2":
                // Logic for modifying a reservation
                System.out.println("Modify Reservation selected.");
                break;
            case "3":
                // Logic for deleting a reservation
                System.out.println("Delete Reservation selected.");
                break;
            case "4":
                // Logic for viewing reservations
                System.out.println("View Reservations selected.");
                break;
            case "5":
                // Back to main menu
                return;
            default:
                System.out.println("Invalid option. Please try again.");
                break;
        }
    }
    private void startReservationProcess(Scanner sc, String userId) {
        System.out.println("Starting reservation process...");
        System.out.println("Please enter reservation details.");

        System.out.println("Arrival Date (YYYY-MM-DD): ");
        LocalDate arrivalDate = LocalDate.parse(sc.nextLine().trim());

        System.out.println("Departure Date (YYYY-MM-DD): ");
        LocalDate departureDate = LocalDate.parse(sc.nextLine().trim());
        
        System.out.println("Number of Guests: ");
        int guestNumber = Integer.parseInt(sc.nextLine().trim());

        System.out.println("Camping site id (leave blank for automatic assignment): ");
        String siteIdInput = sc.nextLine().trim();
        CampingSite campingSite = campingSiteManager.getCampingSiteById(siteIdInput);
        if (!siteIdInput.isEmpty()) {
            campingSite = null;
        }
        Guest guest = (Guest) userManager.getUserById(userId);
        reservationManager.createReservation(arrivalDate, departureDate, guestNumber, guest, campingSite);
    }
    

    // ---- Admin Interfaces -- //
    private void showAdminInterface(Scanner sc) {
        System.out.println("\n--- Welcome Admin! ---");
        sc.nextLine();
        // Admin-specific logic will be implemented here.
    }
}
