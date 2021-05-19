package com.example.nio;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.*;

public class TaxiHandler {

    public static TaxiHandler instance = new TaxiHandler();
    private ArrayList<Taxi> taxis;
    private final Map<String, ArrayList<Taxi>> locations;
//    private ArrayList<Taxi> freeTaxis;

    private TaxiHandler() {

        taxis = new ArrayList<>();
        locations = new HashMap<>();
//        freeTaxis = new ArrayList<>();

        locations.put("A", new ArrayList<>());
        locations.put("B", new ArrayList<>());
        locations.put("C", new ArrayList<>());
        locations.put("D", new ArrayList<>());
        locations.put("E", new ArrayList<>());

        Taxi taxi1 = new Taxi("Taxi 1");
        Taxi taxi2 = new Taxi("Taxi 2");
        Taxi taxi3 = new Taxi("Taxi 3");
        Taxi taxi4 = new Taxi("Taxi 4");

        taxis.add(taxi1);
        taxis.add(taxi2);
        taxis.add(taxi3);
        taxis.add(taxi4);

        locations.get("A").add(taxi1);
        locations.get("A").add(taxi2);
        locations.get("A").add(taxi3);
        locations.get("A").add(taxi4);

    }

    public Taxi bookTaxi(int customerId, String pickupPoint, String dropPoint, double pickupTime, DatagramPacket packet, DatagramSocket socket) {

        pickupPoint = pickupPoint.toUpperCase();
        dropPoint = dropPoint.toUpperCase();
        Taxi taxi = getNearestTaxi(pickupPoint);

        if (taxi == null) {
            return null;
        }

        double dropTime = getDropTime(pickupTime, pickupPoint, dropPoint, taxi.getLocation());
        double amount = calcAmount(pickupPoint, dropPoint);

        taxi.book(locations, pickupPoint, dropPoint, customerId, pickupTime, dropTime, amount);

        String returnString = "Ride Booked on " + taxi.getName() + "Details: " + taxi.lastRide().toString();
//        String returnString = "echo: " + new String(ou, 0, packet.getLength());
        byte[] buffer2 = returnString.getBytes();
        InetAddress address = packet.getAddress();
        int port = packet.getPort();
        packet = new DatagramPacket(buffer2, buffer2.length, address, port);

        try {
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return taxi;
    }

    public static TaxiHandler getInstance() {
        return instance;
    }

    private Taxi getNearestTaxi(String pickupPoint) {

        ArrayList<Taxi> nearestTaxis = null;

        int right = pickupPoint.charAt(0);
        int left = right - 1;

        for (int i = right; i <= (int) ('E'); i++) {
            if (!locations.get(Character.toString((char) i)).isEmpty()) {
                nearestTaxis = locations.get(Character.toString((char) i));
                break;
            } else if (!(locations.get(Character.toString((char) left)).isEmpty())) {
                nearestTaxis = locations.get(Character.toString((char) left));
                break;
            }
            left--;
        }
        while (left >= (int) 'A' && nearestTaxis == null) {
            if (!(locations.get(Character.toString((char) left)).isEmpty())) {
                nearestTaxis = locations.get(Character.toString((char) left));
                break;
            }
            left--;
        }
        assert nearestTaxis != null;
        nearestTaxis.forEach(i -> System.out.println("nearest taxis: " + i.getName() + " " + i.getEarned() + ", "));
        return (nearestTaxis == null || nearestTaxis.isEmpty()) ? null : nearestTaxis.get(0);
    }

    private double getDropTime(double pickupTime, String pickupPoint, String dropPoint, String location) {
        int timeTakerForArrive = Math.abs(((int) pickupPoint.charAt(0)) - ((int) location.charAt(0)));
        int timeTaken = Math.abs(((int) dropPoint.charAt(0)) - ((int) pickupPoint.charAt(0)));
        return pickupTime + timeTaken + timeTakerForArrive;
    }

    private double calcAmount(String pickupPoint, String dropPoint) {
        int distance = Math.abs(((int) dropPoint.charAt(0)) - ((int) pickupPoint.charAt(0))) * 15;
        return 100 + ((distance - 5) * 10);
    }

    public void displayTaxiDetails() {
        for (Taxi taxi : taxis) {
            taxi.getDetails().forEach(
                    d -> System.out.println(
                        d.getBookingId() + " | " +
                        d.getCustomerId() + " | " +
                        d.getFrom() + " | " + d.getTo() + " | " +
                        d.getPickupTime() + " | " + d.getDropTime() +
                        " | " + d.getAmount()));
        }
    }

}
