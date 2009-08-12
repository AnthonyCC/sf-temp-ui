package com.freshdirect.webapp.taglib.callcenter;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.freshdirect.customer.ErpComplaintReason;
import com.freshdirect.fdstore.CallCenterServices;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDCartLineI;
import com.freshdirect.fdstore.customer.FDOrderI;

public class ComplaintUtil {
	private static Object lock = new Object();
    
    private static Map complaintReasons				= new HashMap();
    private static Map complaintReasons_noCartsReq	= new HashMap();

    private static long lastUpdate = 0;
    private static long lastUpdate_noCartsReq = 0;

    private static final long refreshPeriod = 1000 * 60 * 10; // 10 minutes
    
    private static Map getReasonMap(boolean excludeCartonReq) throws FDResourceException {
    	Map ret = null;
        synchronized (lock) {
        	if (excludeCartonReq) {
            	if (System.currentTimeMillis() - lastUpdate_noCartsReq > refreshPeriod) {
		        	complaintReasons_noCartsReq = CallCenterServices.getComplaintReasons(true);
		        	lastUpdate_noCartsReq = System.currentTimeMillis();
            	}
            	ret = complaintReasons_noCartsReq;
        	} else {
            	if (System.currentTimeMillis() - lastUpdate > refreshPeriod) {
	                complaintReasons = CallCenterServices.getComplaintReasons(false);
	                lastUpdate = System.currentTimeMillis();
            	}
            	ret = complaintReasons;
        	}
        }
        return ret;
    }


    // @return List<ErpComplaintReason>
    public static List getReasonsForDepartment(String dept, boolean excludeCartReq) throws FDResourceException {
        List reasons = (List) getReasonMap(excludeCartReq).get(standardizeDepartment(dept));
        return reasons != null ? reasons : Collections.EMPTY_LIST;
    }

    // @return List<ErpComplaintReason>
    // @deprecated use getReasonsForDepartment(String dept, boolean excludeCartReq) instead with 'false' 
    public static List getReasonsForDepartment(String dept) throws FDResourceException {
    	return getReasonsForDepartment(dept, false);
    }

    // @return Set<String>
    public static Set getReasonTextsForDepartment(String dept, boolean excludeCartReq) throws FDResourceException {
        List reasons = (List) getReasonMap(excludeCartReq).get(standardizeDepartment(dept));

        if (reasons != null && reasons.size() > 0) {
        	Set rTexts = new HashSet(reasons.size());
        	
        	for (Iterator it=reasons.iterator(); it.hasNext();) {
        		rTexts.add( ((ErpComplaintReason)it.next()).getReason() );
        	}
        	return rTexts;
        }

        return Collections.EMPTY_SET;
    }


    /**
     * Produces complaint reasons from list of departments. The result will be the intersection of
     * department reasons.
     * 
     * @param depts Collection of department names
     * @return Reasons set
     * @throws FDResourceException
     */
    // @return Set<String>
    public static Set getReasonsForDepartments(Collection depts, boolean excludeCartReq) throws FDResourceException {
    	Iterator it=depts.iterator();
    	
    	// Case k=0
    	if (!it.hasNext())
    		return Collections.EMPTY_SET;
    	
    	String deptName = (String) it.next();
    	Set reasons = getReasonTextsForDepartment(deptName, excludeCartReq);

    	// Case k=1
    	if (!it.hasNext())
    		return reasons;

    	// Case k>1
    	Set r0 = reasons; // convert list to set
    	while (it.hasNext()) {
        	deptName = (String) it.next();
        	Set r1 = getReasonTextsForDepartment(deptName, excludeCartReq);
        	
        	r0.retainAll(r1); // intersect r0 and r1
    	}

    	return r0; // convert final set back to list
    }


    public static ErpComplaintReason getReasonById(String id) throws FDResourceException {
        Map allReasons = getReasonMap(false);
        for (Iterator dIter = allReasons.keySet().iterator(); dIter.hasNext(); ) {
            String dept = (String) dIter.next();
            List deptReasons = (List) allReasons.get(dept);
            for (Iterator rIter = deptReasons.iterator(); rIter.hasNext(); ) {
                ErpComplaintReason reason = (ErpComplaintReason) rIter.next();
                if (reason.getId().equals(id)) {
                    return reason;
                }
            }
        } 
        return null;
    }
    
