package com.freshdirect.transadmin.web;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;

import com.freshdirect.framework.util.TimeOfDay;
import com.freshdirect.transadmin.model.GeoRestriction;
import com.freshdirect.transadmin.model.GeoRestrictionDays;
import com.freshdirect.transadmin.model.GeoRestrictionDaysId;
import com.freshdirect.transadmin.model.Region;
import com.freshdirect.transadmin.model.TrnArea;
import com.freshdirect.transadmin.model.TrnZoneType;
import com.freshdirect.transadmin.model.Zone;
import com.freshdirect.transadmin.service.RestrictionManagerI;
import com.freshdirect.transadmin.util.TransStringUtil;
import com.freshdirect.transadmin.web.editor.TimeOfDayPropertyEditor;

public class GeoRestrictionFormController extends AbstractFormController {
	private RestrictionManagerI restrictionManagerService;
	

	protected Map referenceData(HttpServletRequest request) throws ServletException {
		Map refData = new HashMap();		
		//refData.put("supervisors", getDomainManagerService().getSupervisors());
		//refData.put("zones", getDomainManagerService().getZones());
		refData.put("restrictionDays", (Set)request.getAttribute("restrictionDaysList"));
		
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
		System.out.println("$$$$$$entering preProcess");	

		GeoRestriction modelIn = (GeoRestriction)domainObject;
		//if(TransStringUtil.isEmpty(modelIn.getBuilding().getBuildingId()) ) {
			//modelIn.getBuilding().setBuildingId(modelIn.getBuilding().getBuildingId());
		//}
	}

	public Object getDefaultBackingObject() {
		GeoRestriction domainObj = new GeoRestriction();
		//domainObj.setIsNew("true");
		return domainObj;
	}
	
	protected void initBinder(HttpServletRequest request,
			ServletRequestDataBinder dataBinder) throws Exception {
		
		super.initBinder(request, dataBinder);
		System.out.println("inside initBinder ");				
		dataBinder.registerCustomEditor(TimeOfDay.class, new TimeOfDayPropertyEditor());
		 
	}
	
protected void onBind(HttpServletRequest request, Object command) {
		
		System.out.println("On Bind");
		GeoRestriction model = (GeoRestriction) command;
		String restDtlSizeStr=request.getParameter("restrictionListSize");
		String restrictionId=request.getParameter("restrictionId");
		Set restrictionDaysList=new HashSet();
		if(restDtlSizeStr!=null && restDtlSizeStr.trim().length()>0)
		{
			int restDtlSize=Integer.parseInt(restDtlSizeStr);
			// create restricon detail list from the request
			for(int i=0;i<restDtlSize;i++){
				 String dayOfWeek=request.getParameter("attributeList["+(i+1)+"].dayOfWeek");
				 String condition=request.getParameter("attributeList["+(i+1)+"].condition");
				 String startTime=request.getParameter("attributeList["+(i+1)+"].startTime");
				 String endTime=request.getParameter("attributeList["+(i+1)+"].endTime");
				 
				 System.out.println("dayOfWeek :"+dayOfWeek);
				 System.out.println("condition :"+condition);
				 System.out.println("startTime :"+startTime);
				 System.out.println("endTime :"+endTime);
				 
				 GeoRestrictionDaysId id=new GeoRestrictionDaysId(restrictionId,new BigDecimal(dayOfWeek),new BigDecimal(i));
				 GeoRestrictionDays day;
				try {
					
					day = new GeoRestrictionDays(id,condition,new TimeOfDay(startTime),
							                       			  new TimeOfDay(endTime));
					restrictionDaysList.add(day);
				//} catch (ParseException e) {
					// TODO Auto-generated catch block
				//	e.printStackTrace();
				}
				finally{
					
				}
			}
		}		
		model.setGeoRestrictionDays(restrictionDaysList);
		System.out.println("\n@@@@@@@\n@@@@@@\n@@@@@");
		System.out.println(model);
		System.out.println("\n@@@@@@@\n@@@@@@\n@@@@@");
		request.setAttribute("restrictionDaysList",restrictionDaysList );
		System.out.println("size of the model detail:"+restrictionDaysList.size());
		
	}
	
	
	
	public Object getBackingObject(String id) {
		System.out.println("#########entering getBackingObject");

		//return getLocationManagerService().getDlvBuildingDtl(id);
		GeoRestriction result = getRestrictionManagerService().getGeoRestriction(id);
		if(null == result){
			//DlvBuilding building = getLocationManagerService().getDlvBuilding(id);
			result = new GeoRestriction();
			//result.setBuilding(building);
			result.setName(new String("GeoRestrictionName"));
			//result.setIsNew("true");
			return result;
		}

		//result.setIsNew("false");
		
		System.out.println("#########exiting getBackingObject");
		
		return result;		
	}
	
	
	public boolean isNew(Object command) {
		GeoRestriction modelIn = (GeoRestriction)command;
		return (modelIn.getName() == null);  //tbr
	}
	
	
//	public ModelAndView onSubmit(HttpServletRequest request,
//			HttpServletResponse response, Object command, BindException errors)
//			throws Exception {
//		
//		System.out.println();
//		
//		String messageKey = isNew(command) ? "app.actionmessage.101"
//				: "app.actionmessage.102";
//
//		preProcessDomainObject(command);
//		
//		GeoRestriction modelIn = (GeoRestriction)command;
//		
//		getRestrictionManagerService().removeEntity(modelIn.getGeoRestrictionDays());
//		List tmpList=new ArrayList();
//		tmpList.add(modelIn);
//		getRestrictionManagerService().removeEntity(tmpList);
//		
//		System.out.println("removing both parent and child");
//		
//		Set restDaysList=(Set)request.getAttribute("restrictionDaysList"); 
//		modelIn.setGeoRestrictionDays(restDaysList);
//		List errorList = saveDomainObject(command);
//
//		ModelAndView mav = new ModelAndView(getSuccessView(), errors.getModel());
//		mav.getModel().put(this.getCommandName(), command);
//		mav.getModel().putAll(referenceData(request));
//		if(errorList == null || errorList.isEmpty()) {
//			saveMessage(request, getMessage(messageKey,
//					new Object[] { getDomainObjectName() }));
//		} else {
//			saveErrorMessage(request, errorList);
//		}
//		return mav;
//	}
	
	
	public List saveDomainObject(Object domainObject) {
		
		System.out.println("entering to save");	
		List errorList = new ArrayList();
		GeoRestriction modelIn = (GeoRestriction)domainObject;
		System.out.println(modelIn);  
				
		try 
		{
			//getRestrictionManagerService().removeEntity(modelIn.getGeoRestrictionDays());
			
			//System.out.println("getRestrictionManagerService().removeEntity :"+);
			
			getRestrictionManagerService().saveEntity(domainObject);			
		} catch(Exception e) {
			e.printStackTrace();
			errorList.add(this.getMessage("app.actionmessage.119", new Object[]{getDomainObjectName()}));
		}
		return errorList;
	}
	
	
	

}
