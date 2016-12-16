package com.app.zad.helpUI;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.larvalabs.svgandroid.SVG;
import com.larvalabs.svgandroid.SVGParser;
import com.app.zad.R;


public class Welcome1Fragment extends Fragment {
	private static final String ARG_KEY = "key";

	public static Welcome1Fragment create(String key) {
		Bundle args = new Bundle();
		args.putString(ARG_KEY, key);

		Welcome1Fragment fragment = new Welcome1Fragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.helper_hey, container, false);

		ImageView meego = (ImageView) view.findViewById(R.id.image_logo);
		SVG svg = SVGParser.getSVGFromResource(getResources(), R.raw.zad);
		meego.setImageDrawable(svg.createPictureDrawable());
		meego.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

		return view;
	}
}
