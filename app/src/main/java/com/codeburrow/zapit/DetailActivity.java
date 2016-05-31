package com.codeburrow.zapit;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {

    private static final String LOG_TAG = DetailActivity.class.getSimpleName();

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mTextView = (TextView) findViewById(R.id.text_view);

        Bundle intentExtras = getIntent().getExtras();
        if (intentExtras != null) {
            String ndefMessage = intentExtras.getString(ScanNfcActivity.NDEF_MESSAGE_EXTRA);
            mTextView.setText(ndefMessage);
        }

    }

}
