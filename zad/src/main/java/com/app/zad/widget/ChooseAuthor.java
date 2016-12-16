package com.app.zad.widget;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.app.zad.R;
import com.app.zad.ui.Quote;

public class ChooseAuthor extends Fragment {
	private static final String ARG_KEY = "key";
	public static Map<String, ArrayList<String>> map = new HashMap<String, ArrayList<String>>();
	public static ListView myList;
	public static SharedPreferences sharedpreferences;
	public static final String AUTHORPREFRENCES = "authorChoice";
	public ArrayList<String> selectedItems = new ArrayList<String>();
	public static final String AUTHOR_FIELD = "Author";
	final static ArrayList<String> authors = new ArrayList<String>();
	private static ArrayList<Integer> selectedAuthorsIDs = new ArrayList<Integer>();
	private static int cntChoice;
	private static SparseBooleanArray sparseBooleanArray;
	public static TinyDB tinydb;

	public static ChooseAuthor create(String key) {
		Bundle args = new Bundle();
		args.putString(ARG_KEY, key);

		ChooseAuthor fragment = new ChooseAuthor();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.widget_choose_authors_frag,
				container, false);
		final Context context = getActivity().getApplicationContext();

		final Quote quote = new Quote();
		final ArrayList<Quote> allAuthors = quote.getAllObjectsForField(
				context, AUTHOR_FIELD);
		final int AUTHORS_SIZE = allAuthors.size();
		if (!(authors.size() > 0)) {
			for (int i = 0; i < AUTHORS_SIZE; i++) {
				authors.add(allAuthors.get(i).Author);
			}
		}
		map.put("authors", authors);

		myList = (ListView) view.findViewById(R.id.List_Authors_Widget);
		final ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				getActivity(), R.layout.zad_check_item, authors);
		myList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		myList.setAdapter(adapter);

		EditText inputSearch = new EditText(context);
		inputSearch = (EditText) view.findViewById(R.id.Search_Authors);
		inputSearch.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				adapter.getFilter().filter(s);

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});

		sharedpreferences = getActivity().getSharedPreferences(
				AUTHORPREFRENCES, Context.MODE_PRIVATE);
		if (sharedpreferences.contains(AUTHORPREFRENCES)) {
			LoadSelections();
		} else {
			int count = myList.getAdapter().getCount();
			for (int i = 0; i < count; i++) {
				myList.setItemChecked(i, true);
			}
		}
		Button deselectAll = (Button) view.findViewById(R.id.Cancel_select);
		deselectAll.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				int count = myList.getAdapter().getCount();
				for (int i = 0; i < count; i++) {
					myList.setItemChecked(i, false);
				}

			}
		});

		Button seslectAll = (Button) view.findViewById(R.id.select_all);
		seslectAll.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				int count = myList.getAdapter().getCount();
				for (int i = 0; i < count; i++) {
					myList.setItemChecked(i, true);
				}

			}
		});
		return view;
	}

	public static void submitAuthors(Context context) {
		cntChoice = myList.getCount();
		sparseBooleanArray = myList.getCheckedItemPositions();
		for (int i = 0; i < cntChoice; i++) {
			if (sparseBooleanArray.get(i)) {
				selectedAuthorsIDs.add(i);
				SaveSelections(context);
			}
		}

	}

	public static void SaveSelections(Context context) {
		SharedPreferences.Editor prefEditor = sharedpreferences.edit();
		String savedItems = getSavedItems();
		prefEditor.putString(AUTHORPREFRENCES.toString(), savedItems);
		prefEditor.commit();
		tinydb = new TinyDB(context);
		tinydb.putListInt("AuthorChoiceIDS", selectedAuthorsIDs, context);
	}

	public static String getSavedItems() {
		String savedItems = "";
		int count = myList.getAdapter().getCount();
		for (int i = 0; i < count; i++) {
			if (myList.isItemChecked(i)) {
				if (savedItems.length() > 0) {
					savedItems += "," + myList.getItemAtPosition(i);
				} else {
					savedItems += myList.getItemAtPosition(i);
				}
			}
		}
		return savedItems;
	}

	public void LoadSelections() {
		// if the selections were previously saved load them
		if (sharedpreferences.contains(AUTHORPREFRENCES.toString())) {
			String savedItems = sharedpreferences.getString(
					AUTHORPREFRENCES.toString(), "");
			selectedItems.addAll(Arrays.asList(savedItems.split(",")));
			int count = myList.getAdapter().getCount();
			for (int i = 0; i < count; i++) {
				String currentItem = (String) myList.getAdapter().getItem(i);
				if (selectedItems.contains(currentItem)) {
					myList.setItemChecked(i, true);
				} else {
					myList.setItemChecked(i, false);
				}
			}
		}
	}

}