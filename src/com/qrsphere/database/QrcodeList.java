package com.qrsphere.database;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;

import com.qrsphere.network.GetHistoryList;
import com.qrsphere.widget.MyLog;
import com.qrsphere.widget.QrcodeComparator;

public class QrcodeList {
	
	List<Qrcode> m_list;
	Context context;
	GetHistoryList ghhGlobal = null;
	ProgressDialog pd;


	
	public QrcodeList(Context context, GetHistoryList ghhGlobal,
			ProgressDialog pd) {
		super();
		this.context = context;
		this.ghhGlobal = ghhGlobal;
		this.pd = pd;
		this.m_list = new ArrayList<Qrcode>();
	}
	public QrcodeList(Context context, GetHistoryList ghhGlobal,
			ProgressDialog pd,String jsonString) {
		super();
		this.context = context;
		this.ghhGlobal = ghhGlobal;
		this.pd = pd;
		this.m_list=parseJSONString(jsonString);
	}
	
	public void changeContext(Context context, GetHistoryList ghhGlobal,
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
	//delete the item with specific history id from list
	public boolean deleteItem(int historyId){
		for (int i=0;i<m_list.size();i++){
			Qrcode q = m_list.get(i);
			if (q.getQrcodeJSONData().getQrScanHistoryID()==historyId){
				m_list.remove(i);
				return true;
			}
				
		}
		return false;
	}
	
	public boolean update(){
		m_list.clear();
		String contents= ghhGlobal.getStrJSON();
		m_list=parseJSONString(contents);
		sortList();
		return false;
	}
	public Qrcode addCategoryFromList(Qrcode q){
		//m_list.
		for (Qrcode qc:m_list){
			if (0==q.getHashcode().compareTo(qc.getHashcode()))
				q.addCatalogue(qc.getQrcodeJSONData().getCatalogue());
		}
		return q;
	}
	public void sortList(){
		QrcodeComparator comparator=new QrcodeComparator();
		Collections.sort(m_list, comparator);//TODO: check its sort algorithm in the future
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
	            	Qrcode q = new Qrcode(jsonObj.toString());
	            	list.add(q);
	            }
	        }
		} catch (JSONException e) {
			MyLog.i(e.getMessage());
		}
		
		return list;
	}
	public ProgressDialog sendListRequest() {

		return ghhGlobal.postData(new Qrcode(""));
	}
	
	public String getJSONArrayString(){
		JSONObject j = new JSONObject();
		JSONArray jArray = new JSONArray();
		try {
			for (Qrcode q:m_list){
				JSONObject jObject = new JSONObject(q.getRawdata());

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
