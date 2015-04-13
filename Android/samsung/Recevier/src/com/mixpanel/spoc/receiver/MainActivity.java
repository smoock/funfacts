package com.mixpanel.spoc.receiver;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import com.mixpanel.android.mpmetrics.MixpanelAPI;

import org.json.JSONException;
import org.json.JSONObject;

// pong://start?referrer=drew

public class MainActivity extends Activity {

    public static final String MIXPANEL_API_TOKEN = "bb23c33cf9ba0560f6fc4f43047e5bab";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MixpanelAPI mMixpanel = MixpanelAPI.getInstance(this, MIXPANEL_API_TOKEN);

        mMixpanel.identify("tester@drew.mixpanel.com");
        mMixpanel.getPeople().identify("tester@drew.mixpanel.com");

        mMixpanel.getPeople().initPushHandling("1063380058804");
        mMixpanel.getPeople().set("$first_name", "drew");

        Intent intent = getIntent();
        String action = intent.getAction();
        Uri data = intent.getData();
        Log.i("received deep link to this activity", action);

        if (data != null) {
            new AlertDialog.Builder(this)
                    .setTitle("Received deep link to this activity")
                    .setMessage("Found referrer parameter: " + data.getQueryParameter("referrer"))
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }

        mMixpanel.track("app open", null);

        setContentView(R.layout.activity_main);

        mMixpanel.getPeople().showNotificationIfAvailable(this);

        mMixpanel.flush();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    private MixpanelAPI mMixpanel;
    private static final String LOGTAG = "Mixpanel Example Application";
}
