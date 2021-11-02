package com.example.bluetoothmessagingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.Objects;

public class InstructionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructions);
        Objects.requireNonNull(getSupportActionBar()).hide();

        int[] images = {R.drawable.logo, R.drawable.ic_launcher_background, R.drawable.ic_launcher_foreground};

        ViewPager viewPager = (ViewPager) findViewById(R.id.instructionsPager);
        ViewPagerAdapater viewPagerAdapater = new ViewPagerAdapater(InstructionsActivity.this, images);
        viewPager.setAdapter(viewPagerAdapater);
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