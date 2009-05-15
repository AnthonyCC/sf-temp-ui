package com.freshdirect.transadmin.log;

import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;

import org.springframework.aop.AfterReturningAdvice;
import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.aop.support.DefaultPointcutAdvisor;

import com.freshdirect.transadmin.dao.DispatchManagerDaoI;
import com.freshdirect.transadmin.model.Dispatch;
import com.freshdirect.transadmin.model.DispatchResource;
import com.freshdirect.transadmin.model.Plan;
import com.freshdirect.transadmin.model.PlanResource;
import com.freshdirect.transadmin.service.LogManagerI;

public class ActivityLogAdvisor implements MethodBeforeAdvice
{
	private LogManagerI logManager;
	private DispatchManagerDaoI dispatchManagerDao;
	
	
	public void setDispatchManagerDao(DispatchManagerDaoI dispatchManagerDao) {
		this.dispatchManagerDao = dispatchManagerDao;
	}


	public void setLogManager(LogManagerI logManager) {
		this.logManager = logManager;
	}


	public void before(Method arg1, Object[] arg2, Object arg3) throws Throwable 
	{				
		
		if(arg2!=null&&arg2.length>0)
		{
			if(arg2[0] instanceof Plan)
			{
				Plan newPlan=(Plan)arg2[0];
				Plan oldPlan=dispatchManagerDao.getPlan(newPlan.getPlanId());
				if(oldPlan!=null)
				{					
					LogComparator c=new PlanComparator();
					c.compare(oldPlan, newPlan);
					List updates=c.getUpdateFields();
					if(updates!=null)
					for(int i=0,n=updates.size();i<n;i++)
					{
						logManager.log(newPlan.getUserId(),1,updates.get(i));
					}
					((PlanComparator) c).compareResource(oldPlan.getPlanResources(),newPlan.getPlanResources());
					
					updates=c.getUpdateFields();
					if(updates!=null)
					for(int i=0,n=updates.size();i<n;i++)
					{
						logManager.log(newPlan.getUserId(),2,updates.get(i));
					}
				}
			}else if(arg2[0] instanceof Dispatch)
			{
				Dispatch newDispatch=(Dispatch)arg2[0];
				Dispatch oldDispatch=dispatchManagerDao.getDispatch(newDispatch.getDispatchId());
				if(oldDispatch!=null)
				{
					int type=1;
					LogComparator c=new DispatchComparator();
					c.compare(oldDispatch, newDispatch);
					List updates=c.getUpdateFields();
					if(updates!=null)
					for(int i=0,n=updates.size();i<n;i++)
					{
						logManager.log(newDispatch.getUserId(),3,updates.get(i));
					}
					((DispatchComparator) c).compareResource(oldDispatch.getDispatchResources(),newDispatch.getDispatchResources());
					
					updates=c.getUpdateFields();
					if(updates!=null)
					for(int i=0,n=updates.size();i<n;i++)
					{
						logManager.log(newDispatch.getUserId(),4,updates.get(i));
					}
				}
			}
		}
		
	}
	
	


	

	
}

abstract class LogComparator implements Comparator
{
	protected List updates=null;
    protected int result;
	protected String id;
	protected SimpleDateFormat sf=new SimpleDateFormat("hh:mm aaa");
	protected SimpleDateFormat sf1=new SimpleDateFormat("MM/dd/yyyy");
	
    public List getUpdateFields() {
		// TODO Auto-generated method stub
		return updates;
	}
    public Object compareValueswithNull(String name,Object field1,Object field2)
    {
    	if(field1==null)field1="";
    	if(field2==null)field2="";
    	return compareValues(name,field1,field2);
    }
	//public abstract List getPlanUpdateFields();
	public Object compareValues(String name,Object field1,Object field2)
	{
		if(field1==null&&field2==null) return null;
		if(field1==null&&field2!=null){ result++; return new Object[]{id,name,field1,field2};}
		if(!field1.equals(field2))
		{
			 if(field1 instanceof Comparable) 
			 {
				 result+=((Comparable) field1).compareTo(field2);
			 }			 
			 return new Object[]{id,name,field1,field2};
		}
		return null;
	}
	public void reset()
	{
		result=0;
		updates=new ArrayList();
	}
}

