package com.freshdirect.webapp.taglib.fdstore;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

public class BrowserInfo {
	private static final String MSIE_REGEX = "Mozilla\\/4.0.*MSIE\\s(\\d+\\.\\d+).*";
	private static final String SAFARI_REGEX = ".*AppleWebKit\\/(\\d+\\.\\d+).*";
	private static final String FIREFOX_REGEX = ".*Firefox\\/(\\d+\\.\\d+(\\.\\d+)?).*";
	private static final String OPERA_REGEX = "Opera\\/(\\d+\\.\\d+(\\.\\d+)?).*";

	private String userAgentString;

	String type;
	String version;


	public BrowserInfo(HttpServletRequest request) {
		this.userAgentString = request.getHeader("user-agent");
		evaluate();
	}
	
	public BrowserInfo(String userAgentString) {
		this.userAgentString = userAgentString;
		evaluate();
	}
	
	private void evaluate() {
		// "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)"
		Pattern p = Pattern.compile(MSIE_REGEX);
		Matcher m = p.matcher( this.userAgentString );

		if (m.matches() && m.groupCount() > 0) {
			this.type = "MSIE";
			this.version = m.group(1);
			return;
		}
		
		p = Pattern.compile(SAFARI_REGEX);
		m = p.matcher( this.userAgentString );
		if (m.matches() && m.groupCount() > 0) {
			this.type = "WebKit";
			this.version = m.group(1);
			return;
		}

		p = Pattern.compile(FIREFOX_REGEX);
		m = p.matcher( this.userAgentString );
		if (m.matches() && m.groupCount() > 0) {
			this.type = "Firefox";
			this.version = m.group(1);
			return;
		}

		p = Pattern.compile(OPERA_REGEX);
		m = p.matcher( this.userAgentString );
		if (m.matches() && m.groupCount() > 0) {
			this.type = "Opera";
			this.version = m.group(1);
			return;
		}
	}



	

	public String getUserAgentString() {
		return userAgentString;
	}

	public String getType() {
		return type;
	}

	public String getVersion() {
		return version;
	}


	public boolean isInternetExplorer() {
		return type != null && type.equals("MSIE");
	}

	public boolean isIE6() {
		if (type == null || !type.equals("MSIE"))
			return false;
		
		float v = Float.parseFloat(version);
		return v < 7.0;
	}


	public boolean isChrome() {
		if (type == null || !type.equals("WebKit"))
			return false;
		
		return userAgentString.indexOf("Chrome") > -1;
	}

	public boolean isSafari() {
		if (type == null || !type.equals("WebKit"))
			return false;

		return userAgentString.indexOf("Safari") > -1;
	}

	public boolean isIPhone() {
		if (type == null || !type.equals("WebKit"))
			return false;

		return userAgentString.indexOf("Mobile") > -1 && userAgentString.indexOf("Safari") > -1;
	}

	public boolean isOpera() {
		return type != null && type.equals("Opera");
	}


	public boolean isFirefox() {
		return type != null && type.equals("Firefox");
	}


	public String toString() {
		StringBuffer buf = new StringBuffer();
		if (type != null)
			buf.append("[type="+this.type+";version="+this.version+"]");
		else
			buf.append("[unknown="+this.userAgentString+"]");

		return buf.toString();
	}
}
