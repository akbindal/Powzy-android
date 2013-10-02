package com.powzyapp.powzy;

import com.dm.zbar.android.scanner.ZBarConstants;
import com.dm.zbar.android.scanner.ZBarScannerActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;


public class QrScanner extends BaseActivity{
	public final static int ZBAR_SCANNER_REQUEST = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = new Intent(this, ZBarScannerActivity.class);
		startActivityForResult(intent, ZBAR_SCANNER_REQUEST);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{    
	    if (resultCode == RESULT_OK) 
	    {
	        // Scan result is available by making a call to data.getStringExtra(ZBarConstants.SCAN_RESULT)
	        // Type of the scan result is available by making a call to data.getStringExtra(ZBarConstants.SCAN_RESULT_TYPE)
	        Toast.makeText(this, "Scan Result = " + data.getStringExtra(ZBarConstants.SCAN_RESULT), Toast.LENGTH_SHORT).show();
	        Toast.makeText(this, "Scan Result Type = " + data.getStringExtra(ZBarConstants.SCAN_RESULT_TYPE), Toast.LENGTH_SHORT).show();
	        // The value of type indicates one of the symbols listed in Advanced Options below.
	    } else if(resultCode == RESULT_CANCELED) {
	        Toast.makeText(this, "Camera unavailable", Toast.LENGTH_SHORT).show();
	    }
	}
}
