package com.app.zad.ui;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.app.zad.R;
import com.larvalabs.svgandroid.SVG;
import com.larvalabs.svgandroid.SVGParser;

@SuppressWarnings("deprecation")
public class AboutActivity extends FragmentActivity implements
		ActionBar.TabListener {

	AppSectionsPagerAdapter mAppSectionsPagerAdapter;

	ViewPager mViewPager;

	private ActionBar actionBar;
	static CharSequence who_we_are;
	static CharSequence thanks;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about_activity);

		// Rashwan String For Translation
		who_we_are = getString(R.string.who_we_are);
		thanks = getString(R.string.thanks);

		// KAZAKY FX Yeah
		actionBar = getActionBar();

		Window window;

		if (android.os.Build.VERSION.SDK_INT >= 21) {
			window = getWindow();
			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			window.setStatusBarColor(getResources().getColor(R.color.Purple_Deep_Black));
		}
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setIcon(
				new ColorDrawable(getResources().getColor(
						android.R.color.transparent)));
		getActionBar().setHomeButtonEnabled(true);
		if (android.os.Build.VERSION.SDK_INT >= 18) {
			actionBar.setHomeAsUpIndicator(getResources().getDrawable(
					R.drawable.up_drawable_layer));
		}
		// KAZAKY FX Yeah

		mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(
				getSupportFragmentManager());

		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mAppSectionsPagerAdapter);
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {

						actionBar.setSelectedNavigationItem(position);
					}
				});

		for (int i = 0; i < mAppSectionsPagerAdapter.getCount(); i++) {
			actionBar.addTab(actionBar.newTab()
					.setText(mAppSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.empty_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (item.getItemId() == android.R.id.home) {
			onBackPressed();
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	public static class AppSectionsPagerAdapter extends FragmentPagerAdapter {

		public AppSectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int i) {
			switch (i) {
			case 0:
				return new LaunchpadSectionFragment();

			default:
				Fragment fragment = new DummySectionFragment();
				Bundle args = new Bundle();
				args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, i + 1);
				fragment.setArguments(args);
				return fragment;
			}
		}

		@Override
		public int getCount() {
			return 2;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			switch (position) {
			case 0:
				return who_we_are;
			case 1:
				return thanks;
			default:
				break;
			}
			return null;
		}
	}

	public static class LaunchpadSectionFragment extends Fragment implements
			OnClickListener {

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.about_fragment,
					container, false);
			TextView textTwitter = (TextView) rootView
					.findViewById(R.id.TextViewTitter);
			textTwitter.setText("@appZad");
			ImageView WebsiteBtn = (ImageView) rootView
					.findViewById(R.id.website_Button);
			ImageView Email = (ImageView) rootView
					.findViewById(R.id.about_email);
			ImageView facebook = (ImageView) rootView
					.findViewById(R.id.about_facbook);
			ImageView Twitter = (ImageView) rootView
					.findViewById(R.id.about_twitter);
			ImageView GPlus = (ImageView) rootView
					.findViewById(R.id.about_googleplus);
			ImageView meego = (ImageView) rootView
					.findViewById(R.id.imageViewLogo);
			SVG svg = SVGParser.getSVGFromResource(getResources(), R.raw.zad);
			meego.setImageDrawable(svg.createPictureDrawable());
			meego.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

			WebsiteBtn.setOnClickListener(this);
			Email.setOnClickListener(this);
			facebook.setOnClickListener(this);
			Twitter.setOnClickListener(this);
			GPlus.setOnClickListener(this);

			return rootView;
		}

		public void onClick(View v) {
			Intent browserIntent = new Intent(Intent.ACTION_VIEW);
			String url;
			switch (v.getId()) {
			case R.id.website_Button:
				url = "http://appZad.com";
				browserIntent.setData(Uri.parse(url));
				startActivity(browserIntent);
				break;
			case R.id.about_email:
				Intent emailIntent = new Intent(Intent.ACTION_SENDTO,
						Uri.fromParts("mailto", "contact@appzad.com", null));
				emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Inquiry");
				startActivity(Intent.createChooser(emailIntent,
						"Send mail to Zad"));

				break;
			case R.id.about_facbook:
				String url1 = "https://www.facebook.com/pages/Zad/1519898524894813";

				Intent i1 = new Intent(Intent.ACTION_VIEW);
				try {
					getActivity().getPackageManager().getPackageInfo(
							"com.facebook.katana", 0);

					i1.setData(Uri.parse("fb://page/1519898524894813"));
				} catch (Exception e) {
					i1.setData(Uri.parse(url1));
				}
				startActivity(i1);

				break;
			case R.id.about_twitter:
				url = "https://twitter.com/appZad";
				browserIntent.setData(Uri.parse(url));
				startActivity(browserIntent);

				break;
			case R.id.about_googleplus:
				browserIntent = new Intent(Intent.ACTION_VIEW,
						Uri.parse("http://gplus.to/appZad"));
				startActivity(browserIntent);

				break;

			default:
				break;
			}

		}

	}

	public static class DummySectionFragment extends Fragment {
		public static final String ARG_SECTION_NUMBER = "section_number";

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.thanks_list_layout,
					container, false);

			final String[] arrayCat = getResources().getStringArray(
					R.array.Thanks_List_Names);
			ListView lv = (ListView) rootView.findViewById(R.id.listView1);
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(
					getActivity(), R.layout.zad_text_purple, arrayCat);
			lv.setAdapter(adapter);

			return rootView;
		}

	}

}
