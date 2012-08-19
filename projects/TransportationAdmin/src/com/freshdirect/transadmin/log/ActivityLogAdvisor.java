package com.freshdirect.transadmin.log;

import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
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
import com.freshdirect.transadmin.util.DispatchPlanUtil;
import com.freshdirect.transadmin.util.TransStringUtil;
/**
 * type '1'  Plan delete
 * type '2'  Plan Resource Change
 * type '3'  Dispatch delete
 * type '4'  Any Dispatch Resource change
 * 
 * type '5'  AM Dispatch Resource Change 
 * type '6'  PM Dispatch Resource Change 
 * type '7'  AM Dispatch Resource Change Out of Region
 * type '8'  PM Dispatch Resource Change Out of Region 
 */
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


	public void before(Method arg1, Object[] arg2, Object arg3)
	{	
		try {
			if(arg2!=null&&arg2.length>0)
			{
				if(arg1!=null&&arg1.getName().equalsIgnoreCase("removeEntity"))
				{
					delete(arg1,arg2,arg3);
				}
				else
				{
					if(arg2[0] instanceof Collection)
					{
						Collection c=(Collection)arg2[0];
						Iterator iterator=c.iterator();
						while(iterator.hasNext())
						{
							Object newObj=iterator.next();
							if(newObj instanceof Dispatch)
							{
								Object[] argTemp2=new Object[]{newObj};
								process(arg1,argTemp2,arg3);
							}
						}
					}
					else
					{
						process(arg1,arg2,arg3);
					}
				}
			}
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void delete(Method arg1, Object[] arg2, Object arg3) throws Throwable 
	{
		if(arg2[0] instanceof Collection)
		{
			Collection c=(Collection)arg2[0];
			Iterator iterator=c.iterator();
			while(iterator.hasNext())
			{
				Object newObj=iterator.next();
				if(newObj instanceof Dispatch)
				{
					Dispatch d=(Dispatch)newObj;
					Object[] param=new Object[]{d.getDispatchId(),"DELETED","",""};
					logManager.log(d.getUserId(),3,param);
				}else if(newObj instanceof Plan)
				{
					Plan p=(Plan)newObj;
					Object[] param=new Object[]{p.getPlanId(),"DELETED","",""};
					logManager.log(p.getUserId(),1,param);
				}
					
			}
		}
	}
	public void process(Method arg1, Object[] arg2, Object arg3) throws Throwable 
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
			} else if(arg2[0] instanceof Dispatch)
			{
				Dispatch newDispatch = (Dispatch)arg2[0];
				Dispatch oldDispatch = dispatchManagerDao.getDispatch(newDispatch.getDispatchId());
				dispatchManagerDao.evictDispatch(oldDispatch);
				
				Set<String> inRegionAMResourceChanges = new HashSet<String>();
				Set<String> inRegionPMResourceChanges = new HashSet<String>();
				
				Set<String> resourceAMChanges = new HashSet<String>();
				Set<String> resourcePMChanges = new HashSet<String>();
				
				Set newResourcesCopy=new HashSet();
				newResourcesCopy.addAll(newDispatch.getDispatchResources());
				
				if(oldDispatch!=null) {
					
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
				
				if(newResourcesCopy != null && newResourcesCopy.size() > 0)	{
					Iterator iterator=newResourcesCopy.iterator();
					while(iterator.hasNext()) {
						DispatchResource newPlanResource =(DispatchResource)iterator.next();
						Collection dispatchInfos = new ArrayList();
						dispatchInfos = dispatchManagerDao
								.getDispatchForResource(TransStringUtil.getServerDate(newDispatch.getDispatchDate()),
																								newPlanResource.getId().getResourceId());
						if(dispatchInfos != null) {
							for (Iterator<Dispatch> itr = dispatchInfos.iterator(); itr.hasNext();) {
								Dispatch _resDispatch =  itr.next();
								dispatchManagerDao.evictDispatch(_resDispatch);
								if(_resDispatch.getRegion() != null && newDispatch.getRegion() != null
												&& !_resDispatch.getRegion().getCode().equalsIgnoreCase(newDispatch.getRegion().getCode())) {
									if("AM".equals(DispatchPlanUtil.getShift(newDispatch.getDispatchDate(), newDispatch.getFirstDlvTime()))){
										inRegionAMResourceChanges.add(newPlanResource.getId().getResourceId());
									}else {
										inRegionPMResourceChanges.add(newPlanResource.getId().getResourceId());
									}
								}
								if(newDispatch.getDispatchId() == null 
										|| !newDispatch.getDispatchId().equals(_resDispatch.getDispatchId())) {
									if("AM".equals(DispatchPlanUtil.getShift(newDispatch.getDispatchDate(), newDispatch.getFirstDlvTime()))){
										resourceAMChanges.add(newPlanResource.getId().getResourceId());
									}else {
										resourcePMChanges.add(newPlanResource.getId().getResourceId());
									}
								}
							}	
						}
					}
					
					this.logTeamChanges(newDispatch.getUserId(), 5, newDispatch.getDispatchId(), resourceAMChanges);
					this.logTeamChanges(newDispatch.getUserId(), 6, newDispatch.getDispatchId(), resourcePMChanges);
					this.logTeamChanges(newDispatch.getUserId(), 7, newDispatch.getDispatchId(), inRegionAMResourceChanges);
					this.logTeamChanges(newDispatch.getUserId(), 8, newDispatch.getDispatchId(), inRegionPMResourceChanges);
					
				}
		}		
	}
}

	private void logTeamChanges(String userId, int type, String dispatchId, Set<String> resources) {
		
		if(resources != null) {
			for(String resource : resources) {
				logManager.log(userId, type, new Object[]{dispatchId == null ? "T"+resource : dispatchId, "RESOURCE_ID", "", resource});
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
    	return compareValues(name,field1.toString(),field2.toString());
    }
	//public abstract List getPlanUpdateFields();
	public Object compareValues(String name, Object field1, Object field2) {
		if (field1 == null && field2 == null)
			return null;
		if (field1 == null && field2 != null) {
			result++;
			return new Object[] { id, name, field1, field2 };
		}
		if (!field1.equals(field2)) {
			if (field1 instanceof Comparable) {
				result += ((Comparable) field1).compareTo(field2);
			}
			return new Object[] { id, name, field1, field2 };
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
			if(oldPlan.getOriginFacility()!=null && newPlan.getOriginFacility()!=null &&
					( obj=compareValues("ORIGIN_FACILITY",oldPlan.getOriginFacility().getFacilityId(),newPlan.getOriginFacility().getFacilityId()))!=null)
			{
				updates.add(obj);
			}
			if(oldPlan.getDestinationFacility()!=null && newPlan.getDestinationFacility()!=null &&
					( obj=compareValues("DESTINATION_FACILITY",oldPlan.getDestinationFacility().getFacilityId(),newPlan.getDestinationFacility().getFacilityId()))!=null)
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
			//oldPlan.setFirstDeliveryTime(new Date(oldPlan.getFirstDeliveryTime().getTime()));			
			if(( obj=compareValues("FIRST_DLV_TIME",getTimeOnly(new Date(oldPlan.getFirstDeliveryTime().getTime()),sf),getTimeOnly(newPlan.getFirstDeliveryTime(),sf)))!=null)
			{
				updates.add(obj);
			}
			
			//oldPlan.setStartTime(new Date(oldPlan.getStartTime().getTime()));
			if(( obj=compareValues("START_TIME",getTimeOnly(new Date(oldPlan.getStartTime().getTime()),sf),getTimeOnly(newPlan.getStartTime(),sf)))!=null)
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
		id=newDispatch.getDispatchId();
		if(oldDispatch!=null&&newDispatch!=null)
		{
			updates=new ArrayList();
			Object obj;
			if(( obj=compareValues("DISPATCH_DATE",getTimeOnly(oldDispatch.getDispatchDate(),sf1),getTimeOnly(newDispatch.getDispatchDate(),sf1)))!=null)
			{
				updates.add(obj);
			}
			if(oldDispatch.getOriginFacility()!=null && newDispatch.getOriginFacility()!=null && 
					( obj=compareValues("ORIGIN_FACILITY",oldDispatch.getOriginFacility().getFacilityId(),newDispatch.getOriginFacility().getFacilityId()))!=null)
			{
				updates.add(obj);
			}
			if(oldDispatch.getDestinationFacility()!=null && newDispatch.getDestinationFacility()!=null && 
					( obj=compareValues("DESTINATION_FACILITY",oldDispatch.getDestinationFacility().getFacilityId(),newDispatch.getDestinationFacility().getFacilityId()))!=null)
			{
				updates.add(obj);
			}
			String o = "";
			String n = "";
			if (oldDispatch.getZone() != null)
				o = oldDispatch.getZone().getZoneCode();
			if (newDispatch.getZone() != null)
				n = newDispatch.getZone().getZoneCode();
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
			//oldDispatch.setFirstDlvTime(new Date(oldDispatch.getFirstDlvTime().getTime()));			
			if(( obj=compareValues("FIRST_DLV_TIME",getTimeOnly(new Date(oldDispatch.getFirstDlvTime().getTime()),sf),getTimeOnly(newDispatch.getFirstDlvTime(),sf)))!=null)
			{
				updates.add(obj);
			}
			
			//oldDispatch.setStartTime(new Date(oldDispatch.getStartTime().getTime()));
			if(( obj=compareValues("START_TIME",getTimeOnly(new Date(oldDispatch.getStartTime().getTime()),sf),getTimeOnly(newDispatch.getStartTime(),sf)))!=null)
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
			if(( obj=compareValueswithNull("PLAN_ID",oldDispatch.getPlanId(),newDispatch.getPlanId()))!=null)
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
			if(( obj=compareValueswithNull("ISOVERRIDE",oldDispatch.getIsOverride(),newDispatch.getIsOverride()))!=null)
			{
				updates.add(obj);
			}
			if(( obj=compareValueswithNull("OVERRIDE_REASON_ID",oldDispatch.getOverrideReason(),newDispatch.getOverrideReason()))!=null)
			{
				updates.add(obj);
			}
			if(( obj=compareValueswithNull("OVERRIDE_USER",oldDispatch.getOverrideUser(),newDispatch.getOverrideUser()))!=null)
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