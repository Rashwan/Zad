package com.app.zad.widgetPremium;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.app.zad.R;

public class ChooseUpdateFrequency extends Fragment {
	private static final String ARG_KEY = "key";
	private int appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
	private Context context;
	private static final String PREFS_NAME = "com.app.zad.ui.MainActivity";
	public static final String FREQ_KEY = "freq_key";
	public static EditText freqEditText;
	public final static long MINIUTE_VALUE = 60000;
	public final static long DEFAULTE_VALUE = 1800000;
	

	
	public static ChooseUpdateFrequency create(String key) {
		Bundle args = new Bundle();
		args.putString(ARG_KEY, key);

		ChooseUpdateFrequency fragment = new ChooseUpdateFrequency();
		fragment.setArguments(args);
		return fragment;
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.widget_choose_time,
				container, false);

		freqEditText = (EditText) view.findViewById(R.id.freq_edit_widget);
		try {
			long freq = loadFreq(context, appWidgetId);
			freqEditText.setText(freq / MINIUTE_VALUE + "");
		} catch (Exception e) {
			freqEditText.setText(DEFAULTE_VALUE / MINIUTE_VALUE + "");
		}

		return view;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	public static void saveFreq(Context context, int appWidgetId, int freq) {
		SharedPreferences.Editor prefs = context.getSharedPreferences(
				PREFS_NAME, 0).edit();
		prefs.putLong(FREQ_KEY + appWidgetId, freq * MINIUTE_VALUE);
		prefs.commit();
	}

	static long loadFreq(Context context, int appWidgetId) {
		SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
		long freq = prefs.getLong(FREQ_KEY + appWidgetId, DEFAULTE_VALUE);
		return freq;
	}
}