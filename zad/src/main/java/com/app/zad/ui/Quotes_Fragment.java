package com.app.zad.ui;

import java.sql.SQLException;
import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.app.zad.R;
import com.app.zad.helper.FlipImageView;

public class Quotes_Fragment extends Fragment implements OnItemClickListener {
	public Context mContext;
	String author_retrived;
	String quote_retrived;
	boolean oneQuote;
	Integer id;
	String wiki;
	ArrayList<Quote> allQuotesObjects;
	ListView listView;
	Parcelable ListView_State;

	private boolean favValue;

	FlipImageView StarFavourite;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.list_quotes, container, false);
		listView = (ListView) view.findViewById(R.id.listviewix);

		update();

		return view;

	}

	private ArrayList<Quote> generateData() throws SQLException {
		Quote quoteInstance = new Quote();
		allQuotesObjects = quoteInstance.getAllObjects(mContext);
		return allQuotesObjects;
	}

	public void CheckFavourable(boolean b) {
		favValue = b;

	}

	public void onItemClick(AdapterView<?> parent, View view,
			final int position, long id) {

		oneQuote = true;
		quote_retrived = allQuotesObjects.get(position).Quote;
		author_retrived = allQuotesObjects.get(position).Author;
		id = allQuotesObjects.get(position).ID;
		Quote thequote = allQuotesObjects.get(position);
		wiki = thequote.getwiki(mContext, thequote);
		Intent i1 = new Intent(getActivity(), Quote_view_pager_activity.class);
		i1.putExtra("oneQuote", oneQuote);
		i1.putExtra("quoteRetrived", quote_retrived);
		i1.putExtra("authorRetrived", author_retrived);
		i1.putExtra("wiki", wiki);
		startActivity(i1);

	}

	public void update() {
		mContext = getActivity().getApplicationContext();

		Quotes_List_adapter myadapter = null;
		try {
			myadapter = new Quotes_List_adapter(getActivity(), generateData(),
					favValue);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		listView.setAdapter(myadapter);
		listView.setDivider(getResources().getDrawable(R.drawable.transparent));

		// Restore ListView state
		if (ListView_State != null) {
			Log.d("Quotes_Frag", "trying to restore listview state..");
			listView.onRestoreInstanceState(ListView_State);
		}

		listView.setOnItemClickListener((OnItemClickListener) this);

	}

	@Override
	public void onResume() {
		super.onResume();
		update();
	}

	@Override
	public void onPause() {
		// Save ListView state @ onPause
		Log.d("Quotes_Frag", "saving listview state @ onPause");
		ListView_State = listView.onSaveInstanceState();
		super.onPause();
	}

}