package com.bassiouny.naqalati_driver.activities;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bassiouny.naqalati_driver.R;
import com.bassiouny.naqalati_driver.helper.Constant;
import com.bassiouny.naqalati_driver.helper.Utils;
import com.bassiouny.naqalati_driver.model.FirebaseRoot;
import com.bassiouny.naqalati_driver.model.RequestInfo;
import com.bassiouny.naqalati_driver.model.RequestStatus;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class ShowRequestDetailsActivity extends AppCompatActivity {

    private CircleImageView profileImage;
    private TextView tvUserName;
    private TextView tvUserPhone;
    private TextView tvStartPoint;
    private TextView tvEndPoint;
    private RequestInfo requestInfo;
    private String requestInfoKey;
    private EditText etPrice;
    private Button btnAccept,btnRefuse;
    private ValueEventListener requestListener;
    private LinearLayout llHint;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_request_details);
        findViewById();
        initObject();
        setData();
        onClick();
    }

    private void onClick() {
        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etPrice.getText().toString().trim().isEmpty()){
                    Utils.showWarningDialog(ShowRequestDetailsActivity.this,"برجاء تحديد سعر النقلة");
                    return;
                }else {
                    FirebaseDatabase.getInstance().getReference(FirebaseRoot.DB_DRIVER)
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child(FirebaseRoot.DB_PENDING_REQUEST)
                            .child(requestInfoKey).child(FirebaseRoot.DB_PRICE)
                            .setValue(etPrice.getText().toString());
                    llHint.setVisibility(View.VISIBLE);
                    //addListenerOnRequest();
                }

            }
        });
        btnRefuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelRequest();
            }
        });
    }

    private void setData() {
        if(!requestInfo.getUserImage().isEmpty())
            Utils.showImage(this,requestInfo.getUserImage(),profileImage);
        tvUserName.setText(requestInfo.getUserName());
        tvUserPhone.setText(requestInfo.getUserPhone());
        tvStartPoint.setText(requestInfo.getStartPoint().getLocationString());
        tvEndPoint.setText(requestInfo.getEndPoint().getLocationString());
        etPrice.setText(requestInfo.getPrice().toString());
    }

    private void initObject() {
        requestInfo = (RequestInfo) getIntent().getSerializableExtra(Constant.SHOW_REQUEST_INFO_DETAILS);
        requestInfoKey = getIntent().getStringExtra(Constant.REQUEST_INFO_KEY);
        if(requestInfo==null ||requestInfoKey==null){
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
        etPrice = findViewById(R.id.et_price);
        btnAccept = findViewById(R.id.btn_accept);
        btnRefuse = findViewById(R.id.btn_refuse);
        llHint = findViewById(R.id.ll_hint);
    }
    private void addListenerOnRequest(){
        FirebaseDatabase.getInstance().getReference(FirebaseRoot.DB_REQUESTS)
                .child(requestInfoKey).addValueEventListener(getListenerOnRequest());
    }
    private void removeListenerOnRequest(){
        FirebaseDatabase.getInstance().getReference(FirebaseRoot.DB_REQUESTS)
                .child(requestInfoKey).removeEventListener(requestListener);
    }
    private ValueEventListener getListenerOnRequest(){
        requestListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot==null)
                    return;
                requestInfo = dataSnapshot.getValue(RequestInfo.class);
                if(requestInfo.getRequestStatus()==RequestStatus.ACCEPT){
                    Toast.makeText(ShowRequestDetailsActivity.this, "تم الموافقة على السعر من قبل العميل", Toast.LENGTH_SHORT).show();
                    finish();
                }else if(requestInfo.getRequestStatus()==RequestStatus.REFUSE){
                    Toast.makeText(ShowRequestDetailsActivity.this, "تم رفض السعر", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        return requestListener;
    }

    @Override
    protected void onStop() {
        super.onStop();/*
        removeListenerOnRequest();
        FirebaseDatabase.getInstance().getReference(FirebaseRoot.DB_USER)
                .child(requestInfo.getUserId()).child(FirebaseRoot.DB_REQUEST_STATUS)
                .setValue(RequestStatus.REFUSE);*/
        cancelRequest();
    }
    private void cancelRequest(){

        FirebaseDatabase.getInstance().getReference(FirebaseRoot.DB_DRIVER)
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(FirebaseRoot.DB_PENDING_REQUEST)
                .child(requestInfoKey).removeValue();
        Toast.makeText(ShowRequestDetailsActivity.this, "تم الغاء الطلب", Toast.LENGTH_SHORT).show();
        finish();
    }
}
