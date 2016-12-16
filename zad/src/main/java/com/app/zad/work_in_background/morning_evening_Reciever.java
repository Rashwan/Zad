package com.app.zad.work_in_background;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class morning_evening_Reciever extends BroadcastReceiver {

	boolean from_app_check;

	@Override
	public void onReceive(Context context, Intent intent) {

		String AlarmValue = intent.getStringExtra("keyToCast");

		from_app_check = intent.getExtras()
				.getBoolean("Does_This_right", false);

		Intent service1 = new Intent(context, morning_evening_Service.class);

		service1.putExtra("KeyToService", AlarmValue);

		service1.putExtra("Does_This_right", from_app_check);

		context.startService(service1);

	}

}
