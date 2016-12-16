package com.app.zad.widgetPremium;

import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.RemoteViews;

import com.app.zad.R;
import com.app.zad.helper.GetCroppedBitmap;
import com.app.zad.ui.Authors_list_quotes_notBoring;
import com.app.zad.ui.DatabaseHelper;
import com.app.zad.ui.Quote;
import com.app.zad.ui.Quote_view_pager_activity;
import com.j256.ormlite.android.apptools.OpenHelperManager;

public class WidgetService extends Service {
	public static final String UPDATE = "update";
	private int appWidgetId;
	private ArrayList<Quote> allQuotesObjects;
	public static HashMap<String, Drawable> autortopic = new HashMap<String, Drawable>();
	Context mContext;
	private Bitmap bitmap;
	private Context context;
	private DatabaseHelper dbHelper = null;
	private Bitmap bitmap2;
	private int id;
	private Quote quote;
	public static final String AUTHOR_FIELD_NAME = "Author";

	@SuppressLint("CommitPrefEdits")
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Context context = getApplicationContext();
		try {
			if (intent.getExtras() != null) {
				appWidgetId = intent.getExtras().getInt(
						AppWidgetManager.EXTRA_APPWIDGET_ID,
						AppWidgetManager.INVALID_APPWIDGET_ID);
			}
			RemoteViews views = new RemoteViews(context.getPackageName(),
					R.layout.widget_fav);
			AppWidgetManager appWidgetManager = AppWidgetManager
					.getInstance(context);

			Intent configureIntent = new Intent(context, Configuration.class);
			configureIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
					appWidgetId);
			PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
					configureIntent, PendingIntent.FLAG_UPDATE_CURRENT);
			views.setOnClickPendingIntent(R.id.update_button, pendingIntent);
			Quote quote_instance = new Quote();

			try {
				allQuotesObjects = generateData(context);
				Random rand = new Random();
				id = rand.nextInt(allQuotesObjects.size());
				quote = allQuotesObjects.get(id);
				appWidgetManager.updateAppWidget(appWidgetId, views);
				SharedPreferences.Editor editor = PreferenceManager
						.getDefaultSharedPreferences(context).edit();
				editor.putInt("QUOTE_ID", id);

			} catch (Exception e) {
				SharedPreferences settings = PreferenceManager
						.getDefaultSharedPreferences(context);
				id = settings.getInt("QUOTE_ID", (int)(Math.random() * ((allQuotesObjects.size()) + 1)));
				quote = quote_instance.getAnObjects(context, "ID", id).get(0);
				Log.i("WWW", "CATCH");

			}

			String wQuote = quote.Quote;
			views.setTextViewText(R.id.quote_label, wQuote);

			String wAuthor = quote.Author;
			views.setTextViewText(R.id.author_label, wAuthor);
			try {
				int pic_id = quote.Author_Image;
				String pic_id_string;
				if (pic_id < 10) {
					pic_id_string = "00" + Integer.toString(pic_id);
				} else if (pic_id < 100) {
					pic_id_string = "0" + Integer.toString(pic_id);
				} else {
					pic_id_string = Integer.toString(pic_id);
				}

				InputStream ims = context.getAssets().open(
						"ImagesAuthors/" + pic_id_string + ".jpg");
				Drawable d = Drawable.createFromStream(ims, null);
				bitmap = ((BitmapDrawable) d).getBitmap();

				bitmap2 = GetCroppedBitmap.getCroppedBitmap(bitmap);
				/*
				 * Uri uri = Uri.withAppendedPath(
				 * MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI,
				 * "ImagesAuthors/" + pic_id_string + ".jpg");
				 * views.setImageViewUri(R.id.authorPicWidget, uri);
				 */views.setImageViewBitmap(R.id.authorPicWidget, bitmap2);
			} catch (Exception exception) {
				InputStream ims = context.getAssets().open(
						"ImagesAuthors/" + "1600" + ".jpg");
				Drawable d = Drawable.createFromStream(ims, null);
				bitmap = ((BitmapDrawable) d).getBitmap();
				bitmap2 = GetCroppedBitmap.getCroppedBitmap(bitmap);
				views.setImageViewBitmap(R.id.authorPicWidget, bitmap2);

			}
			Intent intent2 = new Intent(context,
					Quote_view_pager_activity.class);

			intent2.putExtra("oneQuote", true);
			intent2.putExtra("quoteRetrived", wQuote);
			intent2.putExtra("authorRetrived", wAuthor);
			intent2.putExtra("wiki", quote.getwiki(context, quote));
			intent2.putExtra("pic", bitmap);
			intent2.putExtra("favo", false);
			intent2.putExtra("widget", true);
			//Rashwan
			

			TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
			// Adds the back stack
			stackBuilder.addParentStack(Quote_view_pager_activity.class);
			// Adds the Intent to the top of the stack
			stackBuilder.addNextIntent(intent2);
			PendingIntent contentIntent =
			        stackBuilder.getPendingIntent(6, PendingIntent.FLAG_CANCEL_CURRENT);
			//Rashwan
			//PendingIntent contentIntent = PendingIntent.getActivity(context, 1,
			//		intent2, PendingIntent.FLAG_UPDATE_CURRENT);

			views.setOnClickPendingIntent(R.id.quote_label, contentIntent);
			
			Intent authorIntent = new Intent(context, Authors_list_quotes_notBoring.class);
			authorIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);  // Identifies the particular widget..
			authorIntent.putExtra("authorRetrieved", wAuthor);
			authorIntent.putExtra("Image", bitmap);
			authorIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			authorIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
			// Make the pending intent unique...
			intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
			
			PendingIntent pendIntent = PendingIntent.getActivity(context, 7, authorIntent, PendingIntent.FLAG_UPDATE_CURRENT);				
			views.setOnClickPendingIntent(R.id.authorPicWidget, pendIntent);
			
			appWidgetManager.updateAppWidget(appWidgetId, views);
		} catch (Exception e) {
			stopSelf();
			return super.onStartCommand(intent, flags, startId);
		}
		stopSelf();
		return super.onStartCommand(intent, flags, startId);
	}

	public DatabaseHelper getHelper(Context mContext) {
		if (dbHelper == null) {
			dbHelper = (DatabaseHelper) OpenHelperManager.getHelper(mContext,
					DatabaseHelper.class);
		}
		return dbHelper;
	}

	public ArrayList<String> idToAuthor(ArrayList<Integer> authorsId) {
		Map<String, ArrayList<String>> map = new HashMap<String, ArrayList<String>>();
		ArrayList<String> authors = null;
		Quote quote = new Quote();
		ArrayList<Quote> allAuthors = quote.getAllObjectsForField(context,
				"AUTHOR");
		final ArrayList<String> authors2 = new ArrayList<String>();

		final int AUTHORS_SIZE = allAuthors.size();
		if (!(authors2.size() > 0)) {
			for (int i = 0; i < AUTHORS_SIZE; i++) {
				authors2.add(allAuthors.get(i).Author);
			}
		}
		map.put("authors", authors2);

		authors = new ArrayList<String>();
		ArrayList<String> mapmod = map.get("authors");
		for (int i = 0; i < authorsId.size(); i++) {
			authors.add(mapmod.get(authorsId.get(i)));
		}
		return authors;
	}

	private ArrayList<Quote> generateData(Context context) throws SQLException {
		SharedPreferences sp;
		Set<String> setId;
		ArrayList<String> idlist;
		int idinteger;
		ArrayList<Quote> object_retrieved;
		try {
			allQuotesObjects = new ArrayList<Quote>();
			sp = context.getSharedPreferences("com.app.zad.fav_id",
					Context.MODE_PRIVATE);
			setId = sp.getStringSet("ids", null);
			idlist = new ArrayList<String>(setId);
			Log.i("WWW", idlist.size() + "idList");
			Log.i("WWW", setId.size() + "setId");

			for (int i = 0; i < setId.size(); i++) {
				Log.i("WWW","IDS");
				idinteger = Integer.parseInt(idlist.get(i));
				Log.i("WWW", "idinteger");
				Quote quoteInstance = new Quote();
				Log.i("WWW", "quoteInstance");
				object_retrieved = quoteInstance.getAnObjects(context, "ID",
						idinteger);
				Log.i("WWW", idinteger+"");
				Log.i("WWW", "A");
				try {
					allQuotesObjects.add(object_retrieved.get(0));
					Log.i("WWW", object_retrieved.toString());
					Log.i("WWW", object_retrieved.get(0).toString());
					Log.i("WWW", object_retrieved.get(0).Author);
					Log.i("WWW", "ADD");				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Log.i("WWW","GEN1");
				}
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
			Log.i("WWW","GEN2");
		}
		return allQuotesObjects;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
}