package sockets;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ClientController implements Runnable {
    String order = "";
    boolean b = true;
    boolean c = false;

    public boolean isC() {
        return c;
    }

    public void setC(boolean c) {
        this.c = c;
    }

    @Override
    public void run() {
        try {
            Socket socket = new Socket("localhost", 6666);
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            while (b==true) {
                System.out.println("connected");

                if(c==true) {

                    dataOutputStream.writeUTF(order);
                    dataOutputStream.flush();
                    c=false;
                }
            }
            dataOutputStream.close();
            socket.close();
        } catch (Exception e) {
            System.out.println(e.toString());
        }

    }
    public void sendSocket(String order){
        c=true;
        this.order=order;
    }



}
