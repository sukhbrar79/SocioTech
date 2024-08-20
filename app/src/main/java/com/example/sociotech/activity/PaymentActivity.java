package com.example.sociotech.activity;



import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sociotech.R;
import com.stripe.android.paymentsheet.PaymentSheet;
import com.stripe.android.paymentsheet.PaymentSheetResult;

//public
public class PaymentActivity extends AppCompatActivity {


    private PaymentSheet paymentSheet;
    private String paymentIntentClientSecret;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkout);

        // Initialize Stripe instance


        paymentSheet = new PaymentSheet(this, this::onPaymentSheetResult);

        // Fetch Payment Intent client secret from your server
        fetchPaymentIntentClientSecret();

        Button payButton = findViewById(R.id.payButton);
        payButton.setOnClickListener(v -> presentPaymentSheet());
    }

    private void fetchPaymentIntentClientSecret() {
        // TODO: Make a network request to your backend to create a PaymentIntent
        // and obtain its client secret. For now, mock this with a test client secret.
        // Replace this with actual network call to your backend.
        paymentIntentClientSecret = "test_client_secret"; // Replace with real client secret
    }

    private void presentPaymentSheet() {
        if (paymentIntentClientSecret != null) {
            paymentSheet.presentWithPaymentIntent(
                    paymentIntentClientSecret,
                    new PaymentSheet.Configuration("Example, Inc.")
            );
        } else {
            Toast.makeText(this, "Payment Intent not created", Toast.LENGTH_SHORT).show();
        }
    }

    private void onPaymentSheetResult(@NonNull PaymentSheetResult paymentSheetResult) {
        if (paymentSheetResult instanceof PaymentSheetResult.Completed) {
            Toast.makeText(this, "Payment successful", Toast.LENGTH_SHORT).show();
        } else if (paymentSheetResult instanceof PaymentSheetResult.Failed) {
            Toast.makeText(this, "Payment failed", Toast.LENGTH_SHORT).show();
        } else if (paymentSheetResult instanceof PaymentSheetResult.Canceled) {
            Toast.makeText(this, "Payment canceled", Toast.LENGTH_SHORT).show();
        }
    }
}