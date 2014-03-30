package me.heyf.areadinghelper.widget;

import me.heyf.areadinghelper.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class BookDetailView extends RelativeLayout {
	
	public ImageView book_image;
	private TextView book_title;
	private TextView book_author;
	private TextView book_page;
	private TextView book_page_read;

	public BookDetailView(Context context) {
		super(context);
	}
	
	public BookDetailView(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater inflater = (LayoutInflater) 
				context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.view_bookdetail, this);
		book_image = (ImageView) findViewById(R.id.book_detail_image);
		book_title = (TextView) this.findViewById(R.id.book_detail_title);
		book_author = (TextView) this.findViewById(R.id.book_detail_author);
		book_page = (TextView) this.findViewById(R.id.book_detail_page);
		book_page_read = (TextView) this.findViewById(R.id.book_detail_page_read);
	}

	public void setTitle(String text){
		book_title.setText(text);
	}
	
	public void setAuthor(String text){
		book_author.setText(text);
	}
	
	public void setPage(int page){
		book_page.setText(Integer.toString(page));
	}
	
	public void setPageRead(int page_read){
		book_page_read.setText(Integer.toString(page_read));
	}
	
	public void setPageReadViewInvisible(){
		book_page_read.setVisibility(INVISIBLE);
	}
}
