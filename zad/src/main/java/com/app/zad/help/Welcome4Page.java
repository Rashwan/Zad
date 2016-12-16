package com.app.zad.help;

import java.util.ArrayList;

import com.app.zad.helpUI.Welcome4Fragment;

import android.support.v4.app.Fragment;

public class Welcome4Page extends Page {
	public Welcome4Page(ModelCallbacks callbacks, String title) {
		super(callbacks, title);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Fragment createFragment() {
		// TODO Auto-generated method stub
		return Welcome4Fragment.create(getKey());
	}

	@Override
	public void getReviewItems(ArrayList<ReviewItem> dest) {
		// TODO Auto-generated method stub
		
	}

}
