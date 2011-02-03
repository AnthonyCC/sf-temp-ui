package com.freshdirect.cms.fdstore;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.labels.ILabelProvider;

public class HolidayGreetingLabelProvider implements ILabelProvider {
	private static final DateFormat MONTH_DAY = new SimpleDateFormat("MM/dd");

	@Override
	public String getLabel(ContentNodeI node) {
		ContentType type = node.getKey().getType();

		if (FDContentTypes.HOLIDAY_GREETING.equals(type)) {
			StringBuilder buf = new StringBuilder();
			Object code = node.getAttribute("CODE").getValue();
			Object description = node.getAttribute("FULL_NAME").getValue();
			if (code == null && description == null) {
				return node.getKey().getEncoded();
			} else if (description != null)
				buf.append(description);
			else if (code != null) {
				buf.append("Code: ");
				buf.append(code);
			}
			
			Object startDate = node.getAttribute("startDate").getValue();
			Object endDate = node.getAttribute("endDate").getValue();
			if (startDate != null) {
				Date date = (Date) startDate;
				if (endDate != null)
					buf.append(", start=");
				else
					buf.append(", date=");
				buf.append(MONTH_DAY.format(date));
			}
			if (endDate != null) {
				Date date = (Date) endDate;
				buf.append(", end=");
				buf.append(MONTH_DAY.format(date));
			}
			return buf.toString();
		}
		return null;
	}
}
