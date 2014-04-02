package me.heyf.areadinghelper.activity;

import me.heyf.areadinghelper.R;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditRead extends BaseActivity {
	
	EditText editPage;
	EditText editComment;
	Button b;
	
	int readPosition;
	int pageOfBook;
	int pageOfComment;
	String comment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ac_edit_comment);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		editComment = (EditText) this.findViewById(R.id.edittext_add_comment);
		editPage = (EditText) this.findViewById(R.id.edittext_add_page);
		b = (Button) this.findViewById(R.id.button_add_comment_submit);
		
		//Get intent extras
		Intent i = getIntent();
		readPosition = i.getIntExtra("read_position", -1);
		pageOfBook = i.getIntExtra("page_of_book", -1);
		pageOfComment = i.getIntExtra("page_of_comment", -1);
		comment = i.getStringExtra("comment");
		
		editComment.setText(comment);
		editPage.setText(Integer.toString(pageOfComment));
		
		b.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				
				comment = editComment.getText().toString();
				if(editPage.getText()==null){
					Toast.makeText(getApplicationContext(), "Ã»ÊäÈëÒ³Âë£¬Ç×", Toast.LENGTH_LONG).show();
					return;
				}
				pageOfComment = Integer.parseInt(editPage.getText().toString());
				if(pageOfComment<0||pageOfComment>pageOfBook){
					Toast.makeText(getApplicationContext(), "Ò³Âë²»¶Ô£¬Ç×", Toast.LENGTH_LONG).show();
					return;
				}
				Intent i = new Intent();
				i.putExtra("comment", comment);
				i.putExtra("read_position", readPosition);
				i.putExtra("page_of_comment", pageOfComment);
				
				EditRead.this.setResult(RESULT_OK, i);
				EditRead.this.finish();
			}
			
		});
		
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
		case android.R.id.home:
			showAlert();
			return true;
		}
		return super.onOptionsItemSelected(item);
				
	}
			
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			showAlert();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	public void showAlert(){
		AlertDialog.Builder builder = new AlertDialog.Builder(EditRead.this);
		builder.setMessage(R.string.disgard_changes);
		builder.setPositiveButton(R.string.ok,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						onReturn();
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
	
	public void onReturn(){
		EditRead.this.setResult(RESULT_CANCELED);
		EditRead.this.finish();
		return;
	}

}
