package com.freshdirect.transadmin.util.scrib;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.freshdirect.transadmin.model.EmployeeRole;
import com.freshdirect.transadmin.model.EmployeeRoleType;
import com.freshdirect.transadmin.model.Plan;
import com.freshdirect.transadmin.model.PlanResource;
import com.freshdirect.transadmin.model.ResourceId;
import com.freshdirect.transadmin.model.ScheduleEmployeeInfo;
import com.freshdirect.transadmin.model.Scrib;
import com.freshdirect.transadmin.model.ZonetypeResource;

public class PlanTree 
{
	Map d=new HashMap();
	
	public void prepare(Collection scribs)
	{
		for(Iterator i=scribs.iterator();i.hasNext(); )
		{
			Object next=i.next();
			Object key;
			if(next instanceof Scrib)
			{
				Scrib s=(Scrib)next;
				key=s.getScribDate();
				DateNode value=(DateNode)d.get(key);
				if(value==null)
				{
					value=new DateNode();
					d.put(key, value);
				}
				value.prepare(s);
			}
			else if(next instanceof SchdeuleEmployeeDetails)
			{
				SchdeuleEmployeeDetails s=(SchdeuleEmployeeDetails)next;
				key=s.getDate();
				DateNode value=(DateNode)d.get(key);
				if(value==null)
				{
					value=new DateNode();
					d.put(key, value);
				}
				value.prepare(s);
			}
				
			
		}
	}
	
	public Collection getPlan()
	{
		Collection result=new ArrayList();
		for(Iterator i=d.values().iterator();i.hasNext();)
		{
			DateNode value=(DateNode)i.next();			
			result.addAll(value.getPlan());
		}
		return result;
	}
}


class DateNode
{
	Date date;
	Map regions=new HashMap();
	Map depot=new HashMap();
	public void prepare(Scrib s)
	{
		String key=s.getRegion().getCode();
		if(ScheduleEmployeeInfo.DEPOT.equalsIgnoreCase(key))
		{
			DepotNode value=(DepotNode)depot.get(key);
			if(value==null)
			{
				value=new DepotNode();
				depot.put(key, value);
			}
			value.prepare(s);
		}else
		{
			RegionNode value=(RegionNode)regions.get(key);
			if(value==null)
			{
				value=new RegionNode();
				regions.put(key, value);
			}
			value.prepare(s);
		}
	}
	
	
	public void prepare(SchdeuleEmployeeDetails s)
	{
		String key=s.getSchedule().getRegion().getCode();		
		if(ScheduleEmployeeInfo.DEPOT.equalsIgnoreCase(key))
		{
			DepotNode value=(DepotNode)depot.get(key);
			if(value==null)
			{
				value=new DepotNode();
				depot.put(key, value);
			}
			value.prepare(s);
		}else
		{
			RegionNode value=(RegionNode)regions.get(key);
			if(value==null)
			{
				value=new RegionNode();
				regions.put(key, value);
			}
			value.prepare(s);
		}
	}
	public Collection getPlan()
	{
		Collection result=new ArrayList();
		for(Iterator i=regions.values().iterator();i.hasNext();)
		{
			RegionNode value=(RegionNode)i.next();			
			result.addAll(value.getPlan());
		}
		for(Iterator i=depot.values().iterator();i.hasNext();)
		{
			DepotNode value=(DepotNode)i.next();			
			result.addAll(value.getPlan());
		}
		return result;
	}
}

class RegionNode
{
	Map times=new HashMap();
	public void prepare(Scrib s)
	{
		Date key=s.getStartTime();		
		TimeNode value=(TimeNode)times.get(key);
		if(value==null)
		{
			value=new TimeNode();
			times.put(key, value);
		}
		value.prepare(s);
		
	}
	
	public void prepare(SchdeuleEmployeeDetails s)
	{
		Date key=s.getSchedule().getTime();		
		TimeNode value=(TimeNode)times.get(key);
		if(value==null)
		{
			value=new TimeNode();
			times.put(key, value);
		}
		value.prepare(s);
		
	}
	
