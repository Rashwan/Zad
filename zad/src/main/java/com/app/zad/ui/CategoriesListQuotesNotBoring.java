package com.app.zad.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.app.zad.R;
import com.app.zad.helper.ItemClickSupport;

import java.sql.SQLException;
import java.util.ArrayList;

public class CategoriesListQuotesNotBoring extends AppCompatActivity {

	ArrayList<Quote> categoryQuotes;
	Context mContext;
	Integer cat_int;
	String cat_string;

    private RecyclerView categoryQuotesRv;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cat_not_boring);
        mContext = this;
        ImageView categoryImage = (ImageView) findViewById(R.id.category_image);
        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.category_collapsing_toolbar);
        Toolbar categoryToolbar = (Toolbar) findViewById(R.id.category_toolbar);
        categoryQuotesRv = (RecyclerView) findViewById(R.id.category_quotes_rv);
        setSupportActionBar(categoryToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.transparent_black));
        }


		Intent cat_intent = getIntent();
		cat_int = cat_intent.getExtras().getInt("categoriesNum");
		cat_string = cat_intent.getExtras().getString("categoriesString");

        categoryImage.setImageDrawable(chooseCategoryPicture(cat_int));

        collapsingToolbar.setTitle(cat_string);
        collapsingToolbar.setContentScrimColor(ContextCompat.getColor(this,R.color.theme_primary));
        collapsingToolbar.setCollapsedTitleGravity(View.TEXT_ALIGNMENT_VIEW_END);
        collapsingToolbar.setStatusBarScrimColor(ContextCompat.getColor(this,R.color.transparent_black));
        setupRecyclerView();

	}
    private void setupRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        categoryQuotesRv.setHasFixedSize(true);
        categoryQuotesRv.setLayoutManager(linearLayoutManager);
        try {
            Quotes_List_adapter adapter = new Quotes_List_adapter(this, generateData(), false);
            categoryQuotesRv.setAdapter(adapter);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ItemClickSupport.addTo(categoryQuotesRv).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                Intent i1 = new Intent(mContext,
                        Quote_view_pager_activity.class);
                i1.putExtra("categories", true);
                i1.putExtra("oneQuote", false);
                i1.putExtra("catInt", cat_int);
                i1.putExtra("catString", cat_string);
                i1.putExtra("favo", false);
                i1.putExtra("pos", position);
                startActivity(i1);

            }
        });
    }

	private ArrayList<Quote> generateData() throws SQLException {
		Quote quoteInstance = new Quote();
		categoryQuotes = quoteInstance.getAnObjects(mContext, "Category",
				cat_int);
		return categoryQuotes;
	}

	private Drawable chooseCategoryPicture(int categoryPosition) {
        Drawable drawable;
        if (categoryPosition==1){
            categoryPosition = 0;
        }else {
            categoryPosition -= 2;
        }

        switch (categoryPosition){
            case 0:
               drawable = ContextCompat.getDrawable(this,R.drawable.cat_islamic);
                break;
            case 1:
               drawable = ContextCompat.getDrawable(this,R.drawable.cat_politics);
                break;
            case 2:
                drawable = ContextCompat.getDrawable(this,R.drawable.cat_love);
                break;
            case 3:
                drawable = ContextCompat.getDrawable(this,R.drawable.cat_enterperneurship);
                break;
            case 4:
                drawable = ContextCompat.getDrawable(this,R.drawable.cat_humanity);
                break;
            case 5:
                drawable = ContextCompat.getDrawable(this,R.drawable.cat_art);
                break;
            case 6:
                drawable = ContextCompat.getDrawable(this,R.drawable.cat_books);
                break;
            case 7:
               drawable = ContextCompat.getDrawable(this,R.drawable.cat_funny);
                break;
            case 8:
               drawable = ContextCompat.getDrawable(this,R.drawable.cat_sad);
                break;
            case 9:
                drawable = ContextCompat.getDrawable(this,R.drawable.cat_other);
                break;
            default:
                drawable = ContextCompat.getDrawable(this,R.drawable.image_new6);
        }
        return drawable;
	}
}