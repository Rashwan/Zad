package com.app.zad.ui;

import java.sql.SQLException;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Surface;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.app.zad.R;
import com.app.zad.helper.Drawable_into_Bitmap;
import com.app.zad.helper.GetCroppedBitmap;
import com.app.zad.helper.SystemBarTintManager;
import com.app.zad.notboring.ActionBarMagicView;
import com.app.zad.notboring.AlphaForegroundColorSpan;

public class Authors_list_quotes_notBoring extends Activity {
	Parcelable ListView_State;
	private int mActionBarTitleColor;
	private int mActionBarHeight;
	private int mHeaderHeight;
	private int mMinHeaderTranslation;
	private ListView mListView;
	private ActionBarMagicView mHeaderPicture;
	private ImageView mHeaderLogo;
	private View mHeader;
	private View mPlaceHolderView;
	private AccelerateDecelerateInterpolator mSmoothInterpolator;

	private RectF mRect1 = new RectF();
	private RectF mRect2 = new RectF();

	private AlphaForegroundColorSpan mAlphaForegroundColorSpan;
	private SpannableString mSpannableString;

	private TypedValue mTypedValue = new TypedValue();
	Quotes_List_adapter myadapter;
	ArrayList<Quote> allauthorquotes;
	String author_retrived;
	String quote_retrived;
	boolean oneQuote;
	Boolean favo;
	Context mContext;
	Integer pos;
	Drawable author_Image = Magic_Activity.autortopic.get("آرثر سي كلارك");
	private ActionBar ab;
	private TextView author_name;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mSmoothInterpolator = new AccelerateDecelerateInterpolator();
		mHeaderHeight = getResources().getDimensionPixelSize(
				R.dimen.header_height);

		if (android.os.Build.VERSION.SDK_INT >= 19) {

			mMinHeaderTranslation = -mHeaderHeight + getActionBarHeight()
					+ getActionBarHeight();
		} else {
			mMinHeaderTranslation = -mHeaderHeight + getActionBarHeight();

		}
		setContentView(R.layout.authors_not_boring);
		mContext = getApplicationContext();
		ab = getActionBar();

		if (android.os.Build.VERSION.SDK_INT >= 19) {
			setTranslucentStatus(true);
			SystemBarTintManager tintManager = new SystemBarTintManager(this);
			tintManager.setStatusBarTintEnabled(true);
			tintManager.setStatusBarTintResource(R.color.transparent);
		}

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setIcon(
				new ColorDrawable(getResources().getColor(
						android.R.color.transparent)));
		getActionBar().setHomeButtonEnabled(true);

		if (android.os.Build.VERSION.SDK_INT >= 19) {
			ab.setHomeAsUpIndicator(getResources().getDrawable(
					R.drawable.up_drawable_layer));
		}

		mListView = (ListView) findViewById(R.id.listviewMine);

		mHeader = findViewById(R.id.header);
		mHeaderPicture = (ActionBarMagicView) findViewById(R.id.header_bg_picture);
		mHeaderPicture.setResourceIds(R.drawable.image_new6,
				R.drawable.image_new6);
		mHeaderLogo = (ImageView) findViewById(R.id.Author_profilr_pic);
		author_name = (TextView) findViewById(R.id.Author_profilr_Title);

		Intent author_intent = getIntent();
		Quote quoteinstance = new Quote();
		author_retrived = author_intent.getExtras()
				.getString("authorRetrieved");
		Drawable author_Image = quoteinstance.getAuthorImage(author_retrived);
		Intent imageIntent = getIntent();
		final Bitmap bm = imageIntent.getParcelableExtra("Image");

		author_name.setText(author_retrived);
		author_name.setAlpha(255);
		// Restore ListView state
		if (ListView_State != null) {
			Log.d("Quotes_Frag", "trying to restore listview state..");
			mListView.onRestoreInstanceState(ListView_State);
		}

		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent i1 = new Intent(mContext,
						Quote_view_pager_activity.class);
				oneQuote = false;
				favo = false;
				pos = position;
				i1.putExtra("oneQuote", oneQuote);
				i1.putExtra("authorRetrived", author_retrived);
				i1.putExtra("favo", favo);
				i1.putExtra("pos", pos);
				i1.putExtra("authorsfragment", true);
				if (bm != null) {
					i1.putExtra("pic", bm);
				}

