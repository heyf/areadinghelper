package me.heyf.areadinghelper.activity;

import me.heyf.areadinghelper.R;
import me.heyf.areadinghelper.model.Book;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class AddBook extends BaseBookList {
	
		TextView query_result;
		String query_string;
		Toast toast;

		@Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.ac_add_book);
	        
	        query_result = (TextView) findViewById(R.id.query_result);
	        query_result.setText("");

			ListView l = (ListView) findViewById(android.R.id.list);
			l.setAdapter(ia);
			l.setOnItemClickListener(new OnItemClickListener(){
				@Override
				public void onItemClick(AdapterView<?> arg0, View v, int position, long id) {
					Intent i = new Intent(AddBook.this,BookPreview.class);
					JSONObject bookJSON = null;
					try {
						bookJSON = booksArray.getJSONObject(position);
					} catch (JSONException e1) {
						e1.printStackTrace();
					}
					i.putExtra("bookJSON",bookJSON.toString());
					startActivityForResult(i, 1);
				}
			});
			
	    }
		
		@Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	        // Inflate the menu; this adds items to the action bar if it is present.
	        getMenuInflater().inflate(R.menu.add_book, menu);
	        SearchView searchItem = (SearchView) menu.findItem(R.id.menu_search).getActionView();
	        searchItem.setSubmitButtonEnabled(true);
	        searchItem.setIconified(false);
	        
	        searchItem.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
				
				@Override
				public boolean onQueryTextSubmit(String query) {
					query_string = query;
					toast = Toast.makeText(getApplicationContext(), R.string.onquery, Toast.LENGTH_LONG);
					toast.show();
					return search(query);
				}
				
				@Override
				public boolean onQueryTextChange(String newText) {
					return false;
				}
			});
	        return true;
	    }
		
		public boolean search(String query){
			AsyncHttpClient client = new AsyncHttpClient();
			String url = "http://api.douban.com/v2/book/search";
			RequestParams rp = new RequestParams();
			rp.put("q", query);
			client.get(url, rp, new AsyncHttpResponseHandler(){
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
					} catch (JSONException e) {
						e.printStackTrace();
					} finally {
						toast.cancel();
						if(books.size()==0){
							toast = Toast.makeText(getApplicationContext(), 
									getResources().getString(R.string.on_empty_result_prefix) +
									query_string + getResources().getString(R.string.on_empty_result_surfix), 
									Toast.LENGTH_LONG);
							toast.show();
						} else {
							toast = Toast.makeText(getApplicationContext(), R.string.on_have_result, Toast.LENGTH_SHORT);
							toast.show();
						}
						ia.notifyDataSetChanged();
					}
				}
			});
			return true;
		}
	    
		@Override
		protected void onActivityResult(int requestCode, int resultCode,
				Intent data) {
			super.onActivityResult(requestCode, resultCode, data);
			switch(resultCode){
			case RESULT_OK:
				AddBook.this.setResult(RESULT_OK);
				AddBook.this.finish();
				break;
			case INVALID_REQUEST:
				Toast.makeText(AddBook.this, R.string.invalid_request, Toast.LENGTH_SHORT).show();
				break;
			}

		}
}
