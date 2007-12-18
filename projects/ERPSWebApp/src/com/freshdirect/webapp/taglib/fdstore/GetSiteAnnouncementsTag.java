package com.freshdirect.webapp.taglib.fdstore;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.freshdirect.delivery.announcement.EnumPlacement;
import com.freshdirect.delivery.announcement.EnumUserDeliveryStatus;
import com.freshdirect.delivery.announcement.EnumUserLevel;
import com.freshdirect.delivery.announcement.SiteAnnouncement;
import com.freshdirect.fdstore.FDDeliveryManager;
import com.freshdirect.fdstore.FDSiteAnnouncementI;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.webapp.taglib.AbstractGetterTag;

public class GetSiteAnnouncementsTag extends AbstractGetterTag {

	private FDUserI user;

	public void setUser(FDUserI user) {
		this.user = user;
	}

	protected Object getResult() throws Exception {
		Date now = new Date();
		int maxPri = -1;

		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();

		EnumPlacement placement = getPlacement(request.getRequestURI());
		EnumUserDeliveryStatus deliveryStatus = getUserDeliveryStatus(this.user);
		EnumUserLevel userLevel = getUserLevel(this.user);
		Date lastOrderCreate = user.getOrderHistory().getLastOrderCreateDate();

		List siteAnnouncements = new ArrayList(FDDeliveryManager.getInstance().getSiteAnnouncement());

		List lst = new ArrayList();
		for (Iterator i = siteAnnouncements.iterator(); i.hasNext();) {
			SiteAnnouncement ann = (SiteAnnouncement) i.next();

			if (ann.isDisplayable(now, lastOrderCreate, placement, deliveryStatus, userLevel)) {

				int matchPri = placement.getPriority();
				if (matchPri > maxPri) {
					lst.clear();
					maxPri = matchPri;
				}
				if (maxPri == matchPri) {
					lst.add(adaptAnnouncement(ann));
				}
			}

		}

		return lst;
	}

	private static FDSiteAnnouncementI adaptAnnouncement(final SiteAnnouncement ann) {
		return new FDSiteAnnouncementI() {

			public String getHeader() {
				return ann.getHeadline();
			}

			public String getCopy() {
				return ann.getCopy();
			}

		};
	}

	private static EnumUserLevel getUserLevel(FDUserI user) {
		if (user.getLevel() == FDUserI.GUEST) {
			return EnumUserLevel.GUEST;
		}
		return EnumUserLevel.RECOGNIZED;
	}

	private static EnumUserDeliveryStatus getUserDeliveryStatus(FDUserI user) {
		if (user.isDepotUser()) {
			return EnumUserDeliveryStatus.DEPOT_USER;
		}
		if (user.isHomeUser()) {
			return EnumUserDeliveryStatus.HOME_USER;
		}
		if (user.isPickupOnly()) {
			return EnumUserDeliveryStatus.PICKUP_ONLY_USER;
		}
		return null;
	}

	private static EnumPlacement getPlacement(String requestURI) {

		String uri = requestURI.toLowerCase();

		//ordering of this check is important so donot change
		if (uri.startsWith("/index") || uri.equals("/")) {
			return EnumPlacement.HOME;
		}
		if (uri.startsWith("/department")) {
			return EnumPlacement.DEPARTMENT;
		}
		if (uri.indexOf("view_cart") > -1) {
			return EnumPlacement.CART;
		}
		if (uri.startsWith("/checkout/step_2_select")) {
			return EnumPlacement.TIMESLOTS;
		}
		if (uri.startsWith("/checkout")) {
			return EnumPlacement.CHECKOUT;
		}
		if (uri.startsWith("/help/delivery_")
			|| uri.startsWith("/your_account/delivery_info_check")
			|| uri.startsWith("/your_account/delivery_info_avail")) {
			return EnumPlacement.DELIVERY_INFO;
		}
		if (uri.startsWith("/your_account")) {
			return EnumPlacement.YOUR_ACCOUNT;
		}

		return EnumPlacement.UNKNOWN;
	}

	public static class TagEI extends AbstractGetterTag.TagEI {
		protected String getResultType() {
			return "java.util.List";
		}
	}

}
