package com.freshdirect.transadmin.web.util;

import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import com.freshdirect.transadmin.model.UserPref;
import com.freshdirect.transadmin.service.DispatchManagerI;

public class TransWebUtil 
{
	public static ThreadLocal httpRequest = new ThreadLocal();
	
	private static final String USER_PREF="userPref";
	
	public static boolean isPunch(DispatchManagerI service)
	{
		return isTrue(getUserPref((HttpServletRequest)httpRequest.get(),"punch",service));
	}
	
	public static boolean isPunch(HttpServletRequest request,DispatchManagerI service)
	{
		return isTrue(getUserPref(request,"punch",service));
	}
	
	public static boolean isAirClick(HttpServletRequest request,DispatchManagerI service)
	{
		return isTrue(getUserPref(request,"airclick",service));
	}
	public static boolean isTrue(String key)
	{
		if("true".equalsIgnoreCase(key)||"yes".equalsIgnoreCase(key)||"1".equalsIgnoreCase(key)||"on".equalsIgnoreCase(key))
		{
			return true;
		}
		return false;
	}
	public static String getUserPref(HttpServletRequest request,String key,DispatchManagerI service)
	{
		if(request.getSession().getAttribute(USER_PREF)==null)
		{
			String userId=com.freshdirect.transadmin.security.SecurityManager.getUserName(request);
			request.getSession().setAttribute(USER_PREF,service.getUserPref(userId));
		}
		return getUserPref(request,key);
	}
	
	public static String getUserPref(HttpServletRequest request,String key)
	{
		Collection c=(Collection)request.getSession().getAttribute(USER_PREF);
		return getUserPref(c,key);
	}
	
	public static String getUserPref(Collection c,String key)
	{		
		if(c==null) return null;
		Iterator i=c.iterator();
		while(i.hasNext())
		{
			UserPref p=(UserPref)i.next();
			if(key.equalsIgnoreCase(p.getKey())) return p.getValue();
		}
		return null;
	}
	
	public static void savePref(HttpServletRequest request,Collection c)
	{
		request.getSession().setAttribute(USER_PREF,c);
	}
	
	public static void updatePref(HttpServletRequest request,UserPref pref)
	{
		Collection c=(Collection)request.getSession().getAttribute(USER_PREF);
		if(c==null) return ;
		Iterator i=c.iterator();
		boolean updated=false;
		while(i.hasNext())
		{
			UserPref p=(UserPref)i.next();
			if(pref.getKey().equalsIgnoreCase(p.getKey()))
			{
				p.setValue(pref.getValue());
				updated=true;
			}
		}
		if(!updated) c.add(pref);
	}
	public static boolean isUserPref(HttpServletRequest request)
	{
		Collection c=(Collection)request.getSession().getAttribute(USER_PREF);
		if(c==null) return false;
		return true;
	}
}
