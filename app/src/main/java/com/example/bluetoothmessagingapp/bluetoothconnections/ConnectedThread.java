package com.example.bluetoothmessagingapp.bluetoothconnections;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.example.bluetoothmessagingapp.chatslist.ChatsActivity;
import com.example.bluetoothmessagingapp.database.DatabaseConnection;
import com.example.bluetoothmessagingapp.database.DatabaseFunctions;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class ConnectedThread extends Thread implements Runnable{
    private final BluetoothSocket mmSocket;
    private final InputStream mmInStream;
    private final OutputStream mmOutStream;
    private byte[] mmBuffer; // mmBuffer store for the stream

    String username = "";

    Context context;

    public ConnectedThread(BluetoothSocket socket, Context context) {
        mmSocket = socket;
        InputStream tmpIn = null;
        OutputStream tmpOut = null;

        // Get the input and output streams; using temp objects because
        // member streams are final.
        try {
            tmpIn = socket.getInputStream();
        } catch (IOException e) {
            Log.e("ConnectedThread", "Error occurred when creating input stream", e);
        }
        try {
            tmpOut = socket.getOutputStream();
        } catch (IOException e) {
            Log.e("ConnectedThread", "Error occurred when creating output stream", e);
        }

        mmInStream = tmpIn;
        mmOutStream = tmpOut;
        this.context = context;
    }

    @SuppressLint("Range")
    public void run() {
        mmBuffer = new byte[1024];
        int numBytes; // bytes returned from read()

        // Keep listening to the InputStream until an exception occurs.
        while (true) {
            try {
                // Read from the InputStream.
//                System.out.println("input stream - getUsername ===== "+ (getString(mmBuffer).equals("getUsername")));
//                System.out.println("input stream - getUsername 2 ===== "+ (getString(mmBuffer).equals("getUsername")));
                if(getString(mmBuffer).equals("getUsername")) {
                    DatabaseFunctions db = new DatabaseFunctions(context);
                    db.open();
                    Cursor c = db.getLocalUser();
                    username = c.getString(c.getColumnIndex("username"));
                }
            } catch (IOException e) {
                Log.d("ConnectedThread", "Input stream was disconnected", e);
                break;
            }
        }
    }

    private String getString(byte[] buffer) throws IOException {
        int numBytes = mmInStream.read(buffer);
        String s = new String(mmBuffer, StandardCharsets.UTF_8);
        s = s.substring(0, numBytes);
        return s;
    }

    // Call this from the main activity to send data to the remote device.
    public void write(byte[] bytes) {
        try {
            mmOutStream.write(bytes);
//
//            // Share the sent message with the UI activity.
//            Message writtenMsg = handler.obtainMessage(
//                    MessageConstants.MESSAGE_WRITE, -1, -1, mmBuffer);
//            writtenMsg.sendToTarget();
            System.out.println("testingngngngngn");
        } catch (IOException e) {
            Log.e("ConnectedThread", "Error occurred when sending data", e);
        }
    }

    public String getUsername() {
        return username;
    }

    // Call this method from the main activity to shut down the connection.
    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) {
            Log.e("ConnectedThread", "Could not close the connect socket", e);
        }
    }
}