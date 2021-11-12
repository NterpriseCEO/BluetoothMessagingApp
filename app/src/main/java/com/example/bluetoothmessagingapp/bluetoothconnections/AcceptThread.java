package com.example.bluetoothmessagingapp.bluetoothconnections;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.util.UUID;

public class AcceptThread extends Thread implements Runnable{
    private final BluetoothServerSocket mmServerSocket;
    private static final UUID MY_UUID_INSECURE =
            UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");

    Context context;

    String username = "";

    public AcceptThread(Context context) {
        // Use a temporary object that is later assigned to mmServerSocket
        // because mmServerSocket is final.
        BluetoothServerSocket tmp = null;
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        try {
            // MY_UUID is the app's UUID string, also used by the client code.
            tmp = bluetoothAdapter.listenUsingRfcommWithServiceRecord("Gormsson.io", MY_UUID_INSECURE);
            Log.d("AcceptThread", "AcceptThread: Setting up Server using: " + MY_UUID_INSECURE);
        } catch (IOException e) {
            Log.e("AcceptThread", "Socket's listen() method failed", e);
        }
        mmServerSocket = tmp;
        this.context = context;
    }

    public void run() {
        BluetoothSocket socket = null;
        // Keep listening until exception occurs or a socket is returned.
        while (true) {
            try {
                System.out.println("Please work my dude, why won't this work?");
                socket = mmServerSocket.accept();
                System.out.println("LOL this is working now?");
            } catch (IOException e) {
                Log.e("AcceptThread", "Socket's accept() method failed", e);
                break;
            }

            if (socket != null) {
                // A connection was accepted. Perform work associated with
                // the connection in a separate thread.
                ConnectedThread con = new ConnectedThread(socket, context);
                Thread thread = new Thread(con);
                thread.start();
                while(con.getUsername().equals("")) {System.out.println("test");}
                username = con.getUsername();
                System.out.println("This is your username mate: "+username);

                try {
                    mmServerSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    public String getUsername() {
        return username;
    }

    // Closes the connect socket and causes the thread to finish.
    public void cancel() {
        try {
            mmServerSocket.close();
        } catch (IOException e) {
            Log.e("AcceptThread", "Could not close the connect socket", e);
        }
    }
}