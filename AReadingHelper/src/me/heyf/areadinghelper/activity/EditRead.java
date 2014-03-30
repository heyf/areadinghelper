package me.heyf.areadinghelper.activity;

import me.heyf.areadinghelper.R;
import android.app.Activity;
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

public class EditRead extends Activity {
	
	EditText et;
	Button b;
	
	int read_position;
	String comment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ac_edit_comment);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
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