	public Collection getPlan()
	{
		Collection result=new ArrayList();
		for(Iterator i=times.values().iterator();i.hasNext();)
		{
			TimeNode value=(TimeNode)i.next();			
			result.addAll(value.getPlan());
		}
		return result;
	}
}

class DepotNode
{
	Map zones=new HashMap();
	public void prepare(Scrib s)
	{
		String key=s.getZone().getZoneCode();		
		ZoneNode value=(ZoneNode)zones.get(key);
		if(value==null)
		{
			value=new ZoneNode();
			zones.put(key, value);
		}
		value.prepare(s);
	}
	public void prepare(SchdeuleEmployeeDetails s)
	{
		String key=s.getSchedule().getDepotZone().getZoneCode();		
		ZoneNode value=(ZoneNode)zones.get(key);
		if(value==null)
		{
			value=new ZoneNode();
			zones.put(key, value);
		}
		value.prepare(s);
	}
	public Collection getPlan()
	{
		Collection result=new ArrayList();
		for(Iterator i=zones.values().iterator();i.hasNext();)
		{
			ZoneNode value=(ZoneNode)i.next();			
			result.addAll(value.getPlan());
		}
		return result;
	}
}

class ZoneNode
{
	Map times=new HashMap();
	Map runners=new HashMap();
	public void prepare(Scrib s)
	{
		Date key=s.getStartTime();		
		DepotTimeNode value=(DepotTimeNode)times.get(key);
		if(value==null)
		{
			value=new DepotTimeNode(this);
			times.put(key, value);
		}
		value.prepare(s);
	}
	public void prepare(SchdeuleEmployeeDetails s)
	{
		if(TimeNode.isRole(ScheduleEmployeeInfo.RUNNER,s.getEmpRoles()))
		{
			Date key=s.getSchedule().getTime();			
			List value=(List)runners.get(key);
			if(value==null)
			{
				value=new ArrayList();
				runners.put(key, value);
			}
			value.add(s);
		}
		else
		{
			Date key=s.getSchedule().getTime();			
			DepotTimeNode value=(DepotTimeNode)times.get(key);
			if(value==null)
			{
				value=new DepotTimeNode(this);
				times.put(key, value);
			}
			value.prepare(s);
		}
	}
	
	public Collection getPlan()
	{
		Collection result=new ArrayList();
		for(Iterator i=times.values().iterator();i.hasNext();)
		{
			DepotTimeNode value=(DepotTimeNode)i.next();			
			result.addAll(value.getPlan());
		}
		return result;
	}
}

class TimeNode
{
	List trucks=new ArrayList();
	List employees=new ArrayList();
	List plans=new ArrayList();
	
	public Collection getPlan()
	{
		Collections.sort(trucks, new ZoneComparator());
		Collections.sort(employees, new HireDateComparator());
		assemble();
		return plans;
	}
	
