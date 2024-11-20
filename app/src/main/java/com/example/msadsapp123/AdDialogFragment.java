package com.example.msadsapp123;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.bumptech.glide.Glide;

public class AdDialogFragment extends DialogFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the custom dialog layout
        View view = inflater.inflate(R.layout.fragment_ad_dialog, container, false);

        // Load the image into the ImageView
        ImageView adImageView = view.findViewById(R.id.adImageView);
        Glide.with(this)
                .load(R.drawable.barbie)  // Replace with your drawable or image URL
                .into(adImageView);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null && dialog.getWindow() != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }
}

