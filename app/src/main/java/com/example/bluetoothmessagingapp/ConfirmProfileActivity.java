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

    File profileTemp = new File(Environment.getExternalStorageDirectory()+"/bluetoothMessenger/userProfile_temp.jpg");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_profile);
        loadImage();

        Button retakePhoto = findViewById(R.id.retakePhoto);
        retakePhoto.setOnClickListener(this);

        Button confirmPhoto = findViewById(R.id.confirmPhoto);
        confirmPhoto.setOnClickListener(this);

        Objects.requireNonNull(getSupportActionBar()).hide();
    }

    private void loadImage() {
        if(profileTemp.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(profileTemp.getAbsolutePath());

            ExifInterface eiface = null;
            try {
                eiface = new ExifInterface(Environment.getExternalStorageDirectory()+"/bluetoothMessenger/userProfile_temp.jpg");
            } catch (IOException e) {
                e.printStackTrace();
            }
            int orientation = eiface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);

            Bitmap rotatedBitmap = null;
            switch(orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotatedBitmap = rotateImg(bitmap, 90);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotatedBitmap = rotateImg(bitmap, 180);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotatedBitmap = rotateImg(bitmap, 270);
                    break;

                case ExifInterface.ORIENTATION_NORMAL:
                default:
                    rotatedBitmap = bitmap;
            }
            ImageView preview = findViewById(R.id.profilePreview);
            preview.setImageBitmap(rotatedBitmap);
        }
    }

    public static Bitmap rotateImg(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.retakePhoto) {
            finish();
        }else if(view.getId() == R.id.confirmPhoto) {
            try {
                saveProfile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Intent goToInstructions =  new Intent(ConfirmProfileActivity.this, InstructionsActivity.class);
            startActivity(goToInstructions);
        }
    }

    private void saveProfile() throws IOException {
        InputStream input = null;
        OutputStream output = null;

        try {
            input = new FileInputStream(profileTemp);
            output = new FileOutputStream(
            Environment.getExternalStorageDirectory()+"/bluetoothMessenger/userProfile.jpg"
            );
            byte[] buffer = new byte[1024];
            int length;
            while((length = input.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }
        }finally {
            input.close();
            output.close();
            profileTemp.delete();
        }
    }
}