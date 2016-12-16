package com.app.zad.widget;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StreamCorruptedException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.app.zad.R;
import com.app.zad.helper.GetCroppedBitmap;
import com.app.zad.ui.Authors_list_quotes_notBoring;
import com.app.zad.ui.DatabaseHelper;
import com.app.zad.ui.Quote;
import com.app.zad.ui.Quote_view_pager_activity;

public class Widget extends AppWidgetProvider {
	private ArrayList<Quote> allQuotesObjects;
	public static HashMap<String, Drawable> autortopic = new HashMap<String, Drawable>();
	Context context;
	private Bitmap bitmap;
	Context mContext;
	private DatabaseHelper dbHelper = null;
	private Bitmap bitmap2;
	private Quote quote;
	private int id;
	public static final String AUTHOR_FIELD_NAME = "Author";

	@Override
	public void onEnabled(Context context) {
		super.onEnabled(context);
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);
	}

	@SuppressLint("CommitPrefEdits")
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		try {
			for (int appWidgetId : appWidgetIds) {

				setAlarm(context, appWidgetId,
						ChooseUpdateFrequency.loadFreq(context, appWidgetId));

				RemoteViews views = new RemoteViews(context.getPackageName(),
						R.layout.widget_activityx);
				Intent intent = new Intent(context, Configuration.class);
				intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
						appWidgetId);
				PendingIntent pendingIntent = PendingIntent.getActivity(
						context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
				views.setOnClickPendingIntent(R.id.update_button, pendingIntent);

				TinyDB tinydb = new TinyDB(context);

				Quote quote_instance = new Quote();
				try {
					allQuotesObjects = getFilteredObjects(context,
							tinydb.getListInt("CategoryChoiceIDS", context),
							tinydb.getListInt("AuthorChoiceIDS", context));
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
				}
				String wQuote = quote.Quote;
				views.setTextViewText(R.id.quote_label, wQuote);
				String wAuthor = quote.Author;

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
					 * Uri uri = Uri.withAppendedPath( context.getAssets().open(
					 * "ImagesAuthors/" + pic_id_string + ".jpg");
					 * views.setImageViewUri(R.id.authorPicWidget, uri);
					 */

					views.setImageViewBitmap(R.id.authorPicWidget, bitmap2);
				} catch (Exception exception) {
					InputStream ims = context.getAssets().open(
							"ImagesAuthors/" + "1600" + ".jpg");
					Drawable d = Drawable.createFromStream(ims, null);
					bitmap = ((BitmapDrawable) d).getBitmap();
					bitmap2 = GetCroppedBitmap.getCroppedBitmap(bitmap);
					views.setImageViewBitmap(R.id.authorPicWidget, bitmap2);

				}
				views.setTextViewText(R.id.author_label, wAuthor);
				Intent intent2 = new Intent(context,
						Quote_view_pager_activity.class);

				intent2.putExtra("oneQuote", true);
				intent2.putExtra("quoteRetrived", wQuote);
				intent2.putExtra("authorRetrived", wAuthor);
				intent2.putExtra("wiki", quote.getwiki(context, quote));
				intent2.putExtra("pic", bitmap);
				intent2.putExtra("widget", true);

				PendingIntent contentIntent = PendingIntent.getActivity(
						context, 3, intent2, PendingIntent.FLAG_CANCEL_CURRENT);
				views.setOnClickPendingIntent(R.id.quote_label, contentIntent);
				
				Intent authorIntent = new Intent(context, Authors_list_quotes_notBoring.class);
				authorIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);  // Identifies the particular widget...
				authorIntent.putExtra("authorRetrieved", wAuthor);
				authorIntent.putExtra("Image", bitmap);
				authorIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				authorIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

				// Make the pending intent unique...
				intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
				
				PendingIntent pendIntent = PendingIntent.getActivity(context, 2, authorIntent, PendingIntent.FLAG_UPDATE_CURRENT);				
				views.setOnClickPendingIntent(R.id.authorPicWidget, pendIntent);


			}
			super.onUpdate(context, appWidgetManager, appWidgetIds);
		} catch (Exception exception) {
		//	super.onUpdate(context, appWidgetManager, appWidgetIds);
		}
	}

	public static void setAlarm(Context context, int appWidgetId,
			long updateRate) {
		PendingIntent newPending = makeControlPendingIntent(context,
				WidgetService.UPDATE, appWidgetId);
		AlarmManager alarms = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		if (updateRate >= 0) {
			alarms.setRepeating(AlarmManager.ELAPSED_REALTIME,
					SystemClock.elapsedRealtime(), updateRate, newPending);
		} else {
			alarms.cancel(newPending);
		}
	}

	public static PendingIntent makeControlPendingIntent(Context context,
			String command, int appWidgetId) {
		Intent active = new Intent(context, WidgetService.class);
		active.setAction(command);
		active.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);

		Uri data = Uri
				.withAppendedPath(
						Uri.parse("configwidget://widget/id/#" + command
								+ appWidgetId), String.valueOf(appWidgetId));
		active.setData(data);
		return (PendingIntent.getService(context, 0, active,
				PendingIntent.FLAG_UPDATE_CURRENT));
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

	public DatabaseHelper getHelper(Context mContext) {
		if (dbHelper == null) {
			dbHelper = (DatabaseHelper) OpenHelperManager.getHelper(mContext,
					DatabaseHelper.class);
		}
		return dbHelper;
	}

	public ArrayList<Quote> getFilteredObjects(Context mContext,
			ArrayList<Integer> categoriesid, ArrayList<Integer> authorsId)
			throws SQLException, StreamCorruptedException,
			FileNotFoundException, IOException {
		ArrayList<String> authors = idToAuthor(authorsId);
		RuntimeExceptionDao<Quote, Integer> QuoteDao = null;

		QuoteDao = getHelper(mContext).getQuoteRuntimeExceptionDao();
		List<Quote> alllist = QuoteDao.queryBuilder().where()
				.in("Category", categoriesid).and().in("Author", authors)
				.query();
		ArrayList<Quote> all = new ArrayList<Quote>(alllist);
		return all;

	}

	@Override
	public void onDisabled(Context context) {
		context.stopService(new Intent(context, WidgetService.class));
		super.onDisabled(context);
	}

	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		for (int appWidgetId : appWidgetIds) {
			setAlarm(context, appWidgetId, -1);
		}
		super.onDeleted(context, appWidgetIds);
	}
}