package com.app.zad.helpUI;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.app.zad.R;

public class Welcome3Fragment extends Fragment {
	private static final String ARG_KEY = "key";
	static EditText firstname;
	static EditText secondname;
	static Editable firstText;
	static Editable secText;
	public static SharedPreferences sharedpreferences;
	public static SharedPreferences User_Genre_sp;
	static Boolean genre_boolean;

	static RadioGroup rGroup;
	static RadioButton checkedRadioButton;

	public static Welcome3Fragment create(String key) {
		Bundle args = new Bundle();
		args.putString(ARG_KEY, key);

		Welcome3Fragment fragment = new Welcome3Fragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.helper_form, container, false);
		firstname = (EditText) view.findViewById(R.id.editText1);
		secondname = (EditText) view.findViewById(R.id.EditText01);
		sharedpreferences = getActivity().getSharedPreferences("firstlastname",
				Context.MODE_PRIVATE);
		User_Genre_sp = getActivity().getSharedPreferences("USER_GENRE",
				Context.MODE_PRIVATE);

		// This will get the radiogroup
		rGroup = (RadioGroup) view.findViewById(R.id.male_female);

		//Rashwan
		rGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {

				case R.id.CheckedTextView01:
					// male
					genre_boolean = true;

					break;

				case R.id.Radio_X_yeah:
					// female
					genre_boolean = false;

					break;
				}
				Editor editor = User_Genre_sp.edit();
				editor.clear();
				editor.putBoolean("m_or_f_key", genre_boolean);
				editor.commit();
			}
		});

	
		
		firstname.setText(sharedpreferences.getString("firstname", ""));
		secondname.setText(sharedpreferences.getString("secondname", ""));
		RadioButton rbu1 = (RadioButton)view.findViewById(R.id.CheckedTextView01);
        RadioButton rbu2 = (RadioButton)view.findViewById(R.id.Radio_X_yeah);
        if (User_Genre_sp.getBoolean("m_or_f_key", true) == true) {
			rbu1.setChecked(true);
		} else {
			rbu2.setChecked(true);
		}
		

		return view;
	}

	public static void saveNames(Context context) {
		sharedpreferences = context.getSharedPreferences("firstlastname",
				Context.MODE_PRIVATE);

		firstText = firstname.getText();
		String firststring = firstText.toString();

		secText = secondname.getText();
		String secstring = secText.toString();
		Log.i("savefirst", secstring);
		Log.i("savese", firststring);
		SharedPreferences.Editor prefEditor = sharedpreferences.edit();
		prefEditor.putString("firstname", firststring);
		prefEditor.putString("secondname", secstring);
		prefEditor.commit();
		
		//Saving the name in the Settings' SharedPreference
		SharedPreferences setings = context.getSharedPreferences("com.app.zad_preferences",Context.MODE_PRIVATE);
		Editor editor = setings.edit();
		editor.putString("FirstName", firststring);
		editor.putString("LastName", secstring);
		editor.commit();

	}

	

}