				startActivity(i1);

			}
		});
		if (author_Image != null) {
			mHeaderLogo.setImageBitmap(GetCroppedBitmap
					.getCroppedBitmap(Drawable_into_Bitmap
							.drawableToBitmap(author_Image)));
		} else {

			try {

				mHeaderLogo.setImageBitmap(GetCroppedBitmap
						.getCroppedBitmap(bm));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		mActionBarTitleColor = getResources().getColor(R.color.white);

		mSpannableString = new SpannableString(author_retrived);
		mAlphaForegroundColorSpan = new AlphaForegroundColorSpan(
				mActionBarTitleColor);

		setupActionBar();
		setupListView();
	}

	@TargetApi(19)
	private void setTranslucentStatus(boolean on) {
		Window win = getWindow();
		WindowManager.LayoutParams winParams = win.getAttributes();
		final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
		final int bits2 = WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;
		if (on) {
			winParams.flags |= bits;
			winParams.flags |= bits2;
		} else {
			winParams.flags &= ~bits;
			winParams.flags &= ~bits2;
		}
		win.setAttributes(winParams);
	}

	private void setupListView() {

		mPlaceHolderView = getLayoutInflater().inflate(
				R.layout.view_header_placeholder, mListView, false);
		mListView.addHeaderView(mPlaceHolderView);

		mListView
				.setDivider(getResources().getDrawable(R.drawable.transparent));

		mListView.setOnScrollListener(new AbsListView.OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				int scrollY = getScrollY();
				mHeader.setTranslationY(Math.max(-scrollY,
						mMinHeaderTranslation));
				float ratio = clamp(mHeader.getTranslationY()
						/ mMinHeaderTranslation, 0.0f, 1.0f);
				interpolate(mHeaderLogo, getActionBarIconView(),
						mSmoothInterpolator.getInterpolation(ratio));

				// Log.e("Ratio", Float.toString(ratio));
				// Log.e("Clamp", Float.toString(clamp(1000.0F * ratio - 4.0F,
				// 0.0F, 255.0F)));

				author_name.setAlpha(1 / clamp(70.0F * ratio - 4.0F, 0.0F,
						255.0F));

				// author_name.setAlpha(clamp(5.0F * ratio - 4.0F, 0.0F, 1.0F));
				// actionbar title alpha
				// getActionBarTitleView().setAlpha(clamp(5.0F * ratio - 4.0F,
				// 0.0F, 1.0F));
				// ---------------------------------
				// better way thanks to @cyrilmottier
				setTitleAlpha(clamp(5.0F * ratio - 4.0F, 0.0F, 1.0F));
			}
		});
	}

	private ArrayList<Quote> generateData() throws SQLException {
		Quote quoteInstance = new Quote();
		allauthorquotes = quoteInstance.getAnObjects(mContext, "Author",
				author_retrived);
		return allauthorquotes;
	}

	private void setTitleAlpha(float alpha) {
		mAlphaForegroundColorSpan.setAlpha(alpha);
		mSpannableString.setSpan(mAlphaForegroundColorSpan, 0,
				mSpannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		getActionBar().setTitle(mSpannableString);
	}

	public static float clamp(float value, float max, float min) {
		return Math.max(Math.min(value, min), max);
	}

	private void interpolate(View view1, View view2, float interpolation) {
		getOnScreenRect(mRect1, view1);
		getOnScreenRect(mRect2, view2);

		float scaleX = 1.0F + interpolation
				* (mRect2.width() / mRect1.width() - 1.0F);
		float scaleY = 1.0F + interpolation
				* (mRect2.height() / mRect1.height() - 1.0F);
		float translationX = 0.5F * (interpolation * (mRect2.left
				+ mRect2.right - mRect1.left - mRect1.right));
		float translationY = 0.5F * (interpolation * (mRect2.top
				+ mRect2.bottom - mRect1.top - mRect1.bottom));
		view1.setTranslationX(translationX);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

			Display display = ((WindowManager) getApplicationContext()
					.getSystemService(Context.WINDOW_SERVICE))
					.getDefaultDisplay();
			int rotation = display.getRotation();
			if (rotation == Surface.ROTATION_0
					| rotation == Surface.ROTATION_180) {
				view1.setTranslationY(translationY - mHeader.getTranslationY()
						+ 0.5F * getActionBarHeight());
			} else {
				view1.setTranslationY(translationY - mHeader.getTranslationY()
						+ 0.65F * getActionBarHeight());
			}
		} else {
			view1.setTranslationY(translationY - mHeader.getTranslationY());

		}
		view1.setScaleX(scaleX);
		view1.setScaleY(scaleY);
	}

	private RectF getOnScreenRect(RectF rect, View view) {
		rect.set(view.getLeft(), view.getTop(), view.getRight(),
				view.getBottom());
		return rect;
	}

	public int getScrollY() {
		View c = mListView.getChildAt(0);
		if (c == null) {
			return 0;
		}

		int firstVisiblePosition = mListView.getFirstVisiblePosition();
		int top = c.getTop();

		int headerHeight = 0;
		if (firstVisiblePosition >= 1) {
			headerHeight = mPlaceHolderView.getHeight();
		}

		return -top + firstVisiblePosition * c.getHeight() + headerHeight;
	}

	private void setupActionBar() {
		ActionBar actionBar = getActionBar();
		actionBar.setIcon(R.drawable.ic_transparent);

		// getActionBarTitleView().setAlpha(0f);
	}

	private ImageView getActionBarIconView() {
		return (ImageView) findViewById(android.R.id.home);
	}

	/*
	 * private TextView getActionBarTitleView() { int id =
	 * Resources.getSystem().getIdentifier("action_bar_title", "id", "android");
	 * return (TextView) findViewById(id); }
	 */

	public int getActionBarHeight() {
		if (mActionBarHeight != 0) {
			return mActionBarHeight;
		}
		getTheme().resolveAttribute(android.R.attr.actionBarSize, mTypedValue,
				true);
		mActionBarHeight = TypedValue.complexToDimensionPixelSize(
				mTypedValue.data, getResources().getDisplayMetrics());
		return mActionBarHeight;
	}

	@Override
	public void onResume() {

		super.onResume();

		myadapter = null;
		try {
			myadapter = new Quotes_List_adapter(this, generateData(), false);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		mListView.setAdapter(myadapter);
		// Restore ListView state
		if (ListView_State != null) {
			Log.d("Quotes_Frag", "trying to restore listview state..");
			mListView.onRestoreInstanceState(ListView_State);
		}

	}

	@Override
	public boolean onNavigateUp() {

		finish();
		return true;
	}

	@Override
	public void onPause() {
		// Save ListView state @ onPause
		Log.d("Quotes_Frag", "saving listview state @ onPause");
		ListView_State = mListView.onSaveInstanceState();
		super.onPause();
	}

}
