package org.academiadecodigo.codezillas.tripManager;

import org.academiadecodigo.codezillas.ConsoleColors.Colors;
import org.academiadecodigo.codezillas.menu.MenuAssets;
import org.academiadecodigo.codezillas.user.Client;
import org.academiadecodigo.codezillas.user.Driver;
import org.academiadecodigo.codezillas.user.DriverFactory;

import java.io.PrintStream;

public class Manager {

    private static Driver[] drivers;

    public Manager(int taxiAmount) {
        drivers = addDriver(taxiAmount);
    }

    public static Driver[] getDrivers() {
        return drivers;
    }

    public static synchronized void assignDriver(Client client, PrintStream printStream, int passengers) {
        try {
            boolean driverAssigned = false;
            int currentDriver = 0;
            for (int i = 0; i < drivers.length; i++) {

                if (client.getLocation().getX() == drivers[i].getLocation().getX()
                        && client.getLocation().getY() == drivers[i].getLocation().getY()) {
                    if (drivers[i].isAvailable()) {
                        drivers[i].setAvailability(false);
                        driverAssigned = true;
                        currentDriver = i;
                        break;
                    }
                }
            }

            if (!driverAssigned) {
                printStream.println(MenuAssets.NO_DRIVERS);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                printStream.println(MenuAssets.DRIVER_COMING);
                try {
                    Thread.sleep(1000);
                    printStream.println(MenuAssets.DRIVER_ARRIVED);
                    printStream.println("\n");
                    printStream.println("  " + client.getLocation() + MenuAssets.ARROW + client.getDestination());
                    printStream.println(Colors.YELLOW + MenuAssets.TAXI + Colors.RESET);
                    Thread.sleep(3000);
                    printStream.println(MenuAssets.TRIP_OVER);
                    printStream.println(Colors.PURPLE + "Your trip was: " + getCost(passengers, client) + " €" + Colors.RESET);
                    printStream.println(MenuAssets.DRIVER_MESSAGE);
                    drivers[currentDriver].setLocation(client.getDestination());
                    drivers[currentDriver].setAvailability(true);
                    client.cabFare(getCost(passengers, client));
                    Thread.sleep(2500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
    }

    public static void showDrivers() {}

    public static double getCost(int passengers, Client client) {
        return CostCalculator.calculateCost(passengers, client.getLocation(), client.getDestination());
    }

    public Driver[] addDriver(int taxiAmount) {
        drivers = new Driver[taxiAmount];
        for (int i = 0; i < drivers.length; i++) {
            drivers[i] = DriverFactory.getNewDriver();
        }
        return drivers;
    }
}
