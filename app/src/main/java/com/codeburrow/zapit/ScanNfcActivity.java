package com.codeburrow.zapit;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import com.codeburrow.zapit.tasks.GetAllProductsTask;
import com.codeburrow.zapit.tasks.GetAllProductsTask.GetAllProductsResponse;
import com.codeburrow.zapit.tasks.ReadNdefTagTask;
import com.codeburrow.zapit.tasks.ReadNdefTagTask.ReadNdefTagResponse;
import com.codeburrow.zapit.tasks.ResetPaymentTask;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * @author George Spiridakis <george@codeburrow.com>
 * @since May/30/2016.
 * ===================================================
 * ---------->    http://codeburrow.com    <----------
 * ===================================================
 */
public class ScanNfcActivity extends AppCompatActivity implements ReadNdefTagResponse, GetAllProductsResponse, ResetPaymentTask.ResetPaymentResponse {

    private static final String LOG_TAG = ScanNfcActivity.class.getSimpleName();
    public static final String NDEF_MESSAGE_EXTRA = "ndef_message_extra";
    public static final String MIME_TEXT_PLAIN = "text/plain";

    private boolean mCustomerMode;
    private boolean mMerchantMode;
    private boolean mResetMode;

    private NfcAdapter mNfcAdapter;
    private boolean mNfcAdapterSupported;
    private boolean mNfcAdapterEnabled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_nfc);

        setScanMode();

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

        mNfcAdapterSupported = mNfcAdapter != null;
        mNfcAdapterEnabled = mNfcAdapter != null && mNfcAdapter.isEnabled();

        Log.e(LOG_TAG, "Supported: " + mNfcAdapterSupported);
        Log.e(LOG_TAG, "Enabled: " + mNfcAdapterEnabled);

        handleIntent(getIntent());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mNfcAdapterSupported) setupForegroundDispatch(this, mNfcAdapter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mNfcAdapterSupported) stopForegroundDispatch(this, mNfcAdapter);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        String action = intent.getAction();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            String type = intent.getType();
            if (MIME_TEXT_PLAIN.equals(type)) {
                Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

                new ReadNdefTagTask(this).execute(tag);
            } else {
                Log.e(LOG_TAG, "Wrong mime type: " + type);
            }
        }
    }

    /**
     * Helper Method
     *
     * @param activity   the Activity to dispatch to.
     * @param nfcAdapter the NFC Adapter used for the foreground dispatch.
     */
    public static void setupForegroundDispatch(final Activity activity, NfcAdapter nfcAdapter) {
        final Intent intent = new Intent(activity.getApplicationContext(), activity.getClass());
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        final PendingIntent pendingIntent = PendingIntent.getActivity(activity.getApplicationContext(), 0, intent, 0);

        IntentFilter[] filters = new IntentFilter[1];
        String[][] techList = new String[][]{};

        /**
         * Note: Use the same intent-filter as in your AndroidManifest.xml.
         */
        filters[0] = new IntentFilter();
        filters[0].addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
        filters[0].addCategory(Intent.CATEGORY_DEFAULT);
        try {
            filters[0].addDataType(MIME_TEXT_PLAIN);
        } catch (IntentFilter.MalformedMimeTypeException e) {
            throw new RuntimeException("Check your mime type." + e.getMessage());
        }

        nfcAdapter.enableForegroundDispatch(activity, pendingIntent, filters, techList);
    }

    /**
     * Helper Method
     *
     * @param activity   the Activity requesting to disable the foreground dispatch.
     * @param nfcAdapter the NFC Adapter used for the foreground dispatch.
     */
    public static void stopForegroundDispatch(final Activity activity, NfcAdapter nfcAdapter) {
        nfcAdapter.disableForegroundDispatch(activity);
    }

    /**
     * This method runs when the async task that reads the tag -
     * ReadNdefTagTask runs the onPostExecute.
     * The result of the ReadNdefTagTask.
     *
     * @param productSlug The message that is stored in the tag.
     */
    @Override
    public void onProcessReadNdefTagFinish(String productSlug) {
        Log.e(LOG_TAG, "NDEF message: " + productSlug);

        if (mCustomerMode) {
            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra(NDEF_MESSAGE_EXTRA, productSlug);
            startActivity(intent);
        } else if (mMerchantMode) {

        } else if (mResetMode) {
            new ResetPaymentTask(this, productSlug).execute();
        }

    }

    /**
     * This method runs when the async task that reads the tag -
     * GetAllProductsTask runs the onPostExecute.
     * The result of the GetAllProductsTask.
     *
     * @param productsArray A JSONArray with all products.
     */
    @Override
    public void onProcessGetAllProductsFinish(ArrayList<String> productsArray) {
        Log.e(LOG_TAG, productsArray.toString());

        for (String product : productsArray) {
            new ResetPaymentTask(this, product).execute();
        }
    }

    /**
     * This method runs when the async task that reads the tag -
     * ResetPaymentTask runs the onPostExecute.
     * The result of the ResetPaymentTask.
     *
     * @param productObject A JSONObject with product's information.
     */
    @Override
    public void onProcessResetPaymentFinish(JSONObject productObject) {
        Log.e(LOG_TAG, productObject.toString());

        Toast.makeText(ScanNfcActivity.this, productObject.toString(), Toast.LENGTH_SHORT).show();
    }

    public void resetAllPayedStatus(View view) {
        new GetAllProductsTask(this).execute();
    }

    public void onRadioButtonClicked(View view) {
        setScanMode();
    }

    private void setScanMode() {
        mCustomerMode = ((RadioButton) findViewById(R.id.customer_radiobutton)).isChecked();
        mMerchantMode = ((RadioButton) findViewById(R.id.merchant_radiobutton)).isChecked();
        mResetMode = ((RadioButton) findViewById(R.id.reset_radiobutton)).isChecked();
    }
}