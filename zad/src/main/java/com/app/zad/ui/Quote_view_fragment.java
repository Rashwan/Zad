package com.app.zad.ui;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.LabeledIntent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.zad.R;
import com.app.zad.helper.Drawable_into_Bitmap;
import com.app.zad.helper.GetCroppedBitmap;
import com.app.zad.work_in_background.HttpUtility;
import com.facebook.CallbackManager;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Quote_view_fragment extends Fragment {

    private static final String TAG = "QUOTEVIEWFRAGMENT";
    static SharedPreferences sp;
	SharedPreferences.Editor editor;
	 Set<String> ids;
	 ArrayList<String> idlist;
	 Integer idintbun;
	Quote quote2 = new Quote();
	Drawable author_image = Magic_Activity.autortopic.get("الأصمعي");
	String[] arrayCat;
	Drawable d = null;
	AdView adView;
	ImageView RemoveAdsBtn;
	private static final String QUOTE__TO_REPORT_FIELD = "entry.1495570162";
	private static final String REASON_FIELD = "entry.2098680843";
	private static final String CATEGORY_FIELD = "entry.1904974413";
	private static final String QUOTE_TO_EDIT = "entry.705618757";
	private static final String REPORTTURL = "https://docs.google.com/forms/d/1caAn96oLrwhq_vI1_TJ8SmYGkIBlDbbSCxV3cjezFGs/formResponse";
	private static final String SUGGESTURL = "https://docs.google.com/forms/d/1ty5hA2wKy1JW0vUw00r_fH0f5cfFYTa-UZjopQTEVbs/formResponse";
	private String suggested_category = "";
	private String reason;
	private String quote;
	private Boolean isPremium = false;
	private RelativeLayout adLayout;
	private Button advertiseBtn;
    CallbackManager callbackManager;



    @SuppressLint("SimpleDateFormat")
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.quote_view,
				container, false);

        final Context m = getActivity().getApplicationContext();

        setHasOptionsMenu(true);

        // Create an ad.
		adLayout = (RelativeLayout) rootView.findViewById(R.id.ad_layout);
		adView = (AdView) rootView.findViewById(R.id.adView);
		AdRequest adRequest = new AdRequest.Builder().build();
		adView.loadAd(adRequest);
		adView.setAdListener(new AdViewZadListener());
		sp = getActivity().getApplicationContext().getSharedPreferences(
				"com.app.zad.fav_id", Context.MODE_PRIVATE);
		editor = sp.edit();
		ids = sp.getStringSet("ids", new HashSet<String>());
		idlist = new ArrayList<>(ids);
		arrayCat = getActivity().getApplicationContext().getResources()
				.getStringArray(R.array.categories);

		final TextView quote_text = (TextView) rootView
				.findViewById(R.id.Quote_Main_text);
		TextView quote_text_shadow = (TextView) rootView
				.findViewById(R.id.text_shadow);
		final TextView author_name = (TextView) rootView
				.findViewById(R.id.Author_Main_title);
		TextView wiki_text = (TextView) rootView
				.findViewById(R.id.Author_Main_Bio);
		ImageView author_image_view = (ImageView) rootView
				.findViewById(R.id.Author_Main_Pic);
//		final FlipImageView fav_image = (FlipImageView) rootView
//				.findViewById(R.id.Fav_button);
		TextView category_text = (TextView) rootView
				.findViewById(R.id.Category_Text);

		// adSpaceBanner = (RelativeLayout)
		// rootView.findViewById(R.id.ad_space);
		advertiseBtn = (Button) rootView.findViewById(R.id.advertise_with_us);

		advertiseBtn.setOnClickListener(new advertiseOnClick());

		RemoveAdsBtn = (ImageView) rootView.findViewById(R.id.removeAd);

		// Get Shared Premium
		final SharedPreferences iapPrefrence = this.getActivity()
				.getSharedPreferences(getString(R.string.IAPPreference),
						Context.MODE_PRIVATE);
		// Get Premium Value
		isPremium = iapPrefrence.getBoolean(getString(R.string.isPremium),
				false);
		if (isPremium == true) {
			// we love you sir, don't pay anything else
			adLayout.setVisibility(View.GONE);
			adView.destroy();
		}

		// Remove Ads Action
		RemoveAdsBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				// Get Premium Value
				isPremium = iapPrefrence.getBoolean(
						getString(R.string.isPremium), false);

				if (isPremium == true) {
					// we love you sir, don't pay anything else
					adLayout.setVisibility(View.GONE);
					adView.destroy();
				}

				else {
					// hey you, drop some money asshole
					Intent billingX = new Intent(getActivity()
							.getApplicationContext(), Billing.class);
					startActivity(billingX);

				}
			}
		});

		// set custom Font
		Typeface type = Typeface.createFromAsset(getActivity().getAssets(),
				"fonts/adobe.otf");
		if (android.os.Build.VERSION.SDK_INT > 16) {

			quote_text.setTypeface(type);
			quote_text_shadow.setTypeface(type);

		}




		idintbun = getArguments().getInt("idint") + 1;

		final Bitmap bmp = getArguments().getParcelable("pic");
		d = new BitmapDrawable(getResources(), bmp);

		final Quote quoteinstance1 = quote2.getAnObjects(getActivity(),
				"Quote", getArguments().getString("quote")).get(0);

