package com.freshdirect.webapp.util.json;

import com.metaparadigm.jsonrpc.AbstractSerializer;
import com.metaparadigm.jsonrpc.MarshallException;
import com.metaparadigm.jsonrpc.ObjectMatch;
import com.metaparadigm.jsonrpc.Serializer;
import com.metaparadigm.jsonrpc.SerializerState;
import com.metaparadigm.jsonrpc.UnmarshallException;

import org.json.JSONObject;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import com.freshdirect.fdstore.FDConfiguration;

/** Serialize FDConfiguration objects.
 * 
 */
public class FDConfigurationJSONSerializer extends AbstractSerializer implements
		Serializer {


	/** id */
	private static final long serialVersionUID = 4031781516349965787L;
	
	private static FDConfigurationJSONSerializer instance = new FDConfigurationJSONSerializer(true);
	private static FDConfigurationJSONSerializer noClassTagInstance = new FDConfigurationJSONSerializer(false);
	
	/** Get a reusable serializer.
	 * 
	 * @return an FDConfiguration serializer which uses class tags
	 */
	public static Serializer getInstance() { return instance; }
	
	/** Get a reusable serializer.
	 * 
	 * The serializer returned does not use class tags; thus it can only be used within a context where
	 * the FDconfiguration instance is implicitly understood or to marshall objects.
	 * 
	 * @return an FDConfiguration serializes which does not use class tags.
	 */
	public static Serializer getNoClassTagInstance() { return noClassTagInstance; }
	
	private static Class[] _serializableClasses = new Class[] { FDConfiguration.class };
	private static Class[] _JSONClasses = new Class[] { JSONObject.class };
	
	/** whether to use class tagging */
	private boolean useClassTag;
	
	private FDConfigurationJSONSerializer(boolean useTag) {
		useClassTag = useTag;
	}
	
	/** JSON classes.
	 *  @return {org.json.JSONObject}
	 */
	public Class[] getJSONClasses() {
		return _JSONClasses;
	}

	/** Java classes.
	 * @return {FDConfiguration.class}
	 */
	public Class[] getSerializableClasses() {
		return _serializableClasses;
	}

	/** Marshall FDConfiguration instance.
	 * @param state serializer state
	 * @param object FDConfiguration object
	 * @return JSON object for FDConfiguration
	 */
	public Object marshall(SerializerState state, Object object)
			throws MarshallException {
		FDConfiguration conf = (FDConfiguration)object;
		JSONObject ser_obj = new JSONObject();
		if (useClassTag) ser_obj.put("javaClass", com.freshdirect.fdstore.FDConfiguration.class.getName());
		ser_obj.put("quantity", conf.getQuantity());
		ser_obj.put("salesUnit", conf.getSalesUnit());
		JSONObject ser_options = new JSONObject();
		for(Iterator i = conf.getOptions().entrySet().iterator(); i.hasNext(); ) {
			Map.Entry option = (Map.Entry)i.next();
			ser_options.put((String)option.getKey(),option.getValue());
		}
		ser_obj.put("options", ser_options);
		return ser_obj;
	}

	public ObjectMatch tryUnmarshall(SerializerState state, Class clazz,
			Object o) throws UnmarshallException {
		unmarshall(state,clazz,o);
		return ObjectMatch.OKAY;
	}

	/** Unmarshall JSON FDConfiguration object.
	 * @param state serializer state
	 * @param clazz class passed in by serializer
	 * @param o JSON FDConfiguration object
	 * @return FDConfiguration instance
	 */
	public Object unmarshall(SerializerState state, Class clazz, Object o)
			throws UnmarshallException {
		JSONObject jsObject = (JSONObject)o;
		if (useClassTag) {
		    String jClass = jsObject.getString("javaClass");
		    if (jClass == null) throw new UnmarshallException("no type hint");
		    if (!FDConfiguration.class.getName().equals(jClass)) 
			    throw new UnmarshallException(FDConfiguration.class.getName() + " expected for javaClass instead of " + jClass);
		}
	    // if class specified
		if (clazz != null) {
			if (!clazz.equals(FDConfiguration.class)) 
				throw new UnmarshallException(FDConfiguration.class.getName() + " expected instead of " + clazz);
		}
		double quant = jsObject.getDouble("quantity");
		String salesUnit = jsObject.getString("salesUnit");
		
		Map options = new TreeMap();
		JSONObject jsOptions = jsObject.getJSONObject("options");
		for(Iterator i = jsOptions.keys(); i.hasNext();) {
		   String key = (String)i.next();
		   options.put(key,jsOptions.get(key));
		}
		return new FDConfiguration(quant,salesUnit,options);
	}

}
