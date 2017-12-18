package com.bassiouny.naqalati_driver.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bassiouny.naqalati_driver.R;
import com.bassiouny.naqalati_driver.model.FirebaseRoot;
import com.bassiouny.naqalati_driver.model.Message;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MessageActivity extends AppCompatActivity {
    private TextView tvTitle;
    private TextView tvBody;
    private ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        tvTitle = findViewById(R.id.tv_title);
        tvBody = findViewById(R.id.tv_body);
        progress = findViewById(R.id.progress);
        FirebaseDatabase.getInstance().getReference(FirebaseRoot.DB_MESSAGE)
                .child(FirebaseRoot.DB_DRIVER)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot != null) {
                            Message message = dataSnapshot.getValue(Message.class);
                            if (message != null) {
                                tvTitle.setText(message.getTitle());
                                tvBody.setText(message.getBody());
                                progress.setVisibility(View.GONE);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

}