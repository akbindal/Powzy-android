package com.powzyapp.powzy;

import java.util.Arrays;
import java.util.List;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.powzyapp.powzy.model.BusinessView;
import com.powzyapp.powzy.network.AsyncResponse;
import com.powzyapp.powzy.network.BusinessListRequest;
import com.powzyapp.powzy.network.ResponseMessage;
import com.powzyapp.powzy.util.CategoryList;
import com.powzyapp.powzy.util.UIUtil;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MapDescription extends FragmentActivity implements AsyncResponse {
	
	HorizontalScrollView hsv;
	boolean categories[];
	
	private GoogleMap map;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_description);
		hsv = (HorizontalScrollView) findViewById(R.id.horizontalScrollView);
		View child = getLayoutInflater().inflate(R.layout.category_list, null);
		hsv.addView(child);
		categories = new boolean[UIUtil.TOTOAL_CATEGORY];
		Arrays.fill(categories, true);		
		map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
		map.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("hello world"));
		
		//request for fetching business list from server
		BusinessListRequest request = new BusinessListRequest(this);
		request.delegate = this;
		Integer[] ints = {1, 2, 3};
		request.execute(ints);
	}
	
	public void fillMapMarker(BusinessView[] entities) {
		map.clear();
		map.setMyLocationEnabled(true);
		Location location = map.getMyLocation();
		
		map.animateCamera(CameraUpdateFactory.zoomTo(6), 2000, null);
		if(entities ==null) return;
		for(BusinessView entity: entities) {
			
			//LatLng latlng = new LatLng(entity.location.latitude, entity.location.longitude);
			//MarkerOptions markerOption = new MarkerOptions().position(latlng)
			//		.title(entity.name)
			//		.icon(CategoryList.getLocationMarker(entity.categories));
			//map.addMarker(markerOption);
		}
		
		map.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
			
			@Override
			public View getInfoWindow(Marker arg0) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public View getInfoContents(Marker arg0) {
				// TODO Auto-generated method stub
				return null;
			}
			
			
		});
	}
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
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
			image = "category"+(int)(clickItem+1)+"_off";
		} else { 
			categories[clickItem]=true;
			image = "category"+(int)(clickItem+1);
		}

		Context context = v.getContext();
		int id = context.getResources().getIdentifier(image, "drawable", context.getPackageName());
		ImageView imView = (ImageView)v;
		imView.setImageResource(id);
	}

	@Override
	public void processFinish(int responseMessage, BusinessView[] entities) {
		// TODO Auto-generated method stub
		switch(responseMessage) {
		case ResponseMessage.JSON_ERROR:
			break;
		case ResponseMessage.NETWORK_ERROR:
			break;
		case ResponseMessage.SUCCESSFUL_REQUEST:
			Powzy.entities = entities;
			fillMapMarker(entities);
			break;
		}
	}
}
