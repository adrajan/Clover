package com.example.msadsapp;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.util.Base64;
import android.util.Log;
import android.view.ViewGroup;
import android.view.Window;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.clover.connector.sdk.v3.DisplayConnector;
import com.clover.sdk.util.CloverAccount;
import com.clover.sdk.v3.connector.IDisplayConnector;
import com.clover.sdk.v3.connector.IDisplayConnectorListener;
import com.clover.sdk.v3.order.DisplayLineItem;
import com.clover.sdk.v3.order.DisplayOrder;
import com.clover.sdk.v3.order.OrderConnector;
import com.clover.sdk.v3.order.OrderV31Connector;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import android.widget.Toast;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.squareup.picasso.Picasso;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class OrderListenerService extends Service {

    private static final String TAG = "OrderListenerService";
    private OrderV31Connector mOrderConnector;
    private OrderConnector.OnOrderUpdateListener2 mOrderUpdateListener;
    private IDisplayConnector mDisplayConnector;
    private IDisplayConnectorListener mDisplayConnectorListener;
    private DisplayOrder mDisplayOrder;

    private FirebaseFirestore db;
    private String image;
    private String imageAsBase64;


    @Override
    public void onCreate() {
        super.onCreate();
        initializeOrderConnector();
        startForegroundService();
    }

    private void initializeOrderConnector() {
  // firebase Remote config for Dialog image for future

//        FirebaseRemoteConfig mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
//        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
//                .setMinimumFetchIntervalInSeconds(10) // Fetch every 10 seconds during testing
//                .build();
//        mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings);
//        mFirebaseRemoteConfig.fetchAndActivate()
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        boolean updated = task.getResult();
//                         image = mFirebaseRemoteConfig.getString("dialogImage");
//                        Log.d("RemoteConfig", "Config params updated: " + image);
//                        String imageUrl = "https://picsum.photos/200/300";
//                        downloadImageAndConvertToBase64(imageUrl);
//
//                    } else {
//                        Log.e("RemoteConfig", "Fetch failed");
//                    }
//                });

        mOrderUpdateListener = new OrderV31Connector.OnOrderUpdateListener2() {
            //Store itemId list
            @Override
            public void onOrderUpdated(String orderId, boolean selfChange) {
                Log.d("Main Activity", "Order Updated  " );
            }

            @Override
            public void onOrderCreated(String orderId) {
                Log.d("Main Activity", "Order Created  " +orderId);
            }

            @Override
            public void onOrderDeleted(String orderId) {
                Log.d("Main Activity", "Order deleted  " +orderId);
            }

            @Override
            public void onOrderDiscountAdded(String orderId, String discountId) {

            }

            @Override
            public void onOrderDiscountsDeleted(String orderId, List<String> discountIds) {

            }

            @Override
            public void onLineItemsAdded(String orderId, List<String> lineItemIds) {
                Log.d("Main Activity", "Added " +lineItemIds.size());
                mDisplayOrder = new DisplayOrder();
                DisplayLineItem item1 = new DisplayLineItem();
                item1.setName("Anthony");
                item1.setPrice("500");
            }

            @Override
            public void onLineItemsUpdated(String orderId, List<String> lineItemIds) {
                Log.d("Main Activity", "Updated " +lineItemIds.size());

            }

            @Override
            public void onLineItemsDeleted(String orderId, List<String> lineItemIds) {

            }

            @Override
            public void onLineItemModificationsAdded(String orderId, List<String> lineItemIds, List<String> modificationIds) {

            }

            @Override
            public void onLineItemDiscountsAdded(String orderId, List<String> lineItemIds, List<String> discountIds) {

            }

            @Override
            public void onLineItemExchanged(String orderId, String oldLineItemId, String newLineItemId) {

            }

            @Override
            public void onPaymentProcessed(String orderId, String paymentId) {
                Log.d("Main Activity", "Payment Processed " +paymentId);

                showOverlayDialog(orderId);
            }

            @Override
            public void onRefundProcessed(String orderId, String refundId) {

            }

            @Override
            public void onCreditProcessed(String orderId, String creditId) {

            }
        };

        mDisplayConnectorListener = new IDisplayConnectorListener() {
            @Override
            public void onDeviceDisconnected() {
                Log.d(TAG, "onDeviceDisconnected");
                mDisplayConnector.showMessage("Thank you for your purchase!");
            }

            @Override
            public void onDeviceConnected() {
                Log.d(TAG, "onDeviceConnected");
                mDisplayConnector.showMessage("Thank you for your purchase!");
            }
        };
        mDisplayConnector = new DisplayConnector(this, CloverAccount.getAccount(this), mDisplayConnectorListener);

        mOrderConnector = new OrderV31Connector(this, CloverAccount.getAccount(this), null);
        mOrderConnector.connect();
        mOrderConnector.addOnOrderChangedListener(mOrderUpdateListener);
    }
    // Method to download the image and convert to Base64
    private void downloadImageAndConvertToBase64(String imageUrl) {
        // Create an OkHttpClient instance
        OkHttpClient client = new OkHttpClient();

        // Create a request for the image URL
        Request request = new Request.Builder()
                .url(imageUrl)
                .build();

        // Execute the request in a background thread
        new Thread(() -> {
            try {
                // Make the HTTP request
                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    // Get the image bytes
                    byte[] imageBytes = response.body().bytes();

                    // Convert the byte array to Base64
                    imageAsBase64 = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                    Log.d("RemoteConfig", "Image converted to Base64: " + imageAsBase64);
                } else {
                    Log.e("RemoteConfig", "Image download failed");
                }
            } catch (Exception e) {
                Log.e("RemoteConfig", "Error while downloading image", e);
            }
        }).start();
    }

    private void showOverlayDialog(String orderId) {

        new Handler(Looper.getMainLooper()).post(() -> {
            WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
            WindowManager.LayoutParams params;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                params = new WindowManager.LayoutParams(
                        WindowManager.LayoutParams.WRAP_CONTENT,
                        WindowManager.LayoutParams.WRAP_CONTENT,
                        WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                                | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                                | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                        PixelFormat.TRANSLUCENT);
            } else {
                params = new WindowManager.LayoutParams(
                        WindowManager.LayoutParams.WRAP_CONTENT,
                        WindowManager.LayoutParams.WRAP_CONTENT,
                        WindowManager.LayoutParams.TYPE_PHONE,
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                                | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                                | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                        PixelFormat.TRANSLUCENT);
            }

            params.gravity = Gravity.CENTER;

            // Inflate the custom view for the overlay
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View overlayView = inflater.inflate(R.layout.dialog_layout, null);
            // Set the image for the overlay (e.g., an icon or logo)
            ImageView imageView = overlayView.findViewById(R.id.overlay_image);

            // show image from remote config
//            byte[] decodedBytes = Base64.decode(imageAsBase64, Base64.DEFAULT);
//            // Convert byte array to Bitmap
//            Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
//            // Set the Bitmap to ImageView
//            imageView.setImageBitmap(decodedBitmap);
            imageView.setImageResource(R.drawable.thankyou);
            // Handle the close button (image) click to remove the overlay
            ImageView closeButton = overlayView.findViewById(R.id.close_button);
            closeButton.setOnClickListener(v -> {
                windowManager.removeView(overlayView);  // Remove the overlay when clicked
            });

            // Add the view to the window
            windowManager.addView(overlayView, params);

            // Automatically remove the overlay after a delay (e.g., 5 seconds)
//            new Handler().postDelayed(() -> windowManager.removeView(overlayView), 5000);
        });
    }

    @SuppressLint("ForegroundServiceType")
    private void startForegroundService() {
        // Create a persistent notification for the foreground service
        Notification notification = new NotificationCompat.Builder(this, "CHANNEL_ID")
                .setContentTitle("Listening for Orders")
                .setContentText("Order listener is running in the background")
                .setSmallIcon(R.drawable.ic_notification_icon)
                .build();

        startForeground(1, notification);  // Start the service in the foreground with the notification
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;  // Keeps the service running until explicitly stopped
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mOrderConnector != null) {
            mOrderConnector.addOnOrderChangedListener(mOrderUpdateListener);
            mOrderConnector.disconnect();
            mOrderConnector = null;
        }
        if(mDisplayConnector!=null){
            mDisplayConnector.dispose();
            mDisplayConnector=null;
        }
        Log.d(TAG, "OrderListenerService stopped.");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;  // This is a started service, not a bound service
    }
}
