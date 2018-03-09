package com.bassiouny.naqalati_driver.firebase;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.bassiouny.naqalati_driver.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by bassiouny on 14/12/17.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        showNotificationAboutNewRequest(remoteMessage.getNotification().getBody());
    }
    public void showNotificationAboutNewRequest(String body) {
        NotificationCompat.Builder n = new NotificationCompat.Builder(this, "default");
        n.setContentTitle(getString(R.string.app_name));
        n.setContentText(body);
        n.setDefaults(Notification.DEFAULT_SOUND);
        n.setAutoCancel(true);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            n.setSmallIcon(R.drawable.logo);
        } else {
            n.setSmallIcon(R.drawable.logo);
        }


        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0, n.build());
    }
}
