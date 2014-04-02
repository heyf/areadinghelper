package me.heyf.areadinghelper.activity;

import java.sql.SQLException;

import me.heyf.areadinghelper.R;
import me.heyf.areadinghelper.model.Book;
import me.heyf.areadinghelper.utils.DatabaseOpenHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

public class BookPreview extends BaseActivity {
	
	//Views
	Button buttonSubmit;
	ImageView image;
	TextView title;
	TextView viewAuthor;
	TextView moreDetail;
	
	//Database
	DatabaseOpenHelper doh = null;
	Dao<Book, Integer> bookDao = null;
	
	DoubanDetail dd = new DoubanDetail();
	
	ImageLoader imageLoader;
	DisplayImageOptions options;
	
	private class DoubanDetail {
		public String title;
		public String author;
		public String image_url;
		public String publisher;
		public int pages;
		public int douban_id;
		
		public boolean parseJSON(String jsonString){
			if(jsonString==null){
				return false;
			}
			try {
				JSONObject json = new JSONObject(jsonString);
				title = json.getString("title");
				image_url = json.getString("image");
				JSONArray authorArray = json.getJSONArray("author");
				int count = authorArray.length();
				author = "";
				for(int i = 0; i < count; i++){
					author += authorArray.getString(i);
				}
				publisher = json.getString("publisher");
				pages = json.getInt("pages");
				douban_id = json.getInt("id");				
			} catch (JSONException e) {
				e.printStackTrace();
				return false;
			}
			return true;
		}
		
		public Spanned getMoreDetail() {
			String html = "";
			html += addPair("³ö°æÉç",publisher)+"<br/>";
			html += addPair("Ò³Êý",pages)+"<br/>";
			html += addPair("¶¹°êID",douban_id);
			return Html.fromHtml(html);
		}
		
		private String addPair(String name,String value){
			return "<font color=\"grey\">"+name+":</font>"+value;
		}
		
		private String addPair(String name,int value){
			return "<font color=\"grey\">"+name+":</font>"+value;
		}
		
		public Book toBook(){
			Book b = new Book();
			b.name = title;
			b.image_url = image_url;
			b.setDoubanId(douban_id);
			b.publisher = publisher;
			b.pages = pages;
			return b;
		}
	} 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ac_book_preview);
		
		buttonSubmit = (Button) this.findViewById(R.id.book_preview_button);
		image = (ImageView) this.findViewById(R.id.book_preview_image);
		title = (TextView) this.findViewById(R.id.book_preview_title);
		viewAuthor = (TextView) this.findViewById(R.id.book_preview_author);
		moreDetail = (TextView) this.findViewById(R.id.book_preview_more);
		
		imageLoader = ImageLoader.getInstance();
		
		options = new DisplayImageOptions.Builder()
		.showImageForEmptyUri(R.drawable.ic_empty)
		.showImageOnFail(R.drawable.ic_error)
		.resetViewBeforeLoading(true)
		.cacheOnDisc(true)
		.imageScaleType(ImageScaleType.EXACTLY)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.build();
		
		Intent i = getIntent();
		String bookJSONString = i.getStringExtra("bookJSON");
		
		if(!dd.parseJSON(bookJSONString)){
			BookPreview.this.setResult(INVALID_REQUEST);
			BookPreview.this.finish();
		}
		
		doh = OpenHelperManager.getHelper(this, DatabaseOpenHelper.class);
		try {
			bookDao = doh.getBookDao();
		} catch (SQLException e) {
			e.printStackTrace();
		}
						
		buttonSubmit.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				try {
					bookDao.create(dd.toBook());
				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					BookPreview.this.setResult(RESULT_OK);
					BookPreview.this.finish();
				}
			}
			
		});
				
	}

	@Override
	protected void onStart() {
		super.onStart();
		imageLoader.displayImage(dd.image_url, image, options);
		title.setText(dd.title);
		viewAuthor.setText(dd.author);
		moreDetail.setText(dd.getMoreDetail());
	}
	
}
