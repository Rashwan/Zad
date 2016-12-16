package com.app.zad.work_in_background;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class mood_Freq_Question_Reciever extends BroadcastReceiver {

	boolean from_app_check;

	@Override
	public void onReceive(Context context, Intent intent) {
		int AlarmValue = intent.getExtras().getInt("How_User_feels", 4);
		from_app_check = intent.getExtras().getBoolean(
				"Does_This_from_face_Button_or_alarmmanager", false);
		Intent ToMazagService = new Intent(context,
				mood_Freq_Question_Service.class);
		ToMazagService.putExtra("KeyToService", AlarmValue);
		ToMazagService.putExtra("Does_This_from_face_Button_or_alarmmanager",
				from_app_check);

		context.startService(ToMazagService);
	}
}