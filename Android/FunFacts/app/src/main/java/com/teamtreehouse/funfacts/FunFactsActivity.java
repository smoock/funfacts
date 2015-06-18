package com.teamtreehouse.funfacts;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mixpanel.android.mpmetrics.MixpanelAPI;

import org.json.JSONException;
import org.json.JSONObject;


public class FunFactsActivity extends Activity {

    public static final String TAG = FunFactsActivity.class.getSimpleName();
    Context context = this;

    // create objects for factbook and colorwheel
    private FactBook mFactBook = new FactBook();
    private ColorWheel mColorWheel = new ColorWheel();

    // declare Mixpanel variables
    String projectToken = "sm_android";
    MixpanelAPI mixpanel;
    MixpanelAPI.People people;

    int clicks = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fun_facts);

        // initialize mixpanel
        mixpanel = MixpanelAPI.getInstance(this, projectToken);
        people = mixpanel.getPeople();
        String distinctId = mixpanel.getDistinctId();
        people.identify(distinctId);

        // register app for push notifications
        people.initPushHandling("946038181029");

        //create some props and track app open event
        try {
            JSONObject props = new JSONObject();
            props.put("User Type", "Free");
            props.put("Logged in", false);
            mixpanel.track("App Opened", props);
            mixpanel.timeEvent("Session End");
            people.set("User Type", "Free");
            people.setOnce("Facts Viewed", 0);
        }
        catch (JSONException e) {
            Log.e("FUNFACTS", "Unable to add props to json obj", e);
        }

        // Declare our View variables and assign the the Views from the layout file
        final TextView factLabel = (TextView) findViewById(R.id.factTextView);
        final Button showFactButton = (Button) findViewById(R.id.showFactButton);
        final RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);
        final Button userInfoButton = (Button) findViewById(R.id.userInfoButton);


        // setup click listener for the view fact button
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            // when the button is clicked, do the following
            public void onClick(View view) {
                // increment the number of clicks
                clicks++;
                String fact = mFactBook.getFact();
                // Update the label with our dynamic fact
                factLabel.setText(fact);

                int color = mColorWheel.getColor();
                relativeLayout.setBackgroundColor(color);
                showFactButton.setTextColor(color);

                // track the fact click as an event
                try {
                    JSONObject props = new JSONObject();
                    props.put("Color", color);
                    props.put("Fact", fact);
                    props.put("Facts Viewed", clicks);
                    mixpanel.track("New Fact", props);
                    people.increment("Facts Viewed", 1);
                    people.trackCharge(1, null);
                }
                catch (JSONException e) {
                    Log.e("FUNFACTS", "Unable to add props to json obj", e);
                }
            }
        };

        // set the listener to fire when the showFactButton is clicked
        showFactButton.setOnClickListener(listener);

        // create listener for the user info button
        View.OnClickListener userListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // start EnterUserInfo activity
//                EnterUserInfo userInfo = new EnterUserInfo();
                setContentView(R.layout.activity_enter_user_info);
            }
        };
        userInfoButton.setOnClickListener(userListener);

        //Toast.makeText(this, "Yay! Our Activity was created!", Toast.LENGTH_LONG).show();
        Log.d(TAG, "We're logging from the onCreate() method!");
    }

    @Override
    protected void onResume() {
        super.onResume();

        mixpanel.track("App Resumed", null);
        try {
            people.showNotificationIfAvailable(this);
//          Toast.makeText(getApplicationContext(), "yee", Toast.LENGTH_LONG).show();
        }
        catch (Exception e) {
            e.printStackTrace();
//          Toast.makeText(getApplicationContext(), "fuck", Toast.LENGTH_LONG).show();

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mixpanel.track("Session End", null);
        mixpanel.flush();
    }
}









