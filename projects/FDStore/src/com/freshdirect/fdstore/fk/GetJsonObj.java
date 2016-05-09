package com.freshdirect.fdstore.fk;

import java.io.FileInputStream;
import java.util.Iterator;
import java.util.HashMap;
import java.util.Scanner;
import org.json.JSONObject;
import org.json.JSONArray;

import javax.servlet.http.HttpServletRequest;

public class GetJsonObj{
	public GetJsonObj(){}

	public static JSONObject file2jsonObj( String jsonfile, HttpServletRequest request ) {
		String path = request.getSession().getServletContext().getRealPath( jsonfile );
		String jsonStr = "";
		
		FileInputStream inputStream = null;
		Scanner sc = null;

		try{
			inputStream = new FileInputStream(path);
			sc = new Scanner(inputStream, "UTF-8");
			
			try{
				while (sc.hasNextLine()){
					jsonStr += sc.nextLine();
				}
				
				if (sc.ioException() != null) {
			        throw sc.ioException();
			    }
			} finally {
			    if (inputStream != null) {
			        inputStream.close();
			    }
			    if (sc != null) {
			        sc.close();
			    }
			}
			
			return new JSONObject( jsonStr );
			
		} catch (Exception e) { }
		
		//if nothing is yielded from the file read attempt
		return null;
	}
	
	public static HashMap configJson2hm(JSONObject jso){
		HashMap hm = null;
		Iterator iterator = null;
		JSONArray tempJSA = null;
		
		try{
			iterator = jso.keys();
			hm = new HashMap();

			String key = null;
			while (iterator.hasNext()) {
				key = (String) iterator.next();

				tempJSA = (JSONArray) jso.get(key);
				
				hm.put(key, (String) tempJSA.get(0) );
			}
			
			return hm;
		} catch (Exception e) {}
		
		return null;
	}
}