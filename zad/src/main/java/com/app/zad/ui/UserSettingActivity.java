package com.app.zad.ui;

import java.util.Calendar;
import java.util.GregorianCalendar;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.app.zad.R;
import com.app.zad.work_in_background.Alarms_Set_up;

public class UserSettingActivity extends PreferenceActivity implements
		OnSharedPreferenceChangeListener {
	public static final String KEY_PREF_NOTIFICATION = "notificationPref";
	public static final String KEY_PREF_MORNING = "morningKey";
	public static final String KEY_PREF_NIGHT = "nightKey";
	public static final String KEY_PREF_MODE = "modeKey";
	public static final String KEY_PREF_MORNING_EVENING = "morningEvinengNotificationPref";
	public static final String KEY_PREF_MODES = "modesNotificationPref";
	public static final String KEY_PREF_FIRST_NAME = "FirstName";
	public static final String KEY_PREF_LAST_NAME = "LastName";
	public static final String KEY_PREF_GENDER = "Gender";

	public static String sharedPrefsName;
	public AlarmManager alarmManager;
	public AlarmManager alarmManager2;
	SharedPreferences alarmsSharedPreferences;
	SharedPreferences alarmsSharedPreferences2;
	private SharedPreferences genrepref;

	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.user_settings);
		// ActionBar ab = getActionBar();
		// KAZAKY FX Yeah
		Window window;

		if (android.os.Build.VERSION.SDK_INT >= 21) {
			window = getWindow();
			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			window.setStatusBarColor(getResources().getColor(
					R.color.Purple_Deep_soft_light));
		}
		// KAZAKY FX Yeah

		// getActionBar().setHomeButtonEnabled(true);

		/*
		 * getActionBar().setDisplayHomeAsUpEnabled(true);
		 * getActionBar().setIcon( new ColorDrawable(getResources().getColor(
		 * android.R.color.transparent))); if (android.os.Build.VERSION.SDK_INT
		 * >= 18) { ab.setHomeAsUpIndicator(getResources().getDrawable(
		 * R.drawable.up_drawable_layer)); }
		 */
		sharedPrefsName = this.getPreferenceManager()
				.getSharedPreferencesName();
		Log.i("SETTINGSNAME", sharedPrefsName);

		genrepref = getSharedPreferences("USER_GENRE", Context.MODE_PRIVATE);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.empty_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (item.getItemId() == android.R.id.home) {
			onBackPressed();
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {

		if (key.equals(KEY_PREF_NOTIFICATION)) {

			if (!getBoolean(KEY_PREF_NOTIFICATION, false)) {

				Alarms_Set_up cancel_MorEve_Alarms = new Alarms_Set_up(this);
				cancel_MorEve_Alarms.Cancel_Morning_Evening_Alarms();

				Alarms_Set_up cancel_Mode_Alarm = new Alarms_Set_up(this);
				cancel_Mode_Alarm.Cancel_Mode_Alarm();
				;

			}

			else {
				checkPastState();

			}
		}

		else if (key.equals(KEY_PREF_MORNING_EVENING)) {

			if (!getBoolean(KEY_PREF_MORNING_EVENING, false)) {

				Alarms_Set_up cancel_MorEve_Alarms2 = new Alarms_Set_up(this);
				cancel_MorEve_Alarms2.Cancel_Morning_Evening_Alarms();

			} else {

			}
		} else if (key.equals(KEY_PREF_MORNING)) {
			changeMorningeTime();

		} else if (key.equals(KEY_PREF_NIGHT)) {
			changeEveningTime();

		} else if (key.equals(KEY_PREF_MODES)) {
			if (!getBoolean(KEY_PREF_MODES, false)) {

				Alarms_Set_up cancel_Mode_Alarm2 = new Alarms_Set_up(this);
				cancel_Mode_Alarm2.Cancel_Mode_Alarm();

			} else {

				Alarms_Set_up start_Mode_Alarm = new Alarms_Set_up(this);
				start_Mode_Alarm.Alarm_SetUp_Mood();

			}
			// 03/07/2015
		} else if (key.equals(KEY_PREF_MODE)) {
			changeModeTime();

		}
		else if (key.equals(KEY_PREF_FIRST_NAME)) {

			String newFirstName = updateFirstName();
			SharedPreferences namepref = getSharedPreferences("firstlastname",
					Context.MODE_PRIVATE);
			Editor editor = namepref.edit();
			editor.putString("firstname", newFirstName);
			editor.commit();

			Log.i("FNAME", newFirstName);

		} else if (key.equals(KEY_PREF_LAST_NAME)) {
			String newLastName = updateLastName();
			Log.i("LNAME", newLastName);

			SharedPreferences namepref = getSharedPreferences("firstlastname",
					Context.MODE_PRIVATE);
			Editor editor = namepref.edit();
			editor.putString("secondname", newLastName);
			editor.commit();

		} else if (key.equals(KEY_PREF_GENDER)) {
			@SuppressWarnings("deprecation")
			ListPreference listPreference = (ListPreference) findPreference(KEY_PREF_GENDER);
			String currValue = listPreference.getValue();
			Log.i("Man", currValue);
			Boolean is_man = Boolean.valueOf(currValue);
			Editor editor = genrepref.edit();
			editor.clear();
			editor.putBoolean("m_or_f_key", is_man);
			editor.commit();
			updateGender();
		}
	}

	private void checkPastState() {
		if (getBoolean(KEY_PREF_MODES, false)) {
			Alarms_Set_up start_Mode_Alarm = new Alarms_Set_up(this);
			start_Mode_Alarm.Alarm_SetUp_Mood();

		}
		if (getBoolean(KEY_PREF_MORNING_EVENING, false)) {
			changeMorningeTime();
			changeEveningTime();
		}
	}

	// If user changes morning time
	private void changeMorningeTime() {
		// Amr code with small modification

		Calendar cal_now = Calendar.getInstance();
		cal_now.setTimeInMillis(System.currentTimeMillis());

		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		long millisMorning = prefs.getLong("morningKey", 0);

		Calendar cal1 = GregorianCalendar.getInstance();
		cal1.setTimeInMillis(millisMorning);
		cal1.set(Calendar.SECOND, 0);

		// --------------------------------

		// Put Cal1 value in SharedPreferences to get it in service.

		alarmsSharedPreferences = getSharedPreferences("Exact_Time_Services",
				MODE_PRIVATE);

		Editor settingsEditor = alarmsSharedPreferences.edit();

		settingsEditor.putInt("Exact_Time_Services_MornService_get_hr",
				cal1.get(Calendar.HOUR_OF_DAY));

		settingsEditor.putInt("Exact_Time_Services_MornService_get_min",
				cal1.get(Calendar.MINUTE));

		settingsEditor.commit();

		Alarms_Set_up a1 = new Alarms_Set_up(this);

		a1.Alarm_SetUpMorningQuote();

	}

	private void changeEveningTime() {

		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		long millisNight = prefs.getLong("nightKey", 0);

		Calendar cal2 = GregorianCalendar.getInstance();
		cal2.setTimeInMillis(millisNight);
		cal2.set(Calendar.SECOND, 0);

		alarmsSharedPreferences2 = getSharedPreferences("Exact_Time_Services",
				MODE_PRIVATE);

		Editor settingsEditor2 = alarmsSharedPreferences2.edit();

		settingsEditor2.putInt("Exact_Time_Services_EvenService_get_hr",
				cal2.get(Calendar.HOUR_OF_DAY));

		settingsEditor2.putInt("Exact_Time_Services_EvenService_get_min",
				cal2.get(Calendar.MINUTE));

		settingsEditor2.commit();

		Alarms_Set_up a2 = new Alarms_Set_up(this);

		a2.Alarm_SetUpEveningQuote();

	}
	
	private void changeModeTime() {

		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		long millisMode = prefs.getLong("modeKey", 0);

		Calendar cal3 = GregorianCalendar.getInstance();
		cal3.setTimeInMillis(millisMode);
		cal3.set(Calendar.SECOND, 0);

		alarmsSharedPreferences2 = getSharedPreferences("mazzag_prefs",
				MODE_PRIVATE);

		Editor settingsEditor3 = alarmsSharedPreferences2.edit();

		settingsEditor3.putInt("Exact_Time_Services_MazagService_get_hr",
				cal3.get(Calendar.HOUR_OF_DAY));

		settingsEditor3.putInt("Exact_Time_Services_MazagService_get_min",
				cal3.get(Calendar.MINUTE));

		settingsEditor3.commit();

		Alarms_Set_up a3 = new Alarms_Set_up(this);

		a3.Alarm_SetUp_Mood();
		Log.i("NEW",Calendar.HOUR_OF_DAY+"");
		Log.i("NEW",Calendar.MINUTE+"");


	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onResume() {
		super.onResume();
		getPreferenceManager().getSharedPreferences()
				.registerOnSharedPreferenceChangeListener(this);
		updateFirstName();
		updateLastName();
		updateGender();

	}

	private String updateFirstName() {
		@SuppressWarnings("deprecation")
		Preference preference = findPreference(KEY_PREF_FIRST_NAME);

		EditTextPreference editTextPreference = (EditTextPreference) preference;
		String newFirstName = editTextPreference.getText();
		if (newFirstName.trim().length() > 0) {
			editTextPreference.setSummary(newFirstName);
		} else {
			editTextPreference.setSummary(R.string.enterFirstName);
		}
		return newFirstName;
	}

	private String updateLastName() {
		@SuppressWarnings("deprecation")
		Preference preference = findPreference(KEY_PREF_LAST_NAME);
		EditTextPreference editTextPreference = (EditTextPreference) preference;
		String newLastName = editTextPreference.getText();
		if (newLastName.trim().length() > 0) {
			editTextPreference.setSummary(newLastName);
		} else {
			editTextPreference.setSummary(R.string.enterLastName);
		}
		return newLastName;
	}

	private Boolean updateGender() {
		@SuppressWarnings("deprecation")
		ListPreference ListPreference = (ListPreference) findPreference(KEY_PREF_GENDER);

		Boolean is_male = genrepref.getBoolean("m_or_f_key", true);
		Log.i("VALUES", is_male + "");

		if (is_male) {
			ListPreference.setValue(is_male.toString());
			ListPreference.setSummary(getString(R.string.boy));
		} else {
			ListPreference.setValue(is_male.toString());
			ListPreference.setSummary(getString(R.string.girl));
		}
		return is_male;
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onPause() {
		super.onPause();
		getPreferenceManager().getSharedPreferences()
				.unregisterOnSharedPreferenceChangeListener(this);
	}

	// get user chooses
	public boolean getBoolean(String key, boolean defValue) {
		SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());

		return settings.getBoolean(key, defValue);
	}
}
