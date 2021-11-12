package com.example.bluetoothmessagingapp.chatpage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bluetoothmessagingapp.R;
import com.example.bluetoothmessagingapp.bluetoothconnections.BluetoothConnection;
import com.example.bluetoothmessagingapp.database.DatabaseFunctions;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener {

    BluetoothConnection connection;
    EditText textarea;
    Handler mainThreadHandler;
    List<String[]> messages = new ArrayList<>();

    MessageAdapter messagesAdapter;
    RecyclerView.LayoutManager manager;

    String localUser;
    String remoteUser;
    String bluetoothID;

    boolean connected = false;

    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        remoteUser = getIntent().getExtras().getString("username");
        bluetoothID = getIntent().getExtras().getString("ID");

        //Sets the toolbar at the top of the screen
        Toolbar toolbar = findViewById(R.id.chatTitle);
        toolbar.setTitle(remoteUser);
        setSupportActionBar(toolbar);
        //The back button for the chat
        toolbar.setNavigationIcon(ContextCompat.getDrawable(ChatActivity.this, R.drawable.ic_baseline_arrow_back_24));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        DatabaseFunctions db = new DatabaseFunctions(this);
        db.open();
        Cursor c = db.getLocalUser();
        localUser = c.getString(c.getColumnIndex("username"));
        c.close();
        Cursor c2 = db.getMessages(bluetoothID);

        RecyclerView recyclerView = findViewById(R.id.messages);
        messagesAdapter = new MessageAdapter(messages, ChatActivity.this);

        manager = new LinearLayoutManager(this);

        while(c2.moveToNext()) {
            addMessage(c2.getString(c2.getColumnIndex("message")),c2.getString(c2.getColumnIndex("sent_from")), false);
        }
        c2.close();
        db.close();

        mainThreadHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 1) {
                    addMessage(msg.obj.toString(), remoteUser, true);
                }else if(msg.what == 2) {
                    connected = true;
                }
            }
        };

        connection = new BluetoothConnection(mainThreadHandler);

        connection.startServer();

        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();


        //Loops through all paired devices and if they are smart phones, they are added to the chats list
        for(BluetoothDevice device: pairedDevices) {
            BluetoothClass bluetoothClass = device.getBluetoothClass();// The type of bluetooth device
            if (bluetoothClass.getDeviceClass() == BluetoothClass.Device.PHONE_SMART &&
                getIntent().getExtras().getString("username").equals(device.getName())) {
                connection.startClient(device);
                break;
            }
        }

        ImageButton send = findViewById(R.id.send);
        send.setBackground(null);
        send.setOnClickListener(this);

        textarea = findViewById(R.id.textarea);
        textarea.setHint("Type message here");

        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(messagesAdapter);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.send) {
            if(!TextUtils.isEmpty(textarea.getText().toString()) && connected) {
                String message = textarea.getText().toString();
                connection.send(message);
                addMessage(message, localUser, true);
                textarea.setText("");
            }else {
                if(!connected) {
                    Toast.makeText(this, "Cannot send message. Connect to remote user first", Toast.LENGTH_LONG)
                    .show();
                }else if(TextUtils.isEmpty(textarea.getText().toString())) {
                    Toast.makeText(this, "Cannot send message. Message contents is empty", Toast.LENGTH_LONG)
                    .show();
                }
            }
        }
    }

    public void addMessage(String message, String username, boolean insert) {
        if(insert) {
            DatabaseFunctions db = new DatabaseFunctions(this);
            db.open();
            db.insertMessage(username, bluetoothID, message);
            db.close();
        }
        messages.add(new String[]{message, username, String.valueOf(username.equals(localUser))});
        messagesAdapter.notifyItemInserted(messages.size());
        manager.scrollToPosition(messages.size()-1);
    }
}