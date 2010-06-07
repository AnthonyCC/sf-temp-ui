package com.freshdirect.transadmin.web;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
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
import com.freshdirect.transadmin.util.scrib.PlanTree;
import com.freshdirect.transadmin.util.scrib.SchdeuleEmployeeDetails;
import com.freshdirect.transadmin.web.model.WebEmployeeInfo;

public class ScribController extends AbstractMultiActionController
{
	private DispatchManagerI dispatchManagerService;
	private EmployeeManagerI employeeManagerService;
	private DomainManagerI domainManagerService;
	
	
	
	public ModelAndView copyScribHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException 
	{
		try {
			String sourceDate=request.getParameter("sourceDate");
			String destDate=request.getParameter("destinationDate");
			String sourceDay=request.getParameter("sDay");
			
			if(sourceDate!=null&&destDate!=null&&sourceDay!=null)
			{
				List toInsert=new ArrayList();
				List toDelate=new ArrayList();
				String[] sourceDates=getDates(sourceDate, sourceDay);
				String[] destDates=getDates(destDate, sourceDay);
				if(sourceDates!=null&&sourceDates.length>0&&!sourceDates[0].equalsIgnoreCase(destDates[0]))
				{	
					for(int i=0;i<sourceDates.length;i++)
					{
						Collection scribs=dispatchManagerService.getScribList(sourceDates[i]);
						Collection deleteScribs=dispatchManagerService.getScribList(destDates[i]);
						toDelate.addAll(deleteScribs);
						if(scribs!=null)
							for(Iterator j=scribs.iterator();j.hasNext();)
							{
								Scrib s=(Scrib)j.next();
								s.setScribDate(TransStringUtil.serverDateFormat.parse(destDates[i]));
								s.setScribId(null);
								toInsert.add(s);
								
							}
					}
					dispatchManagerService.removeEntity(toDelate);
					dispatchManagerService.saveEntityList(toInsert);
					saveMessage(request, getMessage("app.actionmessage.148", null));
				}
				
				
			}
			ModelAndView mav = new ModelAndView("copyScribView");
			return mav;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ModelAndView mav = new ModelAndView("copyScribView");
			saveMessage(request, getMessage("app.actionmessage.151", null));
			return mav;
		}
	}
	
	
	public ModelAndView scribHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException 
	{
		try {
			String daterange = request.getParameter("daterange");
			if(daterange==null)daterange=TransStringUtil.getCurrentDate();
			String day=request.getParameter("scribDay");
			if(day==null)day="All";
			String[] dates=getDates(daterange,day);
			List allScribs=new ArrayList();
			if(dates!=null)
			{	
				for(int i=0;i<dates.length;i++)
				{
					Collection scribs=dispatchManagerService.getScribList(dates[i]);
					if(scribs!=null)
					for(Iterator j=scribs.iterator();j.hasNext();)
					{
						Scrib s=(Scrib)j.next();
						if(s.getSupervisorCode()!=null)
						{
							WebEmployeeInfo webEmp=employeeManagerService.getEmployee(s.getSupervisorCode());
							if(webEmp!=null&&webEmp.getEmpInfo()!=null)
							s.setSupervisorName(webEmp.getEmpInfo().getName());
						}
					}
					allScribs.addAll(scribs);
					if("y".equalsIgnoreCase(request.getParameter("p")))
					{
						createPlan(dates[i],request);						
					}
				}
				if("y".equalsIgnoreCase(request.getParameter("p")))
				{					
					saveMessage(request, getMessage("app.actionmessage.149", null));
				}
			}
			ModelAndView mav = new ModelAndView("scribView");	
			mav.getModel().put("scriblist",allScribs);
			return mav;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			saveMessage(request, getMessage("app.actionmessage.151", null));
			return new ModelAndView("scribView");	
		}
	}
	
	public void createPlan(String scribDate,HttpServletRequest request)throws Exception 
	{		
		
		Collection planList=dispatchManagerService.getPlanList(scribDate);
		for(Iterator i=planList.iterator();i.hasNext();)
		{
			Plan p=(Plan)i.next();
			p.setUserId(SecurityManager.getUserName(request));
		}
		dispatchManagerService.removeEntity(planList);
		Date date=TransStringUtil.serverDateFormat.parse(scribDate);
		Collection scribs=dispatchManagerService.getScribList(scribDate);
		String day=new SimpleDateFormat("EEE").format(date).toUpperCase();
		Collection employees=employeeManagerService.getScheduledEmployees(day,scribDate);
		filldate(date,employees);
		
		PlanTree tree=new PlanTree();
		tree.prepare(scribs);
		tree.prepare(employees);
		tree.prepareTeam(domainManagerService.getTeamInfo());
		Collection plans=tree.getPlan();
		for(Iterator i=plans.iterator();i.hasNext();)
		getDispatchManagerService().savePlan((Plan)i.next());
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
	
	
	
	
	public void filldate(Date date,Collection c)
	{
		for(Iterator it=c.iterator();it.hasNext();)
		{
			SchdeuleEmployeeDetails detail=( SchdeuleEmployeeDetails)it.next();
			detail.setDate(date);
		}
	}
	
	
	public String[] getDates(String date,String day) throws Exception
	{
		
		Date d=TransStringUtil.getDate(date);
		Calendar c=Calendar.getInstance();
		c.setFirstDayOfWeek(Calendar.MONDAY);
		c.setTime(d);
		if("All".equalsIgnoreCase(day))
		{
			String[] dates=new String[7];			
			for(int i=2;i<=8;i++)
			{
			c.set(Calendar.DAY_OF_WEEK , i);
			/*if(i==8)
			{
				c.set(Calendar.DAY_OF_WEEK , 7);
				c.set(Calendar.DAY_OF_YEAR, c.get(Calendar.DAY_OF_YEAR)+1);
			}*/
			String ds=TransStringUtil.getServerDate(c.getTime());
			dates[i-2]=ds;
			}
			return dates;
		}
		else
		{
			if(day==null)
			{
				return new String[]{TransStringUtil.getServerDate(c.getTime())};
			}
			else
			{
				try {
					int k=Integer.parseInt(day);
				//	if(k<8)
					{
					c.set(Calendar.DAY_OF_WEEK , k);
					}
				/*	else
					{
						c.set(Calendar.DAY_OF_WEEK , 7);
						c.set(Calendar.DAY_OF_YEAR, c.get(Calendar.DAY_OF_YEAR)+1);
					}*/
					String ds=TransStringUtil.getServerDate(c.getTime());
					return new String[]{ds};
				} catch (Exception e) {
					
				}
			}
		}
		return null;
		
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
