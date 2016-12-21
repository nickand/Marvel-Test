package com.test.marvelapp.activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.test.marvelapp.R;
import com.test.marvelapp.Utils.App;
import com.test.marvelapp.Utils.GridSpacingItemDecoration;
import com.test.marvelapp.database.CharactersTb;
import com.bumptech.glide.Glide;
import com.bumptech.glide.util.Util;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.flaviofaria.kenburnsview.Transition;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import butterknife.ButterKnife;

/**
 * Created by Nicolas on 18/12/2016.
 */

public class SessionFacebookActivity extends AppCompatActivity {

    private static final String TAG = SessionFacebookActivity.class.getSimpleName();
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private boolean isFacebookLogged = false;

    private String fullName = "";
    private String imageProfile = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_session_fb);

        callbackManager = CallbackManager.Factory.create();

        Button buttonLogin = (Button) findViewById(R.id.buttonLogin);
        loginButton = (LoginButton) findViewById(R.id.login_button);

        final KenBurnsView kbv = (KenBurnsView) findViewById(R.id.image);
        kbv.resume();

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginButton.performClick();
            }
        });

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "onSuccess: ");
                isFacebookLogged = true;
                requestFacebookData();
                kbv.pause();
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "onCancel: ");
            }

            @Override
            public void onError(FacebookException e) {
                Log.d(TAG, "onError: ");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isFacebookLoggedIn()) {
            getLaunchIntent(getApplicationContext(), MainActivity.class);
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

    public void getLaunchIntent(Context context, Class<?> classname) {
        Intent intent = new Intent(context, classname);
        startActivity(intent);
    }

    public void requestFacebookData() {
        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(),
            new GraphRequest.GraphJSONObjectCallback() {
                @Override
                public void onCompleted(JSONObject object, GraphResponse response) {

                    JSONObject json = response.getJSONObject();
                    try {
                        if (json != null) {

                            String id = null;
                            try {
                                id = json.getString("id");
                                String name = json.getString("first_name");
                                String lastName = json.getString("last_name");
                                String img_avatar = json.getJSONObject("picture").getJSONObject("data").getString("url");

                                fullName = name.concat(" ").concat(lastName);
                                imageProfile = img_avatar;

                                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                i.putExtra("fullName", fullName);
                                i.putExtra("imageProfile", imageProfile);
                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(i);
                                finish();

                                Log.d(TAG, "ID: " + id + " name: " + name + " lastname: " + lastName + " image: " + img_avatar);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (Exception e) {
                        Log.d(TAG, "ERROR: ".concat(e.getMessage()));
                    }
                }
            });

            Bundle parameters = new Bundle();
            parameters.putString("fields",
                    "id, name, first_name, last_name, link, email, picture.width(150).height(150), timezone, locale, gender, age_range");
            request.setParameters(parameters);
            request.executeAsync();
    }

    public boolean isFacebookLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}