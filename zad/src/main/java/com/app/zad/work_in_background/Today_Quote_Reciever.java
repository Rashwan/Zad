package com.app.zad.work_in_background;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class Today_Quote_Reciever extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Intent service1 = new Intent(context, TodayQuote_Service.class);
		context.startService(service1);

	}
}