//		if (isFav(idintbun - 1)) {
//			fav_image.setRotationReversed(true);
//			fav_image.setChecked(true);
//			favcheck = true;
//		} else {
//			fav_image.setRotationReversed(false);
//			fav_image.setChecked(false);
//			favcheck = false;
//		}
//
//
//		fav_image.setOnClickListener(new View.OnClickListener() {
//
//			Integer idfav = quoteinstance1.ID;
//			String idfavstring5 = idfav.toString();
//
//			@Override
//			public void onClick(View v) {
//
//				if (favcheck) {
//					fav_image.setRotationReversed(false);
//					fav_image.setChecked(false);
//					ids.remove(idfavstring5);
//					favcheck = false;
//				} else {
//					fav_image.setRotationReversed(true);
//					fav_image.setChecked(true);
//					ids.add(idfavstring5);
//					favcheck = true;
//				}
//				editor.clear();
//				editor.putStringSet("ids", ids);
//				editor.commit();
//			}
//		});

		author_image_view.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent boring = new Intent(m,
						Authors_list_quotes_notBoring.class);

				boring.putExtra("authorRetrieved",
						getArguments().getString("author"));
				boring.putExtra("Image", bmp);

				startActivity(boring);

			}
		});

		Quote quoteInstance = new Quote();

		String initString = getArguments().getString("quote");
		String finalString = "\" " + initString + " \"";

		// setting Quotations Colors using Spans
		final SpannableStringBuilder sb = new SpannableStringBuilder(
				finalString);
		final ForegroundColorSpan fcs = new ForegroundColorSpan(getActivity()
				.getResources().getColor(R.color.yellow));
		final ForegroundColorSpan fcs2 = new ForegroundColorSpan(getActivity()
				.getResources().getColor(R.color.yellow));
		sb.setSpan(fcs, 0, 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
		sb.setSpan(fcs2, finalString.length() - 1, finalString.length(),
				Spannable.SPAN_INCLUSIVE_INCLUSIVE);

		quote_text.setText(sb);

		// ShadowText & MainText must be the same paragraph trxt on line & same
		// line spacing & same size so that it will look Pretty
		String finalShadow = "  " + initString + "  ";
		quote_text_shadow.setText(finalShadow);
		quote_text_shadow.setScaleX((float) 1.4);
		quote_text_shadow.setScaleY((float) 1.4);
		quote_text_shadow.setLineSpacing(1.3f, 1.4f);
		quote_text.setOnLongClickListener(new View.OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {

				Vibrator vb = (Vibrator) getActivity().getSystemService(
						Context.VIBRATOR_SERVICE);
				vb.vibrate(50);
				Copy_process();
				return false;
			}
		});

		category_text.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				// Rashwan, Get me this !!!!!!!!
				/*
				 * Intent i1 = new Intent(getActivity(),
				 * CategoriesListQuotesNotBoring.class);
				 * i1.putExtra("categoriesString", arrayCat[position]); if
				 * (position == 0) { i1.putExtra("categoriesNum", position + 1);
				 * } else { i1.putExtra("categoriesNum", position + 2); }
				 * startActivity(i1);
				 */

			}
		});

		author_name.setText(getArguments().getString("author"));
		wiki_text.setText(getArguments().getString("wiki"));
		author_image = quoteInstance.getAuthorImage(getArguments().getString(
				"author"));
		Integer catnum = quoteinstance1.Category;

		category_text.setText(arrayCat[catnum - 1]);

		if (getArguments().getBoolean("where")) {
			Bitmap b = Drawable_into_Bitmap.drawableToBitmap(d);
			Bitmap bX = GetCroppedBitmap.getCroppedBitmap(b);
			author_image_view.setImageBitmap(bX);
		} else {
			Bitmap c = Drawable_into_Bitmap.drawableToBitmap(author_image);
			Bitmap cX = GetCroppedBitmap.getCroppedBitmap(c);
			author_image_view.setImageBitmap(cX);
		}


        return rootView;

	}


    public void shareOnFb() {
        Intent intent = new Intent(getActivity(),Facebook_Share.class);
        intent.putExtra("shareQuote", getArguments().getString("quote"));
        intent.putExtra("shareAuthor",getArguments().getString("author"));
        startActivity(intent);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }


    public static Quote_view_fragment newinstance(String quote, String author,
                                                  String wiki, Integer idInteger, Boolean fromwhere) {
		Quote_view_fragment instance = new Quote_view_fragment();
		Bundle b = new Bundle();
		b.putString("quote", quote);
		b.putString("author", author);
		b.putString("wiki", wiki);
		b.putBoolean("where", fromwhere);
		b.putInt("idint", idInteger);
		instance.setArguments(b);

		return instance;
	}

	public static Quote_view_fragment newinstance(String quote, String author,
			String wiki, Integer idInteger, Bitmap bitmap, Boolean fromwhere) {
		Quote_view_fragment instance = new Quote_view_fragment();
		Bundle b = new Bundle();
		b.putString("quote", quote);
		b.putString("author", author);
		b.putString("wiki", wiki);
		b.putParcelable("pic", bitmap);
		b.putInt("idint", idInteger);
		b.putBoolean("where", fromwhere);
		instance.setArguments(b);
		return instance;
	}

	public Quote_view_fragment() {
		super();
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

	}

