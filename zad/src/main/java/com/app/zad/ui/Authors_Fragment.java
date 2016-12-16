package com.app.zad.ui;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.app.zad.R;

public class Authors_Fragment extends Fragment {

	Drawable[] PicsArray;
	int numColumns = 1;
	GridView grid;
	public Context mContext;
	String author_retrieved;
	Parcelable GridView_State;
	String[] pics_asssets = null;
	ArrayList<String> all;
	Author_Grid_adapter adapter = null;

	void decodeStream() {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				InputStream ins = null;
				try {
					AssetManager assetManager = getActivity().getAssets();
					pics_asssets = getActivity().getAssets().list(
							"ImagesAuthors");
					PicsArray = new Drawable[pics_asssets.length];
					BitmapFactory.Options options = new BitmapFactory.Options();
					options.inSampleSize = 2;
					for (int i = 0; i < pics_asssets.length; i++) {
						ins = assetManager.open("ImagesAuthors/"
								+ pics_asssets[i]);
						Bitmap originalBitmap = BitmapFactory.decodeStream(ins,
								null, options);
						originalBitmap.recycle();
						Drawable d = new BitmapDrawable(getResources(),
								originalBitmap);
						PicsArray[i] = d;
					}
				} catch (final IOException e) {
					e.printStackTrace();
				} finally {
					if (ins != null)
						try {
							ins.close();
						} catch (IOException e) {
						}
				}

			}
		});
		thread.start();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.grid_view, container, false);
		mContext = getActivity().getApplicationContext();
		// Inflate the layout for this fragment
		// This layout contains your list view

		decodeStream();
		Author_Grid_adapter adapter = null;
		try {
			adapter = new Author_Grid_adapter(getActivity(), generateData());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		grid = (GridView) view.findViewById(R.id.GridViewix);
		adapter.sort(new Comparator<Author_Grid_Item>() {

			public int compare(Author_Grid_Item lhs, Author_Grid_Item rhs) {
				return lhs.getAuthor_Title().compareTo(rhs.getAuthor_Title());
			}
		});

		grid.setAdapter(adapter);
		final Author_Grid_adapter adapter2 = adapter;
		// Restore ListView state
		if (GridView_State != null) {
			Log.d("Quotes_Frag", "trying to restore listview state..");
			grid.onRestoreInstanceState(GridView_State);
		}

		grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			/**
			 * Rashwan Stores the author of the clicked quote into
			 * author_retrieved in order to use to show all the quotes of the
			 * author in the next fragment
			 */
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				author_retrieved = adapter2.getItem(position).getAuthor_Title();

				Intent i1 = new Intent(getActivity(),
						Authors_list_quotes_notBoring.class);
				i1.putExtra("authorRetrieved", author_retrieved);
				startActivity(i1);
			}
		});

		return view;
	}

	/**
	 * Rashwan generating a list of all the authors in the database then links
	 * every author with its picture(the linking is hard coded now but not the
	 * generation).
	 * 
	 * @throws SQLException
	 */
	private ArrayList<Author_Grid_Item> generateData() throws SQLException {
		ArrayList<Author_Grid_Item> items = new ArrayList<Author_Grid_Item>();

		all = new ArrayList<String>(Magic_Activity.allunknowncleaned);
		all.addAll(Magic_Activity.allknowncleaned);
		Log.i("allsize", all.size() + "");
		Log.i("unknownsize", Magic_Activity.allunknowncleaned.size() + "");
		for (int i = 0; i < Magic_Activity.allunknowncleaned.size(); i++) {
			items.add(new Author_Grid_Item(
					Magic_Activity.allunknowncleaned.get(i),
					Magic_Activity.PicsArray[Magic_Activity.PicsArray.length - 1]));
		}
		Log.i("knownsize", Magic_Activity.allknowncleaned.size() + "");
		for (int i = 0; i < Magic_Activity.allknowncleaned.size(); i++) {
			items.add(new Author_Grid_Item(Magic_Activity.allknowncleaned
					.get(i), Magic_Activity.PicsArray[i]));

		}

		return items;
	}

	@Override
	public void onPause() {
		// Save ListView state @ onPause
		Log.d("Quotes_Frag", "saving listview state @ onPause");
		GridView_State = grid.onSaveInstanceState();
		super.onPause();
	}

}