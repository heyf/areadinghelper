package me.heyf.areadinghelper.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import me.heyf.areadinghelper.R;
import me.heyf.areadinghelper.model.Book;

import org.json.JSONArray;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class BaseBookList extends Activity {
		
	ImageLoader imageLoader = ImageLoader.getInstance();
	
	DisplayImageOptions options = new DisplayImageOptions.Builder()
	.showStubImage(R.drawable.ic_stub)
	.showImageForEmptyUri(R.drawable.ic_empty)
	.showImageOnFail(R.drawable.ic_error)
	.cacheInMemory(true)
	.cacheOnDisc(true)
	.displayer(new RoundedBitmapDisplayer(20))
	.build();;
	
	List<Book> books = new ArrayList<Book>();
	JSONArray booksArray = new JSONArray();
	
	MainListAdapter ia = new MainListAdapter();
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageLoader.init(ImageLoaderConfiguration.createDefault(BaseBookList.this));
        
    	options = new DisplayImageOptions.Builder()
    	.showStubImage(R.drawable.ic_stub)
    	.showImageForEmptyUri(R.drawable.ic_empty)
    	.showImageOnFail(R.drawable.ic_error)
    	.cacheInMemory(true)
    	.cacheOnDisc(true)
    	.displayer(new RoundedBitmapDisplayer(20))
    	.build();
	}

	class MainListAdapter extends BaseAdapter {
		private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

		private class ViewHolder {
			public TextView author;
			public TextView title;
			public ImageView image;
			
		}

		@Override
		public int getCount() {
			return books.size();
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
		public View getView(final int position, View convertView, ViewGroup parent) {
			View view = convertView;
			final ViewHolder holder;
			if (convertView == null) {
				view = getLayoutInflater().inflate(R.layout.item_book, parent, false);
				holder = new ViewHolder();
				holder.image = (ImageView) view.findViewById(R.id.image);
				holder.title = (TextView) view.findViewById(R.id.title);
				holder.author = (TextView) view.findViewById(R.id.author);
				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}

			holder.title.setText(books.get(position).name);
			holder.author.setText(books.get(position).author);

			imageLoader.displayImage(books.get(position).image_url, holder.image, options, animateFirstListener);

			return view;
		}
					
	}
	
	private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

		static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					FadeInBitmapDisplayer.animate(imageView, 500);
					displayedImages.add(imageUri);
				}
			}
		}
	}
}
