package com.test.marvelapp.activities;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.test.marvelapp.R;
import com.test.marvelapp.database.CharactersTb;
import com.bumptech.glide.Glide;
import com.facebook.login.LoginManager;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    private static final String EXTRA_FULLNAME = "com.marvelapp.mName";
    private static final String EXTRA_IMAGE_PROFILE = "com.marvelapp.mImage";

    private String mName = "";
    private String mImage = "";
    private MainActivity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initView();
    }

    public void initView() {
        validateData();
    }

    private void validateData() {

        CircleImageView imageProfile = (CircleImageView) findViewById(R.id.imageProfile);
        TextView textFullName = (TextView) findViewById(R.id.textFullName);
        Button buttonLogout = (Button) findViewById(R.id.buttonLogout);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            mName = extras.getString("fullName");
            mImage = extras.getString("imageProfile");

            if (mName != null && !mName.isEmpty()) {
                textFullName.setText(mName);
            } else {
                textFullName.setText("User data is not available");
            }

            if (mImage != null && !mImage.isEmpty()) {
                Glide.with(this).load(mImage).into(imageProfile);
            }
        }

        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getApplicationContext(), SessionFacebookActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);

                finish();
            }
        });
    }
}
