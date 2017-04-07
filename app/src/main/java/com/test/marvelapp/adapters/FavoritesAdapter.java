package com.test.marvelapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.test.marvelapp.R;
import com.test.marvelapp.Utils.Constants;
import com.test.marvelapp.database.CharactersTb;
import com.bumptech.glide.Glide;
import com.test.marvelapp.fragments.DetailFragment;
import com.test.marvelapp.fragments.FavoritesFragment;
import com.test.marvelapp.interfaces.OnClickActivityListener;

import java.util.ArrayList;

/**
 * Created by Nicolas on 28/05/2016.
 */
public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.MyViewHolder> {

    private static final String CLASS_TAG = FavoritesAdapter.class.getName();
    private Context mContext;
    private ArrayList<CharactersTb> charactersList;
    private OnClickActivityListener mListener;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, count;
        public ImageView thumbnail, overflow;

        private CharactersTb marvel;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            overflow = (ImageView) view.findViewById(R.id.overflow);
        }
    }


    public FavoritesAdapter(Context mContext, ArrayList<CharactersTb> charactersList) {
        this.mContext = mContext;
        this.charactersList = charactersList;
    }

    public FavoritesAdapter(Context mContext, ArrayList<CharactersTb> charactersList, OnClickActivityListener mListener) {
        this.mContext = mContext;
        this.charactersList = charactersList;
        this.mListener = mListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.marvel = charactersList.get(position);
        holder.title.setText(holder.marvel.getTitle());

        // loading album cover_bg using Glide library
        Glide.with(mContext).load(holder.marvel.getThumbnail()).into(holder.thumbnail);

        holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(holder.overflow);
            }
        });

        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();

                bundle.putInt(Constants.EXTRA_ID, holder.marvel.getId());
                bundle.putString(Constants.EXTRA_TITLE, holder.marvel.getTitle());
                bundle.putString(Constants.EXTRA_THUMBNAIL, holder.marvel.getThumbnail());
                bundle.putString(Constants.EXTRA_DESCRIPTION, holder.marvel.getDescription());
                bundle.putFloat(Constants.EXTRA_PRICE, holder.marvel.getPrice());
                bundle.putString(Constants.EXTRA_DATE, holder.marvel.getModified());
                bundle.putInt(Constants.EXTRA_PAGE_COUNT, holder.marvel.getPageCount());
                bundle.putString(Constants.EXTRA_STORIES, holder.marvel.getStories());
                bundle.putString(Constants.EXTRA_CREATORS, holder.marvel.getCreators());
                bundle.putString(Constants.EXTRA_CHARACTERS, holder.marvel.getCharacters());

                mListener.navigateTo(DetailFragment.newInstance(bundle));
            }
        });
    }

    /**
     * Showing popup menu when tapping on 3 dots
     */
    private void showPopupMenu(View view) {
        // inflate menu
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_album, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
        popup.show();
    }

    /**
     * Click listener for popup menu items
     */
    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        public MyMenuItemClickListener() {
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_add_favourite:
                    Toast.makeText(mContext, "Add to favorites", Toast.LENGTH_SHORT).show();
                    return true;
                default:
            }
            return false;
        }
    }

    @Override
    public int getItemCount() {
        return charactersList.size();
    }
}
