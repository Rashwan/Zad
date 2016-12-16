package com.app.zad.work_in_background;

import java.util.ArrayList;
import java.util.Random;

import com.app.zad.ui.Quote;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.IBinder;

public class Random_Quote_Service extends Service {

	public static final String Second_BROADCAST_ACTION = "R_intent";

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent2, int flags, int startId) {

		intent2 = new Intent(Second_BROADCAST_ACTION);

		Quote quoteinstance2 = new Quote();
		ArrayList<Quote> allQuotesObjects2 = quoteinstance2.getAllObjects(this);

		int min2 = 0;
		int max2 = allQuotesObjects2.size() - 1;

		Random r = new Random();
		int i2 = r.nextInt(max2 - min2 + 1) + min2;

		Quote getRanQuote2 = allQuotesObjects2.get(i2);
		String RQuote = getRanQuote2.Quote;
		String RAuthor = getRanQuote2.Author;
		String Rwiki = getRanQuote2.getwiki(this, getRanQuote2);
		int category_retrived = getRanQuote2.Category;

		intent2.putExtra("Sec_Key1", RQuote);
		intent2.putExtra("Sec_Key2", RAuthor);
		intent2.putExtra("Sec_Key3", Rwiki);
		intent2.putExtra("Sec_Key4", category_retrived);

		sendBroadcast(intent2);

		SharedPreferences Sersharedpreferences2 = getSharedPreferences(
				"MyPREFERENCES2", Context.MODE_PRIVATE);
		Editor editor = Sersharedpreferences2.edit();

		editor.putString("Skey1_2", RQuote);
		editor.putString("Skey2_2", RAuthor);
		editor.putString("Skey3_2", Rwiki);
		editor.putInt("Skey4_2", category_retrived);

		editor.commit();

		return START_STICKY;
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