class PlanComparator extends LogComparator
{	
	
	public int compare(Object o1, Object o2) 
	{
		Plan oldPlan=(Plan)o1;
		Plan newPlan=(Plan)o2;
		id=newPlan.getPlanId();
		if(oldPlan!=null&&newPlan!=null)
		{
			updates=new ArrayList();
			Object obj;
			if(( obj=compareValues("PLAN_DATE",getTimeOnly(oldPlan.getPlanDate(),sf1),getTimeOnly(newPlan.getPlanDate(),sf1)))!=null)
			{
				updates.add(obj);
			}
			if(( obj=compareValues("ZONE",oldPlan.getZoneCode(),newPlan.getZoneCode()))!=null)
			{
				updates.add(obj);
			}
			if(( obj=compareValues("REGION",oldPlan.getRegion().getCode(),newPlan.getRegion().getCode()))!=null)
			{
				updates.add(obj);
			}
			oldPlan.setFirstDeliveryTime(new Date(oldPlan.getFirstDeliveryTime().getTime()));			
			if(( obj=compareValues("FIRST_DLV_TIME",getTimeOnly(oldPlan.getFirstDeliveryTime(),sf),getTimeOnly(newPlan.getFirstDeliveryTime(),sf)))!=null)
			{
				updates.add(obj);
			}
			
			oldPlan.setStartTime(new Date(oldPlan.getStartTime().getTime()));
			if(( obj=compareValues("START_TIME",getTimeOnly(oldPlan.getStartTime(),sf),getTimeOnly(newPlan.getStartTime(),sf)))!=null)
			{
				updates.add(obj);
			}
			if(( obj=compareValues("SEQUENCE",""+oldPlan.getSequence(),""+newPlan.getSequence()))!=null)
			{
				updates.add(obj);
			}
			if(( obj=compareValues("IS_BULLPEN",oldPlan.getIsBullpen(),newPlan.getIsBullpen()))!=null)
			{
				updates.add(obj);
			}
			if(( obj=compareValues("SUPERVISOR_ID",oldPlan.getSupervisorId(),newPlan.getSupervisorId()))!=null)
			{
				updates.add(obj);
			}
		}
		
		return result;
	}
	
	public String getTimeOnly(Date date,SimpleDateFormat s)
	{
		try 
		{			
			return s.format(date);
		} catch (RuntimeException e) 
		{
			
		}
		return "";
	}
	
	public int compareResource(Set oldResources,Set newResources)
	{
		reset();		
		Set oldResourcesCopy=new HashSet();
		oldResourcesCopy.addAll(oldResources);
		
		Set newResourcesCopy=new HashSet();
		newResourcesCopy.addAll(newResources);
		if(newResourcesCopy!=null)
		{
			Iterator iterator=newResourcesCopy.iterator();
			while(iterator.hasNext())
			{
				PlanResource newPlanResource=(PlanResource)iterator.next();
				Iterator iteratorOld=oldResourcesCopy.iterator();
				while(iteratorOld.hasNext())
				{
					PlanResource oldPlanResource=(PlanResource)iteratorOld.next();
					if(newPlanResource.getId().getResourceId().equals(oldPlanResource.getId().getResourceId()))
					{
						iteratorOld.remove();
						iterator.remove();
						break;
					}
				}
			}
		}
		
		Iterator iterator=newResourcesCopy.iterator();
		while(iterator.hasNext())
		{
			PlanResource newPlanResource=(PlanResource)iterator.next();
			updates.add(new Object[]{id,"RESOURCE_ID","",newPlanResource.getId().getResourceId()});
			updates.add(new Object[]{id,"ROLE","",newPlanResource.getEmployeeRoleType().getCode()});
		}
		
		Iterator iteratorOld=oldResourcesCopy.iterator();
		while(iteratorOld.hasNext())
		{
			PlanResource newPlanResource=(PlanResource)iteratorOld.next();
			updates.add(new Object[]{id,"RESOURCE_ID",newPlanResource.getId().getResourceId(),""});
			updates.add(new Object[]{id,"ROLE",newPlanResource.getEmployeeRoleType().getCode(),""});
		}
		return result;
	}
	
}

