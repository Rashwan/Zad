package com.app.zad.work_in_background;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class Random_Quote_Reciever extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {

		Intent service2 = new Intent(context, Random_Quote_Service.class);
		context.startService(service2);

	}

}
