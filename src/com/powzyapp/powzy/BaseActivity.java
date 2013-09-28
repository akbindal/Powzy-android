package com.powzyapp.powzy;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import android.app.Activity;
import android.os.Bundle;

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
        slidginMenu.setMenu(R.layout.sliding_menu);
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
