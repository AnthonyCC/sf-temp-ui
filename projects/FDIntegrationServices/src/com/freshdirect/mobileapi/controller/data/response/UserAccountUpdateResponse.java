package com.freshdirect.mobileapi.controller.data.response;

import com.freshdirect.mobileapi.controller.data.Message;

/**
 * Response object for the update user account information request.
 *
 */
public class UserAccountUpdateResponse extends Message {

	private ResultCode Result= ResultCode.OK;
	
	
	
	public ResultCode getResult() {
		return Result;
	}



	public void setResult(ResultCode result) {
		Result = result;
	}



	public enum ResultCode
	{
		/*
		 * Call succeeded
		 */
		OK,
		
		/*
		 * Error occurred while deserializing request object;
		 */
		REQUEST_FORMAT_ERROR,
		
		/*
		 * Password didn't meet minimum constraints.
		 * Currently, this means the password is not less than 4 characters long.
		 */
		INVALID_PASSWORD_ERROR,
		
		/*
		 * The requested user ID already exists in the system.
		 * Note: If this error occurs, then the password change successfully executed.
		 */
		USERIDALREADYTAKEN,
		
		/*
		 * User Not in the session
		 */
		USER_NOT_LOGGED_IN,
		
		/*
		 * Both userName and Password are empty
		 */
		BOTH_USERNAME_PASSWORD_EMPTY
	}
}
