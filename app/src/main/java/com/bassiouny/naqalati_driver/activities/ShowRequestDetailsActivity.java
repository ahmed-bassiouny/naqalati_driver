package com.bassiouny.naqalati_driver.activities;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.database.ChildEventListener;
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
    private LinearLayout llHint;
    private TextView tvProductType,tvProductSize;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_request_details);
        findViewById();
        initObject();
        setData();
        onClick();
        addListenerForRequest();
    }

    private void onClick() {
        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etPrice.getText().toString().trim().isEmpty() ||Integer.parseInt(etPrice.getText().toString())<=0){
                    Utils.showWarningDialog(ShowRequestDetailsActivity.this,"برجاء تحديد سعر النقلة");
                    return;
                }else {
                    FirebaseDatabase.getInstance().getReference(FirebaseRoot.DB_DRIVER)
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child(FirebaseRoot.DB_PENDING_REQUEST)
                            .child(requestInfoKey).child(FirebaseRoot.DB_PRICE)
                            .setValue(etPrice.getText().toString());
                    llHint.setVisibility(View.VISIBLE);
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
        tvProductType.append(requestInfo.getProductType());
        tvProductSize.append(requestInfo.getProductSize());
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
        tvProductType = findViewById(R.id.tv_product_type);
        tvProductSize = findViewById(R.id.tv_product_size);
    }

    private void cancelRequest(){
        FirebaseDatabase.getInstance().getReference(FirebaseRoot.DB_DRIVER)
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(FirebaseRoot.DB_PENDING_REQUEST)
                .child(requestInfoKey).removeValue();

        requestInfo.setDriverId(FirebaseAuth.getInstance().getCurrentUser().getUid());
        requestInfo.setRequestStatus(RequestStatus.REFUSE_FROM_DRIVER);
        // generate key for request
        String key =FirebaseDatabase.getInstance().getReference(FirebaseRoot.DB_REQUESTS).push().getKey();
        // update currentRequest in driver
        FirebaseDatabase.getInstance().getReference(FirebaseRoot.DB_REQUESTS).child(key)
                .setValue(requestInfo);
        finish();
    }

    @Override
    public void onBackPressed() {
    }
    private void addListenerForRequest(){
        FirebaseDatabase.getInstance().getReference(FirebaseRoot.DB_DRIVER)
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(FirebaseRoot.DB_PENDING_REQUEST)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                        finish();
                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }
}
