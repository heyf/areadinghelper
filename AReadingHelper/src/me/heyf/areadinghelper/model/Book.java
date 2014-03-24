package me.heyf.areadinghelper.model;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

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
	public int douban_id;
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
	
	//runtime
	
	public Book(){};
	
	public Book(JSONObject bookJSON){
		try {
			name = bookJSON.getString("title");
			image_url = bookJSON.getString("image");
			author = bookJSON.getJSONArray("author").getString(0);
			douban_id = bookJSON.getInt("id");
			publisher = bookJSON.getString("publisher");
			pages = bookJSON.getInt("pages");
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
	}

	public int getId(){
		return id;
	}
}
