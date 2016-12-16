package com.app.zad.ui;

import android.content.Context;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.field.DatabaseField;

public class Wikis {

	@DatabaseField
	String Author_Name;

	@DatabaseField
	public
	String Author_Wiki;



	public Wikis() {
	}

	public Wikis(String author_Name, String author_Wiki) {
		super();
		Author_Name = author_Name;
		Author_Wiki = author_Wiki;
	}

	private DatabaseHelper dbHelper = null;

	public DatabaseHelper getHelper(Context mContext) {
		if (dbHelper == null) {
			dbHelper = (DatabaseHelper) OpenHelperManager.getHelper(mContext,
					DatabaseHelper.class);
		}
		return dbHelper;
	}

	@Override
	public String toString() {
		return "Wikis [Author_Name=" + Author_Name + ", Author_Wiki="
				+ Author_Wiki + "]";
	}

}
