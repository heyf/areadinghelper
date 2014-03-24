package me.heyf.areadinghelper.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName="read")
public class Read {
	@DatabaseField(generatedId = true)
	private int id;
	@DatabaseField(foreign = true)
	public Book book;
	@DatabaseField
	public String comment;
//	@DatabaseField
//	public int type;
	
	@DatabaseField
	private long startTime;
	@DatabaseField
	private long length;
	
	public Read(){};
	
	public Read(Book mBook){
		this.book = mBook;
		this.comment = "ÔÝÎÞÆÀÂÛ";
		this.startTime = System.currentTimeMillis();
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
}
