package com.app.zad.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.android.vending.billing.Bill;
import com.app.zad.R;
import com.app.zad.work_in_background.HttpUtility;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import RateUs.RatingDialog;

public class Magic_Activity extends AppCompatActivity {
	private static final String QUOTE__TO_REPORT_FIELD = "entry.1495570162";
	private static final String REASON_FIELD = "entry.2098680843";
	private static final String CATEGORY_SUGGEST_FIELD = "entry.1904974413";
	private static final String QUOTE_TO_EDIT = "entry.705618757";
	private static final String REPORTTURL = "https://docs.google.com/forms/d/1caAn96oLrwhq_vI1_TJ8SmYGkIBlDbbSCxV3cjezFGs/formResponse";
	private static final String SUGGESTURL = "https://docs.google.com/forms/d/1ty5hA2wKy1JW0vUw00r_fH0f5cfFYTa-UZjopQTEVbs/formResponse";
	protected static final String NAME_FIELD = "entry.1361055940";
	protected final String QUOTE_FIELD = "entry.1865861650";
	protected final String AUTHOR_FIELD = "entry.232540";
	protected final String SOURCE_FIELD = "entry.1865650096";
	protected final String CATEGORY_FIELD = "entry.188899649";
	protected final String REQUESTURL = "https://docs.google.com/forms/d/1bGTUD2eROLnto8lxyQx8Z5NKMHGZ1LwKVLXeiEp-CSw/formResponse";

	private DrawerLayout mDrawerLayout;

	List<Quote> allknown;
	List<String> cleaned = new ArrayList<>();
	LinkedHashSet<String> allclean;
	public static List<String> allknowncleaned;
	public static List<String> allunknowncleaned;
	List<Quote> allunkown;
	public static Drawable[] PicsArray;
	public static HashMap<String, Drawable> autortopic = new HashMap<>();
	String[] pics_asssets = null;

	Context mContext;

	private Intent i1;
	private Intent i2;
	private Intent i3;

	RatingDialog rd;
	private Window window;
	private int PublicPos = 1;
	NavigationView navigationView;
    Toolbar toolbar;
    ActionBarDrawerToggle mDrawerToggle;



