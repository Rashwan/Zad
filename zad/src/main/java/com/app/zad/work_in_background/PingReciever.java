package com.app.zad.work_in_background;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.LabeledIntent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.net.Uri;
import android.widget.Toast;

import com.app.zad.ui.Facebook_Share;

public class PingReciever extends BroadcastReceiver {
	
	String quote_id;
	Boolean is_fav ;
	String quote;
	String author;
	Integer tag;
	SharedPreferences fav_sp;
	Editor editor;
	Set<String>ids;
	Context mcontext;
	
	@Override
	public void onReceive(Context context, Intent intent) {

		mcontext = context;
		tag = intent.getExtras().getInt("Tag");

		if (intent.getAction().equals("Share")) {

			// Share button code:
			
			quote = intent.getExtras().getString("Quote");
			author = intent.getExtras().getString("Author");
			
//			Intent facebook_intent = new Intent(context, Facebook_Share.class);
//			facebook_intent.putExtra("shareQuote", quote);
//			facebook_intent.putExtra("shareAuthor",author);
//			facebook_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			
			NotificationManager manager = (NotificationManager) context.getSystemService(Service.NOTIFICATION_SERVICE);
			manager.cancel(tag);
			context.sendBroadcast(new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
			shareTextUrl();
			//context.startActivity(facebook_intent);
		} else if ((intent.getAction().equals("Like"))) {

			// Like button code:
			
			
			quote_id = intent.getExtras().getString("QuoteID");
			is_fav = intent.getExtras().getBoolean("IsFav");
			
			fav_sp = context.getSharedPreferences("com.app.zad.fav_id", Context.MODE_PRIVATE);
			editor = fav_sp.edit();
			ids = fav_sp.getStringSet("ids", new HashSet<String>());
			
			if (is_fav) {
			
				ids.remove(quote_id);
				Toast.makeText(context,com.app.zad.R.string.removed_from_favourites, Toast.LENGTH_SHORT).show();

			} else {
			
				ids.add(quote_id);
				Toast.makeText(context,com.app.zad.R.string.add_to_favourites, Toast.LENGTH_SHORT).show();

			}
			editor.clear();
			editor.putStringSet("ids", ids);
			editor.commit();
			NotificationManager manager = (NotificationManager) context.getSystemService(Service.NOTIFICATION_SERVICE);
			manager.cancel(tag);
			context.sendBroadcast(new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));

		}
					
	}
	
	private void shareTextUrl() {

		String shareBody = "\"" + quote + "\""
				+ '\n' + "- " + author;

		Resources resources = mcontext.getResources();
		PackageManager pm = mcontext.getPackageManager();
		Intent sendIntent = new Intent(Intent.ACTION_SEND);
		sendIntent.setType("text/plain");

		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");

		List<ResolveInfo> resInfo = pm.queryIntentActivities(intent, 0);
		List<LabeledIntent> intentList = new ArrayList<LabeledIntent>();
		for (int i = 0; i < resInfo.size(); i++) {
			// Extract the label, append it, and repackage it in a LabeledIntent
			ResolveInfo ri = resInfo.get(i);
			String packageName = ri.activityInfo.packageName;

			if (packageName.contains("facebook.katana")) {

			
				intent = new Intent(mcontext, Facebook_Share.class);
				intent.putExtra("shareQuote", quote);
				intent.putExtra("shareAuthor",author);
				intentList.add(new LabeledIntent(intent, packageName,
						"facebook", ri.getIconResource()));

			} else {

				intent.setComponent(new ComponentName(packageName,
						ri.activityInfo.name));
				intent.setAction(Intent.ACTION_SEND);
				// intent.setType("text/plain");
				intent.putExtra(Intent.EXTRA_TEXT, shareBody);
				intentList.add(new LabeledIntent(intent, packageName, ri
						.loadLabel(pm), ri.icon));
			}
			
		}

		
		Intent emailIntent = new Intent(Intent.ACTION_SENDTO,
				Uri.parse("mailto:"));
		emailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Intent chooser = Intent.createChooser(emailIntent,
				resources.getString(com.app.zad.R.string.share_quote)).putExtra(
				Intent.EXTRA_INITIAL_INTENTS,
				intentList.toArray(new LabeledIntent[intentList.size()]));
		chooser.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				
		mcontext.startActivity(chooser);
	}

	}