class DispatchComparator extends LogComparator
{	
	
	public int compare(Object o1, Object o2) 
	{
		Dispatch oldDispatch=(Dispatch)o1;
		Dispatch newDispatch=(Dispatch)o2;
		id=newDispatch.getPlanId();
		if(oldDispatch!=null&&newDispatch!=null)
		{
			updates=new ArrayList();
			Object obj;
			if(( obj=compareValues("DISPATCH_DATE",getTimeOnly(oldDispatch.getDispatchDate(),sf1),getTimeOnly(newDispatch.getDispatchDate(),sf1)))!=null)
			{
				updates.add(obj);
			}
			String o="";
			String n="";
			if(oldDispatch.getZone()!=null)o=oldDispatch.getZone().getZoneCode();
			if(newDispatch.getZone()!=null)n=newDispatch.getZone().getZoneCode();
			if(( obj=compareValues("ZONE",o,n))!=null)
			{
				updates.add(obj);
			}
			if(( obj=compareValues("SUPERVISOR_ID",oldDispatch.getSupervisorId(),newDispatch.getSupervisorId()))!=null)
			{
				updates.add(obj);
			}
			if(( obj=compareValues("ROUTE",oldDispatch.getRoute(),newDispatch.getRoute()))!=null)
			{
				updates.add(obj);
			}
			if(( obj=compareValues("TRUCK",oldDispatch.getTruck(),newDispatch.getTruck()))!=null)
			{
				updates.add(obj);
			}
			oldDispatch.setFirstDlvTime(new Date(oldDispatch.getFirstDlvTime().getTime()));			
			if(( obj=compareValues("FIRST_DLV_TIME",getTimeOnly(oldDispatch.getFirstDlvTime(),sf),getTimeOnly(newDispatch.getFirstDlvTime(),sf)))!=null)
			{
				updates.add(obj);
			}
			
			oldDispatch.setStartTime(new Date(oldDispatch.getStartTime().getTime()));
			if(( obj=compareValues("START_TIME",getTimeOnly(oldDispatch.getStartTime(),sf),getTimeOnly(newDispatch.getStartTime(),sf)))!=null)
			{
				updates.add(obj);
			}
			if(( obj=compareValues("CONFIRMED",oldDispatch.getConfirmed(),newDispatch.getConfirmed()))!=null)
			{
				updates.add(obj);
			}
			String oldCode="";
			String newCode="";
			if(oldDispatch.getDispositionType()!=null)oldCode=oldDispatch.getDispositionType().getCode();
			if(newDispatch.getDispositionType()!=null)newCode=newDispatch.getDispositionType().getCode();
			if(( obj=compareValues("DISPOSITION",oldCode,newCode))!=null)
			{
				updates.add(obj);
			}
			if(( obj=compareValues("PLAN_ID",oldDispatch.getPlanId(),newDispatch.getPlanId()))!=null)
			{
				updates.add(obj);
			}
			o="";
			n="";
			if(oldDispatch.getComments()!=null)o=oldDispatch.getComments();
			if(newDispatch.getComments()!=null)n=newDispatch.getComments();
			if(( obj=compareValues("COMMENTS",o,n))!=null)
			{
				updates.add(obj);
			}			
			if(( obj=compareValues("IS_BULLPEN",oldDispatch.getBullPen(),newDispatch.getBullPen()))!=null)
			{
				updates.add(obj);
			}
			if(( obj=compareValues("REGION",oldDispatch.getRegion().getCode(),newDispatch.getRegion().getCode()))!=null)
			{
				updates.add(obj);
			}
			if(( obj=compareValueswithNull("GPS_NO",oldDispatch.getGpsNumber(),newDispatch.getGpsNumber()))!=null)
			{
				updates.add(obj);
			}
			if(( obj=compareValueswithNull("EZPASS_NO",oldDispatch.getEzpassNumber(),newDispatch.getEzpassNumber()))!=null)
			{
				updates.add(obj);
			}
			if(( obj=compareValueswithNull("DISPATCH_TIME",getTimeOnly(oldDispatch.getDispatchTime(),sf),getTimeOnly(newDispatch.getDispatchTime(),sf)))!=null)
			{
				updates.add(obj);
			}
			if(( obj=compareValueswithNull("PHONE_ASSIGNED",oldDispatch.getPhonesAssigned(),newDispatch.getPhonesAssigned()))!=null)
			{
				updates.add(obj);
			}
			if(( obj=compareValueswithNull("KEYS_READY",oldDispatch.getKeysReady(),newDispatch.getKeysReady()))!=null)
			{
				updates.add(obj);
			}
			if(( obj=compareValueswithNull("CHECKEDIN_TIME",getTimeOnly(oldDispatch.getCheckedInTime(),sf),getTimeOnly(newDispatch.getCheckedInTime(),sf)))!=null)
			{
				updates.add(obj);
			}
		}
		
		return result;
	}
	
