package com.freshdirect.listadmin.render.sql;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import com.freshdirect.listadmin.db.ConstantClause;
import com.freshdirect.listadmin.db.ParamClause;
import com.freshdirect.listadmin.db.StaticDropdownClause;
import com.freshdirect.listadmin.query.ClauseI;
import com.freshdirect.listadmin.render.ClauseRendererI;

public class SqlClauseRenderFactory {
	private static Map typeMap;
	private static String myPackage;
	
	private static void addType(Class c) {
		try {
			String className = c.getName();
			int pos   = className.lastIndexOf('.');
			if(pos != -1) {
				className = className.substring(pos+1);
			}
			String rendererName = myPackage + "." + className + "Renderer";
			Class renderClass   = Class.forName(rendererName);
			
			if(renderClass != null) {
				Constructor cons = renderClass.getConstructor(new Class[] {c});
				if(cons != null) {
					typeMap.put(c.getName(),cons);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	static {
		try {
			String myClassName = SqlClauseRenderFactory.class.getName();
			int pos = myClassName.lastIndexOf('.');
			myPackage = myClassName.substring(0,pos);
			
			typeMap = new HashMap();
			addType(ParamClause.class);
			addType(ConstantClause.class);
			addType(StaticDropdownClause.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static ClauseRendererI getRenderer(ClauseI clause) {
		Constructor cons = (Constructor) typeMap.get(clause.getClass().getName());
		
		if(cons != null) {
			try {
				return (ClauseRendererI) cons.newInstance(new Object[] {clause});
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
