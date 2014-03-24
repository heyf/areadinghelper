package me.heyf.areadinghelper.activity;

import java.sql.SQLException;

import me.heyf.areadinghelper.R;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class BookPreview extends BaseBookDetail {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ac_book_preview);
		
		t = (TextView) findViewById(R.id.book_preview_title);
		b = (Button) findViewById(R.id.book_preview_button);
		detailImageView = (ImageView) findViewById(R.id.book_preview_image);
				
		b.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				try {
					bookDao.create(book);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					Intent i = new Intent(BookPreview.this,Main.class);
					startActivity(i);					
				}
			}
			
		});
	}
}
