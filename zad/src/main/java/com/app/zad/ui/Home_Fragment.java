package com.app.zad.ui;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.app.zad.R;
import com.app.zad.helper.Drawable_into_Bitmap;
import com.app.zad.helper.GetCroppedBitmap;
import com.app.zad.helper.JoyStickAnimation;
import com.app.zad.helper.floodBTN;
import com.app.zad.work_in_background.Alarms_Set_up;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.Target;
import com.github.amlcurran.showcaseview.targets.ViewTarget;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

public class Home_Fragment extends Fragment implements OnClickListener {

	public static final String home_BROADCAST_ACTION = "home_intent";
	AlarmManager mazag_alarmManager;
	PendingIntent mazag_pendingIntent;

	String author_retrived;
	String quote_retrived;
	boolean oneQuote;
	String wiki_random;
	Intent i;
	IntentFilter intentFilter;
	DatabaseHelper dbHelper;
	PendingIntent pendingIntent;
	AlarmManager alarmManager;
	PendingIntent pendingIntent2;
	AlarmManager alarmManager2;
	TextView TvToday;
	TextView TvMazag;
	TextView TvRandom;
	TextView TvToday_Auth;
	TextView TvMazag_Auth;
	TextView TvRandom_Auth;

	SharedPreferences Ab_mazag;
	SharedPreferences last_mazag_quote_wiki_sharedpreferences;

	SharedPreferences Exact_service_from_home;
	SharedPreferences MoodQuestionSharedPreference;
	private BroadcastReceiver broadcastReceiver;

	private BroadcastReceiver broadcastReceiver2;

	private SeekBar JoyStick;
	private Vibrator vibro;
	private Bitmap cropped1;
	private Bitmap cropped2;
	private Bitmap cropped3;
	private Dialog dlg;
	private RelativeLayout Color_layout;
	private Button Happy;
	private Button Sad;
	private Button Mabdoon;
	private Button Angry;
	private Button Afraid;
	private Button Loved;
	private Button Compressed;
	private ImageView Dismiss;
	private RelativeLayout happy_big;
	private RelativeLayout sad_big;
	private RelativeLayout mabdoon_big;
	private RelativeLayout angry_big;
	private RelativeLayout afraid_big;
	private RelativeLayout loved_big;
	private RelativeLayout compress_big;

	private int CurrentFaceValue = 1;
	SharedPreferences Sersharedpreferences2;
	SharedPreferences Sersharedpreferences;
	SharedPreferences mazagy_SharedPreference;
	private int lastMazgID;
	public int peep;
	private int siz;
	public static ArrayList<Quote> MazagList = new ArrayList<Quote>();
	Drawable mazag_Image = Magic_Activity.autortopic.get("ابن قيم الجوزية");
	Drawable random_Image = Magic_Activity.autortopic.get("ابن قيم الجوزية");
	Drawable today_image = Magic_Activity.autortopic.get("ابن قيم الجوزية");
	ImageView Author_pic_random;
	ImageView Author_pic_today;
	ImageView Author_pic_mazag;
	private ImageView MzagyFaceMutton;
	private Animation rotatee;
	private ImageView btnRefresh;
	private Animation slideIn;
	private Animation slideOut;
	private Animation fade_In;
	private Animation fade_out;
	private Animation slideInLeft;
	private Animation slideOutRight;
	private Animation slideInRight;
	private Animation slideOutLeft;
	HashMap<Integer, Integer> generate_Database_values;
	private SharedPreferences Hmprefs;
	private View view;
	private SharedPreferences sharedpreferences;
	private Activity a;
	@SuppressWarnings("deprecation")
	// NOTIFICATION
	public static final String KEY_PREF_NOTIFICATION = "notificationPref";
	public static final String KEY_PREF_MORNING_EVENING = "morningEvinengNotificationPref";
	public static final String KEY_PREF_MODES = "modesNotificationPref";

	// KAZAKY FX x1
	private ShowcaseView showcaseView;
	private int counter = 0;
	private Window window;
	private Animation grow_fade;
	private Animation grow;
	private TextView right_Red;
	private TextView left_Red;
    private ActionBar actionBar;


    public class ShowCaseViewOnClick implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			switch (counter) {
			case 0:
				showcaseView.setShowcase(new ViewTarget(TvMazag), true);
				showcaseView.setContentDescription(getActivity().getString(
						R.string.help_2));
				showcaseView.setContentText(getActivity().getString(
						R.string.click_to_learn_more_quote));
				showcaseView.setContentTitle(getActivity().getString(
						R.string.quote_details));

				TvMazag.setSelected(true);
				TvToday.setSelected(true);
				TvRandom.setSelected(true);

				break;

			case 1:
				showcaseView.setShowcase(new ViewTarget(JoyStick), true);
				TvMazag.setSelected(false);
				TvToday.setSelected(false);
				TvRandom.setSelected(false);

				showcaseView.setButtonText(getActivity().getString(
						R.string.hide));
				showcaseView.setContentDescription(getActivity().getString(
						R.string.help_1));
				showcaseView.setContentText(getActivity().getString(
						R.string.drag_slider));
				showcaseView.setContentTitle(getActivity().getString(
						R.string.changing_between_quotes));

				break;

			case 2:
				showcaseView.setTarget(Target.NONE);
				showcaseView.hide();

