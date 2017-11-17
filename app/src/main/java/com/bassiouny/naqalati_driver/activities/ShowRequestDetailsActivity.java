package com.bassiouny.naqalati_driver.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.bassiouny.naqalati_driver.R;
import com.bassiouny.naqalati_driver.helper.Constant;
import com.bassiouny.naqalati_driver.helper.Utils;
import com.bassiouny.naqalati_driver.model.RequestInfo;

import de.hdodenhof.circleimageview.CircleImageView;

public class ShowRequestDetailsActivity extends AppCompatActivity {

    private CircleImageView profileImage;
    private TextView tvUserName;
    private TextView tvUserPhone;
    private TextView tvStartPoint;
    private TextView tvEndPoint;
    private RequestInfo requestInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_request_details);
        findViewById();
        initObject();
        setData();
    }

    private void setData() {
        if(!requestInfo.getUserImage().isEmpty())
            Utils.showImage(this,requestInfo.getUserImage(),profileImage);
        tvUserName.setText(requestInfo.getUserName());
        tvUserPhone.setText(requestInfo.getUserPhone());
        tvStartPoint.setText(requestInfo.getStartPoint().getLocationString());
        tvEndPoint.setText(requestInfo.getEndPoint().getLocationString());
    }

    private void initObject() {
        requestInfo = (RequestInfo) getIntent().getSerializableExtra(Constant.SHOW_REQUEST_INFO_DETAILS);
        if(requestInfo==null){
            Utils.ContactSuppot(this);
            finish();
        }
    }

    private void findViewById() {
        profileImage = findViewById(R.id.profile_image);
        tvUserName = findViewById(R.id.tv_user_name);
        tvUserPhone = findViewById(R.id.tv_user_phone);
        tvStartPoint = findViewById(R.id.tv_start_point);
        tvEndPoint = findViewById(R.id.tv_end_point);
    }
}
