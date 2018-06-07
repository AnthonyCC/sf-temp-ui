package com.freshdirect.cms.ui.editor.permission.domain;

import java.util.Collection;

/**
 * Result of permission check
 *
 * @author segabor
 */
public enum Permit {
    ALLOW,
    REJECT;

    public static Permit valueOf(boolean flag) {
        return flag ? ALLOW : REJECT;
    }

    public static Permit negativeValueOf(boolean flag) {
        return flag ? REJECT : ALLOW;
    }

    public static boolean isAnyRejected(Collection<Permit> permits) {
        for (final Permit p : permits) {
            if (Permit.REJECT == p) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean valueOf(Permit p) {
        return p == ALLOW;
    }
}