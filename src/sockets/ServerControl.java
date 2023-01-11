package sockets;

import Model.Aircraft;
import javafx.beans.property.SimpleDoubleProperty;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerControl implements Runnable {
    private final Aircraft aircraft;

    public ServerControl(Aircraft aircraft) {
        this.aircraft = aircraft;
    }

    @Override
    public void run () {
        try {
            ServerSocket serverSocket;
            serverSocket = new ServerSocket(6666);
            Socket socket = serverSocket.accept();
            while (aircraft != null) {
                final DataInputStream dis = new DataInputStream(socket.getInputStream());
                final String order = dis.readUTF();
                handelOrder(order);
            }
            System.out.println("connecteddd");

            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handelOrder (String order){
        System.out.println("order = " + order);

        if ("rollRight".equals(order)) {
            aircraft.rollRight();
        } else if ("rollLeft".equals(order)) {
            aircraft.rollLeft();
        } else if ("speedDown".equals(order)) {
            aircraft.speedDown();
        } else if ("speedUp".equals(order)) {
            aircraft.speedUp();
        }else if ("pitchUp".equals(order)) {
            aircraft.pitchUp();
        }else if ("pitchDown".equals(order)) {
            aircraft.pitchDown();
        }else if ("goHigher".equals(order)) {
            aircraft.goHigher();
        }else if ("goLower".equals(order)) {
            aircraft.goLower();
        }
        else {
            System.out.println("Unexpected value");
        }
    }

}

