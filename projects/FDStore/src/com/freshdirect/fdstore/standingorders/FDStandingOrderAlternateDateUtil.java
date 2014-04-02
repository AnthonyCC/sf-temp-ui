package com.freshdirect.fdstore.standingorders;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.freshdirect.fdstore.FDResourceException;

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

public static List<String> validate(FDStandingOrderAltDeliveryDate altDate,List<String> errors){
	if(null !=altDate){
		Date currentDate = Calendar.getInstance().getTime();
		if(null ==altDate.getOrigDate()){
			errors.add("Original delivery date can't be empty");
		} else if(altDate.getOrigDate().before(currentDate)){
			errors.add("Original delivery date shouldn't be a past date");
		}
		if(EnumStandingOrderAlternateDeliveryType.ALTERNATE_DELIVERY.getName().equals(altDate.getActionType()) && null == altDate.getAltDate() ){
			errors.add("Alternate delivery date can't be empty to change the standing order delivery date");				
		} else if(null != altDate.getAltDate() && altDate.getOrigDate().before(currentDate)){
			errors.add("Alternate delivery date shouldn't be a past date");
		}		
		if(null != altDate.getSoId() && !StringUtils.isNumeric(altDate.getSoId())){
			errors.add("SO Id should be an integer");
		}
		if(errors.isEmpty()){
			boolean isDuplicate = false;
			try {
				isDuplicate = FDStandingOrdersManager.getInstance().checkIfAlreadyExists(altDate);
			} catch (FDResourceException e) {
				
			}
			if(isDuplicate){
				if(altDate.getSoId() == null){
					errors.add("There is already a standing order alternate date setup for the given delivery date");
				}else{
					errors.add("There is already a standing order alternate date setup for the given delivery date and SO Id");
				}
			}
		}
	}
	return errors;
}
}
