package com.freshdirect.test.checkout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.freshdirect.delivery.restriction.DlvRestrictionsList;
import com.freshdirect.delivery.restriction.EnumDlvRestrictionCriterion;
import com.freshdirect.delivery.restriction.EnumDlvRestrictionReason;
import com.freshdirect.delivery.restriction.FDRestrictedAvailability;
import com.freshdirect.erp.model.ErpInventoryEntryModel;
import com.freshdirect.erp.model.ErpInventoryModel;
import com.freshdirect.fdstore.FDDeliveryManager;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.atp.FDAvailabilityI;
import com.freshdirect.fdstore.atp.FDCompositeAvailability;
import com.freshdirect.fdstore.atp.FDMuniAvailability;
import com.freshdirect.fdstore.atp.FDStockAvailability;
import com.freshdirect.fdstore.atp.NullAvailability;
import com.freshdirect.fdstore.customer.FDCartLineI;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.taglib.fdstore.SessionName;

/**
 * use this test after a time slot has been selected to go back to a mocked step_2_unavailable.jsp
 * 
 * @author tgelesz
 */
public class TestUnavailabilityServlet extends HttpServlet {

	private static final long serialVersionUID = -2857072784874929405L;

	@SuppressWarnings("unchecked")
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		FDSessionUser user = (FDSessionUser) request.getSession().getAttribute(SessionName.USER);
		FDCartModel cart = user.getShoppingCart();
		LinkedList<FDAvailabilityI> failures = new LinkedList<FDAvailabilityI>();

		failures.add(new FDMuniAvailability(null));

		DlvRestrictionsList allRestrictions;
		try {
			allRestrictions = FDDeliveryManager.getInstance().getDlvRestrictions();
		} catch (FDResourceException e) {
			throw new ServletException(e);
		}
		List<EnumDlvRestrictionReason> reasons = EnumDlvRestrictionReason.getEnumList();
		for (EnumDlvRestrictionReason reason : reasons) {
			Set<EnumDlvRestrictionReason> singleReasonSet = new HashSet<EnumDlvRestrictionReason>();
			singleReasonSet.add(reason);
			failures.add(new FDRestrictedAvailability(NullAvailability.AVAILABLE, new DlvRestrictionsList(allRestrictions.getRestrictions(EnumDlvRestrictionCriterion.DELIVERY, singleReasonSet))));
			failures.add(new FDRestrictedAvailability(NullAvailability.AVAILABLE, new DlvRestrictionsList(allRestrictions.getRestrictions(EnumDlvRestrictionCriterion.CUTOFF, singleReasonSet))));
			failures.add(new FDRestrictedAvailability(NullAvailability.AVAILABLE, new DlvRestrictionsList(allRestrictions.getRestrictions(EnumDlvRestrictionCriterion.PURCHASE, singleReasonSet))));
		}

		Map<String, FDAvailabilityI> invs = new HashMap<String, FDAvailabilityI>();
		double availableAmount = 0;
		boolean odd = true;

		for (FDCartLineI cartline : cart.getOrderLines()) {
			FDAvailabilityI inv;
			if (odd || failures.isEmpty()) {
				availableAmount %= 3;
				List<ErpInventoryEntryModel> erpEntries = new ArrayList<ErpInventoryEntryModel>();
				erpEntries.add(new ErpInventoryEntryModel(new Date(), availableAmount++));
				inv = new FDStockAvailability(new ErpInventoryModel("na", new Date(), erpEntries), cartline.getQuantity(), 1, 1, null);
			} else {
				inv = failures.pop();
			}
			invs.put(Integer.toString(cartline.getRandomId()), inv);
			odd = !odd;
		}

		cart.setAvailability(new FDCompositeAvailability(invs));

        if (cart.getDeliveryReservation() != null) {
            cart.getDeliveryReservation().getTimeslot().setMinOrderAmt(100000);
        }

		if ("true".equals(request.getParameter("xc"))) {
			response.sendRedirect("/expressco/checkout.jsp");
		} else {
			response.sendRedirect("/checkout/step_2_unavail.jsp?successPage=/checkout/step_3_choose.jsp");
		}
	}

}
