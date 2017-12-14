package com.bassiouny.naqalati_driver.firebase;

import android.util.Log;

import com.bassiouny.naqalati_driver.helper.SharedPref;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by bassiouny on 14/12/17.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        SharedPref.setToken(this,refreshedToken);
    }
}
