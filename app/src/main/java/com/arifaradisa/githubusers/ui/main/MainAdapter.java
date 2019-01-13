package com.arifaradisa.githubusers.ui.main;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.arifaradisa.githubusers.R;
import com.arifaradisa.githubusers.api.response.Item;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MainViewHolder> {

    private Context mContext;
    private List<Item> mItemList;

    public MainAdapter(Context context, List<Item> itemList) {
        mContext = context;
        mItemList = itemList;
    }

    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_main, parent, false);
        return new MainViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MainViewHolder holder, int position) {
        Item item = mItemList.get(position);

        Picasso.get()
                .load(item.getAvatarUrl())
                .into(holder.image);

        holder.name.setText(item.getLogin());
    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }

    class MainViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_image)
        ImageView image;
        @BindView(R.id.item_name)
        TextView name;

        public MainViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
