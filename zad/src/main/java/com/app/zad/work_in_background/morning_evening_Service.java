package com.app.zad.work_in_background;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.app.zad.R;
import com.app.zad.ui.Quote;
import com.app.zad.ui.Quote_view_pager_activity;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class morning_evening_Service extends Service {

	private static final String NOTIFICATIONS_VIBRATE = "vibreateKey";
	public static final String NOTIFICATIONS_RINGTONE = "ringtoneKey";
	private static final String NOTIFICATIONS_LIGHT = "lightKey";
	public static final String KEY_PREF_NOTIFICATION = "notificationPref";
	public static final String KEY_PREF_MORNING = "morningKey";
	public static final String KEY_PREF_NIGHT = "nightKey";
	public static final String KEY_PREF_MORNING_EVENING = "morningEvinengNotificationPref";
	public static final String KEY_PREF_MODES = "modesNotificationPref";
	public static final Integer NOTIFICATION_TAG = 7;
	private Bitmap bitmap;
	Bitmap Bm = null;

	SharedPreferences mor_Eve_SharedPreferences;
	int Author_Image;
	private boolean from_Where;
	
	static SharedPreferences fav_sp ;
	Editor editor ;
	private boolean favcheck;
	
	

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		 fav_sp = getApplicationContext().getSharedPreferences("com.app.zad.fav_id", Context.MODE_PRIVATE);
		 editor = fav_sp.edit();
		 
		Calendar now_time = Calendar.getInstance();
		now_time.setTimeInMillis(System.currentTimeMillis());

		try {
			from_Where = intent.getExtras()
					.getBoolean("Does_This_right", false);

		} catch (Exception e) {

			from_Where = false;

			e.printStackTrace();
		}
		if (from_Where) {
			startNotifications();
		}
		return START_STICKY;
	}

	public void startNotifications() {

		Quote quoteinstance2 = new Quote();
		ArrayList<Quote> allQuotesObjects2 = quoteinstance2.getAllObjects(this);

		int min2 = 0;
		int max2 = allQuotesObjects2.size() - 1;

		Random r = new Random();
		int i2 = r.nextInt(max2 - min2 + 1) + min2;

		Quote getRanQuote2 = allQuotesObjects2.get(i2);
		Integer quote_id = getRanQuote2.ID;
		String Noti_Quote = getRanQuote2.Quote;
		String Noti_Author = getRanQuote2.Author;
		String Wiki_from_Notif = getRanQuote2.getwiki(this, getRanQuote2);

		Boolean oneQuote = true;
		String quote_retrived = Noti_Quote;
		String author_retrived = Noti_Author;
		int category_retrived = getRanQuote2.Category;

		try {

			int pic_id = getRanQuote2.Author_Image;
			String pic_id_string;
			if (pic_id < 10) {
				pic_id_string = "00" + Integer.toString(pic_id);
			} else if (pic_id < 100) {
				pic_id_string = "0" + Integer.toString(pic_id);
			} else {
				pic_id_string = Integer.toString(pic_id);
			}

			InputStream ims = this.getAssets().open(
					"ImagesAuthors/" + pic_id_string + ".webp");
			Drawable d = Drawable.createFromStream(ims, null);

			bitmap = ((BitmapDrawable) d).getBitmap();

		} catch (Exception e) {

			bitmap = null;
			e.printStackTrace();
		}

		int height = (int) getResources().getDimension(
				android.R.dimen.notification_large_icon_height);
		int width = (int) getResources().getDimension(
				android.R.dimen.notification_large_icon_width);

		Bm = Bitmap.createScaledBitmap(bitmap, width, height, false);

		Intent intent = new Intent(this, Quote_view_pager_activity.class);
		intent.putExtra("oneQuote", oneQuote);
		intent.putExtra("quoteRetrived", quote_retrived);
		intent.putExtra("authorRetrived", author_retrived);
		intent.putExtra("wiki", Wiki_from_Notif);
		intent.putExtra("categoryRetrived", category_retrived);
		intent.putExtra("pic", bitmap);
		intent.putExtra("notifi", true);
		intent.putExtra("favo", false);
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
		// Adds the back stack
		stackBuilder.addParentStack(Quote_view_pager_activity.class);
		// Adds the Intent to the top of the stack
		stackBuilder.addNextIntent(intent);
		// Gets a PendingIntent containing the entire back stack

		// Zad 1.3 --> MELEGY
		PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,
				PendingIntent.FLAG_ONE_SHOT);
		// Zad 1.3 --> MELEGY

		/*
		 * PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
		 * intent, PendingIntent.FLAG_UPDATE_CURRENT);
		 */

		NotificationCompat.Builder builder = new NotificationCompat.Builder(
				this);

		builder.setAutoCancel(true);
		builder.setContentTitle(Noti_Author);
		builder.setContentText(Noti_Quote);
		builder.setSmallIcon(R.drawable.ic_notif);
		builder.setLargeIcon(Bm);
		builder.setStyle(new NotificationCompat.BigTextStyle()
				.bigText(Noti_Quote));
		
		int circleColor = ContextCompat.getColor(this,R.color.Purple_Deep);
		builder.setColor(circleColor);
		// set Action Buttons

		//Share Action
		Intent shIntent = new Intent(this, PingReciever.class);
		shIntent.setAction("Share");
		shIntent.putExtra("Quote", Noti_Quote);
		shIntent.putExtra("Author", Noti_Author);
		shIntent.putExtra("Tag", NOTIFICATION_TAG);
		shIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
		PendingIntent shareIntent = PendingIntent.getBroadcast(this, 0,
				shIntent, PendingIntent.FLAG_ONE_SHOT);

		builder.addAction(R.drawable.ic_action_share, getString(R.string.share_quote), shareIntent); // ed-1 replace ic_launher with share icon

		//Like Action
		favcheck = isFav(quote_id);
		Log.i("ISFAV", favcheck+"");
		Intent LIntent = new Intent(this, PingReciever.class);
		LIntent.setClass(this, PingReciever.class);
		LIntent.setAction("Like");
		LIntent.putExtra("QuoteID", quote_id.toString());
		Log.i("QuoteID From Like", quote_id.toString());
		LIntent.putExtra("IsFav", favcheck);
		LIntent.putExtra("Tag", NOTIFICATION_TAG);
		LIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
		PendingIntent likeIntent = PendingIntent.getBroadcast(this, 0, LIntent,
				PendingIntent.FLAG_ONE_SHOT);
		
		
		
		if (favcheck) {
			builder.addAction(R.drawable.ic_ab_fav_clicked, getString(R.string.add_to_notfaves), likeIntent);   // ed-2 replace ic_launher with like icon
		} else {
			builder.addAction(R.drawable.ic_ab_fav, getString(R.string.add_to_faves), likeIntent);   // ed-2 replace ic_launher with like icon
		}
		
		builder.setContentIntent(resultPendingIntent);
		String ringtone = getString(NOTIFICATIONS_RINGTONE, null);
		if (ringtone != null && ringtone.length() > 0) {
			builder.setSound(Uri.parse(ringtone));
		}

		if (getBoolean(NOTIFICATIONS_VIBRATE, false)) {
			builder.setVibrate(new long[] { 0, 1000 });
		}

		if (getBoolean(NOTIFICATIONS_LIGHT, false)) {
			builder.setLights(0xffffffff, 300, 1000);
		}

		Notification notification = builder.build();
		NotificationManager manager = (NotificationManager) this
				.getSystemService(NOTIFICATION_SERVICE);
		manager.notify(NOTIFICATION_TAG, notification);

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	public boolean getBoolean(String key, boolean defValue) {
		SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		return settings.getBoolean(key, defValue);
	}

	public String getString(String key, String defValue) {
		SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		return settings.getString(key, defValue);
	}
	
	public static Boolean isFav(Integer idInteger) {
		String idfavstring = idInteger.toString();
		Set<String>ids = fav_sp.getStringSet("ids", new HashSet<String>());
		ArrayList<String> idlist = new ArrayList<String>(ids);
		Boolean fav_not = idlist.contains(idfavstring);
		return fav_not;
	}

}