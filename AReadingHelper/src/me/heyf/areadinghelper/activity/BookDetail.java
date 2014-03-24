package me.heyf.areadinghelper.activity;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import me.heyf.areadinghelper.R;
import me.heyf.areadinghelper.model.Read;
import me.heyf.areadinghelper.utils.BookDetailView;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.j256.ormlite.dao.Dao;

public class BookDetail extends BaseBookDetail {

	Button buttonAddRead;
	Button buttonFinishRead;
	Dao<Read, Integer> readDao = null;
	
	List<Read> reads = new ArrayList<Read>();
	BaseAdapter la;
	
	Read read;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ac_book_detail);
		bookDetailView = (BookDetailView) this.findViewById(R.id.book_detail);
		
		buttonAddRead = (Button) this.findViewById(R.id.button_book_detail_add_read);
		buttonAddRead.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				read = new Read(book);
				read.startReading();
				buttonAddRead.setVisibility(View.INVISIBLE);
				buttonFinishRead.setVisibility(View.VISIBLE);
			}
		});
		
		buttonFinishRead = (Button) this.findViewById(R.id.button_book_detail_finish_read);
		buttonFinishRead.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				read.finishReading();
				reads.add(read);
				la.notifyDataSetChanged();
				buttonAddRead.setVisibility(View.VISIBLE);
				buttonFinishRead.setVisibility(View.INVISIBLE);
			}
		});
		
		la = new BaseAdapter(){
			
			class ViewHolder {
				public TextView comment;
				public TextView startTime;
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
					holder.startTime = (TextView) convertView.findViewById(R.id.read_start_time);
					holder.length = (TextView) convertView.findViewById(R.id.read_length);
					convertView.setTag(holder);
				} else {
					holder = (ViewHolder) convertView.getTag();
				}
				holder.comment.setText(reads.get(position).comment);
				
				Date startDate = new Date(reads.get(position).getStartTime());
				SimpleDateFormat formatter = new SimpleDateFormat("MM月dd日 HH:mm", Locale.CHINA);
				holder.startTime.setText(formatter.format(startDate));
				
				Date lengthDate = new Date(reads.get(position).getLength());
				SimpleDateFormat lengthFormatter = new SimpleDateFormat("HH小时mm分ss秒",Locale.CHINA);
				holder.length.setText(lengthFormatter.format(lengthDate));
				
				return convertView;
			}
			
		};
		
		ListView l = (ListView) findViewById(android.R.id.list);
		l.setAdapter(la);
	}

	@Override
	protected void onStart() {
		super.onStart();
		try {
			readDao = doh.getReadDao();
			reads = readDao.queryForEq("book_id", book);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
}
