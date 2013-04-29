package com.qrsphere.database;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;

import com.qrsphere.network.GetHistoryListProcess;
import com.qrsphere.widget.MyLog;

public class QrcodeList {
	
	List<Qrcode> m_list;
	Context context;
	GetHistoryListProcess ghhGlobal = null;
	ProgressDialog pd;


	
	public QrcodeList(Context context, GetHistoryListProcess ghhGlobal,
			ProgressDialog pd) {
		super();
		this.context = context;
		this.ghhGlobal = ghhGlobal;
		this.pd = pd;
		this.m_list = new ArrayList<Qrcode>();
	}
	public QrcodeList(Context context, GetHistoryListProcess ghhGlobal,
			ProgressDialog pd,String jsonString) {
		super();
		this.context = context;
		this.ghhGlobal = ghhGlobal;
		this.pd = pd;
		this.m_list=parseJSONString(jsonString);
	}
	
	public void changeContext(Context context, GetHistoryListProcess ghhGlobal,
			ProgressDialog pd){
		this.context = context;
		this.ghhGlobal = ghhGlobal;
		this.pd = pd;
	}
	
	public List<Qrcode> getHistoryList(){
		return m_list;
	}
	public List<Qrcode> getFavoriteList(){
		List<Qrcode> list =  new ArrayList<Qrcode>();
		for (Qrcode q:m_list){
			if (q.getQrcodeJSONData().isFavorite())
				list.add(q);
		}
		return list;
	}
	
	public boolean update(){
		m_list.clear();
		String contents= ghhGlobal.getStrJSON();
		m_list=parseJSONString(contents);
		return false;
	}
	public List<Qrcode> parseJSONString(String jsonString){
		JSONObject jObject;
		List<Qrcode> list = new ArrayList<Qrcode>();
		try {
			jObject = new JSONObject(jsonString.trim());
			JSONArray qrs = jObject.getJSONArray("ResponseContent");
			  for(int i = 0; i < qrs.length() ; i++){
				  JSONObject jsonObj = ((JSONObject)qrs.opt(i)) ;
	            if( jsonObj != null ){
	            	
	            	String url = jsonObj.getString("ScanContent");
	            	Qrcode q = new Qrcode(url,context);
	            	list.add(q);
	            }
	        }
		} catch (JSONException e) {
			MyLog.i(e.getMessage());
		}
		
		return list;
	}
	public ProgressDialog sendListRequest() {
		pd = ghhGlobal.sentToServer(context, null);
		return pd;
	}
	
	public String getJSONArrayString(){
		JSONObject j = new JSONObject();
		JSONArray jArray = new JSONArray();
		try {
			for (Qrcode q:m_list){
				JSONObject jObject = new JSONObject();
				jObject.put("ScanContent", q.getQrcodeJSONData().getUrl());
				
				jArray.put(jObject);
				
			}
			j.put("ResponseContent", jArray);
		}
		 catch (JSONException e) {
			MyLog.i(e.getMessage());
		}

		return j.toString();
	}
	

}
