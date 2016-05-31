package com.codeburrow.zapit.tasks;

import android.os.AsyncTask;

import com.codeburrow.zapit.api.ProductApi;

import org.json.JSONObject;

/**
 * @author George Spiridakis <george@codeburrow.com>
 * @since May/31/2016.
 * ===================================================
 * ---------->    http://codeburrow.com    <----------
 * ===================================================
 */
public class FindProductTask extends AsyncTask<Void, Void, JSONObject> {

    private static final String LOG_TAG = FindProductTask.class.getSimpleName();
    private FindProductResponse asyncResponse = null;
    private String productSlug;

    public interface FindProductResponse{

        void onProcessFindProductFinish(JSONObject result);
    }

    public FindProductTask(FindProductResponse asyncResponse, String productSlug){
        this.asyncResponse = asyncResponse;
        this.productSlug = productSlug;
    }

    @Override
    protected JSONObject doInBackground(Void... params) {
        return ProductApi.findProductBySlug(productSlug);
    }

    @Override
    protected void onPostExecute(JSONObject productObject) {
        if (productObject != null){
            asyncResponse.onProcessFindProductFinish(productObject);
        }
    }
}
