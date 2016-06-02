package com.codeburrow.zapit;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.codeburrow.zapit.api.ZapItApi;
import com.codeburrow.zapit.tasks.GetPaymentStatusTask;
import com.codeburrow.zapit.tasks.GetPaymentStatusTask.GetPaymentStatusResponse;
import com.codeburrow.zapit.tasks.RequestPaymentTask;
import com.codeburrow.zapit.tasks.RequestPaymentTask.RequestPaymentResponse;

import org.json.JSONException;
import org.json.JSONObject;

public class PaymentActivity extends AppCompatActivity implements GetPaymentStatusResponse, RequestPaymentResponse {

    private static final String LOG_TAG = PaymentActivity.class.getSimpleName();

    private Button mRequestPaymentButton;

    private String mProductSlug;
    private boolean mPayedStatus = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        mRequestPaymentButton = (Button) findViewById(R.id.request_payment_button);
        mRequestPaymentButton.setEnabled(mPayedStatus);

        Bundle intentExtras = getIntent().getExtras();
        mProductSlug = intentExtras != null ? intentExtras.getString(DetailActivity.PRODUCT_SLUG_EXTRA) : null;

        if (mProductSlug != null) {
            new GetPaymentStatusTask(this, mProductSlug).execute();
        }

    }

    private void updatePayedStatus(JSONObject result) {
        try {
            mPayedStatus = result.getJSONObject(ZapItApi.DATA).getBoolean(ZapItApi.PAYED);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage());
        }
    }

    public void requestPayment(View view) {
        new RequestPaymentTask(this, mProductSlug).execute();
    }

    @Override
    public void onProcessGetPaymentStatusFinish(JSONObject result) {
        updatePayedStatus(result);


        if (!mPayedStatus) {
            mRequestPaymentButton.setEnabled(!mPayedStatus);
            mRequestPaymentButton.setBackgroundResource(android.R.color.holo_green_light);
        }

        Log.e(LOG_TAG, "Value of mPayed: " + String.valueOf(mPayedStatus));
    }

    @Override
    public void onProcessRequestPaymentFinish(JSONObject result) {
        updatePayedStatus(result);

        if (mPayedStatus) {
            mRequestPaymentButton.setEnabled(!mPayedStatus);
            mRequestPaymentButton.setBackgroundResource(android.R.drawable.btn_default);
        }
    }
}