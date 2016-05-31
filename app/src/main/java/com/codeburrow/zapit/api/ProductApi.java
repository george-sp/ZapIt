package com.codeburrow.zapit.api;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author George Spiridakis <george@codeburrow.com>
 * @since May/31/2016.
 * ===================================================
 * ---------->    http://codeburrow.com    <----------
 * ===================================================
 */
public class ProductApi extends ZapItApi {

    private static final String LOG_TAG = ProductApi.class.getSimpleName();
    private static final String API_URL = "https://zapit-web.herokuapp.com/api/v1/products";

    /**
     * @return A JSONArray with all the products
     */
    public static JSONArray getAllProducts() {
        JSONObject jsonResponse = makeGetRequest(API_URL, null);
        try {
            return jsonResponse.getJSONArray(ZapItApi.DATA);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage());
        }
        return null;
    }

    /**
     * Search the product list for products with this slug.
     *
     * @param productSlug
     * @return The details (name, price, description, payed-status) of the product.
     */
    public static JSONObject findProductBySlug(String productSlug) {
        JSONArray productsArray = getAllProducts();

        for (int i = 0; i < (productsArray != null ? productsArray.length() : 0); i++) {
            try {
                if (productSlug.equals(productsArray.getJSONObject(i).getString(ZapItApi.SLUG))) {
                    return productsArray.getJSONObject(i);
                }
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage());
            }
        }

        return null;
    }

}
