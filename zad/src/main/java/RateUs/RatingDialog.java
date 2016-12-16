package RateUs;

import java.util.Date;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.app.zad.R;

public class RatingDialog {

	public interface ConditionTrigger {
		public boolean shouldShow();
	}

	Context mContext;
	SharedPreferences mPreferences;
	ConditionTrigger mCondition;
	AlertDialog mDialog;

	private static final String PREFS_NAME = "erd_rating";
	private static final String KEY_WAS_RATED = "KEY_WAS_RATED";
	private static final String KEY_NEVER_REMINDER = "KEY_NEVER_REMINDER";
	private static final String KEY_FIRST_HIT_DATE = "KEY_FIRST_HIT_DATE";
	private static final String KEY_LAUNCH_TIMES = "KEY_LAUNCH_TIMES";

	public RatingDialog(Context context) {
		mContext = context;
		mPreferences = context.getSharedPreferences(PREFS_NAME, 0);

	}

	public void onStart() {

		if (didRate() || didNeverReminder())
			return;

		int lauchTimes = mPreferences.getInt(KEY_LAUNCH_TIMES, 0);
		long firstDate = mPreferences.getLong(KEY_FIRST_HIT_DATE, -1L);

		if (firstDate == -1L) {
			registerDate();
		}

		registerHitCount(++lauchTimes);
	}

	public void showIfNeeded() {
		if (mCondition != null) {
			if (mCondition.shouldShow())
				tryShow(mContext);
		} else {
			if (shouldShow())
				tryShow(mContext);
		}
	}

	public void neverReminder() {
		mPreferences.edit().putBoolean(KEY_NEVER_REMINDER, true).commit();
		mDialog.dismiss();
	}

	public void rateNow() {
		String appPackage = mContext.getPackageName();
		Intent intent = new Intent(Intent.ACTION_VIEW,
				Uri.parse("https://play.google.com/store/apps/details?id="
						+ appPackage));
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		mContext.startActivity(intent);
		mPreferences.edit().putBoolean(KEY_WAS_RATED, true).commit();

	}

	public void remindMeLater() {
		registerHitCount(0);
		registerDate();
		mDialog.dismiss();
	}

	public boolean isShowing() {
		return mDialog != null && mDialog.isShowing();
	}

	public boolean didRate() {
		return mPreferences.getBoolean(KEY_WAS_RATED, false);
	}

	public boolean didNeverReminder() {
		return mPreferences.getBoolean(KEY_NEVER_REMINDER, false);
	}

	public void setConditionTrigger(ConditionTrigger condition) {
		mCondition = condition;
	}

	public void setCanceable(boolean canceable) {
		mDialog.setCancelable(canceable);
	}

	private void tryShow(Context context) {
		if (isShowing())
			return;

		try {
			mDialog = null;
			mDialog = createDialogCustom(context);
			mDialog.show();
		} catch (Exception e) {
			// It prevents many Android exceptions
			// when user interactions conflicts with UI thread or Activity
			// expired window token
			// BadTokenException, IllegalStateException ...
		}
	}

	private boolean shouldShow() {

		Log.e(getClass().getSimpleName(), "shouldDo!");

		if (mPreferences.getBoolean(KEY_NEVER_REMINDER, false))
			return false;
		if (mPreferences.getBoolean(KEY_WAS_RATED, false))
			return false;

		int lauchTimes = mPreferences.getInt(KEY_LAUNCH_TIMES, 0);
		long firstDate = mPreferences.getLong(KEY_FIRST_HIT_DATE, 0L);
		long today = new Date().getTime();
		int maxLaunchTimes = mContext.getResources().getInteger(
				R.integer.rd_launch_times);
		int maxDaysAfter = mContext.getResources().getInteger(
				R.integer.rd_max_days_after);

		if (daysBetween(firstDate, today) > maxDaysAfter
				|| lauchTimes > maxLaunchTimes) {

			return true;

		}

		return false;
	}

	private void registerHitCount(int hitCount) {
		mPreferences
				.edit()
				.putInt(KEY_LAUNCH_TIMES, Math.min(hitCount, Integer.MAX_VALUE))
				.commit();
	}

	private void registerDate() {
		Date today = new Date();
		mPreferences.edit().putLong(KEY_FIRST_HIT_DATE, today.getTime())
				.commit();
	}

	private long daysBetween(long firstDate, long lastDate) {
		return (lastDate - firstDate) / (1000 * 60 * 60 * 24);
	}

	private AlertDialog createDialogCustom(Context contextX) {

		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(contextX);
		// ...Irrelevant code for customizing the buttons and title
		LayoutInflater myLayout = LayoutInflater.from(contextX);
		View dialogView = myLayout.inflate(R.layout.rate_us_layout, null);
		dialogBuilder.setView(dialogView);

		Button rate_btn = (Button) dialogView.findViewById(R.id.yes_rate);
		Button no_thanks = (Button) dialogView.findViewById(R.id.no_thanks);
		Button remind_btn = (Button) dialogView
				.findViewById(R.id.remind_me_later);

		rate_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				rateNow();

			}
		});
		no_thanks.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				neverReminder();
			}
		});

		remind_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				remindMeLater();

			}
		});

		dialogBuilder.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				remindMeLater();

			}
		});
		// AlertDialog alertDialog;
		// alertDialog.show();
		return dialogBuilder.create();
	}
}
