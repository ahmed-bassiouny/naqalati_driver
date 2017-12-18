package com.bassiouny.naqalati_driver.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RatingBar;

import com.bassiouny.naqalati_driver.R;
import com.bassiouny.naqalati_driver.helper.Constant;
import com.bassiouny.naqalati_driver.model.FirebaseRoot;
import com.google.firebase.database.FirebaseDatabase;

public class RateActivity extends AppCompatActivity implements View.OnClickListener {

    private RatingBar ratingBar;
    private String requestId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);
        ratingBar = findViewById(R.id.ratingBar);
        requestId = getIntent().getStringExtra(Constant.REQUEST_ID);
        findViewById(R.id.btn_send).setOnClickListener(this);
        findViewById(R.id.btn_cancel).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_send:
                if (requestId == null || requestId.isEmpty()) {
                    finish();
                } else {
                    FirebaseDatabase.getInstance().getReference(FirebaseRoot.DB_REQUESTS)
                            .child(requestId)
                            .child("rate_from_driver")
                            .setValue(ratingBar.getRating());
                    finish();
                }
                break;
            case R.id.btn_cancel:
                finish();
        }
    }

    @Override
    public void onBackPressed() {
    }
}
