/*
 * EmailUtil.java
 *
 * Created on March 19, 2002, 1:14 PM
 */

package com.freshdirect.mail;

import org.apache.oro.text.perl.Perl5Util;

/**
 *
 * @author  mrose
 * @version
 */
public class EmailUtil {
    
    private static Perl5Util emailRegExp = new Perl5Util();
    private static String emailPattern = "/^[a-zA-Z0-9._-]+@([a-zA-Z0-9-_]+\\.)+[a-zA-Z]{2,}$/";

	private EmailUtil() {
	}
    
    public static boolean isValidEmailAddress(String emailAddress) {
        return emailRegExp.match(emailPattern, emailAddress);
    }
    
}
