package com.example.bluetoothmessagingapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bluetoothmessagingapp.chatpage.ChatActivity;
import com.example.bluetoothmessagingapp.database.DatabaseFunctions;

import java.io.File;
import java.io.IOException;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    final String pattern = "^(^[a-zA-Z0-9@.#$%^&*_&\\\\/,.\\-\\+=!\"£$%^&*()€])+$";
    TextView username;
    String currentUsername;

    File profileImage = new File(Environment.getExternalStorageDirectory()+"/bluetoothMessenger/userProfile.jpg");
    Bitmap image;

    @SuppressLint("Range")
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
        DatabaseFunctions db = new DatabaseFunctions(SettingsActivity.this);
        db.open();
        Cursor c = db.getLocalUser();
        username = findViewById(R.id.username);
        currentUsername = c.getString(c.getColumnIndex("username"));
        username.setText(currentUsername);
        c.close();
        db.close();

        loadImage();
        ImageView profilePicture = findViewById(R.id.profilePicture);
        profilePicture.setImageBitmap(image);

        Button changeUsername = findViewById(R.id.changeUsername);
        changeUsername.setOnClickListener(this);

        Button changeProfilePicture = findViewById(R.id.changeProfilePicture);
        changeProfilePicture.setOnClickListener(this);
    }

    private void loadImage() {
        //Checks if the image exists
        if(profileImage.exists()) {
            //Converts the image to a bitmap
            Bitmap bitmap = BitmapFactory.decodeFile(profileImage.getAbsolutePath());

            //Exit interface data of the image
            ExifInterface eiface = null;
            try {
                //Gets the exit interface data from the image
                eiface = new ExifInterface(Environment.getExternalStorageDirectory()+"/bluetoothMessenger/userProfile.jpg");
            } catch (IOException e) {
                e.printStackTrace();
            }
            //Gets the image orientation
            int orientation = eiface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);

            //Rotates the image to be right way up
            Bitmap rotatedBitmap = null;
            switch(orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotatedBitmap = rotateImg(bitmap, 270);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotatedBitmap = rotateImg(bitmap, 180);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotatedBitmap = rotateImg(bitmap, 90);
                    break;

                case ExifInterface.ORIENTATION_NORMAL:
                default:
                    rotatedBitmap = bitmap;
                    break;
            }
            image = rotatedBitmap;
            System.out.println("This is not working  my dyde "+ image);
        }
    }

    public static Bitmap rotateImg(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
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
                String usernameNew = newUsername.getText().toString();
                usernameNew = usernameNew.replace(pattern, "");
                newUsername.setText(usernameNew);
                db.updateLocalUser(usernameNew);
                db.updateMessageUsername(usernameNew, currentUsername);
                currentUsername = usernameNew;
                //Lets the user know it was updated
                Toast.makeText(SettingsActivity.this, "Username Updated!", Toast.LENGTH_LONG).show();
                c.close();
                db.close();
                username.setText(newUsername.getText().toString());
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