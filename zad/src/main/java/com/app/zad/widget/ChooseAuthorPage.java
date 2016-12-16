package com.app.zad.widget;

import java.util.ArrayList;

import android.support.v4.app.Fragment;

import com.app.zad.help.ModelCallbacks;
import com.app.zad.help.Page;
import com.app.zad.help.ReviewItem;
import com.app.zad.help.SingleFixedChoicePage;

public class ChooseAuthorPage extends SingleFixedChoicePage {
	public ChooseAuthorPage(ModelCallbacks callbacks, String title) {
		super(callbacks, title);
	}

	@Override
	public Fragment createFragment() {
		return ChooseAuthor.create(getKey());
	}

	@Override
	public void getReviewItems(ArrayList<ReviewItem> dest) {
		StringBuilder sb = new StringBuilder();

		ArrayList<String> selections = mData
				.getStringArrayList(Page.SIMPLE_DATA_KEY);
		if (selections != null && selections.size() > 0) {
			for (String selection : selections) {
				if (sb.length() > 0) {
					sb.append(", ");
				}
				sb.append(selection);
			}
		}

		dest.add(new ReviewItem(getTitle(), sb.toString(), getKey()));
	}

	@Override
	public boolean isCompleted() {
		ArrayList<String> selections = mData
				.getStringArrayList(Page.SIMPLE_DATA_KEY);
		return selections != null && selections.size() > 0;
	}
}
