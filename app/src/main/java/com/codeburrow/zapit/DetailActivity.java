package com.codeburrow.zapit;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.codeburrow.zapit.api.ZapItApi;
import com.codeburrow.zapit.tasks.FindProductTask;

import org.json.JSONException;
import org.json.JSONObject;

public class DetailActivity extends AppCompatActivity implements FindProductTask.FindProductResponse {

    private static final String LOG_TAG = DetailActivity.class.getSimpleName();

    private TextView mSlugTextView;
    private TextView mNameTextView;
    private TextView mPriceTextView;
    private TextView mDescriptionTextView;
    private TextView mPayedTextView;

    private String mNdefMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mSlugTextView = (TextView) findViewById(R.id.detail_slug_textview);
        mNameTextView = (TextView) findViewById(R.id.detail_name_textview);
        mPriceTextView = (TextView) findViewById(R.id.detail_price_textview);
        mDescriptionTextView = (TextView) findViewById(R.id.detail_description_textview);
        mPayedTextView = (TextView) findViewById(R.id.detail_payed_textview);

        Bundle intentExtras = getIntent().getExtras();
        mNdefMessage = intentExtras != null ? intentExtras.getString(ScanNfcActivity.NDEF_MESSAGE_EXTRA) : null;

        if (mNdefMessage != null) {
            new FindProductTask(this, mNdefMessage).execute();
        }

    }

    @Override
    public void onProcessFindProductFinish(JSONObject result) {
        getProductDetailsFromJson(result);

        Log.e(LOG_TAG, result.toString());
    }

    private void getProductDetailsFromJson(JSONObject productJson) {
        try {
            mSlugTextView.setText(productJson.getString(ZapItApi.SLUG));
            mNameTextView.setText(productJson.getString(ZapItApi.NAME));
            mPriceTextView.setText(productJson.getString(ZapItApi.PRICE));
            mDescriptionTextView.setText(productJson.getString(ZapItApi.DESCRIPTION));
            mPayedTextView.setText(String.format("%s", productJson.getBoolean(ZapItApi.PAYED)));
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage());
        }
    }

}
