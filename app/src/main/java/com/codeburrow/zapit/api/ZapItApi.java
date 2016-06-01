package com.codeburrow.zapit.api;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author George Spiridakis <george@codeburrow.com>
 * @since May/31/2016.
 * ===================================================
 * ---------->    http://codeburrow.com    <----------
 * ===================================================
 */

/**
 * See the ZapIt API documentation here:
 * https://zapit-web.herokuapp.com/apidoc/#api-Products  <--- (ctr + click)
 */
public class ZapItApi {

    private static final String LOG_TAG = ZapItApi.class.getSimpleName();
    public static final String STATUS_CODE = "status_code";
    public static final String DATA = "data";
    public static final String SLUG = "slug";
    public static final String NAME = "name";
    public static final String PRICE = "price";
    public static final String DESCRIPTION = "description";
    public static final String PAYED = "payed";
    public static final String PRODUCT_SLUG_QUERY_PARAM = "product-slug";


    public static JSONObject makeGetRequest(String url, List<NameValuePair> params) {
        if (null == params) {
            params = new ArrayList<>();
        }

        String paramString = URLEncodedUtils.format(params, "UTF-8");
        url += "?" + paramString;
        HttpGet httpGet = new HttpGet(url);

        Log.e(LOG_TAG, url);

        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpResponse httpResponse = null;

        try {
            httpResponse = httpClient.execute(httpGet);
            return convertHttpResponse(httpResponse.getEntity().getContent());
        } catch (Exception e) {
            Log.e(LOG_TAG, e.getMessage());
        }
        return null;
    }

    private static JSONObject convertHttpResponse(InputStream httpEntityContent) {
        String json = null;

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    httpEntityContent, "UTF-8"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            httpEntityContent.close();
            json = sb.toString();
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }

        JSONObject jsonObject = null;

        try {
            jsonObject = new JSONObject(json);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }

        return jsonObject;
    }

}
