package com.app.zad.work_in_background;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class Alarms_Set_up {
	public static final String KEY_PREF_NOTIFICATION = "notificationPref";
	public static final String KEY_PREF_MORNING_EVENING = "morningEvinengNotificationPref";
	public static final String KEY_PREF_MODES = "modesNotificationPref";

	private Context context;
	AlarmManager alarmManager1;
	PendingIntent pendingIntent1;

	SharedPreferences alarmsSharedPreferences;
	PendingIntent pendingIntent2;

	PendingIntent TodayQuote_pendingIntent;
	AlarmManager TodayQuote_alarmManager;

	PendingIntent RandomQuote_pendingIntent;
	AlarmManager RandomQuote_alarmManager;

	PendingIntent mazag_pendingIntent;
	AlarmManager mazag_alarmManager;

	SharedPreferences Exact_service_from_home;

	public Alarms_Set_up(Context context) {
		super();
		this.context = context;
	}

	public void Alarm_SetUpMorningQuote() {
		if ((getBoolean(KEY_PREF_NOTIFICATION, false))
				&& (getBoolean(KEY_PREF_MORNING_EVENING, false))) {

			alarmsSharedPreferences = context.getSharedPreferences(
					"Exact_Time_Services", Context.MODE_PRIVATE);

			int x = alarmsSharedPreferences.getInt(
					"Exact_Time_Services_MornService_get_hr", 9);

			int y = alarmsSharedPreferences.getInt(
					"Exact_Time_Services_MornService_get_min", 0);

			Calendar cal1 = Calendar.getInstance();
			cal1.setTimeInMillis(System.currentTimeMillis());

			Calendar cal_now = Calendar.getInstance();
			cal_now.setTimeInMillis(System.currentTimeMillis());

			cal1.set(Calendar.HOUR_OF_DAY, x);
			cal1.set(Calendar.MINUTE, y);

			cal1.set(Calendar.SECOND, 0);

			Intent intent1 = new Intent(context, morning_evening_Reciever.class);

			alarmManager1 = (AlarmManager) context
					.getSystemService(Context.ALARM_SERVICE);

			intent1.putExtra(
					"keyToCast",
					"ALarm at " + cal1.get(Calendar.HOUR_OF_DAY) + " : "
							+ cal1.get(Calendar.MINUTE));

			intent1.putExtra("Does_This_right", true);

			pendingIntent1 = PendingIntent.getBroadcast(context, 0, intent1,
					PendingIntent.FLAG_UPDATE_CURRENT);

			int diff = cal_now.compareTo(cal1);

			if (diff == 0) {
				alarmManager1.setRepeating(AlarmManager.RTC_WAKEUP,
						cal1.getTimeInMillis(), AlarmManager.INTERVAL_DAY,
						pendingIntent1);
			}

			else if (diff > 0) {

				int m6f = cal1.get(Calendar.DAY_OF_YEAR) + 1;
				cal1.set(Calendar.DAY_OF_YEAR, m6f);

				alarmManager1.setRepeating(AlarmManager.RTC_WAKEUP,
						cal1.getTimeInMillis(), AlarmManager.INTERVAL_DAY,
						pendingIntent1);

			} else if (diff < 0) {

				alarmManager1.setRepeating(AlarmManager.RTC_WAKEUP,
						cal1.getTimeInMillis(), AlarmManager.INTERVAL_DAY,
						pendingIntent1);

			}

		}
	}

	@SuppressWarnings("unused")
	private int getInt(String string) {
		return 0;
	}

	public void Alarm_SetUpEveningQuote() {
		if ((getBoolean(KEY_PREF_NOTIFICATION, false))
				&& (getBoolean(KEY_PREF_MORNING_EVENING, false))) {
			alarmsSharedPreferences = context.getSharedPreferences(
					"Exact_Time_Services", Context.MODE_PRIVATE);

			int x2 = alarmsSharedPreferences.getInt(
					"Exact_Time_Services_EvenService_get_hr", 21);

			int y2 = alarmsSharedPreferences.getInt(
					"Exact_Time_Services_EvenService_get_min", 0);

			Calendar cal2 = Calendar.getInstance();
			cal2.setTimeInMillis(System.currentTimeMillis());

			cal2.set(Calendar.HOUR_OF_DAY, x2);
			cal2.set(Calendar.MINUTE, y2);

			cal2.set(Calendar.SECOND, 0);

			// Toast.makeText(context, "" + x2, Toast.LENGTH_SHORT).show();
			// Toast.makeText(context, "" + y2, Toast.LENGTH_SHORT).show();

			Intent intent2 = new Intent(context, morning_evening_Reciever.class);
			AlarmManager alarmManager2 = (AlarmManager) context
					.getSystemService(Context.ALARM_SERVICE);

			intent2.putExtra(
					"keyToCast",
					"ALarm at " + cal2.get(Calendar.HOUR_OF_DAY) + " : "
							+ cal2.get(Calendar.MINUTE));

			intent2.putExtra("Does_This_right", true);

			pendingIntent2 = PendingIntent.getBroadcast(context, 1, intent2,
					PendingIntent.FLAG_UPDATE_CURRENT);

			Calendar cal_now2 = Calendar.getInstance();
			cal_now2.setTimeInMillis(System.currentTimeMillis());

			int diff2 = cal_now2.compareTo(cal2);

			if (diff2 == 0) {
				alarmManager2.setRepeating(AlarmManager.RTC_WAKEUP,
						cal2.getTimeInMillis(), AlarmManager.INTERVAL_DAY,
						pendingIntent2);

			} else if (diff2 > 0) {

				int y = cal2.get(Calendar.DAY_OF_YEAR) + 1;
				cal2.set(Calendar.DAY_OF_YEAR, y);

				alarmManager2.setRepeating(AlarmManager.RTC_WAKEUP,
						cal2.getTimeInMillis(), AlarmManager.INTERVAL_DAY,
						pendingIntent2);

			} else if (diff2 < 0) {

				alarmManager2.setRepeating(AlarmManager.RTC_WAKEUP,
						cal2.getTimeInMillis(), AlarmManager.INTERVAL_DAY,
						pendingIntent2);

			}

		}
	}

	public void Alarm_SetUp_TodayQuote() {

		Calendar cal_today = Calendar.getInstance();
		cal_today.setTimeInMillis(System.currentTimeMillis());

		Calendar cal_after_24hrs = Calendar.getInstance();
		cal_after_24hrs.setTimeInMillis(System.currentTimeMillis());

		cal_today.set(Calendar.HOUR_OF_DAY, 0);
		cal_today.set(Calendar.MINUTE, 0);
		cal_today.set(Calendar.SECOND, 0);
		cal_today.set(Calendar.MILLISECOND, 0);

		cal_after_24hrs.add(cal_today.get(Calendar.HOUR_OF_DAY), 24);
		cal_after_24hrs.set(Calendar.MINUTE, 0);
		cal_after_24hrs.set(Calendar.SECOND, 0);
		cal_after_24hrs.set(Calendar.MILLISECOND, 0);

		Exact_service_from_home = context.getSharedPreferences(
				"Exact_Time_Services", Context.MODE_PRIVATE);

		Editor editor2 = Exact_service_from_home.edit();

		editor2.putLong("Exact_Time_Services_TodayQuote_Service_today",
				cal_today.getTimeInMillis());
		editor2.putLong("Exact_Time_Services_TodayQuote_Service_after_24_hrs",
				cal_after_24hrs.getTimeInMillis());

		editor2.commit();

		Intent myIntent = new Intent(context,
				com.app.zad.work_in_background.Today_Quote_Reciever.class);

		TodayQuote_pendingIntent = PendingIntent.getBroadcast(context, 0,
				myIntent, 0);

		TodayQuote_alarmManager = (AlarmManager) context
				.getSystemService(Service.ALARM_SERVICE);

		TodayQuote_alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
				cal_today.getTimeInMillis(), AlarmManager.INTERVAL_DAY,
				TodayQuote_pendingIntent);

	}

	public void Alarm_SetUp_RandomQuote() {

		Calendar cal2 = Calendar.getInstance();
		cal2.setTimeInMillis(System.currentTimeMillis());

		Intent myIntent2 = new Intent(context,
				com.app.zad.work_in_background.Random_Quote_Reciever.class);
		RandomQuote_pendingIntent = PendingIntent.getBroadcast(context, 1,
				myIntent2, PendingIntent.FLAG_CANCEL_CURRENT);

		RandomQuote_alarmManager = (AlarmManager) context
				.getSystemService(Service.ALARM_SERVICE);
		// Every 4 hours
		RandomQuote_alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
				cal2.getTimeInMillis(), 1000 * 60 * 60 * 4,
				RandomQuote_pendingIntent);

	}

	public void Alarm_SetUp_Mood() {

		if ((getBoolean(KEY_PREF_NOTIFICATION, false))
				&& (getBoolean(KEY_PREF_MODES, false))) {
			SharedPreferences mazag_sp = context.getSharedPreferences(
					"mazzag_prefs", Context.MODE_PRIVATE);

			int currentFaceValue2 = mazag_sp.getInt("currentFaceValue2", 0);

			int x = mazag_sp.getInt("Exact_Time_Services_MazagService_get_hr",   // ed-1 create time picker + sh pref
					21);

			int y = mazag_sp.getInt("Exact_Time_Services_MazagService_get_min",  // ed-2 create time picker + sh pref
					0);

			Calendar mazag_cal = Calendar.getInstance();
			mazag_cal.setTimeInMillis(System.currentTimeMillis());

			mazag_cal.set(Calendar.HOUR_OF_DAY, x);
			mazag_cal.set(Calendar.MINUTE, y);
			mazag_cal.set(Calendar.SECOND, 0);
			mazag_cal.set(Calendar.MILLISECOND, 0);

			mazag_alarmManager = (AlarmManager) context
					.getSystemService(Service.ALARM_SERVICE);

			Intent mazag_Intent = new Intent(context,
					mood_Freq_Question_Reciever.class);

			mazag_Intent.putExtra("Does_This_from_face_Button_or_alarmmanager",
					true);

			mazag_Intent.putExtra("How_User_feels", currentFaceValue2);

			mazag_pendingIntent = PendingIntent.getBroadcast(context, 0,
					mazag_Intent, PendingIntent.FLAG_UPDATE_CURRENT);

			Calendar cal_now = Calendar.getInstance();
			cal_now.setTimeInMillis(System.currentTimeMillis());

			int diff = cal_now.compareTo(mazag_cal);

			if (diff == 0) {
				mazag_alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
						mazag_cal.getTimeInMillis(), AlarmManager.INTERVAL_DAY,
						mazag_pendingIntent);

			} else if (diff > 0) {

				int nxt_day = mazag_cal.get(Calendar.DAY_OF_YEAR) + 1;
				mazag_cal.set(Calendar.DAY_OF_YEAR, nxt_day);

				mazag_alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
						mazag_cal.getTimeInMillis(), AlarmManager.INTERVAL_DAY,
						mazag_pendingIntent);

			} else if (diff < 0) {

				mazag_alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
						mazag_cal.getTimeInMillis(), AlarmManager.INTERVAL_DAY,
						mazag_pendingIntent);

			}

		}
	}

	public void Cancel_Morning_Evening_Alarms() {

		Intent intentstop2 = new Intent(context, morning_evening_Reciever.class);
		Intent intentstop3 = new Intent(context, morning_evening_Reciever.class);

		PendingIntent senderstop = PendingIntent.getBroadcast(context, 0,
				intentstop2, 0);

		PendingIntent senderstop2 = PendingIntent.getBroadcast(context, 1,
				intentstop3, 0);

		AlarmManager alarmManagerstop = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		AlarmManager alarmManagerstop2 = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);

		alarmManagerstop.cancel(senderstop);
		alarmManagerstop2.cancel(senderstop2);

		Intent myIntentS = new Intent(context, morning_evening_Service.class);

		context.stopService(myIntentS);

	}

	public void Cancel_Mode_Alarm() {

		Intent intentstop = new Intent(context,
				mood_Freq_Question_Reciever.class);

		PendingIntent senderstop = PendingIntent.getBroadcast(context, 0,
				intentstop, 0);

		AlarmManager alarmManagerstop = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);

		alarmManagerstop.cancel(senderstop);

		Intent cancel_Mode_IntentS = new Intent(context,
				mood_Freq_Question_Service.class);

		context.stopService(cancel_Mode_IntentS);

	}

	public boolean getBoolean(String key, boolean defValue) {
		SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(context);

		return settings.getBoolean(key, defValue);
	}
}