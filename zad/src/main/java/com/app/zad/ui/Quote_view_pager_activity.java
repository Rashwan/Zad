package com.app.zad.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ShareActionProvider;
import android.widget.Toast;

import com.app.zad.R;

public class Quote_view_pager_activity extends FragmentActivity {

	private static int NUM_PAGES = 1;

	@SuppressWarnings("unused")
	private MenuItem ActionFav;
	private String filePath;

	private ViewPager mPager;
	private Quote quoteInstance;
	private ArrayList<Quote> all = new ArrayList<Quote>();
	private boolean oneQuote;
	private boolean favo;
	private String author;
	private String quote;
	private String wiki = null;

	ActionBar ab;

	private PagerAdapter mPagerAdapter;
	public Context mContext;
	private ShareActionProvider myShareActionProvider;
	private MenuInflater myinflater;
	private Menu mymenu;
	SharedPreferences sp;
	SharedPreferences.Editor editor;
	Set<String> ids;
	Integer id_integer;
	ArrayList<Quote> quote_retrieved;
	List<String> idlist;
	Integer idfav;
	Integer idinteger;
	ArrayList<Quote> object_retrieved;
	Integer pos = 1;
	Boolean widget;
	Boolean categories;
	Integer catiInteger;
	Bitmap bitmap;
	Boolean notification;
	Boolean Mazag;
	Integer mazagID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_pager);

		ab = getActionBar();

		// KAZAKY FX Yeah
		Window window;

		if (android.os.Build.VERSION.SDK_INT >= 21) {
			window = getWindow();
			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			window.setStatusBarColor(getResources().getColor(R.color.Purple_Deep_Black));
		}

		ab.hide();
		ImageView view = (ImageView) findViewById(android.R.id.home);
		view.setPadding(10, 0, 0, 0);
		mContext = getApplicationContext();
		sp = mContext.getSharedPreferences("com.app.zad.fav_id",
				Context.MODE_PRIVATE);
		editor = sp.edit();
		ids = sp.getStringSet("ids", new HashSet<String>());
		idlist = new ArrayList<String>(ids);
		Intent sourceIntent = getIntent();
		oneQuote = sourceIntent.getExtras().getBoolean("oneQuote");
		favo = sourceIntent.getExtras().getBoolean("favo");
		wiki = sourceIntent.getExtras().getString("wiki");

		try {
			categories = sourceIntent.getExtras().getBoolean("categories");
		} catch (Exception e) {
			categories = false;
			e.printStackTrace();
		}

		try {
			Mazag = sourceIntent.getExtras().getBoolean("Mazag");
		} catch (Exception e) {
			Mazag = false;
			e.printStackTrace();
		}

		try {

			widget = sourceIntent.getExtras().getBoolean("widget");

		} catch (Exception ex) {
			widget = false;
		}
		try {
			notification = sourceIntent.getExtras().getBoolean("notifi");
		} catch (Exception exception) {
			notification = false;
		}
		if ((oneQuote == true) && ((widget == false)) && notification == false) {
			author = sourceIntent.getExtras().getString("authorRetrived");
			quote = sourceIntent.getExtras().getString("quoteRetrived");
			Quote thequote = new Quote();
			all = thequote.getAnObjects(mContext, "Quote", quote);
			idfav = all.get(0).ID;
			NUM_PAGES = 1;

		} else if (!oneQuote && favo) {
			author = sourceIntent.getExtras().getString("authorRetrived");
			pos = sourceIntent.getExtras().getInt("pos");
			for (int i = 0; i < idlist.size(); i++) {

				idinteger = Integer.parseInt(idlist.get(i));
				Quote quoteInstance = new Quote();
				object_retrieved = quoteInstance.getAnObjects(mContext, "ID",
						idinteger);
				all.add(object_retrieved.get(0));
			}
			NUM_PAGES = all.size();

		} else if (!oneQuote && categories) {
			catiInteger = sourceIntent.getExtras().getInt("catInt");
			pos = sourceIntent.getExtras().getInt("pos");
			quoteInstance = new Quote();
			all = quoteInstance.getAnObjects(mContext, "Category", catiInteger);
			NUM_PAGES = all.size();
			runUserTour();

		} else if (widget == true) {
			quote = sourceIntent.getExtras().getString("quoteRetrived");
			author = sourceIntent.getExtras().getString("authorRetrived");
			Quote thequote = new Quote();
			all = thequote.getAnObjects(mContext, "Quote", quote);
			idfav = all.get(0).ID;
			NUM_PAGES = 1;
			bitmap = (Bitmap) sourceIntent.getParcelableExtra("pic");

		} else if (notification == true) {
			quote = sourceIntent.getExtras().getString("quoteRetrived");
			author = sourceIntent.getExtras().getString("authorRetrived");
			Quote thequote = new Quote();
			all = thequote.getAnObjects(mContext, "Quote", quote);
			idfav = all.get(0).ID;
			NUM_PAGES = 1;
			bitmap = (Bitmap) sourceIntent.getParcelableExtra("pic");

		} else if (Mazag == true) {
			pos = sourceIntent.getExtras().getInt("Position") + 1;
			all = Home_Fragment.MazagList;
			NUM_PAGES = all.size();
			runUserTour();
		} else {
			author = sourceIntent.getExtras().getString("authorRetrived");
			pos = sourceIntent.getExtras().getInt("pos");
			quoteInstance = new Quote();
			all = quoteInstance.getAnObjects(mContext, "Author", author);
			bitmap = sourceIntent.getParcelableExtra("pic");
			NUM_PAGES = all.size();
			runUserTour();
		}

		mPager = (ViewPager) findViewById(R.id.pager);
		mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
		mPager.setAdapter(mPagerAdapter);
		mPager.setCurrentItem(pos - 1);
	}
	public void runUserTour() {

		// Read
		SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
		int nowHere = sharedPref.getInt("here_before", 0);

		if (nowHere <= 1) {
			// show Tour
			Handler handler = new Handler();
			handler.postDelayed(new Runnable() {
				public void run() {
					MoveNext(mPager);

					Handler handler = new Handler();
					handler.postDelayed(new Runnable() {
						public void run() {
							MovePrevious(mPager);
						}
					}, 1000);

				}
			}, 2000);

		}

		nowHere += 1;
		// Edit
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putInt("here_before", nowHere);
		editor.commit();

	}

	public void MoveNext(View view) {
		// it doesn't matter if you're already in the last item
		mPager.setCurrentItem(mPager.getCurrentItem() + 1);

	}

	public void MovePrevious(View view) {
		// it doesn't matter if you're already in the first item
		mPager.setCurrentItem(mPager.getCurrentItem() - 1);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		mymenu = menu;
		myinflater = getMenuInflater();
		myinflater.inflate(R.menu.quote_menu, mymenu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		ActionFav = mymenu.findItem(R.id.action_favourite);

		switch (item.getItemId()) {

		case R.id.action_share:
			/*
			 * Intent shareintent = new Intent(getApplicationContext(),
			 * Facebook_Share.class); startActivity(shareintent);
			 */
			break;

		case R.id.action_favourite:

			if (item.isChecked()) {
				item.setChecked(false);
				Toast.makeText(getApplicationContext(),
						getString(R.string.removed_from_favourites),
						Toast.LENGTH_SHORT).show();

			} else {
				item.setChecked(true);
				Toast.makeText(getApplicationContext(),
						R.string.add_to_favourites, Toast.LENGTH_SHORT).show();
			}
			break;

		}

		return super.onOptionsItemSelected(item);
	}

	private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
		public ScreenSlidePagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			if (oneQuote == true) {

				if (widget || notification) {
					return Quote_view_fragment.newinstance(quote, author, wiki,
							idfav, bitmap, true);

				} else {
					return Quote_view_fragment.newinstance(quote, author, wiki,
							idfav, false);
				}

			} else {
				if (bitmap != null) {
					quote = all.get(position).Quote;
					author = all.get(position).Author;

					idfav = all.get(position).ID;

					Quote thequote = all.get(position);
					wiki = thequote.getwiki(mContext, thequote);

					return Quote_view_fragment.newinstance(quote, author, wiki,
							idfav, bitmap, true);
				} else {
					quote = all.get(position).Quote;
					author = all.get(position).Author;

					idfav = all.get(position).ID;

					Quote thequote = all.get(position);
					wiki = thequote.getwiki(mContext, thequote);

					return Quote_view_fragment.newinstance(quote, author, wiki,
							idfav, false);
				}
			}
		}

		@Override
		public int getCount() {
			return NUM_PAGES;
		}
	}

	public void saveBitmap(Bitmap bitmap) {
		filePath = Environment.getExternalStorageDirectory() + File.separator
				+ "Pictures/quote.png";
		File imagePath = new File(filePath);
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(imagePath);
			bitmap.compress(CompressFormat.PNG, 100, fos);
			fos.flush();
			fos.close();
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		}
	}

	public void SetShareImageIntent(String path) {
		Intent myIntent = new Intent(Intent.ACTION_SEND);
		myIntent.setType("image/png");
		Uri myUri = Uri.parse("file://" + path);
		myIntent.putExtra(Intent.EXTRA_STREAM, myUri);
		myShareActionProvider.setShareIntent(myIntent);

	}

	@SuppressWarnings("unused")
	private void setShareIntent() {
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("plain/text");
		intent.putExtra(Intent.EXTRA_TEXT, author);
		myShareActionProvider.setShareIntent(intent);
	}

	@SuppressWarnings("unused")
	private void shiftHomeView(float offset) {
		try {
			View grandParentView = (View) ((View) findViewById(
					android.R.id.home).getParent()).getParent();

			View upView = ((ViewGroup) ((ViewGroup) grandParentView)
					.getChildAt(0)).getChildAt(0);

			grandParentView.setX(grandParentView.getX() - offset);
			upView.setX(upView.getX() + offset);

			if ((getActionBar().getDisplayOptions() & ActionBar.DISPLAY_SHOW_CUSTOM) != 0)
				getActionBar().getCustomView().setX(
						getActionBar().getCustomView().getX() - offset);
		} catch (Exception e) {
		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

}