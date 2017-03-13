package com.app.zad.widgetPremium;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.app.zad.R;
import com.app.zad.help.AbstractWizardModel;
import com.app.zad.help.ModelCallbacks;
import com.app.zad.help.Page;
import com.app.zad.helpUI.PageFragmentCallbacks;
import com.app.zad.helpUI.ReviewFragment;
import com.app.zad.helpUI.StepPagerStrip;
import com.app.zad.ui.Billing;
import com.app.zad.ui.Quote;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Configuration extends FragmentActivity implements
		PageFragmentCallbacks, ReviewFragment.Callbacks, ModelCallbacks {
	private ViewPager mPager;
	private MyPagerAdapter mPagerAdapter;

	private boolean mEditingAfterReview;

	private AbstractWizardModel mWizardModel = new ConfigurationWizard(this);

	private boolean mConsumePageSelectedEvent;

	private Button mNextButton;
	private Button mPrevButton;

	private List<Page> mCurrentPageSequence;
	private StepPagerStrip mStepPagerStrip;

	private int appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
	final Context context = Configuration.this;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		final SharedPreferences iapPrefrence = this.getSharedPreferences(
				getString(R.string.IAPPreference), Context.MODE_PRIVATE);

		// Get Premium Value
		boolean isPremium = iapPrefrence.getBoolean(
				getString(R.string.isPremium), false);
		if (isPremium == true) {
			if (favourited(context)) {
				Intent launchIntent = getIntent();
				Bundle extras = launchIntent.getExtras();
				if (extras != null) {
					appWidgetId = extras.getInt(
							AppWidgetManager.EXTRA_APPWIDGET_ID,
							AppWidgetManager.INVALID_APPWIDGET_ID);
				}

				setContentView(R.layout.widget_setup_fav);

				if (savedInstanceState != null) {
					mWizardModel.load(savedInstanceState.getBundle("model"));
				}

				mWizardModel.registerListener(this);

				mPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
				mPager = (ViewPager) findViewById(R.id.pager);
				mPager.setAdapter(mPagerAdapter);
				mStepPagerStrip = (StepPagerStrip) findViewById(R.id.strip);
				mStepPagerStrip
						.setOnPageSelectedListener(new StepPagerStrip.OnPageSelectedListener() {
							@Override
							public void onPageStripSelected(int position) {
								position = Math.min(
										mPagerAdapter.getCount() - 1, position);
								if (mPager.getCurrentItem() != position) {
									mPager.setCurrentItem(position);
								}
							}
						});

				mNextButton = (Button) findViewById(R.id.next_button);
				mPrevButton = (Button) findViewById(R.id.prev_button);

				mPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						mStepPagerStrip.setCurrentPage(position);

						if (mConsumePageSelectedEvent) {
							mConsumePageSelectedEvent = false;
							return;
						}

						mEditingAfterReview = false;
						updateBottomBar();
					}
				});

				mNextButton.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						if (mPager.getCurrentItem() == mCurrentPageSequence
								.size() - 1) {
							if (Integer
									.parseInt(ChooseUpdateFrequency.freqEditText
											.getText().toString()) >= ChooseUpdateFrequency.DEFAULTE_VALUE
									/ ChooseUpdateFrequency.MINIUTE_VALUE) {
								ChooseUpdateFrequency.saveFreq(
										context,
										appWidgetId,
										Integer.parseInt(ChooseUpdateFrequency.freqEditText
												.getText().toString()));

								AppWidgetManager appWidgetManager = AppWidgetManager
										.getInstance(context);

								ComponentName thisAppWidget = new ComponentName(
										context.getPackageName(), Widget.class
												.getName());

								Intent updateIntent = new Intent(context,
										Widget.class);

								int[] appWidgetIds = appWidgetManager
										.getAppWidgetIds(thisAppWidget);

								updateIntent
										.setAction("android.appwidget.action.APPWIDGET_UPDATE");

								updateIntent.putExtra(
										AppWidgetManager.EXTRA_APPWIDGET_IDS,
										appWidgetIds);

								context.sendBroadcast(updateIntent);

								setResult(RESULT_OK, new Intent().putExtra(
										AppWidgetManager.EXTRA_APPWIDGET_ID,
										appWidgetId));
								finish();
							} else {
								ChooseUpdateFrequency.freqEditText.setText("");
							}

						}

						if (mEditingAfterReview) {
							mPager.setCurrentItem(mPagerAdapter.getCount() - 1);
						} else {
							mPager.setCurrentItem(mPager.getCurrentItem() + 1);
						}

					}
				});

				mPrevButton.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						mPager.setCurrentItem(mPager.getCurrentItem() - 1);
					}
				});

				onPageTreeChanged();
				updateBottomBar();
			} else {
				Toast.makeText(
						Configuration.this,
						R.string.must_fav_for_widget,
						Toast.LENGTH_LONG).show();
				finish();
			}
		} else {
			Toast.makeText(
					Configuration.this,
					R.string.must_buy_for_widget,
					Toast.LENGTH_LONG).show();
			Intent iBilling = new Intent(getApplicationContext(), Billing.class);
			startActivity(iBilling);
			finish();
		}
	}

	@Override
	public void onPageTreeChanged() {
		mCurrentPageSequence = mWizardModel.getCurrentPageSequence();
		recalculateCutOffPage();
		mStepPagerStrip.setPageCount(mCurrentPageSequence.size()); // + 1 =
																	// review
																	// step
		mPagerAdapter.notifyDataSetChanged();
		updateBottomBar();
	}

	private void updateBottomBar() {
		int position = mPager.getCurrentItem();
		if (position == mCurrentPageSequence.size() - 1) {
			mNextButton.setText(R.string.finishwidget);
			mNextButton.setBackgroundResource(R.drawable.finish_background);
			mNextButton.setTextAppearance(this, R.style.TextAppearanceFinish);
		} else {
			mNextButton.setText(mEditingAfterReview ? R.string.review
					: R.string.next);
			mNextButton
					.setBackgroundResource(R.drawable.action_bar_item_drawable);
			TypedValue v = new TypedValue();
			getTheme().resolveAttribute(android.R.attr.textAppearanceMedium, v,
					true);
			mNextButton.setTextAppearance(this, v.resourceId);
			mNextButton.setEnabled(position != mPagerAdapter.getCutOffPage());
		}

		mPrevButton
				.setVisibility(position <= 0 ? View.INVISIBLE : View.VISIBLE);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mWizardModel.unregisterListener(this);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putBundle("model", mWizardModel.save());
	}

	@Override
	public AbstractWizardModel onGetModel() {
		return mWizardModel;
	}

	@Override
	public void onEditScreenAfterReview(String key) {
		for (int i = mCurrentPageSequence.size() - 1; i >= 0; i--) {
			if (mCurrentPageSequence.get(i).getKey().equals(key)) {
				mConsumePageSelectedEvent = true;
				mEditingAfterReview = true;
				mPager.setCurrentItem(i);
				updateBottomBar();
				break;
			}
		}
	}

	@Override
	public void onPageDataChanged(Page page) {
		if (page.isRequired()) {
			if (recalculateCutOffPage()) {
				mPagerAdapter.notifyDataSetChanged();
				updateBottomBar();
			}
		}
	}

	@Override
	public Page onGetPage(String key) {
		return mWizardModel.findByKey(key);
	}

	private boolean recalculateCutOffPage() {
		// Cut off the pager adapter at first required page that isn't completed
		int cutOffPage = mCurrentPageSequence.size() + 1;
		for (int i = 0; i < mCurrentPageSequence.size(); i++) {
			Page page = mCurrentPageSequence.get(i);
			if (page.isRequired() && !page.isCompleted()) {
				cutOffPage = i;
				break;
			}
		}

		if (mPagerAdapter.getCutOffPage() != cutOffPage) {
			mPagerAdapter.setCutOffPage(cutOffPage);
			return true;
		}

		return false;
	}

	public class MyPagerAdapter extends FragmentStatePagerAdapter {
		private int mCutOffPage;
		private Fragment mPrimaryItem;

		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int i) {
			if (i >= mCurrentPageSequence.size()) {
				return new ReviewFragment();
			}

			return mCurrentPageSequence.get(i).createFragment();
		}

		@Override
		public int getItemPosition(Object object) {
			// TODO: be smarter about this
			if (object == mPrimaryItem) {
				// Re-use the current fragment (its position never changes)
				return POSITION_UNCHANGED;
			}

			return POSITION_NONE;
		}

		@Override
		public void setPrimaryItem(ViewGroup container, int position,
				Object object) {
			super.setPrimaryItem(container, position, object);
			mPrimaryItem = (Fragment) object;
		}

		@Override
		public int getCount() {
			if (mCurrentPageSequence == null) {
				return 0;
			}
			return Math.min(mCutOffPage + 1, mCurrentPageSequence.size());
		}

		public void setCutOffPage(int cutOffPage) {
			if (cutOffPage < 0) {
				cutOffPage = Integer.MAX_VALUE;
			}
			mCutOffPage = cutOffPage;
		}

		public int getCutOffPage() {
			return mCutOffPage;
		}
	}

	private Boolean favourited(Context context) {
		SharedPreferences sp;
		Set<String> setId;
		ArrayList<Quote> allQuotesObjects;
		allQuotesObjects = new ArrayList<Quote>();
		sp = context.getSharedPreferences("com.app.zad.fav_id",
				Context.MODE_PRIVATE);
		setId = sp.getStringSet("ids", null);
		if (setId == null || setId.size() == 0) {
			return false;
		} else if (setId.size() > 0) {
			return true;
		} else
			return false;
	}

}