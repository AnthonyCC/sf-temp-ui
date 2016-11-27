package com.freshdirect.delivery.announcement;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

public class SiteAnnouncement implements Serializable {

	private final String headline;
	private final String copy;
	private final Date startDate;
	private final Date endDate;
	private final Set placements;
	private final Set userLevels;
	private final Set deliveryStatuses;
	private final Date lastOrderBefore;

	public SiteAnnouncement(
		String headline,
		String copy,
		Date startDate,
		Date endDate,
		Set placements,
		Set userLevels,
		Set deliveryStatuses,
		Date lastOrderedBefore) {

		this.headline = headline;
		this.copy = copy;
		this.startDate = startDate;
		this.endDate = endDate;
		this.placements = placements;
		this.userLevels = userLevels;
		this.deliveryStatuses = deliveryStatuses;
		this.lastOrderBefore = lastOrderedBefore;
	}

	public String getHeadline() {
		return headline;
	}

	public String getCopy() {
		return copy;
	}

	public Date getStartDate() {
		return startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public Set getPlacements() {
		return placements;
	}

	public Set getUserLevels() {
		return userLevels;
	}

	public Set getDeliveryStatuses() {
		return deliveryStatuses;
	}

	public Date getLastOrderBefore() {
		return this.lastOrderBefore;
	}

	public boolean isDisplayable(
		Date now,
		Date lastOrderDate,
		EnumPlacement placement,
		EnumUserDeliveryStatus deliveryStatus,
		EnumUserLevel userLevel) {

		return this.deliveryStatuses.contains(deliveryStatus)
			&& this.placements.contains(placement)
			&& this.userLevels.contains(userLevel)
			&& this.startDate.before(now)
			&& this.endDate.after(now)
			&& (this.lastOrderBefore == null || lastOrderDate == null || lastOrderDate.before(this.lastOrderBefore));

	}

}
