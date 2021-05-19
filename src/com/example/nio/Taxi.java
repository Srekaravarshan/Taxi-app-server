package com.example.nio;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Taxi {
    private final String name;
    private String location;
    private double earned;
    private TaxiRideDetail lastRideDetail;
    private final List<TaxiRideDetail> details;

    public Taxi(String name) {
        this.name = name;
        location = "A";
        earned = 0;
        details = new ArrayList<>();
    }

    public void book(Map<String, ArrayList<Taxi>> locations, String pickupPoint, String dropPoint, int customerId, double pickupTime, double dropTime, double amount) {
                TaxiRideDetail rideDetail = new TaxiRideDetail(customerId, customerId, pickupPoint, dropPoint, pickupTime, dropTime, amount);
                this.setLastRideDetail(rideDetail);
        new Thread(() -> {
            try {
                locations.get(pickupPoint).remove(this);

                System.out.println(this.name + " went from " + pickupPoint);

                Thread.sleep((int)(dropTime - pickupTime) * 10000);

        this.addDetails(rideDetail);
        this.setLocation(dropPoint);
        this.earned += amount;
                System.out.println(this.name + " reached on "+ dropPoint);
                int index = getIndex(locations.get(dropPoint));
                System.out.println(index);


                locations.get(dropPoint).forEach(i -> System.out.print(i.getName() +" " + i.getEarned() + ", "));
                locations.get(dropPoint).add(index, this);
                locations.get(dropPoint).forEach(i -> System.out.print(i.getName() +" " + i.getEarned() + ", "));
                System.out.println();


            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private int getIndex(ArrayList<Taxi> taxis){
        if (taxis.isEmpty()){ return 0; }
        if(taxis.size() == 1){
            if (this.earned > taxis.get(0).earned ){
                return 1;
            } else {
                return 0;
            }
        }

        int left = 0;
        int right = taxis.size() - 1;
        int mid = (right - left) / 2;

        while ((right - left) > 0) {
            mid = (right - left) / 2;
            if(taxis.get(mid).earned > this.earned){
                right = mid - 1;
            } else if (taxis.get(mid).earned < this.earned){
                left = mid + 1;
            } else {
                return mid;
            }
        }

        return mid;
    }

    public String getLocation() {
        return location;
    }

    public String getName() {
        return name;
    }

    public double getEarned() {
        return earned;
    }

    public List<TaxiRideDetail> getDetails() {
        return details;
    }

    public void addDetails(TaxiRideDetail detail) {
        details.add(detail);
    }

    public TaxiRideDetail lastRide() {
        return lastRideDetail;
    }

    public void setLastRideDetail(TaxiRideDetail lastRideDetail) {
        this.lastRideDetail = lastRideDetail;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}