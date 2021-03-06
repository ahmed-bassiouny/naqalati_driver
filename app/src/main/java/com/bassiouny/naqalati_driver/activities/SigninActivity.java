package com.bassiouny.naqalati_driver.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bassiouny.naqalati_driver.helper.SharedPref;
import com.bassiouny.naqalati_driver.model.Driver;
import com.bassiouny.naqalati_driver.model.FirebaseRoot;
import com.crashlytics.android.Crashlytics;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.bassiouny.naqalati_driver.R;
import com.bassiouny.naqalati_driver.helper.Utils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.fabric.sdk.android.Fabric;

public class SigninActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private TextInputEditText etPhone;
    private TextInputEditText etPassword;
    private ProgressBar progress;
    private TextView tvNewUser;
    private Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_signin);
        findViewById();
        initObjects();
        onClick();

    }

    private void onClick() {
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etPhone.getText().toString().trim().length() != 11) {
                    etPhone.setError(getString(R.string.enter_phone));
                } else if (etPassword.getText().toString().trim().length() < 6) {
                    etPassword.setError(getString(R.string.invalid_password));
                } else {
                    startLogin();
                    signIn(Utils.convertPhoneToEmail(etPhone.getText().toString()), etPassword.getText().toString());
                }
            }
        });
        tvNewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SigninActivity.this, SignupActivity.class));
            }
        });
    }

    private void findViewById() {
        etPhone = findViewById(R.id.et_phone);
        etPassword = findViewById(R.id.et_password);
        progress = findViewById(R.id.progress);
        tvNewUser = findViewById(R.id.tv_new_user);
        btnSubmit = findViewById(R.id.btn_submit);
    }

    private void initObjects() {
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(final FirebaseUser currentUser) {
        if (currentUser != null && SharedPref.isUserSetFullData(this)) {
            Utils.showDialog(this);
            FirebaseDatabase.getInstance().getReference().child(FirebaseRoot.DB_DRIVER)
                    .child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot==null){
                        Utils.dismissDialog();
                        Utils.ContactSuppot(SigninActivity.this);
                        return;
                    }
                    Driver driver = dataSnapshot.getValue(Driver.class);
                    if(driver==null){
                        Utils.dismissDialog();
                        Utils.ContactSuppot(SigninActivity.this);
                        return;
                    }
                    Utils.dismissDialog();
                    Log.e( "onDataChange: ",currentUser.getUid() );
                    if(driver.isAdminAccept()){
                        startActivity(new Intent(SigninActivity.this, HomeActivity.class));
                        finish();
                    }else {
                        startActivity(new Intent(SigninActivity.this, AcceptAdminActivity.class));
                        finish();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Utils.dismissDialog();
                    Toast.makeText(SigninActivity.this, "حدث خطا ما", Toast.LENGTH_SHORT).show();
                }
            });
        } else if (currentUser != null) {
            startActivity(new Intent(SigninActivity.this, NextRegisterActivity.class));
            finish();
        }
    }

    private void signIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user.getDisplayName() != null && user.getDisplayName().equals(FirebaseRoot.DB_DRIVER)) {
                                SharedPref.setFullData(SigninActivity.this, true);
                                updateUI(user);
                            } else {
                                stopLogin();
                                FirebaseAuth.getInstance().signOut();
                                Utils.showErrorDialog(SigninActivity.this, getString(R.string.user_not_found));
                            }
                        } else {
                            if (Utils.isNetworkConnected(SigninActivity.this)) {
                                stopLogin();
                                Utils.showErrorDialog(SigninActivity.this, getString(R.string.phone_password_invalid));
                            } else {
                                stopLogin();
                                Utils.showWarningDialog(SigninActivity.this, getString(R.string.check_internet));
                            }
                        }
                    }
                });
    }

    private void startLogin() {
        btnSubmit.setEnabled(false);
        progress.setVisibility(View.VISIBLE);
    }

    private void stopLogin() {
        btnSubmit.setEnabled(true);
        progress.setVisibility(View.INVISIBLE);
    }

}
