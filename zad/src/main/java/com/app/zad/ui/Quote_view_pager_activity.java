package com.app.zad.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ShareActionProvider;

import com.app.zad.R;
import com.app.zad.helper.FlipImageView;
import com.facebook.share.widget.ShareButton;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Quote_view_pager_activity extends AppCompatActivity {

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
    FlipImageView fav_image;
    boolean favcheck;


    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_pager);
		Toolbar toolbar = (Toolbar) findViewById(R.id.quote_toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
        fav_image = (FlipImageView) findViewById(R.id.Fav_button);
        ShareButton fbShareButton = (ShareButton) findViewById(R.id.fb_share_button);
        fbShareButton.setEnabled(true);
        fbShareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareFb();
            }
        });
//
//        ab = getSupportActionBar();

		// KAZAKY FX Yeah
		Window window;

		if (android.os.Build.VERSION.SDK_INT >= 21) {
			window = getWindow();
			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			window.setStatusBarColor(getResources().getColor(R.color.Purple_Deep_Black));
		}


		mContext = getApplicationContext();
		sp = mContext.getSharedPreferences("com.app.zad.fav_id",
				Context.MODE_PRIVATE);
		editor = sp.edit();
		ids = sp.getStringSet("ids", new HashSet<String>());
		idlist = new ArrayList<>(ids);
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
		if ((oneQuote) && ((!widget)) && !notification) {
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

		} else if (widget) {
			quote = sourceIntent.getExtras().getString("quoteRetrived");
			author = sourceIntent.getExtras().getString("authorRetrived");
			Quote thequote = new Quote();
			all = thequote.getAnObjects(mContext, "Quote", quote);
			idfav = all.get(0).ID;
			NUM_PAGES = 1;
			bitmap = (Bitmap) sourceIntent.getParcelableExtra("pic");

		} else if (notification) {
			quote = sourceIntent.getExtras().getString("quoteRetrived");
			author = sourceIntent.getExtras().getString("authorRetrived");
			Quote thequote = new Quote();
			all = thequote.getAnObjects(mContext, "Quote", quote);
			idfav = all.get(0).ID;
			NUM_PAGES = 1;
			bitmap = (Bitmap) sourceIntent.getParcelableExtra("pic");

		} else if (Mazag) {
			pos = sourceIntent.getExtras().getInt("Position") + 1;
			all = Home_Fragment.MazagList;
			NUM_PAGES = all.size();
			runUserTour();
		} else {
			author = sourceIntent.getExtras().getString("authorRetrived");
			pos = sourceIntent.getExtras().getInt("pos") + 1 ;
			quoteInstance = new Quote();
			all = quoteInstance.getAnObjects(mContext, "Author", author);
			bitmap = sourceIntent.getParcelableExtra("pic");
			NUM_PAGES = all.size();
			runUserTour();
		}

		mPager = (ViewPager) findViewById(R.id.pager);
		mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        setFavoriteState();
        mPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                setFavoriteState();
                super.onPageSelected(position);
            }
        });
        mPager.setCurrentItem(pos - 1);

        like();
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





	private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
		public ScreenSlidePagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
            supportInvalidateOptionsMenu();
			if (oneQuote) {

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
	private void shareFb(){
        Intent intent = new Intent(this,Facebook_Share.class);
        Quote_view_fragment selectedFragment = (Quote_view_fragment)
                ((ScreenSlidePagerAdapter) mPager.getAdapter())
                .getItem(mPager.getCurrentItem());

        intent.putExtra("shareQuote",selectedFragment.getCurrentQuote());
        intent.putExtra("shareAuthor", selectedFragment.getCurrentAuthor());
        startActivity(intent);
	}
	private void setFavoriteState(){
        Log.d("BLABLA","CHECK");
        Quote_view_fragment selectedFragment = (Quote_view_fragment)
                ((ScreenSlidePagerAdapter) mPager.getAdapter())
                        .getItem(mPager.getCurrentItem());
        if (isFav(selectedFragment.getCurrentId())) {
            fav_image.setRotationReversed(true);
            fav_image.setChecked(true);
            favcheck = true;
        } else {
            fav_image.setRotationReversed(false);
            fav_image.setChecked(false);
            favcheck = false;
        }
    }
	private void like(){
        fav_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Quote_view_fragment selectedFragment = (Quote_view_fragment)
                        ((ScreenSlidePagerAdapter) mPager.getAdapter())
                                .getItem(mPager.getCurrentItem());
                final Quote quoteinstance1 = new Quote().getAnObjects(v.getContext(),
                        "Quote", selectedFragment.getCurrentQuote()).get(0);
                Integer idfav = quoteinstance1.ID;
                String idfavstring5 = idfav.toString();
                if (favcheck) {
                    fav_image.setRotationReversed(false);
                    fav_image.setChecked(false);
                    ids.remove(idfavstring5);
                    favcheck = false;
                } else {
                    fav_image.setRotationReversed(true);
                    fav_image.setChecked(true);
                    ids.add(idfavstring5);
                    favcheck = true;
                }
                editor.clear();
                editor.putStringSet("ids", ids);
                editor.commit();
            }
        });
    }
    private Boolean isFav(Integer idInteger) {
        String idFavString = idInteger.toString();
        Set<String> allIds = sp.getStringSet("ids", new HashSet<String>());
        ArrayList<String> allIdsList = new ArrayList<>(allIds);
        return allIdsList.contains(idFavString);
    }


}