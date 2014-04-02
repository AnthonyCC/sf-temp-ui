package com.freshdirect.fdstore.standingorders;

import java.util.Iterator;
import java.util.List;

public class FDStandingOrderAlternateDateUtil {
	
public static String buildResponse(List<String> errors) {
		
		StringBuffer buffer = new StringBuffer();
		
		if(null !=errors && errors.size() > 0) {
			buffer.append("<table  valign=\"top\" width=\"480\" cellpadding=\"0\" cellspacing=\"0\">");
			if(errors.size() > 0) {
				buffer.append("<tr>").append("<th   colspan=\"4\" align=\"center\">").append("ERRORS ").append("</th>").append("</tr>");
				Iterator<String> itr = errors.iterator();
				buffer.append("<tr>");
				buffer.append("<td  colspan=\"4\">");
				while(itr.hasNext()) {
					String exception = itr.next();
					buffer.append(exception).append("<br/>");
				}				
				buffer.append("<br/></td>");
				buffer.append("</tr>");	
			}			
			
			buffer.append("</table>");	
		}
		
		return buffer.toString();
	}
}
