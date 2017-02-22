package com.app.zad.ui;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.zad.R;

/**
 * Created by rashwan on 2/22/17.
 */

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.CategoriesVH> {
    private Context context;
    private final String[] categoriesList ;
    private Typeface type;


    public CategoriesAdapter(Context context) {
        this.context = context;
        categoriesList = context.getResources().getStringArray(R.array.categoriesnew);
        type = Typeface.createFromAsset(context.getAssets(),
                "fonts/adobe.otf");
    }

    @Override
    public CategoriesVH onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.categories_list_item, parent, false);

        return new CategoriesVH(view);
    }

    @Override
    public void onBindViewHolder(CategoriesVH holder, int position) {
        holder.categoryTv.setTypeface(type);
        switch (position){
            case 0:
                holder.categoryTv.setText(categoriesList[position]);
                holder.categoryIv
                        .setImageDrawable(ContextCompat.getDrawable(context,R.drawable.cat_islamic));
                break;
            case 1:
                holder.categoryTv.setText(categoriesList[position]);
                holder.categoryIv
                        .setImageDrawable(ContextCompat.getDrawable(context,R.drawable.cat_politics));
                break;
            case 2:
                holder.categoryTv.setText(categoriesList[position]);
                holder.categoryIv
                        .setImageDrawable(ContextCompat.getDrawable(context,R.drawable.cat_love));
                break;
            case 3:
                holder.categoryTv.setText(categoriesList[position]);
                holder.categoryIv
                        .setImageDrawable(ContextCompat.getDrawable(context,R.drawable.cat_enterperneurship));
                break;
            case 4:
                holder.categoryTv.setText(categoriesList[position]);
                holder.categoryIv
                        .setImageDrawable(ContextCompat.getDrawable(context,R.drawable.cat_humanity));
                break;
            case 5:
                holder.categoryTv.setText(categoriesList[position]);
                holder.categoryIv
                        .setImageDrawable(ContextCompat.getDrawable(context,R.drawable.cat_art));
                break;
            case 6:
                holder.categoryTv.setText(categoriesList[position]);
                holder.categoryIv
                        .setImageDrawable(ContextCompat.getDrawable(context,R.drawable.cat_books));
                break;
            case 7:
                holder.categoryTv.setText(categoriesList[position]);
                holder.categoryIv
                        .setImageDrawable(ContextCompat.getDrawable(context,R.drawable.cat_funny));
                break;
            case 8:
                holder.categoryTv.setText(categoriesList[position]);
                holder.categoryIv
                        .setImageDrawable(ContextCompat.getDrawable(context,R.drawable.cat_sad));
                break;
            case 9:
                holder.categoryTv.setText(categoriesList[position]);
                holder.categoryIv
                        .setImageDrawable(ContextCompat.getDrawable(context,R.drawable.cat_other));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return categoriesList.length;
    }

    public static class CategoriesVH extends RecyclerView.ViewHolder{
        ImageView categoryIv;
        TextView categoryTv;
        public CategoriesVH(View itemView) {
            super(itemView);
            categoryIv = (ImageView) itemView.findViewById(R.id.category_image);
            categoryTv = (TextView) itemView.findViewById(R.id.category_name);
        }
    }
}
