package com.freshdirect.framework.util;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public class StringUtil {
	
	private static final String DEFAULT_SEPARATOR = ",";
	
	public static String leftPad(String strToPad, int maxLength, char padChar) {
		String paddedStr = strToPad;
		if (strToPad != null  && maxLength > 0) {
			int numCharsToPad = maxLength - strToPad.length();
			for (int i = 0; i < numCharsToPad; i++) {
				paddedStr = padChar + paddedStr;
			}
		}
		return paddedStr;
	}
	
	public static String rightPad(String strToPad, int maxLength, char padChar) {
		String paddedStr = strToPad;
		if (strToPad != null && maxLength > 0) {
			int numCharsToPad = maxLength - strToPad.length();
			for (int i = 0; i < numCharsToPad; i++) {
				paddedStr += padChar;
			}
		}
		return paddedStr;
	}

	public static String smartCapitalize(String str) {
		if (str==null) {
			return "";
		}
		
		// replace underscore w/ space
		String s = str.replace('_', ' ');

		// insert a space after any lowerchase char followed by an uppercase
		StringBuffer sb = new StringBuffer(s);
		for (int i = 0; i < sb.length() - 1; i++) {
			if (Character.isLowerCase(sb.charAt(i))
					&& Character.isUpperCase(sb.charAt(i + 1))) {
				sb.insert(i+1, ' ');
				i++;
			}
		}

		// capitalize words
		return StringUtils.capitaliseAllWords(sb.toString().toLowerCase());
	}
	
	
	public static String escapeHTML(String str) {
		if (str == null) return "";
		StringBuffer buff = new StringBuffer(3*str.length()/2);
		for(int i=0; i< str.length(); ++i) {
			
			char c = str.charAt(i);
			switch(c) {
			   case '<':  buff.append("&lt;"); break;
			   case '>':  buff.append("&gt;"); break;
			   case '\"': buff.append("&quot;"); break;
			   case '&':  buff.append("&amp;"); break;
			   default:
				   buff.append(c);
			}		
		}
		return buff.toString();
	}
	
	
	/** 
	 * Append the hex code of char to buff
	 * @param c
	 * @param buff
	 */
	private static void appendHexCode(char c, StringBuffer buff) {
	    int hi = (((int)c) & 0xf0) >>> 4;
	    int low = ((int)c) & 0x0f;
	    buff.append(hi < 10 ? (char)('0' + hi) : (char)('A' + hi - 10));
	    buff.append(low < 10 ? (char)('0' + low) : (char)('A' + low - 10));
	}
	
	/** 
	 * Is character reserved for URI's.
	 * Source RFC 2396
	 * @param c character
	 * @return whether c is reserved
	 */
	public static boolean isReservedUriChar(char c) {
		switch(c) {
			case ';': 
			case '/':
			case '?':
			case ':':
			case '@':
			case '&':
			case '=':
			case '+':
			case '$':
			case ',':
				return true;
			default:
				return false;
		}
	}
	
	/**
	 * Is characte unreserved for URI's.
	 * Source RFC 2396
	 * @param c
	 * @return whether c is unreserved
	 */
	public static boolean isUnreservedUriChar(char c) {
		switch(c) {
			// MARK characters (RFC 2396)
			case '-':
			case '_':
			case '.':
			case '!':
			case '~':
			case '*':
			case '\'':
			case '|':
			case '(':
			case ')':
				return true;
		}
		
		// ALPHANUM characters (RFC 2396)
		if ((c >= 'a' && c <= 'z') || // lower case
			(c >= 'A' && c <= 'Z') || // upper case
			(c >= '0' && c <= '9')) { // digit
			return true;
		}
		
		return false;
	}
	
	/**
	 * Excerpt from RFC 2396
	 * <pre>
	 * Data must be escaped if it does not have a representation using an
     * unreserved character; this includes data that does not correspond to
     * a printable character of the US-ASCII coded character set, or that
     * corresponds to any US-ASCII character that is disallowed [...]
     * </pre>
	 * 
	 * @param str
	 * @return
	 */
	public static String escapeUri(String str) {
		if (str == null) return "";
		
		// it is probably faster to scan through before replicating the string
		// in FD, most params are likely to use unreserved characters
		// known exceptions include white spaces as in 'New York' or accented
		// characters in exotic (aka French) food names
		int width = 0;
		for(int i=0; i< str.length(); width += isUnreservedUriChar(str.charAt(i++)) ? 1 : 3);
		
		if (width == str.length()) return str;
		StringBuffer buff = new StringBuffer(width);
		for(int i=0; i< str.length(); ++i) {
			char c = str.charAt(i);
			if (!isUnreservedUriChar(c)) { 
				buff.append('%'); 
				appendHexCode(c, buff);
			} else buff.append(c);
		}
		return buff.toString();
	}
	
	/**
	 * Escape only the well known potentially XSS vulnerability causing characters in a URI.
	 * 
	 * Escapes the &lt;, &quot; and &gt; characters.
	 * @param queryString
	 * @return escaped queryString
	 */
	public static String escapeXssUri(String queryString) {
		if (queryString == null) return "";
		
		StringBuffer buff = new StringBuffer(queryString.length()+1);
		for(int i=0; i< queryString.length(); ++i) {
			char c = queryString.charAt(i);
			switch(c) {
				case '<':
				case '>':
				case '\"':
					buff.append('%');
					appendHexCode(c, buff);
					break;
				default:
					buff.append(c);
			}
		}
		return buff.toString();
	}
	
	/**
	 * Append the string URI escaped.
	 * @param str
	 * @param buff
	 * @return buff
	 */
	public static StringBuffer appendUriEscaped(String str, StringBuffer buff) {
		if (str == null) return buff;
		for(int i=0; i< str.length(); ++i) {
			char c = str.charAt(i);
			if (!isUnreservedUriChar(c)) {
				buff.append('%');
				appendHexCode(c,buff);
			} else buff.append(c);
	    }
		return buff;
	}


	public static String escapeJavaScript(String str) {
		if (str == null) return "";
			
		StringBuffer buff = new StringBuffer(3*str.length()/2);
		for(int i=0; i< str.length(); ++i) {
			
			char c = str.charAt(i);
			switch(c) {
			   case '\b':  buff.append("\\b"); break;
			   case '\f':  buff.append("\\f"); break;
			   case '\n': buff.append("\\n"); break;
			   case '\r':  buff.append("\\r"); break;
			   case '\t':  buff.append("\\t"); break;
			   case '\'':  buff.append("\\\'"); break;
			   case '\"':  buff.append("\\\""); break;
			   case '\\':  buff.append("\\\\"); break;
			   default: 
				   if (c < ' ' || c >= 128) {
                      String t = "000" + Integer.toHexString(c);
                      buff.append("\\u" + t.substring(t.length() - 4));
                   } else {   
				      buff.append(c);
			       }		
		    }
		}
		return buff.toString();
	}
	
	public static String maskCreditCard(String cc){
		if (cc == null) return "";
		int length = cc.length();
		if(length<=4){
			return cc;
		}
		else{
			StringBuffer temp = new StringBuffer();
			for(int i=0;i<length-4;i++){
				temp =temp.append("*");
			}
			return temp.append(cc.substring(length-4)).toString();
		}
	}
	
	public static String[] decodeStrings(String stringVal) {	
		return StringUtils.split(stringVal, DEFAULT_SEPARATOR);
	}

	public static String encodeString(String strArray[]) {
		return StringUtils.join(strArray, DEFAULT_SEPARATOR);
	}

	public static String encodeString(List list) {
				
		if (list != null && list.size() > 0) {
			return StringUtils.join(list.toArray(), DEFAULT_SEPARATOR);
		}
		return null;
	}
	
	public static boolean isEmpty(String stringVal) {
		return StringUtils.isEmpty(stringVal);
	}
	
	public static boolean isDecimal(String stringVal) {
		try {
			Double.parseDouble(stringVal);
			return true;
		} catch(NumberFormatException e) {
			return false;
		}		
	}
	
	public static boolean isNumeric(String stringVal) {
		return StringUtils.isNumeric(stringVal);
	}
	
	/**
	 * Parse a file name from a Unix or Windows path.
	 * @param fileName path
	 * @param removeExtension whether to remove extension from file name
	 * @return filename part
	 */
	public static String parseFilename(String fileName, boolean removeExtension) {
		int start = fileName.lastIndexOf('/') + 1;
		if (start == 0) {
			start = fileName.lastIndexOf('\\') + 1;
		}
		
		
		int lastDot = removeExtension ? -1 : fileName.lastIndexOf(".");
		if (lastDot == -1) lastDot = fileName.length();
	
		return fileName.substring(start, lastDot);
	}
	
	/**
	 * NOTE: Moved from com.freshdirect.cms.listeners.BulkLoader
	 * @param escaped HTML escaped string
	 * @return accent escapes removed
	 */
	public static String removeHTMLEntities(String escaped) {
        if (escaped == null) return escaped;
        int currentIdx = 0;
        while ((escaped.indexOf('&', currentIdx) != -1) && (escaped.indexOf(';', currentIdx+1) != -1)) {
            int ampIdx = escaped.indexOf('&', currentIdx);
            int semiIdx = escaped.indexOf(';', ampIdx);
            //
            // what gets swapped?
            //
            String entityCode = escaped.substring(ampIdx+1, semiIdx);
            String originalChar = "";
            if (entityCode.startsWith("#")) {
                String numericCode = entityCode.substring(1);
                originalChar += (char) Integer.parseInt(numericCode);
            } else if ("amp".equals(entityCode)) {
                originalChar = "&";
            } else if ("lt".equals(entityCode)) {
                originalChar = "<";
            } else if ("gt".equals(entityCode)) {
                originalChar = ">";
            } else if ("quot".equals(entityCode)) {
                originalChar = "\"";
            } else if ("Aacute".equals(entityCode)) {
                originalChar = "\u00C1";
            } else if ("Agrave".equals(entityCode)) {
                originalChar = "\u00C0";
            } else if ("Acirc".equals(entityCode)) {
                originalChar = "\u00C5";
            } else if ("Atilde".equals(entityCode)) {
                originalChar = "\u00C3";
            } else if ("Aring".equals(entityCode)) {
                originalChar = "\u00C5";
            } else if ("Auml".equals(entityCode)) {
                originalChar = "\u00C4";
            } else if ("AElig".equals(entityCode)) {
                originalChar = "\u00C6";
            } else if ("Ccedil".equals(entityCode)) {
                originalChar = "\u00C7";
            } else if ("Eacute".equals(entityCode)) {
                originalChar = "\u00C9";
            } else if ("Egrave".equals(entityCode)) {
                originalChar = "\u00C8";
            } else if ("Ecirc".equals(entityCode)) {
                originalChar = "\u00CA";
            } else if ("Euml".equals(entityCode)) {
                originalChar = "\u00CB";
            } else if ("Iacute".equals(entityCode)) {
                originalChar = "\u00CD";
            } else if ("Igrave".equals(entityCode)) {
                originalChar = "\u00CC";
            } else if ("Icirc".equals(entityCode)) {
                originalChar = "\u00CE";
            } else if ("Iuml".equals(entityCode)) {
                originalChar = "\u00CF";
            } else if ("ETH".equals(entityCode)) {
                originalChar = "\u00D0";
            } else if ("Ntilde".equals(entityCode)) {
                originalChar = "\u00D1";
            } else if ("Oacute".equals(entityCode)) {
                originalChar = "\u00D3";
            } else if ("Ograve".equals(entityCode)) {
                originalChar = "\u00D2";
            } else if ("Ocirc".equals(entityCode)) {
                originalChar = "\u00D4";
            } else if ("Otilde".equals(entityCode)) {
                originalChar = "\u00D5";
            } else if ("Ouml".equals(entityCode)) {
                originalChar = "\u00D6";
            } else if ("Oslash".equals(entityCode)) {
                originalChar = "\u00D8";
            } else if ("Uacute".equals(entityCode)) {
                originalChar = "\u00DA";
            } else if ("Ugrave".equals(entityCode)) {
                originalChar = "\u00D9";
            } else if ("Ucirc".equals(entityCode)) {
                originalChar = "\u00DB";
            } else if ("Uuml".equals(entityCode)) {
                originalChar = "\u00DC";
            } else if ("Yacute".equals(entityCode)) {
                originalChar = "\u00DD";
            } else if ("THORN".equals(entityCode)) {
                originalChar = "\u00DE";
            } else if ("szlig".equals(entityCode)) {
                originalChar = "\u00DF";
            } else if ("aacute".equals(entityCode)) {
                originalChar = "\u00E1";
            } else if ("agrave".equals(entityCode)) {
                originalChar = "\u00E0";
            } else if ("acirc".equals(entityCode)) {
                originalChar = "\u00E2";
            } else if ("atilde".equals(entityCode)) {
                originalChar = "\u00E3";
            } else if ("auml".equals(entityCode)) {
                originalChar = "\u00E4";
            } else if ("aelig".equals(entityCode)) {
                originalChar = "\u00E6";
            } else if ("ccedil".equals(entityCode)) {
                originalChar = "\u00E7";
            } else if ("eacute".equals(entityCode)) {
                originalChar = "\u00E9";
            } else if ("egrave".equals(entityCode)) {
                originalChar = "\u00E8";
            } else if ("ecirc".equals(entityCode)) {
                originalChar = "\u00EA";
            } else if ("euml".equals(entityCode)) {
                originalChar = "\u00EB";
            } else if ("iacute".equals(entityCode)) {
                originalChar = "\u00ED";
            } else if ("igrave".equals(entityCode)) {
                originalChar = "\u00EC";
            } else if ("icirc".equals(entityCode)) {
                originalChar = "\u00EE";
            } else if ("iuml".equals(entityCode)) {
                originalChar = "\u00EF";
            } else if ("eth".equals(entityCode)) {
                originalChar = "\u00F0";
            } else if ("ntilde".equals(entityCode)) {
                originalChar = "\u00F1";
            } else if ("oacute".equals(entityCode)) {
                originalChar = "\u00F3";
            } else if ("ograve".equals(entityCode)) {
                originalChar = "\u00F2";
            } else if ("ocirc".equals(entityCode)) {
                originalChar = "\u00F4";
            } else if ("otilde".equals(entityCode)) {
                originalChar = "\u00F5";
            } else if ("ouml".equals(entityCode)) {
                originalChar = "\u00F6";
            } else if ("oslash".equals(entityCode)) {
                originalChar = "\u00F8";
            } else if ("uacute".equals(entityCode)) {
                originalChar = "\u00FA";
            } else if ("ugrave".equals(entityCode)) {
                originalChar = "\u00F9";
            } else if ("ucirc".equals(entityCode)) {
                originalChar = "\u00FB";
            } else if ("uuml".equals(entityCode)) {
                originalChar = "\u00FC";
            } else if ("yacute".equals(entityCode)) {
                originalChar = "\u00FD";
            } else if ("thorn".equals(entityCode)) {
                originalChar = "\u00FE";
            } else if ("yuml".equals(entityCode)) {
                originalChar = "\u00FF";
            }
            escaped = escaped.substring(0, ampIdx) + originalChar + escaped.substring(semiIdx+1);
            currentIdx = ampIdx + 1;
        }
        return escaped;
    }
	
	/**
	 * NOTE: Moved from com.freshdirect.cms.listeners.BulkLoader
	 * @param escaped HTML escaped string
	 * @return accent escapes removed
	 */
	public static String adjustAlphaHTMLEntities(String escaped) {
        if (escaped == null) return escaped;
        int currentIdx = 0;
        while ((escaped.indexOf('&', currentIdx) != -1) && (escaped.indexOf(';', currentIdx+1) != -1)) {
            int ampIdx = escaped.indexOf('&', currentIdx);
            int semiIdx = escaped.indexOf(';', ampIdx);
            //
            // what gets swapped?
            //
            String entityCode = escaped.substring(ampIdx+1, semiIdx);
            String originalChar = "";
            if ("Aacute".equals(entityCode)) {
                originalChar = "A";
            } else if ("Agrave".equals(entityCode)) {
                originalChar = "A";
            } else if ("Acirc".equals(entityCode)) {
                originalChar = "A";
            } else if ("Atilde".equals(entityCode)) {
                originalChar = "A";
            } else if ("Aring".equals(entityCode)) {
                originalChar = "A";
            } else if ("Auml".equals(entityCode)) {
                originalChar = "A";
            } else if ("AElig".equals(entityCode)) {
                originalChar = "AE";
            } else if ("Ccedil".equals(entityCode)) {
                originalChar = "C";
            } else if ("Eacute".equals(entityCode)) {
                originalChar = "E";
            } else if ("Egrave".equals(entityCode)) {
                originalChar = "E";
            } else if ("Ecirc".equals(entityCode)) {
                originalChar = "E";
            } else if ("Euml".equals(entityCode)) {
                originalChar = "E";
            } else if ("Iacute".equals(entityCode)) {
                originalChar = "I";
            } else if ("Igrave".equals(entityCode)) {
                originalChar = "I";
            } else if ("Icirc".equals(entityCode)) {
                originalChar = "I";
            } else if ("Iuml".equals(entityCode)) {
                originalChar = "I";
            } else if ("ETH".equals(entityCode)) {
                originalChar = "D";
            } else if ("Ntilde".equals(entityCode)) {
                originalChar = "N";
            } else if ("Oacute".equals(entityCode)) {
                originalChar = "O";
            } else if ("Ograve".equals(entityCode)) {
                originalChar = "O";
            } else if ("Ocirc".equals(entityCode)) {
                originalChar = "O";
            } else if ("Otilde".equals(entityCode)) {
                originalChar = "O";
            } else if ("Ouml".equals(entityCode)) {
                originalChar = "O";
            } else if ("Oslash".equals(entityCode)) {
                originalChar = "O";
            } else if ("Uacute".equals(entityCode)) {
                originalChar = "U";
            } else if ("Ugrave".equals(entityCode)) {
                originalChar = "U";
            } else if ("Ucirc".equals(entityCode)) {
                originalChar = "U";
            } else if ("Uuml".equals(entityCode)) {
                originalChar = "U";
            } else if ("Yacute".equals(entityCode)) {
                originalChar = "Y";
            } else if ("THORN".equals(entityCode)) {
                originalChar = "th";
            } else if ("szlig".equals(entityCode)) {
                originalChar = "ss";
            } else if ("aacute".equals(entityCode)) {
                originalChar = "a";
            } else if ("agrave".equals(entityCode)) {
                originalChar = "a";
            } else if ("acirc".equals(entityCode)) {
                originalChar = "a";
            } else if ("atilde".equals(entityCode)) {
                originalChar = "a";
            } else if ("auml".equals(entityCode)) {
                originalChar = "a";
            } else if ("aelig".equals(entityCode)) {
                originalChar = "ae";
            } else if ("ccedil".equals(entityCode)) {
                originalChar = "c";
            } else if ("eacute".equals(entityCode)) {
                originalChar = "e";
            } else if ("egrave".equals(entityCode)) {
                originalChar = "e";
            } else if ("ecirc".equals(entityCode)) {
                originalChar = "e";
            } else if ("euml".equals(entityCode)) {
                originalChar = "e";
            } else if ("iacute".equals(entityCode)) {
                originalChar = "i";
            } else if ("igrave".equals(entityCode)) {
                originalChar = "i";
            } else if ("icirc".equals(entityCode)) {
                originalChar = "i";
            } else if ("iuml".equals(entityCode)) {
                originalChar = "i";
            } else if ("eth".equals(entityCode)) {
                originalChar = "d";
            } else if ("ntilde".equals(entityCode)) {
                originalChar = "n";
            } else if ("oacute".equals(entityCode)) {
                originalChar = "o";
            } else if ("ograve".equals(entityCode)) {
                originalChar = "o";
            } else if ("ocirc".equals(entityCode)) {
                originalChar = "o";
            } else if ("otilde".equals(entityCode)) {
                originalChar = "o";
            } else if ("ouml".equals(entityCode)) {
                originalChar = "o";
            } else if ("oslash".equals(entityCode)) {
                originalChar = "o";
            } else if ("uacute".equals(entityCode)) {
                originalChar = "u";
            } else if ("ugrave".equals(entityCode)) {
                originalChar = "u";
            } else if ("ucirc".equals(entityCode)) {
                originalChar = "u";
            } else if ("uuml".equals(entityCode)) {
                originalChar = "u";
            } else if ("yacute".equals(entityCode)) {
                originalChar = "y";
            } else if ("thorn".equals(entityCode)) {
                originalChar = "the";
            } else if ("yuml".equals(entityCode)) {
                originalChar = "y";
            }
            escaped = escaped.substring(0, ampIdx) + originalChar + escaped.substring(semiIdx+1);
            currentIdx = ampIdx + 1;
        }
        return escaped;
    }
	
	/**
	 * Removes all, including internal whitespace.
	 * 
	 * For example, it makes "A&W" from " A &    W ".
	 * 
	 * @param string string to remove white space from
	 * @return a new string without the white space.
	 */
	public static String removeAllWhiteSpace(CharSequence string) {
		StringBuffer buffer = new StringBuffer(string.length());
		
		for(int i = 0; i< string.length(); ++i) {
			char c = string.charAt(i);
			switch(c) {
				case ' ':
				case '\t':
				case '\r':
				case '\n':
					break;
				default:
					buffer.append(c);
			}
		}
		
		return buffer.toString();
	}
	
	/**
	 * Test for diacritic characters.
	 * 
	 * @param s string
	 * @return if the string has accented characters
	 */
	public static boolean hasDiacritic(CharSequence s) {
		for(int i = 0; i< s.length(); ++i) {
			char c = s.charAt(i);
			if (Character.isLowerCase(s.charAt(i)) && (c < 'a' || c > 'z')) return true;
		}
		return false;
	}
	
	public static String formQueryString(List lstValues) {
		
		if(lstValues != null) {			
	    	if(lstValues.size() > 1) {
	    		Iterator iterator = lstValues.iterator();
	    		int intCount = 0;
	    		StringBuffer strBuf = new StringBuffer();	    		
	        	while(iterator.hasNext()) {
	        		intCount++;
	        		strBuf.append("'").append(iterator.next()).append("'");
	        		if(intCount != lstValues.size()) {
	        			strBuf.append(",");
	        		}
	        	}
	        	return "in ("+strBuf.toString()+")";
	    	} else if (lstValues.size() == 1){
	    		return "= '"+lstValues.get(0)+"'";
	    	}
		} 
		return null;
    	
	}
	
    /**
     * this function emulates the class.getSimpleName() found in java 1.5 or
     * more TODO : after migration to 1.5 remove!
     * 
     * @param cls
     */
    public static String getSimpleName(Class cls) {
        String name = cls.getName();
        int lastDot = name.lastIndexOf('.');
        if (lastDot != -1) {
            name = name.substring(lastDot + 1);
        }
        return name;
    }
	
}
