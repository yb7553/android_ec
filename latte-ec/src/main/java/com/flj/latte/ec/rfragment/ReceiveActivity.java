package com.flj.latte.ec.rfragment;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;


import com.flj.latte.ec.R;


public class ReceiveActivity extends AppCompatActivity {

    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive);
        tv = (TextView) findViewById(R.id.tv);
        String pushData = getIntent().getStringExtra("pushData");
        tv.setText(pushData);

    }
}
