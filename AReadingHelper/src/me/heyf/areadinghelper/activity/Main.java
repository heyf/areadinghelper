package me.heyf.areadinghelper.activity;

import java.sql.SQLException;

import me.heyf.areadinghelper.R;
import me.heyf.areadinghelper.model.Book;
import me.heyf.areadinghelper.utils.DatabaseOpenHelper;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

public class Main extends BaseBookList {

	private DatabaseOpenHelper doh = null;
	private Dao<Book, Integer> bookDao = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ac_book_list);

		doh = OpenHelperManager.getHelper(this, DatabaseOpenHelper.class);
		try {
			bookDao = doh.getBookDao();
			} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ListView l = (ListView) findViewById(R.id.book_list);
		l.setAdapter(ia);
		l.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int position, long id) {
				Intent i = new Intent(Main.this,BookDetail.class);
				i.putExtra("book", books.get(position));
				startActivity(i);
			}
		});
		
		refresh();
	}
		
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Toast toast = Toast.makeText(Main.this, R.string.add_book_success, Toast.LENGTH_SHORT);
		toast.show();
		refresh();
	}
    
	//≤Àµ•…Ë÷√

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
    	case R.id.action_refresh:
    		refresh();
    		return true;
    	case R.id.action_add_book:
    		gotoAddBook();
    		return true;
		default:
			return false;
    	}
    }
    
	private void refresh() {
		books.clear();
		try {
			books = bookDao.queryForAll();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ia.notifyDataSetChanged();
		return;
	}
	
	private void gotoAddBook(){
		Intent i = new Intent(Main.this,AddBook.class);
		startActivityForResult(i,1);
		return;
	}
}