    private static String standardizeDepartment(String dept) {
        String r = "none";
             if ( "bakery".equalsIgnoreCase(dept) ) { r = "BAK"; }
		else if ( "cheese".equalsIgnoreCase(dept) ) { r = "CHE"; }
		else if ( "coffee".equalsIgnoreCase(dept) ) { r = "COF"; }
		else if ( "dairy".equalsIgnoreCase(dept) ) { r = "DAI"; }
		else if ( "deli".equalsIgnoreCase(dept) ) { r = "DEL"; }
		else if ( "frozen".equalsIgnoreCase(dept) ) { r = "FRO"; }
		else if ( "fruit".equalsIgnoreCase(dept) ) { r = "FRU"; }
		else if ( "grocery".equalsIgnoreCase(dept) ) { r = "GRO"; }
		else if ( "meals".equalsIgnoreCase(dept) || "kitchen".equalsIgnoreCase(dept) || "Heat & Eat".equalsIgnoreCase(dept)) { r = "HMR"; }
		else if ( "meat".equalsIgnoreCase(dept) ) { r = "MEA"; }
		else if ( "pasta".equalsIgnoreCase(dept) ) { r = "PAS"; }
		else if ( "seafood".equalsIgnoreCase(dept) ) { r = "SEA"; }
		else if ( "specialty".equalsIgnoreCase(dept) ) { r = "GRO"; }
		else if ( "tea".equalsIgnoreCase(dept) ) { r = "TEA"; }
		else if ( dept.toLowerCase().indexOf("vegetables") != -1 ) { r = "VEG"; }
		else if ( dept.toLowerCase().indexOf("organic") != -1 ) { r = "ORG"; }
		else if ( "catering".equalsIgnoreCase(dept) ) { r = "CAT"; }
		else if ( "goodwill".equalsIgnoreCase(dept) ) { r = "GDW"; }
		else if ( "transportation".equalsIgnoreCase(dept) ) { r = "TRN"; }
        else if ( "extraitem".equalsIgnoreCase(dept) ) { r = "XTR"; }
        else if ( "kosher".equalsIgnoreCase(dept) ) { r = "KOS"; }
        else if ( "wine".equalsIgnoreCase(dept) ||"usq".equalsIgnoreCase(dept)){ r = "WIN"; }
        else if ( "Easy Meals".equalsIgnoreCase(dept) ){ r = "EZM"; }
        else if ( "Ready to Cook".equalsIgnoreCase(dept) ){ r = "RTC"; }
        else if ( dept.toLowerCase().indexOf("health") != -1 ) { r = "HBA"; }
        else if ( "Our Picks".equalsIgnoreCase(dept) ){ r = "OURPICKS"; }        
        else if ( "4-Minute Meals".equalsIgnoreCase(dept) ){ r = "FDI"; }
        else if ( "Makegood".equalsIgnoreCase(dept) ){ r = "MGD"; }

        else if ( "at the office".equalsIgnoreCase(dept) ){ r = "COS"; }
        else if ( "buy big".equalsIgnoreCase(dept) ){ r = "BIG"; }
        else if ( "local".equalsIgnoreCase(dept) ){ r = "LOC"; }
        else if ( "what's good".equalsIgnoreCase(dept) ){ r = "WGD"; }

        return r;
    }

	/**
	 * Builds a HashMap of ComplaintDeptInfo objects that hold total amounts spent in
	 * each department in the current order.
	 */
	public HashMap getOrderInfo(FDOrderI order) {
		//
		// Calculate the boundary amounts that credits may not exceed
		//
		HashMap map = new HashMap();
		String lastDept = null;
		Collection lines = null;
		lines = order.getOrderLines();

		for (Iterator it = lines.iterator(); it.hasNext(); ) {
			FDCartLineI line = (FDCartLineI) it.next();
			if ( lastDept==null || !lastDept.equalsIgnoreCase( line.getDepartmentDesc() ) ) {
				lastDept = standardizeDepartment(line.getDepartmentDesc());
			}

			ComplaintDeptInfo deptInfo = null;
			if ( map.containsKey(lastDept) ) {
				deptInfo = (ComplaintDeptInfo) map.get(lastDept);
			} else {
				deptInfo = new ComplaintDeptInfo();
                map.put(lastDept, deptInfo);
			}

			deptInfo.setLabel(lastDept);
			Double deptTotal = deptInfo.getTotal();
			if (deptTotal == null) { deptTotal = new Double(0.0); }
			deptTotal = new Double( deptTotal.doubleValue() + line.getPrice() );
			deptInfo.setTotal(deptTotal);
		}

		return map;

	}

}