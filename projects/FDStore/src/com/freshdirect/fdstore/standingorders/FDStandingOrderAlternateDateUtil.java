package com.freshdirect.fdstore.standingorders;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.freshdirect.fdstore.FDResourceException;

public class FDStandingOrderAlternateDateUtil {
	
private static final String ERROR_MSG_ALREADY_STANDING_ORDER_ALT_DATE_FOR_DELIVERY_DATE = " There is already a standing order alternate date setup for the given delivery date: ";
private static final String ERROR_MSG_SO_ID_INVALID = " SO Id is invalid. It should be an integer.";
private static final String ERROR_MSG_ALTERNATE_DELIVERY_DATE_INVALID = " Alternate delivery date is invalid. It shouldn't be a past date.";
private static final String ERROR_MSG_ALTERNATE_DELIVERY_DATE_EMPTY = " Alternate delivery date can't be empty to change the standing order delivery date.";
private static final String ERROR_MSG_ORIGINAL_DELIVERY_DATE_INVALID = " Original delivery date shouldn't be a past date.";
private static final String ERROR_MSG_ORIGINAL_DELIVERY_DATE_EMPTY = " Original delivery date can't be empty.";
private static final String ERROR_MSG_ROW_NUM = " At row num: ";
private static final String ERROR_MSG_SO_ID = " and SO Id: ";


public static String buildResponse(List<String> errors) {
		
		StringBuffer buffer = new StringBuffer();
		
		if(null !=errors && errors.size() > 0) {
			buffer.append("<table  valign=\"top\" width=\"480\" cellpadding=\"0\" cellspacing=\"0\">");
			if(errors.size() > 0) {
				buffer.append("<tr>").append("<th   colspan=\"4\" align=\"center\">").append("<U>Errors</U> ").append("</th>").append("</tr>");
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

public static List<String> validate(FDStandingOrderAltDeliveryDate altDate,List<String> errors,Integer rowNum){
	if(null == rowNum){
		return validate(altDate, errors);
	}else{
		if(null !=altDate){
			Date currentDate = Calendar.getInstance().getTime();
			if(null ==altDate.getOrigDate()){
				errors.add(ERROR_MSG_ROW_NUM+rowNum+ERROR_MSG_ORIGINAL_DELIVERY_DATE_EMPTY);
			} else if(altDate.getOrigDate().before(currentDate)){
				errors.add(ERROR_MSG_ROW_NUM+rowNum+", "+altDate.getOrigDateFormatted()+ERROR_MSG_ORIGINAL_DELIVERY_DATE_INVALID);
			}
			if(EnumStandingOrderAlternateDeliveryType.ALTERNATE_DELIVERY.getName().equals(altDate.getActionType()) && null == altDate.getAltDate() ){
				errors.add(ERROR_MSG_ROW_NUM+rowNum+ERROR_MSG_ALTERNATE_DELIVERY_DATE_EMPTY);				
			} else if(null != altDate.getAltDate() && altDate.getAltDate().before(currentDate)){
				errors.add(ERROR_MSG_ROW_NUM+rowNum+", "+altDate.getAltDateFormatted()+ERROR_MSG_ALTERNATE_DELIVERY_DATE_INVALID);
			}		
			if(null != altDate.getSoId() && !StringUtils.isNumeric(altDate.getSoId())){
				errors.add(ERROR_MSG_ROW_NUM+rowNum+", "+altDate.getSoId()+ERROR_MSG_SO_ID_INVALID);
			}
			if(errors.isEmpty()){
				boolean isDuplicate = false;
				try {
					isDuplicate = FDStandingOrdersManager.getInstance().checkIfAlreadyExists(altDate);
				} catch (FDResourceException e) {
					
				}
				if(isDuplicate){
					if(altDate.getSoId() == null || "".equals(altDate.getSoId())){
						errors.add(ERROR_MSG_ROW_NUM+rowNum+ERROR_MSG_ALREADY_STANDING_ORDER_ALT_DATE_FOR_DELIVERY_DATE+altDate.getOrigDateFormatted());
					}else{
						errors.add(ERROR_MSG_ROW_NUM+rowNum+ERROR_MSG_ALREADY_STANDING_ORDER_ALT_DATE_FOR_DELIVERY_DATE+altDate.getOrigDateFormatted()+ERROR_MSG_SO_ID+altDate.getSoId());
					}
				}
			}
		}	
		return errors;
	}
}

private static List<String> validate(FDStandingOrderAltDeliveryDate altDate,
		List<String> errors) {
	if(null !=altDate){
		Date currentDate = Calendar.getInstance().getTime();
		if(null ==altDate.getOrigDate()){
			errors.add(ERROR_MSG_ORIGINAL_DELIVERY_DATE_EMPTY);
		} else if(altDate.getOrigDate().before(currentDate)){
			errors.add(ERROR_MSG_ORIGINAL_DELIVERY_DATE_INVALID);
		}
		if(EnumStandingOrderAlternateDeliveryType.ALTERNATE_DELIVERY.getName().equals(altDate.getActionType()) && null == altDate.getAltDate() ){
			errors.add(ERROR_MSG_ALTERNATE_DELIVERY_DATE_EMPTY);				
		} else if(null != altDate.getAltDate() && altDate.getAltDate().before(currentDate)){
			errors.add(altDate.getAltDateFormatted()+ERROR_MSG_ALTERNATE_DELIVERY_DATE_INVALID);
		}		
		if(null != altDate.getSoId() && !StringUtils.isNumeric(altDate.getSoId())){
			errors.add(altDate.getSoId()+ERROR_MSG_SO_ID_INVALID);
		}
		if(errors.isEmpty()){
			boolean isDuplicate = false;
			try {
				isDuplicate = FDStandingOrdersManager.getInstance().checkIfAlreadyExists(altDate);
			} catch (FDResourceException e) {
				
			}
			if(isDuplicate){
				if(altDate.getSoId() == null){
					errors.add(ERROR_MSG_ALREADY_STANDING_ORDER_ALT_DATE_FOR_DELIVERY_DATE+altDate.getOrigDateFormatted());
				}else{
					errors.add(ERROR_MSG_ALREADY_STANDING_ORDER_ALT_DATE_FOR_DELIVERY_DATE+altDate.getOrigDateFormatted()+ERROR_MSG_SO_ID+altDate.getSoId());
				}
			}
		}
	}
	return errors;
}
}
