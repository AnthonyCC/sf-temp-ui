package com.freshdirect.webapp.unbxdanalytics.visitor;

import com.freshdirect.fdstore.customer.FDUserI;

/**
 * This class represents the visitor type of
 * UNBXD analytics events
 * 
 * Every UNBXD event carry the following visitor properties:
 * - uid
 * - visitor type
 * 
 * Visitor UID is currently formed obtained from {@link FDUserI#getCookie()}
 * 
 * @author segabor
 *
 * @see VisitType
 * @see VisitTypeCache
 *
 */
public final class Visitor {

    private final String uid;

    private final VisitType visitType;

    /**
     * Default way to obtain a Visitor
     * 
     * @param user
     * 
     * @return
     */
    public static Visitor withUser(FDUserI user) {
        
        final String uid = (user.getCookie() == null ? Double.toString(Math.random()) : user.getCookie());

        final VisitType visitType = VisitTypeCache.getInstance().createVisitType(uid);
        
        return new Visitor(uid, visitType);
    }

    /**
     * Don't use it, instead {@link Visitor#withUser(FDUserI)} is the preferred
     * way to instantiate a Visitor
     * 
     * @param uid
     */
    public Visitor(String uid, VisitType visitType) {
        this.uid = uid;
        this.visitType = visitType;
    }

    public boolean isFirstTime() {
        return ! visitType.repeat;
    }

    public boolean isRepeat() {
        return visitType.repeat;
    }

    /**
     * Returns value for "uid" event attribute
     * @return
     */
    public String getUID() {
        return uid;
    }

    /**
     * Returns "visit_type" event attribute value
     * 
     * @return
     */
    public String getVisitType() {
        return visitType.toString();
    }

    public boolean isVisitorEventRequired() {
        return visitType.visitorEventRequired;
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
}
