package com.app.zad.helpUI;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.app.zad.R;

public class Welcome2Fragemnt extends Fragment {
	private static final String ARG_KEY = "key";

	public static Welcome2Fragemnt create(String key) {
		Bundle args = new Bundle();
		args.putString(ARG_KEY, key);

		Welcome2Fragemnt fragment = new Welcome2Fragemnt();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.helper_mood, container, false);

		ImageView meego = (ImageView) view.findViewById(R.id.image_Faces);
		meego.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

		return view;
	}
}