	public void prepare(Scrib s)
	{
		int n=s.getCount();		
		for(int i=0;i<n;i++)
		{
			TruckNode t=new TruckNode();
			trucks.add(t);
			t.prepare(s);
		}
	}
	public void prepare(SchdeuleEmployeeDetails s)
	{
		employees.add(s);
	}
	public void assemble()
	{
		for(Iterator i=trucks.iterator();i.hasNext();)
		{
			Scrib s=((TruckNode)i.next()).s;
			
			Plan p=new Plan();
			plans.add(p);
			p.setPlanDate(s.getScribDate());
			p.setZone(s.getZone());
			p.setRegion(s.getRegion());
			p.setStartTime(s.getStartTime());
			p.setFirstDeliveryTime(s.getFirstDlvTime());
			p.setSupervisorId(s.getSupervisorCode());
			p.setMaxTime(s.getMaxTimeOrig());
			if(s.getZone().getTrnZoneType()==null)continue;
			Set resources=s.getZone().getTrnZoneType().getZonetypeResources();
			
			for(Iterator j=resources.iterator();j.hasNext();)
			{
				ZonetypeResource r=(ZonetypeResource)j.next();
				//Driver
				if(ScheduleEmployeeInfo.DRIVER.equalsIgnoreCase(r.getId().getRole()))
				{
					int min=r.getRequiredNo().intValue();
					int count=0;
					for(Iterator k=employees.iterator();k.hasNext();)
					{
						if(count>=min) break;
						SchdeuleEmployeeDetails se=(SchdeuleEmployeeDetails)k.next();						
						Collection c=se.getEmpRoles();
						if(isRole(ScheduleEmployeeInfo.DRIVER,c))
						{
							PlanResource planResource=new PlanResource();
							EmployeeRoleType type=new EmployeeRoleType();
							type.setCode(ScheduleEmployeeInfo.DRIVER);
							ResourceId resource=new ResourceId();
							resource.setResourceId(se.info.getEmployeeId());
							planResource.setEmployeeRoleType(type);
							planResource.setId(resource);
							p.getPlanResources().add(planResource);
							k.remove();
							count++;
						}						
					}
				}
				
				//Helper
				if(ScheduleEmployeeInfo.HELPER.equalsIgnoreCase(r.getId().getRole()))
				{
					int min=r.getRequiredNo().intValue();
					int count=0;
					for(Iterator k=employees.iterator();k.hasNext();)
					{
						if(count>=min) break;
						SchdeuleEmployeeDetails se=(SchdeuleEmployeeDetails)k.next();						
						Collection c=se.getEmpRoles();
						if(isRole(ScheduleEmployeeInfo.HELPER,c))
						{
							PlanResource planResource=new PlanResource();
							EmployeeRoleType type=new EmployeeRoleType();
							type.setCode(ScheduleEmployeeInfo.HELPER);
							ResourceId resource=new ResourceId();
							resource.setResourceId(se.info.getEmployeeId());
							planResource.setEmployeeRoleType(type);
							planResource.setId(resource);
							p.getPlanResources().add(planResource);
							k.remove();
							count++;
						}					
					}
				}
				
				//Runner
				if(ScheduleEmployeeInfo.RUNNER.equalsIgnoreCase(r.getId().getRole()))
				{
					int min=r.getRequiredNo().intValue();
					int count=0;
					for(Iterator k=employees.iterator();k.hasNext();)
					{
						if(count>=min) break;
						SchdeuleEmployeeDetails se=(SchdeuleEmployeeDetails)k.next();
						Collection c=se.getEmpRoles();
						if(isRole(ScheduleEmployeeInfo.RUNNER,c))
						{
							PlanResource planResource=new PlanResource();
							EmployeeRoleType type=new EmployeeRoleType();
							type.setCode(ScheduleEmployeeInfo.RUNNER);
							ResourceId resource=new ResourceId();
							resource.setResourceId(se.info.getEmployeeId());
							planResource.setEmployeeRoleType(type);
							planResource.setId(resource);
							p.getPlanResources().add(planResource);
							k.remove();
							count++;
						}
					}
				}				
			}
		}
		
		//add bullpen for remaining employees
		
			if(employees!=null&&employees.size()>0)
			{
				SchdeuleEmployeeDetails s=(SchdeuleEmployeeDetails)employees.get(0);
				Plan p=new Plan();
				plans.add(p);
				p.setPlanDate(s.getDate());
				
				p.setRegion(s.getSchedule().getRegion());
				p.setStartTime(s.getSchedule().getTime());
				p.setFirstDeliveryTime(s.getSchedule().getTime());
				p.setIsBullpen("Y");
				for(Iterator i=employees.iterator();i.hasNext();)
				{
					SchdeuleEmployeeDetails ss=(SchdeuleEmployeeDetails )i.next();					
					PlanResource planResource=new PlanResource();
					EmployeeRoleType type=new EmployeeRoleType();
					for( Iterator si=ss.getEmpRoles().iterator();si.hasNext();)
					{
						type.setCode(((EmployeeRole) (si.next())).getId().getRole()); break;
					}
					ResourceId resource=new ResourceId();
					resource.setResourceId(ss.info.getEmployeeId());
					planResource.setEmployeeRoleType(type);
					planResource.setId(resource);
					p.getPlanResources().add(planResource);
					
				}
			}
	}
	
