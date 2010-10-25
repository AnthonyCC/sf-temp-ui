package com.freshdirect.framework.webapp;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.VariableInfo;

public abstract class BodyTagSupportEx extends BodyTagSupport {
	private static final long serialVersionUID = 5008632767031851375L;
	
	protected HttpServletRequest request;

	@Override
	public void setPageContext(PageContext pageContext) {
		super.setPageContext(pageContext);
		request = (HttpServletRequest) pageContext.getRequest();
	}
	
	@Override
	public void release() {
		request = null;
		super.release();
	}

	public static VariableInfo declareVariableInfo(String name, Type type, int scope) {
		return new VariableInfo(name, getTypeName(type), true, scope);
	}
	
	@SuppressWarnings("unchecked")
	public static String getTypeName(Type type) {
		if (type instanceof Class)
			return ((Class) type).getName().replace('$', '.');
		else if (type instanceof ParameterizedType) {
			ParameterizedType pType = (ParameterizedType) type;
			StringBuilder buf = new StringBuilder();
			buf.append(getTypeName(pType.getRawType()));
			buf.append("<");
			Type[] typeArgs = pType.getActualTypeArguments();
			if (typeArgs.length > 0)
				buf.append(getTypeName(typeArgs[0]));
			for (int i = 1; i < typeArgs.length; i++) {
				buf.append(",");
				buf.append(getTypeName(typeArgs[i]));
			}
			buf.append(">");
			return buf.toString();
		} else if (type instanceof GenericArrayType) {
			GenericArrayType gaType = (GenericArrayType) type;
			return getTypeName(gaType.getGenericComponentType()) + "[]";
		} else {
			return Object.class.getName();
		}
	}
}
