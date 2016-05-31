package com.codeburrow.zapit;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.codeburrow.zapit.tasks.FindProductTask;

import org.json.JSONObject;

public class DetailActivity extends AppCompatActivity implements FindProductTask.FindProductResponse {

    private static final String LOG_TAG = DetailActivity.class.getSimpleName();

    private TextView mTextView;
    private String mNdefMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mTextView = (TextView) findViewById(R.id.text_view);

        Bundle intentExtras = getIntent().getExtras();
        mNdefMessage = intentExtras != null ? intentExtras.getString(ScanNfcActivity.NDEF_MESSAGE_EXTRA) : null;

        if (mNdefMessage != null) {
            new FindProductTask(this, mNdefMessage).execute();
        }

    }

    @Override
    public void onProcessFindProductFinish(JSONObject result) {
        mTextView.setText(result.toString());

        Log.e(LOG_TAG, result.toString());
    }
}
