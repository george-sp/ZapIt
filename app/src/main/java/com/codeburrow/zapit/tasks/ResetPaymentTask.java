package com.codeburrow.zapit.tasks;

import android.os.AsyncTask;

import com.codeburrow.zapit.api.PaymentApi;

import org.json.JSONObject;

/**
 * @author George Spiridakis <george@codeburrow.com>
 * @since Jun/01/2016.
 * ===================================================
 * ---------->    http://codeburrow.com    <----------
 * ===================================================
 */
public class ResetPaymentTask extends AsyncTask<Void, Void, JSONObject> {

    private static final String LOG_TAG = RequestPaymentTask.class.getSimpleName();
    private ResetPaymentResponse asyncResponse = null;
    private String productSlug;

    public interface ResetPaymentResponse {

        void onProcessResetPaymentFinish(JSONObject result);
    }

    public ResetPaymentTask(ResetPaymentResponse asyncResponse, String productSlug) {
        this.asyncResponse = asyncResponse;
        this.productSlug = productSlug;
    }

    @Override
    protected JSONObject doInBackground(Void... params) {
        return PaymentApi.resetPayment(productSlug);
    }

    @Override
    protected void onPostExecute(JSONObject productObject) {
        if (productObject != null) {
            asyncResponse.onProcessResetPaymentFinish(productObject);
        }
    }
}