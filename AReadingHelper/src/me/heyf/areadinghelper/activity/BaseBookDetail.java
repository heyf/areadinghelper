package me.heyf.areadinghelper.activity;

import java.sql.SQLException;

import me.heyf.areadinghelper.R;
import me.heyf.areadinghelper.model.Book;
import me.heyf.areadinghelper.utils.DatabaseOpenHelper;
import me.heyf.areadinghelper.widget.BookDetailView;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

public class BaseBookDetail extends Activity {
	
	BookDetailView bookDetailView;
		
	Book book;
	
	DatabaseOpenHelper doh = null;
	Dao<Book, Integer> bookDao = null;

	ImageLoader imageLoader;
	DisplayImageOptions options;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		imageLoader = ImageLoader.getInstance();
		
		Intent i = getIntent();
		book = i.getParcelableExtra("book");

		options = new DisplayImageOptions.Builder()
		.showImageForEmptyUri(R.drawable.ic_empty)
		.showImageOnFail(R.drawable.ic_error)
		.resetViewBeforeLoading(true)
		.cacheOnDisc(true)
		.imageScaleType(ImageScaleType.EXACTLY)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.build();
		
		doh = OpenHelperManager.getHelper(this, DatabaseOpenHelper.class);
		try {
			bookDao = doh.getBookDao();
			} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		bookDetailView.setTitle(book.name);
		bookDetailView.setAuthor(book.author);
		bookDetailView.setPage(book.pages);
		bookDetailView.setPageRead(book.page_read);
		imageLoader.displayImage(book.image_url, bookDetailView.book_image, options);
	}
	
	
	
	

}
