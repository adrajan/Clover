package com.example.msadsapp;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;
import android.util.Log;
import androidx.core.app.NotificationCompat;

import com.clover.connector.sdk.v3.DisplayConnector;
import com.clover.sdk.util.CloverAccount;
import com.clover.sdk.v1.BindingException;
import com.clover.sdk.v1.ClientException;
import com.clover.sdk.v1.ServiceConnector;
import com.clover.sdk.v1.ServiceException;
import com.clover.sdk.v3.connector.IDisplayConnector;
import com.clover.sdk.v3.connector.IDisplayConnectorListener;
import com.clover.sdk.v3.order.DisplayLineItem;
import com.clover.sdk.v3.order.DisplayOrder;
import com.clover.sdk.v3.order.Order;
import com.clover.sdk.v3.order.OrderConnector;
import com.clover.sdk.v3.order.OrderV31Connector;
import com.clover.connector.sdk.v3.DisplayV3Connector;
import com.clover.connector.sdk.v3.DisplayIntent;


import java.util.List;

public class OrderListenerService extends Service {

    private static final String TAG = "OrderListenerService";
    private OrderV31Connector mOrderConnector;
    private OrderConnector.OnOrderUpdateListener2 mOrderUpdateListener;
    private IDisplayConnector mDisplayConnector;
    private IDisplayConnectorListener mDisplayConnectorListener;
    private DisplayOrder mDisplayOrder;

    @Override
    public void onCreate() {
        super.onCreate();
        initializeOrderConnector();
        startForegroundService();
    }

    private void initializeOrderConnector() {
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
                handleOrderUpdate(orderId);
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

    private void handleOrderUpdate(String orderId) {
        Log.d(TAG, "Payment complete !!");
        //mDisplayConnector.showMessage("Order Completed by MS !!");

        mDisplayConnector.showDisplayOrder(mDisplayOrder);

        /*Intent intent = new Intent(this, AdDialogActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);*/

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
