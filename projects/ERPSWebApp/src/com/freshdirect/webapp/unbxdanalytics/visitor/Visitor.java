package com.freshdirect.webapp.unbxdanalytics.visitor;

import com.freshdirect.fdstore.customer.FDIdentity;

/**
 * This class represents the visitor type of
 * UNBXD analytics events
 * 
 * Every UNBXD event carry the following visitor properties:
 * - uid
 * - visitor type (see below)
 * 
 * Rules
 * =====
 * 
 * When a user makes an event(search hit, add to cart, etc), you need to check if the visitor event has been fired for that user.
 * #1 If it has not been fired, you need to fire it
 *      passing the value "first_time" to "visitor_type"
 * #2 If it has been fired before, you need to check
 *      if the last time it got fired was before 30 minutes from now.
 * #3 If it was before 30 minutes, you need to fire it
 *      passing the value "repeat" to "visitor_type"
 * #4 Once the visitor event is fired
 *      you can fire other events' APIs.
 * 
 * Visitor UID is currently formed from FDIdentity
 * 
 * @author segabor
 *
 */
public final class Visitor {

    public final static String VISITOR_TYPE_VALUE_FIRST = "first_time";
    public final static String VISITOR_TYPE_VALUE_REPEAT = "repeat";

    private final String uid;

    private boolean repeat = false;
    
    /**
     * Default constructor
     * 
     * @param uid
     */
    public Visitor(String uid) {
        this.uid = uid;
    }

    /**
     * Convenience constructor
     * 
     * @param identity
     */
    public Visitor(FDIdentity identity) {
        this(Visitor.createUID(identity));
    }

    public boolean isFirstTime() {
        return repeat;
    }

    public boolean isRepepat() {
        return !repeat;
    }

    /**
     * Returns value for "uid" event attribute
     * @return
     */
    public String getUID() {
        return uid;
    }

    /**
     * Returns "visitor_type" event attribute value
     * 
     * @return
     */
    public String getVisitType() {
        return repeat ? VISITOR_TYPE_VALUE_FIRST : VISITOR_TYPE_VALUE_REPEAT;
    }

    /**
     * 
     */
    public void setRepeat(boolean flag) {
        this.repeat = flag;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((uid == null) ? 0 : uid.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Visitor other = (Visitor) obj;
        if (uid == null) {
            if (other.uid != null) {
                return false;
            }
        } else if (!uid.equals(other.uid)) {
            return false;
        }
        return true;
    }

    /**
     * Create UNBXD Visitor uid out of FDIdentity
     * 
     * @param identity
     * @return
     */
    public static String createUID(FDIdentity identity) {
        return identity.getErpCustomerPK();
    }

}
