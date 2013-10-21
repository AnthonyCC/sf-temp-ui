package com.freshdirect.transadmin.web.validation;

import org.springframework.validation.Errors;

import com.freshdirect.transadmin.model.ScheduleEmployee;
import com.freshdirect.transadmin.model.ScheduleEmployeeInfo;
import com.freshdirect.transadmin.web.model.WebSchedule;


public class ScheduleValidator extends AbstractValidator {

	@SuppressWarnings("rawtypes")
	public boolean supports(Class clazz) {
		return WebSchedule.class.isAssignableFrom(clazz);
	}

	public void validate(Object obj, Errors errors) {
		WebSchedule model = (WebSchedule) obj;
		validate(model.getMon(), "mon", errors);
		validate(model.getTue(), "tue", errors);
		validate(model.getWed(), "wed", errors);
		validate(model.getThu(), "thu", errors);
		validate(model.getFri(), "fri", errors);
		validate(model.getSat(), "sat", errors);
		validate(model.getSun(), "sun", errors);

	}
	
	public void validate(ScheduleEmployee s, String id, Errors errors) {
		if (s == null) {
			return;
		}
		if (s.getRegion() != null
				&& !s.getRegion().getCode().equals(ScheduleEmployeeInfo.OFF)) {
			if (s.getDispatchGroupTime() == null) {
				errors.rejectValue(id + ".dispatchGroupS", "app.error.112",	new Object[] { "Group time" }, "required field");
			}
			if (s.getRegion().getCode().equals("Depot")
					&& s.getDepotFacility() == null) {
				errors.rejectValue(id + ".depotFacility", "app.error.112",	new Object[] { "Depot Facility" }, "required field");
			}
			if (!s.getRegion().getCode().equals("Depot")) {
				s.setDepotFacility(null);
			}
		}
	}
}

