package com.freshdirect.webapp.taglib.fdstore;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

public class BrowserInfo {
	private static final String MSIE_REGEX = "Mozilla\\/4.0.*MSIE\\s(\\d+\\.\\d+).*";
	private static final String SAFARI_REGEX = ".*AppleWebKit\\/(\\d+(\\.\\d+)?).*";
	private static final String FIREFOX_REGEX = ".*Firefox\\/(\\d+\\.\\d+(\\.\\d+)?).*";
	private static final String OPERA_REGEX = "Opera\\/(\\d+\\.\\d+(\\.\\d+)?).*";
	private static final String OPERA_REGE2 = ".*Opera\\s(\\d+\\.\\d+(\\.\\d+)?).*";

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
		Pattern p;
		Matcher m;

		p = Pattern.compile(OPERA_REGEX);
		m = p.matcher( this.userAgentString );
		if (m.matches() && m.groupCount() > 0) {
			this.type = "Opera";
			this.version = m.group(1);
			return;
		}

		p = Pattern.compile(OPERA_REGE2);
		m = p.matcher( this.userAgentString );
		if (m.matches() && m.groupCount() > 0) {
			this.type = "Opera";
			this.version = m.group(1);
			return;
		}

		p = Pattern.compile(MSIE_REGEX);
		m = p.matcher( this.userAgentString );
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
		return "MSIE".equals(type);
	}

	public boolean isIE6() {
		if (!"MSIE".equals(type))
			return false;
		
		float v = Float.parseFloat(version);
		return v < 7.0;
	}


	public boolean isWebKit() {
		return "WebKit".equals(type);
	}


	public boolean isChrome() {
		if (!"WebKit".equals(type))
			return false;
		
		return userAgentString.indexOf("Chrome") > -1;
	}

	public boolean isSafari() {
		if (!"WebKit".equals(type))
			return false;

		return userAgentString.indexOf("Safari") > -1 && userAgentString.indexOf("Chrome") == -1;
	}

	public boolean isIPhone() {
		if (!"WebKit".equals(type))
			return false;

		return userAgentString.indexOf("Mobile") > -1 && userAgentString.indexOf("Safari") > -1 && userAgentString.indexOf("Android") == -1;
	}

	public boolean isAndroid() {
		if (!"WebKit".equals(type))
			return false;

		return userAgentString.indexOf("Safari") > -1 && userAgentString.indexOf("Android") > -1;
	}

	public boolean isOpera() {
		return "Opera".equals(type);
	}


	public boolean isFirefox() {
		return "Firefox".equals(type);
	}

	public boolean isUnsupported() {
		return type == null;
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
