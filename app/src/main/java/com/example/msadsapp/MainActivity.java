package com.example.msadsapp;

import android.accounts.Account;
import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import androidx.appcompat.app.AppCompatActivity;

import android.provider.Settings;
import android.util.Log;

import com.clover.sdk.util.CloverAccount;
import com.clover.sdk.v1.BindingException;
import com.clover.sdk.v1.ClientException;
import com.clover.sdk.v1.ServiceException;
import com.clover.sdk.v3.order.FulfillmentInfo;
import com.clover.sdk.v3.order.LineItem;
import com.clover.sdk.v3.order.Order;
import com.clover.sdk.v3.order.OrderConnector;
import com.clover.sdk.v3.order.OrderV31Connector;
import com.clover.sdk.v3.order.OrderV31Connector.OnOrderUpdateListener;
import com.clover.sdk.v3.order.OrderV31Connector.OnOrderUpdateListener2;
import com.clover.sdk.v3.order.PaymentState;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Account mAccount;
    private OrderV31Connector mOrderConnector;
    private OrderV31Connector.OnOrderUpdateListener2 mOrderUpdateListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        FirebaseRemoteConfig mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
//        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
//                .setMinimumFetchIntervalInSeconds(10) // Fetch every 10 seconds during testing
//                .build();
//        mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings);
//        mFirebaseRemoteConfig.fetchAndActivate()
//                .addOnCompleteListener(this, task -> {
//                    if (task.isSuccessful()) {
//                        boolean updated = task.getResult();
//                        String welcomeMessage = mFirebaseRemoteConfig.getString("image1");
//                        Log.d("RemoteConfig", "Config params updated: " + welcomeMessage);
//
//                    } else {
//                        Log.e("RemoteConfig", "Fetch failed");
//                    }
//                });
        // Create the notification channel for the foreground service
        createNotificationChannel();

        // Start the OrderListenerService as a foreground service
        Intent serviceIntent = new Intent(this, OrderListenerService.class);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent);  // Use startForegroundService() for Android 8.0+
        } else {
            startService(serviceIntent);  // Use startService() for lower Android versions
        }
    }

    private void createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "CHANNEL_ID",                       // Same ID used in the service
                    "Order Listener Channel",            // Channel name shown to users
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            channel.setDescription("Listening for order events");

            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }



    // Handle the completion of an order by launching AdActivity
    private void handleOrderCompletion(String orderId) {
        Log.d("MainActivity", "Payment complete !!");
    //if (order != null &&  Order.STATE_COMPLETED.equals(order.getState())) {
        // Launch AdActivity to show the post-checkout content
        //List<LineItem> l = order.getLineItems();
        Intent intent = new Intent(this, AdActivity.class);
        intent.putExtra("ORDER_ID", orderId); // Pass order details if needed
        startActivity(intent);
    }
}
