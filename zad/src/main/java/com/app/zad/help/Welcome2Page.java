package com.app.zad.help;

import java.util.ArrayList;

import com.app.zad.helpUI.Welcome2Fragemnt;

import android.support.v4.app.Fragment;

public class Welcome2Page extends Page{

	public Welcome2Page(ModelCallbacks callbacks, String title) {
		super(callbacks, title);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Fragment createFragment() {
		// TODO Auto-generated method stub
		return Welcome2Fragemnt.create(getKey());
	}

	@Override
	public void getReviewItems(ArrayList<ReviewItem> dest) {
		// TODO Auto-generated method stub
		
	}

}
