package com.teamtreehouse.funfacts;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.mixpanel.android.mpmetrics.MixpanelAPI;

public class EnterUserInfo extends ActionBarActivity {

    MixpanelAPI mixpanel;
    MixpanelAPI.People people;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_user_info);

        String projectToken = "sm_android";
        mixpanel = MixpanelAPI.getInstance(this, projectToken);
        people = mixpanel.getPeople();

        final Button submissionButton = (Button) findViewById(R.id.submitButton);
        final RelativeLayout userInfoLayout = (RelativeLayout) findViewById(R.id.userInfoLayout);

        final EditText firstText = (EditText) findViewById(R.id.firstNameText);
        final EditText lastText = (EditText) findViewById(R.id.lastNameText);
        final EditText phoneText = (EditText) findViewById(R.id.phoneText);
        final EditText emailText = (EditText) findViewById(R.id.emailText);

        View.OnClickListener submissionListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
    }
}
