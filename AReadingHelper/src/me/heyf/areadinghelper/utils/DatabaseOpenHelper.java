package me.heyf.areadinghelper.utils;

import java.sql.SQLException;

import me.heyf.areadinghelper.model.Book;
import me.heyf.areadinghelper.model.Read;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class DatabaseOpenHelper extends OrmLiteSqliteOpenHelper {
	
	private static final String DATABASE_NAME = "areadinghelper.db"; 
	private static final int DATABASE_VERSION = 9;
	
	private Dao<Book, Integer> bookDao = null;
	private Dao<Read, Integer> readDao = null;

	public DatabaseOpenHelper(Context context) { 
		  super(context, DATABASE_NAME, null, DATABASE_VERSION); 
		} 
	
	@Override
	public void onCreate(SQLiteDatabase arg0, ConnectionSource arg1) {
		try {
			TableUtils.createTable(connectionSource, Book.class);
			TableUtils.createTable(connectionSource, Read.class);
		} catch (SQLException e) {
			e.printStackTrace(); 
		}
	}

	public void onUpgrade(SQLiteDatabase db, ConnectionSource cs, int oldVersion,
			int newVersion) {
		try {
			TableUtils.dropTable(cs, Book.class, true);
			TableUtils.dropTable(cs, Read.class, true);
			onCreate(db,cs);
		} catch (SQLException e) {
			e.printStackTrace(); 
		}		

	}
	
	public Dao<Book, Integer> getBookDao() throws SQLException {
		if ( bookDao == null) {
			bookDao = getDao(Book.class);
		}
		return bookDao;
	}
	
	public Dao<Read, Integer> getReadDao() throws SQLException {
		if ( readDao == null) {
			readDao = getDao(Read.class);
		}
		return readDao;
	}

}
