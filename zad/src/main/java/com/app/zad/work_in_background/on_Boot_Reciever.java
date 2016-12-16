package com.app.zad.work_in_background;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class on_Boot_Reciever extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {

		try {
			Alarms_Set_up hopa = new Alarms_Set_up(context);
			hopa.Alarm_SetUpMorningQuote();
			Alarms_Set_up hopa2 = new Alarms_Set_up(context);
			hopa2.Alarm_SetUpEveningQuote();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Alarms_Set_up hopa3 = new Alarms_Set_up(context);
		hopa3.Alarm_SetUp_TodayQuote();
		Alarms_Set_up hopa4 = new Alarms_Set_up(context);
		hopa4.Alarm_SetUp_RandomQuote();
		Alarms_Set_up hopa5 = new Alarms_Set_up(context);
		hopa5.Alarm_SetUp_Mood();
	}

	public boolean getBoolean(String key, boolean defValue, Context context) {

		SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(context);

		return settings.getBoolean(key, defValue);
	}
}