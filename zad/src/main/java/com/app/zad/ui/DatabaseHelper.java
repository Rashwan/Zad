package com.app.zad.ui;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
	private static final String DATABASE_NAME = "Zad";
	private static final int DATABASE_VERSION = 3;
	static final String DATABASE_PATH = "/data/data/com.app.zad/databases/";
	private Dao<Quote, Integer> QuoteDao = null;
	private RuntimeExceptionDao<Quote, Integer> QuoteRuntimeDao = null;
	private Dao<Wikis, Integer> WikiDao = null;
	private RuntimeExceptionDao<Wikis, Integer> WikiRuntimeDao = null;
	private Context mContext;
	
	
	public DatabaseHelper(Context context) {
		super(context, DATABASE_PATH + DATABASE_NAME, null, DATABASE_VERSION);
		this.mContext = context;
		SharedPreferences Hmprefs = PreferenceManager
				.getDefaultSharedPreferences(mContext);
			Boolean dbExist = checkDatabase();
			if (dbExist) {
				if (DATABASE_VERSION > Hmprefs.getInt("firstTimeDatabase", 1)) {
					onUpgrade();
					SharedPreferences.Editor editor = Hmprefs.edit();
					editor.putInt("firstTimeDatabase", DATABASE_VERSION);
					editor.commit();
				}
			} else {
				File f = new File(DATABASE_PATH);
				if (!f.exists()) {
					f.mkdir();
					copyDatabase();
				}
			
			}
			
		}
	

	@Override
	public void onCreate(SQLiteDatabase database,
			ConnectionSource connectionSource) {
		/*Boolean dbExist = checkDatabase();
		Log.i("databasecreate", "seqe");
		if (dbExist) {
		} else {
			File f = new File(DATABASE_PATH);
			if (!f.exists()) {
				f.mkdir();
				copyDatabase();
			}
		}*/
		
	}

	
	public void onUpgrade() {
		Log.i("database update", "to " + DATABASE_VERSION);
		try {
			File db = new File(DATABASE_PATH+DATABASE_NAME);
			db.delete();
			File databasefile = new File(DATABASE_PATH);
			databasefile.mkdir();
			copyDatabase();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	//not working 
	/*@Override
	public void onDowngrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		Log.i("database downgrade", "from " + newVersion + "to " + oldVersion);
		File dbs = new File(DATABASE_PATH+DATABASE_NAME);
		dbs.delete();
		File databasefile = new File(DATABASE_PATH);
		databasefile.mkdir();
		copyDatabase();
	}*/
	

	public Dao<Quote, Integer> getQuoteDao() throws SQLException {
		if (QuoteDao == null) {
			QuoteDao = getDao(Quote.class);
		}
		return QuoteDao;
	}

	public Dao<Wikis, Integer> getWikiDao() throws SQLException {
		if (WikiDao == null) {
			WikiDao = getDao(Wikis.class);
		}
		return WikiDao;
	}

	public RuntimeExceptionDao<Quote, Integer> getQuoteRuntimeExceptionDao()
			throws SQLException {
		if (QuoteRuntimeDao == null) {
			QuoteRuntimeDao = getRuntimeExceptionDao(Quote.class);
		}
		return QuoteRuntimeDao;
	}

	public RuntimeExceptionDao<Wikis, Integer> getWikiRuntimeExceptionDao()
			throws SQLException {
		if (WikiRuntimeDao == null) {
			WikiRuntimeDao = getRuntimeExceptionDao(Wikis.class);
		}
		return WikiRuntimeDao;
	}

	private boolean checkDatabase() {
		boolean checkdb = false;

		String myPath = DATABASE_PATH + DATABASE_NAME;
		File dbfile = new File(myPath);
		checkdb = dbfile.exists();

		Log.i(DatabaseHelper.class.getName(), "DB Exist : " + checkdb);

		return checkdb;
	}

	private void copyDatabase() {
		try {
			InputStream myinput = mContext.getAssets().open(DATABASE_NAME);
			String outfilename = DATABASE_PATH + DATABASE_NAME;
			Log.i(DatabaseHelper.class.getName(), "DB Path : " + outfilename);
			OutputStream myoutput = new FileOutputStream(outfilename);
			byte[] buffer = new byte[1024];
			int length;
			while ((length = myinput.read(buffer)) > 0) {
				myoutput.write(buffer, 0, length);
			}
			myoutput.flush();
			myoutput.close();
			myinput.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, ConnectionSource arg1, int arg2,
			int arg3) {
		// TODO Auto-generated method stub
		
	}
}