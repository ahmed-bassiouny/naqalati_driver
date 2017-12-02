package com.bassiouny.naqalati_driver.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.bassiouny.naqalati_driver.R;
import com.bassiouny.naqalati_driver.adapter.RequestAdapter;
import com.bassiouny.naqalati_driver.helper.Constant;
import com.bassiouny.naqalati_driver.helper.Utils;
import com.bassiouny.naqalati_driver.model.RequestListener;
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

import java.util.ArrayList;
import java.util.List;

public class ShowRequestsActivity extends AppCompatActivity implements RequestListener {

    private RecyclerView recyclerView;
    private String driverId;
    List<RequestInfo> requestInfoList;
    List<String>requestInfoKeyList;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_requests);
        recyclerView=findViewById(R.id.recycler);
        progressBar=findViewById(R.id.progress);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        requestInfoList=new ArrayList<>();
        requestInfoKeyList=new ArrayList<>();
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
                            requestInfoKeyList.add(snapshot.getKey());
                        }
                        RequestAdapter requestAdapter = new RequestAdapter(requestInfoList,requestInfoKeyList,ShowRequestsActivity.this);
                        recyclerView.setAdapter(requestAdapter);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }
    @Override
    public void showMore(RequestInfo requestInfo,String parentKey) {
        Intent intent = new Intent(ShowRequestsActivity.this,ShowRequestDetailsActivity.class);
        intent.putExtra(Constant.SHOW_REQUEST_INFO_DETAILS,requestInfo);
        intent.putExtra(Constant.REQUEST_INFO_KEY,parentKey);
        startActivity(intent);
        finish();
    }

    /*@Override
    public void accept(String userId) {
        recyclerView.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        FirebaseDatabase.getInstance().getReference(FirebaseRoot.DB_USER)
                .child(userId).child(FirebaseRoot.DB_REQUEST_STATUS)
                .setValue(RequestStatus.ACCEPT).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isComplete())
                    finish();
            }
        });
    }

    @Override
    public void refuse(String userId) {
        recyclerView.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        FirebaseDatabase.getInstance().getReference(FirebaseRoot.DB_USER)
                .child(userId).child(FirebaseRoot.DB_REQUEST_STATUS)
                .setValue(RequestStatus.REFUSE).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isComplete())
                    finish();
            }
        });

    }*/
}
