package com.freshdirect.webapp.util.json;

import org.json.JSONObject;

import com.freshdirect.fdstore.standingorders.EnumStandingOrderFrequency;
import com.metaparadigm.jsonrpc.AbstractSerializer;
import com.metaparadigm.jsonrpc.MarshallException;
import com.metaparadigm.jsonrpc.ObjectMatch;
import com.metaparadigm.jsonrpc.Serializer;
import com.metaparadigm.jsonrpc.SerializerState;
import com.metaparadigm.jsonrpc.UnmarshallException;

public class EnumStandingOrderFrequencyJSONSerializer extends AbstractSerializer {

	private static final long serialVersionUID = -2174762506075217990L;

	@SuppressWarnings("unchecked")
	private static Class[] _serializableClasses = new Class[] { EnumStandingOrderFrequency.class };
	
	@SuppressWarnings("unchecked")
	private static Class[] _JSONClasses = new Class[] { JSONObject.class };
	
	private static Serializer instance = new EnumStandingOrderFrequencyJSONSerializer();

	private EnumStandingOrderFrequencyJSONSerializer() {
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

		if (obj instanceof EnumStandingOrderFrequency) {
			EnumStandingOrderFrequency en = (EnumStandingOrderFrequency) obj;
			
			JSONObject jsObject = new JSONObject();
			jsObject.put("javaClass", en.getClass().getName());
			jsObject.put("_ord", en.ordinal());
			jsObject.put("title", en.getTitle());
			jsObject.put("frequency", en.getFrequency());
			
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
		final int ord = jsObject.getInt("_ord");
		
		for (EnumStandingOrderFrequency item : EnumStandingOrderFrequency.values()) {
			if (item.ordinal() == ord)
				return item;
		}

		return null;
	}
}
