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
	@DatabaseField
	public int type;
	
	@DatabaseField
	private long startTime;
	@DatabaseField
	private long length;
	
	public Read(){};
	
	public Read(Book book, String comment, int type){
		this.comment = comment;
		this.type = type;
		this.startTime = System.currentTimeMillis();
		return;
	}
	
	public void finishReading(){
		this.length = System.currentTimeMillis() - startTime;
		return;
	}
	
	public int getId(){
		return id;
	}
		
	public int getType(){
		return type;
	}
	
	public void setType(int type){
		this.type = type;
		return;
	}
	
	public long getStartTime(){
		return this.startTime;
	}
	
	public void setStartTime(long startTime){
		this.startTime = startTime;
		return;
	}
	
	public long getLength(){
		return this.length;
	}
	
	public void setLength(long length){
		this.length = length;
		return;
	}
}
