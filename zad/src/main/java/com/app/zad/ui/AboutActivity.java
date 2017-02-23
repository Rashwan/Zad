package com.app.zad.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.app.zad.R;

public class AboutActivity extends AppCompatActivity  {
	ViewPager mViewPager;
	Toolbar toolbar;
    TabLayout tabLayout;
    private AppSectionsPagerAdapter viewPagerAdapter;

    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about_activity);
		toolbar = (Toolbar) findViewById(R.id.about_toolbar);
        tabLayout = (TabLayout) findViewById(R.id.about_tabs);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        setSupportActionBar(toolbar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.Purple_Deep_Black));
		}
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);


		viewPagerAdapter = new AppSectionsPagerAdapter(this,getSupportFragmentManager());
		mViewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(mViewPager);
	}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private static class AppSectionsPagerAdapter extends FragmentPagerAdapter {
        private Context context;
		public AppSectionsPagerAdapter(Context context,FragmentManager fm) {
            super(fm);
            this.context = context;
		}

		@Override
		public Fragment getItem(int i) {
			switch (i) {
			case 0:
				return new LaunchpadSectionFragment();

			case 1:
				Fragment fragment = new DummySectionFragment();
				Bundle args = new Bundle();
				args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, i + 1);
				fragment.setArguments(args);
				return fragment;
			}
			return null;
		}

		@Override
		public int getCount() {
			return 2;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			switch (position) {
			case 0:
				return context.getString(R.string.who_we_are);
			case 1:
				return context.getString(R.string.thanks);
			}
			return "";
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
			textTwitter.setText(R.string.zad_twitter_handle);
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
			meego.setImageDrawable(ContextCompat.getDrawable(getActivity(),R.drawable.ic_zad));
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
			ArrayAdapter<String> adapter = new ArrayAdapter<>(
					getActivity(), R.layout.zad_text_purple, arrayCat);
			lv.setAdapter(adapter);

			return rootView;
		}

	}

}
