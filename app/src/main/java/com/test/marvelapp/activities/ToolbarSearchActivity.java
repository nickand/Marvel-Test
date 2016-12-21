package com.test.marvelapp.activities;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.test.marvelapp.R;
import com.test.marvelapp.Utils.GridSpacingItemDecoration;
import com.test.marvelapp.adapters.SearchableAdapter;
import com.test.marvelapp.database.CharactersTb;
import com.test.marvelapp.database.DBManager;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class ToolbarSearchActivity extends AppCompatActivity {

    private static final String CLASS_TAG = ToolbarSearchActivity.class.getName();
    // The data to show
    private List<CharactersTb> characters;

    private int mPos = 0;
    private SearchableAdapter adapter;
    private RecyclerView recyclerView;
    private GridSpacingItemDecoration gridItemDecoration;
    private TextView textNotFound;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toolbar_search);

        initView();

        // React to user clicks on item
        recyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // We know the View is a <extView so we can cast it
                TextView clickedView = (TextView) v.findViewById(R.id.name);

                Toast.makeText(getApplicationContext(), "Item with id ["+v.getId()+"] - Position ["+mPos+"] - HEROE ["+clickedView.getText()+"]", Toast.LENGTH_SHORT).show();
            }
        });


        // we register for the contextmneu
        registerForContextMenu(recyclerView);

        EditText editTxt = (EditText) findViewById(R.id.editTxt);

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
    }


    // We want to create a context Menu when the user long click on an item
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {

        super.onCreateContextMenu(menu, v, menuInfo);
        AdapterView.AdapterContextMenuInfo aInfo = (AdapterView.AdapterContextMenuInfo) menuInfo;

        // We know that each row in the adapter is a Map
        CharactersTb planet =  adapter.getItem(aInfo.position);

        menu.setHeaderTitle("Options for " + planet.getTitle());
        menu.add(1, 1, 1, "Details");
        menu.add(1, 2, 2, "Delete");

    }




    // This method is called when user selects an Item in the Context menu
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        AdapterView.AdapterContextMenuInfo aInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        characters.remove(aInfo.position);
        adapter.notifyDataSetChanged();
        return true;
    }


    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        textNotFound = (TextView) findViewById(R.id.tvMessageNotFound);

        // We get the ListView component from the layout
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);

        characters = new ArrayList<CharactersTb>();

        gridItemDecoration = new GridSpacingItemDecoration(this, 2, dpToPx(5), true);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(gridItemDecoration);

        try {
            Glide.with(this).load(R.drawable.cover_marvel).into((ImageView) findViewById(R.id.backdrop));
        } catch (Exception e) {
            e.printStackTrace();
        }

        characters = DBManager.getInstance(getApplicationContext()).getCharacters();

        for (int i = 0; i < characters.size(); i++) {
            Log.d(CLASS_TAG, "HEROES = " + characters.get(i).getThumbnail());
        }

        adapter = new SearchableAdapter(ToolbarSearchActivity.this, characters);
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