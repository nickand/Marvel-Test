package com.test.marvelapp.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.test.marvelapp.Utils.Constants;
import com.test.marvelapp.database.DBManager;
import com.test.marvelapp.models.MarvelResults;
import com.test.marvelapp.models.MarvelSuper;
import com.google.gson.Gson;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by javiexpo on 25/7/16.
 */
public class DataService extends IntentService {
    private static String TAG = DataService.class.getName();

    private DBManager dbManager;

    public DataService(){
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent workIntent) {
        // Gets data from the incoming Intent
        String dataString = workIntent.getDataString();
        switch (dataString){
            case Constants.SERVCMD_GET_COMICS:
                if (!DBManager.getInstance(getApplicationContext()).hasComics()){
                    loadMarvelComics();
                } else {
                    broadcastProgressStatus(100);
                }
        }
    }

    private void broadcastProgressStatus(int status){
        Intent localIntent =
                new Intent(Constants.BROADCAST_ACTION)
                        .putExtra(Constants.EXTRA_DATA_STATUS, status);
        LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);
    }

    public void loadMarvelComics() {
        RestClient.IMarvelService service = RestClient.getClient();
        Call<MarvelSuper> call = service.getComics();
        call.enqueue(new Callback<MarvelSuper>() {

            @Override
            public void onResponse(Call<MarvelSuper> call, Response<MarvelSuper> response) {
                Log.d(TAG, "Status Code = " + response.code());
                if (response.isSuccessful()) {
                    // request successful (status code 200, 201)
                    MarvelSuper result = response.body();
                    Log.d(TAG, "response = " + new Gson().toJson(result));

                    ArrayList<MarvelResults> characters = result.getData().getResults();
                    if (DBManager.getInstance(getApplicationContext()).saveCharacters(characters)){
                        broadcastProgressStatus(100);
                    }
                } else {
                    // response received but request not successful (like 400,401,403 etc)
                    //Handle errors
                }
            }

            @Override
            public void onFailure(Call<MarvelSuper> call, Throwable t) {
                Log.e(TAG, t.getMessage());
            }
        });
    }
}
