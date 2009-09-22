package com.freshdirect.fdstore.util;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.TreeSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.freshdirect.fdstore.FDReservation;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.FDTimeslot;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.customer.ProfileModel;
import com.freshdirect.framework.conf.ResourceUtil;
import com.freshdirect.framework.util.EnumLogicalOperator;
import com.thoughtworks.xstream.XStream;

public class CTDeliveryCapacityLogic 
{	
	public static boolean loadedCT=false;
	public static Set<CTProfileConfig> CT_CONFIG;
	
	public static boolean loadedPR1=false;
	public static Set<CTProfileConfig> PR1_CONFIG;
	
	public static final DateFormat MONTH_DATE_YEAR_FORMATTER = new SimpleDateFormat("MM/dd/yyyy");
	public static void loadCTConfig(int order)
	{
		if((order==1&&!loadedCT)||(order==2&&!loadedPR1))
		{
			Set<CTProfileConfig> CONFIG=new TreeSet<CTProfileConfig>();
			XStream xstream = new XStream();
			xstream.alias("campaign", CTProfileConfig.class);
			xstream.alias("code", String.class);
			xstream.alias("profiles", List.class);
			xstream.alias("profile", Profile.class);
			xstream.alias("name", String.class);
			xstream.alias("value", String.class);
			xstream.alias("zone", String.class);
			xstream.alias("min", String.class);
			xstream.alias("max", String.class);
			xstream.alias("startDate", String.class);
			xstream.alias("endDate", String.class);
			xstream.alias("priority", String.class);
			xstream.alias("name", String.class);
			xstream.alias("matchAllprofiles", String.class);
			xstream.alias("defaultZoneCapacityCondition", String.class);
			xstream.alias("defaultZoneCapacityValue", String.class);
			xstream.alias("zones", List.class);
			xstream.alias("zone", Zone.class);
			xstream.alias("code", String.class);
			xstream.alias("condition", Zone.class);
			xstream.alias("value", Zone.class);
			
			try {
				InputStream is;
				if(order==1)
				{
					is = ResourceUtil.openResource("classpath: "+FDStoreProperties.getCTCapacityFileName());
				}
				else
				{
					is = ResourceUtil.openResource("classpath: "+FDStoreProperties.getPR1CapacityFileName());
				}
				if (is == null) 
				{
					throw new IOException("cannot find the file rules.xml on classpath");
				}
				ObjectInputStream in = xstream.createObjectInputStream(new InputStreamReader(is));
				while (true) {
					try {
						CTProfileConfig r = (CTProfileConfig) in.readObject();
						CONFIG.add(r);
					} catch (EOFException e) {
						break;
					}
				}	
				if(order==1)
				{
					loadedCT=true;
					CT_CONFIG=CONFIG;
				}
				else
				{
					loadedPR1=true;
					PR1_CONFIG=CONFIG;
				}
			
			}catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		
	}
	
	public static String isEligible(FDUserI user,FDTimeslot timeSlot)
	{
		
			try {
			String zone=timeSlot.getZoneCode();
			loadCTConfig(1);		
			int totalOrder=user.getOrderHistory().getValidOrderCount();			
			for(CTProfileConfig config:CT_CONFIG)
			{ 
				long currentTime=System.currentTimeMillis();
				if(config.getStartDate()!=null&&config.getEndDate()!=null&&config.getStartDate().getTime()<=currentTime&&currentTime<=config.getEndDate().getTime())
				if(config.getMin()<=totalOrder && totalOrder<=config.getMax())
				{
					
					//check zone validation
					if((config.getZones()!=null&&config.getZones().size()>0)||(config.getAllZoneCondition()!=null))
					{
						Zone z=null;
						if(config.getZones()!=null&&config.getZones().size()>0)
						{
							for(Zone tempZ:config.getZones())
							{
								if(zone.equalsIgnoreCase(tempZ.getCode()))
								{
									z=tempZ;
								}
							}
							if(z==null)continue;
						}
						String condition=config.getAllZoneCondition();
						int matchingValue=0;
						if(timeSlot.getDlvTimeslot().getCapacity()!=0)
						matchingValue=(timeSlot.getDlvTimeslot().getTotalAvailable()*100/timeSlot.getDlvTimeslot().getCapacity());
						int value=config.getAllZonevalue();
						if(z!=null)
						{
							condition=z.getCondition();
							value=z.getValue();
						}
						if (condition.equals(EnumLogicalOperator.LESS_THAN.toString())&&!(matchingValue<value)) 
						{             
							continue;
			            }
			            else if (condition.equals(EnumLogicalOperator.GREATER_THAN.toString())&&!(matchingValue>value)) {              
			            	continue;
			            }
			            else if (condition.equals(EnumLogicalOperator.LESS_THAN_OR_EQUAL.toString())&&!(matchingValue<=value)) {               
			            	continue;
			            }
			            else if (condition.equals(EnumLogicalOperator.GREATER_THAN_OR_EQUAL.toString())&&!(matchingValue>=value)) {                
			            	continue;
			            }
						
					}
					
					//this is for first time users.
					if(config.getProfiles()==null||config.getProfiles().size()==0)return config.getCode();
					if(!config.isAllConditionMatch())
					{
						if(user.getFDCustomer()!=null&&user.getFDCustomer().getProfile()!=null)
						{
							ProfileModel profile=user.getFDCustomer().getProfile();
							for(Profile p:config.getProfiles())
							{
								String value=profile.getAttribute(p.getName());
								if(value!=null&&p.getValue()!=null&&p.getValue().contains(value))
								{
									return config.getCode();
								}
							}
						}
					}
					else
					{
						boolean allMatched=true;
						if(user.getFDCustomer()!=null&&user.getFDCustomer().getProfile()!=null)
						{
							ProfileModel profile=user.getFDCustomer().getProfile();
							for(Profile p:config.getProfiles())
							{
								String value=profile.getAttribute(p.getName());
								if(value==null||p.getValue()==null||!p.getValue().contains(value))
								{
									allMatched=false;
								}
							}
						}
						if(allMatched) return config.getCode();
					}
				}
			}
			return null;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}		
	}
	
	public static boolean isPR1(FDUserI user,FDTimeslot timeSlot)
	{
		
			try 
			{
			String zone=timeSlot.getZoneCode();
			loadCTConfig(2);		
			int totalOrder=user.getOrderHistory().getValidOrderCount();			
			for(CTProfileConfig config:PR1_CONFIG)
			{ 
				long currentTime=System.currentTimeMillis();
				if(config.getStartDate()!=null&&config.getEndDate()!=null&&config.getStartDate().getTime()<=currentTime&&currentTime<=config.getEndDate().getTime())
				if(config.getMin()<=totalOrder && totalOrder<=config.getMax())
				{
					
					//check zone validation
					if((config.getZones()!=null&&config.getZones().size()>0)||(config.getAllZoneCondition()!=null))
					{
						Zone z=null;
						if(config.getZones()!=null&&config.getZones().size()>0)
						{
							for(Zone tempZ:config.getZones())
							{
								if(zone.equalsIgnoreCase(tempZ.getCode()))
								{
									z=tempZ;
								}
							}
							if(z==null)continue;
						}
						String condition=config.getAllZoneCondition();
						int matchingValue=0;
						if(timeSlot.getDlvTimeslot().getCapacity()!=0)
						matchingValue=(timeSlot.getDlvTimeslot().getTotalAvailable()*100/timeSlot.getDlvTimeslot().getCapacity());
						int value=config.getAllZonevalue();
						if(z!=null)
						{
							condition=z.getCondition();
							value=z.getValue();
						}
						if (condition.equals(EnumLogicalOperator.LESS_THAN.toString())&&!(matchingValue<value)) 
						{             
							continue;
			            }
			            else if (condition.equals(EnumLogicalOperator.GREATER_THAN.toString())&&!(matchingValue>value)) {              
			            	continue;
			            }
			            else if (condition.equals(EnumLogicalOperator.LESS_THAN_OR_EQUAL.toString())&&!(matchingValue<=value)) {               
			            	continue;
			            }
			            else if (condition.equals(EnumLogicalOperator.GREATER_THAN_OR_EQUAL.toString())&&!(matchingValue>=value)) {                
			            	continue;
			            }
						
					}
					
					//this is for first time users.
					if(config.getProfiles()==null||config.getProfiles().size()==0)return true;
					if(!config.isAllConditionMatch())
					{
						if(user.getFDCustomer()!=null&&user.getFDCustomer().getProfile()!=null)
						{
							ProfileModel profile=user.getFDCustomer().getProfile();
							for(Profile p:config.getProfiles())
							{
								String value=profile.getAttribute(p.getName());
								if(value!=null&&p.getValue()!=null&&p.getValue().contains(value))
								{
									return true;
								}
							}
						}
					}
					else
					{
						boolean allMatched=true;
						if(user.getFDCustomer()!=null&&user.getFDCustomer().getProfile()!=null)
						{
							ProfileModel profile=user.getFDCustomer().getProfile();
							for(Profile p:config.getProfiles())
							{
								String value=profile.getAttribute(p.getName());
								if(value==null||p.getValue()==null||!p.getValue().contains(value))
								{
									allMatched=false;
								}
							}
						}
						if(allMatched) return true;
					}
				}
			}
			return false;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}		
	}	
}

class CTProfileConfig implements Comparable
{
	private List<Profile> profiles;
	private String code;	
	private int min=-1;
	private int max=-1;	
	private int priority=0;
	private String name;
	private String startDate;
	private String endDate;
	private boolean matchAllprofiles;
	private List<Zone> zones;
	private String defaultZoneCapacityCondition;
	private int defaultZoneCapacityValue;
	
