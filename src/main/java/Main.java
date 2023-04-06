import java.util.ArrayList;
import java.util.Random;

public class Main {

    public static void doWork() {
        try {
            Thread.sleep(new Random().nextInt(5000));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        ArrayList<Integer> warehouse = new ArrayList<>();

        Thread farmerThread = new Thread(() -> {
            while (true) {
                doWork();
                synchronized (warehouse) {
                    if (warehouse.size() < 5) {
                        warehouse.add(warehouse.size());
                        System.out.println("Farmer added orange: " + warehouse.size());
                        System.out.println("Farmer calls driver and notifies him");
                        warehouse.notify(); // Notify the driver thread that an orange has been added.
                    } else {
                        try {
                            System.out.println("Warehouse is full! Farmer waiting for notification");
                            warehouse.wait(); // Wait for the driver thread to remove an orange.
                        } catch (InterruptedException e) {}
                    }
                }
            }
        });

        Thread driverThread = new Thread(() -> {
            while (true) {
                doWork();
                synchronized (warehouse) {
                    if (warehouse.size() > 0) {
                        int orange = warehouse.get(0);
                        warehouse.remove(0);
                        System.out.println("Driver picked up orange: " + orange);
                        System.out.println("Driver calls farmer and notifies him.");
                        warehouse.notify(); // Notify the farmer thread that an orange has been removed.
                    } else {
                        try {
                            System.out.println("Warehouse is empty! Driver waiting for notification");
                            warehouse.wait(); // Wait for the farmer thread to add an orange.
                        } catch (InterruptedException e) {}
                    }
                }
            }
        });

        farmerThread.start();
        driverThread.start();
    }
}
