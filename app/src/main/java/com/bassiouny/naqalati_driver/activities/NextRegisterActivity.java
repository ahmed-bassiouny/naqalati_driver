package com.bassiouny.naqalati_driver.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bassiouny.naqalati_driver.R;
import com.bassiouny.naqalati_driver.helper.SharedPref;
import com.bassiouny.naqalati_driver.helper.Utils;
import com.bassiouny.naqalati_driver.model.Driver;
import com.bassiouny.naqalati_driver.model.FirebaseRoot;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class NextRegisterActivity extends AppCompatActivity implements View.OnClickListener{

    private String error="برجاء ادخال البيانات";
    private Spinner spCarType;
    private ProgressBar progress;
    private EditText etOwnerName,etOwnerPhone,city,etWe7damerorOwner,etCarNumber;
    private EditText etShaceh,etMatorNumber,etSize,etCarModel;
    private CheckBox accept;
    Driver driver;
    String driverId="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next_register);
        spCarType = findViewById(R.id.sp_car_type);
        ArrayAdapter mAdapter = ArrayAdapter.createFromResource(this, R.array.car_type_value,
                android.R.layout.simple_spinner_dropdown_item);
        spCarType.setAdapter(mAdapter);
        progress = findViewById(R.id.progress);
        etOwnerName=findViewById(R.id.et_owner_name);
        etOwnerPhone=findViewById(R.id.et_owner_phone);
        city=findViewById(R.id.et_city);
        etWe7damerorOwner=findViewById(R.id.et_we7da_meror_owner);
        etCarNumber=findViewById(R.id.et_car_number);
        etShaceh=findViewById(R.id.et_shaceh);
        etMatorNumber=findViewById(R.id.et_mator_number);
        etSize=findViewById(R.id.et_size);
        etCarModel=findViewById(R.id.et_car_model);
        accept=findViewById(R.id.accept);
        findViewById(R.id.btn_register).setOnClickListener(this);
        findViewById(R.id.show_permission).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NextRegisterActivity.this,PermissionActivity.class));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        getDriverId();
        loadData();
    }

    @Override
    public void onClick(View v) {
        if(!Utils.isNetworkConnected(this)){
            Utils.showWarningDialog(NextRegisterActivity.this, getString(R.string.check_internet));
            return;
        }
        else if(etOwnerName.getText().toString().isEmpty()){
            etOwnerName.setError(error);
            return;
        }else if(etOwnerPhone.getText().toString().length()!=11){
            etOwnerPhone.setError(error);
            return;
        }else if(etCarNumber.getText().toString().isEmpty()){
            etCarNumber.setError(error);
            return;
        }else if(etSize.getText().toString().isEmpty()){
            etSize.setError(error);
            return;
        }else if(!accept.isChecked()){
            Toast.makeText(this, "يجرب الموافقة على شروط الاستخدام", Toast.LENGTH_LONG).show();
        }
        else if(driver ==null || driver.getUserPhone()==null ||driver.getUserPhone().isEmpty()) {
            // check if driver contain data or empty
            Utils.showErrorDialog(this,"ﻻ يمكن حفظ البيانات الان");
        }else {
            // update driver
            progress.setVisibility(View.VISIBLE);
            findViewById(R.id.btn_register).setVisibility(View.INVISIBLE);
            setDriver();
            FirebaseDatabase.getInstance().getReference(FirebaseRoot.DB_DRIVER)
                    .child(driverId).setValue(driver).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        SharedPref.setFullData(NextRegisterActivity.this,true);
                        Toast.makeText(NextRegisterActivity.this, R.string.register_success, Toast.LENGTH_SHORT).show();
                        finish();
                    }else {
                        Utils.showErrorDialog(NextRegisterActivity.this,"ﻻ يمكن حفظ البيانات الان");
                        progress.setVisibility(View.INVISIBLE);
                        findViewById(R.id.btn_register).setVisibility(View.VISIBLE);
                    }
                }
            });
        }
    }
    private void setDriver(){
        driver.setCarOwner(etOwnerName.getText().toString());
        driver.setPhoneOwner(etOwnerPhone.getText().toString());
        driver.setCity(city.getText().toString());
        driver.setWe7datMerorOwner(etWe7damerorOwner.getText().toString());
        driver.setCarNumber(etCarNumber.getText().toString());
        driver.setShaceh(etShaceh.getText().toString());
        driver.setMotor(etMatorNumber.getText().toString());
        driver.setSize(etSize.getText().toString());
        driver.setModel(etCarModel.getText().toString());
        driver.setCarType(spCarType.getSelectedItem().toString());
    }
    private void getDriverId(){
        if(FirebaseAuth.getInstance().getCurrentUser()==null){
            Utils.ContactSuppot(this);
            finish();
        }else {
            driverId=FirebaseAuth.getInstance().getCurrentUser().getUid();
        }
    }
    private void loadData(){
        Utils.showDialog(this);
        FirebaseDatabase.getInstance().getReference(FirebaseRoot.DB_DRIVER)
                .child(driverId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot==null)
                    return;
                driver=dataSnapshot.getValue(Driver.class);
                Utils.dismissDialog();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
