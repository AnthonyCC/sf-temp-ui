package com.freshdirect.webapp.taglib.callcenter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.freshdirect.WineUtil;
import com.freshdirect.customer.ErpComplaintReason;
import com.freshdirect.fdstore.CallCenterServices;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDCartLineI;
import com.freshdirect.fdstore.customer.FDOrderI;

public class ComplaintUtil {
	private static Object lock = new Object();
    
    private static Map<String, List<ErpComplaintReason>> complaintReasons				= new HashMap<String, List<ErpComplaintReason>>();
    private static Map<String, List<ErpComplaintReason>> complaintReasons_noCartsReq	= new HashMap<String, List<ErpComplaintReason>>();

    private static long lastUpdate = 0;
    private static long lastUpdate_noCartsReq = 0;

    private static final long refreshPeriod = 1000 * 60 * 10; // 10 minutes
    
    private static Map<String, List<ErpComplaintReason>> getReasonMap(boolean excludeCartonReq) throws FDResourceException {
    	Map<String, List<ErpComplaintReason>> ret = null;
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
    public static List<ErpComplaintReason> getReasonsForDepartment(String dept, boolean excludeCartReq) throws FDResourceException {
    	List<ErpComplaintReason> reasons = getReasonMap(excludeCartReq).get(standardizeDepartment(dept));
        return reasons != null ? reasons : Collections.EMPTY_LIST;
    }

    // @return List<ErpComplaintReason>
    // @deprecated use getReasonsForDepartment(String dept, boolean excludeCartReq) instead with 'false' 
    public static List<ErpComplaintReason> getReasonsForDepartment(String dept) throws FDResourceException {
    	return getReasonsForDepartment(dept, false);
    }

    // @return Set<String>
    public static Set<String> getReasonTextsForDepartment(String dept, boolean excludeCartReq) throws FDResourceException {
    	List<ErpComplaintReason> reasons = getReasonMap(excludeCartReq).get(standardizeDepartment(dept));

        if (reasons != null && reasons.size() > 0) {
        	Set<String> rTexts = new HashSet<String>(reasons.size());
        	
        	for (final ErpComplaintReason reason : reasons) {
        		rTexts.add( reason.getReason() );
        	}
        	return rTexts;
        }

        return Collections.emptySet();
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
    public static Set<String> getReasonsForDepartments(Collection<String> depts, boolean excludeCartReq) throws FDResourceException {
    	Iterator<String> it=depts.iterator();
    	
    	// Case k=0
    	if (!it.hasNext())
    		return Collections.emptySet();
    	
    	String deptName = (String) it.next();
    	Set<String> reasons = getReasonTextsForDepartment(deptName, excludeCartReq);

    	// Case k=1
    	if (!it.hasNext())
    		return reasons;

    	// Case k>1
    	Set<String> r0 = reasons; // convert list to set
    	while (it.hasNext()) {
        	deptName = (String) it.next();
        	Set<String> r1 = getReasonTextsForDepartment(deptName, excludeCartReq);
        	
        	r0.retainAll(r1); // intersect r0 and r1
    	}

    	return r0; // convert final set back to list
    }

    /**
     * 
     * @param depts
     * @param excludeCartReq
     * @return
     * @throws FDResourceException
     */
    public static List<ErpComplaintReason> getReasonsListForDepartments(Collection<String> depts, boolean excludeCartReq) throws FDResourceException {
    	Iterator<String> it=depts.iterator();
    	
    	// Case k=0
    	if (!it.hasNext())
    		return Collections.emptyList();
    	
    	String deptName = (String) it.next();
    	List<ErpComplaintReason> reasons = getReasonsForDepartment(deptName, excludeCartReq);

    	// Case k=1
    	if (!it.hasNext())
    		return reasons;

    	// Case k>1
    	List<ErpComplaintReason> r0 = new ArrayList<ErpComplaintReason>(reasons); // convert list to set
    	while (it.hasNext()) {
        	deptName = (String) it.next();
        	List<ErpComplaintReason> r1 = getReasonsForDepartment(deptName, excludeCartReq);
        	
        	r0.retainAll(r1); // intersect r0 and r1
    	}

    	return r0; // convert final set back to list
    }

    public static ErpComplaintReason getReasonById(String id) throws FDResourceException {
    	Map<String, List<ErpComplaintReason>> allReasons = getReasonMap(false);
        for (Iterator<String> dIter = allReasons.keySet().iterator(); dIter.hasNext(); ) {
            String dept = dIter.next();
            List<ErpComplaintReason> deptReasons = allReasons.get(dept);
            for (final ErpComplaintReason reason : deptReasons) {
                if (reason.getId().equals(id)) {
                    return reason;
                }
            }
        } 
        return null;
    }
    
    public static ErpComplaintReason getReasonByDeptAndText(String dept,String reasonText) throws FDResourceException {
    	List<ErpComplaintReason> allReasons = getReasonsForDepartment(dept);
    	for (final ErpComplaintReason reason : allReasons) {
            if (reason.getReason().equals(reasonText)) {
                return reason;
            }
        } 
        return null;
    }
    
    private static String standardizeDepartment(String dept) {
        String r = "none";
        
        if ( "bakery".equalsIgnoreCase(dept) || (dept != null && dept.toLowerCase().contains("bakery")) ) { r = "BAK"; }
		else if ( "cheese".equalsIgnoreCase(dept)  || (dept != null && dept.toLowerCase().contains("cheese")) ) { r = "CHE"; }
		else if ( "coffee".equalsIgnoreCase(dept) ) { r = "COF"; }
		else if ( "dairy".equalsIgnoreCase(dept) ) { r = "DAI"; }
		else if ( "deli".equalsIgnoreCase(dept) ) { r = "DEL"; }
		else if ( "frozen".equalsIgnoreCase(dept) ) { r = "FRO"; }
		else if ( "fruit".equalsIgnoreCase(dept) ) { r = "FRU"; }
		else if ( "grocery".equalsIgnoreCase(dept) || "baby".equalsIgnoreCase(dept) || "beverages".equalsIgnoreCase(dept) || "household".equalsIgnoreCase(dept) || "pantry".equalsIgnoreCase(dept) || "beer".equalsIgnoreCase(dept) ) { r = "GRO"; }
		else if ( "meals".equalsIgnoreCase(dept) || "kitchen".equalsIgnoreCase(dept) || "Heat & Eat".equalsIgnoreCase(dept)) { r = "HMR"; }
		else if ( "meat".equalsIgnoreCase(dept) ) { r = "MEA"; }
		else if ( "pasta".equalsIgnoreCase(dept) || (dept != null && dept.toLowerCase().contains("pasta")) ) { r = "PAS"; }
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
        else if ( (dept != null && dept.toLowerCase().contains("wine") ) || (dept!=null && dept.toLowerCase().contains("usq")) || (dept!=null && dept.toLowerCase().startsWith(WineUtil.getWineAssociateId().toLowerCase())) ){ r = "WIN"; }
        else if ( "Easy Meals".equalsIgnoreCase(dept) ){ r = "EZM"; }
        else if ( dept != null && dept.toLowerCase().contains("cook")) { r = "RTC"; }
        else if ( "Healthy Living".equalsIgnoreCase(dept) || "Our Picks".equalsIgnoreCase(dept) ){ r = "OURPICKS"; }
        else if ( dept.toLowerCase().indexOf("health") != -1 ) { r = "HBA"; }
        else if ( "4-Minute Meals".equalsIgnoreCase(dept) ){ r = "FDI"; }
        else if ( "Makegood".equalsIgnoreCase(dept) ){ r = "MGD"; }
        else if ( "at the office".equalsIgnoreCase(dept) ){ r = "COS"; }
        else if ( "buy big".equalsIgnoreCase(dept) ){ r = "BIG"; }
        else if ( "local".equalsIgnoreCase(dept) ){ r = "LOC"; }
        else if ( "what's good".equalsIgnoreCase(dept) ){ r = "WGD"; }
        else if ( "RAF".equalsIgnoreCase(dept) ){ r = "RAF"; }
        else if ( "Flowers".equalsIgnoreCase(dept) ){ r = "FLO"; }
        else if ( "Pet".equalsIgnoreCase(dept) ){ r = "PET"; }
        
        else if ( "Deals".equalsIgnoreCase(dept) ){ r = "DEALS"; }
        else if ( "Drugstore & Household".equalsIgnoreCase(dept) ){ r = "DRUGHAUS"; }
        else if ( "FD Kitchen".equalsIgnoreCase(dept) ){ r = "HMR"; }
        else if ( "Fresh".equalsIgnoreCase(dept) || "Top-Rated".equalsIgnoreCase(dept) ){ r = "FRESHTOP"; }
        else if ( "Pantry & Freezer".equalsIgnoreCase(dept) ){ r = "PNTRYFRZ"; }
        else if ( "Pastry".equalsIgnoreCase(dept) ){ r = "PASTRY"; }
        else if ( "Snacks".equalsIgnoreCase(dept) ){ r = "SNACKS"; }

        return r;
    }

	/**
	 * Builds a HashMap of ComplaintDeptInfo objects that hold total amounts spent in
	 * each department in the current order.
	 */
	public Map<String, ComplaintDeptInfo> getOrderInfo(FDOrderI order) {
		//
		// Calculate the boundary amounts that credits may not exceed
		//
		Map<String, ComplaintDeptInfo> map = new HashMap<String, ComplaintDeptInfo>();
		String lastDept = null;
		Collection<FDCartLineI> lines = order.getOrderLines();

		for (Iterator<FDCartLineI> it = lines.iterator(); it.hasNext(); ) {
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
	
	public static ErpComplaintReason getReasonByCompCode(String cCode) throws FDResourceException {
		return CallCenterServices.getReasonByCompCode(cCode);
	}

}
