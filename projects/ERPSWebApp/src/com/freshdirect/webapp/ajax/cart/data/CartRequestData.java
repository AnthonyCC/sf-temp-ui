package com.freshdirect.webapp.ajax.cart.data;

import java.io.Serializable;
import java.util.Map;

/**
 *	Simple java bean for cart requests. 
 *	Class structure is representing the received JSON structure. 	
 * 
 * @author treer
 */
public class CartRequestData implements Serializable {
	
	private static final long	serialVersionUID	= -7995056288199323295L;

	private Object header;
	private Map<Integer,Change> data;
	
	public Object getHeader() {
		return header;
	}	
	public void setHeader( Object header ) {
		this.header = header;
	}	
	public Map<Integer,Change> getData() {
		return data;
	}	
	public void setData( Map<Integer,Change> data ) {
		this.data = data;
	}
	
	public static class Change {
		
		public static final String CHANGE_QUANTITY = "cqu"; 
		public static final String CHANGE_SALESUNIT = "csu"; 
		public static final String REMOVE = "rmv"; 
		
		private String type;
		private Object data;
		
		public String getType() {
			return type;
		}		
		public void setType( String type ) {
			this.type = type;
		}		
		public Object getData() {
			return data;
		}		
		public void setData( Object data ) {
			this.data = data;
		}
	}
	
}
