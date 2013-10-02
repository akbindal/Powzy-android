package com.powzyapp.powzy.network;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.powzyapp.powzy.config.WSConfig;
import com.powzyapp.powzy.model.BusinessView;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

public class BusinessListRequest extends AsyncTask<Integer[], Void, String>{
	private final Context mContext;
	public AsyncResponse delegate = null;
	
	private ProgressDialog dialog;
	
	private int FUNCTION_CALL;
	
	public BusinessListRequest(Context context) {
		super();
		this.mContext = context;
		dialog = new ProgressDialog(mContext);
	}
	
	@Override
	protected void onPreExecute() {
		this.dialog.show();
	}
	
	@Override
	protected String doInBackground(Integer[]... args) {
		String catParam = new Gson().toJson(args);
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(WSConfig.BUSINESSINFO_BY_CATEGORY);
		try {
			StringEntity se = new StringEntity(catParam);
			se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
			//se.setContentType(new BasicHeader("Accept, "application/json"));
			httpPost.setEntity(se);
			//httpPost.setHeader("Accept", "application/json");
			//httpPost.setHeader("Content-type", "application/json");
		} catch (Exception e) {
			delegate.processFinish(ResponseMessage.JSON_ERROR, null);
			return null;
		}
		HttpResponse response;
		try {
			response = httpClient.execute(httpPost);
			String temp = EntityUtils.toString(response.getEntity());
			
			/*BufferedReader reader= new BufferedReader(new InputStreamReader (response
					.getEntity().getContent(), "UTF-8"));
			StringBuilder builder = new StringBuilder();
			for (String line = null; (line = reader.readLine()) != null;) {
			    builder.append(line).append("\n");
			}*/
			return temp;//builder.toString();
		} catch(Exception e) {
			e.printStackTrace();
			delegate.processFinish(ResponseMessage.NETWORK_ERROR, null);
			return null;
		}
	}
	
	@Override 
	protected void onPostExecute(String jsonResp) {
        if(dialog!=null) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
        if (jsonResp==null)
        	return;
        
        BusinessView[] entities;
        try {
        	int firstIndex = jsonResp.indexOf(':');
        	jsonResp = jsonResp.substring(firstIndex+1);
        	Gson gson = new Gson();
        	Type catType = new TypeToken<BusinessView[]>() {}.getType();
        	entities = gson.fromJson(jsonResp, catType);
        	
        } catch (JsonSyntaxException e)  {
        	delegate.processFinish(ResponseMessage.JSON_ERROR, null);
        	return;
        }
        if (entities==null) delegate.processFinish(ResponseMessage.REQUEST_ERROR, null);
        else delegate.processFinish(ResponseMessage.SUCCESSFUL_REQUEST, entities);
        return;
	}

	
}
