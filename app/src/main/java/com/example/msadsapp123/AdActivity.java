package com.example.msadsapp123;

import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;

public class AdActivity extends AppCompatActivity {

    private static final long DISPLAY_DURATION_MS = 5000; // Show for 5 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad);

        // ImageView to display an image
        ImageView adImageView = findViewById(R.id.adImageView);
        Glide.with(this)
                .load(R.drawable.barbie) // Replace with your image URL
                .into(adImageView);

        // Close the activity after a delay
        new Handler().postDelayed(this::finish, DISPLAY_DURATION_MS);
    }
}
