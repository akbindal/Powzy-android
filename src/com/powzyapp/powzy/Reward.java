package com.powzyapp.powzy;

import java.util.Arrays;

import com.powzyapp.powzy.util.UIUtil;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;

public class Reward extends BaseActivity{
	
	HorizontalScrollView hsv;
	boolean[] categories;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.reward);
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

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	public void onCategorySelect(View v) {
		int itemId = v.getId();
		int clickItem=0;
		switch(itemId)  {
			case R.id.category1:
				clickItem=0;
				break;
			case R.id.category2:
				clickItem = 1;
				break;
			case R.id.category3:
				clickItem = 2;
				break;
			case R.id.category4:
				clickItem = 3;
				break;
			case R.id.category5:
				clickItem = 4;
				break;
			case R.id.category6:
				clickItem = 5;
				break;
			case R.id.category7:
				clickItem = 6;
				break;
			case R.id.category8:
				clickItem = 7;
				break;
				
		}
		//change 
		String image;
		if(categories[clickItem]){
			categories[clickItem]=false;
			image = "category"+clickItem+1+"_off";
		} else { 
			categories[clickItem]=true;
			image = "category"+(int)(clickItem+1)+"_off";
		}

		Context context = v.getContext();
		int id = context.getResources().getIdentifier(image, "drawable", context.getPackageName());
		ImageView imView = (ImageView)v;
		imView.setImageResource(id);
	}
}
