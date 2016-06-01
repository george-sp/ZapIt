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
public class GetPaymentStatusTask extends AsyncTask<Void, Void, JSONObject> {

    private static final String LOG_TAG = GetPaymentStatusTask.class.getSimpleName();
    private GetPaymentStatusResponse asyncResponse = null;
    private String productSlug;

    public interface GetPaymentStatusResponse{

        void onProcessGetPaymentStatusFinish(JSONObject result);
    }

    public GetPaymentStatusTask(GetPaymentStatusResponse asyncResponse, String productSlug){
        this.asyncResponse = asyncResponse;
        this.productSlug = productSlug;
    }

    @Override
    protected JSONObject doInBackground(Void... params) {
        return PaymentApi.getPaymentStatus(productSlug);
    }

    @Override
    protected void onPostExecute(JSONObject productObject) {
        if (productObject != null){
            asyncResponse.onProcessGetPaymentStatusFinish(productObject);
        }
    }
}
