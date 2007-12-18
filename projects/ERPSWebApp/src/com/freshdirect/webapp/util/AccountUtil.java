package com.freshdirect.webapp.util;

import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.webapp.taglib.fdstore.EnumUserInfoName;
import com.freshdirect.webapp.taglib.fdstore.SystemMessageList;

public class AccountUtil {
	
	public static final int HOME_USER= 10;
	public static final int DEPOT_USER= 20;
	public static final int CORP_USER= 30;
	
	private final static int MIN_PASSWORD_LENGTH = 4;
	
	public static void validatePassword(ActionResult result, String password, String repeatPassword) {
		if ("".equals(password)) {
			result.addError(new ActionError(EnumUserInfoName.PASSWORD.getCode(), SystemMessageList.MSG_REQUIRED));

		} else if (password.length() < MIN_PASSWORD_LENGTH) {
			result.addError(new ActionError(EnumUserInfoName.PASSWORD.getCode(), SystemMessageList.MSG_PASSWORD_LENGTH));

		} else if (!password.equals(repeatPassword)) {
			result.addError(new ActionError(EnumUserInfoName.REPEAT_PASSWORD.getCode(), SystemMessageList.MSG_PASSWORD_REPEAT));
		}
	}
}
