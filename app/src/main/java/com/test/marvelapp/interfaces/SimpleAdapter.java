package com.android.marvelapp.interfaces;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.test.marvelapp.R;
import com.test.marvelapp.activities.DetailsActivity;
import com.test.marvelapp.database.CharactersTb;
import com.bumptech.glide.Glide;

import java.util.List;

public class SimpleAdapter extends RecyclerView.Adapter<SimpleAdapter.SimpleViewHolder>
        implements ItemClickListener {
    private final Context context;
    private List<CharactersTb> items;

    public static class SimpleViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        // Campos respectivos de un item
        public TextView nombre, description;
        public ImageView avatar;
        public ItemClickListener listener;

        public SimpleViewHolder(View v, ItemClickListener listener) {
            super(v);

            nombre = (TextView) v.findViewById(R.id.list_item_textview);
            description = (TextView) v.findViewById(R.id.tv_description);
            avatar = (ImageView) v.findViewById(R.id.avatar);
            this.listener = listener;
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onItemClick(v, getAdapterPosition());
        }
    }

    public SimpleAdapter(Context context, List<CharactersTb> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_item, viewGroup, false);
        return new SimpleViewHolder(v, this);
    }

    @Override
    public void onBindViewHolder(SimpleViewHolder viewHolder, int i) {
        CharactersTb currentItem = items.get(i);
        viewHolder.nombre.setText(currentItem.getName());
        viewHolder.description.setText(currentItem.getDescription());
        Glide.with(viewHolder.avatar.getContext())
                .load(currentItem.getThumbnail())
                .centerCrop()
                .into(viewHolder.avatar);
    }

    /**
     * Sobrescritura del método de la interfaz {@link ItemClickListener}
     *
     * @param view     item actual
     * @param position posición del item actual
     */
    @Override
    public void onItemClick(View view, int position) {
        DetailsActivity.createInstance(
                (Activity) context, items.get(position));
    }


}


interface ItemClickListener {
    void onItemClick(View view, int position);
}