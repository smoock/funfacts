package com.mixpanel.spoc.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;


public class CustomGCMReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();
        if ("com.google.android.c2dm.intent.RECEIVE".equals(action)) {
            // mp_message will always exist and contain the message you entered
            // for the push notification body in the builder.
            Bundle extras = intent.getExtras();
            final String message = extras.getString("mp_message");
            Uri data = Uri.parse(extras.getString("url"));
            Log.i(extras.getString("url"), action);
            Intent viewIntent = new Intent(Intent.ACTION_VIEW, data);
            viewIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(viewIntent);
            if (message == null) {
                return;
            }
        }
    }
}