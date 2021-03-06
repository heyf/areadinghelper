package me.heyf.areadinghelper.model;

import java.util.Calendar;
import java.util.Date;

import java.text.DateFormat;

import android.text.Html;
import android.text.Spanned;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName="read")
public class Read {
	@DatabaseField(generatedId = true,columnName="read_id")
	private int id;
	@DatabaseField(foreign = true)
	public Book book;
	@DatabaseField
	public String comment;
//	@DatabaseField
//	public int type;
	
	@DatabaseField
	public int page_read;
	@DatabaseField
	private long startTime;
	@DatabaseField
	private long length;
	
	private Calendar startDate = null;
	private static final String[] MONTHS = new String[] { "Jan", "Feb", "Mar", "Apr", "May", "Jun", 
		"Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };
	
	public Read(){};
	
	public Read(Book mBook){
		this.book = mBook;
		this.comment = "暂无评论";
		this.startTime = System.currentTimeMillis();
		this.page_read = 0;
		this.length = 0;
		return;
	}
	
	public void startReading(){
		this.startTime = System.currentTimeMillis();
		return;
	}
	
	public void finishReading(){
		this.length = System.currentTimeMillis() - startTime;
		return;
	}
	
	public void leaveComment(String mComment){
		this.comment = mComment;
		return;
	}
	
	public int getId(){
		return id;
	}
		
//	public int getType(){
//		return type;
//	}
//	
//	public void setType(int type){
//		this.type = type;
//		return;
//	}
	
	public void set(Book mBook, String mComment, long mStartTime, long mLength){
		this.book = mBook;
		this.comment = mComment;
		this.startTime = mStartTime;
		this.length = mLength;
		return;
	}
	
	public long getStartTime(){
		return startTime;
	}
	
	public long getLength(){
		return length;
	}
	
	public String getLengthString(){
		int sec = (int) length / 1000;
		int min = sec / 60;
		int hour = min / 60;
		String lengthString = sec + " 秒";
		if(min>0) {
			lengthString = min + " 分  ";
		}
		if(hour>0){
			lengthString = hour + " 小时 " + lengthString;
		}
		
		return lengthString;
	}
	
	public String getMonthString(){
		Calendar c = getStartDate();
		int month = c.get(Calendar.MONTH);
		return MONTHS[month];
	}
	
	public String getDayString(){
		Calendar c = getStartDate();
		int day = c.get(Calendar.DAY_OF_MONTH);
		return Integer.toString(day);
	}
	
	public String getDateString(){
		Date d = new Date(startTime);
		DateFormat sdf = DateFormat.getDateInstance();
		return sdf.format(d);
	}
	
	private Calendar getStartDate(){
		if(startDate==null){
			startDate = Calendar.getInstance();
			startDate.setTimeInMillis(startTime);
		}
		return startDate;
	}

	public String getPageString() {
		return Integer.toString(page_read);
	}

	public Spanned getComment() {
		String html = "<font color=\"#99CC00\">"+page_read+"页</font> "+comment;
		return Html.fromHtml(html);
	}
}
