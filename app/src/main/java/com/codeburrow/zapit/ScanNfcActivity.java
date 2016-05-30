package com.codeburrow.zapit;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.codeburrow.zapit.tasks.ReadNdefTagTask;

/**
 * @author George Spiridakis <george@codeburrow.com>
 * @since May/30/2016.
 * ===================================================
 * ---------->    http://codeburrow.com    <----------
 * ===================================================
 */
public class ScanNfcActivity extends AppCompatActivity {

    private static final String LOG_TAG = ScanNfcActivity.class.getSimpleName();
    public static final String MIME_TEXT_PLAIN = "text/plain";

    private NfcAdapter mNfcAdapter;
    private boolean mNfcAdapterSupported;
    private boolean mNfcAdapterEnabled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_nfc);

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

        mNfcAdapterSupported = mNfcAdapter != null;
        mNfcAdapterEnabled = mNfcAdapter != null && mNfcAdapter.isEnabled();

        Log.e(LOG_TAG, "Supported: " + mNfcAdapterSupported);
        Log.e(LOG_TAG, "Enabled: " + mNfcAdapterEnabled);
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

                new ReadNdefTagTask().execute(tag);
            } else {
                Log.e(LOG_TAG, "Wrong mime type: " + type);
            }
        } else if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {
            // In case we still use the ACTION_TECH_DISCOVERED intent.
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            String[] techList = tag.getTechList();
            String searchedTech = Ndef.class.getName();

            for (String tech : techList) {
                if (searchedTech.equals(tech)) {

                    new ReadNdefTagTask().execute(tag);
                    break;
                }
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

        /*
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

}