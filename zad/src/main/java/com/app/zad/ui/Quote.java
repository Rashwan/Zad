package com.app.zad.ui;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StreamCorruptedException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.app.zad.widget.ChooseAuthor;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.stmt.UpdateBuilder;

public class Quote {
	@DatabaseField(id = true)
	public int ID;

	@DatabaseField
	public String Author;

	@DatabaseField
	public String Quote;

	@DatabaseField
	public Integer Category;

	@DatabaseField
	public Integer Mood;

	@DatabaseField
	public Integer Author_Image;

	@DatabaseField
	public Integer Important;

	public String getQuote() {
		return Quote;
	}

	public void setQuote(String quote) {
		Quote = quote;
	}

	public String getAuthor() {
		return Author;
	}

	public void setAuthor(String author) {
		Author = author;
	}

	public Quote() {
	}

	public Quote(int iD, String author, String quote, Integer category,
			Integer mood, Integer author_Image, Integer important) {
		super();
		ID = iD;
		Author = author;
		Quote = quote;
		Category = category;
		Mood = mood;
		Author_Image = author_Image;
		Important = important;
	}

	private DatabaseHelper dbHelper = null;

	private Context context;

	public static final String AUTHOR_FIELD_NAME = "Author";

	public DatabaseHelper getHelper(Context mContext) {
		if (dbHelper == null) {
			dbHelper = (DatabaseHelper) OpenHelperManager.getHelper(mContext,
					DatabaseHelper.class);
		}
		return dbHelper;
	}

	public Boolean updateFavourite(Context mContext, String Quote, int value)
			throws SQLException {
		RuntimeExceptionDao<Quote, Integer> QuoteDao = null;
		try {
			QuoteDao = getHelper(mContext).getQuoteRuntimeExceptionDao();
		} catch (SQLException e) {

			e.printStackTrace();
		}
		UpdateBuilder<Quote, Integer> updateBuilder = QuoteDao.updateBuilder();
		updateBuilder.updateColumnValue("Fav", value);
		updateBuilder.where().eq("Quote", Quote);
		updateBuilder.update();
		return true;
	}

	public String getwiki(Context mContext, Quote quote) {
		RuntimeExceptionDao<Wikis, Integer> WikiDao = null;
		try {
			WikiDao = getHelper(mContext).getWikiRuntimeExceptionDao();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		String Author_Name = quote.Author;
		List<Wikis> wikiObject = WikiDao.queryForEq("Author_Name", Author_Name);

		String wiki;
		try {
			wiki = wikiObject.get(0).Author_Wiki;
		} catch (Exception e) {
			wiki = "";
			e.printStackTrace();
		}
		return wiki;
	}

	public ArrayList<Quote> getAllObjects(Context mContext) {
		RuntimeExceptionDao<Quote, Integer> QuoteDao = null;
		try {
			QuoteDao = getHelper(mContext).getQuoteRuntimeExceptionDao();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		List<Quote> all = QuoteDao.queryForAll();
		OpenHelperManager.releaseHelper();
		ArrayList<Quote> allQuotesObjects = new ArrayList<Quote>(all);
		return allQuotesObjects;
	}

	public ArrayList<Quote> getAnObjects(Context mContext, String field,
			String value) {
		RuntimeExceptionDao<Quote, Integer> QuoteDao = null;
		try {
			QuoteDao = getHelper(mContext).getQuoteRuntimeExceptionDao();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		List<Quote> all = QuoteDao.queryForEq(field, value);
		OpenHelperManager.releaseHelper();
		ArrayList<Quote> allQuotesObjects = new ArrayList<Quote>(all);
		return allQuotesObjects;
	}

	public ArrayList<Quote> getAnObjects(Context mContext, String field,
			Integer value) {
		RuntimeExceptionDao<Quote, Integer> QuoteDao = null;
		try {
			QuoteDao = getHelper(mContext).getQuoteRuntimeExceptionDao();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		List<Quote> all = QuoteDao.queryForEq(field, value);
		OpenHelperManager.releaseHelper();
		ArrayList<Quote> allQuotesObjects = new ArrayList<Quote>(all);
		return allQuotesObjects;
	}

	public ArrayList<Quote> getAllObjectsForField(Context mContext, String field) {
		RuntimeExceptionDao<Quote, Integer> QuoteDao = null;
		try {
			QuoteDao = getHelper(mContext).getQuoteRuntimeExceptionDao();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		List<Quote> all = null;
		try {
			all = QuoteDao.queryBuilder().distinct().selectColumns(field)
					.query();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		OpenHelperManager.releaseHelper();
		ArrayList<Quote> allQuotesObjects = new ArrayList<Quote>(all);
		return allQuotesObjects;
	}

	public ArrayList<String> idToAuthor(ArrayList<Integer> authorsId)
			throws StreamCorruptedException, FileNotFoundException, IOException {
		Map<String, ArrayList<String>> map = null;
		ArrayList<String> authors = null;
		try {
			map = ChooseAuthor.map;
			authors = new ArrayList<String>();
			ArrayList<String> mapmod = map.get("authors");
			for (int i = 0; i < authorsId.size(); i++) {
				authors.add(mapmod.get(authorsId.get(i)));
			}
		} catch (Exception exception) {
			final ArrayList<Quote> allAuthors = getAllObjectsForField(context,
					Author);
			final int AUTHORS_SIZE = allAuthors.size();
			for (int i = 0; i < AUTHORS_SIZE; i++) {
				authors.add(allAuthors.get(i).Author);
			}
			map.put("authors", authors);
			authors = new ArrayList<String>();
			ArrayList<String> mapmod = map.get("authors");
			for (int i = 0; i < authorsId.size(); i++) {
				authors.add(mapmod.get(authorsId.get(i)));
			}
		}

		return authors;
	}

	public ArrayList<Quote> getauthors(Context mContext, Boolean autknown)
			throws SQLException {

		RuntimeExceptionDao<Quote, Integer> QuoteDao = null;
		try {
			QuoteDao = getHelper(mContext).getQuoteRuntimeExceptionDao();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		ArrayList<Quote> all;
		if (autknown) {
			List<Quote> alllist = QuoteDao.queryBuilder().where()
					.notIn("Author_Image", 1600).query();

			all = new ArrayList<Quote>(alllist);
		} else {
			List<Quote> alllist = QuoteDao.queryBuilder().where()
					.in("Author_Image", 1600).query();
			all = new ArrayList<Quote>(alllist);
		}

		return all;
	}

	public Drawable getAuthorImage(String author_name) {
		
		Drawable author_image = Magic_Activity.autortopic.get(author_name);
		if (author_image == null) {
			
			author_image = Magic_Activity.autortopic.get("الأصمعي");
		}
		return author_image;
	}

	@Override
	public String toString() {
		return "Quote [ID=" + ID + ", Author=" + Author + ", Quote=" + Quote
				+ ", Category=" + Category + ", Mood=" + Mood
				+ ", Author_Image=" + Author_Image + ", Important=" + Important
				+ "]";
	}
}