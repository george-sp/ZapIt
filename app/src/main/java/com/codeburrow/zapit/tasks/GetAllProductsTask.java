package com.codeburrow.zapit.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.codeburrow.zapit.api.ProductApi;
import com.codeburrow.zapit.api.ZapItApi;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * @author George Spiridakis <george@codeburrow.com>
 * @since Jun/04/2016.
 * ===================================================
 * ---------->    http://codeburrow.com    <----------
 * ===================================================
 */
public class GetAllProductsTask extends AsyncTask<Void, Void, JSONArray> {

    private static final String LOG_TAG = GetAllProductsTask.class.getSimpleName();
    private GetAllProductsResponse asyncResponse = null;

    public interface GetAllProductsResponse {

        void onProcessGetAllProductsFinish(ArrayList<String> result);
    }

    public GetAllProductsTask(GetAllProductsResponse asyncResponse) {
        this.asyncResponse = asyncResponse;
    }

    @Override
    protected JSONArray doInBackground(Void... params) {
        return ProductApi.getAllProducts();
    }

    @Override
    protected void onPostExecute(JSONArray productsArray) {
        ArrayList<String> products = new ArrayList<>();

        for (int i = 0; i < (productsArray != null ? productsArray.length() : 0); i++) {
            try {
                products.add(productsArray.getJSONObject(i).getString(ZapItApi.SLUG));
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage());
            }
        }

        asyncResponse.onProcessGetAllProductsFinish(products);
    }
}