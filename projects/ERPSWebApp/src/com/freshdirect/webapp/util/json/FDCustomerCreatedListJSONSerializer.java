package com.freshdirect.webapp.util.json;

import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;

import com.freshdirect.fdstore.FDConfiguration;
import com.freshdirect.fdstore.lists.FDCustomerCreatedList;
import com.freshdirect.fdstore.lists.FDCustomerProductListLineItem;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.util.NVL;
import com.metaparadigm.jsonrpc.AbstractSerializer;
import com.metaparadigm.jsonrpc.MarshallException;
import com.metaparadigm.jsonrpc.ObjectMatch;
import com.metaparadigm.jsonrpc.Serializer;
import com.metaparadigm.jsonrpc.SerializerState;
import com.metaparadigm.jsonrpc.UnmarshallException;


/** Serialize (marshall and unmarshall) product selection objects bundled in a collection.
 *
 */
public class FDCustomerCreatedListJSONSerializer extends AbstractSerializer
		implements Serializer {
	
	private static final String nullId = "__null";
	
	/** Generated ID */
	private static final long serialVersionUID = -1561691060365246437L;
	
	private static Class[] _serializableClasses = new Class[] { FDCustomerCreatedList.class };
	private static Class[] _JSONClasses = new Class[] { JSONObject.class };
	
	private static Serializer instance = new FDCustomerCreatedListJSONSerializer();
	
	/** Use getInstance() */
	private FDCustomerCreatedListJSONSerializer() {}
	
	/** Get a reusable instance of this serializer.
	 * 
	 * @return serializer instance
	 */
	public static Serializer getInstance() { return instance; }
	
	/** Recognized JSON classes 
	 * @return {org.json.JSONObject}
	 */
	public Class[] getJSONClasses() {
		return _JSONClasses;
	}

	/** Serializable java classes
	 * @return {FDCustomerCreateListLineItems.class}
	 */
	public Class[] getSerializableClasses() {
		return _serializableClasses;
	}

	/**
	 * @param state serializer state
	 * @param o FDCustomerCreatedList instance
	 * @return JSON FDCustomerCreatedList object (as org.json.JSONObject)
	 */
	public Object marshall(SerializerState state, Object o)
			throws MarshallException {
		FDCustomerCreatedList selection = (FDCustomerCreatedList)o;
		JSONObject jsObject = new JSONObject();
		jsObject.put("javaClass",o.getClass().getName());
		JSONArray jsArray = new JSONArray();
		for(Iterator i = selection.getLineItems().iterator(); i.hasNext();) {
			
			FDCustomerProductListLineItem lineItem = (FDCustomerProductListLineItem)i.next();
			JSONObject jsItem= new JSONObject();
			jsItem.put("fullName", lineItem.getFullName());
			jsItem.put("skuCode",lineItem.getSkuCode());
			jsItem.put("recipeSourceId",NVL.apply(lineItem.getRecipeSourceId(),nullId));
			jsItem.put(
				"configuration",
				FDConfigurationJSONSerializer.getNoClassTagInstance().marshall(state,lineItem.getConfiguration()));
			jsItem.put("categoryID", lineItem.getCategoryId());
			jsItem.put("productID", lineItem.getProductId());
			if (lineItem.getPK() != null)
				jsItem.put("lineID", lineItem.getPK().getId());
			else
				jsItem.put("lineID", nullId);
			jsArray.put(jsItem);
		}
		jsObject.put("selection", jsArray);

		if (selection.getId() != null)
			jsObject.put("listID", selection.getId());
		else
			jsObject.put("listID", nullId);

		return jsObject;
	}

	public ObjectMatch tryUnmarshall(SerializerState state, Class clazz,
			Object o) throws UnmarshallException {
		unmarshall(state,clazz,o);
		return ObjectMatch.OKAY;
	}

	/** Unmarshall JSON object.
	 * 
	 * @param state serializer state
	 * @param clazz class passed by JSONSerializer (expected)
	 * @param o JSON object on stream
	 * @return FDCustomerCreatedList instance
	 */
	public Object unmarshall(SerializerState state, Class clazz, Object o)
			throws UnmarshallException {
		JSONObject jsObject = (JSONObject)o;
		String jClass = jsObject.getString("javaClass");
		if (jClass == null) throw new UnmarshallException("no type hint");
		try {
		    if (!FDCustomerCreatedList.class.isAssignableFrom(Class.forName(jClass))) 
			    throw new UnmarshallException("Class implementing  " + FDCustomerCreatedList.class.getName() +
				    " expected for javaClass instead of " + jClass);
		} catch(ClassNotFoundException e) {
			throw new UnmarshallException("Unrecognized class " + jClass);
		}
		if (clazz != null) {
			if (!clazz.isAssignableFrom(clazz)) { 
				throw new UnmarshallException("Class implementing " + FDCustomerCreatedList.class.getName() +
					" expected instead of " + clazz);
			}
		}
			

		FDCustomerCreatedList selection = new FDCustomerCreatedList();		
		if (!nullId.equals(jsObject.getString("listID"))) {
			selection.setId(jsObject.getString("listID"));
		}


		JSONArray jsArray = jsObject.getJSONArray("selection");
		for(int i = 0; i< jsArray.length(); ++i) {
			JSONObject jsItem = jsArray.getJSONObject(i);
			String recipeSourceId = jsItem.getString("recipeSourceId");
			FDCustomerProductListLineItem lineItem = 
				new FDCustomerProductListLineItem(
					jsItem.getString("skuCode"),
					(FDConfiguration)FDConfigurationJSONSerializer.getNoClassTagInstance().unmarshall(
							state,
							FDConfiguration.class,
							jsItem.get("configuration")
					    ),
					nullId.equals(recipeSourceId) ? null : recipeSourceId
				);
			if (!nullId.equals(jsItem.getString("lineID")))
				lineItem.setPK( new PrimaryKey(jsItem.getString("lineID")) );

			selection.addLineItem(lineItem);
		}
		
		return selection;
	}
}
