package com.powzyapp.powzy;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class BaseActivity extends Activity{
	SlidingMenu slidginMenu;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		/***adding sliding menu****/
		slidginMenu = new SlidingMenu(this);
		slidginMenu.setMode(SlidingMenu.LEFT);
		slidginMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		slidginMenu.setShadowWidthRes(R.dimen.shadow_width);
		slidginMenu.setShadowDrawable(R.drawable.shadow);
		slidginMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		slidginMenu.setFadeDegree(0.35f);
        slidginMenu.attachToActivity(this, SlidingMenu.SLIDING_WINDOW);
        slidginMenu.setMenu(R.layout.search);
	}
	int currentMenu;
	
	public void slidingMenuAction(View v) {
		int menuItem = v.getId();
		if(currentMenu==menuItem) return;
		else currentMenu = menuItem;
		Intent intent;
		switch(menuItem)  {
		case R.id.menu_home:
			//go to home,
			intent = new Intent(this, MainActivity.class);				
			startActivity(intent);
			finish();
			break;
		case R.id.menu_friend:
			intent = new Intent(this, Friends.class);				
			startActivity(intent);
			finish();
			break;
		case R.id.menu_brand:
			intent = new Intent(this, Brand.class);				
			startActivity(intent);
			finish();
			break;
		case R.id.menu_setting:
			break;
		case R.id.menu_favorite:
			break;
		case R.id.menu_profile:
			break;
		case R.id.menu_reward:
			intent = new Intent(this, Reward.class);				
			startActivity(intent);
			finish();
			break;
		case R.id.menu_games:
			break;
		}
	}
	@Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
	
	@Override
	protected void onResume() {
		super.onResume();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
