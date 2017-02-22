package com.app.zad.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.app.zad.R;
import com.app.zad.helper.Drawable_into_Bitmap;
import com.app.zad.helper.ItemClickSupport;

import java.sql.SQLException;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Authors_list_quotes_notBoring extends AppCompatActivity{

    private static final String QUOTES_RV_POSITION_KEY = "QUOTES_RV_POSITION_KEY";
	private RecyclerView authorQuotesRV;

	ArrayList<Quote> authorQuotesList;
	String authorRetrieved;
    int quotesRvPosition;
    private Context context;
    private Drawable authorImage;


    @SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		setContentView(R.layout.authors_not_boring);
        context = this;
        TextView authorTitle = (TextView) findViewById(R.id.author_profile_Title);
        CircleImageView authorProfileImage = (CircleImageView) findViewById(R.id.author_profile_image);
        Toolbar toolbar = (Toolbar) findViewById(R.id.author_toolbar);
        authorQuotesRV = (RecyclerView) findViewById(R.id.recycler_view_author_quotes);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.transparent_black));
		}



		Intent author_intent = getIntent();
		Quote quoteInstance = new Quote();
		authorRetrieved = author_intent.getExtras().getString("authorRetrieved");
        authorImage = quoteInstance.getAuthorImage(authorRetrieved);
        authorTitle.setText(authorRetrieved);
        authorProfileImage.setImageDrawable(authorImage);
        setupRecyclerView();


	}
	private void setupRecyclerView(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        authorQuotesRV.setHasFixedSize(true);
        authorQuotesRV.setLayoutManager(linearLayoutManager);
        try {
            Quotes_List_adapter adapter = new Quotes_List_adapter(this, generateData(), false);
            authorQuotesRV.setAdapter(adapter);
            authorQuotesRV.smoothScrollToPosition(quotesRvPosition);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ItemClickSupport.addTo(authorQuotesRV).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                Intent i1 = new Intent(context,
                        Quote_view_pager_activity.class);
                i1.putExtra("oneQuote", false);
                i1.putExtra("authorRetrived", authorRetrieved);
                i1.putExtra("favo", false);
                i1.putExtra("pos", position);
                i1.putExtra("authorsfragment", true);
                if ( authorImage != null) {
                    i1.putExtra("pic", Drawable_into_Bitmap.drawableToBitmap(authorImage));
                }
                startActivity(i1);

            }
        });
    }


	private ArrayList<Quote> generateData() throws SQLException {
		Quote quoteInstance = new Quote();
		authorQuotesList = quoteInstance.getAnObjects(this, "Author", authorRetrieved);
		return authorQuotesList;
	}


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        LinearLayoutManager layoutManager = (LinearLayoutManager) authorQuotesRV.getLayoutManager();
        outState.putInt(QUOTES_RV_POSITION_KEY,layoutManager.findFirstCompletelyVisibleItemPosition());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        quotesRvPosition = savedInstanceState.getInt(QUOTES_RV_POSITION_KEY,0);
    }
}

