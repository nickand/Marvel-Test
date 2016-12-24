package com.test.marvelapp.fragments;

import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.test.marvelapp.R;
import com.test.marvelapp.Utils.Constants;
import com.test.marvelapp.Utils.GridSpacingItemDecoration;
import com.test.marvelapp.adapters.MarvelAdapter;
import com.test.marvelapp.adapters.SearchableAdapter;
import com.test.marvelapp.database.CharactersTb;
import com.test.marvelapp.database.DBManager;
import com.test.marvelapp.interfaces.OnClickActivityListener;
import com.test.marvelapp.interfaces.OnLoginActivityListener;
import com.test.marvelapp.services.DataService;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MainFragment extends Fragment {

    private static final String TAG = MainFragment.class.getSimpleName();
    private static MainFragment fragment;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;
    @BindView(R.id.imageSearch)
    ImageView imageSearch;
    @BindView(R.id.imageFavorites)
    ImageView imageFavorites;
    @BindView(R.id.imageProfile)
    ImageView imageProfile;

    private List<CharactersTb> characters;
    private MarvelAdapter adapter;
    private GridSpacingItemDecoration gridItemDecoration;

    private int mProgressStatus;

    private OnClickActivityListener mListener;

    public MainFragment() {
        // Required empty public constructor
    }

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    public static MainFragment newInstance(Bundle param) {
        fragment = new MainFragment();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        ButterKnife.bind(this, view);

        startLoading();
        initCollapsingToolbar(view);

        mProgressBar.setVisibility(View.VISIBLE);

        characters = new ArrayList<CharactersTb>();

        gridItemDecoration = new GridSpacingItemDecoration(getActivity(), 2, dpToPx(1), true);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(gridItemDecoration);

        try {
            Glide.with(this).load(R.drawable.cover_marvel).into((ImageView) view.findViewById(R.id.backdrop));
        } catch (Exception e) {
            e.printStackTrace();
        }

        imageFavorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = null;
                fragment = FavoritesFragment.newInstance();
                mListener.navigateTo(fragment);
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnClickActivityListener) {
            mListener = (OnClickActivityListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnClickActivityListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * Initializing collapsing toolbar
     * Will show and hide the toolbar title on scroll
     */
    private void initCollapsingToolbar(View view) {
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) view.findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) view.findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(getString(R.string.app_name));
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }

    private void startLoading() {
        //Nos suscribimos para recibir BROADCAST_ACTION
        // Filtramos la acción BROADCAST_ACTION
        IntentFilter mStatusIntentFilter = new IntentFilter(Constants.BROADCAST_ACTION);
        //Instanciamos al receiver
        ResponseReceiver mResponseReceiver = new ResponseReceiver();
        // Registramos el receiver y el filter
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(
                mResponseReceiver, mStatusIntentFilter);

        //Iniciamos el status del ProgressBar a 10%
        mProgressBar.setProgress(10);

        //Instanciamos un Intent para disparar el Servicio DataService
        Intent mServiceIntent = new Intent(getActivity(), DataService.class);
        //Indicamos los datos que se necesitan pasar al Servicio
        mServiceIntent.setData(Uri.parse(Constants.SERVCMD_GET_COMICS));
        //Mandamos al servicio a ejecutar la acción SERVCMD_GET_CARACTERS
        getActivity().startService(mServiceIntent);
    }

    private class ResponseReceiver extends BroadcastReceiver {
        // Prevenimos la instanciación
        private ResponseReceiver() {
        }

        // Este método es llamado cuando el BroadcastReceiver obtiene un Intent para el cual
        // ha sido registrado recibir
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "ResponseReceiver onReceive");
            switch (intent.getAction()) {
                case Constants.BROADCAST_ACTION:
                    mProgressStatus = intent.getIntExtra(Constants.EXTRA_DATA_STATUS, 10);
                    mProgressBar.setProgress(mProgressStatus);
                    if (mProgressStatus == 100) {
                        mProgressBar.setVisibility(View.GONE);

                        characters = DBManager.getInstance(getActivity()).getCharacters();

                        for (int i = 0; i < characters.size(); i++) {
                            Log.d(TAG, "COMICS PRECIO= " + characters.get(i).getPrice());
                        }

                        adapter = new MarvelAdapter(characters, mListener);
                        recyclerView.setAdapter(adapter);
                    }

                    break;
                default:
                    break;
            }

        }
    }
    /**
     * Converting dp to pixel
     */
    public int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}
