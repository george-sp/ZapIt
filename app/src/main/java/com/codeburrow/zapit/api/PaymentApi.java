package com.codeburrow.zapit.api;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author George Spiridakis <george@codeburrow.com>
 * @since Jun/01/2016.
 * ===================================================
 * ---------->    http://codeburrow.com    <----------
 * ===================================================
 */
public class PaymentApi extends ZapItApi {

    private static final String LOG_TAG = PaymentApi.class.getSimpleName();
    private static final String API_STATUS_URL = "https://zapit-web.herokuapp.com/api/v1/products/payment/status";
    private static final String API_REQUEST_URL = "https://zapit-web.herokuapp.com/api/v1/products/payment/request";
    private static final String API_RESET_URL = "https://zapit-web.herokuapp.com/api/v1/products/payment/reset";

    public static JSONObject getPaymentStatus(String productSlug) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair(PRODUCT_SLUG_QUERY_PARAM, productSlug));

        return makeGetRequest(API_STATUS_URL, params);
    }

    public static JSONObject requestPayment(String productSlug){
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair(PRODUCT_SLUG_QUERY_PARAM, productSlug));

        return makeGetRequest(API_REQUEST_URL, params);
    }

    public static JSONObject resetPayment(String productSlug){
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair(PRODUCT_SLUG_QUERY_PARAM, productSlug));

        return makeGetRequest(API_RESET_URL, params);
    }

}