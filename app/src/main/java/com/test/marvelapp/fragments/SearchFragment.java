package com.test.marvelapp.fragments;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.test.marvelapp.R;
import com.test.marvelapp.Utils.GridSpacingItemDecoration;
import com.test.marvelapp.adapters.SearchableAdapter;
import com.test.marvelapp.database.CharactersTb;
import com.test.marvelapp.database.DBManager;

import java.util.ArrayList;
import java.util.List;


public class SearchFragment extends Fragment {
    private static final String CLASS_TAG = SearchFragment.class.getName();
    private static final String ARG_PARAM1 = "param";
    // The data to show
    private List<CharactersTb> characters;

    private int mPos = 0;
    private SearchableAdapter adapter;
    private RecyclerView recyclerView;
    private GridSpacingItemDecoration gridItemDecoration;
    private TextView textNotFound;

    public SearchFragment() {
    }

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    public static SearchFragment newInstance(String param1) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
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
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        initView(view);

        // React to user clicks on item
        recyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // We know the View is a <extView so we can cast it
                TextView clickedView = (TextView) v.findViewById(R.id.name);

                Toast.makeText(getActivity(), "Item with id ["+v.getId()+"] - Position ["+mPos+"] - HEROE ["+clickedView.getText()+"]", Toast.LENGTH_SHORT).show();
            }
        });


        // we register for the contextmneu
        registerForContextMenu(recyclerView);

        EditText editTxt = (EditText) view.findViewById(R.id.editTxt);

        if (editTxt != null) {
            textNotFound.setVisibility(View.GONE);
            editTxt.addTextChangedListener(new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    System.out.println("Text ["+s+"] - Start ["+start+"] - Before ["+before+"] - Count ["+count+"]");
                    if (count < before) {
                        // We're deleting char so we need to reset the adapter data
                        adapter.resetData();
                    }

                    adapter.getFilter().filter(s.toString());

                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count,
                                              int after) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
        } else {
            textNotFound.setVisibility(View.VISIBLE);
        }

        return view;
    }

    private void initView(View view) {

        textNotFound = (TextView) view.findViewById(R.id.tvMessageNotFound);

        // We get the ListView component from the layout
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);

        characters = new ArrayList<CharactersTb>();

        gridItemDecoration = new GridSpacingItemDecoration(getActivity(), 2, dpToPx(5), true);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(gridItemDecoration);

        try {
            Glide.with(this).load(R.drawable.cover_marvel).into((ImageView) view.findViewById(R.id.backdrop));
        } catch (Exception e) {
            e.printStackTrace();
        }

        characters = DBManager.getInstance(getActivity()).getCharacters();

        for (int i = 0; i < characters.size(); i++) {
            Log.d(CLASS_TAG, "HEROES = " + characters.get(i).getThumbnail());
        }

        adapter = new SearchableAdapter(getActivity(), characters);
        recyclerView.setAdapter(adapter);
    }

    /**
     * Converting dp to pixel
     */
    public int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}
