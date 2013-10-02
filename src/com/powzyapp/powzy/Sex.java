package com.powzyapp.powzy;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringBufferInputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.graphics.Bitmap;

public class Sex extends Activity implements SensorEventListener {

	Sensor accelometer;
	SensorManager sm;
	TextView text;

	float x = 0;
	float y = 0;
	float z = 0;
	boolean overPush = false;
	int pushCounter = 0;
	float maxNumber = 0;
	Date maxDate;

	float volumeValue;
	float accValue;
	Date startTime;
	Date stopTime;
	double duration;

	boolean working = false;

	ArrayList<Float> accArray;
	ArrayList<Float> voiceArray;

	String acc;
	String voice;

	ArrayList<Float> tempAcc;
	ArrayList<Float> tempVoice;

	Timer timer;
	Timer voiceSample;

	float max = 0;

	static final private double EMA_FILTER = 0.6;

	private MediaRecorder mRecorder = null;
	private double mEMA = 0.0;
	WebView gameWebView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sex);

		gameWebView = (WebView) findViewById(R.id.gameVebView);
		gameWebView.loadUrl("file:///android_asset/www/index.html");

		WebSettings webSettings = gameWebView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setAllowFileAccess(true);
		webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
		webSettings.setLayoutAlgorithm(LayoutAlgorithm.NORMAL);
		
		sm = (SensorManager) getSystemService(SENSOR_SERVICE);
		accelometer = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		sm.registerListener(this, accelometer, SensorManager.SENSOR_DELAY_NORMAL);
		
		timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				if(working)
				{
					float accAmplitude = 0;
					for (int i = 0; i < tempAcc.size(); i++) {
						accAmplitude = (float) tempAcc.get(i);
					}
					tempAcc.clear();
					accArray.add(accAmplitude);
		
					float voiceAmplitude = 0;
					for (int i = 0; i < tempVoice.size(); i++) {
						voiceAmplitude = (float) tempVoice.get(i);
					}
					voiceArray.add(voiceAmplitude);
					tempVoice.clear();
				}
			}
		}, 1000, 1000);
				
		voiceSample = new Timer();
		// Set the schedule function and rate
		voiceSample.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				if(working)
				{
					sampleVoice();
				}
			}
		}, 100, 100);
		
		gameWebView.setWebViewClient(new WebViewClient() {
			
			@Override
			public void onPageFinished(WebView view, String url) {
								
				if (url.endsWith("finish.html")) {	
					
					stop();

					duration = stopTime.getTime() - startTime.getTime();

					double min = (int) (duration) / (1000 * 60);
					double sec = (int) (duration - (60 * min)) / (1000);

					float highestA = 0;
					for (int i = 0; i < accArray.size(); i++) {
						if (i == 0) {
							highestA = (float) accArray.get(i);
						} else {
							if ((float) accArray.get(i) > highestA)
								highestA = (float) accArray.get(i);
						}
					}

					float highestV = 0;
					for (int i = 0; i < voiceArray.size(); i++) {
						if (i == 0) {
							highestV = (float) voiceArray.get(i);
						} else {
							if ((float) voiceArray.get(i) > highestV)
								highestV = (float) voiceArray.get(i);
						}
					}

					String text = readFileFromAssets("www/finish.html");						

					acc = "[";
					voice = "[";

					for (int i = 0; i < accArray.size(); i++) {
						float roundedA = ((float) accArray.get(i) / (float) highestA) * 100;

						float roundedV = ((float) voiceArray.get(i) / (float) highestV) * 100;

						if (i + 1 != accArray.size()) {
							acc += (int)roundedA + ",";

							// **voice += String.format("%4.0f", roundedV) +								
							voice += "2,";

						} else {
							acc += (int)roundedA + "]";

							// voice += String.format("%4.0f]", roundedV);
							voice += (int)roundedV + "]";
						}
					}
					
					text = text.replace("!##1##!", acc);
					text = text.replace("!##2##!", voice);
					
					String time;
					
					if (min < 10 && sec < 10)
						time = "0" + (int)min + ":0" + (int)sec;
					else if (min < 10)
						time = "0" + (int)min + ":" + (int)sec;
					else
						time = (int)min + ":" + (int)sec;					
					
					String maxPush = String.format("%4.0f", (maxNumber * 250)) + " kj at " + maxDate.getHours() + ":" + maxDate.getMinutes();					
					view.loadUrl("javascript:drawGraph(" + acc + ", " + voice + ", '" + time + "', '" + pushCounter + "', '" + maxPush + "')");										
				}				
			}

			@Override
			public WebResourceResponse shouldInterceptRequest( final WebView view, String url) {

				if (url.endsWith("time")) {
					// System.out.println(volumeValue + ":" + accValue);

					String volume = String.format("%.3f", volumeValue);
					String acc = String.format("%.3f", accValue);

					view.loadUrl("javascript:doit('" + volume + ":" + acc
							+ "')");

					return null;

				} else if (url.endsWith("start")) {
					System.out.println("start");
					start();
				}

				return null;
			}
		});

	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {

	}

	public void onResume() {
		super.onResume();
		sm.registerListener(this, accelometer, SensorManager.SENSOR_DELAY_NORMAL);
		
	}

	@Override
	public void onStop() {
		sm.unregisterListener(this);
		super.onPause();
		timer.cancel();
		voiceSample.cancel();
		System.out.println("STOP");
	}
	
	
	@Override
	public void onPause() {
		sm.unregisterListener(this);
		super.onPause();
		timer.cancel();
		voiceSample.cancel();
		System.out.println("PAUSE");
	}
	
	@Override
	public void onSensorChanged(SensorEvent event) {

		if(working)
		{		
			float d_x = (float) event.values[0] - x;
			float d_y = event.values[1] - y;
			float d_z = event.values[2] - z;
	
			x = event.values[0];
			y = event.values[1];
			z = event.values[2];
	
			if (d_x < 0)
				d_x *= -1;
			if (d_y < 0)
				d_y *= -1;
			if (d_z < 0)
				d_z *= -1;
	
			float combined = (d_x + d_y + d_z) / 3;
	
			combined = (float) combined / (float) 10.0;
	
			if (combined > 1)
				combined = 1;
	
			if (!overPush && combined > 0.04) {
				pushCounter += 1;
				overPush = true;
				System.out.println("PUSH");
			}
			if (overPush && combined <= 0.02) {
				overPush = false;
			}
	
			if (maxNumber < combined) {
				maxNumber = combined;
				maxDate = new Date();			
			}
	
			accValue = combined;
			tempAcc.add(combined);
			System.out.println("OUT" + combined);
			
		}
	}

	public void start() {
		
		working = true;
		
		accArray = new ArrayList<Float>();
		voiceArray = new ArrayList<Float>();

		tempAcc = new ArrayList<Float>();
		tempVoice = new ArrayList<Float>();
		
		startTime = new Date();

		/*
		 * if (mRecorder == null) { mRecorder = new MediaRecorder();
		 * 
		 * mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		 * mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
		 * mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
		 * mRecorder.setOutputFile("/dev/null"); try { mRecorder.prepare(); }
		 * catch (IllegalStateException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); } catch (IOException e) { // TODO Auto-generated
		 * catch block e.printStackTrace(); } mRecorder.start(); mEMA = 0.0; }
		 */
	}

	public void stop() {
		stopTime = new Date();
		// [recorder stop];		
		
		working = false;
	}

	public void sampleVoice() {

	}

	public String readFileFromAssets(String fileName) {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(getAssets().open(
					fileName)));
			String temp;
			while ((temp = br.readLine()) != null)
				sb.append(temp);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				br.close(); // stop reading
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

}
