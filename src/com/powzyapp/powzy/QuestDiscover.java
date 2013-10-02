package com.powzyapp.powzy;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.powzyapp.powzy.model.BusinessView;
import com.powzyapp.powzy.model.Position;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class QuestDiscover extends BaseActivity implements SensorListener,
LocationListener  {
     
	ArrayList<BusinessView> businesses;	
	private SurfaceView preview = null;
	private SurfaceHolder previewHolder = null;
	private Camera camera = null;
	private boolean inPreview = false;
	private boolean cameraConfigured = false;
	private boolean moreVisiable = false;
	private boolean isLarge = false;

	private ImageView aLeftArrow;
	private ImageView aRightArrow;
	
	private Map<String, ImageView> annotationImages;
	private Map<String, LinearLayout> annotationLayouts;	

	private float density;
	private int maxDistance;
	private int maxAngle = 30;
	private int width;
	private int height;

	private int closestAnnotationUnique;
	private int closestAnnotationIndex;
	private int closestAnnotationAngle;
	
	
	private boolean haveAnnotation = false;

	SeekBar seekbar;
	TextView distance;	

	// location
	
	// compass
	SensorManager sensorManager;
	static final int sensor = SensorManager.SENSOR_ORIENTATION;
	private int orientation = 0;
	private boolean orientationChanged = false;

	
	//location
	private LocationManager locationManager;
	private String towers;
	Location location;
	private boolean locationChanged = false;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.discover_quest);
		
		preview = (SurfaceView) findViewById(R.id.a_preview);
		previewHolder = preview.getHolder();
		previewHolder.addCallback(surfaceCallback);
		previewHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		
		
		// test if leyout-large
		if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE) 
			isLarge = true;
		
		//define sensor
		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		
		//define screen parameters
		Display display = getWindowManager().getDefaultDisplay();
		density = getResources().getDisplayMetrics().density;		
		width = display.getWidth();
		height = display.getHeight();
		
		Intent intent = getIntent();
		haveAnnotation = intent.getBooleanExtra("haveAnnotation", false);
					
		// define annotations		
		annotationLayouts = new HashMap<String, LinearLayout>();
		annotationImages = new HashMap<String, ImageView>();
		
		
		// set location manager
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		
	    if ( !locationManager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {

	    	AlertDialog alertDialog = new AlertDialog.Builder(this).create();
	    	alertDialog.setTitle("GPS is disabled");
	    	alertDialog.setMessage("GPS is disabled");
	    	alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
	    	   public void onClick(DialogInterface dialog, int which) {
	    	      // TODO Add your code for the button here.
	    	   }
	    	});
	    	// Set the Icon for the Dialog	    	
	    	alertDialog.show();
	    		    	
	    }
		
	    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 10, this);
	    	    
		Location newLocation = new Location("");
		if (locationManager != null) {
			newLocation = locationManager
					.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		}

		if (newLocation != null) {
			location = newLocation;
		} else
			System.out.println("Location null zakaj ");
		
		// draw points on screen
		drawSelectedAnnotations();	
		
		
	}

	
	@Override
	public void onResume() {
		super.onResume();
		sensorManager.registerListener(this, sensor);
		camera = Camera.open();
		startPreview();
		locationManager.requestLocationUpdates(
  				LocationManager.GPS_PROVIDER, 10000, 10, this);
	}

	@Override
	public void onPause() {
		if (inPreview) {
			camera.stopPreview();
			sensorManager.unregisterListener(this);
		}

		camera.release();
		camera = null;
		inPreview = false;
		locationManager.removeUpdates(this);
		super.onPause();
	}

	private Camera.Size getBestPreviewSize(int width, int height,
			Camera.Parameters parameters) {
		Camera.Size result = null;

		for (Camera.Size size : parameters.getSupportedPreviewSizes()) {
			if (size.width <= width && size.height <= height) {
				if (result == null) {
					result = size;
				} else {
					int resultArea = result.width * result.height;
					int newArea = size.width * size.height;

					if (newArea > resultArea) {
						result = size;
					}
				}
			}
		}

		return (result);
	}

	private void initPreview(int width, int height) {
		if (camera != null && previewHolder.getSurface() != null) {
			try {
				camera.setPreviewDisplay(previewHolder);
			} catch (Throwable t) {
				Log.e("PreviewDemo-surfaceCallback",
						"Exception in setPreviewDisplay()", t);
				Toast.makeText(QuestDiscover.this, t.getMessage(),
						Toast.LENGTH_LONG).show();
			}

			if (!cameraConfigured) {
				Camera.Parameters parameters = camera.getParameters();
				Camera.Size size = getBestPreviewSize(width, height, parameters);

				if (size != null) {
					parameters.setPreviewSize(size.width, size.height);
					camera.setParameters(parameters);
					cameraConfigured = true;
				}
			}
		}
	}

	private void startPreview() {
		if (cameraConfigured && camera != null) {
			camera.startPreview();
			inPreview = true;
		}
	}
	
	
	SurfaceHolder.Callback surfaceCallback = new SurfaceHolder.Callback() {
		public void surfaceCreated(SurfaceHolder holder) {
			// no-op -- wait until surfaceChanged()
		}

		public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height) {
			
			initPreview(width, height);
			startPreview();
			
		}

		public void surfaceDestroyed(SurfaceHolder holder) {
			// no-op
		}
	};
	
	// on orientation change
		public void onSensorChanged(int sensor, float[] values) {
			
			if (sensor != this.sensor)
				return;
			
			if (orientation != (int) values[0]) {
				orientation = (int) values[0];
				orientationChanged = true;
				placeSelectedAnnotations();
			}
		}
		
		public void onAccuracyChanged(int sensor, int accuracy) {
			
		}
		
		public void drawSelectedAnnotations() {

			FrameLayout screen = (FrameLayout) findViewById(R.id.a_allscreen);

			for (Map.Entry<String, ImageView> entry : annotationImages.entrySet()) {
				entry.getValue().setImageResource(0);
				entry.getValue().setImageDrawable(null);
			}

			for (Map.Entry<String, LinearLayout> entry : annotationLayouts
					.entrySet()) {
				entry.getValue().removeAllViews();
				screen.removeView(entry.getValue());
			}

			annotationLayouts.clear();
			annotationImages.clear();	
			
			annotationLayouts = new HashMap<String, LinearLayout>();
			annotationImages = new HashMap<String, ImageView>();

			int marginTop = (int) (height / 2) - (int) (40 * density) ;
			int iconW = (int) (44 * density);
			int iconH = (int) (50 * density);

			businesses = new ArrayList<BusinessView>();
			
			
			BusinessView business1 = new BusinessView();
			business1.id = (long) 13213231;
			business1.name = "Hello";
			
			Position position1 = new Position();
			position1.latitude = 46.343136;
			position1.longitude = 6.926422;
			business1.location = position1;			
			
			BusinessView business2 = new BusinessView();
			business2.id = (long) 13213230;
			business2.name = "Hello";
			
			Position position2 = new Position();
			position2.latitude = 46.269139;
			position2.longitude = 7.467499;
			business2.location = position2;
			
			businesses.add(business1);
			businesses.add(business2);
			
			for (int i = 0; i < businesses.size(); i++) {
				BusinessView business = businesses.get(i);
	
				LinearLayout tempLayout = new LinearLayout(this);
				LinearLayout.LayoutParams linearLayoutParams = new LinearLayout.LayoutParams(
						LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT,
						Gravity.TOP);

				annotationLayouts.put(String.valueOf(business.id),
						tempLayout);

				ImageView image = new ImageView(this);
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
						66, 66);

				params.width = iconW;
				params.height = iconH;
				params.topMargin = marginTop;

				image.setVisibility(View.INVISIBLE);
				image.setScaleType(ImageView.ScaleType.CENTER_CROP);
				image = setImage(image, i+1);
				tempLayout.addView(image, params);
				screen.addView(tempLayout, linearLayoutParams);

				annotationImages.put(String.valueOf(business.id), image);	
			}
			 	
			if (orientationChanged) {
				placeSelectedAnnotations();
			}
		}

		public ImageView setImage(ImageView image, int type) {
			if (type == 1) {
				if (isLarge)
					image.setBackgroundResource(R.drawable.location_1);
				else
					image.setBackgroundResource(R.drawable.location_1);
			} else if (type == 2) {
				if (isLarge)
					image.setBackgroundResource(R.drawable.location_2);
				else
					image.setBackgroundResource(R.drawable.location_2);
			} else if (type == 3) {
				if (isLarge)
					image.setBackgroundResource(R.drawable.location_3);
				else
					image.setBackgroundResource(R.drawable.location_3);
			} else if (type == 4) {
				if (isLarge)
					image.setBackgroundResource(R.drawable.location_4);
				else
					image.setBackgroundResource(R.drawable.location_4);
			} else if (type == 5) {
				if (isLarge)
					image.setBackgroundResource(R.drawable.location_5);
				else
					image.setBackgroundResource(R.drawable.location_5);
			} else if (type == 6) {
				if (isLarge)
					image.setBackgroundResource(R.drawable.location_6);
				else
					image.setBackgroundResource(R.drawable.location_6);
			} else if (type == 7) {
				if (isLarge)
					image.setBackgroundResource(R.drawable.location_7);
				else
					image.setBackgroundResource(R.drawable.location_7);
			} else if (type == 8) {
				if (isLarge)
					image.setBackgroundResource(R.drawable.location_8);
				else
					image.setBackgroundResource(R.drawable.location_8);
			} 

			return image;
		}
		
		
		// place annotation on right place
		public void placeSelectedAnnotations() {
			closestAnnotationUnique = 0;
			closestAnnotationAngle = maxAngle + 1;			
						
			for (int i = 0; i < businesses.size(); i++) {
				BusinessView business = businesses.get(i);

				
				// get distance between two points
				Location locat = new Location("");
				locat.setLatitude(business.location.latitude);
				locat.setLongitude(business.location.longitude);

				// if location is null
				if (location != null) {
					// get distance between locations
					//annotation.distance = locat.distanceTo(location);

					// get image
					ImageView image = annotationImages.get(String
							.valueOf(business.id));

					
					if (image != null) {
						// get angle between two points
						
						double angle = getAngle(locat, location);

						// orientation fix
						if (orientation > 270 && angle < 90) {
							angle = angle + 360;
						} else if (orientation < 270 && angle > 90) {
							angle = angle - 360;
						}

						// get angle between two angle
						float headingAngle = (float) angle - orientation;
						if (i == 0)
							System.out.println( i + "LOC:" + location.getLatitude() + " LAG:" + location.getLongitude() + "H:" + headingAngle + " A:" + angle + " O:" + orientation);
						
						//System.out.println("H:" + headingAngle + " A:" + angle + " O:" + orientation);
												
						// if not on screen
						if (java.lang.Math.abs(headingAngle) > maxAngle) {
							image.setVisibility(View.INVISIBLE);
							
							if (haveAnnotation)
							{
								if (headingAngle < -180 || headingAngle > 0)
									aRightArrow.setVisibility(View.VISIBLE);
								else								
									aLeftArrow.setVisibility(View.VISIBLE);								
							}
							
						} else {
							
							// set position on screen
							LinearLayout.LayoutParams params = (LayoutParams) image
									.getLayoutParams();

							float step = (width / 2) / (float) maxAngle;
							int leftMargin = (int) ((step * headingAngle) + (width / 2));

							params.leftMargin = leftMargin;
							image.setLayoutParams(params);
							image.setVisibility(View.VISIBLE);
						}
					}		
				}
			}
			
		}
		
		// calculate angle
		public double getAngle(Location annotationLocation, Location currentLocation) {

			double lat1 = (currentLocation.getLatitude() * java.lang.Math.PI) / 180.0;
			double lat2 = (annotationLocation.getLatitude() * java.lang.Math.PI) / 180.0;
			double lon1 = (currentLocation.getLongitude() * java.lang.Math.PI) / 180.0;
			double lon2 = (annotationLocation.getLongitude() * java.lang.Math.PI) / 180.0;
			double dLon = lon2 - lon1;

			double y = java.lang.Math.sin(dLon) * java.lang.Math.cos(lat2);
			double x = java.lang.Math.cos(lat1) * java.lang.Math.sin(lat2)
					- java.lang.Math.sin(lat1) * java.lang.Math.cos(lat2)
					* java.lang.Math.cos(dLon);
			double brng = (java.lang.Math.atan2(y, x) * 180.0) / java.lang.Math.PI;

			brng = (brng + 360);
			brng = (brng > 360) ? (brng - 360) : brng;

			return brng;
		}


		
		// LOCATION
		@Override
		public void onLocationChanged(Location newLocation) {
			
			if (newLocation != null) {
				location = newLocation;				
				locationChanged = true;
				placeSelectedAnnotations();
			}
		}
		
		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
		}
	
	

}