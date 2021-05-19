package com.example.nio;

public class TaxiRideDetail {
    private final int bookingId;
    private final int customerId;
    private final String from;
    private final String to;
    private final double pickupTime;
    private final double DropTime;
    private final double amount;

    public TaxiRideDetail(int bookingId, int customerId, String from, String to, double pickupTime, double dropTime, double amount) {
        this.bookingId = bookingId;
        this.customerId = customerId;
        this.from = from;
        this.to = to;
        this.pickupTime = pickupTime;
        DropTime = dropTime;
        this.amount = amount;
    }

    public int getBookingId() {
        return bookingId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public double getPickupTime() {
        return pickupTime;
    }

    public double getDropTime() {
        return DropTime;
    }

    public double getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return "bookingId=" + bookingId +
                ", customerId=" + customerId +
                ", from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", pickupTime=" + pickupTime +
                ", DropTime=" + DropTime +
                ", amount=" + amount;
    }
}
