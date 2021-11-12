package com.example.bluetoothmessagingapp.bluetoothconnections;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class ConnectThread extends Thread implements Runnable{
    private BluetoothSocket mmSocket;
    private BluetoothDevice mmDevice;
    private static final UUID MY_UUID_INSECURE =
            UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");

    private String username = "";

    Context context;

    int doWhat = 0;

    public ConnectThread(BluetoothDevice device, Context context) {
        // Use a temporary object that is later assigned to mmSocket
        // because mmSocket is final.
        BluetoothSocket tmp = null;
        mmDevice = device;
        this.context = context;
    }

    public void run() {
        // Cancel discovery because it otherwise slows down the connection.
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        bluetoothAdapter.cancelDiscovery();

        BluetoothSocket temp = null;
        try {
            // Get a BluetoothSocket to connect with the given BluetoothDevice.
            // MY_UUID is the app's UUID string, also used in the server code.
            temp = mmDevice.createRfcommSocketToServiceRecord(MY_UUID_INSECURE);
            Log.e("ConnectThread", "Creating connection to another device");
        } catch (IOException e) {
            Log.e("ConnectThread", "Socket's create() method failed", e);
        }
        mmSocket = temp;

        try {
            // Connect to the remote device through the socket. This call blocks
            // until it succeeds or throws an exception.
            mmSocket.connect();
            Log.e("ConnectThread", "Connecting to another device");
        } catch (IOException connectException) {
            // Unable to connect; close the socket and return.
            try {
                mmSocket.close();
            } catch (IOException closeException) {
                Log.e("ConnectThread", "Could not close the client socket", closeException);
            }
            return;
        }

        // The connection attempt succeeded. Perform work associated with
        // the connection in a separate thread.
        System.out.println("This is working now!!!!!!!!!!!!");
        ConnectedThread con = new ConnectedThread(mmSocket, context);
        Thread thread = new Thread(con);
        thread.start();
        while(true) {
            if(doWhat == 1) {
                con.write("getUsername".getBytes(StandardCharsets.UTF_8));
                doWhat = 0;
            }
        }
//        con.getUsername();
    }

    // Closes the client socket and causes the thread to finish.
    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) {
            Log.e("ConnectThread", "Could not close the client socket", e);
        }
    }

    public void getUsername() {
        doWhat = 1;
    }
}