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
import android.widget.LinearLayout;

public class MainActivity extends BaseActivity {
	
	public int currentTab;
	
	public final static int FEED_TAB=0;
	public final static int STATUS_TAB=1;
	public final static int MISSION_TAB=2;
	
	//UI
	ImageButton discover;
	
	ImageButton taskBtn;
	ImageButton feedBtn;
	ImageButton statusBtn;
	
	LinearLayout feedLayout;
	LinearLayout missionLayout;
	LinearLayout statusLayout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		setupUI();
		discover.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(MainActivity.this, GymGame.class);				
				startActivity(i);
			}
		});
		
		taskBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				change_layouts(MISSION_TAB);
			}
		});
		
		feedBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				change_layouts(FEED_TAB);
			}
		});
		
		statusBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				change_layouts(STATUS_TAB);
			}
		});
		//discover.setOnClickListener(new OnClickListner() {)
		//frameLayout.get
	}
	
	public void setupUI() {
		currentTab = MISSION_TAB;
		discover = (ImageButton) findViewById(R.id.first_discover);      
		taskBtn = (ImageButton) findViewById(R.id.task_btn);
		feedBtn = (ImageButton) findViewById(R.id.feed_btn);
		statusBtn = (ImageButton) findViewById(R.id.status_btn);
		
		feedLayout = (LinearLayout) findViewById(R.id.home_feed);
		missionLayout = (LinearLayout) findViewById(R.id.home_mission);
		statusLayout = (LinearLayout) findViewById(R.id.home_status);
	}
	
	public void change_layouts(int selectedTab) {
		if(currentTab==selectedTab) return;
		//make currentTab invisible
		makeInvisible(currentTab);
		switch(selectedTab) {
			case STATUS_TAB:
				statusLayout.setVisibility(View.VISIBLE);
				currentTab=STATUS_TAB;
				break;
			case MISSION_TAB:
				missionLayout.setVisibility(View.VISIBLE);
				currentTab = MISSION_TAB;
				break;
			case FEED_TAB:
				feedLayout.setVisibility(View.VISIBLE);
				currentTab = FEED_TAB;
				break;
		}
		
	}
	
	public void makeInvisible(int currentTab) {
		switch(currentTab) {
			case STATUS_TAB:
				statusLayout.setVisibility(View.GONE);
				break;
			case MISSION_TAB:
				missionLayout.setVisibility(View.GONE);
				break;
			case FEED_TAB:
				feedLayout.setVisibility(View.GONE);
				break;
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
