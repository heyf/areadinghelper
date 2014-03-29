package me.heyf.areadinghelper.activity;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import me.heyf.areadinghelper.R;
import me.heyf.areadinghelper.model.Read;
import me.heyf.areadinghelper.view.BookDetailView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;

public class BookDetail extends BaseBookDetail {
	
	static final int CUSTOM_RED = 0xffff4444;
	static final int CUSTOM_GREEN = 0xff99CC00;
	
	static final int STANDING_BY = 1;
	static final int READING = 2;
	
	int flag = 1;
	
	int chosenId = -1;
	
	Button buttonSubmit;
	Dao<Read, Integer> readDao = null;
	
	List<Read> reads = new ArrayList<Read>();
	BaseAdapter la;
	
	Read read;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ac_book_detail);
		bookDetailView = (BookDetailView) this.findViewById(R.id.book_detail);
		
		buttonSubmit = (Button) this.findViewById(R.id.button_book_detail_submit);
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
		
		la = new BaseAdapter(){
			
			class ViewHolder {
				public TextView comment;
				public TextView month;
				public TextView day_of_month;
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
					holder.month = (TextView) convertView.findViewById(R.id.read_month);
					holder.day_of_month = (TextView) convertView.findViewById(R.id.read_day_of_month);
					holder.length = (TextView) convertView.findViewById(R.id.read_length);
					convertView.setTag(holder);
				} else {
					holder = (ViewHolder) convertView.getTag();
				}
				holder.comment.setText(reads.get(position).comment);
				holder.month.setText(reads.get(position).getMonthString());
				holder.day_of_month.setText(reads.get(position).getDayString());				
				holder.length.setText(reads.get(position).getLengthString());
				
				return convertView;
			}
			
		};
		
		ListView l = (ListView) findViewById(android.R.id.list);
		l.setAdapter(la);

		l.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long id) {
				Intent i = new Intent(BookDetail.this,EditRead.class);
				i.putExtra("comment",reads.get(position).comment);
				i.putExtra("read_position",position);
				startActivityForResult(i,200);
			}
			
		});

	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		int position = data.getIntExtra("read_position", -1);
		String comment = data.getStringExtra("comment");
		if(position>-1){
			reads.get(position).comment = comment;
			try {
				readDao.update(reads.get(position));
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				la.notifyDataSetChanged();
			}
		} else {
			// TODO invalid 
		}

	}

	void onButtonInit(){
		buttonSubmit.setBackgroundColor(CUSTOM_GREEN);
		buttonSubmit.setText(R.string.book_detail_add_read);
		flag = STANDING_BY;
	}

	@Override
	protected void onStart() {
		super.onStart();
		try {
			readDao = doh.getReadDao();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		refresh();
	}
	
	public void refresh(){
		try {
			//reads = readDao.queryForEq("book_id", book);
			QueryBuilder<Read,Integer> qb = readDao.queryBuilder();
			qb.orderBy("read_id", false);
			qb.where().eq("book_id",book);
			reads = qb.query();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			la.notifyDataSetChanged();
		}
	}

}
