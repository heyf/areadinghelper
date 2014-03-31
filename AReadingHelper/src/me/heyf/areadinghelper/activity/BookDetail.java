package me.heyf.areadinghelper.activity;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import me.heyf.areadinghelper.R;
import me.heyf.areadinghelper.model.Book;
import me.heyf.areadinghelper.model.Read;
import me.heyf.areadinghelper.utils.DatabaseOpenHelper;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;

public class BookDetail extends Activity {
	
	//Constrains
	static final int CUSTOM_RED = 0xffff4444;
	static final int CUSTOM_GREEN = 0xff99CC00;
	
	static final int STANDING_BY = 1;
	static final int READING = 2;
	
	//VAR
	int flag = 1;
	int chosenId = -1;
	
	//Views
	Button buttonSubmit;
	ProgressBar progressBar;
	TextView pageText;
	TextView percentText;
	
	//Data
	DatabaseOpenHelper doh = null;
	Dao<Read, Integer> readDao = null;
	Dao<Book, Integer> bookDao = null;
	Book book;
	Read read;
	
	//list
	List<Read> reads = new ArrayList<Read>();
	BaseAdapter la;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ac_book_detail);
		
		Intent i = getIntent();
		book = i.getParcelableExtra("book");
		
		//bind views
		buttonSubmit = (Button) this.findViewById(R.id.button_book_detail_submit);
		progressBar = (ProgressBar) this.findViewById(R.id.progressbar_book_detail);
		pageText = (TextView) this.findViewById(R.id.textview_book_detail_pages);
		percentText = (TextView) this.findViewById(R.id.textview_book_detail_percent);
		
		//Database Actions
		doh = OpenHelperManager.getHelper(this, DatabaseOpenHelper.class);
		try {
			readDao = doh.getReadDao();
			bookDao = doh.getBookDao();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		//ActionBar		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		getActionBar().setTitle(book.name);
		
		
		//Reading Button
		buttonSubmit.setOnClickListener(new OnClickListener(){
					
			@Override
			public void onClick(View v) {
				switch(flag){
				case STANDING_BY:
					onStartReading();
					break;
				case READING:
					onFinishReading();
					break;
				default:
					onButtonInit();
					break;
				}
					
			}
			
			void onStartReading(){
				buttonSubmit.setBackgroundColor(CUSTOM_RED);
				buttonSubmit.setText(R.string.book_detail_finish_read);
				read = new Read(book);
				read.startReading();
				flag = READING;
			}
			
			void onFinishReading(){
				read.finishReading();
				reads.add(0,read);
				try {
					readDao.create(read);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					onButtonInit();
					la.notifyDataSetChanged();
				}
			}

		});
		onButtonInit();
		
		//List of Read
		la = new BaseAdapter(){
			
			class ViewHolder {
				public TextView comment;
				public TextView date;
				public TextView length;
			}

			@Override
			public int getCount() {
				return reads.size();
			}

			@Override
			public Object getItem(int position) {
				return position;
			}

			@Override
			public long getItemId(int position) {
				return position;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				final ViewHolder holder;
				if(convertView==null){
					convertView = getLayoutInflater().inflate(R.layout.item_read,parent,false);
					holder = new ViewHolder();
					holder.comment = (TextView) convertView.findViewById(R.id.read_comment);
					holder.date = (TextView) convertView.findViewById(R.id.read_date);
					holder.length = (TextView) convertView.findViewById(R.id.read_length);
					convertView.setTag(holder);
				} else {
					holder = (ViewHolder) convertView.getTag();
				}
				holder.comment.setText(reads.get(position).getComment());			
				holder.length.setText(reads.get(position).getLengthString());
				holder.date.setText(reads.get(position).getDateString());
				return convertView;
			}
			
		};
		
		ListView l = (ListView) findViewById(android.R.id.list);
		l.setAdapter(la);

		l.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long id) {
				
				goToEditComment(position);
				
			}
			
		});
						
		refreshBookView();
		
		new BookDetailRefreshTask().execute();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode!=RESULT_OK){
			return;
		}
		int position = data.getIntExtra("read_position", -1);
		String comment = data.getStringExtra("comment");
		int pageOfComment = data.getIntExtra("page_of_comment", -1);
		if(position>-1&&pageOfComment>-1){
			reads.get(position).comment = comment;
			reads.get(position).page_read = pageOfComment;
			if(book.page_read!=pageOfComment){
				book.page_read = pageOfComment;
				refreshBookView();
				try {
					bookDao.update(book);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			try {
				readDao.update(reads.get(position));
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				la.notifyDataSetChanged();
			}
		} else {
			// TODO invalid 
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.book_detail, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
		case R.id.action_delete_book:
			showAlert();
			return true;
		}
		return false;
	}

	public void showAlert(){
		AlertDialog.Builder builder = new AlertDialog.Builder(BookDetail.this);
		
		builder.setMessage(R.string.confirm_delete_book);
		
		builder.setPositiveButton(R.string.ok,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						deletebook();
					}
				});
		builder.setNegativeButton(R.string.cancel,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// User cancelled the dialog
					}
				});
		AlertDialog d = builder.create();
		d.show();
		return;
	}

	protected void goToEditComment(int position) {
		Intent i = new Intent(BookDetail.this,EditRead.class);
		i.putExtra("comment",reads.get(position).comment);
		i.putExtra("read_position",position);
		i.putExtra("page_of_book", book.pages);
		i.putExtra("page_of_comment", reads.get(position).page_read);
		startActivityForResult(i,200);
	}
	
	private void deletebook() {
		BookDetail.this.setResult(Main.DELETE_BOOK, new Intent().putExtra("book", book));
		BookDetail.this.finish();
	}
	
	void onButtonInit(){
		buttonSubmit.setBackgroundColor(CUSTOM_GREEN);
		buttonSubmit.setText(R.string.book_detail_add_read);
		flag = STANDING_BY;
	}
		
	private void refreshBookView(){
		int percentInt = book.page_read * 100 / book.pages;
		progressBar.setProgress( percentInt );
		pageText.setText(Integer.toString(book.page_read)+"/"+Integer.toString(book.pages));
		percentText.setText( percentInt +"%");
	}
	
	private class BookDetailRefreshTask extends AsyncTask<Void,Void,Void>{

		@Override
		protected Void doInBackground(Void... arg0) {
			try {
				//reads = readDao.queryForEq("book_id", book);
				QueryBuilder<Read,Integer> qb = readDao.queryBuilder();
				qb.orderBy("read_id", false);
				qb.where().eq("book_id",book);
				reads = qb.query();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			la.notifyDataSetChanged();
		}
		
	}
	
//	private class ReadingTimer extends AsyncTask<Void,Void,Void> {
//
//		@Override
//		protected Void doInBackground(Void... arg0) {
//			// TODO Auto-generated method stub
//			return null;
//		}
//		
//	}
	
}
