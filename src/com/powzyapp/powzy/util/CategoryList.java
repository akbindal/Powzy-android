package com.powzyapp.powzy.util;

import java.math.BigDecimal;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.powzyapp.powzy.R;

import android.view.View;

public class CategoryList {
	public final static int ART = 1;
	public final static int SERVICE = 2;
	public final static int FOOD = 3;
	public final static int BEER = 4;
	public final static int NATURE = 5;
	public final static int BANK = 6;
	public final static int SHOP = 7;
	public final static int TRAVEL = 8;
	
	public static BitmapDescriptor getLocationMarker(Integer service) {
		//int service = new BigDecimal(services).intValueExact();
		switch(service) {
		case ART:
			return BitmapDescriptorFactory.fromResource(R.drawable.location_1);
		case SERVICE:
			return BitmapDescriptorFactory.fromResource(R.drawable.location_2);
		case FOOD:
			return BitmapDescriptorFactory.fromResource(R.drawable.location_3);
		case BEER:
			return BitmapDescriptorFactory.fromResource(R.drawable.location_4);
		case NATURE:
			return BitmapDescriptorFactory.fromResource(R.drawable.location_5);
		case BANK:
			return BitmapDescriptorFactory.fromResource(R.drawable.location_6);
		case SHOP:
			return BitmapDescriptorFactory.fromResource(R.drawable.location_7);
		case TRAVEL:
			return BitmapDescriptorFactory.fromResource(R.drawable.location_8);
		}
		return null;
	}
	
	public static void ImageAction(View v) {
		int itemId = v.getId();
		switch(itemId) {
		case R.id.category1:
			//take action1
			break;
		case R.id.category2:
			break;
		case R.id.category3:
			break;
		case R.id.category4:
			break;
		case R.id.category5:
			break;
		case R.id.category6:
			break;
		case R.id.category7:
			break;
		case R.id.category8:
			break;
			
		}
	}
}