    @Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);

		setContentView(R.layout.magic_activity);

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nvView);
		setupDrawerContent(navigationView);
        toolbar = (Toolbar) findViewById(R.id.home_toolbar);
        setSupportActionBar(toolbar);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.drawer_open
                , R.string.drawer_close);
        mDrawerLayout.addDrawerListener(mDrawerToggle);

		mContext = this;
		if (PublicPos > 0) {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
				window = getWindow();
				window.setStatusBarColor(ContextCompat.getColor(this, R.color.Purple_Deep_Black));
			}
		}

		try {
			generateAuthorsOics();
		} catch (SQLException | IOException e) {
			e.printStackTrace();
		}


		// Checking if the user is premium or not and save his/her status in a
		// shared prefernece
		Bill bill = new Bill();
		bill.init(mContext);

        setupNavigationFooter();

		open_Zabatly_from_notif();

		rd = new RatingDialog(this);
		// NEW - MELEGY
		sendUnsetRequest();
		// -------------

        if (savedInstanceState == null){
            selectItem(navigationView.getMenu().findItem(R.id.nav_home));
        }

    }

    private void setupNavigationFooter() {
        MenuItem footerItem = navigationView.getMenu().findItem(R.id.nav_footer);
        View footerView = MenuItemCompat.getActionView(footerItem);

        ImageView fbButton = (ImageView) footerView.findViewById(R.id.facebook);
        ImageView TwitButton = (ImageView) footerView.findViewById(R.id.twitter);
        ImageView GplusButton = (ImageView) footerView.findViewById(R.id.googleplus);

        String url1 = "https://www.facebook.com/pages/Zad/1519898524894813";
        String url2 = "https://twitter.com/appZad";
        String url3 = "https://google.com/+appzad";

        i1 = new Intent(Intent.ACTION_VIEW);
        try {
            getPackageManager().getPackageInfo("com.facebook.katana", 0);

            i1.setData(Uri.parse("fb://page/1519898524894813"));

        } catch (Exception e) {
            i1.setData(Uri.parse(url1));

        }

        i2 = new Intent(Intent.ACTION_VIEW);
        i2.setData(Uri.parse(url2));

        i3 = new Intent(Intent.ACTION_VIEW);
        i3.setData(Uri.parse(url3));

        fbButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(i1);
            }
        });
        TwitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(i2);
            }
        });
        GplusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(i3);
            }
        });
    }

    // NEW - MELEGY
	private void sendUnsetRequest() {

		SharedPreferences prefs = getSharedPreferences("MyPref", 0);
		Boolean unsent_new = prefs.getBoolean("unsent_new", false);
		Boolean unsent_report = prefs.getBoolean("unsent_report", false);
		Boolean unsent_suggest = prefs.getBoolean("unsent_suggest", false);

		if (unsent_new) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);

			Map<String, String> params = new HashMap<>();
			SharedPreferences pref = getSharedPreferences("MyPref", 0);
			params.put(QUOTE_FIELD, pref.getString(QUOTE_FIELD, ""));
			params.put(AUTHOR_FIELD, pref.getString(AUTHOR_FIELD, ""));
			params.put(SOURCE_FIELD, pref.getString(SOURCE_FIELD, ""));
			params.put(CATEGORY_FIELD, pref.getString(CATEGORY_FIELD, ""));
			SharedPreferences sharedPreferences = getSharedPreferences(
					"firstlastname", Context.MODE_PRIVATE);
			String first_name = sharedPreferences.getString("firstname", "");
			String last_name = sharedPreferences.getString("secondname", "");
			params.put(NAME_FIELD, first_name + " " + last_name);
			try {
				HttpUtility.sendPostRequest(REQUESTURL, params);
				HttpUtility.readMultipleLinesRespone();
				pref.edit().putBoolean("unsent_new", false).apply();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

		if (unsent_report) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
			Map<String, String> params = new HashMap<>();
			SharedPreferences pref = getSharedPreferences("MyPref", 0);
			params.put(QUOTE__TO_REPORT_FIELD,
					pref.getString(QUOTE__TO_REPORT_FIELD, ""));
			params.put(REASON_FIELD, pref.getString(REASON_FIELD, ""));

			try {
				HttpUtility.sendPostRequest(REPORTTURL, params);
				HttpUtility.readMultipleLinesRespone();
				pref.edit().putBoolean("unsent_report", false).apply();
			} catch (IOException e) {
				e.printStackTrace();
			}

			HttpUtility.disconnect();
		}

		if (unsent_suggest) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);

			Map<String, String> params = new HashMap<>();
			SharedPreferences pref = getSharedPreferences("MyPref", 0);
			params.put(QUOTE_TO_EDIT, pref.getString(QUOTE_TO_EDIT, ""));
			params.put(CATEGORY_SUGGEST_FIELD,
					pref.getString(CATEGORY_SUGGEST_FIELD, ""));
			try {
				HttpUtility.sendPostRequest(SUGGESTURL, params);
				HttpUtility.readMultipleLinesRespone();
				pref.edit().putBoolean("unsent_suggest", false).apply();

			} catch (IOException ex) {
                ex.printStackTrace();
			}
			HttpUtility.disconnect();
		}
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_menu, menu);

		return super.onCreateOptionsMenu(menu);
	}



	@Override
	protected void onStart() {
		super.onStart();

		rd.onStart();
	}

	@Override
	protected void onResume() {
		super.onResume();
		rd.showIfNeeded();
	}

	@Override
	protected void onDestroy() {

		super.onDestroy();

	}
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		switch (item.getItemId()) {

            case R.id.action_search:
			startActivity(new Intent(this, Search_Tabs.class));

			return true;

		case R.id.Settings:
			Intent i2 = new Intent(Magic_Activity.this,
					UserSettingActivity.class);
			startActivity(i2);
			return true;
		case R.id.intro:

			Intent i3 = new Intent(Magic_Activity.this, IntroActivity.class);
			startActivity(i3);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}




	private void setupDrawerContent(NavigationView navigationView) {
		navigationView.setNavigationItemSelectedListener(
				new NavigationView.OnNavigationItemSelectedListener() {
					@Override
					public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
						selectItem(menuItem);
						return true;
					}
				});
        navigationView.setCheckedItem(R.id.nav_home);


	}


	private void selectItem(MenuItem menuItem) {

            switch (menuItem.getItemId()) {

                case R.id.nav_home:
                    Home_Fragment homeFrag = new Home_Fragment();
					FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.content_frame, homeFrag, "home").commit();

                    break;

                case R.id.nav_authors:
                    Authors_Fragment Authors_Frag = new Authors_Fragment();
                    fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.content_frame, Authors_Frag).commit();


                    break;

                case R.id.nav_categories:
                    CategoriesFragment catfragment = new CategoriesFragment();
                    fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.content_frame, catfragment).commit();

                    break;

                case R.id.nav_quotes:
                    Quotes_Fragment Quotes_fragment = new Quotes_Fragment();
                    fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.content_frame, Quotes_fragment).commit();

                    break;
                case R.id.nav_fav:
                    Fav_Quotes_Fragment fav_Quotes_fragment = new Fav_Quotes_Fragment();
                    fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.content_frame, fav_Quotes_fragment).commit();
                    break;
                case R.id.nav_suggest:
                    AddQuote_Fragment add_quote = new AddQuote_Fragment();
                    fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.content_frame, add_quote).commit();
                    break;
                case R.id.nav_premium:

                    Intent billingX = new Intent(getApplicationContext(), Billing.class);
                    startActivity(billingX);
                    break;

            }

		if (PublicPos > 0 && 6 > PublicPos) {

			if (android.os.Build.VERSION.SDK_INT >= 21) {
				window = getWindow();
				window.setStatusBarColor(ContextCompat.getColor(this,R.color.Purple_Deep_Black));
			}
		}

		menuItem.setChecked(true);
		mDrawerLayout.closeDrawers();
	}


	private void open_Zabatly_from_notif() {
		Intent getNotiIntent = getIntent();
		Boolean noti_or_not = getNotiIntent.getBooleanExtra(
				"open_zabtly_or_not_Extras", false);

		if (noti_or_not) {

			SharedPreferences moodPref = getSharedPreferences(
					"open_zabtly_or_not_sharedpreferences",
					Context.MODE_PRIVATE);
			Editor editor = moodPref.edit();

			editor.putBoolean("Zabatly_or_not_key", true);

			editor.apply();

		}

	}

	public HashMap<String, Drawable> generateAuthorsOics() throws SQLException,
			IOException {
		InputStream ins ;
		AssetManager assetManager = getAssets();
		pics_asssets = getAssets().list("ImagesAuthors");
		PicsArray = new Drawable[pics_asssets.length];
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 2;
		for (int i = 0; i < pics_asssets.length; i++) {
			ins = assetManager.open("ImagesAuthors/" + pics_asssets[i]);
			Bitmap originalBitmap = BitmapFactory.decodeStream(ins, null,
					options);
			Drawable d = new BitmapDrawable(getResources(), originalBitmap);
			PicsArray[i] = d;
		}
		Quote quoteInstance = new Quote();
		Quote quoteinstamce2 = new Quote();
		allknown = quoteInstance.getauthors(mContext, true);

		for (int i = 0; i < allknown.size(); i++) {
			cleaned.add(allknown.get(i).Author);
		}
		allclean = new LinkedHashSet<>(cleaned);
		allknowncleaned = new ArrayList<>(allclean);

		cleaned.clear();
		allunkown = quoteinstamce2.getauthors(mContext, false);
		for (int i = 0; i < allunkown.size(); i++) {
			cleaned.add(allunkown.get(i).Author);
		}
		allclean = new LinkedHashSet<>(cleaned);
		allunknowncleaned = new ArrayList<>(allclean);

		for (int i = 0; i < allunknowncleaned.size(); i++) {

			autortopic.put(allunknowncleaned.get(i),
					PicsArray[PicsArray.length - 1]);
		}
		for (int i = 0; i < allknowncleaned.size(); i++) {

			autortopic.put(allknowncleaned.get(i), PicsArray[i]);
		}
		return autortopic;
	}


}
