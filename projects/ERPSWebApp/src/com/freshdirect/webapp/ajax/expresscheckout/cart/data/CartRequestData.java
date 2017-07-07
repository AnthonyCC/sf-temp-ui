package com.freshdirect.webapp.ajax.expresscheckout.cart.data;

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
	private Map<Integer,Change> change;
    private String page;
    private String warningMessage;
	
	public Object getHeader() {
		return header;
	}	
	public void setHeader( Object header ) {
		this.header = header;
	}	
	public Map<Integer,Change> getChange() {
		return change;
	}	
	public void setChange( Map<Integer,Change> change ) {
		this.change = change;
	}
	
    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getWarningMessage() {
        return warningMessage;
    }

    public void setWarningMessage(String warningMessage) {
        this.warningMessage = warningMessage;
    }

    public static class Change implements Serializable {
		
		private static final long	serialVersionUID	= -506884437929605359L;
		
		public static final String CHANGE_QUANTITY = "cqu"; 
		public static final String CHANGE_SALESUNIT = "csu"; 
		public static final String REMOVE = "rmv";
		// make-good order case
		public static final String CHANGE_COMPLAINT_REASON = "ccr";
		
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
