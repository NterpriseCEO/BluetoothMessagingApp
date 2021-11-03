package com.example.bluetoothmessagingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.bluetoothmessagingapp.chatslist.ChatsActivity;

import java.util.Objects;

public class InstructionsActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructions);

        //The instruction images
        int[] images = {R.drawable.logo, R.drawable.ic_launcher_background, R.drawable.ic_launcher_foreground};

        //The swipeable list of images containing the instructions
        ViewPager viewPager = (ViewPager) findViewById(R.id.instructionsPager);
        //Sets the custom adapter that will render the images
        ViewPagerAdapater viewPagerAdapater = new ViewPagerAdapater(InstructionsActivity.this, images);
        viewPager.setAdapter(viewPagerAdapater);

        Button skipInstructions = findViewById(R.id.skipInstructions);
        skipInstructions.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        //Goes to the Chats activity page and clears the intent stack
        //This makes sure that you can't go back to the setup page
        if(view.getId() == R.id.skipInstructions) {
            Intent skipInstructions = new Intent(InstructionsActivity.this, ChatsActivity.class);
            skipInstructions.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(skipInstructions);
        }
    }

    class ViewPagerAdapater extends PagerAdapter {
        Context ctx;

        int[] images;

        LayoutInflater layoutInflater;

        public ViewPagerAdapater(Context context, int[] images) {
            this.ctx = context;
            this.images = images;
            layoutInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return images.length;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == ((LinearLayout) object);
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, final int position) {
            //References each image in list of images and displays it on the screen
            View itemView = layoutInflater.inflate(R.layout.item, container, false);
            ImageView imageView = itemView.findViewById(R.id.imageView);
            imageView.setImageResource(images[position]);
            Objects.requireNonNull(container).addView(itemView);

            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((LinearLayout) object);
        }
    }
}