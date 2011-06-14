package com.freshdirect.transadmin.util;

public final class EnumStatus implements Comparable
{
	public static final  EnumStatus NoStatus=new EnumStatus(0,"");
	public static final  EnumStatus Route=new EnumStatus(1,"Route");
	public static final  EnumStatus ActualTruck=new EnumStatus(2,"Truck");
	public static final  EnumStatus PlannedTruck=new EnumStatus(2,"Truck-Pl");
	public static final  EnumStatus Packet=new EnumStatus(3,"Packet");
	public static final  EnumStatus EmpReady=new EnumStatus(4,"EmpReady");
	public static final  EnumStatus Ready=new EnumStatus(5,"Ready");
	public static final  EnumStatus HTOut=new EnumStatus(6,"HTOut");
	public static final  EnumStatus Dispatched=new EnumStatus(7,"Dispatched");
	public static final  EnumStatus HTIn=new EnumStatus(8,"HTIn");
	public static final  EnumStatus CheckedIn=new EnumStatus(9,"CheckedIn");
	public static final  EnumStatus OffPremises=new EnumStatus(10,"OffPremises");
	
	private int status;
	private String display;
	private EnumStatus(int i,String display)
	{
		this.status=i;
		this.display=display;
	}
	public int compareTo(Object o) 
	{
		if(o instanceof EnumStatus)
		{
			EnumStatus temp=(EnumStatus)o;
			return status-temp.status;
		}
		return -1;
	}
	public boolean equals(Object obj) 
	{
		if(obj instanceof EnumStatus)
		{
			EnumStatus temp=(EnumStatus)obj;
			return temp.status==status?true:false;
		}
		return false;
	}
	public int hashCode() {
		// TODO Auto-generated method stub
		return 17*status;
	}
	public String toString() {
		// TODO Auto-generated method stub
		return display;
	}
	
	
}
