package com.freshdirect.transadmin.web;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import com.freshdirect.framework.util.StringUtil;
import com.freshdirect.transadmin.model.Plan;
import com.freshdirect.transadmin.model.Scrib;
import com.freshdirect.transadmin.security.SecurityManager;
import com.freshdirect.transadmin.service.DispatchManagerI;
import com.freshdirect.transadmin.service.DomainManagerI;
import com.freshdirect.transadmin.service.EmployeeManagerI;
import com.freshdirect.transadmin.util.TransStringUtil;
import com.freshdirect.transadmin.util.TransStringUtil.DateFilterException;

public class ScribController extends AbstractMultiActionController
{
	private DispatchManagerI dispatchManagerService;
	private EmployeeManagerI employeeManagerService;
	private DomainManagerI domainManagerService;
	
	
	
	
	
	
	public ModelAndView scribHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException 
	{
		try {
			String daterange = request.getParameter("daterange");		
			if(daterange==null)daterange=TransStringUtil.getCurrentServerDate();	
			else daterange=TransStringUtil.getServerDate(daterange);
			Collection scribs=dispatchManagerService.getScribList(daterange);
			ModelAndView mav = new ModelAndView("scribView");	
			mav.getModel().put("scriblist",scribs);
			return mav;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException("Error in getiing Scrib List");
		}
	}
	
	public ModelAndView deleteScribHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException 
	{
		try {
			Set scribSet=new HashSet();
			String arrEntityList[] = getParamList(request);
			if (arrEntityList != null) {
				int arrLength = arrEntityList.length;
				for (int intCount = 0; intCount < arrLength; intCount++) 
				{
					Scrib p=dispatchManagerService.getScrib(arrEntityList[intCount]);					
					scribSet.add(p);
				}
			}
			dispatchManagerService.removeEntity(scribSet);
			saveMessage(request, getMessage("app.actionmessage.103", null));
			return scribHandler(request,response);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException("Error in getiing Scrib List");
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public DispatchManagerI getDispatchManagerService() {
		return dispatchManagerService;
	}
	public void setDispatchManagerService(DispatchManagerI dispatchManagerService) {
		this.dispatchManagerService = dispatchManagerService;
	}
	public DomainManagerI getDomainManagerService() {
		return domainManagerService;
	}
	public void setDomainManagerService(DomainManagerI domainManagerService) {
		this.domainManagerService = domainManagerService;
	}
	public EmployeeManagerI getEmployeeManagerService() {
		return employeeManagerService;
	}
	public void setEmployeeManagerService(EmployeeManagerI employeeManagerService) {
		this.employeeManagerService = employeeManagerService;
	}
	
	
	
}
