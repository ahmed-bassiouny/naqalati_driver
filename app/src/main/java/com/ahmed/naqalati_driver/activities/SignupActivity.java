package com.ahmed.naqalati_driver.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ahmed.naqalati_driver.model.CarType;
import com.ahmed.naqalati_driver.model.Driver;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mvc.imagepicker.ImagePicker;
import com.ahmed.naqalati_driver.R;
import com.ahmed.naqalati_driver.helper.Utils;
import com.ahmed.naqalati_driver.model.FirebaseRoot;
import com.ahmed.naqalati_driver.model.User;

import de.hdodenhof.circleimageview.CircleImageView;

public class SignupActivity extends AppCompatActivity {

    private CircleImageView profileImage;
    private TextView tvChooseImage;
    private ProgressBar progress;
    private Button btnRegister;
    private EditText etPhone, etPassword, etConfirmPassword, etName ,etCarNumber;
    private Spinner spCarType;
    private final int requestLocationPermission =123;

    private Uri photoUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        findViewById();
        onClick();
        requestLocationPermission();
    }

    private void requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, requestLocationPermission);
        }

    private void onClick() {
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(photoUri==null){
                    Utils.showWarningDialog(SignupActivity.this,getString(R.string.invalid_image));
                } else if (etPhone.getText().toString().length() != 11) {
                    etPhone.setError(getString(R.string.enter_phone));
                } else if (etPassword.getText().toString().length() < 6) {
                    etPassword.setError(getString(R.string.invalid_password));
                } else if (!etPassword.getText().toString().equals(etConfirmPassword.getText().toString())) {
                    etConfirmPassword.setError(getString(R.string.invalid_confirm_password));
                } else if (etName.getText().toString().isEmpty()) {
                    etName.setError(getString(R.string.invalid_user_name));
                }  else if (etCarNumber.getText().toString().length()<5) {
                    etCarNumber.setError(getString(R.string.invalid_car_number));
                }else {
                    if (Utils.isNetworkConnected(SignupActivity.this)) {
                        startSignup();
                        signUp();
                    } else {
                        Utils.showWarningDialog(SignupActivity.this, getString(R.string.check_internet));
                    }
                }
            }
        });
        tvChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.pickImage(SignupActivity.this, getString(R.string.choose_image));
            }
        });
    }

    private void uploadImage() {
        String userId =FirebaseAuth.getInstance().getCurrentUser().getUid();
        StorageReference storageRef = FirebaseStorage.getInstance().getReference(userId);
        storageRef.putFile(photoUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        createUserObject(downloadUrl.toString());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        stopSignup();
                        Utils.showErrorDialog(SignupActivity.this, exception.getLocalizedMessage());
                    }
                });
    }

    private void findViewById() {
        profileImage = findViewById(R.id.profile_image);
        tvChooseImage = findViewById(R.id.tv_choose_image);
        progress = findViewById(R.id.progress);
        btnRegister = findViewById(R.id.btn_register);
        etPhone = findViewById(R.id.et_phone);
        etPassword = findViewById(R.id.et_password);
        etConfirmPassword = findViewById(R.id.et_confirm_password);
        etName = findViewById(R.id.et_user_name);
        etCarNumber = findViewById(R.id.et_car_number);
        spCarType = findViewById(R.id.sp_car_type);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bitmap photoBitmap = ImagePicker.getImageFromResult(this, requestCode, resultCode, data);
        if(photoBitmap!=null&&data!=null) {
            profileImage.setImageBitmap(photoBitmap);
            photoUri=data.getData();
        }
    }

    private void startSignup() {
        progress.setVisibility(View.VISIBLE);
        btnRegister.setEnabled(false);
    }

    private void stopSignup() {
        progress.setVisibility(View.INVISIBLE);
        btnRegister.setEnabled(true);
        if(FirebaseAuth.getInstance().getCurrentUser()!=null)
            FirebaseAuth.getInstance().signOut();
    }


    private void createUserObject(String url) {
        Driver driver = new Driver();
        driver.setUserName(etName.getText().toString());
        driver.setUserPhone(etPhone.getText().toString());
        driver.setUserPasswrod(etPassword.getText().toString());
        driver.setUserAvatar(url);
        driver.setLat(0.0);
        driver.setLng(0.0);
        driver.setCarNumber(etCarNumber.getText().toString());
        driver.setCarType(getCarTypeFromSpinner(spCarType.getSelectedItemPosition()));

        FirebaseDatabase.getInstance().getReference(FirebaseRoot.DB_DRIVER)
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .setValue(driver).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    stopSignup();
                    Toast.makeText(SignupActivity.this, R.string.register_success, Toast.LENGTH_SHORT).show();
                    finish();
                }else {
                    stopSignup();
                    Utils.showWarningDialog(SignupActivity.this,getString(R.string.error));
                }
            }
        });

    }

    private void signUp() {
        // First create email and password
        // Second Upload Image
        // Third create user object and save in databse
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                Utils.convertPhoneToEmail(etPhone.getText().toString()), etPassword.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(FirebaseRoot.DB_DRIVER).build();
                            FirebaseAuth.getInstance().getCurrentUser().updateProfile(profileUpdates);
                            uploadImage();
                        } else {
                            stopSignup();
                            Utils.showErrorDialog(SignupActivity.this, task.getException().getLocalizedMessage());
                        }
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode== requestLocationPermission &&grantResults[0]==PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, requestLocationPermission);
        }
    }
    private CarType getCarTypeFromSpinner(int position){
        CarType result;
        switch (position){
            case 0:result = CarType.FULL; break;
            case 1:result = CarType.MEDIUM; break;
            default: result = CarType.FULL;
        }
        return result;
    }
}
