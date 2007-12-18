/*
 * 
 * LocationControllerTag.java
 * Date: Aug 6, 2002 Time: 6:39:50 PM
 */
package com.freshdirect.webapp.taglib.dlv;

/**
 * 
 * @author knadeem
 */
import java.text.*;
import java.io.IOException;
import javax.naming.*;
import javax.ejb.*;
import java.rmi.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.BodyContent;

import org.apache.log4j.Category;

import com.freshdirect.common.address.AddressModel;
import com.freshdirect.delivery.depot.DlvLocationModel;
import com.freshdirect.delivery.depot.ejb.*;
import com.freshdirect.delivery.*;
import com.freshdirect.fdstore.FDDeliveryManager;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.framework.util.NVL;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.webapp.taglib.AbstractControllerTag;

public class LocationControllerTag extends AbstractControllerTag {

	private static Category LOGGER = LoggerFactory.getInstance(LocationControllerTag.class);

	private String depotId;
	private String redirectPage;
	private DlvLocationModel location;

	public void setDepotId(String depotId) {
		this.depotId = depotId;
	}

	public void setLocation(DlvLocationModel location) {
		this.location = location;
	}

	public void setRedirectPage(String redirectPage) {
		this.redirectPage = redirectPage;
	}

	protected boolean performAction(HttpServletRequest request, ActionResult actionResult) throws JspException {
		String action = this.getActionName();
		Context ctx = null;

		try {

			ctx = DlvProperties.getInitialContext();
			DlvDepotManagerHome home = (DlvDepotManagerHome) getHome("freshdirect.delivery.DepotManager", ctx);
			DlvDepotManagerSB sb = home.create();

			if ("add_location".equalsIgnoreCase(action)) {
				this.populateLocation(request);
				this.validate(actionResult);
				if (actionResult.isSuccess()) {
					sb.addDepotLocation(this.depotId, this.location);
				}
			}

			if ("edit_location".equalsIgnoreCase(action)) {
				this.populateLocation(request);
				this.validate(actionResult);
				if (actionResult.isSuccess()) {
					sb.updateDepotLocation(this.depotId, this.location);
				}
			}

			if ("delete_location".equalsIgnoreCase(action)) {
				sb.deleteDepotLocation(this.depotId, this.location.getPK().getId());
			}

			return true;

		} catch (NamingException e) {
			throw new JspException(e);
		} catch (CreateException e) {
			throw new JspException(e);
		} catch (RemoteException e) {
			throw new JspException(e);
		} catch (DlvResourceException e) {
			throw new JspException(e);
		} catch (ParseException e) {
			throw new JspException(e);
		} catch (FDResourceException e) {
			throw new JspException(e);
		} finally {
			try {
				if (ctx != null) {
					ctx.close();
				}
			} catch (NamingException e) {
				LOGGER.warn("NamingException while trying to close CTX in finally cleanup", e);

			}
		}
	}

	private void populateLocation(HttpServletRequest request) throws ParseException {
		SimpleDateFormat sf = new SimpleDateFormat("MM-dd-yyyy");

		String startDate = NVL.apply(request.getParameter("startDate"), "");
		String endDate = NVL.apply(request.getParameter("endDate"), "");
		boolean deliveryChargeWaived = request.getParameter("deliveryChargeWaived") != null;

		location.setFacility(NVL.apply(request.getParameter("facility"), ""));
		location.setStartDate(!"".equals(startDate) ? sf.parse(startDate) : null);
		location.setEndDate(!"".equals(endDate) ? sf.parse(endDate) : null);
		location.setZoneCode(NVL.apply(request.getParameter("zoneCode"), ""));
		location.setInstructions(NVL.apply(request.getParameter("instructions"), ""));
		location.setDeliveryChargeWaived(deliveryChargeWaived);

		AddressModel address = new AddressModel();
		address.setAddress1(NVL.apply(request.getParameter("address1"), ""));
		address.setAddress2(NVL.apply(request.getParameter("address2"), ""));
		address.setApartment(NVL.apply(request.getParameter("apartment"), ""));
		address.setCity(NVL.apply(request.getParameter("city"), ""));
		address.setState(NVL.apply(request.getParameter("state"), "").trim());
		address.setZipCode(NVL.apply(request.getParameter("zipCode").trim(), ""));

		location.setAddress(address);
	}

	private void validate(ActionResult actionResult) throws FDResourceException {

		actionResult.addError("".equals(location.getFacility()), "facility", "required");
		actionResult.addError("".equals(location.getZoneCode()), "zoneCode", "required");
		actionResult.addError(location.getStartDate() == null, "startDate", "required");
		actionResult.addError(location.getEndDate() == null, "endDate", "required");

		AddressModel address = location.getAddress();
		actionResult.addError("".equals(address.getAddress1()), "address1", "required");
		actionResult.addError("".equals(address.getCity()), "city", "required");
		actionResult.addError("".equals(address.getState()), "state", "required");
		actionResult.addError("".equals(address.getZipCode()), "zipCode", "required");
		if (actionResult.isSuccess()) {

			DlvAddressVerificationResponse response = FDDeliveryManager.getInstance().scrubAddress(address, true);
			actionResult.addError(
				!EnumAddressVerificationResult.ADDRESS_OK.equals(response.getResult()),
				"badAddress",
				response.getResult().getCode());
		}

	}

	public int doEndTag() throws JspException {
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		String action = this.getActionName();
		String method = request.getMethod();

		if ("POST".equalsIgnoreCase(method) && "delete_location".equalsIgnoreCase(action)) {
			try {
				HttpServletResponse response = (HttpServletResponse) pageContext.getResponse();
				response.sendRedirect(response.encodeRedirectURL(redirectPage));
				JspWriter writer = pageContext.getOut();
				writer.close();
			} catch (IOException ie) {
				throw new JspException(ie.getMessage());
			}

			return SKIP_PAGE;
		} else {
			try {
				BodyContent body = getBodyContent();
				body.writeOut(getPreviousOut());
			} catch (IOException e) {
				LOGGER.warn("IOException while trying to write the body", e);
			}
			return EVAL_PAGE;
		}
	}

	public static class TagEI extends AbstractControllerTag.TagEI {
		// default impl
	}

}
