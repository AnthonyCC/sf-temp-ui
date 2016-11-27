package com.freshdirect.transadmin.datamanager;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;

import com.freshdirect.routing.util.RoutingDateUtil;
import com.freshdirect.transadmin.util.TransportationAdminProperties;

public class GenericBeanDataManager extends RouteDataManager  {
	
public RoutingResult process(byte[] inputInfo, String userName, Map paramMap) throws IOException {
		
		RoutingResult result = new RoutingResult();
		StringBuffer strBuf = new StringBuffer();
		if(userName != null && userName.trim().length() > 0) {
			List inputDataList = fileManager.parseRouteFile(TransportationAdminProperties.get(userName)
					, new ByteArrayInputStream(inputInfo), ROW_IDENTIFIER, ROW_BEAN_IDENTIFIER
					, null);
			if(inputDataList != null) {
				Iterator beanIterator = inputDataList.iterator();
				if(inputDataList.size() >0) {
					Object tmpBean = inputDataList.get(0);
					String propertyKey = null;
					try {
						Map propertyMap = PropertyUtils.describe(tmpBean); 
						Object[] keySet = propertyMap.keySet().toArray();	
						strBuf.append("<thead>");
						strBuf.append("<tr>");							
						for(int intCount=0;intCount<keySet.length;intCount++) {
							propertyKey = (String)keySet[intCount];
							if(!"class".equals(propertyKey)) {
								strBuf.append("<td class=\"fileTabHeader\">");											
								strBuf.append(propertyKey.toUpperCase());
								strBuf.append("</td>");
							}
							
						}
						strBuf.append("</tr>");
						strBuf.append("</thead>");
						strBuf.append("<tbody class=\"fileTabBody\" >");
						String rowClass = "fileTabEvenRow";
						while(beanIterator.hasNext()) {
							tmpBean = beanIterator.next();
							strBuf.append("<tr class=\"").append(rowClass).append("\">");							
							for(int intCount=0;intCount<keySet.length;intCount++) {								
								propertyKey = (String)keySet[intCount];	
								if(!"class".equals(propertyKey)) {
									strBuf.append("<td>");
									strBuf.append(notNullValue(PropertyUtils.getProperty(tmpBean, propertyKey)));
									strBuf.append("</td>");
								}	
								
							}
							strBuf.append("</tr>");
							if("fileTabEvenRow".equals(rowClass)) {
								rowClass = "fileTabOddRow";
							} else {
								rowClass = "fileTabEvenRow";
							}
							
						}
						strBuf.append("</tbody>");
					} catch(Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		result.setAdditionalInfo(strBuf.toString());
		return result;
		
	}

	public static Object notNullValue(Object val) {
		if(val != null ) {
			if(val instanceof Date) {
				try {
					return RoutingDateUtil.formatDateTime((Date)val);
				} catch(ParseException e) {
					// do  nothing
				}
			} 
			return val;
		}
		return "";
	}
}
