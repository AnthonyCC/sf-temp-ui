package com.freshdirect.transadmin.util;

import java.text.SimpleDateFormat;


public interface IDateContants {
	
	/** less than or equal. usage: <= 18-12-1997 */
    public static final String LESS_THAN_OR_EQUAL = "<=";

    /** greater than or equal. usage: >= 18-12-1997 */
    public static final String GREATER_THAN_OR_EQUAL = ">=";
    
    /** less than or equal. usage: <= 18-12-1997 */
    public static final String LESS_THAN = "<";

    /** greater than or equal. usage: >= 18-12-1997 */
    public static final String GREATER_THAN = ">";

    /** date between. usage: <> 18-12-1997 19-12-1997 */
    public static final String BETWEEN = "<>";
    
    /** date between. usage: <> 18-12-1997 19-12-1997 */
    public static final String SQL_BETWEEN = "BETWEEN";
    
    /** date between. usage: <> 18-12-1997 19-12-1997 */
    public static final String LOGICAL_AND = "AND";

    /** date not equal. ussage: != 18-12-2004 */
    public static final String NOT_EQUAL = "!=";
    
    /** date not equal. ussage: != 18-12-2004 */
    public static final String EQUAL = "=";

    /** delimiters */
    public static final String DELIM = "\\s";
        
    public static final String asterisk = "*";
    public static final String emptyString = "";
    
    public static final SimpleDateFormat FILTER_DATEFORMAT = new SimpleDateFormat("MM/dd/yyyy");
}
