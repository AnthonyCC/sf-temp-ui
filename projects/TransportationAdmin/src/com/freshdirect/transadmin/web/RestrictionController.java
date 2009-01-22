package com.freshdirect.transadmin.web;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.servlet.ModelAndView;

import com.freshdirect.transadmin.model.Dispatch;
import com.freshdirect.transadmin.model.DlvBuildingDtl;
import com.freshdirect.transadmin.model.GeoRestriction;
import com.freshdirect.transadmin.model.Region;
import com.freshdirect.transadmin.model.TrnAdHocRoute;
import com.freshdirect.transadmin.model.TrnArea;
import com.freshdirect.transadmin.model.TrnCutOff;
import com.freshdirect.transadmin.model.TrnTruck;
import com.freshdirect.transadmin.model.TrnZoneType;
import com.freshdirect.transadmin.model.Zone;
import com.freshdirect.transadmin.service.DomainManagerI;
import com.freshdirect.transadmin.service.EmployeeManagerI;
import com.freshdirect.transadmin.service.LocationManagerI;
import com.freshdirect.transadmin.service.RestrictionManagerI;
import com.freshdirect.transadmin.util.EnumCachedDataType;
import com.freshdirect.transadmin.util.TransStringUtil;
import com.freshdirect.transadmin.web.model.WebEmployeeInfo;

/**
 * <code>MultiActionController</code> that handles all non-form URL's.
 *
 * @author Sivachandar
 */
public class RestrictionController extends AbstractMultiActionController {


	private RestrictionManagerI restrictionManagerService;


	private static final String IS_OBSOLETE = "X";
	
	private static final DateFormat DATE_FORMAT=new SimpleDateFormat("MM/dd/yyyy");

	/**
	 * Custom handler for welcome
	 * @param request current HTTP request
	 * @param response current HTTP response
	 * @return a ModelAndView to render the response
	 */
	public ModelAndView geoRestrictionHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException {

		Collection dataList = restrictionManagerService.getGeoRestrictions();
/*
		Iterator iterator = dataList.iterator();
		GeoRestriction type = null;
		while(iterator.hasNext()) {
			type = (GeoRestriction)iterator.next();
			type.setActive( type.getActive() != null && type.getActive().equals("1")  ? "Yes" : "No");
		}
		*/
		
		return new ModelAndView("geoRestrictionView","geoRestrictions",dataList);
	}

	/**
	 * Custom handler for welcome
	 * @param request current HTTP request
	 * @param response current HTTP response
	 * @return a ModelAndView to render the response
	 */
	public ModelAndView geoRestrictionDeleteHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException {

		Set areaSet=new HashSet();
		String arrEntityList[] = getParamList(request);
		GeoRestriction tmpEntity = null;
		if (arrEntityList != null) {
			try {
				int arrLength = arrEntityList.length;
				for (int intCount = 0; intCount < arrLength; intCount++) {
					tmpEntity = restrictionManagerService.getGeoRestriction(arrEntityList[intCount]);
					if(tmpEntity.getActive() == null || !tmpEntity.getActive().equals("X")) {
						areaSet.add(tmpEntity);
					}
				}
				if(areaSet.size() == arrLength) {
					restrictionManagerService.removeEntity(areaSet);
					saveMessage(request, getMessage("app.actionmessage.103", null));
				} else {
					saveMessage(request, getMessage("app.actionmessage.141", null));
				}
			} catch (DataIntegrityViolationException e) {
				saveMessage(request, getMessage("app.actionmessage.141", null));
			}
		}
		return geoRestrictionHandler(request, response);
	}

	public RestrictionManagerI getRestrictionManagerService() {
		return restrictionManagerService;
	}

	public void setRestrictionManagerService(
			RestrictionManagerI restrictionManagerService) {
		this.restrictionManagerService = restrictionManagerService;
	}

}
