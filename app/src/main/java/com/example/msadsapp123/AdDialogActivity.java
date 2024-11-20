package com.example.msadsapp123;

import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;

public class AdDialogActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Show the dialog fragment
        AdDialogFragment dialogFragment = new AdDialogFragment();
        dialogFragment.show(getSupportFragmentManager(), "AdDialogFragment");
    }
}
