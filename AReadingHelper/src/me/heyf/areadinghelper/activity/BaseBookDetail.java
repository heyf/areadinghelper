package me.heyf.areadinghelper.activity;

import java.sql.SQLException;

import me.heyf.areadinghelper.R;
import me.heyf.areadinghelper.model.Book;
import me.heyf.areadinghelper.utils.DatabaseOpenHelper;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

public class BaseBookDetail extends Activity {

	TextView t;
	Button b; 
	ImageView detailImageView;
		
	Book book;
	
	DatabaseOpenHelper doh = null;
	Dao<Book, Integer> bookDao = null;
	
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	
	DisplayImageOptions options;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		Intent i = getIntent();
		book = i.getParcelableExtra("book");
		t.setText(book.name);
		imageLoader.displayImage(book.image_url, detailImageView, options);
	}
	
	
	
	

}
