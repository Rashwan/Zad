package com.app.zad.ui;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.app.zad.R;
import com.app.zad.helper.ItemClickSupport;
import com.app.zad.helper.SpacesItemDecoration;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;

public class Authors_Fragment extends Fragment {

	Drawable[] PicsArray;
	String[] pics_asssets = null;
	ArrayList<String> all;
	AuthorsAdapter adapter ;
	RecyclerView authorsRv;
    private String author_retrieved;
    public static final String AUTHOR_SHARED_ELEMENT_NAME = "com.app.zad.ui.Authors_Fragment";


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
						} catch (IOException ignored) {
						}
				}
			}
		});
		thread.start();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_authors, container, false);
		authorsRv = (RecyclerView) view.findViewById(R.id.authors_rv);

		decodeStream();
        setupRecyclerView();

		return view;
	}
    private void setupRecyclerView(){
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),3);

        authorsRv.setHasFixedSize(true);
        authorsRv.setLayoutManager(gridLayoutManager);
        authorsRv.addItemDecoration(new SpacesItemDecoration(20));
        try {
            adapter = new AuthorsAdapter(getActivity(),generateData());
            authorsRv.setAdapter(adapter);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ItemClickSupport.addTo(authorsRv).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                author_retrieved = adapter.getItem(position).getAuthor_Title();
                ImageView authorImage = (ImageView) v.findViewById(R.id.author_image_iv);

                Intent i1 = new Intent(getActivity(),
                        Authors_list_quotes_notBoring.class);
                i1.putExtra("authorRetrieved", author_retrieved);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    i1.putExtra(AUTHOR_SHARED_ELEMENT_NAME,authorImage.getTransitionName());
                    ActivityOptions activityOptions = ActivityOptions.
                            makeSceneTransitionAnimation(getActivity(), authorImage, authorImage.getTransitionName());
                    startActivity(i1,activityOptions.toBundle());
                }else {
                    startActivity(i1);
                }
            }
        });
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
}