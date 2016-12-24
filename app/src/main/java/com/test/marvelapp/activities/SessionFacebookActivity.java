package com.test.marvelapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.facebook.login.LoginManager;
import com.test.marvelapp.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.test.marvelapp.fragments.LoginFragment;
import com.test.marvelapp.fragments.MainFragment;
import com.test.marvelapp.interfaces.OnClickActivityListener;
import com.test.marvelapp.interfaces.OnLoginActivityListener;

import org.json.JSONObject;

import java.util.Arrays;

/**
 * Created by Nicolas on 18/12/2016.
 */

public class SessionFacebookActivity extends AppCompatActivity implements OnClickActivityListener, OnLoginActivityListener {

    private static final String TAG = SessionFacebookActivity.class.getSimpleName();

    private Fragment mFragment;
    private boolean isFacebookLogged = false;
    private CallbackManager callbackManager;
    private Bundle args;
    private FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session_fb);

        fm = getSupportFragmentManager();

        FacebookSdk.sdkInitialize(this.getApplicationContext());

        // do something else
        Fragment fragment = null;
        fragment = LoginFragment.newInstance();
        navigateTo(fragment);

        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                        Log.d(TAG, "ON SUCCESS FACEBOOK");
                        isFacebookLogged = true;
//                        showProgressDialog();
                        requestFacebookData();
//                        navigateTo(MainFragment.newInstance(args));
                        onGoToMainActivity();
                    }

                    @Override
                    public void onCancel() {
                        // App code
                        Log.d(TAG, "ON CANCEL FACEBOOK");
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                        Log.d(TAG, "ON ERROR FACEBOOK");
                    }
                });
    }

    public void requestFacebookData() {
        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(),
            new GraphRequest.GraphJSONObjectCallback() {
                @Override
                public void onCompleted(JSONObject object, GraphResponse response) {

                    JSONObject json = response.getJSONObject();
                    try {
                        if (json != null) {

                            String id = json.getString("id");
                            String name = json.getString("first_name");
                            String lastName = json.getString("last_name");
                            String img_avatar = json.getJSONObject("picture").getJSONObject("data").getString("url");

                            Log.d(TAG, "ID: " + id + " name: " + name + " lastname: " + lastName + " image: " + img_avatar);

                            args = new Bundle();
                            args.putString("first_name", name);
                            args.putString("last_name", lastName);
                            args.putString("picture", img_avatar);
                        }
                    } catch (Exception e) {
                        Log.d(TAG, "ERROR: " + e);
                    }
                }
            }
        );
        Bundle parameters = new Bundle();
        parameters.putString("fields",
                "id, name, first_name, last_name, link, email, picture.width(150).height(150), timezone, locale, gender, age_range");
        request.setParameters(parameters);
        request.executeAsync();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private String getLastTAG() {
        if (fm.getBackStackEntryCount() < 1){
            return "";
        } else{
            return fm.getBackStackEntryAt(fm.getBackStackEntryCount() - 1).getName();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setTitleToolbar(String title) {
    }

    @Override
    public void navigateTo(Fragment fragment) {
        navigateTo(fragment, true);
    }

    @Override
    public void navigateTo(Fragment fragment, boolean addToBackStack) {
        final FragmentManager manager = getSupportFragmentManager();

        // Removes current fragment from back stack,
        // if user presses back later he skips this fragment.
        // Avoid adding this fragment to back stack, causes fragments overlapping.
        if (!addToBackStack) {
            manager.popBackStackImmediate();
        }

        FragmentTransaction fragmentTransaction = manager.beginTransaction();

        if (mFragment == null) {
            fragmentTransaction.add(R.id.fragment_container, fragment).commit();

        } else {

            fragmentTransaction.setCustomAnimations(
                    R.anim.enter_from_right, R.anim.exit_to_left,
                    R.anim.enter_from_left, R.anim.exit_to_right);
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();


        }

        mFragment = fragment;
    }

    @Override
    public void onFavoritesClick() {

    }

    @Override
    public void onSearchClick() {

    }

    @Override
    public void onProfileClick() {

    }

    @Override
    public void onFacebookIconClick() {
        Log.d(TAG, "EJECUTANDO ONCLICK FACEBOOK LISTENER");
        LoginManager.getInstance().logInWithReadPermissions(SessionFacebookActivity.this,
                Arrays.asList("user_birthday", "user_status", "user_friends"));
    }

    @Override
    public void onGoToMainActivity() {
        Intent mIntent = new Intent(getApplicationContext(), MainActivity.class);
        mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(mIntent);
        finish();
    }

    public void logoutFacebook() {
        LoginManager.getInstance().logOut();
    }

    @Override
    public void onClearBackStack() {
        for (int i = 0; i < fm.getBackStackEntryCount(); i++) {
            fm.popBackStackImmediate();
        }
    }
}