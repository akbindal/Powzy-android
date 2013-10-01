package com.powzyapp.powzy;

import java.util.Arrays;

import com.powzyapp.powzy.util.UIUtil;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.HorizontalScrollView;

public class MapDescription extends BaseActivity  {
	
	HorizontalScrollView hsv;
	boolean categories[];
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_description);
		hsv = (HorizontalScrollView) findViewById(R.id.horizontalScrollView);
		View child = getLayoutInflater().inflate(R.layout.category_list, null);
		hsv.addView(child);
		categories = new boolean[UIUtil.TOTOAL_CATEGORY];
		Arrays.fill(categories, true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
