package me.heyf.areadinghelper.activity;

import me.heyf.areadinghelper.R;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class BookDetail extends BaseBookDetail {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ac_book_detail);
		
		t = (TextView) findViewById(R.id.book_detail_title);
		b = (Button) findViewById(R.id.book_detail_button);
		detailImageView = (ImageView) findViewById(R.id.book_detail_image);
		
		Intent i = getIntent();
		book = i.getParcelableExtra("book");
				
//		b.setOnClickListener(new OnClickListener(){
//			@Override
//			public void onClick(View v) {
//				Intent i = new Intent(BookDetail.this,PlanList.class);
//				i.putExtra("book", book);
//				startActivityForResult(i, 1);
//			}
//		});
	}
}
