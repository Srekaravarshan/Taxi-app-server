package com.example.nio;

import java.io.IOException;
import java.net.*;

public class Main {

    public static void main(String[] args) {
        try {
            DatagramSocket socket = new DatagramSocket(5000);

            while(true) {
                byte[] buffer = new byte[50];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);
                String[] input = new String(buffer, 0, packet.getLength()).split(" ");
                TaxiHandler.getInstance().bookTaxi(Integer.parseInt(input[0]), input[1], input[2], Double.parseDouble(input[3]), packet, socket);
            }

        } catch(SocketException e) {
            System.out.println("SocketException: " + e.getMessage());
        } catch(IOException e) {
            System.out.println("IOException: " + e.getMessage());
        }
    }
}
