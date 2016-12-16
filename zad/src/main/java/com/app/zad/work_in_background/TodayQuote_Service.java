package com.app.zad.work_in_background;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.IBinder;
import android.preference.PreferenceManager;

import com.app.zad.ui.Quote;

public class TodayQuote_Service extends Service {

	SharedPreferences TodayQuoteSharedPreference;

	private SharedPreferences Exact_service_from_TodayQuoteService;

	public static final String BROADCAST_ACTION = "T_intent";

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		SharedPreferences Hmprefs2 = PreferenceManager
				.getDefaultSharedPreferences(this);
		if (!Hmprefs2.getBoolean("firstTime_TodayQuote", false)) {

			ChangeTheTodayQuote();

			SharedPreferences.Editor editor = Hmprefs2.edit();
			editor.putBoolean("firstTime_TodayQuote", true);
			editor.commit();
		} else {
			CheckTheDay();
		}
		return START_STICKY;

	}

	private void CheckTheDay() {

		TodayQuoteSharedPreference = getSharedPreferences(
				"Exact_Time_Services", MODE_PRIVATE);

		long saved_day = TodayQuoteSharedPreference.getLong(
				"Exact_Time_Services_TodayQuote_Service_today", 0);
		long saved_day_24hrs = TodayQuoteSharedPreference.getLong(
				"Exact_Time_Services_TodayQuote_Service_after_24_hrs", 0);

		Calendar Today_cal = Calendar.getInstance();
		Today_cal.setTimeInMillis(System.currentTimeMillis());

		Today_cal.set(Calendar.HOUR_OF_DAY, 0);
		Today_cal.set(Calendar.MINUTE, 0);
		Today_cal.set(Calendar.SECOND, 0);
		Today_cal.set(Calendar.MILLISECOND, 0);

		if (Today_cal.getTimeInMillis() >= saved_day
				&& Today_cal.getTimeInMillis() <= saved_day_24hrs) {

		} else {
			ChangeTheTodayQuote();

			Calendar cal_after_24hrs = Calendar.getInstance();
			cal_after_24hrs.setTimeInMillis(System.currentTimeMillis());

			cal_after_24hrs.add(Today_cal.get(Calendar.HOUR_OF_DAY), 24);
			cal_after_24hrs.set(Calendar.MINUTE, 0);
			cal_after_24hrs.set(Calendar.SECOND, 0);
			cal_after_24hrs.set(Calendar.MILLISECOND, 0);

			Exact_service_from_TodayQuoteService = getSharedPreferences(
					"Exact_Time_Services", MODE_PRIVATE);

			Editor editor2 = Exact_service_from_TodayQuoteService.edit();

			editor2.putLong("Exact_Time_Services_TodayQuote_Service_today",
					Today_cal.getTimeInMillis());
			editor2.putLong(
					"Exact_Time_Services_TodayQuote_Service_after_24_hrs",
					cal_after_24hrs.getTimeInMillis());

			editor2.commit();

		}

	}

	private void ChangeTheTodayQuote() {

		Intent intent;
		intent = new Intent(BROADCAST_ACTION);

		Quote quoteinstance = new Quote();
		ArrayList<Quote> allQuotesObjects = quoteinstance.getAnObjects(this,
				"Important", 1);

		int min = 0;
		int max = allQuotesObjects.size() - 1;

		Random r = new Random();
		int i = r.nextInt(max - min + 1) + min;

		Quote getRanQuote = allQuotesObjects.get(i);
		String TQuote = getRanQuote.Quote;
		String TAuthor = getRanQuote.Author;
		String Today_wiki = getRanQuote.getwiki(this, getRanQuote);
		int category_retrived = getRanQuote.Category;

		intent.putExtra("Key1", TQuote);
		intent.putExtra("Key2", TAuthor);
		intent.putExtra("Key3", Today_wiki);
		intent.putExtra("Key4", category_retrived);

		sendBroadcast(intent);

		SharedPreferences Sersharedpreferences = getSharedPreferences(
				"MyPREFERENCES", Context.MODE_PRIVATE);
		Editor editor = Sersharedpreferences.edit();

		editor.putString("Skey1", TQuote);
		editor.putString("Skey2", TAuthor);
		editor.putString("Skey3", Today_wiki);
		editor.putInt("Skey4", category_retrived);

		editor.commit();

	}

	@Override
	public void onDestroy() {
		super.onDestroy();

	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
}