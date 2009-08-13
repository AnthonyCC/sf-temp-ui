package com.freshdirect.transadmin.web;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.ServletRequestDataBinder;

import com.freshdirect.framework.util.EnumLogicalOperator;
import com.freshdirect.framework.util.TimeOfDay;
import com.freshdirect.transadmin.model.GeoRestriction;
import com.freshdirect.transadmin.model.GeoRestrictionDays;
import com.freshdirect.transadmin.model.GeoRestrictionDaysId;
import com.freshdirect.transadmin.service.RestrictionManagerI;
import com.freshdirect.transadmin.util.EnumDayOfWeek;
import com.freshdirect.transadmin.util.TransStringUtil;
import com.freshdirect.transadmin.web.editor.TimeOfDayPropertyEditor;

public class GeoRestrictionFormController extends AbstractFormController {
	private RestrictionManagerI restrictionManagerService;

	protected Map referenceData(HttpServletRequest request)
			throws ServletException {
		Map refData = new HashMap();
		
		refData.put("restrictionDays", (Set) request
				.getAttribute("restrictionDaysList"));
		refData.put("restrictionBoundries", getRestrictionManagerService()
				.getGeoRestrictionBoundaries());
		refData.put("DayOfWeeks", (List) EnumDayOfWeek.getEnumList());
		refData.put("conditions", (List) EnumLogicalOperator.getEnumList());
		return refData;
	}

	public RestrictionManagerI getRestrictionManagerService() {
		return restrictionManagerService;
	}

	public void setRestrictionManagerService(
			RestrictionManagerI restrictionManagerService) {
		this.restrictionManagerService = restrictionManagerService;
	}

	public String getDomainObjectName() {
		return "Geo Restriction";
	}

	protected void preProcessDomainObject(Object domainObject) {
		
		GeoRestriction modelIn = (GeoRestriction) domainObject;		
	}

	public Object getDefaultBackingObject() {
		GeoRestriction domainObj = new GeoRestriction();
		return domainObj;
	}

	protected void initBinder(HttpServletRequest request,
			ServletRequestDataBinder dataBinder) throws Exception {

		super.initBinder(request, dataBinder);
		// System.out.println("inside initBinder ");
		dataBinder.registerCustomEditor(TimeOfDay.class,
				new TimeOfDayPropertyEditor());
		dataBinder.registerCustomEditor(EnumLogicalOperator.class,
				new TimeOfDayPropertyEditor());

	}

	public String[] parseRestElelemntLink(String restLink) {
		if (restLink == null || restLink.trim().length() == 0)
			return null;

		StringTokenizer tokens = new StringTokenizer(restLink.substring(1), "$");
		String tmp[] = new String[tokens.countTokens()];
		int i = 0;
		while (tokens.hasMoreElements()) {
			tmp[i++] = (String) tokens.nextElement();
		}
		return tmp;
	}

	protected void onBind(HttpServletRequest request, Object command) {

		GeoRestriction model = (GeoRestriction) command;
		if(TransStringUtil.isEmpty(model.getServiceType())) {
			model.setServiceType(null);
		}
		String restDtlSizeStr = request.getParameter("restrictionListSize");
		String restrictionId = request.getParameter("restrictionId");
		String restrictionLinkStr = request.getParameter("restrictionLinkStr");
		String restIndexStr[] = parseRestElelemntLink(restrictionLinkStr);

		Set restrictionDaysList = new HashSet();
		if (restIndexStr != null && restIndexStr.length > 0) {
			int restDtlSize = Integer.parseInt(restDtlSizeStr);
			// create restricon detail list from the request
			for (int i = 0; i < restIndexStr.length; i++) {
				int indexSize = Integer.parseInt(restIndexStr[i]);
				String dayOfWeek = request.getParameter("attributeList["
						+ (indexSize) + "].dayOfWeek");
				String condition = request.getParameter("attributeList["
						+ (indexSize) + "].condition");
				String startTime = request.getParameter("attributeList["
						+ (indexSize) + "].startTime");
				String endTime = request.getParameter("attributeList["
						+ (indexSize) + "].endTime");

				if (null == dayOfWeek || "".equals(dayOfWeek)) { // tbr
					break;
				}
				if (null == condition || "".equals(condition)) { // tbr
					break;
				}
				if (null == startTime || "".equals(startTime)) { // tbr
					break;
				}
				if (null == endTime || "".equals(endTime)) { // tbr
					break;
				}

				BigDecimal objDayOfWeek = new BigDecimal(dayOfWeek);

				GeoRestrictionDaysId id = new GeoRestrictionDaysId(
						restrictionId, objDayOfWeek, new BigDecimal(i));
				GeoRestrictionDays day;
				try {

					day = new GeoRestrictionDays(id, EnumLogicalOperator
							.getEnum(condition), new TimeOfDay(startTime),
							new TimeOfDay(endTime));
					restrictionDaysList.add(day);
				} finally {

				}
			}
		}
		model.setGeoRestrictionDays(restrictionDaysList);
		System.out.println("size of the model detail:"
				+ restrictionDaysList.size());
	}

	public Object getBackingObject(String id) {

		GeoRestriction result = getRestrictionManagerService()
				.getGeoRestriction(id);
		if (null == result) {

			result = new GeoRestriction();
			result.setName(new String("GeoRestrictionName"));
			return result;
		}
		return result;
	}

	public boolean isNew(Object command) {
		GeoRestriction modelIn = (GeoRestriction) command;
		return (modelIn.getName() == null); // tbr
	}

	public List saveDomainObject(Object domainObject) {

		List errorList = new ArrayList();
		GeoRestriction modelIn = (GeoRestriction) domainObject;

		try {

			if (modelIn.getRestrictionId() != null) {
				getRestrictionManagerService().removeEntity(
						getRestrictionManagerService().getGeoRestrictionDays(
								modelIn.getRestrictionId()));
			}

			Set tmpRestDays = modelIn.getGeoRestrictionDays();
			modelIn.setGeoRestrictionDays(null);
			getRestrictionManagerService().saveGeoRestriction(modelIn);

			modelIn.setGeoRestrictionDays(tmpRestDays);

			if (tmpRestDays != null) {
				Iterator iterator = tmpRestDays.iterator();
				while (iterator.hasNext()) {
					GeoRestrictionDays days = (GeoRestrictionDays) iterator
							.next();
					days.getRestrictionDaysId().setRestrictionId(
							modelIn.getRestrictionId());
				}
			}

			modelIn.setGeoRestrictionDays(tmpRestDays);
			getRestrictionManagerService().saveEntityList(
					modelIn.getGeoRestrictionDays());

		} catch (Exception e) {
			e.printStackTrace();
			errorList.add(this.getMessage("app.actionmessage.119",
					new Object[] { getDomainObjectName() }));
		}
		return errorList;
	}

}
