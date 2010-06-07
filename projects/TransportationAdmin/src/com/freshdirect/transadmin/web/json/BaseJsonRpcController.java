package com.freshdirect.transadmin.web.json;

import java.text.ParseException;
import java.util.Date;

import com.freshdirect.transadmin.util.TransStringUtil;

public class BaseJsonRpcController extends JsonRpcController {
	
	protected Date getFromClientDate(String clientDate) {
		Date retDate = null;
		try {
			retDate = TransStringUtil.getDate(clientDate);
		} catch(ParseException parExp) {
			parExp.printStackTrace();
		}
		return retDate;
	}
	
	protected String getParsedDate(Date _date) {

		try {
			return TransStringUtil.getServerDate(_date);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
}
