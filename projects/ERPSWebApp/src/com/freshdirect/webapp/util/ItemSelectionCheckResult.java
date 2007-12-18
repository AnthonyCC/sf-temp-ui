package com.freshdirect.webapp.util;


import org.json.JSONObject;
import org.json.JSONArray;

import com.metaparadigm.jsonrpc.AbstractSerializer;
import com.metaparadigm.jsonrpc.MarshallException;
import com.metaparadigm.jsonrpc.ObjectMatch;
import com.metaparadigm.jsonrpc.Serializer;
import com.metaparadigm.jsonrpc.SerializerState;
import com.metaparadigm.jsonrpc.UnmarshallException;
import com.metaparadigm.jsonrpc.JSONSerializer;

import java.util.Iterator;
import java.util.Collection;
import java.util.LinkedList;

import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.framework.webapp.ActionWarning;

import com.freshdirect.fdstore.lists.FDCustomerCreatedList;
import com.freshdirect.webapp.util.json.FDCustomerCreatedListJSONSerializer;

public class ItemSelectionCheckResult {
	
	public static final String COPY_SELECTION = "copy_selection";
	public static final String SAVE_SELECTION = "save_selection";
	
	private String listName = null;
	private FDCustomerCreatedList selection = null;
	private String responseType = "invalid"; // by defualt
	private Integer lineCount = null;
	
	private Collection errors = new LinkedList(); 
    private Collection warnings = new LinkedList();
    
    /** Constructs a selection check with null fields and "invalid" response type.
     *
     */
    public ItemSelectionCheckResult() {
    }
    
    
    public void setListName(String listName) {
    	this.listName = listName;
    }
    
    public void setSelection(FDCustomerCreatedList selection) {
    	this.selection = selection;
    	this.lineCount = new Integer(selection.getLineItems().size());
    }
    
    public void setResponseType(String responseType) {
    	this.responseType = responseType;
    }
    
    public void setErrors(Collection errors) {
    	this.errors = errors;
    }
    
    public void setWarnings(Collection warnings) {
    	this.warnings = warnings;
    }
    
    /** Serialize ItemSekectionCheckResult instances.
     * No unmarshalling.
     */
    private static class CheckSerializer extends AbstractSerializer implements Serializer {
    	
    	private static Serializer instance = new CheckSerializer();
    	
		private static final long serialVersionUID = 3689567450323264979L;
		
		private static final Class[] _JSONClasses = new Class[] {};
    	private static final Class[] _serializableClasses = new Class[] { ItemSelectionCheckResult.class };
    	
    	public Class[] getJSONClasses() {
    		return _JSONClasses;
    	}

    	public Class[] getSerializableClasses() {
    		return _serializableClasses;
    	}
    	
    	/***
    	 * <li>type: response type (copy or save)
    	 * <li>errors: a list of errors discovered during the check
    	 * <li>warnings: a list of warnings
    	 * <li>items: selected products with sku and configuration
    	 * <li>list_name: target list name (only for copying)
    	 * <li>item_count: number of selected items
    	 */
    	public Object marshall(SerializerState state, Object o)
		throws MarshallException {
    		ItemSelectionCheckResult checkResult = (ItemSelectionCheckResult)o;
    		JSONObject jsObject = new JSONObject();
    		
    		jsObject.put("type", checkResult.responseType);
    		if (checkResult.selection != null)
    			jsObject.putOpt("items",FDCustomerCreatedListJSONSerializer.getInstance().marshall(state,checkResult.selection));
   
    		JSONArray jsErrors = new JSONArray();
    		for(Iterator I = checkResult.errors.iterator(); I.hasNext();) {
    			 ActionError error = (ActionError)I.next();
    			 JSONObject jsError = new JSONObject();
    			 jsError.put("type", error.getType());
    			 jsError.put("description", error.getDescription());
    			 jsErrors.put(jsError);

    		}
    		jsObject.put("errors",jsErrors);
    		
    		JSONArray jsWarnings = new JSONArray();
    		for(Iterator I = checkResult.warnings.iterator(); I.hasNext();) {
    			 ActionWarning warning = (ActionWarning)I.next();
    			 JSONObject jsWarning = new JSONObject();
    			 jsWarning.put("type", warning.getType());
    			 jsWarning.put("description", warning.getDescription());
    			 jsWarnings.put(jsWarning);

    		}
    		jsObject.put("warnings",jsWarnings);
    		
    		jsObject.putOpt("list_name", checkResult.listName);
    		jsObject.putOpt("line_count",checkResult.lineCount);
    		return jsObject;
    	}
    	
    	/** no unmarshalling */
    	public Object unmarshall(SerializerState state, Class clazz,
    			Object o) throws UnmarshallException {
    		throw new UnmarshallException(ItemSelectionCheckResult.class.getName() + " cannot be unmarshalled");
    	}
    	
    	/** no unmarshalling */
    	public ObjectMatch tryUnmarshall(SerializerState state, Class clazz,
    			Object o) throws UnmarshallException {
    		throw new UnmarshallException(ItemSelectionCheckResult.class.getName() + " cannot be unmarshalled");
    	}
    }
    
    private final static JSONSerializer ser = new JSONSerializer();
    static {
    	try {
    	    ser.registerSerializer(CheckSerializer.instance);
    	} catch (Exception e) {
    		throw new RuntimeException(e);
    	}
    }
    
    /** Produce a "pretty printed" JSON string.
     * 
     * @return JSON string of this instance
     * @throws MarshallException
     */
    public String toJSON() throws MarshallException {
    	return ((JSONObject)ser.marshall(new SerializerState(), this)).toString(4);
    }
    
}
