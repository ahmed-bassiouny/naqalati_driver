package com.bassiouny.naqalati_driver.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bassiouny.naqalati_driver.R;
import com.bassiouny.naqalati_driver.helper.Utils;
import com.bassiouny.naqalati_driver.model.File;
import com.bassiouny.naqalati_driver.model.FirebaseRoot;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mvc.imagepicker.ImagePicker;

public class UploadFilesActivity extends AppCompatActivity {

    private ImageView ivImage;
    private Uri photoUri;
    private Button btnSelect, btnUpload;
    private TextView status;
    private ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_files);
        ivImage = findViewById(R.id.iv_image);
        btnSelect = findViewById(R.id.btn_select);
        btnUpload = findViewById(R.id.btn_upload);
        status = findViewById(R.id.btn_status);
        progress = findViewById(R.id.progress);
        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.pickImage(UploadFilesActivity.this, getString(R.string.choose_image));
            }
        });
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (photoUri == null) {
                    Toast.makeText(UploadFilesActivity.this, "برجاء اختيار صورة", Toast.LENGTH_SHORT).show();
                } else {
                    btnSelect.setVisibility(View.INVISIBLE);
                    btnUpload.setVisibility(View.INVISIBLE);
                    status.setVisibility(View.VISIBLE);
                    progress.setVisibility(View.VISIBLE);
                    uploadImage();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bitmap photoBitmap = ImagePicker.getImageFromResult(this, requestCode, resultCode, data);
        if (photoBitmap != null && data != null) {
            ivImage.setImageBitmap(photoBitmap);
            photoUri = data.getData();
        }
    }

    private void uploadImage() {
        final File file = new File();
        final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        file.setUserUploadedId(userId);
        file.setUserType(FirebaseRoot.DB_DRIVER);
        file.setDate(Utils.getCurrentDate());
        final DatabaseReference dbUrl = FirebaseDatabase.getInstance().getReference(FirebaseRoot.DB_FILES);
        StorageReference storageRef = FirebaseStorage.getInstance().getReference(userId);
        storageRef.putFile(photoUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        file.setPath(downloadUrl.toString());
                        String key = dbUrl.push().getKey();
                        dbUrl.child(key).setValue(file).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(UploadFilesActivity.this, "تم تحميل الصورة بنجاح", Toast.LENGTH_LONG).show();
                                    finish();
                                } else {
                                    Utils.showErrorDialog(UploadFilesActivity.this, "ﻻ يمكن تحميل الصورة ");
                                }
                            }
                        });

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Utils.showErrorDialog(UploadFilesActivity.this, "ﻻ يمكن تحميل الصورة ");
                        btnSelect.setVisibility(View.VISIBLE);
                        btnUpload.setVisibility(View.VISIBLE);
                        status.setVisibility(View.INVISIBLE);
                        progress.setVisibility(View.INVISIBLE);
                    }
                });
    }

}
