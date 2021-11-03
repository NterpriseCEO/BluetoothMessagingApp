package com.example.bluetoothmessagingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;

public class ConfirmProfileActivity extends AppCompatActivity implements View.OnClickListener {
    //File path to the temporary profile image create
    //A temporary image was created so as not to override the current profile image
    File profileTemp = new File(Environment.getExternalStorageDirectory()+"/bluetoothMessenger/userProfile_temp.jpg");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_profile);
        //Loads the image that the user took
        loadImage();

        Button retakePhoto = findViewById(R.id.retakePhoto);
        retakePhoto.setOnClickListener(this);

        Button confirmPhoto = findViewById(R.id.confirmPhoto);
        confirmPhoto.setOnClickListener(this);
    }

    private void loadImage() {
        //Checks if the image exists
        if(profileTemp.exists()) {
            //Converts the image to a bitmap
            Bitmap bitmap = BitmapFactory.decodeFile(profileTemp.getAbsolutePath());

            //Exit interface data of the image
            ExifInterface eiface = null;
            try {
                //Gets the exit interface data from the image
                eiface = new ExifInterface(Environment.getExternalStorageDirectory()+"/bluetoothMessenger/userProfile_temp.jpg");
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
            }
            //Sets the source of the imageView to be the new rotated image
            ImageView preview = findViewById(R.id.profilePreview);
            preview.setImageBitmap(rotatedBitmap);
        }
    }
    //Rotates tbe image by x degrees and returns the new image
    public static Bitmap rotateImg(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    @Override
    public void onClick(View view) {
        //Goes back to the camera
        if(view.getId() == R.id.retakePhoto) {
            finish();
        }else if(view.getId() == R.id.confirmPhoto) {
            //Saves the image as the official profile image of the user
            try {
                saveProfile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //Goes to the instructions page
            Intent goToInstructions =  new Intent(ConfirmProfileActivity.this, InstructionsActivity.class);
            startActivity(goToInstructions);
        }
    }
    //Saves the profile iamge
    private void saveProfile() throws IOException {
        //The temporary profile image
        InputStream input = null;
        //The userProfile image
        OutputStream output = null;

        try {
            //Reads the temporary image in
            input = new FileInputStream(profileTemp);
            //Creates the temporary image
            output = new FileOutputStream(
            Environment.getExternalStorageDirectory()+"/bluetoothMessenger/userProfile.jpg"
            );
            //Creates a byte buffer
            byte[] buffer = new byte[1024];
            int length;
            //Checks if there is still write to load
            //and writes it to userProfile.png
            while((length = input.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }
        }finally {
            //Closes both file streams and deletes the temporary profile image
            input.close();
            output.close();
            profileTemp.delete();
        }
    }
}