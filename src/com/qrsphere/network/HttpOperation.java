package com.qrsphere.network;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.util.Log;

public class HttpOperation {
	 
    public HttpURLConnection urlconn= null;
    String urlStr="";
    String result ="";
    List<NameValuePair> params = new ArrayList<NameValuePair>();
    
    public List<NameValuePair> AddValueToParams(String key,String value){
    	params.add(new BasicNameValuePair(key, value));
    	return params;
    }
    
    public HttpOperation(String urlStr) {
		super();
		this.urlStr = urlStr;
	}

	private void Init() throws IOException
    {
        if (urlStr=="")
        {
            urlStr="https://www.google.com";
        }
        URL url = new URL(urlStr);
        //open a connection object of specific url
        urlconn = (HttpURLConnection)url.openConnection();
    }
    
    public String HttpGetMethod() throws IOException
    {
    	

    	        if(urlconn == null)
    	        {
    	            try {
						Init();
					} catch(Exception e){Log.i("HTTPGET", e.toString());}
    	        }
    	        
    	        try{result = StreamDeal(urlconn.getInputStream()).toString();}
    	        catch(Exception e){Log.i("HTTPGET", e.toString());}
    	        
    	        urlconn.disconnect();


        return result;
    }
    


    public static byte[] StreamDeal(InputStream inputStream) throws Exception {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = -1;
		while((len=inputStream.read(buffer))!= -1){
			outStream.write(buffer, 0, len);
		}
		outStream.close();
		inputStream.close();
		return outStream.toByteArray();
	}

	public String HttpPostMethod(String key,String value) throws IOException
    {
        if (urlconn==null)
        {
            Init();
        }
        //set URLConnection readable
        urlconn.setDoInput(true);
        //set URLConnection writable
        urlconn.setDoOutput(true);
        //use POST to send data
        urlconn.setRequestMethod("POST");
        //without caches
        urlconn.setUseCaches(false);
        urlconn.connect();
        
        OutputStreamWriter writer = new OutputStreamWriter(urlconn.getOutputStream());
        
        String urlQueryStr = key+"="+URLEncoder.encode(value, "Utf-8");
        writer.write(urlQueryStr);
        
        writer.flush();
        writer.close();
       
        
		try {
			result = StreamDeal(urlconn.getInputStream()).toString();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.i("HTTPOST", e.toString());
		}
        return result;
        
    }
	
    public String HttpClientGetMethod()
    {
        String result = "";
        try
        {
        HttpGet httpRequest = new HttpGet(urlStr);
        HttpClient httpClient = new DefaultHttpClient();
        HttpResponse httpResponse = httpClient.execute(httpRequest);
        if(httpResponse.getStatusLine().getStatusCode()==HttpStatus.SC_OK)
        {
            result = EntityUtils.toString(httpResponse.getEntity());
        }
        else
        {
        	result = httpResponse.getStatusLine().getStatusCode()+"";
        	Log.i("HTTPOST", "StatusCode = : "+result);
            //return result;
        }
        return result;
        }
        catch(Exception e)
        {
        	Log.i("HTTPGET", e.toString());
            return null;
        }
    }
    
    
    public String HttpClientPostMethod()
        {
           // String result = "";
       
        
        				//params.add(new BasicNameValuePair(key, value));

        	         
        				HttpPost httpRequest = new HttpPost(urlStr);
        	            

        	         try{
        	            HttpEntity httpentity = new UrlEncodedFormEntity(params, "Utf-8");
        	            
        	            httpRequest.setEntity(httpentity);
        	           
        	            HttpClient httpclient = new DefaultHttpClient();
        	           
        	            HttpResponse httpResponse = httpclient.execute(httpRequest);
        	            //sleep(500);
        	            int nStatusCode =httpResponse.getStatusLine().getStatusCode();
        	            // HttpStatus.SC_OK demonstrate success
        	            if (nStatusCode == HttpStatus.SC_OK) {
        	                
        	                result = EntityUtils.toString(httpResponse.getEntity());
        	                return result; 
        	            } else {
        	            	result = nStatusCode+"";
        	            	Log.i("HTTPOST", "StatusCode = : "+nStatusCode);
        	                return result;
        	            }
        	           }
        	            catch(Exception e)
        	           {
        	    			Log.i("HTTPOST", e.toString());
        	                return null;
        	           }


        }
    
    public String loginPost(String data, String auth,String posttype)
    throws Exception{
    	String strResp = ""; 
    	if(posttype=="login"){
    		HttpPost post = new HttpPost(urlStr);  
    		post.addHeader("Authorization","Basic " +  auth);
            StringEntity se = new StringEntity(data, HTTP.UTF_8);  
            se.setContentType("application/json");  
            post.setEntity(se);
            HttpClient httpclient = new DefaultHttpClient();
            try{
            HttpResponse resp = httpclient.execute(post);  
            if (resp.getStatusLine().getStatusCode() == HttpURLConnection.HTTP_OK)  
                strResp = EntityUtils.toString(resp.getEntity()); 
            }catch(Exception e){
            	throw e;
            }
             

    	}

    	
    	return strResp;
    	
    }
    
    
    public String doPost( String data, String contentType)   
    throws Exception {  
    HttpPost post = new HttpPost(urlStr);  
    try  {  
        // add params to request  
        StringEntity se = new StringEntity(data, HTTP.UTF_8);  
        se.setContentType(contentType);  
        post.setEntity(se);  
        // send request  
        Log.v("http", "doPost:" + urlStr);  
        HttpClient httpclient = new DefaultHttpClient();
        HttpResponse resp = httpclient.execute(post);  
        String strResp = "";  
        if (resp.getStatusLine().getStatusCode() == HttpURLConnection.HTTP_OK)  
            strResp = EntityUtils.toString(resp.getEntity());  
        else  
            // if status code is not OK, throw exception
            throw new Exception("Error Response:" + resp.getStatusLine().toString());  
        return strResp;  
    }  
    finally {  
        post.abort();  
    }  
}  

}
