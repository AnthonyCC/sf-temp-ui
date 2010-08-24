package com.freshdirect.webapp.taglib.fdstore;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.freshdirect.delivery.restriction.DlvRestrictionsList;
import com.freshdirect.delivery.restriction.EnumDlvRestrictionCriterion;
import com.freshdirect.delivery.restriction.EnumDlvRestrictionReason;
import com.freshdirect.delivery.restriction.EnumDlvRestrictionType;
import com.freshdirect.delivery.restriction.RestrictionI;
import com.freshdirect.fdstore.FDDeliveryManager;
import com.freshdirect.framework.util.DateRange;
import com.freshdirect.webapp.taglib.AbstractGetterTag;

public class GetDlvRestrictionsTag extends AbstractGetterTag<List<RestrictionI>> {

	private EnumDlvRestrictionReason reason;
	private boolean withinHorizon = false;

	public void setWithinHorizon(boolean b) {
		this.withinHorizon = b;
	}

	public void setReason(EnumDlvRestrictionReason reason) {
		this.reason = reason;
	}

	protected List<RestrictionI> getResult() throws Exception {
		DlvRestrictionsList restrictions = FDDeliveryManager.getInstance().getDlvRestrictions();
		if (withinHorizon) {
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DATE, 1);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			Date startDate = cal.getTime();

			cal.add(Calendar.DATE, 7);
			Date endDate = cal.getTime();

			return restrictions.getRestrictions(
				EnumDlvRestrictionCriterion.DELIVERY,
				this.reason,
				EnumDlvRestrictionType.ONE_TIME_RESTRICTION,
				new DateRange(startDate, endDate));
		}
		return restrictions.getRestrictions(
			EnumDlvRestrictionCriterion.DELIVERY,
			this.reason,
			EnumDlvRestrictionType.ONE_TIME_RESTRICTION);
	}

	public static class TagEI extends AbstractGetterTag.TagEI {
		protected String getResultType() {
			return "java.util.List<com.freshdirect.delivery.restriction.RestrictionI>";
		}
	}
}
