package com.bassiouny.naqalati_driver.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.bassiouny.naqalati_driver.R;
import com.bassiouny.naqalati_driver.helper.SharedPref;
import com.bassiouny.naqalati_driver.helper.Utils;
import com.bassiouny.naqalati_driver.model.Driver;
import com.bassiouny.naqalati_driver.model.FirebaseRoot;
import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeInfoDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.interfaces.Closure;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mvc.imagepicker.ImagePicker;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener {
    private CircleImageView profileImage;
    private TextView tvChooseImage;
    private Spinner spCarType;
    private ProgressBar progress;
    private Uri photoUri;
    String driverId;
    Driver user;
    String[] carType;
    private Switch isOnline;


    private EditText etUserName;
    private EditText etCarNumber;
    private EditText etUserEmail;
    private EditText etUserEdaraMeror;
    private EditText etWe7daMeror;
    private EditText etRo5esa;
    private EditText etRo5esaNumber;
    private EditText etUserId;
    private EditText etUserAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        driverId=FirebaseAuth.getInstance().getCurrentUser().getUid();
        profileImage = findViewById(R.id.profile_image);
        tvChooseImage = findViewById(R.id.tv_choose_image);
        spCarType = findViewById(R.id.sp_car_type);
        isOnline = findViewById(R.id.is_online);

        etUserName = findViewById(R.id.et_user_name);
        etCarNumber = findViewById(R.id.et_car_number);
        etUserEmail = findViewById(R.id.et_user_email);
        etUserEdaraMeror = findViewById(R.id.et_user_edara_meror);
        etWe7daMeror = findViewById(R.id.et_we7da_meror);
        etRo5esa = findViewById(R.id.et_ro5esa);
        etRo5esaNumber = findViewById(R.id.et_ro5esa_number);
        etUserId = findViewById(R.id.et_user_id);
        etUserAddress = findViewById(R.id.et_user_address);
        spCarType = findViewById(R.id.sp_car_type);


        ArrayAdapter mAdapter = ArrayAdapter.createFromResource(this, R.array.car_type_value,
                android.R.layout.simple_spinner_dropdown_item);
        spCarType.setAdapter(mAdapter);
        findViewById(R.id.btn_register).setOnClickListener(this);
        progress = findViewById(R.id.progress);
        findViewById(R.id.btn_singout).setOnClickListener(this);
        loadData();
        tvChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.pickImage(EditProfileActivity.this, getString(R.string.choose_image));

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_register:
                if(etUserName.getText().toString().trim().isEmpty())
                    return;
                else if(etCarNumber.getText().toString().trim().isEmpty())
                    return;
                else if(etUserId.getText().toString().trim().isEmpty())
                    return;
                else {
                    progress.setVisibility(View.VISIBLE);
                    findViewById(R.id.btn_singout).setVisibility(View.INVISIBLE);
                    findViewById(R.id.btn_register).setVisibility(View.INVISIBLE);
                    if(photoUri==null){
                        // update name
                        updateName();
                    }else {
                        // update image and name
                        uploadImage();
                    }
                }
                break;
            case R.id.btn_singout:
                new AwesomeInfoDialog(EditProfileActivity.this)
                        .setTitle(R.string.app_name)
                        .setMessage(R.string.you_want_signout)
                        .setColoredCircle(R.color.dialogInfoBackgroundColor)
                        .setDialogIconAndColor(R.drawable.ic_dialog_info, R.color.white)
                        .setCancelable(true)
                        .setPositiveButtonText(getString(R.string.yes))
                        .setPositiveButtonbackgroundColor(R.color.red_logo)
                        .setPositiveButtonTextColor(R.color.white)
                        .setNegativeButtonText(getString(R.string.no))
                        .setNegativeButtonbackgroundColor(R.color.dialogInfoBackgroundColor)
                        .setNegativeButtonTextColor(R.color.white)
                        .setPositiveButtonClick(new Closure() {
                            @Override
                            public void exec() {
                                FirebaseAuth.getInstance().signOut();
                                SharedPref.setFullData(EditProfileActivity.this,false);
                                startActivity(new Intent(EditProfileActivity.this, SigninActivity.class));
                                finish();
                            }
                        })
                        .setNegativeButtonClick(new Closure() {
                            @Override
                            public void exec() {
                            }
                        })
                        .show();
                break;
        }
    }
    private void loadData() {
        carType=getResources().getStringArray(R.array.car_type_value);
        final int carTypeSize=carType.length;
        FirebaseDatabase.getInstance().getReference(FirebaseRoot.DB_DRIVER)
                .child(driverId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progress.setVisibility(View.GONE);
                if(dataSnapshot==null)
                    finish();
                user = dataSnapshot.getValue(Driver.class);
                etUserName.setText(user.getUserName());
                etCarNumber.setText(user.getCarNumber());
                etUserEmail.setText(user.getEmail());
                etUserEdaraMeror.setText(user.getEdaraMeror());
                etWe7daMeror.setText(user.getWe7detMeror());
                etRo5esa.setText(user.getRo5esa());
                etRo5esaNumber.setText(user.getRo5esaNumber());
                etUserId.setText(user.getNumberID());
                etUserAddress.setText(user.getAddress());
                isOnline.setChecked(user.isOnline());
                for(int i=0;i<carTypeSize; i++){
                    if(carType[i].equals(user.getCarType())) {
                        spCarType.setSelection(i);
                        break;
                    }
                }
                if(!user.getUserAvatar().isEmpty())
                    Utils.showImage(EditProfileActivity.this,user.getUserAvatar(),profileImage);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void uploadImage() {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference(driverId);
        storageRef.putFile(photoUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        updateImage(downloadUrl.toString());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Utils.showErrorDialog(EditProfileActivity.this, exception.getLocalizedMessage());
                    }
                });
    }
    private void updateImage(String url){
        FirebaseDatabase.getInstance().getReference(FirebaseRoot.DB_DRIVER)
                .child(driverId).child("userAvatar").setValue(url).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isComplete())
                    updateName();
            }
        });
    }
    private void updateName(){
        user.setUserName(etUserName.getText().toString());
        user.setCarNumber(etCarNumber.getText().toString());
        user.setCarType(spCarType.getSelectedItem().toString());

        user.setEmail(etUserEmail.getText().toString());
        user.setEdaraMeror(etUserEdaraMeror.getText().toString());
        user.setWe7detMeror(etWe7daMeror.getText().toString());
        user.setRo5esa(etRo5esa.getText().toString());
        user.setRo5esaNumber(etRo5esaNumber.getText().toString());
        user.setNumberID(etUserId.getText().toString());
        user.setAddress(etUserAddress.getText().toString());

        user.setOnline(isOnline.isChecked());
        FirebaseDatabase.getInstance().getReference(FirebaseRoot.DB_DRIVER)
                .child(driverId).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isComplete())
                    onBackPressed();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bitmap photoBitmap = ImagePicker.getImageFromResult(this, requestCode, resultCode, data);
        if (photoBitmap != null && data != null) {
            profileImage.setImageBitmap(photoBitmap);
            photoUri = data.getData();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(EditProfileActivity.this,MapActivity.class));
        finish();
    }
}