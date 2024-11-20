package com.example.msadsapp123;

import static com.clover.sdk.v1.Intents.EXTRA_ORDER_ID;

import android.accounts.Account;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.clover.sdk.util.CloverAccount;
import com.clover.sdk.v3.order.Order;

import androidx.appcompat.app.AppCompatActivity;
import com.clover.sdk.v3.order.OrderConnector;
import com.clover.sdk.v1.Intents;
import com.clover.sdk.v3.payments.Payment;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PaymentProcessedActivity extends AppCompatActivity {

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_successful);
        context = this;
        // Retrieve data from intent
        String orderId = getIntent().getStringExtra("ORDER_ID");
        String paymentId = getIntent().getStringExtra("PAYMENT_ID");
        // Find buttons by their IDs

        TextView paymentText = findViewById(R.id.payment_text);

         final ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
         singleThreadExecutor.execute(new Runnable() {
             @Override
             public void run() {
                 Account account = CloverAccount.getAccount(context);
                 OrderConnector orderConnector = new OrderConnector(context , account, null);
                 Order orderData;
                 try {
                     orderData = orderConnector.getOrder(orderId);
                     List<Payment> data  = orderData.getPayments();
                     Long result = 0L;
                     for(int i = 0 ; i < data.size() ; i++){
                         result += data.get(i).getAmount();
                     }
                     double formattedResult = result / 100.0;
                     String formattedString = String.format("%.2f", formattedResult);
                     paymentText.setText("Paid £"+result);


                 } catch (Exception e) {
                     throw new RuntimeException(e);
                 }

             }
         });



        Button buttonReceipt = findViewById(R.id.button_no_receipt);

        // Set click listeners
        buttonReceipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent dror = new Intent(Intents.ACTION_START_PRINT_RECEIPTS);
                dror.putExtra(EXTRA_ORDER_ID, orderId);
                startActivity(dror);
            }
        });

        // Update UI if needed

    }


}
