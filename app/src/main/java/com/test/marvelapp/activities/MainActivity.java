package com.test.marvelapp.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.test.marvelapp.R;
import com.test.marvelapp.fragments.FavoritesFragment;
import com.test.marvelapp.fragments.MainFragment;
import com.test.marvelapp.interfaces.OnClickActivityListener;

public class MainActivity extends AppCompatActivity implements OnClickActivityListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private Fragment mFragment;
    private Fragment firstFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // do something else
        Fragment fragment = null;
        fragment = MainFragment.newInstance();
        navigateTo(fragment);

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
        Fragment fragment = null;
        fragment = FavoritesFragment.newInstance();
        navigateTo(fragment);
    }

    @Override
    public void onSearchClick() {

    }

    @Override
    public void onProfileClick() {

    }
}
