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
public class RequestPaymentTask extends AsyncTask<Void, Void, JSONObject> {

    private static final String LOG_TAG = RequestPaymentTask.class.getSimpleName();
    private RequestPaymentResponse asyncResponse = null;
    private String productSlug;

    public interface RequestPaymentResponse {

        void onProcessRequestPaymentFinish(JSONObject result);
    }

    public RequestPaymentTask(RequestPaymentResponse asyncResponse, String productSlug) {
        this.asyncResponse = asyncResponse;
        this.productSlug = productSlug;
    }

    @Override
    protected JSONObject doInBackground(Void... params) {
        return PaymentApi.requestPayment(productSlug);
    }

    @Override
    protected void onPostExecute(JSONObject productObject) {
        if (productObject != null){
            asyncResponse.onProcessRequestPaymentFinish(productObject);
        }
    }

}
