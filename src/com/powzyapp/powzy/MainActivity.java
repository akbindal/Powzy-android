package com.powzyapp.powzy;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;

public class MainActivity extends BaseActivity {
	
	
	
	//UI
	ImageButton discover;
	ImageButton taskBtn;
	ImageButton feedBtn;
	
	FrameLayout frameLayout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		setupUI();
		discover.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(MainActivity.this, Brand.class);				
				startActivity(i);
			}
		});
		
		//discover.setOnClickListener(new OnClickListner() {)
		//frameLayout.get
	}
	
	public void setupUI() {
		discover = (ImageButton) findViewById(R.id.first_discover);      
		taskBtn = (ImageButton) findViewById(R.id.task_btn);
		feedBtn = (ImageButton) findViewById(R.id.feed_btn);
		
		frameLayout = (FrameLayout) findViewById(R.id.bottom_framelayout);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
