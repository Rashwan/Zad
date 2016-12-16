package com.app.zad.widget;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.app.zad.R;

public class ChooseCategory extends Fragment {

	private static final String ARG_KEY = "key";

	public static Map<String, ArrayList<String>> map = new HashMap<String, ArrayList<String>>();
	public static ListView myList;
	public static SharedPreferences sharedpreferences;
	public static final String CATEGORYPREFRENCES = "categoryChoice";
	public static ArrayList<String> selectedCategories = new ArrayList<String>();
	public static final String CATEGORY_FIELD = "Category";
	public final static ArrayList<String> categories = new ArrayList<String>();
	private static ArrayList<Integer> selectedCategoriesIDs = new ArrayList<Integer>();

	private static int cntChoice;

	public static SparseBooleanArray sparseBooleanArray;

	public static ChooseCategory create(String key) {
		Bundle args = new Bundle();
		args.putString(ARG_KEY, key);

		ChooseCategory fragment = new ChooseCategory();
		fragment.setArguments(args);
		return fragment;
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.widget_choose_cat_frag,
				container, false);

		if (!(categories.size() > 0)) {
			categories.add(getActivity().getString(R.string.religious));
			categories.add(getActivity().getString(R.string.politics));
			categories.add(getActivity().getString(R.string.love));
			categories.add(getActivity().getString(R.string.entrepreneurship));
			categories.add(getActivity().getString(R.string.life));
			categories.add(getActivity().getString(R.string.culture));
			categories.add(getActivity().getString(R.string.education));
			categories.add(getActivity().getString(R.string.funny));
			categories.add(getActivity().getString(R.string.evil));
			categories.add(getActivity().getString(R.string.other));
		}
		map.put("categories", categories);
		myList = (ListView) view.findViewById(R.id.List_Categories_Widget);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
				R.layout.zad_check_item, categories);
		myList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		myList.setAdapter(adapter);
		sharedpreferences = getActivity().getSharedPreferences(
				CATEGORYPREFRENCES, Context.MODE_PRIVATE);
		if (sharedpreferences.contains(CATEGORYPREFRENCES)) {
			LoadCategoriesSelections();
		} else {
			int count = myList.getAdapter().getCount();
			for (int i = 0; i < count; i++) {
				myList.setItemChecked(i, true);
			}
		}

		return view;
	}

	public static void submitCategories(Context contex) {
		cntChoice = myList.getCount();
		sparseBooleanArray = myList.getCheckedItemPositions();
		for (int i = 0; i < cntChoice; i++) {
			if (sparseBooleanArray.get(i)) {
				if (i == 0) {
					selectedCategoriesIDs.add(i + 1);
				} else {
					selectedCategoriesIDs.add(i + 2);
				}
				SaveCategoriesSelections(contex);
			}
		}
	}

	public static void SaveCategoriesSelections(Context context) {
		// save the selections in the shared preference in private mode for the
		// user
		SharedPreferences.Editor prefEditor = sharedpreferences.edit();
		String savedItems = getSavedCategories();
		prefEditor.putString(CATEGORYPREFRENCES.toString(), savedItems);
		prefEditor.commit();
		TinyDB tinydb = new TinyDB(context);
		tinydb.putListInt("CategoryChoiceIDS", selectedCategoriesIDs, context);
	}

	public static String getSavedCategories() {
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

	public void LoadCategoriesSelections() {
		// if the selections were previously saved load them
		if (sharedpreferences.contains(CATEGORYPREFRENCES.toString())) {
			String savedItems = sharedpreferences.getString(
					CATEGORYPREFRENCES.toString(), "");
			selectedCategories.addAll(Arrays.asList(savedItems.split(",")));
			int count = myList.getAdapter().getCount();
			for (int i = 0; i < count; i++) {
				String currentItem = (String) myList.getAdapter().getItem(i);
				if (selectedCategories.contains(currentItem)) {
					myList.setItemChecked(i, true);
				} else {
					myList.setItemChecked(i, false);
				}
			}
		}
	}
}
