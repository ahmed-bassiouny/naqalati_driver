package com.bassiouny.naqalati_driver.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bassiouny.naqalati_driver.R;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView ivMap;
    private TextView tvMap;
    private ImageView ivFiles;
    private TextView tvFiles;
    private ImageView ivContact;
    private TextView tvContact;
    private ImageView ivMessage;
    private TextView tvMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ivMap = findViewById(R.id.iv_map);
        tvMap = findViewById(R.id.tv_map);
        ivFiles = findViewById(R.id.iv_files);
        tvFiles = findViewById(R.id.tv_files);
        ivContact = findViewById(R.id.iv_contact);
        tvContact = findViewById(R.id.tv_contact);
        ivMessage = findViewById(R.id.iv_message);
        tvMessage = findViewById(R.id.tv_message);

        ivMap.setOnClickListener(this);
        tvMap.setOnClickListener(this);
        ivFiles.setOnClickListener(this);
        tvFiles.setOnClickListener(this);
        ivContact.setOnClickListener(this);
        tvContact.setOnClickListener(this);
        ivMessage.setOnClickListener(this);
        tvMessage.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_map:
            case R.id.tv_map:
                startActivity(new Intent(HomeActivity.this, MapActivity.class));
                break;
            case R.id.iv_files:
            case R.id.tv_files:
                startActivity(new Intent(HomeActivity.this, UploadFilesActivity.class));
                break;
            case R.id.tv_contact:
            case R.id.iv_contact:
                startActivity(new Intent(HomeActivity.this, ContactUsActivity.class));
                break;
            case R.id.tv_message:
            case R.id.iv_message:
                startActivity(new Intent(HomeActivity.this, MessageActivity.class));
                break;
        }
    }
}
