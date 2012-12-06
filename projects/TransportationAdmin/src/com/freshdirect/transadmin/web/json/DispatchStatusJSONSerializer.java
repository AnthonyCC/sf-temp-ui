package com.freshdirect.transadmin.web.json;

import org.json.JSONArray;
import org.json.JSONObject;

import com.freshdirect.transadmin.web.model.DispatchStatus;
import com.freshdirect.transadmin.web.model.DispatchStatusList;
import com.metaparadigm.jsonrpc.AbstractSerializer;
import com.metaparadigm.jsonrpc.MarshallException;
import com.metaparadigm.jsonrpc.ObjectMatch;
import com.metaparadigm.jsonrpc.Serializer;
import com.metaparadigm.jsonrpc.SerializerState;
import com.metaparadigm.jsonrpc.UnmarshallException;

public class DispatchStatusJSONSerializer extends AbstractSerializer {

	private static final long serialVersionUID = -2174762506074567990L;

	@SuppressWarnings("unchecked")
	private static Class[] _serializableClasses = new Class[] { DispatchStatusList.class };
	
	@SuppressWarnings("unchecked")
	private static Class[] _JSONClasses = new Class[] { JSONObject.class };
	
	private static Serializer instance = new DispatchStatusJSONSerializer();

	private DispatchStatusJSONSerializer() {
	}

	public static Serializer getInstance() {
		return instance;
	}


	@SuppressWarnings("unchecked")
	@Override
	public Class[] getJSONClasses() {
		return _JSONClasses;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Class[] getSerializableClasses() {
		return _serializableClasses;
	}


	@Override
	public Object marshall(SerializerState state, Object obj) throws MarshallException {

		if (obj instanceof DispatchStatusList) {
			JSONObject jsObject = new JSONObject();
			JSONArray jsArray = new JSONArray();
			for(DispatchStatus en : ((DispatchStatusList)obj).getDispatchStatus() ) {
				
				JSONObject _jsObject = new JSONObject();
				_jsObject.put("javaClass", en.getClass().getName());
				_jsObject.put("dispatchId", en.getDispatchId());			
				_jsObject.put("isKeysReady", en.isKeysReady());
				_jsObject.put("phoneAssigned", en.isPhoneAssigned());
				_jsObject.put("isDispatched", en.isDispatched());
				_jsObject.put("isCheckedIn", en.isCheckedIn());
				_jsObject.put("isKeysIn", en.isKeysIn());
				jsArray.put(_jsObject);
			}		
			jsObject.put("dispatch", jsArray);
			
			return jsObject;
		}
		throw new MarshallException("Invalid class " + obj.getClass());
	}

	@SuppressWarnings("unchecked")
	@Override
	public ObjectMatch tryUnmarshall(SerializerState state, Class clazz, Object obj) throws UnmarshallException {
		
		unmarshall(state, clazz, obj);
		return ObjectMatch.OKAY;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object unmarshall(SerializerState state, Class klass, Object obj) throws UnmarshallException {

		final JSONObject jsObject = (JSONObject) obj;
		JSONArray jsArray = jsObject.getJSONArray("dispatch");
		DispatchStatusList result = new DispatchStatusList();
		for(int i=0 ; i < jsArray.length() ; i++) {
			result.getDispatchStatus().add(new DispatchStatus(((JSONObject)jsArray.get(i)).getString("dispatchId")
																	, ((JSONObject)jsArray.get(i)).getBoolean("isKeysReady")
																	, ((JSONObject)jsArray.get(i)).getBoolean("phoneAssigned")
																	, ((JSONObject)jsArray.get(i)).getBoolean("isDispatched")																	
																	, ((JSONObject)jsArray.get(i)).getBoolean("isCheckedIn")
																	, ((JSONObject)jsArray.get(i)).getBoolean("isKeysIn")));
		}
		return result;
	}
}
