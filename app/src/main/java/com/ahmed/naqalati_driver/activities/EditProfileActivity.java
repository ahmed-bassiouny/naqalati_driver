package com.ahmed.naqalati_driver.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.ahmed.naqalati_driver.R;
import com.ahmed.naqalati_driver.helper.SharedPref;
import com.ahmed.naqalati_driver.helper.Utils;
import com.ahmed.naqalati_driver.model.Driver;
import com.ahmed.naqalati_driver.model.FirebaseRoot;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        driverId=FirebaseAuth.getInstance().getCurrentUser().getUid();
        profileImage = findViewById(R.id.profile_image);
        tvChooseImage = findViewById(R.id.tv_choose_image);
        spCarType = findViewById(R.id.sp_car_type);
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

    private EditText getEtUserName(){
        return (EditText) findViewById(R.id.et_user_name);
    }

    private EditText getEtCarNumber(){
        return (EditText) findViewById(R.id.et_car_number);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_register:
                if(getEtUserName().getText().toString().trim().isEmpty())
                    return;
                else if(getEtCarNumber().getText().toString().trim().isEmpty())
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
                user=dataSnapshot.getValue(Driver.class);
                getEtUserName().setText(user.getUserName());
                getEtCarNumber().setText(user.getCarNumber());
                for(int i=0;i<carTypeSize; i++){
                    if(carType[i].equals(user.getCarType()))
                        spCarType.setSelection(i);
                    Log.e("onDataChange", i+"" );
                    break;
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
        user.setUserName(getEtUserName().getText().toString());
        user.setCarNumber(getEtCarNumber().getText().toString());
        user.setCarType(spCarType.getSelectedItem().toString());
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
        startActivity(new Intent(EditProfileActivity.this,HomeActivity.class));
        finish();
    }
}