package me.heyf.areadinghelper.activity;

import me.heyf.areadinghelper.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class AddReadComment extends Activity {
	
	EditText et;
	Button b;
	
	int read_position;
	String comment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ac_add_comment);
		
		et = (EditText) this.findViewById(R.id.edittext_add_comment);
		b = (Button) this.findViewById(R.id.button_add_comment_submit);
		
		Intent i = getIntent();
		read_position = i.getIntExtra("read_position", -1);
		comment = i.getStringExtra("comment");
		
		b.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				comment = et.getText().toString();
				Intent i = new Intent();
				i.putExtra("comment", comment);
				i.putExtra("read_position", read_position);
				AddReadComment.this.setResult(RESULT_OK, i);
				AddReadComment.this.finish();
			}
			
		});
		
		
		
	}
	
	

}
