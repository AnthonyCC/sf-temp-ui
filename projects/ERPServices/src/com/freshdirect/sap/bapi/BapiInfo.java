/*
 * $Workfile: BapiInfo.java$
 *
 * $Date: 8/27/2001 7:19:01 PM$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.sap.bapi;

import java.io.Serializable;

/**
 * Meta-information about a BapiResult. Based on the "return" structure in the response.
 *
 * @version $Revision: 1$
 * @author $Author: Viktor Szathmary$
 */
public class BapiInfo implements Serializable {
	public final static String TYPE_ABORT = "A";
	public final static String TYPE_ERROR = "E";
	public final static String TYPE_WARNING = "W";
	public final static String TYPE_INFO = "I";
	public final static String TYPE_SUCCESS = "S";
	
	public final static int LEVEL_INFO = 0;
	public final static int LEVEL_WARNING = 1;
	public final static int LEVEL_ERROR = 2;
	public final static int LEVEL_UNKNOWN = 3;
	
	private int level;
	private String type;
	private String code;
	private String logNo;
	private String logMsgNo;
	private String message;

	public BapiInfo(String type, String code, String logNo, String logMsgNo, String message) {
		this.type=type;
		
		if (TYPE_ABORT.equals(type) || TYPE_ERROR.equals(type)) {
			this.level = LEVEL_ERROR;
		} else if (TYPE_WARNING.equals(type)) {
			this.level = LEVEL_WARNING;
		} else if (TYPE_INFO.equals(type) || TYPE_SUCCESS.equals(type)) {
			this.level = LEVEL_INFO;
		} else {
			this.level = LEVEL_UNKNOWN;	
		}

		this.code = code;
		this.logNo = logNo;
		this.logMsgNo = logMsgNo;
		this.message = message;
	}

	public int getLevel() {
		return this.level;
	}

	public String getType() {
		return this.type;
	}
	
	public String getCode() {
		return this.code;
	}

	public String getLogNo() {
		return this.logNo;
	}

	public String getLogMsgNo() {
		return this.logMsgNo;
	}

	public String getMessage() {
		return this.message;
	}

	public String toString() {
		return "BapiInfo[level=" + level + ",type=" + type +  ",code="+ code + ",msg=" + message +"]";
	}
}
