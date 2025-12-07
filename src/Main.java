import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Main app = new Main();
        app.runApp();
    }

    public void welcomeInterface() {
        System.out.println("Welcome to the Camping Reservation System!");
        System.out.println("Please select an option:");
        System.out.println("1. Guest");
        System.out.println("2. Admin");
    }

    public void runApp() {
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

    private void clearConsole() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    private void showGuestInterface(Scanner sc) {
        System.out.println("\n--- Welcome Guest! ---");
        sc.nextLine();
    }

    private void showAdminInterface(Scanner sc) {
        System.out.println("\n--- Welcome Admin! ---");
        sc.nextLine();
        // Admin-specific logic will be implemented here.
    }
}
