/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.webapp.taglib.callcenter;

import javax.servlet.http.*;
import javax.servlet.jsp.*;

import org.apache.log4j.*;

import com.freshdirect.customer.*;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.taglib.fdstore.SessionName;
import com.freshdirect.fdstore.customer.*;

/**
 *
 * @version $Revision$
 * @author $Author$
 */
public class GetCustomerObjectsTag extends com.freshdirect.framework.webapp.BodyTagSupport implements SessionName {

	private static Category LOGGER = LoggerFactory.getInstance( GetCustomerObjectsTag.class );

    private String fdCustomerId;
    private String erpCustomerId;
    private String erpCustomerInfoId;

    private FDIdentity identity;

    public void setIdentity(FDIdentity identity) {
        this.identity = identity;
    }

    public void setFdCustomerId(String s) {
        this.fdCustomerId = s;
    }

    public void setErpCustomerId(String s) {
        this.erpCustomerId = s;
    }

    public void setErpCustomerInfoId(String s) {
        this.erpCustomerInfoId = s;
    }

    public int doStartTag() throws JspException {

		HttpSession session = pageContext.getSession();

		FDCustomerModel fdCustomer = null;
		ErpCustomerModel erpCustomer = null;
		//
		// Now get Customer information
		//
		try {
			if (identity != null) {
                //
                // identity supplied, look 'em up
                //
                fdCustomer = FDCustomerFactory.getFDCustomer( identity.getFDCustomerPK() );
                erpCustomer = FDCustomerFactory.getErpCustomer( identity.getErpCustomerPK() );

                session.setAttribute(SessionName.FDCUSTOMER, fdCustomer);
                session.setAttribute(SessionName.ERPCUSTOMER, erpCustomer);

            } else {
                //
                // no identity, recall from session
                //
                fdCustomer = (FDCustomerModel) session.getAttribute(SessionName.FDCUSTOMER);
                erpCustomer = (ErpCustomerModel) session.getAttribute(SessionName.ERPCUSTOMER);
            }
			//
			// place as scripting variables in the page
			//
			if (fdCustomer != null) pageContext.setAttribute(fdCustomerId, fdCustomer);
            if (erpCustomer != null) {
                pageContext.setAttribute(erpCustomerId, erpCustomer);
                pageContext.setAttribute(erpCustomerInfoId, erpCustomer.getCustomerInfo());
            }
		} catch (Exception ex) {
			LOGGER.warn("Exception", ex);
            throw new JspException(ex.getMessage());
		}

		return EVAL_BODY_BUFFERED;
    }


}
