package com.example.msadsapp123;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

public class DialogActivity extends AppCompatActivity {

    private static final long DISPLAY_DURATION_MS = 5000; // Show for 5 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_layout);

        new Handler().postDelayed(this::finish, DISPLAY_DURATION_MS);
    }

}