package com.freshdirect.webapp.taglib.erp;

import javax.servlet.jsp.JspException;


public class AvailabilityControllerTag extends com.freshdirect.framework.webapp.TagSupport {

	//private static Category LOGGER = LoggerFactory.getInstance(AvailabilityControllerTag.class);

	public int doStartTag() throws JspException {
		/*
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		if ("POST".equalsIgnoreCase(request.getMethod())) {
			String action = request.getParameter("action");
			String materialNumber = request.getParameter("materialNumber");
			Context ctx = null;
			ErpInventoryHome home = null;
			ErpUnavailabilityModel unavailability = null;
			try {
				ctx = ErpServicesProperties.getInitialContext();
				if ("update".equalsIgnoreCase(action)) {
					unavailability = processForm(request);
					home = (ErpInventoryHome) getHome("freshdirect.erp.Inventory", ctx);
					ErpInventoryEB eb = (ErpInventoryEB) home.findByPrimaryKey(new PrimaryKey(materialNumber));
					eb.setUnavailability(unavailability);

				}
				if ("delete".equalsIgnoreCase(action)) {
					home = (ErpInventoryHome) getHome("freshdirect.erp.Inventory", ctx);
					ErpInventoryEB eb = (ErpInventoryEB) home.findByPrimaryKey(new PrimaryKey(materialNumber));
					eb.removeUnavailability();
				}
			} catch (NamingException ne) {
				throw new JspException(ne.getMessage());
			} catch (RemoteException re) {
				throw new JspException(re.getMessage());
			} catch (FinderException fe) {
				try {
					if("update".equalsIgnoreCase(action)){
						ErpInventoryModel inventory = new ErpInventoryModel(materialNumber, new Date(), new ArrayList());
						inventory.setUnavailability(unavailability);
						home.create(inventory);
					}
				} catch (CreateException ce) {
					throw new JspException(ce.getMessage());
				} catch (RemoteException re) {
					throw new JspException(re.getMessage());
				}
			} finally {
				try {
					if (ctx != null) {
						ctx.close();
						ctx = null;
					}
				} catch (NamingException ne) {
					LOGGER.warn("Exception while trying to close Context", ne);
				}
			}
		}
		*/
		return EVAL_PAGE;
	}

	/*
	private ErpUnavailabilityModel processForm(HttpServletRequest request) throws JspException {
		SimpleDateFormat sf = new SimpleDateFormat("MM-dd-yyyy");

		String startDate = request.getParameter("start_date");
		String endDate = request.getParameter("end_date");
		String shortDesc = request.getParameter("short_desc");
		String longDesc = request.getParameter("long_desc");
		ErpUnavailabilityModel model = null;
		try {
			model = new ErpUnavailabilityModel(sf.parse(startDate), sf.parse(endDate), shortDesc, longDesc, new Date());
		} catch (ParseException pe) {
			throw new JspException(pe.getMessage());
		}
		return model;
	}
	*/
}