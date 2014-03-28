package me.heyf.areadinghelper.activity;

import me.heyf.areadinghelper.R;
import me.heyf.areadinghelper.model.Book;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class AddBook extends BaseBookList {

		@Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.ac_add_book);
	        
	        Button b = (Button)findViewById(R.id.main_button);
	        b.setOnClickListener(new OnClickListener(){
	    		@Override
	    		public void onClick(View arg0) {
	    			EditText et = (EditText) findViewById(R.id.editText1);
	    			String query = et.getText().toString();
	    			//Log.d("query",query);
	    			AsyncHttpClient client = new AsyncHttpClient();
	    			String url = "http://api.douban.com/v2/book/search?q="+ query;
	    			client.get(url, new AsyncHttpResponseHandler(){
	    				@Override
	    				public void onSuccess(String response) {
	    					books.clear();
	    					try {
	    						JSONObject jo = new JSONObject(response);
	    						int count = jo.getInt("count");
	    						booksArray = jo.getJSONArray("books");
	    						for(int i=0;i<count;i++){
	    							Book book = new Book(booksArray.getJSONObject(i));
	    							books.add(book);
	    						}
	    						ia.notifyDataSetChanged();
	    					} catch (JSONException e) {
	    						e.printStackTrace();
	    					}
	    				}
	    			});
	    		}
	        });
	        			
			//query list
			ListView l = (ListView) findViewById(android.R.id.list);
			l.setAdapter(ia);
			l.setOnItemClickListener(new OnItemClickListener(){
				@Override
				public void onItemClick(AdapterView<?> arg0, View v, int position, long id) {
					Intent i = new Intent(AddBook.this,BookPreview.class);
					i.putExtra("book",books.get(position));
					startActivityForResult(i, 1);
				}
			});

	    }

		@Override
		protected void onActivityResult(int requestCode, int resultCode,
				Intent data) {
			// TODO Auto-generated method stub
			super.onActivityResult(requestCode, resultCode, data);
			AddBook.this.setResult(RESULT_OK);
			AddBook.this.finish();
		}
		
		
}
