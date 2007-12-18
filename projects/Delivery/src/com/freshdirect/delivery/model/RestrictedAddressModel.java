package com.freshdirect.delivery.model;

import java.util.Comparator;
import java.util.Date;

import com.freshdirect.common.address.AddressModel;
import com.freshdirect.delivery.EnumRestrictedAddressReason;

public class RestrictedAddressModel extends AddressModel {
	
	public RestrictedAddressModel(){
		
	}
	
	private Date lastModified=null;	
	private String modifiedBy=null;	
	private EnumRestrictedAddressReason reason=null;

	public Date getLastModified() {
		return lastModified;
	}

	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public EnumRestrictedAddressReason getReason() {
		return reason;
	}

	public void setReason(EnumRestrictedAddressReason reason) {
		this.reason = reason;
	}
	
	
	public static Comparator getAddressComparator(){
		
        return new Comparator(){
        	
        	public int compare(Object o1,Object o2){
        		if(o1 instanceof RestrictedAddressModel && o2 instanceof RestrictedAddressModel){
        			RestrictedAddressModel model1=(RestrictedAddressModel)o1;
        			RestrictedAddressModel model2=(RestrictedAddressModel)o2;
        			return model1.getAddress1().compareTo(model2.getAddress1());        			
        		}
        		return -1;
        	}        	
        };				
	}
	
	public static Comparator getZipCodeComparator(){
		
        return new Comparator(){
        	
        	public int compare(Object o1,Object o2){
        		if(o1 instanceof RestrictedAddressModel && o2 instanceof RestrictedAddressModel){
        			RestrictedAddressModel model1=(RestrictedAddressModel)o1;
        			RestrictedAddressModel model2=(RestrictedAddressModel)o2;
        			return model1.getZipCode().compareTo(model2.getZipCode());        			
        		}
        		return -1;
        	}        	
        };				
	}

	public static Comparator getApartmentComparator(){		
        return new Comparator(){
        	
        	public int compare(Object o1,Object o2){
        		if(o1 instanceof RestrictedAddressModel && o2 instanceof RestrictedAddressModel){
        			RestrictedAddressModel model1=(RestrictedAddressModel)o1;
        			RestrictedAddressModel model2=(RestrictedAddressModel)o2;
        			if(model1.getApartment()!=null && model2.getApartment()!=null )
        			    return model1.getApartment().compareTo(model2.getApartment());
        			else if(model1.getApartment()!=null && model2.getApartment()==null)
        				return 1;
        			else if(model1.getApartment()==null && model2.getApartment()!=null)
        				return -1;
        			else return 0;
        		}
        		return -1;
        	}        	
        };				
	}
	
	public static Comparator getModificationDateComparator(){		
        return new Comparator(){        	
        	public int compare(Object o1,Object o2){
        		if(o1 instanceof RestrictedAddressModel && o2 instanceof RestrictedAddressModel){
        			RestrictedAddressModel model1=(RestrictedAddressModel)o1;
        			RestrictedAddressModel model2=(RestrictedAddressModel)o2;
        			if(model1.getLastModified()!=null && model2.getLastModified()!=null){
        			    if(model1.getLastModified().after(model2.getLastModified())){
        			    	return 1;
        			    }else if(model1.getLastModified().before(model2.getLastModified())){
        			    	return -1;
        			    }
        			    else{
        			    	return 0;
        			    }
        			}        			
        			else if(model1.getLastModified()!=null && model2.getLastModified()==null) {
        				return 1;
        			}else if(model1.getLastModified()==null && model2.getLastModified()!=null){
        				return -1;
        			}
        			else return 0;
        		}
        		return -1;
        	}        	
        };				
	}

	public static Comparator getModifiedByComparator(){		
        return new Comparator(){        	
        	public int compare(Object o1,Object o2){
        		if(o1 instanceof RestrictedAddressModel && o2 instanceof RestrictedAddressModel){
        			RestrictedAddressModel model1=(RestrictedAddressModel)o1;
        			RestrictedAddressModel model2=(RestrictedAddressModel)o2;
        			if(model1.getModifiedBy()!=null && model2.getModifiedBy()!=null)
        			    return model1.getModifiedBy().compareTo(model2.getModifiedBy());
        			else if(model1.getModifiedBy()!=null && model2.getModifiedBy()==null)
        				return 1;
        			else if(model1.getModifiedBy()==null && model2.getModifiedBy()!=null)
        				return -1;
        			else return 0;
        		}
        		return -1;
        	}        	
        };				
	}

}
