package com.example.bluetoothmessagingapp.chatpage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bluetoothmessagingapp.R;

import java.io.File;
import java.io.IOException;
import java.util.List;

class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
    List<String[]> messagesList;
    Context ctx;
    File profileImage = new File(Environment.getExternalStorageDirectory()+"/bluetoothMessenger/userProfile.jpg");
    Bitmap image;
    public MessageAdapter(List messagesList, Context ctx) {
        this.messagesList = messagesList;
        this.ctx = ctx;

        loadImage();
    }

    @NonNull
    @Override
    public MessageAdapter.MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Creates a recyclerView list from the chats_item template
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.message, parent, false);
        return new MessageAdapter.MessageViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MessageAdapter.MessageViewHolder holder, int position) {
        //Sets the correct text of the current item being rendered
        System.out.println(messagesList.get(position)[1]);
        holder.messageContents.setText(messagesList.get(position)[0]);
        holder.username.setText(messagesList.get(position)[1]);
        if(new Boolean(messagesList.get(position)[2])) {
            //Sets the source of the imageView to be the new rotated image
            System.out.println(image);
            holder.profile.setImageBitmap(image);
        }
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
    public int getItemCount() {
        //Returns the amount of items in the recyclerView
        return messagesList.size();
    }

    class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView username;
        TextView messageContents;
        //Sets the source of the imageView to be the new rotated image
        ImageView profile;
        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            //A reference to the textView in each list item
            messageContents = itemView.findViewById(R.id.messageContents);
            username = itemView.findViewById(R.id.username);
            profile = itemView.findViewById(R.id.userIcon);
        }
    }
}