	public int getMin() {
		return min;
	}
	public void setMin(int min) {
		this.min = min;
	}
	public void setMin(String min) {
		try {
			this.min =Integer.parseInt( min);
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public int getMax() {
		return max;
	}
	public void setMax(int max) {
		this.max = max;
	}
	public void setMax(String min) {
		try {
			this.max =Integer.parseInt( min);
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public Date getStartDate() {
		try {
			return CTDeliveryCapacityLogic.MONTH_DATE_YEAR_FORMATTER.parse(startDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public Date getEndDate() {
		try {
			return CTDeliveryCapacityLogic.MONTH_DATE_YEAR_FORMATTER.parse(endDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	public void setPriority(String priority) {
		this.priority =Integer.parseInt( priority);
	}
	public int compareTo(Object o) {
		if(o instanceof CTProfileConfig)
		{
			return priority-((CTProfileConfig)o).priority;
		}
		return 0;
	}
	public List<Profile> getProfiles() {
		return profiles;
	}
	public void setProfiles(List<Profile> profiles) {
		this.profiles = profiles;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isAllConditionMatch() {
		return matchAllprofiles;
	}
	public void setAllConditionMatch(boolean allConditionMatch) {
		this.matchAllprofiles = allConditionMatch;
	}
	public List<Zone> getZones() {
		return zones;
	}
	public void setZones(List<Zone> zones) {
		this.zones = zones;
	}
	public String getAllZoneCondition() {
		return defaultZoneCapacityCondition;
	}
	public void setAllZoneCondition(String allZoneCondition) {
		this.defaultZoneCapacityCondition = allZoneCondition;
	}
	public int getAllZonevalue() {
		return defaultZoneCapacityValue;
	}
	public void setAllZonevalue(int allZonevalue) {
		this.defaultZoneCapacityValue = allZonevalue;
	}
}
class Profile
{
	String name;
	private String value;	
	public String getName() 
	{
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<String> getValue() {
		if(value!=null)
		{
			String[] bcc = value.split(",");
	 		List<String>  bccs= new ArrayList<String>(bcc.length);
	 		for (int i = 0; i < bcc.length; i++) {
	 			String addr = bcc[i].trim();
	 			if (addr.length() != 0)
	 				bccs.add(addr);
	 		}
			return bccs;
		}
		return null;
	}
	
	
}

class Zone
{
	String code;
	String condition;
	int value;
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getCondition() {
		return condition;
	}
	public void setCondition(String condition) {
		this.condition = condition;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	
	
}