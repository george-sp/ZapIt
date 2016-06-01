package com.codeburrow.zapit;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.codeburrow.zapit.api.ZapItApi;
import com.codeburrow.zapit.tasks.GetPaymentStatusTask;
import com.codeburrow.zapit.tasks.GetPaymentStatusTask.GetPaymentStatusResponse;

import org.json.JSONException;
import org.json.JSONObject;

public class PaymentActivity extends AppCompatActivity implements GetPaymentStatusResponse {

    private static final String LOG_TAG = PaymentActivity.class.getSimpleName();
    private String mProductSlug;
    private boolean mPaymentStatus = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        Bundle intentExtras = getIntent().getExtras();
        mProductSlug = intentExtras != null ? intentExtras.getString(DetailActivity.PRODUCT_SLUG_EXTRA) : null;

        if (mProductSlug != null) {
            new GetPaymentStatusTask(this, mProductSlug).execute();
        }

    }

    @Override
    public void onProcessGetPaymentStatusFinish(JSONObject result) {
        updatePaymentStatus(result);

        Log.e(LOG_TAG, "Value of mPayed: " + String.valueOf(mPaymentStatus));
    }

    private void updatePaymentStatus(JSONObject result) {
        try {
            mPaymentStatus = result.getJSONObject(ZapItApi.DATA).getBoolean(ZapItApi.PAYED);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage());
        }
    }
}
