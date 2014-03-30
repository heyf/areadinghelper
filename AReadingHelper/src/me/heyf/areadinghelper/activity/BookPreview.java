package me.heyf.areadinghelper.activity;

import java.sql.SQLException;

import me.heyf.areadinghelper.R;
import me.heyf.areadinghelper.widget.BookDetailView;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class BookPreview extends BaseBookDetail {
	
	Button b;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ac_book_preview);
		bookDetailView = (BookDetailView) this.findViewById(R.id.book_preview);
		
		b = (Button) this.findViewById(R.id.book_preview_button);
				
		b.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				try {
					bookDao.create(book);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					BookPreview.this.setResult(RESULT_OK);
					BookPreview.this.finish();
				}
			}
			
		});
		
		bookDetailView.setPageReadViewInvisible();
		
	}
}