	public static boolean isRole(String role,Collection c)
	{
		boolean result=false;
		for(Iterator k=c.iterator();k.hasNext();)
		{
			EmployeeRole e=(EmployeeRole)k.next();
			if(role.equalsIgnoreCase(e.getId().getRole()))
			{
				return true;
			}
		}
		return result;
	}
	
}

class DepotTimeNode
{
	List trucks=new ArrayList();
	List employees=new ArrayList();	
	List plans=new ArrayList();
	
	ZoneNode parent;
	
	public DepotTimeNode(ZoneNode parent)
	{
		this.parent=parent;
	}
	public Collection getPlan()
	{
		Collections.sort(trucks, new ZoneComparator());
		Collections.sort(employees, new HireDateComparator());
		assemble();
		return plans;
	}
	
	public void prepare(Scrib s)
	{
		int n=s.getCount();		
		for(int i=0;i<n;i++)
		{
			TruckNode t=new TruckNode();
			trucks.add(t);
			t.prepare(s);
		}
	}
	public void prepare(SchdeuleEmployeeDetails s)
	{
		employees.add(s);
	}
	public void assemble()
	{
		for(Iterator i=trucks.iterator();i.hasNext();)
		{
			Scrib s=((TruckNode)i.next()).s;
			
			Plan p=new Plan();
			plans.add(p);
			p.setPlanDate(s.getScribDate());
			p.setZone(s.getZone());
			p.setRegion(s.getRegion());
			p.setStartTime(s.getStartTime());
			p.setFirstDeliveryTime(s.getFirstDlvTime());
			p.setSupervisorId(s.getSupervisorCode());
			p.setMaxTime(s.getMaxTimeOrig());
			if(s.getZone().getTrnZoneType()==null)continue;
			Set resources=s.getZone().getTrnZoneType().getZonetypeResources();
			
			for(Iterator j=resources.iterator();j.hasNext();)
			{
				ZonetypeResource r=(ZonetypeResource)j.next();
				//Driver
				if(ScheduleEmployeeInfo.DRIVER.equalsIgnoreCase(r.getId().getRole()))
				{
					int min=r.getRequiredNo().intValue();
					int count=0;
					for(Iterator k=employees.iterator();k.hasNext();)
					{
						if(count>=min) break;
						SchdeuleEmployeeDetails se=(SchdeuleEmployeeDetails)k.next();
						Collection c=se.getEmpRoles();
						if(TimeNode.isRole(ScheduleEmployeeInfo.DRIVER,c))
						{
							PlanResource planResource=new PlanResource();
							EmployeeRoleType type=new EmployeeRoleType();
							type.setCode(ScheduleEmployeeInfo.DRIVER);
							ResourceId resource=new ResourceId();
							resource.setResourceId(se.info.getEmployeeId());
							planResource.setEmployeeRoleType(type);
							planResource.setId(resource);
							p.getPlanResources().add(planResource);
							k.remove();
							count++;
						}
					}
				}
				
				//Helper
				if(ScheduleEmployeeInfo.HELPER.equalsIgnoreCase(r.getId().getRole()))
				{
					int min=r.getRequiredNo().intValue();
					int count=0;
					for(Iterator k=employees.iterator();k.hasNext();)
					{
						if(count>=min) break;
						SchdeuleEmployeeDetails se=(SchdeuleEmployeeDetails)k.next();
						Collection c=se.getEmpRoles();
						if(TimeNode.isRole(ScheduleEmployeeInfo.HELPER,c))
						{
							PlanResource planResource=new PlanResource();
							EmployeeRoleType type=new EmployeeRoleType();
							type.setCode(ScheduleEmployeeInfo.HELPER);
							ResourceId resource=new ResourceId();
							resource.setResourceId(se.info.getEmployeeId());
							planResource.setEmployeeRoleType(type);
							planResource.setId(resource);
							p.getPlanResources().add(planResource);
							k.remove();
							count++;
						}
					}
				}								
			}
			//Runner[add all the runners available]
			List runners=getRunners(s.getFirstDlvTime());
			if(runners!=null)
			for(Iterator k=runners.iterator();k.hasNext();)
			{						
				SchdeuleEmployeeDetails se=(SchdeuleEmployeeDetails)k.next();						
				PlanResource planResource=new PlanResource();
				EmployeeRoleType type=new EmployeeRoleType();
				type.setCode(ScheduleEmployeeInfo.RUNNER);
				ResourceId resource=new ResourceId();
				resource.setResourceId(se.info.getEmployeeId());
				planResource.setEmployeeRoleType(type);
				planResource.setId(resource);
				p.getPlanResources().add(planResource);							
			}
		}
		
		//add bullpen for remaining employees
		
			if(employees!=null&&employees.size()>0)
			{
				SchdeuleEmployeeDetails s=(SchdeuleEmployeeDetails)employees.get(0);
				Plan p=new Plan();
				plans.add(p);
				p.setPlanDate(s.getDate());
				
				p.setRegion(s.getSchedule().getRegion());
				p.setStartTime(s.getSchedule().getTime());
				p.setFirstDeliveryTime(s.getSchedule().getTime());
				p.setIsBullpen("Y");
				for(Iterator i=employees.iterator();i.hasNext();)
				{
					SchdeuleEmployeeDetails ss=(SchdeuleEmployeeDetails )i.next();
					Collection c=ss.getEmpRoles();
					if(!TimeNode.isRole(ScheduleEmployeeInfo.RUNNER,c))
					{
					PlanResource planResource=new PlanResource();
					EmployeeRoleType type=new EmployeeRoleType();
					for( Iterator si=ss.getEmpRoles().iterator();si.hasNext();)
					{
						type.setCode(((EmployeeRole) (si.next())).getId().getRole()); break;
					}
					ResourceId resource=new ResourceId();
					resource.setResourceId(ss.info.getEmployeeId());
					planResource.setEmployeeRoleType(type);
					planResource.setId(resource);
					p.getPlanResources().add(planResource);
					}
				}
			}
	}
	

