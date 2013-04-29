package com.qrsphere.database;



import java.util.ArrayList;
import java.util.List;


import com.qrsphere.userinfo.StringTransfer;





import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class QrcodeDataOperator {

	private DBOpenHelper helper;
	
	public QrcodeDataOperator(Context context){
		helper = new DBOpenHelper(context);
	}
	
    public void insert(Qrcode p) {  
        SQLiteDatabase db = helper.getWritableDatabase();     
          
        // prepare data  
        ContentValues values = new ContentValues();
        
        values.put("hashcode", p.getHashcode());  
        values.put("rawdata", p.getRawdata());
      //  values.put("time", p.getTimeStamp());
          
        // insert with contentvalues
        db.insert("qrcode", "id", values);        
          
        db.close();  
    }  
    public void insertToFavorite(Qrcode p){
        SQLiteDatabase db = helper.getWritableDatabase();     
        
        // prepare data  
        ContentValues values = new ContentValues();
        
        values.put("hashcode", p.getHashcode());  
        values.put("rawdata", p.getRawdata());
       // values.put("time", p.getTimeStamp());
          
        // insert with contentvalues
        db.insert("favor", "id", values);        
          
        db.close();
    }
    
    public void delete(int id) {  
        SQLiteDatabase db = helper.getWritableDatabase();  
          
       
        db.delete("qrcode", "id=?", new String[]{String.valueOf(id)});  
          
        db.close();  
    } 
    public void delete(String qrData) {  
        SQLiteDatabase db = helper.getWritableDatabase();  
          
        
        db.delete("qrcode", "rawdata=?", new String[]{String.valueOf(qrData)});  
          
        db.close();  
    } 
    public void deleteFromFavorite(String qrData) {  
        SQLiteDatabase db = helper.getWritableDatabase();  
          
          
        db.delete("favor", "rawdata=?", new String[]{String.valueOf(qrData)});  
          
        db.close();  
    } 
    
    public List<Qrcode> queryAll() {  
        SQLiteDatabase db = helper.getReadableDatabase();  
  
        // query all record  
        Cursor c = db.query(false, "qrcode", new String[]{"id","rawdata", "time","hashcode"}, null, null, null, null, "id DESC", null);  
          
        List<Qrcode> qrcodes = new ArrayList<Qrcode>();  
//        while (c.moveToNext())  
//        	qrcodes.add(new Qrcode( c.getString(1),c.getLong(2),c.getString(3)));  
        c.close();  
        db.close();  
        return qrcodes;  
    } 
    public List<Qrcode> queryAllFavorite() {  
        SQLiteDatabase db = helper.getReadableDatabase();  
  
        // query all record  
        Cursor c = db.query(false, "favor", new String[]{"id","rawdata", "time","hashcode"}, null, null, null, null, "id ASC", null);  
          
        List<Qrcode> qrcodes = new ArrayList<Qrcode>();  
//        while (c.moveToNext())  
//        	qrcodes.add(new Qrcode( c.getString(1),c.getLong(2),c.getString(3)));  
        c.close();  
        db.close();  
        return qrcodes;  
    } 
    
    public Qrcode query(int id) {  
        SQLiteDatabase db = helper.getReadableDatabase();  
          
        
        Cursor c = db.query(false, "qrcode", new String[]{"id","rawdata", "time","hashcode"}, "id=?", new String[]{String.valueOf(id)}, null, null, null, null);   
          
        Qrcode p = null;  
       
//        if (c.moveToNext())  
//            
//            p = new Qrcode( c.getString(1), c.getLong(2),c.getString(3));  
          
        
        c.close();  
        db.close();  
        return p;  
    }
    public Qrcode query(String qrData) {  
        SQLiteDatabase db = helper.getReadableDatabase();  
          
        
        Cursor c = db.query(false, "qrcode", new String[]{"id","rawdata", "time","hashcode"}, "rawdata=?", new String[]{String.valueOf(qrData)}, null, null, null, null);   
          
        Qrcode p = null;  
        
//        if (c.moveToNext())  
//             
//            p = new Qrcode( c.getString(1), c.getLong(2),c.getString(3));  
          
       
        c.close();  
        db.close();  
        return p;  
    }
    public Qrcode queryFromFavor(String qrData){
        SQLiteDatabase db = helper.getReadableDatabase();  
          
        
        Cursor c = db.query(false, "favor", new String[]{"id","rawdata", "time","hashcode"}, "rawdata=?", new String[]{String.valueOf(qrData)}, null, null, null, null);   
          
        Qrcode p = null;  
        
//        if (c.moveToNext())  
//             
//            p = new Qrcode( c.getString(1), c.getLong(2),c.getString(3));  
          
       
        c.close();  
        db.close();  
        return p; 
    }
    public void queryAndSendToFavorite(String qrData){
    	Qrcode qc = query(qrData);
    	insertToFavorite(qc);
    }
    public int queryCount() {  
        SQLiteDatabase db = helper.getReadableDatabase();  
          
        // query the number of records  
        Cursor c = db.query(false, "qrcode", new String[]{"COUNT(*)"}, null, null, null, null, null, null);  
          
        c.moveToNext();  
        int count =c.getInt(0);   
        c.close();  
        db.close();  
        return count;  
    } 
    
    public void storeUserInfo(String macAddress,String info){
    	String cipher="";
        SQLiteDatabase db = helper.getWritableDatabase();     
        
        ContentValues values = new ContentValues();
       
		
        try {
        	cipher = StringTransfer.encrypt(macAddress, info);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        values.put("infodata", cipher);
        
        db.insert("userinfo", "id", values);        
          
        db.close();  
    }
    
    public String withdrawUserInfo(String macAddress){
    	String info = "";  
    	try {
        	 SQLiteDatabase db = helper.getReadableDatabase();  
        
        // query all record  
	        Cursor c = db.query(false, "userinfo", new String[]{"id","infodata"}, null, null, null, null, "id ASC", null);  
	          
	        String cipher ="";
	        while (c.moveToNext())  
	        	 cipher = c.getString(1);  
	        c.close();  
	        db.close(); 
	       
				info = StringTransfer.decrypt(macAddress, cipher);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	      return info; 
    }
}
