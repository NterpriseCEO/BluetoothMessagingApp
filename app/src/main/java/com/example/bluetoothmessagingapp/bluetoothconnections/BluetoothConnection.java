package com.example.bluetoothmessagingapp.bluetoothconnections;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.database.Cursor;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.bluetoothmessagingapp.database.DatabaseFunctions;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class BluetoothConnection {

    private static final UUID MY_UUID_INSECURE =
            UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");

    private ConnectedThread connectedThread;

    Handler handler;

    public BluetoothConnection(Handler handler) {
        this.handler = handler;
    }

    public void startServer() {
        AcceptThread aThread = new AcceptThread();
        aThread.start();
    }

    public void startClient(BluetoothDevice device) {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        adapter.cancelDiscovery();

        ConnectThread cThread = new ConnectThread(device);
        cThread.start();
    }

    private class AcceptThread extends Thread{
        private final BluetoothServerSocket mmServerSocket;

        public AcceptThread() {
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
                    connected(socket);
                    try {
                        mmServerSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
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

    private class ConnectThread extends Thread{
        private BluetoothSocket mmSocket;
        private BluetoothDevice mmDevice;

        public ConnectThread(BluetoothDevice device) {
            // Use a temporary object that is later assigned to mmSocket
            // because mmSocket is final.
            BluetoothSocket tmp = null;
            mmDevice = device;
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
            Message message = new Message();
            message.what = 2;
            handler.sendMessage(message);
            connected(mmSocket);
        }

        // Closes the client socket and causes the thread to finish.
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e("ConnectThread", "Could not close the client socket", e);
            }
        }
    }

    private class ConnectedThread extends Thread implements Runnable{
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;
        private byte[] mmBuffer; // mmBuffer store for the stream

        public ConnectedThread(BluetoothSocket socket) {
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
        }

        @SuppressLint("Range")
        public void run() {
            mmBuffer = new byte[1024];
            int numBytes; // bytes returned from read()

            // Keep listening to the InputStream until an exception occurs.
            while (true) {
                try {
                    Message message = new Message();
                    message.what = 1;
                    message.obj = getString(mmBuffer);
                    handler.sendMessage(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
//                try {
//                     Read from the InputStream.
//                System.out.println("input stream - getUsername ===== "+ (getString(mmBuffer).equals("getUsername")));
//                System.out.println("input stream - getUsername 2 ===== "+ (getString(mmBuffer).equals("getUsername")));
//                    if(getString(mmBuffer).equals("getUsername")) {
//                        DatabaseFunctions db = new DatabaseFunctions(context);
//                        db.open();
//                        Cursor c = db.getLocalUser();
//                        username = c.getString(c.getColumnIndex("username"));
//                    }
//                } catch (IOException e) {
//                    Log.d("ConnectedThread", "Input stream was disconnected", e);
//                    break;
//                }
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

        // Call this method from the main activity to shut down the connection.
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e("ConnectedThread", "Could not close the connect socket", e);
            }
        }
    }

    public void connected(BluetoothSocket bSocket) {
        connectedThread = new ConnectedThread(bSocket);
        connectedThread.start();
    }

    public void send(String message) {
        connectedThread.write(message.getBytes(StandardCharsets.UTF_8));
    }
}
