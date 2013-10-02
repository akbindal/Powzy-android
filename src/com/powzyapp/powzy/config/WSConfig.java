package com.powzyapp.powzy.config;

public class WSConfig {
	public final static boolean isLocal = false;
	public  final static boolean isDebug = false;
	//powzy
	//http://powzy-service.appspot.com/rest
	//http://10.56.155.84:8888/rest
	public final static String BASE_URL = "http://powzy-service.appspot.com/rest";
	public final static String BUSINESSINFO_GET_LIST_URL = BASE_URL + "/businessInfo/getList";
	public final static String BUSINESSINFO_BY_CATEGORY = BASE_URL + "/businessInfo/category";
}