//	public static Boolean isFav(Integer idInteger) {
//		idfavstring = idInteger.toString();
//		ids = sp.getStringSet("ids", new HashSet<String>());
//		idlist = new ArrayList<String>(ids);
//		fav_not = idlist.contains(idfavstring);
//		return fav_not;
//	}


	private void showConfirmationDialog() {
		final Dialog dlg = new Dialog(getActivity());
		dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dlg.setContentView(R.layout.submit_report_layout);
		dlg.setTitle(getString(R.string.chooseCategoryDialogTitle));

		final Button submit = (Button) dlg.findViewById(R.id.send_report);
		Button cancel = (Button) dlg.findViewById(R.id.cancel_report);
		final EditText reasontxt = (EditText) dlg.findViewById(R.id.otherText);
		reasontxt.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (s.length() == 0 || s.equals("")) {
					submit.setEnabled(false);
				} else {
					submit.setEnabled(true);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {

				reason = s.toString();
			}
		});

		submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dlg.cancel();
				submitReport(reason);

			}
		});
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dlg.cancel();

			}
		});
		Window window = dlg.getWindow();
		WindowManager.LayoutParams wlp = window.getAttributes();

		wlp.gravity = Gravity.CENTER;
		wlp.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;

		window.setAttributes(wlp);
		dlg.setCanceledOnTouchOutside(true);
		dlg.show();

	}

	private void submitReport(String reason) {
		Map<String, String> params = new HashMap<String, String>();
		quote = getArguments().getString("quote");

		params.put(QUOTE__TO_REPORT_FIELD, quote);
		params.put(REASON_FIELD, reason);
		try {
			HttpUtility.sendPostRequest(REPORTTURL, params);
			HttpUtility.readMultipleLinesRespone();
			Toast.makeText(getActivity(),
					getString(R.string.succsufellyReporte), Toast.LENGTH_LONG)
					.show();
		} catch (IOException ex) {
			// NEW - MELEGY
			SharedPreferences pref = getActivity().getSharedPreferences(
					"MyPref", 0);
			pref.edit().putBoolean("unsent_report", true).commit();
			Toast.makeText(getActivity(),
					getString(R.string.send_when_connected), Toast.LENGTH_LONG)
					.show();
			Editor editor = pref.edit();
			for (String s : params.keySet()) {
				editor.putString(s, params.get(s));
			}
			editor.commit();
		}
		// ------------------------------------------------------------
		HttpUtility.disconnect();
	}

	private void submitEdit(String suggestd_category) {
		Map<String, String> params = new HashMap<String, String>();
		quote = getArguments().getString("quote");
		params.put(QUOTE_TO_EDIT, quote);
		params.put(CATEGORY_FIELD, suggestd_category);
		try {
			HttpUtility.sendPostRequest(SUGGESTURL, params);
			HttpUtility.readMultipleLinesRespone();
			Toast.makeText(getActivity(),
					getString(R.string.succsufellyEdited), Toast.LENGTH_LONG)
					.show();
		} catch (IOException ex) {
			// NEW - MELEGY
			SharedPreferences pref = getActivity().getSharedPreferences(
					"MyPref", 0);
			pref.edit().putBoolean("unsent_suggest", true).commit();
			Toast.makeText(
					getActivity(),
					R.string.reprot_when_connected,
					Toast.LENGTH_LONG).show();
			Editor editor = pref.edit();
			for (String s : params.keySet()) {
				editor.putString(s, params.get(s));
			}
			editor.commit();
		}
		// ------------------------------------------------------------
		HttpUtility.disconnect();
	}

	private void showEditDialog() {
		final String[] items = getResources()
				.getStringArray(R.array.categories);
		final Dialog dlg2 = new Dialog(getActivity());
		dlg2.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dlg2.setContentView(R.layout.submit_new_cat);

		final Button submit = (Button) dlg2.findViewById(R.id.send_report);
		submit.setEnabled(false);
		Button cancel = (Button) dlg2.findViewById(R.id.cancel_report);
		final ListView CatList = (ListView) dlg2.findViewById(R.id.CatList);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
				R.layout.zad_radio_item, items);
		CatList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		CatList.setAdapter(adapter);

		CatList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				suggested_category = items[position];
				submit.setEnabled(true);

			}
		});

		submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				dlg2.dismiss();
				submitEdit(suggested_category);

			}
		});
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dlg2.dismiss();

			}
		});

		Window window2 = dlg2.getWindow();
		WindowManager.LayoutParams wlp = window2.getAttributes();

		wlp.gravity = Gravity.CENTER;
		wlp.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;

		window2.setAttributes(wlp);
		dlg2.setCanceledOnTouchOutside(true);
		dlg2.show();

	}

	private void shareTextUrl() {

		String shareBody = "\"" + getArguments().getString("quote") + "\""
				+ '\n' + "- " + getArguments().getString("author");

		Resources resources = getResources();
		PackageManager pm = getActivity().getPackageManager();
		Intent sendIntent = new Intent(Intent.ACTION_SEND);
		sendIntent.setType("text/plain");

		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");
		// Intent openInChooser =
		// Intent.createChooser(sendIntent,resources.getString(R.string.share_quote));
		List<ResolveInfo> resInfo = pm.queryIntentActivities(intent, 0);
		List<LabeledIntent> intentList = new ArrayList<LabeledIntent>();
		for (int i = 0; i < resInfo.size(); i++) {
			// Extract the label, append it, and repackage it in a LabeledIntent
			ResolveInfo ri = resInfo.get(i);
			String packageName = ri.activityInfo.packageName;

			if (packageName.contains("facebook.katana")) {

				// Warning: Facebook IGNORES our text. They say
				// "These fields are intended for users to express themselves. Pre-filling these fields erodes the authenticity of the user voice."
				// One workaround is to use the Facebook SDK to post, but that
				// doesn't allow the user to choose how they want to share. We
				// can also make a custom landing page, and the link
				// will show the <meta content ="..."> text from that page with
				// our link in Facebook.
				intent = new Intent(getActivity(), Facebook_Share.class);
				intent.putExtra("shareQuote", getArguments().getString("quote"));
				intent.putExtra("shareAuthor",
						getArguments().getString("author"));
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
			// intentList.add(new LabeledIntent(intent, packageName,
			// ri.loadLabel(pm), ri.icon));
		}

		// convert intentList to array
		/*
		 * Intent openInChooser = Intent.createChooser(intentList.remove(0),
		 * resources.getString(R.string.share_quote));
		 */
		/*
		 * LabeledIntent[] extraIntents = intentList .toArray(new
		 * LabeledIntent[intentList.size()]);
		 */
		Intent emailIntent = new Intent(Intent.ACTION_SENDTO,
				Uri.parse("mailto:"));
		startActivity(Intent.createChooser(emailIntent,
				resources.getString(R.string.share_quote)).putExtra(
				Intent.EXTRA_INITIAL_INTENTS,
				intentList.toArray(new LabeledIntent[intentList.size()])));
	}

	@Override
	public void onResume() {
		super.onResume();
		if (adView != null) {
			adView.resume();
		}
	}

	@Override
	public void onPause() {
		if (adView != null) {
			adView.pause();
		}
		super.onPause();
	}

	/** Called before the activity is destroyed. */
	@Override
	public void onDestroy() {

		// Destroy the AdView.
		if (adView != null) {
			adView.destroy();
		}
		super.onDestroy();
	}

	@Override
	public void onStart() {
		super.onStart();
		// The rest of your onStart() code.
		// EasyTracker.getInstance(this).activityStart(this); // Add this
		// method.
	}

	@Override
	public void onStop() {
		super.onStop();
		// The rest of your onStop() code.
		// EasyTracker.getInstance(this).activityStop(this); // Add this method.
	}

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode,resultCode,data);

    }

    public class AdViewZadListener extends AdListener {

		private String LOG_TAG = "Ad_Error";

		/** Called when an ad failed to load. */
		@Override
		public void onAdFailedToLoad(int error) {
			String message = "onAdFailedToLoad: " + getErrorReason(error);
			Log.d(LOG_TAG, message);
			// Toast.makeText(getActivity(), message,
			// Toast.LENGTH_SHORT).show();
			adLayout.setVisibility(View.GONE);
			// adSpaceBanner.setVisibility(View.VISIBLE);
		}

		/** Called when an ad is loaded. */
		@Override
		public void onAdLoaded() {
			Log.d(LOG_TAG, "onAdLoaded");
			// Toast.makeText(getActivity(), "onAdLoaded", Toast.LENGTH_SHORT)
			// .show();
			// adSpaceBanner.setVisibility(View.INVISIBLE);

		}

	}

	public class advertiseOnClick implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			Intent i = new Intent(Intent.ACTION_SEND);
			i.setType("message/rfc822");
			i.putExtra(Intent.EXTRA_EMAIL,
					new String[] { "advertise@appZad.com" });
			i.putExtra(Intent.EXTRA_SUBJECT, "Advertise in Zad");
			i.putExtra(Intent.EXTRA_TEXT, "I would like to advertise about ..");
			try {
				startActivity(Intent.createChooser(i, "Send e-mail..."));
			} catch (android.content.ActivityNotFoundException ex) {
				Toast.makeText(getActivity(),
						"There are no email clients installed.",
						Toast.LENGTH_SHORT).show();
			}

		}
	}

	private String getErrorReason(int errorCode) {
		String errorReason = "";
		switch (errorCode) {
		case AdRequest.ERROR_CODE_INTERNAL_ERROR:
			errorReason = "Internal error";
			break;
		case AdRequest.ERROR_CODE_INVALID_REQUEST:
			errorReason = "Invalid request";
			break;
		case AdRequest.ERROR_CODE_NETWORK_ERROR:
			errorReason = "Network Error";
			break;
		case AdRequest.ERROR_CODE_NO_FILL:
			errorReason = "No fill";
			break;
		}
		return errorReason;
	}

	public void Copy_process() {
		String shareBody = "\"" + getArguments().getString("quote") + "\""
				+ '\n' + "- " + getArguments().getString("author");

		android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getActivity()
				.getSystemService(Context.CLIPBOARD_SERVICE);
		android.content.ClipData clip = android.content.ClipData.newPlainText(
				"Text Label", shareBody);
		clipboard.setPrimaryClip(clip);
		Toast.makeText(getActivity().getApplicationContext(),
				R.string.copied_to_clipboard_, Toast.LENGTH_SHORT).show();

	}

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.quote_menu,menu);
    }

    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
			case android.R.id.home:
				getActivity().onBackPressed();
                return true;
            case R.id.action_copy:
                Copy_process();
                return true;
            case R.id.action_share:
                shareTextUrl();
                return true;
            case R.id.action_report:
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);
                showConfirmationDialog();
                return true;
            case R.id.action_suggest_edit:
                StrictMode.ThreadPolicy policy2 = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy2);
                showEditDialog();
                return true;
        }
		return super.onOptionsItemSelected(item);
	}
	public String getCurrentQuote(){
        return getArguments().getString("quote");
    }
    public String getCurrentAuthor(){
        return getArguments().getString("author");
    }
    public int getCurrentId(){
        return getArguments().getInt("idint");
    }
}