				break;

			}
			counter++;
		}
	}

	// KAZAKY FX x1

	@SuppressWarnings("deprecation")
	public View onCreateView(final LayoutInflater inflater,
			final ViewGroup container, final Bundle savedInstanceState) {

		view = inflater.inflate(R.layout.home, container, false);
        actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();


		a = getActivity();

		TvMazag = (TextView) view.findViewById(R.id.mazag_quote);
		TvToday = (TextView) view.findViewById(R.id.TodayQuote);
		TvRandom = (TextView) view.findViewById(R.id.Random_quote);
		TvToday_Auth = (TextView) view.findViewById(R.id.Today_Author);
		TvRandom_Auth = (TextView) view.findViewById(R.id.Random_quote_Author);
		TvMazag_Auth = (TextView) view.findViewById(R.id.mazag_author);
		MzagyFaceMutton = (ImageView) view.findViewById(R.id.faceButton);

		rotatee = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate);
		slideIn = AnimationUtils.loadAnimation(getActivity(),
				R.anim.slide_in_from_bottom);
		slideOut = AnimationUtils.loadAnimation(getActivity(),
				R.anim.slide_out_to_bottom);
		slideInLeft = AnimationUtils.loadAnimation(getActivity(),
				R.anim.in_from_left);
		slideOutLeft = AnimationUtils.loadAnimation(getActivity(),
				R.anim.out_to_left);
		slideInRight = AnimationUtils.loadAnimation(getActivity(),
				R.anim.in_from_right);
		slideOutRight = AnimationUtils.loadAnimation(getActivity(),
				R.anim.out_to_right);

		fade_In = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in);
		grow_fade = AnimationUtils.loadAnimation(getActivity(),
				R.anim.abc_grow_fade_in_from_bottom);
		grow = AnimationUtils.loadAnimation(getActivity(), R.anim.abc_grow);
		fade_out = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_out);

		Color_layout = (RelativeLayout) view.findViewById(R.id.color_layout);

		get_Last_Chosen_Mazag();

		get_Last_mazagList();

		Open_Zabtly_or_Not();

		mCallback.onMazagGet(lastMazgID);
		CurrentFaceValue = lastMazgID;
		Color_layout.setBackground(new ColorDrawable(getMatchingColor(CurrentFaceValue)));
        actionBar.setBackgroundDrawable(new ColorDrawable(getMatchingColor(CurrentFaceValue)));
        if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
            window = getActivity().getWindow();
            window.setStatusBarColor(MazagToColor(CurrentFaceValue));
        }


        JoyStick = (SeekBar) view.findViewById(R.id.JoyStickController);
		btnRefresh = (ImageView) view.findViewById(R.id.Refresh_button);
		vibro = (Vibrator) this.getActivity().getSystemService(
				Context.VIBRATOR_SERVICE);
		Author_pic_mazag = (ImageView) view.findViewById(R.id.Author_pic_mazag);
		Author_pic_today = (ImageView) view.findViewById(R.id.Author_pic_today);
		Author_pic_random = (ImageView) view
				.findViewById(R.id.Author_pic_random);
		right_Red = (TextView) view.findViewById(R.id.right_dot);
		left_Red = (TextView) view.findViewById(R.id.left_dot);

		SetSelectedFAB(true);
		Drawable d1 = mazag_Image;
		Drawable d2 = today_image;
		Drawable d3 = random_Image;

		Bitmap bm1 = Drawable_into_Bitmap.drawableToBitmap(d1);
		Bitmap bm2 = Drawable_into_Bitmap.drawableToBitmap(d2);
		Bitmap bm3 = Drawable_into_Bitmap.drawableToBitmap(d3);

		cropped1 = GetCroppedBitmap.getCroppedBitmap(bm1);
		cropped2 = GetCroppedBitmap.getCroppedBitmap(bm2);
		cropped3 = GetCroppedBitmap.getCroppedBitmap(bm3);

		Author_pic_mazag.setImageBitmap(cropped1);
		Author_pic_today.setImageBitmap(cropped2);
		Author_pic_random.setImageBitmap(cropped3);

		Author_pic_mazag.setOnClickListener(this);
		Author_pic_random.setOnClickListener(this);
		Author_pic_today.setOnClickListener(this);

		JoyStick.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				ShakeItBaby();
				right_Red.setVisibility(View.INVISIBLE);
				left_Red.setVisibility(View.INVISIBLE);

			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				Quote QWERT;
				Quote QWERT2;

				getActivity();
				last_mazag_quote_wiki_sharedpreferences = getActivity()
						.getSharedPreferences("last_mazag_quote_wiki_folder",
								Context.MODE_PRIVATE);

				Editor mazagWikiEditor = last_mazag_quote_wiki_sharedpreferences
						.edit();
				int category_retrived;
				if (progress > 99) {
					vibro.vibrate(50);

					if (peep == MazagList.size() - 1) {
						right_Red.setVisibility(View.VISIBLE);
					}

					try {
						int nxt_index = peep + 1;
						if (peep < MazagList.size() - 1) {

							peep = peep + 1;
						}
						QWERT = MazagList.get(nxt_index);
						String maz_Quote = QWERT.Quote;
						String maz_Author = QWERT.Author;
						mazag_Image = QWERT.getAuthorImage(maz_Author);
						String Rwiki = QWERT.getwiki(getActivity(), QWERT);

						category_retrived = QWERT.Category;

						mazagWikiEditor.putString("last_mazag_quote_wiki",
								Rwiki);
						mazagWikiEditor.putInt("last_mazag_quote_category",
								category_retrived);

						mazagWikiEditor.commit();

						TvMazag.startAnimation(slideOutRight);
						TvMazag_Auth.startAnimation(slideOutRight);
						Author_pic_mazag.startAnimation(fade_out);

						TvMazag.setText(maz_Quote);
						TvMazag_Auth.setText(maz_Author);
						Author_pic_mazag.setImageBitmap(GetCroppedBitmap
								.getCroppedBitmap(Drawable_into_Bitmap
										.drawableToBitmap(mazag_Image)));

						TvMazag.startAnimation(slideInRight);
						TvMazag_Auth.startAnimation(slideInRight);
						Author_pic_mazag.startAnimation(fade_In);

						getActivity();
						last_mazag_quote_wiki_sharedpreferences = getActivity()
								.getSharedPreferences(
										"last_mazag_quote_wiki_folder",
										Context.MODE_PRIVATE);

						Editor mazagrightEditor = last_mazag_quote_wiki_sharedpreferences
								.edit();

						mazagrightEditor.putInt(
								"last_mazag_quote_ID_inDataBase", QWERT.ID);

						mazagrightEditor.commit();

					} catch (Exception e) {
						e.printStackTrace();
					}

				} else if (progress < 1) {

					vibro.vibrate(50);
					if (peep < 1) {
						left_Red.setVisibility(View.VISIBLE);
					}

					try {
						int brv_index = peep - 1;
						if (peep > 0) {

							peep = peep - 1;
						}
						QWERT2 = MazagList.get(brv_index);

						String maz_Quote2 = QWERT2.Quote;
						String maz_Author2 = QWERT2.Author;
						mazag_Image = QWERT2.getAuthorImage(maz_Author2);
						String Rwiki = QWERT2.getwiki(getActivity(), QWERT2);
						category_retrived = QWERT2.Category;

						mazagWikiEditor.putString("last_mazag_quote_wiki",
								Rwiki);
						mazagWikiEditor.putInt("last_mazag_quote_category",
								category_retrived);
						mazagWikiEditor.commit();

						TvMazag.startAnimation(slideOutLeft);
						TvMazag_Auth.startAnimation(slideOutLeft);
						Author_pic_mazag.startAnimation(fade_out);

						TvMazag.setText(maz_Quote2);
						TvMazag_Auth.setText(maz_Author2);
						Author_pic_mazag.setImageBitmap(GetCroppedBitmap
								.getCroppedBitmap(Drawable_into_Bitmap
										.drawableToBitmap(mazag_Image)));

						TvMazag.startAnimation(slideInLeft);
						TvMazag_Auth.startAnimation(slideInLeft);
						Author_pic_mazag.startAnimation(fade_In);

						getActivity();
						last_mazag_quote_wiki_sharedpreferences = getActivity()
								.getSharedPreferences(
										"last_mazag_quote_wiki_folder",
										Context.MODE_PRIVATE);

						Editor mazagrightEditor2 = last_mazag_quote_wiki_sharedpreferences
								.edit();

						mazagrightEditor2.putInt(
								"last_mazag_quote_ID_inDataBase", QWERT2.ID);
						mazagrightEditor2.commit();

					} catch (Exception e) {
						e.printStackTrace();
					}

				}
			}
		});

		TvMazag.setOnClickListener(this);
		TvToday.setOnClickListener(this);
		TvRandom.setOnClickListener(this);
		btnRefresh.setOnClickListener(this);
		MzagyFaceMutton.setOnClickListener(this);
		getActivity();
		Sersharedpreferences = getActivity().getSharedPreferences(
				"MyPREFERENCES", Context.MODE_PRIVATE);

		TvToday_Auth.setText(Sersharedpreferences.getString("Skey2",
				"Loading.."));
		Quote quoteinstance = new Quote();
		today_image = quoteinstance.getAuthorImage(Sersharedpreferences
				.getString("Skey2", "Loading.."));
		TvToday.setText(Sersharedpreferences.getString("Skey1", "Loading.."));
		Author_pic_today.setImageBitmap(GetCroppedBitmap
				.getCroppedBitmap(Drawable_into_Bitmap
						.drawableToBitmap(today_image)));

		getActivity();
		Sersharedpreferences2 = getActivity().getSharedPreferences(
				"MyPREFERENCES2", Context.MODE_PRIVATE);

		TvRandom.setText(Sersharedpreferences2
				.getString("Skey1_2", "Loading.."));
		TvRandom_Auth.setText(Sersharedpreferences2.getString("Skey2_2",
				"Loading.."));
		Quote quoteinstance2 = new Quote();
		random_Image = quoteinstance2.getAuthorImage(Sersharedpreferences2
				.getString("Skey2_2", "Loading.."));
		Author_pic_random.setImageBitmap(GetCroppedBitmap
				.getCroppedBitmap(Drawable_into_Bitmap
						.drawableToBitmap(random_Image)));

		Hmprefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
		sharedpreferences = getActivity().getSharedPreferences("firstlastname",
				Context.MODE_PRIVATE);
		if (!Hmprefs.getBoolean("firstTime", false)
				|| sharedpreferences.getString("firstname", "false").equals(
						"false")) {
			UpdateTodayQuote();
			UpdateRandomQuote();

			SetDefaultSettinForTheFirstTime();
			Intent intro = new Intent(getActivity(), IntroActivity.class);
			startActivityForResult(intro, 1);

			Editor editor = Hmprefs.edit();
			editor.putBoolean("firstTime", true);
			editor.commit();
		}

		setHasOptionsMenu(true);
		return view;
	}

	private void SetDefaultSettinForTheFirstTime() {
		SharedPreferences.Editor editor = PreferenceManager
				.getDefaultSharedPreferences(
						getActivity().getApplicationContext()).edit();
		editor.putBoolean(KEY_PREF_NOTIFICATION, true);
		editor.putBoolean(KEY_PREF_MORNING_EVENING, true);
		editor.putBoolean(KEY_PREF_MODES, true);

		editor.commit();
		Alarms_Set_up def_mor = new Alarms_Set_up(getActivity());
		Alarms_Set_up def_even = new Alarms_Set_up(getActivity());
		Alarms_Set_up def_run_mood_Question = new Alarms_Set_up(getActivity());

		def_mor.Alarm_SetUpMorningQuote();
		def_even.Alarm_SetUpEveningQuote();
		def_run_mood_Question.Alarm_SetUp_Mood();

	}

	private void get_Last_mazagList() {

		mazagy_SharedPreference = getActivity().getSharedPreferences(
				"Chosen_mazag", Context.MODE_PRIVATE);

		int maz_id = mazagy_SharedPreference.getInt("Chosen_mazag_id", 0);

		SharedPreferences mazag_Hmprefs = PreferenceManager
				.getDefaultSharedPreferences(getActivity());
		if (!mazag_Hmprefs.getBoolean("firstTime55", false)) {

			update_Mazag_Card(maz_id);

			SharedPreferences.Editor maz_editor = mazag_Hmprefs.edit();
			maz_editor.putBoolean("firstTime55", true);
			maz_editor.commit();
		} else {

			Update_MazagList_and_Peep(maz_id);

		}

	}

	@SuppressLint("UseSparseArrays")
	private void Update_MazagList_and_Peep(int maz_id) {

		getActivity();
		SharedPreferences last_mazag_quote_sharedpreferences = getActivity()
				.getSharedPreferences("last_mazag_quote_wiki_folder",
						Context.MODE_PRIVATE);

		int last_quote_id = last_mazag_quote_sharedpreferences.getInt(
				"last_mazag_quote_ID_inDataBase", 2);

		Quote laQuote = new Quote();

		Quote theQuote = laQuote.getAnObjects(getActivity(), "ID",
				last_quote_id).get(0);

		TvMazag.setText(theQuote.Quote);
		TvMazag_Auth.setText(theQuote.Author);

		// //

		Quote another_MazagQuote = new Quote();

		generate_Database_values = new HashMap<Integer, Integer>();

		generate_Database_values.put(0, 4);
		generate_Database_values.put(4, 1);
		generate_Database_values.put(3, 2);
		generate_Database_values.put(5, 5);
		generate_Database_values.put(2, 7);
		generate_Database_values.put(1, 3);
		generate_Database_values.put(6, 6);

		MazagList = another_MazagQuote.getAnObjects(getActivity(), "Mood",
				generate_Database_values.get(maz_id));

		Collections.shuffle(MazagList);

		mazag_Image = theQuote.getAuthorImage(theQuote.Author);

		String Rwiki2 = theQuote.getwiki(getActivity(), theQuote);
		int category_retrived = theQuote.Category;

		// try {
		//
		// Author_pic_mazag.setImageBitmap(GetCroppedBitmap
		// .getCroppedBitmap(Drawable_into_Bitmap
		// .drawableToBitmap(mazag_Image)));
		//
		// } catch (Exception e) {
		//
		// e.printStackTrace();
		// }

		ArrayList<Integer> Black_hasn = new ArrayList<Integer>();

		for (int i = 0; i < MazagList.size(); i++) {

			Black_hasn.add(MazagList.get(i).ID);
		}

		peep = Black_hasn.indexOf(last_quote_id);

		getActivity();
		last_mazag_quote_wiki_sharedpreferences = getActivity()
				.getSharedPreferences("last_mazag_quote_wiki_folder",
						Context.MODE_PRIVATE);

		Editor mazagWikiEditor2 = last_mazag_quote_wiki_sharedpreferences
				.edit();

		mazagWikiEditor2.putString("last_mazag_quote_wiki", Rwiki2);
		mazagWikiEditor2.putInt("last_mazag_quote_category", category_retrived);

		mazagWikiEditor2.commit();

	}

	private void get_Last_Chosen_Mazag() {

		mazagy_SharedPreference = getActivity().getSharedPreferences(
				"Chosen_mazag", Context.MODE_PRIVATE);

		lastMazgID = mazagy_SharedPreference.getInt("Chosen_mazag_id", 0);

	}

	private void update_Mazag_Card(int currentFaceValue2) {
		switch (currentFaceValue2) {
		case 0:

			Filtered_mazag_quotes(4);
			break;

		case 4:
			Filtered_mazag_quotes(1);

			break;
		case 3:
			Filtered_mazag_quotes(2);

			break;
		case 5:
			Filtered_mazag_quotes(5);
			break;
		case 2:
			;
			Filtered_mazag_quotes(7);

			break;
		case 1:
			Filtered_mazag_quotes(3);

			break;
		case 6:
			Filtered_mazag_quotes(6);

			break;

		default:
			break;
		}

	}

	private void Filtered_mazag_quotes(int Mood_id) {

		Quote MazagQuote = new Quote();
		MazagList = MazagQuote.getAnObjects(getActivity(), "Mood", Mood_id);
		Collections.shuffle(MazagList);

		siz = MazagList.size();
		int min2 = 0;
		int max2 = siz - 1;
		Random r = new Random();

		peep = r.nextInt(max2 - min2 + 1) + min2;

		Quote Ran_mazag_quote = MazagList.get(peep);

		String maz_Quote = Ran_mazag_quote.Quote;
		String maz_Author = Ran_mazag_quote.Author;

		mazag_Image = Ran_mazag_quote.getAuthorImage(maz_Author);

		String Rwiki = Ran_mazag_quote.getwiki(getActivity(), Ran_mazag_quote);

		int category_retrived = Ran_mazag_quote.Category;

		try {

			Author_pic_mazag.startAnimation(fade_out);
			Author_pic_mazag.setImageBitmap(GetCroppedBitmap
					.getCroppedBitmap(Drawable_into_Bitmap
							.drawableToBitmap(mazag_Image)));

			Author_pic_mazag.startAnimation(fade_In);

		} catch (Exception e) {
			e.printStackTrace();
		}

		getActivity();
		last_mazag_quote_wiki_sharedpreferences = getActivity()
				.getSharedPreferences("last_mazag_quote_wiki_folder",
						Context.MODE_PRIVATE);

		Editor mazagWikiEditor = last_mazag_quote_wiki_sharedpreferences.edit();

		mazagWikiEditor.putString("last_mazag_quote_wiki", Rwiki);
		mazagWikiEditor.putInt("last_mazag_quote_category", category_retrived);

		mazagWikiEditor.putInt("last_mazag_quote_ID_inDataBase",
				Ran_mazag_quote.ID);

		mazagWikiEditor.commit();

		TvMazag.startAnimation(slideOutRight);
		TvMazag_Auth.startAnimation(slideOutRight);

		TvMazag.setText(maz_Quote);
		TvMazag_Auth.setText(maz_Author);

		TvMazag.startAnimation(slideInRight);
		TvMazag_Auth.startAnimation(slideInRight);
	}

	OnMazagSelectedListener mCallback;

	public interface OnMazagSelectedListener {
		public void onMazagGet(int i);

	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);

		try {
			mCallback = (OnMazagSelectedListener) context;
		} catch (ClassCastException e) {
			throw new ClassCastException(context.getClass().getSimpleName()
					+ " must implement OnHeadlineSelectedListener");
		}
	}

	protected void ShakeItBaby() {
		JoyStickAnimation anim = new JoyStickAnimation(JoyStick,
				JoyStick.getProgress(), 50);
		anim.setDuration(500);
		JoyStick.startAnimation(anim);
	}

	private void UpdateRandomQuote() {

		Alarms_Set_up Random_Quote_instance = new Alarms_Set_up(getActivity());
		Random_Quote_instance.Alarm_SetUp_RandomQuote();

	}

	private void UpdateTodayQuote() {

		Alarms_Set_up today_Quote_instance = new Alarms_Set_up(getActivity());
		today_Quote_instance.Alarm_SetUp_TodayQuote();

	}

	@SuppressWarnings("deprecation")
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.Author_pic_mazag:
			author_retrived = (String) TvMazag_Auth.getText();
			Intent notboring = new Intent(getActivity(),
					Authors_list_quotes_notBoring.class);
			notboring.putExtra("authorRetrieved", author_retrived);
			startActivity(notboring);
			break;

		case R.id.Author_pic_random:
			author_retrived = (String) TvRandom_Auth.getText();
			Intent notboring2 = new Intent(getActivity(),
					Authors_list_quotes_notBoring.class);
			notboring2.putExtra("authorRetrieved", author_retrived);
			startActivity(notboring2);
			break;

		case R.id.Author_pic_today:
			author_retrived = (String) TvToday_Auth.getText();
			Intent notboring3 = new Intent(getActivity(),
					Authors_list_quotes_notBoring.class);
			notboring3.putExtra("authorRetrieved", author_retrived);
			startActivity(notboring3);
			break;

		case R.id.mazag_quote:
			oneQuote = false;
			quote_retrived = (String) TvMazag.getText();
			author_retrived = (String) TvMazag_Auth.getText();
			String mazag_wiki = last_mazag_quote_wiki_sharedpreferences
					.getString("last_mazag_quote_wiki", "None Also!");

			Intent i1 = new Intent(getActivity(),
					Quote_view_pager_activity.class);

			i1.putExtra("oneQuote", oneQuote);
			i1.putExtra("quoteRetrived", quote_retrived);
			i1.putExtra("authorRetrived", author_retrived);
			i1.putExtra("wiki", mazag_wiki);
			i1.putExtra("Mazag", true);
			i1.putExtra("Position", peep);

			startActivity(i1);
			break;

		case R.id.TodayQuote:
			oneQuote = true;
			quote_retrived = (String) TvToday.getText();
			author_retrived = (String) TvToday_Auth.getText();

			String Today_wiki = Sersharedpreferences.getString("Skey3",
					"None Also!");
			int Today_category = Sersharedpreferences.getInt("Skey4", 2);

			Intent i2 = new Intent(getActivity(),
					Quote_view_pager_activity.class);
			i2.putExtra("oneQuote", oneQuote);
			i2.putExtra("quoteRetrived", quote_retrived);
			i2.putExtra("authorRetrived", author_retrived);
			i2.putExtra("categoryRetrived", Today_category);
			i2.putExtra("wiki", Today_wiki);
			startActivity(i2);
			break;

		case R.id.Random_quote:
			oneQuote = true;
			quote_retrived = (String) TvRandom.getText();
			author_retrived = (String) TvRandom_Auth.getText();
			String Ran_wiki = Sersharedpreferences2.getString("Skey3_2",
					"None Also!");
			int Random_category = Sersharedpreferences2.getInt("Skey4_2", 2);

			Intent i3 = new Intent(getActivity(),
					Quote_view_pager_activity.class);
			i3.putExtra("oneQuote", oneQuote);
			i3.putExtra("quoteRetrived", quote_retrived);
			i3.putExtra("authorRetrived", author_retrived);
			i3.putExtra("wiki", Ran_wiki);
			i3.putExtra("categoryRetrived", Random_category);

			startActivity(i3);
			break;
		case R.id.faceButton:
			Zabtly_Mazagy_WelNabi(lastMazgID);
			break;

		case R.id.happy:
            dlg.dismiss();

            animateToolbar(getMatchingColor(CurrentFaceValue), getMatchingColor(0));
            animateCard(getMatchingColor(CurrentFaceValue), getMatchingColor(0));
            if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
                animateStatusBar(getMatchingColor(CurrentFaceValue), getMatchingColor(0));
            }

            CurrentFaceValue = 0;
			mCallback.onMazagGet(CurrentFaceValue);
			save_My_Mazag(CurrentFaceValue);
			update_Mazag_Card(CurrentFaceValue);
			edit_freqently_mazag_Question(CurrentFaceValue); // e1


			SetSelectedFAB(true);

			break;
		case R.id.loved:
            dlg.dismiss();

            animateToolbar(getMatchingColor(CurrentFaceValue), getMatchingColor(1));
            animateCard(getMatchingColor(CurrentFaceValue), getMatchingColor(1));
            if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
                animateStatusBar(getMatchingColor(CurrentFaceValue), getMatchingColor(1));
            }
			CurrentFaceValue = 1;
			mCallback.onMazagGet(CurrentFaceValue);

			save_My_Mazag(CurrentFaceValue);
			edit_freqently_mazag_Question(CurrentFaceValue); // e1
			update_Mazag_Card(CurrentFaceValue);

			SetSelectedFAB(true);

			break;
		case R.id.afraid:
            dlg.dismiss();

            animateToolbar(getMatchingColor(CurrentFaceValue), getMatchingColor(2));
            animateCard(getMatchingColor(CurrentFaceValue), getMatchingColor(2));
            if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
                animateStatusBar(getMatchingColor(CurrentFaceValue), getMatchingColor(2));
            }

			CurrentFaceValue = 2;
			mCallback.onMazagGet(CurrentFaceValue);
			save_My_Mazag(CurrentFaceValue);
			edit_freqently_mazag_Question(CurrentFaceValue); // e1
			update_Mazag_Card(CurrentFaceValue);

			SetSelectedFAB(true);

			break;
		case R.id.Angry:
            dlg.dismiss();

            animateToolbar(getMatchingColor(CurrentFaceValue), getMatchingColor(3));
            animateCard(getMatchingColor(CurrentFaceValue), getMatchingColor(3));
            if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
                animateStatusBar(getMatchingColor(CurrentFaceValue), getMatchingColor(3));
            }

			CurrentFaceValue = 3;
			mCallback.onMazagGet(CurrentFaceValue);
			save_My_Mazag(CurrentFaceValue);
			edit_freqently_mazag_Question(CurrentFaceValue); // e1
			update_Mazag_Card(CurrentFaceValue);

			SetSelectedFAB(true);

			break;

		case R.id.Sad:
            dlg.dismiss();

            animateToolbar(getMatchingColor(CurrentFaceValue), getMatchingColor(4));
            animateCard(getMatchingColor(CurrentFaceValue), getMatchingColor(4));
            if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
                animateStatusBar(getMatchingColor(CurrentFaceValue), getMatchingColor(4));
            }

			CurrentFaceValue = 4;
			mCallback.onMazagGet(CurrentFaceValue);
			save_My_Mazag(CurrentFaceValue);
			edit_freqently_mazag_Question(CurrentFaceValue); // e1
			update_Mazag_Card(CurrentFaceValue);

			SetSelectedFAB(true);

			break;

		case R.id.compressed:
            dlg.dismiss();

            animateToolbar(getMatchingColor(CurrentFaceValue), getMatchingColor(5));
            animateCard(getMatchingColor(CurrentFaceValue), getMatchingColor(5));
            if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
                animateStatusBar(getMatchingColor(CurrentFaceValue), getMatchingColor(5));
            }

			CurrentFaceValue = 5;
			mCallback.onMazagGet(CurrentFaceValue);
			save_My_Mazag(CurrentFaceValue);
			edit_freqently_mazag_Question(CurrentFaceValue); // e1
			update_Mazag_Card(CurrentFaceValue);

			SetSelectedFAB(true);

			break;

		case R.id.mabdoon:
            dlg.dismiss();

            animateToolbar(getMatchingColor(CurrentFaceValue), getMatchingColor(6));
            animateCard(getMatchingColor(CurrentFaceValue), getMatchingColor(6));
            if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
                animateStatusBar(getMatchingColor(CurrentFaceValue), getMatchingColor(6));
            }

			CurrentFaceValue = 6;
			mCallback.onMazagGet(CurrentFaceValue);
			save_My_Mazag(CurrentFaceValue);
			edit_freqently_mazag_Question(CurrentFaceValue); // e1
			update_Mazag_Card(CurrentFaceValue);

			SetSelectedFAB(true);

			break;
		case R.id.buttonDismiss:
			dlg.dismiss();
			break;
		case R.id.Refresh_button:

			btnRefresh.setAnimation(rotatee);
			if (VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN) {
				btnRefresh.postOnAnimation(new Runnable() {

					@Override
					public void run() {
						refreshhShette();

					}
				});
			} else {
				btnRefresh.postDelayed(new Runnable() {

					@Override
					public void run() {
						refreshhShette();

					}
				}, 16);
			}
			rotatee.setAnimationListener(new AnimationListener() {

				@Override
				public void onAnimationStart(Animation animation) {

					refreshhShette();

				}

				@Override
				public void onAnimationRepeat(Animation animation) {
					// TODO Auto-generated method stub
				}

				@Override
				public void onAnimationEnd(Animation animation) {
					// TODO Auto-generated method stub
				}
			});

			btnRefresh.startAnimation(rotatee);
			break;

		}

	}

	private void edit_freqently_mazag_Question(int currentFaceValue2) {

		getActivity();
		SharedPreferences mazag_sp = getActivity().getSharedPreferences(
				"mazzag_prefs", Context.MODE_PRIVATE);

		Editor mazEditor = mazag_sp.edit();

		mazEditor.putInt("currentFaceValue2", currentFaceValue2);

		mazEditor.commit();

		Alarms_Set_up mazag_instance = new Alarms_Set_up(getActivity());
		mazag_instance.Alarm_SetUp_Mood();

	}

	private void save_My_Mazag(int thecurrentFaceValue) {
		mazagy_SharedPreference = getActivity().getSharedPreferences(
				"Chosen_mazag", Context.MODE_PRIVATE);
		Editor editor = mazagy_SharedPreference.edit();

		editor.putInt("Chosen_mazag_id", thecurrentFaceValue);
		editor.commit();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		broadcastReceiver2 = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context2, Intent intent2) {
				Quote quoteinstance = new Quote();
				TvRandom.setText(intent2.getStringExtra("Sec_Key1"));
				TvRandom_Auth.setText(intent2.getStringExtra("Sec_Key2"));

				random_Image = quoteinstance.getAuthorImage(intent2
						.getStringExtra("Sec_Key2"));
				Author_pic_random.setImageBitmap((GetCroppedBitmap
						.getCroppedBitmap(Drawable_into_Bitmap
								.drawableToBitmap(random_Image))));

			}
		};
		getActivity()
				.registerReceiver(
						broadcastReceiver2,
						new IntentFilter(
								com.app.zad.work_in_background.Random_Quote_Service.Second_BROADCAST_ACTION));

		broadcastReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				Quote quoteinstance2 = new Quote();
				TvToday.setText(intent.getStringExtra("Key1"));
				TvToday_Auth.setText(intent.getStringExtra("Key2"));
				today_image = quoteinstance2.getAuthorImage(intent
						.getStringExtra("Key2"));
				Author_pic_today.setImageBitmap(GetCroppedBitmap
						.getCroppedBitmap(Drawable_into_Bitmap
								.drawableToBitmap(today_image)));

			}
		};
		getActivity()
				.registerReceiver(
						broadcastReceiver,
						new IntentFilter(
								com.app.zad.work_in_background.TodayQuote_Service.BROADCAST_ACTION));

	}

	@Override
	public void onDestroy() {
		getActivity().unregisterReceiver(broadcastReceiver);
		getActivity().unregisterReceiver(broadcastReceiver2);
		super.onDestroy();
	}

	public void Zabtly_Mazagy_WelNabi(int giveMeFace) {

		dlg = new Dialog(getActivity(), R.style.Dialog_Mazagoo);
		dlg.setContentView(R.layout.circle_layout);
		dlg.setTitle("Change Your Mode");

		Happy = (Button) dlg.findViewById(R.id.happy);
		Loved = (Button) dlg.findViewById(R.id.loved);
		Afraid = (Button) dlg.findViewById(R.id.afraid);
		Angry = (Button) dlg.findViewById(R.id.Angry);
		Sad = (Button) dlg.findViewById(R.id.Sad);
		Compressed = (Button) dlg.findViewById(R.id.compressed);
		Mabdoon = (Button) dlg.findViewById(R.id.mabdoon);
		Dismiss = (ImageView) dlg.findViewById(R.id.buttonDismiss);

		happy_big = (RelativeLayout) dlg.findViewById(R.id.happy_big);
		sad_big = (RelativeLayout) dlg.findViewById(R.id.sad_big);
		afraid_big = (RelativeLayout) dlg.findViewById(R.id.afraid_big);
		angry_big = (RelativeLayout) dlg.findViewById(R.id.angry_big);
		mabdoon_big = (RelativeLayout) dlg.findViewById(R.id.mabdoon_big);
		compress_big = (RelativeLayout) dlg.findViewById(R.id.compress_big);
		loved_big = (RelativeLayout) dlg.findViewById(R.id.loved_big);

		PreSelectedFace();

		SetSelectedFAB(false);
		Happy.setOnClickListener(this);
		Loved.setOnClickListener(this);
		Afraid.setOnClickListener(this);
		Angry.setOnClickListener(this);
		Sad.setOnClickListener(this);
		Compressed.setOnClickListener(this);
		Mabdoon.setOnClickListener(this);
		Dismiss.setOnClickListener(this);

		Window window = dlg.getWindow();
		WindowManager.LayoutParams wlp = window.getAttributes();

		wlp.gravity = Gravity.CENTER;
		wlp.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;

		window.setAttributes(wlp);
		dlg.setCanceledOnTouchOutside(true);
		dlg.show();
	}

	private void SetSelectedFAB(Boolean isAnime) {

		mazagy_SharedPreference = getActivity().getSharedPreferences(
				"Chosen_mazag", Context.MODE_PRIVATE);
		int z2 = mazagy_SharedPreference.getInt("Chosen_mazag_id", 0);
		int[] MazgaatFaceees = { R.drawable.face_happy_bas,
				R.drawable.face_loved_bas, R.drawable.face_afraid_bas,
				R.drawable.face_angry_bas, R.drawable.face_sad_bas,
				R.drawable.face_stressed_bas, R.drawable.face_bored_bas };

		MzagyFaceMutton.setImageResource(MazgaatFaceees[z2]);
		if (isAnime) {
			MzagyFaceMutton.startAnimation(grow_fade);
		}

	}

	private void PreSelectedFace() {
		Drawable[] MazgaatFaceees = {
				getActivity().getResources().getDrawable(R.drawable.face_happy),
				getActivity().getResources().getDrawable(R.drawable.face_loved),
				getActivity().getResources()
						.getDrawable(R.drawable.face_afraid),
				getActivity().getResources().getDrawable(R.drawable.face_angry),
				getActivity().getResources().getDrawable(R.drawable.face_sad),
				getActivity().getResources().getDrawable(
						R.drawable.face_stressed),
				getActivity().getResources().getDrawable(R.drawable.face_bored) };

		Drawable d = ContextCompat.getDrawable(getActivity(),
				R.drawable.add_schedule_fab_checked_default);
		// Drawable d2 = getResources().getDrawable(
		// R.drawable.add_schedule_fab_checked_black);

		mazagy_SharedPreference = getActivity().getSharedPreferences(
				"Chosen_mazag", Context.MODE_PRIVATE);
		int z = mazagy_SharedPreference.getInt("Chosen_mazag_id", 0);

		Drawable[] kaz = new Drawable[2];
		kaz[0] = d;
		// kaz[1] = d2;
		kaz[1] = MazgaatFaceees[z];

		floodBTN btd = new floodBTN(kaz);
		btd.startTransition(1000, 500);

		switch (z) {
		case 0:
			happy_big.setBackground(btd);

			break;
		case 1:
			loved_big.setBackground(btd);
			break;
		case 2:
			afraid_big.setBackground(btd);
			break;
		case 3:
			angry_big.setBackground(btd);
			break;
		case 4:
			sad_big.setBackground(btd);
			break;
		case 5:
			compress_big.setBackground(btd);
			break;
		case 6:
			mabdoon_big.setBackground(btd);
			break;

		default:
			break;
		}

	}

	public int getMatchingColor(int GiveMeNumber) {
		int[] MazgaatColors = {
				ContextCompat.getColor(getActivity(),R.color.yellow),
                ContextCompat.getColor(getActivity(),R.color.pink),
                ContextCompat.getColor(getActivity(),R.color.blue_grey),
                ContextCompat.getColor(getActivity(),R.color.red),
                ContextCompat.getColor(getActivity(),R.color.grey),
				ContextCompat.getColor(getActivity(),R.color.orange),
                ContextCompat.getColor(getActivity(),R.color.green),
                ContextCompat.getColor(getActivity(),R.color.white)

		};

		return MazgaatColors[GiveMeNumber];
	}

	public int MazagToColor(int GiveMeNumber) {
		int[] MazgaatColorsX = {
				getActivity().getResources().getColor(R.color.yellow_clicked),
				getActivity().getResources().getColor(R.color.pink_clicked),
				getActivity().getResources().getColor(R.color.blue_grey_clicked),
				getActivity().getResources().getColor(R.color.red_reverse),
				getActivity().getResources().getColor(R.color.grey_clicked),
				getActivity().getResources().getColor(R.color.orange_reverse),
				getActivity().getResources().getColor(R.color.green_clicked) };
		return MazgaatColorsX[GiveMeNumber];
	}



	private void Open_Zabtly_or_Not() {

		SharedPreferences getmoodPref = getActivity().getSharedPreferences(
				"open_zabtly_or_not_sharedpreferences", Context.MODE_PRIVATE);
		Boolean open_zabtly_decision = getmoodPref.getBoolean(
				"Zabatly_or_not_key", false);

		if (open_zabtly_decision) {

			SharedPreferences just_other_SharedPreference = getActivity()
					.getSharedPreferences("Chosen_mazag", Context.MODE_PRIVATE);
			int fin_value_of_faceid = just_other_SharedPreference.getInt(
					"Chosen_mazag_id", 6);

			Editor editor = getmoodPref.edit();

			editor.putBoolean("Zabatly_or_not_key", false);

			editor.commit();

			Zabtly_Mazagy_WelNabi(fin_value_of_faceid);

		}
	}

	private void refreshhShette() {
		Quote quoteinstance2 = new Quote();
		ArrayList<Quote> allQuotesObjects = quoteinstance2
				.getAllObjects(getActivity());

		int min = 0, max = allQuotesObjects.size() - 1;
		Random r = new Random();
		int ran_id = r.nextInt(max - min + 1) + min;

		Quote getRanQuote2 = allQuotesObjects.get(ran_id);
		final String RQuote = getRanQuote2.Quote;
		final String RAuthor = getRanQuote2.Author;
		String wiki_Refresh = getRanQuote2.getwiki(getActivity(), getRanQuote2);
		random_Image = getRanQuote2.getAuthorImage(RAuthor);
		Author_pic_random.startAnimation(fade_out);
		TvRandom.startAnimation(slideOut);
		TvRandom_Auth.startAnimation(slideOut);
		Author_pic_random.setImageBitmap(GetCroppedBitmap
				.getCroppedBitmap(Drawable_into_Bitmap
						.drawableToBitmap(random_Image)));
		TvRandom.setText(RQuote);
		TvRandom_Auth.setText(RAuthor);
		TvRandom.startAnimation(slideIn);
		TvRandom_Auth.startAnimation(slideIn);
		Author_pic_random.startAnimation(fade_In);

		@SuppressWarnings("static-access")
		SharedPreferences Sersharedpreferences = getActivity()
				.getSharedPreferences("MyPREFERENCES2",
						getActivity().MODE_PRIVATE);
		Editor editor2 = Sersharedpreferences.edit();

		editor2.putString("Skey1_2", RQuote);
		editor2.putString("Skey2_2", RAuthor);
		editor2.putString("Skey3_2", wiki_Refresh);

		editor2.commit();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_CANCELED) {
			getActivity().finish();

		}
		if (resultCode == Activity.RESULT_OK) {

			showHomeHelpCaseView();

		}

	}

	public void showHomeHelpCaseView() {
		showcaseView = new ShowcaseView.Builder(a)
				.setTarget(new ViewTarget(view.findViewById(R.id.faceButton)))
				.setOnClickListener(new ShowCaseViewOnClick()).build();
		showcaseView.setStyle(R.style.CustomShowcaseTheme);

		showcaseView.setButtonText(a.getString(R.string.next));
		showcaseView.setContentDescription(a.getString(R.string.help_1));
		showcaseView
				.setContentText(a.getString(R.string.choose_your_face_mood));
		showcaseView.setContentTitle(a.getString(R.string.change_your_mood));
		showcaseView.setShouldCentreText(false);
		showcaseView.setButtonText(a.getString(R.string.next));
		/*
		 * RelativeLayout.LayoutParams lps = new RelativeLayout.LayoutParams(
		 * ViewGroup.LayoutParams.WRAP_CONTENT,
		 * ViewGroup.LayoutParams.WRAP_CONTENT);
		 * lps.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		 * lps.addRule(RelativeLayout.ALIGN_PARENT_LEFT); int margin = ((Number)
		 * (getResources().getDisplayMetrics().density * 20)) .intValue();
		 * showcaseView.setButtonPosition(lps);
		 */

	}

	private void animateToolbar(int colorFrom,int colorTo){
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                actionBar.setBackgroundDrawable(new ColorDrawable((Integer) animation.getAnimatedValue()));
            }
        });
        colorAnimation.setDuration(1000);
        colorAnimation.setStartDelay(100);
        colorAnimation.start();
    }
    private void animateCard(int colorFrom,int colorTo){
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Color_layout.setBackground(new ColorDrawable((Integer) animation.getAnimatedValue()));
            }
        });
        colorAnimation.setDuration(1000);
        colorAnimation.setStartDelay(100);
        colorAnimation.start();
    }

    @RequiresApi(api = VERSION_CODES.LOLLIPOP)
    private void animateStatusBar(int colorFrom,int colorTo){

        final Window window = getActivity().getWindow();
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                window.setStatusBarColor((Integer) animation.getAnimatedValue());
            }
        });
        colorAnimation.setDuration(1000);
        colorAnimation.setStartDelay(100);
        colorAnimation.start();

    }

}