	public String getTimeOnly(Date date,SimpleDateFormat s)
	{
		try 
		{			
			return s.format(date);
		} catch (RuntimeException e) 
		{
			
		}
		return "";
	}
	
	public int compareResource(Set oldResources,Set newResources)
	{
		reset();		
		Set oldResourcesCopy=new HashSet();
		oldResourcesCopy.addAll(oldResources);
		
		Set newResourcesCopy=new HashSet();
		newResourcesCopy.addAll(newResources);
		if(newResourcesCopy!=null)
		{
			Iterator iterator=newResourcesCopy.iterator();
			while(iterator.hasNext())
			{
				DispatchResource newPlanResource=(DispatchResource)iterator.next();
				Iterator iteratorOld=oldResourcesCopy.iterator();
				while(iteratorOld.hasNext())
				{
					DispatchResource oldPlanResource=(DispatchResource)iteratorOld.next();
					if(newPlanResource.getId().getResourceId().equals(oldPlanResource.getId().getResourceId()))
					{
						iteratorOld.remove();
						iterator.remove();
						break;
					}
				}
			}
		}
		
		Iterator iterator=newResourcesCopy.iterator();
		while(iterator.hasNext())
		{
			DispatchResource newPlanResource=(DispatchResource)iterator.next();
			updates.add(new Object[]{id,"RESOURCE_ID","",newPlanResource.getId().getResourceId()});
			updates.add(new Object[]{id,"ROLE","",newPlanResource.getEmployeeRoleType().getCode()});
			if(newPlanResource.getNextTelNo()!=null&&newPlanResource.getNextTelNo().trim().length()>0)
			updates.add(new Object[]{id,"NEXTEL_NO","",newPlanResource.getNextTelNo()});
		}
		
		Iterator iteratorOld=oldResourcesCopy.iterator();
		while(iteratorOld.hasNext())
		{
			DispatchResource newPlanResource=(DispatchResource)iteratorOld.next();
			updates.add(new Object[]{id,"RESOURCE_ID",newPlanResource.getId().getResourceId(),""});
			updates.add(new Object[]{id,"ROLE",newPlanResource.getEmployeeRoleType().getCode(),""});
			if(newPlanResource.getNextTelNo()!=null&&newPlanResource.getNextTelNo().trim().length()>0)
			updates.add(new Object[]{id,"NEXTEL_NO","",newPlanResource.getNextTelNo()});
		}
		return result;
	}
	
}