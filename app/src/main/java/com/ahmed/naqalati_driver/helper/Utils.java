package com.ahmed.naqalati_driver.helper;

import android.app.Activity;
import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.widget.ImageView;

import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeErrorDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeWarningDialog;
import com.ahmed.naqalati_driver.R;
import com.awesomedialog.blennersilva.awesomedialoglibrary.interfaces.Closure;
import com.bumptech.glide.Glide;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by bassiouny on 10/11/17.
 */

public class Utils {
    static AwesomeWarningDialog awesomeWarningDialog;
    static AwesomeErrorDialog awesomeErrorDialog;

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

    public static String convertPhoneToEmail(String phone) {
        return phone + "@gmail.com";
    }

    public static void showWarningDialog(Context context, String msg) {
        if (awesomeWarningDialog == null)
            awesomeWarningDialog = new AwesomeWarningDialog(context);

        awesomeWarningDialog.setMessage(msg)
                .setColoredCircle(R.color.dialogNoticeBackgroundColor)
                .setDialogIconAndColor(R.drawable.ic_notice, R.color.white)
                .setCancelable(true)
                .show();
    }

    public static void showErrorDialog(Context context, String msg) {
        if (awesomeErrorDialog == null)
            awesomeErrorDialog = new AwesomeErrorDialog(context);

        awesomeErrorDialog.setMessage(msg)
                .setColoredCircle(R.color.dialogErrorBackgroundColor)
                .setDialogIconAndColor(R.drawable.ic_dialog_error, R.color.white)
                .setCancelable(true)
                .show();
    }
    public static void showImage(Context context, String url, ImageView imageView){
        Glide.with(context).load(url)
                .into(imageView);
    }
    public static void ContactSuppot(final Activity activity) {
        AwesomeErrorDialog awesomeErrorDialog = new AwesomeErrorDialog(activity);
        awesomeErrorDialog.setMessage(activity.getString(R.string.contact_suppoty))
                .setColoredCircle(R.color.dialogErrorBackgroundColor)
                .setDialogIconAndColor(R.drawable.ic_dialog_error, R.color.white)
                .setCancelable(true)
                .setButtonText(activity.getString(R.string.yes))
                .setButtonBackgroundColor(R.color.red_logo)
                .setButtonTextColor(R.color.white)
                .setErrorButtonClick(new Closure() {
                    @Override
                    public void exec() {
                        activity.finish();
                    }
                })
                .show();
    }

}
