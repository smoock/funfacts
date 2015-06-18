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

    private FactBook mFactBook = new FactBook();
    private ColorWheel mColorWheel = new ColorWheel();

    String projectToken = "sm_android";
    MixpanelAPI mixpanel;
    MixpanelAPI.People people;

    int clicks = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fun_facts);

        mixpanel = MixpanelAPI.getInstance(this, projectToken);
        people = mixpanel.getPeople();
        String distinctId = mixpanel.getDistinctId();
        people.identify(distinctId);
        people.initPushHandling("946038181029");

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
        final Button submissionButton = (Button) findViewById(R.id.submitButton);
        final RelativeLayout userInfoLayout = (RelativeLayout) findViewById(R.id.userInfoLayout);

        final EditText firstText = (EditText) findViewById(R.id.firstNameText);
        final EditText lastText = (EditText) findViewById(R.id.lastNameText);
        final EditText phoneText = (EditText) findViewById(R.id.phoneText);
        final EditText emailText = (EditText) findViewById(R.id.emailText);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clicks++;
                String fact = mFactBook.getFact();
                // Update the label with our dynamic fact
                factLabel.setText(fact);

                int color = mColorWheel.getColor();
                relativeLayout.setBackgroundColor(color);
                showFactButton.setTextColor(color);

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
        showFactButton.setOnClickListener(listener);

        View.OnClickListener userListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EnterUserInfo.class);
                setContentView(R.layout.activity_enter_user_info);
            }
        };
        userInfoButton.setOnClickListener(userListener);

        View.OnClickListener submissionListener = new View.OnClickListener() {
            @Override
            public void onClick(View view2) {
                String firstName = firstText.getText().toString();
                String lastName = lastText.getText().toString();
                String phone = phoneText.getText().toString();
                String email = emailText.getText().toString();

                people.set("$first_name", firstName);
                people.set("$last_name", lastName);
                people.set("$phone", phone);
                people.set("$email", email);
                setContentView(R.layout.activity_fun_facts);
            }
        };
        submissionButton.setOnClickListener(submissionListener);

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









