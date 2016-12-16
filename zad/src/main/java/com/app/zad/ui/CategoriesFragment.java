package com.app.zad.ui;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;

import com.app.zad.R;

public class CategoriesFragment extends Fragment {

	int numColumns = 1;
	GridView grid;
	public Context mContext;
	String author_retrieved;
	String[] pics_asssets = null;
	ArrayList<String> all;
	ArrayList<String> categories = new ArrayList<String>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.categories_fragment, container,
				false);
		mContext = getActivity().getApplicationContext();

		final String[] arrayCat = getResources().getStringArray(
				R.array.categoriesnew);
		ListView lv = (ListView) view.findViewById(R.id.listView1);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
				R.layout.zad_text_purple, arrayCat);
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				Intent i1 = new Intent(getActivity(),
						CategoriesListQuotesNotBoring.class);
				i1.putExtra("categoriesString", arrayCat[position]);
				if (position == 0) {
					i1.putExtra("categoriesNum", position + 1);
				} else {
					i1.putExtra("categoriesNum", position + 2);
				}
				startActivity(i1);
			}
		});

		return view;
	}

}