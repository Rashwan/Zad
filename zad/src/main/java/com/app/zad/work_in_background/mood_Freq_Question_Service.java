package com.app.zad.work_in_background;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import com.app.zad.R;
import com.app.zad.helper.Drawable_into_Bitmap;
import com.app.zad.ui.Magic_Activity;

public class mood_Freq_Question_Service extends Service {

	String TheUserMood;
	String theContentTitle;
	boolean from_Where;
	int last_Emotion_Icon;
	SharedPreferences User_Genre_sp;

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		try {
			from_Where = intent.getExtras().getBoolean(
					"Does_This_from_face_Button_or_alarmmanager", false);

		} catch (Exception e) {

			from_Where = false;

			e.printStackTrace();
		}

		if (from_Where) {
			Start_Mood_Question_Notification(intent);
		}
		return START_STICKY;
	}

	private void Start_Mood_Question_Notification(Intent intent) {
		SharedPreferences mood_shear = getSharedPreferences("firstlastname",
				MODE_PRIVATE);

		String mood_t = mood_shear.getString("firstname", "No_name");
		int user_mood_id = intent.getExtras().getInt("KeyToService");

		User_Genre_sp = getSharedPreferences("USER_GENRE", MODE_PRIVATE);

		boolean male_or_female = User_Genre_sp.getBoolean("m_or_f_key", true);
		// if true >> male
		// if false >> female

		// ----------------------

		// get suitable question to the user

		theContentTitle = Suitable_Question_to_user(male_or_female,
				user_mood_id, mood_t);

		// ------------

		last_Emotion_Icon = Suitable_mood_Icon_to_user(user_mood_id);
		Intent toHomeIntent = new Intent(this, Magic_Activity.class);
		toHomeIntent.putExtra("open_zabtly_or_not_Extras", true);
		PendingIntent mazag_contentIntent = PendingIntent.getActivity(this, 0,
				toHomeIntent, 0);
		NotificationCompat.Builder builder = new NotificationCompat.Builder(
				this);
		builder.setAutoCancel(true);
		builder.setContentTitle(theContentTitle);

		builder.setContentText(getString(R.string.press_change_your_mood));
		builder.setSmallIcon(R.drawable.ic_notif);
		// Shahawi Edited That 15-3-2015
		builder.setLargeIcon(Drawable_into_Bitmap
				.drawableToBitmap(ContextCompat.getDrawable(this,last_Emotion_Icon)));

		builder.setContentIntent(mazag_contentIntent);

		Notification notification = builder.build();
		NotificationManager manager = (NotificationManager) this
				.getSystemService(NOTIFICATION_SERVICE);
		manager.notify(7, notification);

	}

	private String Suitable_Question_to_user(Boolean user_Gendre,
			int user_mood_Num, String user_Name) {

		String question;

		if (user_Gendre) {

			// male

			switch (user_mood_Num) {
			case 0:
				question = getResources().getString(R.string.stay_happy_male)
						+ " " + user_Name;
				break;
			case 4:
				// sad
				question = getResources().getString(R.string.still_sad_male)
						+ " " + user_Name + getResources().getString(R.string.Qm);

				break;
			case 3:
				// angry
				question = getResources().getString(R.string.still_angry_male)
				+ " " + user_Name + getResources().getString(R.string.Qm);

				

				break;
			case 5:
				//pressed
				question = getResources().getString(R.string.still_pressed_male)
				+ " " + user_Name + getResources().getString(R.string.Qm);


				break;
			case 2:
				//scared
				question = getResources().getString(R.string.still_scared_male)
				+ " " + user_Name + getResources().getString(R.string.Qm);


				break;
			case 1:
				//inlove

				question = getResources().getString(R.string.still_inlove_male)
				+ " " + user_Name ;

				break;
			case 6:
				//bored
				question = getResources().getString(R.string.still_bored_male)
				+ " " + user_Name + getResources().getString(R.string.Qm);

				break;

			default:
				question = "None!";
				break;
			}

		} else {
			// female

			switch (user_mood_Num) {
			case 0:
				question = getResources().getString(R.string.stay_happy_female)
						+ " " + user_Name;
				break;
			case 4:
				// sad
				question = getResources().getString(R.string.still_sad_female)
						+ " " + user_Name + getResources().getString(R.string.Qm);

				break;
			case 3:
				// angry
				question = getResources().getString(R.string.still_angry_female)
				+ " " + user_Name +getResources().getString(R.string.Qm);

				

				break;
			case 5:
				//pressed
				question = getResources().getString(R.string.still_pressed_female)
				+ " " + user_Name + getResources().getString(R.string.Qm);


				break;
			case 2:
				//scared
				question = getResources().getString(R.string.still_scared_female)
				+ " " + user_Name ;


				break;
			case 1:
				//inlove

				question = getResources().getString(R.string.still_inlove_female)
				+ " " + user_Name + getResources().getString(R.string.Qm);

				break;
			case 6:
				//bored
				question = getResources().getString(R.string.still_bored_female)
				+ " " + user_Name + getResources().getString(R.string.Qm);

				break;

			default:
				question = "None!";
				break;


			}
		}
		return question;

	}

	private int Suitable_mood_Icon_to_user(int user_mood_Num) {

		int getIconId;

		switch (user_mood_Num) {

		case 0:
			getIconId = R.drawable.face_happy;

			break;
		case 4:
			getIconId = R.drawable.face_sad;

			break;
		case 3:
			getIconId = R.drawable.face_angry;

			break;
		case 5:
			getIconId = R.drawable.face_stressed;

			break;
		case 2:
			getIconId = R.drawable.face_afraid;

			break;
		case 1:
			getIconId = R.drawable.face_loved;

			break;
		case 6:
			getIconId = R.drawable.face_bored;

			break;

		default:
			getIconId = R.drawable.ic_launcher;

			break;
		}

		return getIconId;

	}

	@Override
	public void onDestroy() {

		super.onDestroy();
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

}
