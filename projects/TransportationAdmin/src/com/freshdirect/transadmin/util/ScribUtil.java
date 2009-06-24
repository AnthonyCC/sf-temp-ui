package com.freshdirect.transadmin.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import com.freshdirect.transadmin.model.Scrib;
import com.freshdirect.transadmin.model.ScribEmployee;

public class ScribUtil 
{
	public static Collection getPlans(Scrib scrib, Collection allEmployees)
	{
		return null;
	}
	
	public static Collection getEmployees(Scrib scrib, Collection employees)
	{
		Collection result=new ArrayList();
		if(employees!=null)
		{
			Iterator employeeIterator=employees.iterator();
			while(employeeIterator.hasNext())
			{
				ScribEmployee employee=(ScribEmployee)employeeIterator.next();
				if(employee.getScheduleDate().equals(scrib.getScribDate())&&employee.getRegion().equals(scrib.getRegion().getCode())&&employee.getStartTime().equals(scrib.getStartTime()))
				{
					result.add(employee);
				}
			}
		}
		return result;
	}
}
