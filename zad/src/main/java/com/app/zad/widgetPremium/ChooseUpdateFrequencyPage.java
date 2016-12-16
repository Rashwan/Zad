package com.app.zad.widgetPremium;

import java.util.ArrayList;

import android.support.v4.app.Fragment;

import com.app.zad.help.ModelCallbacks;
import com.app.zad.help.Page;
import com.app.zad.help.ReviewItem;

public class ChooseUpdateFrequencyPage extends Page {

	public static final String NAME_DATA_KEY = null;

	public ChooseUpdateFrequencyPage(ModelCallbacks callbacks, String title) {
		super(callbacks, title);
	}

	@Override
	public Fragment createFragment() {
		return ChooseUpdateFrequency.create(NAME_DATA_KEY);
	}

	@Override
	public void getReviewItems(ArrayList<ReviewItem> dest) {

	}
}
