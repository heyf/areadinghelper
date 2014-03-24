package me.heyf.areadinghelper.model;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName="book")
public class Book implements Parcelable {

	@DatabaseField(generatedId = true)
	private int id;
	@DatabaseField
	public String name;
	@DatabaseField
	public String image_url;
	@DatabaseField
	private int douban_id;
	@DatabaseField
	public String author;
	@DatabaseField
	public String publisher;
	@DatabaseField
	public int pages;
	@DatabaseField
	public String comment;
	@DatabaseField
	public int current_state;
	
	//plan
	@DatabaseField
	public int plan_type;
	@DatabaseField
	public int page_read;
//	@DatabaseField
//	public long plan_time;
	
	//runtime
	
	public Book(){};
	
	public Book(JSONObject bookJSON){
		try {
			name = bookJSON.getString("title");
			image_url = bookJSON.getString("image");
			author = bookJSON.getJSONArray("author").getString(0);
			douban_id = bookJSON.getInt("id");
			Log.d("parse_douban id", Integer.toString(douban_id));
			publisher = bookJSON.getString("publisher");
			//pages = Integer.parseInt(bookJSON.getString("pages"));
			pages = bookJSON.getInt("pages");
			plan_type = 0;
			page_read = 0;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static final Parcelable.Creator<Book> CREATOR = new Creator<Book>(){

		@Override
		public Book createFromParcel(Parcel source) {
			Book mBook = new Book();
			mBook.id = source.readInt();
			mBook.name = source.readString();
			mBook.image_url = source.readString();
			mBook.author = source.readString();
			mBook.douban_id = source.readInt();
			mBook.publisher = source.readString();
			mBook.comment = source.readString();
			mBook.pages = source.readInt();
			mBook.current_state = source.readInt();
			mBook.plan_type = source.readInt();
			mBook.page_read = source.readInt();
			//mBook.plan_time = source.readLong();
			return mBook;
		}

		@Override
		public Book[] newArray(int size) {
			return null;
		}
	
	};
	
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeString(name);
		dest.writeString(image_url);
		dest.writeString(author);
		dest.writeInt(douban_id);
		dest.writeString(publisher);
		dest.writeString(comment);
		dest.writeInt(pages);
		dest.writeInt(current_state);
		dest.writeInt(plan_type);
		dest.writeInt(page_read);
		//dest.writeLong(plan_time);
	}

	public int getId(){
		return id;
	}
	
	public int getDoubanId(){
		return douban_id;
	}
}