package com.test.marvelapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.AccessToken;
import com.facebook.Profile;
import com.facebook.login.widget.LoginButton;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.test.marvelapp.R;
import com.test.marvelapp.activities.MainActivity;
import com.test.marvelapp.interfaces.OnLoginActivityListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginFragment extends Fragment {

    public static final String TAG = LoginFragment.class.getSimpleName();
    private static final String PARCEL_KEY = "parcel_key";
    private Bundle args;

    @BindView(R.id.login_button)
    LoginButton loginFbButton;
    @BindView(R.id.image)
    KenBurnsView kenBurnsView;

    private OnLoginActivityListener mListener;

    public LoginFragment() {}

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        return fragment;
    }

    public static LoginFragment newInstance(Bundle param) {
        LoginFragment fragment = new LoginFragment();
        fragment.setArguments(param);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isFacebookLoggedIn()) {
            Log.d(TAG, "ESTOY LOGUEADO CON FACEBOOK");
            Profile profile = Profile.getCurrentProfile();
            userLoggedGoMainActivity(profile);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnLoginActivityListener) {
            mListener = (OnLoginActivityListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnLoginActivityListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @OnClick({R.id.buttonLogin})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonLogin:
//                onButtonFacebookPressed();
                onGoMainActivity();
                break;
        }
    }

    public void onGoMainActivity() {
        if (mListener != null) {
            mListener.onGoToMainActivity();
        }
    }

    private void onButtonFacebookPressed() {
        if (mListener != null) {
            mListener.onFacebookIconClick();
        }
    }

    public boolean isFacebookLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }

    private void userLoggedGoMainActivity(Profile profile) {

        if (profile != null && isFacebookLoggedIn()) {
            Log.d(TAG, "HOME FRAGMENT SENDING DATA FACEBOOK");
            Intent mIntent = new Intent(getActivity().getApplicationContext(), MainActivity.class);
            args = new Bundle();
            args.putParcelable(PARCEL_KEY, profile);
            mIntent.putExtras(args);
            startActivity(mIntent);
            getActivity().finish();
        }
    }
}
