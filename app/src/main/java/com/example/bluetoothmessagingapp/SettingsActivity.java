package com.example.bluetoothmessagingapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Camera;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bluetoothmessagingapp.chatpage.ChatActivity;
import com.example.bluetoothmessagingapp.database.DatabaseFunctions;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //Shows the toolbar and adds a back button to it
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(ContextCompat.getDrawable(SettingsActivity.this, R.drawable.ic_baseline_arrow_back_24));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Button changeUsername = findViewById(R.id.changeUsername);
        changeUsername.setOnClickListener(this);

        Button changeProfilePicture = findViewById(R.id.changeProfilePicture);
        changeProfilePicture.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.changeUsername) {
            //Shows an alert dialog that allows you to change the username
            this.setNewUsername();
        }else if(view.getId() == R.id.changeProfilePicture) {
            //Shows the camera, allowing you to change your profile_picture
            Intent goToCamera = new Intent(SettingsActivity.this, CameraActivity.class);
            startActivity(goToCamera);
        }
    }

    private void setNewUsername() {
        //Creates an alert dialog builder and sets the title
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.changeUsername);

        //Sets the content of the alert dialog
        final View layout = getLayoutInflater().inflate(R.layout.change_username_dialog, null);
        builder.setView(layout);

        //Connects the database
        DatabaseFunctions db = new DatabaseFunctions(SettingsActivity.this);
        db.open();
        Cursor c = db.getLocalUser();
        //References the input inside the dialog box
        EditText newUsername = layout.findViewById(R.id.newUsername);
        //Sets the input value equal to your current username
        newUsername.setText(c.getString(c.getColumnIndexOrThrow("username")));

        //When the OK button is clicked, updates the username of the local user
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                db.updateLocalUser(newUsername.getText().toString());
                //Lets the user know it was updated
                Toast.makeText(SettingsActivity.this, "Username Updated!", Toast.LENGTH_LONG).show();
                c.close();
                db.close();
            }
        });
        //CANCEL button only closes the DB connection
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                c.close();
                db.close();
            }
        });
        //Creates an alert dialog from the builder and displays it on screen
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}