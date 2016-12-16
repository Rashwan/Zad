package com.app.zad.ui;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

public class Fav_Quotes_Fragment extends Fragment implements
		OnItemClickListener {
	public Context mContext;
	String author_retrived;
	String quote_retrived;
	boolean oneQuote;
	Parcelable ListView_State;
	boolean favo;
	ArrayList<Quote> allQuotesObjects;
	ArrayList<Quote> object_retrieved;
	SharedPreferences sp;
	Set<String> id;
	List<String> idlist;
	Integer idinteger;
	boolean favValue;
	Integer pos;
	Quotes_List_adapter myadapter;
	View view;
	ListView listView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.list_quotes, container, false);
		listView = (ListView) view.findViewById(R.id.listviewix);

		update();
		return view;

	}

	private ArrayList<Quote> generateData() throws SQLException {
		try {
			allQuotesObjects = new ArrayList<Quote>();
			sp = mContext.getSharedPreferences("com.app.zad.fav_id",
					Context.MODE_PRIVATE);
			id = sp.getStringSet("ids", null);
			idlist = new ArrayList<String>(id);
			for (int i = 0; i < id.size(); i++) {
				idinteger = Integer.parseInt(idlist.get(i));
				Quote quoteInstance = new Quote();
				object_retrieved = quoteInstance.getAnObjects(mContext, "ID",
						idinteger);
				try {
					allQuotesObjects.add(object_retrieved.get(0));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		return allQuotesObjects;
	}

	public void CheckFavourable(boolean b) {
		favValue = b;

	}

	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		Intent i1 = new Intent(mContext, Quote_view_pager_activity.class);
		oneQuote = false;
		favo = true;
		pos = position;
		i1.putExtra("oneQuote", oneQuote);
		i1.putExtra("authorRetrived", author_retrived);
		i1.putExtra("favo", favo);
		i1.putExtra("pos", pos + 1);
		startActivity(i1);

	}

	@SuppressLint("InflateParams")
	public void update() {
		mContext = getActivity().getApplicationContext();
		myadapter = null;
		try {
			myadapter = new Quotes_List_adapter(getActivity(), generateData(),
					favValue);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		listView.setAdapter(myadapter);
		listView.setDivider(getResources().getDrawable(R.drawable.transparent));
		View emtpy_view = ((LayoutInflater) getActivity()
				.getApplicationContext().getSystemService(
						Context.LAYOUT_INFLATER_SERVICE)).inflate(
				R.layout.empty_fav_layout, null, true);
		emtpy_view.setVisibility(View.GONE);
		((ViewGroup) listView.getParent()).addView(emtpy_view);
		listView.setEmptyView(emtpy_view);
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