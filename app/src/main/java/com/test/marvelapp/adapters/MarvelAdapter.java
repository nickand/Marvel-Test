package com.test.marvelapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.test.marvelapp.R;
import com.test.marvelapp.activities.DetailsActivity;
import com.test.marvelapp.database.CharactersTb;
import com.test.marvelapp.database.DBManager;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by Nicolas on 28/05/2016.
 */
public class MarvelAdapter extends RecyclerView.Adapter<MarvelAdapter.MyViewHolder> {

    private static final String CLASS_TAG = MarvelAdapter.class.getName();
    private Context mContext;
    private List<CharactersTb> charactersList;
    private CharactersTb marvel;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, price;
        public ImageView thumbnail, overflow;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            price = (TextView) view.findViewById(R.id.count);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            overflow = (ImageView) view.findViewById(R.id.overflow);
        }
    }

    public MarvelAdapter(Context mContext, List<CharactersTb> charactersList) {
        this.mContext = mContext;
        this.charactersList = charactersList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        marvel = charactersList.get(position);
        holder.title.setText(marvel.getTitle());

        if (marvel.getPrice() == 0.0){
            holder.price.setText("Not available");
        } else {
            holder.price.setText(marvel.getPrice() + "$");
        }

        // loading album cover_bg using Glide library
        Glide.with(mContext).load(marvel.getThumbnail()).into(holder.thumbnail);

        holder.overflow.setTag(position);
        holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(holder.overflow);
            }
        });

        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(mContext, "Position: "+position, Toast.LENGTH_SHORT).show();
                DetailsActivity.createInstance(
                        (Activity) mContext, charactersList.get(position));
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
        int pos = (Integer) view.getTag();
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener(pos));
        popup.show();
    }

    /**
     * Click listener for popup menu items
     */
    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {
        private int mPos;

        public MyMenuItemClickListener(int position) {
            mPos = position;
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_add_favourite:
                    Toast.makeText(mContext, "Added to favorites", Toast.LENGTH_SHORT).show();
                    addCharacterToFavorite(mPos);
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

    public void addCharacterToFavorite(int pos) {
        Log.i(CLASS_TAG, "Clicked!");
        DBManager dbManager = DBManager.getInstance(mContext);
        dbManager.saveAsFavorite(charactersList.get(pos));
    }
}