	public List getRunners(Date time)
	{
		Date key=null;		
		Map r=this.parent.runners;
		for(Iterator i=r.keySet().iterator();i.hasNext();)
		{
			Date temp=(Date)i.next();
			if(time.getTime()>=temp.getTime())
			{
				if(key!=null)
				{
					if(key.getTime()<temp.getTime())
					key=temp;
				}
				else
				{
					key=temp;
				}
			}
		}
		return (List)r.get(key);
		
	}
	
}

class TruckNode
{
	Scrib s;
	public void prepare(Scrib s)
	{
		this.s=s;
	}
}

class ZoneComparator implements Comparator
{

	public int compare(Object obj, Object obj1) 
	{
		if(obj instanceof Scrib &&obj1 instanceof Scrib)
		{
			Scrib s1=(Scrib)obj;
			Scrib s2=(Scrib)obj1;
			int z1=0;
			int z2=0;
			if(s1.getZone()!=null&&s1.getZone().getPriority()!=null) z1=s1.getZone().getPriority().intValue();
			if(s2.getZone()!=null&&s2.getZone().getPriority()!=null) z2=s2.getZone().getPriority().intValue();
			return z2-z1;
		}
		return 0;
	}
	
}

class HireDateComparator implements Comparator
{

	public int compare(Object obj, Object obj1) 
	{
		if(obj instanceof SchdeuleEmployeeDetails &&obj1 instanceof SchdeuleEmployeeDetails)
		{
			SchdeuleEmployeeDetails s1=(SchdeuleEmployeeDetails)obj;
			SchdeuleEmployeeDetails s2=(SchdeuleEmployeeDetails)obj1;
			long z1=0;
			long z2=0;
			if(s1.getInfo()!=null&&s1.getInfo().getHireDate()!=null) z1=s1.getInfo().getHireDate().getTime();
			if(s2.getInfo()!=null&&s2.getInfo().getHireDate()!=null) z2=s2.getInfo().getHireDate().getTime();
			int result=-1;
			if(z1>z2)result=1;
			return result;
		}
		return 0;
	}
	
}