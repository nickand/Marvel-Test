package com.test.marvelapp.interfaces;

import android.support.v4.app.Fragment;

/**
 * Created by Nicolas on 13/10/2016.
 */

public interface OnClickActivityListener {

    void setTitleToolbar(String title);
    void navigateTo(Fragment fragment);
    void navigateTo(Fragment fragment, boolean addToBackStack);
    void onFavoritesClick();
    void onSearchClick();
    void onProfileClick();
}
