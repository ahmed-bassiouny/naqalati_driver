package com.ahmed.naqalati_driver.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.ahmed.naqalati_driver.R;
import com.ahmed.naqalati_driver.adapter.RequestAdapter;
import com.ahmed.naqalati_driver.helper.Constant;
import com.ahmed.naqalati_driver.helper.Utils;
import com.ahmed.naqalati_driver.model.RequestListener;
import com.ahmed.naqalati_driver.model.FirebaseRoot;
import com.ahmed.naqalati_driver.model.RequestInfo;
import com.ahmed.naqalati_driver.model.RequestStatus;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ShowRequestsActivity extends AppCompatActivity implements RequestListener {

    private RecyclerView recyclerView;
    private String driverId;
    List<RequestInfo> requestInfoList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_requests);
        recyclerView=findViewById(R.id.recycler);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        requestInfoList=new ArrayList<>();
        if(FirebaseAuth.getInstance().getCurrentUser()==null){
            Utils.ContactSuppot(this);
            finish();
        }
        driverId= FirebaseAuth.getInstance().getCurrentUser().getUid();
        loadData();
    }

    private void loadData() {
        FirebaseDatabase.getInstance().getReference(FirebaseRoot.DB_DRIVER)
                .child(driverId).child(FirebaseRoot.DB_PENDING_REQUEST)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                            if(snapshot==null)
                                return;
                            RequestInfo requestInfo = snapshot.getValue(RequestInfo.class);
                            requestInfoList.add(requestInfo);
                        }
                        RequestAdapter requestAdapter = new RequestAdapter(requestInfoList,ShowRequestsActivity.this);
                        recyclerView.setAdapter(requestAdapter);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }
    @Override
    public void showMore(RequestInfo requestInfo) {
        Intent intent = new Intent(ShowRequestsActivity.this,ShowRequestDetailsActivity.class);
        intent.putExtra(Constant.SHOW_REQUEST_INFO_DETAILS,requestInfo);
        startActivity(intent);
    }

    @Override
    public void accept(String userId) {
        FirebaseDatabase.getInstance().getReference(FirebaseRoot.DB_USER)
                .child(userId).child(FirebaseRoot.DB_REQUEST_STATUS)
                .setValue(RequestStatus.ACCEPT);
        finish();
    }

    @Override
    public void refuse(String userId) {
        FirebaseDatabase.getInstance().getReference(FirebaseRoot.DB_USER)
                .child(userId).child(FirebaseRoot.DB_REQUEST_STATUS)
                .setValue(RequestStatus.REFUSE);
        finish();
    }
}
