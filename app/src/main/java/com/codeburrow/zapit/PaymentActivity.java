package com.codeburrow.zapit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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
    private TextView mNamePurchaseTextView;
    private TextView mDescriptionPurchaseTextView;
    private ImageView mImageView;
//    private ProgressDialog progressDialog;

    private String mProductSlug;
    private boolean mPayedStatus = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        mNamePurchaseTextView = (TextView) findViewById(R.id.purchase_name_textview);
        mDescriptionPurchaseTextView = (TextView) findViewById(R.id.purchase_description_textview);
        mImageView = (ImageView) findViewById(R.id.imageview);
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

    private void updateProductInformation(JSONObject result) {
        try {
            mNamePurchaseTextView.setText(result.getJSONObject(ZapItApi.DATA).getString(ZapItApi.NAME));
            mDescriptionPurchaseTextView.setText(result.getJSONObject(ZapItApi.DATA).getString(ZapItApi.DESCRIPTION));
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage());
        }
    }

    public void requestPayment(View view) {
        new RequestPaymentTask(this, mProductSlug).execute();
    }

    public void requestScan(View view) {
        Intent scanIntent = new Intent(this, ScanNfcActivity.class);
        startActivity(scanIntent);
    }

    @Override
    public void onProcessGetPaymentStatusFinish(JSONObject result) {
        updatePayedStatus(result);
        updateProductInformation(result);

        if (!mPayedStatus) {
            mRequestPaymentButton.setEnabled(!mPayedStatus);
            mRequestPaymentButton.setBackgroundResource(R.color.colorPositive);
        } else {
            mImageView.setImageResource(R.drawable.not_available);
        }

        Log.e(LOG_TAG, "Value of mPayed: " + String.valueOf(mPayedStatus));
    }

    @Override
    public void onProcessRequestPaymentFinish(JSONObject result) {
        updatePayedStatus(result);

        if (mPayedStatus) {
            mImageView.setImageResource(R.drawable.completed);
            mRequestPaymentButton.setEnabled(!mPayedStatus);
            mRequestPaymentButton.setBackgroundResource(android.R.drawable.btn_default);
        }
    }
}