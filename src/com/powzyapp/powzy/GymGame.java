package com.powzyapp.powzy;

import com.dm.zbar.android.scanner.ZBarConstants;
import com.dm.zbar.android.scanner.ZBarScannerActivity;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class GymGame extends BaseActivity  {
	public final static int ZBAR_SCANNER_REQUEST = 0;

	WebView gameWebView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gym);

		gameWebView = (WebView) findViewById(R.id.gameVebView);
		gameWebView.loadUrl("http://aplus1games.com/games/gym/?userGameId=5629499534213120&gameLaunchId=5714368087982080&userId=5172102697058304");

		WebSettings webSettings = gameWebView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setAllowFileAccess(true);
		webSettings.setJavaScriptCanOpenWindowsAutomatically(true);		
		
		gameWebView.setWebViewClient(new WebViewClient() {
			
			@Override
			public void onPageFinished(WebView view, String url) {
			}
			
			@Override
			public void onReceivedError(WebView view, int errorCode, 
					String description, String  failingUrl) {
				System.out.println("errorCode="+errorCode + " , "+ description
						+ ", url = " + failingUrl);
			}
			@Override
			public WebResourceResponse shouldInterceptRequest( final WebView view, String url) {

				System.out.println("Scan:" + url);
				if(url.endsWith("reader")) {
					Intent intent = new Intent(GymGame.this, ZBarScannerActivity.class);
					startActivityForResult(intent, ZBAR_SCANNER_REQUEST);
				}
				return null;
			}
		});

	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{    
	    if (resultCode == RESULT_OK) 
	    {
	        // Scan result is available by making a call to data.getStringExtra(ZBarConstants.SCAN_RESULT)
	        // Type of the scan result is available by making a call to data.getStringExtra(ZBarConstants.SCAN_RESULT_TYPE)
	        String variable = data.getStringExtra(ZBarConstants.SCAN_RESULT);
	    	Toast.makeText(this, "Scan Result = " + data.getStringExtra(ZBarConstants.SCAN_RESULT), Toast.LENGTH_SHORT).show();
	        Toast.makeText(this, "Scan Result Type = " + data.getStringExtra(ZBarConstants.SCAN_RESULT_TYPE), Toast.LENGTH_SHORT).show();
	        gameWebView.loadUrl("javascript:drawAction('" + variable + "')") ;										
	        // The value of type indicates one of the symbols listed in Advanced Options below.
	    } else if(resultCode == RESULT_CANCELED) {
	        Toast.makeText(this, "Camera unavailable", Toast.LENGTH_SHORT).show();
	    }
	}
	
	@Override
	public void onBackPressed()
	{
	    if(gameWebView.canGoBack())
	    	gameWebView.goBack();
	    else
	        super.onBackPressed();
	}
}
