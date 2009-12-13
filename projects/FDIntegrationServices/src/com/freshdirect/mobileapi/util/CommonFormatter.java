package com.freshdirect.mobileapi.util;

import com.freshdirect.webapp.util.CCFormatter;

public class CommonFormatter {

    public static String formatCurrency(double amount) {
        return CCFormatter.formatCurrency(amount);
}
    
}
