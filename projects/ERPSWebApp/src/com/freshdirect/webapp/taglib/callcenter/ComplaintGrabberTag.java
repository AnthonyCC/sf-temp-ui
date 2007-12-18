/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */

package com.freshdirect.webapp.taglib.callcenter;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.ListIterator;

import javax.servlet.jsp.JspException;

import com.freshdirect.customer.EnumComplaintLineType;
import com.freshdirect.customer.EnumComplaintStatus;
import com.freshdirect.customer.ErpComplaintLineModel;
import com.freshdirect.customer.ErpComplaintModel;
import com.freshdirect.fdstore.customer.FDOrderI;


/**
 *
 * @version $Revision$
 * @author $Author$
 */
public class ComplaintGrabberTag extends com.freshdirect.framework.webapp.BodyTagSupport {

    private String lineComplaints					= null;		// orderline complaints key
    private String deptComplaints					= null;		// department complaints key
    private String miscComplaints					= null;		// miscellaneous complaints key
    private String fullComplaints					= null;		// full refund complaints key
    private String restockComplaints				= null;		// 75 percent refund complaints key
    private String complaints						= null;		// complaints collection key
    private boolean retrievePending					= false;	// whether to retrieve complaints with a PENDING status
    private boolean retrieveApproved				= false;	// whether to retrieve complaints with a APPROVED status
    private boolean retrieveRejected				= false;	// whether to retrieve complaints with a REJECTED status

    private FDOrderI order 							= null; 	// input variable


    public String getLineComplaints() {
        return this.lineComplaints;
    }
    public void setLineComplaints(String s) {
        this.lineComplaints = s;
    }

    public String getDeptComplaints() {
        return this.deptComplaints;
    }
    public void setDeptComplaints(String s) {
        this.deptComplaints = s;
    }

    public String getMiscComplaints() {
        return this.miscComplaints;
    }
    public void setMiscComplaints(String s) {
        this.miscComplaints = s;
    }

    public String getFullComplaints() {
        return this.fullComplaints;
    }
    public void setFullComplaints(String s) {
        this.fullComplaints = s;
    }

    public String getRestockComplaints() {
        return this.restockComplaints;
    }
    public void setRestockComplaints(String s) {
        this.restockComplaints = s;
    }

    public String getComplaints() {
        return this.complaints;
    }
    public void setComplaints(String s) {
        this.complaints = s;
    }

    public boolean getRetrievePending() {
        return this.retrievePending;
    }
    public void setRetrievePending(boolean b) {
        this.retrievePending = b;
    }

	public boolean getRetrieveApproved() {
		return this.retrieveApproved;
	}
	public void setRetrieveApproved(boolean b) {
		this.retrieveApproved = b;
    }

	public boolean getRetrieveRejected() {
		return this.retrieveRejected;
	}
	public void setRetrieveRejected(boolean b) {
		this.retrieveRejected = b;
    }

    public FDOrderI getOrder() {
		return this.order;
	}

	public void setOrder(FDOrderI o) {
		this.order = o;
	}


    public int doStartTag() throws JspException {

		ArrayList lineComplaintsValue = new ArrayList();
		ArrayList deptComplaintsValue = new ArrayList();
		ArrayList miscComplaintsValue = new ArrayList();
		ArrayList fullComplaintsValue = new ArrayList();
		ArrayList restockComplaintsValue = new ArrayList();

		ArrayList complaintsValue = new ArrayList(order.getComplaints());
		//
		// Remove complaints according to the parameters passed in attributes
		//
		for (ListIterator it = complaintsValue.listIterator(); it.hasNext(); ) {
			ErpComplaintModel complaint = (ErpComplaintModel) it.next();
			if ( EnumComplaintStatus.REJECTED.equals( complaint.getStatus() ) && !this.retrieveRejected )
				it.remove();
			if ( EnumComplaintStatus.PENDING.equals( complaint.getStatus() ) && !this.retrievePending )
				it.remove();
			if ( EnumComplaintStatus.APPROVED.equals( complaint.getStatus() ) && !this.retrieveApproved )
				it.remove();
		}
		//
		// Put complaint lines in proper Collection
		//
		for (Iterator it = complaintsValue.iterator(); it.hasNext(); ) {
			ErpComplaintModel complaint = (ErpComplaintModel) it.next();
			Collection lines = complaint.getComplaintLines();
			for (Iterator it2 = lines.iterator(); it2.hasNext(); ) {
				ErpComplaintLineModel line = (ErpComplaintLineModel) it2.next();
				if ( EnumComplaintLineType.ORDER_LINE.equals( line.getType() ) ) {
					lineComplaintsValue.add(line);
				} else if ( EnumComplaintLineType.DEPARTMENT.equals( line.getType() ) ) {
					deptComplaintsValue.add(line);
				} else if ( EnumComplaintLineType.MISCELLANEOUS.equals( line.getType() ) ) {
					miscComplaintsValue.add(line);
				} else if ( EnumComplaintLineType.FULL_REFUND.equals( line.getType() ) ) {
					fullComplaintsValue.add(line);
				} else if ( EnumComplaintLineType.SEVENTY_FIVE_PCT.equals( line.getType() ) ) {
					restockComplaintsValue.add(line);
				}
			}
		}
		//
		// Set variables in PageContext
		//
		pageContext.setAttribute(lineComplaints, lineComplaintsValue);
		pageContext.setAttribute(deptComplaints, deptComplaintsValue);
		pageContext.setAttribute(miscComplaints, miscComplaintsValue);
		pageContext.setAttribute(fullComplaints, fullComplaintsValue);
		pageContext.setAttribute(restockComplaints, restockComplaintsValue);
		pageContext.setAttribute(complaints, complaintsValue);

		return EVAL_BODY_BUFFERED;

    } // method doStartTag

} // class ConfigureProductTag