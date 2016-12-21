package com.test.marvelapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.test.marvelapp.R;
import com.test.marvelapp.activities.DetailsActivity;
import com.test.marvelapp.database.CharactersTb;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nicolas on 07/08/2016.
 */
public class SearchableAdapter extends RecyclerView.Adapter<SearchableAdapter.MyViewHolder> implements Filterable {

    private List<CharactersTb> charactersList;
    private Context mContext;
    private Filter searchFilter;
    private List<CharactersTb> origCharacterList;
    private CharactersTb marvel;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, textNotFound;
        public ImageView thumbnail;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.name);
            textNotFound = (TextView) view.findViewById(R.id.tvMessageNotFound);
            thumbnail = (ImageView) view.findViewById(R.id.img);
        }
    }

    public SearchableAdapter(Context mContext, List<CharactersTb> charactersList) {
        this.mContext = mContext;
        this.charactersList = charactersList;
        this.origCharacterList = charactersList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.img_row_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        marvel = charactersList.get(position);
        holder.title.setText(marvel.getTitle());

        // loading album cover_bg using Glide library
        Glide.with(mContext).load(marvel.getThumbnail()).into(holder.thumbnail);


        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(mContext, "Position: "+position, Toast.LENGTH_SHORT).show();
                DetailsActivity.createInstance(
                        (Activity) mContext, charactersList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return charactersList.size();
    }

    public CharactersTb getItem(int position) {
        return charactersList.get(position);
    }

    public void resetData() {
        charactersList = origCharacterList;
    }

	/*
	 * We create our filter
	 */

    @Override
    public Filter getFilter() {
        if (searchFilter == null)
            searchFilter = new CharacterFilter();

        return searchFilter;
    }

    private class CharacterFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            // We implement here the filter logic
            if (constraint == null || constraint.length() == 0) {
                // No filter implemented we return all the list
                results.values = origCharacterList;
                results.count = origCharacterList.size();
            }
            else {
                // We perform filtering operation
                List<CharactersTb> heroeList = new ArrayList<CharactersTb>();

                for (CharactersTb p : charactersList) {
                    if (p.getTitle().toUpperCase().startsWith(constraint.toString().toUpperCase()))
                        heroeList.add(p);
                }

                results.values = heroeList;
                results.count = heroeList.size();

            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {

            // Now we have to inform the adapter about the new list filtered
            if (results.count == 0)
                notifyDataSetChanged();
            else {
                charactersList = (List<CharactersTb>) results.values;
                notifyDataSetChanged();
            }

        }

    }
}