package com.freshdirect.transadmin.web.ui;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.extremecomponents.table.bean.Column;
import org.extremecomponents.table.cell.AbstractCell;
import org.extremecomponents.table.core.TableModel;

public class FDEnumCell extends AbstractCell  {
		    
    protected String getCellValue(TableModel model, Column column) {
    	String format = column.getFormat();
        String value = column.getValueAsString();
                 
		try {
			Class clazz = Class.forName(format);
			Method mainMethod = clazz.getMethod("getEnum", new Class[]{String.class});
	        Object methodResult = mainMethod.invoke(null, new Object[]{column.getValueAsString()});
	        if(methodResult != null) {
	        	value = (String)methodResult.getClass().getMethod("getDescription", new Class[]{}).invoke(methodResult, null);
	        }
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch blOck
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return value;
    }
}
