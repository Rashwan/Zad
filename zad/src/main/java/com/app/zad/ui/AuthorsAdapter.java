package com.app.zad.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.zad.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by rashwan on 2/23/17.
 */

public class AuthorsAdapter extends RecyclerView.Adapter<AuthorsAdapter.AuthorsVH> {
    private Context context;
    private ArrayList<Author_Grid_Item> authorsList;
    public AuthorsAdapter(Context context, ArrayList<Author_Grid_Item> authorsList) {
        this.context = context;
        this.authorsList = authorsList;
        Collections.sort(authorsList, new Comparator<Author_Grid_Item>() {
            @Override
            public int compare(Author_Grid_Item o1, Author_Grid_Item o2) {
                return o1.getAuthor_Title().compareTo(o2.getAuthor_Title());
            }
        });

    }

    @Override
    public AuthorsVH onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.author_grid_item, parent, false);
        return new AuthorsVH(view);
    }

    @Override
    public void onBindViewHolder(AuthorsVH holder, int position) {
        Author_Grid_Item authorItem = authorsList.get(position);
        holder.authorNameTv.setText(authorItem.getAuthor_Title());
        holder.authorImageIv.setImageDrawable(authorItem.getAuthor_Pic());
    }

    @Override
    public int getItemCount() {
        return authorsList.size();
    }
    public Author_Grid_Item getItem(int position){
        return authorsList.get(position);
    }

    public static class AuthorsVH extends RecyclerView.ViewHolder{
        ImageView authorImageIv;
        TextView authorNameTv;
        public AuthorsVH(View itemView) {
            super(itemView);
            authorImageIv = (ImageView) itemView.findViewById(R.id.author_image_iv);
            authorNameTv = (TextView) itemView.findViewById(R.id.author_name_tv);
        }
    }
}
