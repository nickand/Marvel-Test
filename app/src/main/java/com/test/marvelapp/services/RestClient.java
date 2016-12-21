package com.test.marvelapp.services;

import com.test.marvelapp.models.MarvelCharacter;
import com.test.marvelapp.models.MarvelSuper;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;


/**
 * Created by Nicolas on 28/05/2016.
 */
public class RestClient {

    private static final String CLASS_TAG = RestClient.class.getName();
    private static IMarvelService marvelApiInterface ;
    private static String baseUrl = "https://gateway.marvel.com/" ;

    public static IMarvelService getClient() {
        if (marvelApiInterface == null) {

            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient okClient = new OkHttpClient();
            okClient.newBuilder().addInterceptor(interceptor).build();
            okClient.newBuilder().addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request original = chain.request();

                    // Request customization: add request headers
                    Request.Builder requestBuilder = original.newBuilder()
                            .method(original.method(), original.body());

                    Request request = requestBuilder.build();

                    return chain.proceed(request);
                }
            }).build();

            Retrofit client = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(okClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            marvelApiInterface = client.create(IMarvelService.class);
        }
        return marvelApiInterface ;
    }

    public interface IMarvelService {

        @GET("/v1/public/comics?ts=1&apikey=8f6f5e5729414b00579c45287647d111&hash=8eee14e76a0617ce8eeba6d624ae44ae")
        Call<MarvelSuper> getComics();
        @GET("/v1/public/comics/{comicId}?ts=1&apikey=8f6f5e5729414b00579c45287647d111&hash=8eee14e76a0617ce8eeba6d624ae44ae")
        Call<MarvelCharacter> getComicsById(@Path("comicId") int id);
    }

}
