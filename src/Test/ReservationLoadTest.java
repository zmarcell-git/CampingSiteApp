package Test;

import Service.CampingSiteManager;
import Service.ReservationManager;
import Model.CampingSite;
import Model.CampingType;
import Model.Guest;

import java.time.LocalDate;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ReservationLoadTest {

    public static void main(String[] args) throws InterruptedException {

        // Inicializálás
        CampingSiteManager siteManager = new CampingSiteManager();
        ReservationManager reservationManager = new ReservationManager(siteManager);

        // Tesztadatok létrehozása
        CampingSite site1 = new CampingSite(CampingType.CABIN, 4, 10000); // id, kapacitás, ár
        siteManager.AddCampingSite(site1);

        Guest guest = new Guest("stressTestUser");

        int numberOfThreads = 200; // párhuzamos szálak száma
        int reservationsPerThread = 50; // foglalások száma szálanként

        ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);

        long startTime = System.currentTimeMillis();

        for (int i = 0; i < numberOfThreads; i++) {
            executor.submit(() -> {
                for (int j = 0; j < reservationsPerThread; j++) {
                    try {
                        // Minden foglalás másik dátummal, hogy ne ütközzenek
                        LocalDate arrival = LocalDate.now().plusDays((long) (Math.random() * 30));
                        LocalDate departure = arrival.plusDays(1 + (long) (Math.random() * 5));

                        reservationManager.createReservation(arrival, departure, 2, guest, site1);

                    } catch (Exception e) {
                        System.out.println("Hiba a foglalásnál: " + e.getMessage());
                    }
                }
            });
        }

        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.MINUTES);

        long endTime = System.currentTimeMillis();

        System.out.println("Terheléses teszt befejezve.");
        System.out.println("Összes foglalás: " + reservationManager.getReservations().size());
        System.out.println("Teljes futási idő: " + (endTime - startTime) + " ms");
    }
}
