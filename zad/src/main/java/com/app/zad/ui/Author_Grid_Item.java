package com.app.zad.ui;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;

public class Author_Grid_Item implements Comparable<Author_Grid_Item> {

	private final String Author_Title;
	private final Drawable Author_Pic;

	public Author_Grid_Item(String Author_Title, Drawable picsArray) {
		this.Author_Pic = picsArray;
		this.Author_Title = Author_Title;
	}

	public String getAuthor_Title() {
		return Author_Title;
	}

	public Drawable getAuthor_Pic() {
		return Author_Pic;
	}

	public int getCount() {
		return Author_Title.length();
	}

	public Object getItem(int position) {
		return null;
	}

	public long getItemId(int position) {
		return 0;
	}


	@Override
	public int compareTo(@NonNull Author_Grid_Item o) {
		return this.getAuthor_Title().compareTo(o.getAuthor_Title());
	}
}