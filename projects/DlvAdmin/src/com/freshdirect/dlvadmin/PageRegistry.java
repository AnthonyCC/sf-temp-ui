package com.freshdirect.dlvadmin;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

public class PageRegistry {

	public final static String GUEST = "Guest";
	public final static String ADMIN = "Admin";
	public final static String MARKETING = "Marketing";
	
	private final static Map PAGES = new HashMap();
	private final static Map NAV_GROUPS = new HashMap();

	static {
		addPage("Home", "Delivery admin", new String[] {ADMIN});
		addPage("AddPlan", "Delivery admin", new String[] {ADMIN});
		addPage("EditPlan", "Edit Plan", new String[] {ADMIN});
		addPage("CapacityReportPage", "Timeslot Capacity Report", new String[] {ADMIN});

		addPage("ShowCurrentTimeslots", "Current Timeslots", new String[] {ADMIN,GUEST,MARKETING});
		addPage("ViewTimeslots", "View Timeslots", new String[] {ADMIN,MARKETING});
		addPage("MapPage", "Current Map", new String[] {ADMIN});
		addPage("ManageResources", "Manage Resources", new String[] {ADMIN});
		addPage("CapacityDashboard", "Dashboard", new String[] {ADMIN,MARKETING});
		addPage("EarlyWarningPage", "Early Warning", new String[] {ADMIN,GUEST,MARKETING});
		addPage("ManageCTZones", "Manage Chef's Table Zones", new String[] {ADMIN,MARKETING});
		addPage("UserAudit", "User Audit", new String[] {ADMIN, MARKETING, GUEST});
		addPage("ManageUnattendedDeliveryZones","Manage Unattended Delivery Zones", new String[] {ADMIN,MARKETING,GUEST});
		
		addPage("region", "EditRegion", "Edit Region", new String[] {ADMIN});
		addPage("region", "AddPlan", "Add Plan", new String[] {ADMIN});
		
		addPage("depot/index.jsp", "Depot Admin", new String[] {ADMIN});
		
		NAV_GROUPS.put(null, new String[] {
			"Home", "ShowCurrentTimeslots", "ViewTimeslots", "ManageResources", "ManageCTZones", "CapacityReportPage", "CapacityDashboard", "EarlyWarningPage", "MapPage",
			"depot/index.jsp", "UserAudit", "ManageUnattendedDeliveryZones"
		});
		NAV_GROUPS.put("region", new String[] { "Home", "AddPlan" });
	}

	private PageRegistry() {
	}

	private static class PageDescription {
		public final String navGroup;
		public final String title;
		public final String[] rolesAllowed;

		public PageDescription(String navGroup, String title, String[] rolesAllowed) {
			this.navGroup = navGroup;
			this.title = title;
			this.rolesAllowed = rolesAllowed;
		}
	}

	public static String[] getNavGroup(String pageName) {
		String navGroup = get( pageName ).navGroup;
		return (String[])NAV_GROUPS.get( navGroup );
	}

	public static void addPage(String pageName, String title, String[] rolesAllowed) {
		addPage(null, pageName, title, rolesAllowed);
	}

	public static void addPage(String navGroup, String pageName, String title, String[] rolesAllowed) {
		PAGES.put(pageName, new PageDescription(navGroup, title, rolesAllowed));
	}

	private static PageDescription get(String pageName) {
		return (PageDescription) PAGES.get(pageName);
	}

	public static String getTitle(String pageName) {
		return get(pageName).title;
	}

	public static boolean isAccessable(String pageName, String role) {
        String[] roles = get(pageName).rolesAllowed;
        
        for (int i = 0; i<roles.length;i++)
        {
            if (roles[i].equals(role))
                return true;
        }
        return false;
	}
	
	public static boolean isUserAdmin(ServletRequest request) {
		return isUserInRole(PageRegistry.ADMIN, request);
	}
	
	private static boolean isUserInRole(String role, ServletRequest request) {
		return ((HttpServletRequest)request).isUserInRole(role);
	}

	public static boolean isUserGuest(ServletRequest request) {
		return isUserInRole(PageRegistry.GUEST, request);
	}
	public static boolean isUserMarketing(ServletRequest request) {
		return isUserInRole(PageRegistry.MARKETING, request);
	}

	public static String getUserRole(ServletRequest request){
		if (isUserAdmin(request)) {
			return PageRegistry.ADMIN;
		}
		if (isUserMarketing(request)) {
			return PageRegistry.MARKETING;
		}
		return PageRegistry.GUEST;
    }	
}
