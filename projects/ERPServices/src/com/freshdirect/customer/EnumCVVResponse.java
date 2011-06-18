package com.freshdirect.customer;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.enums.ValuedEnum;

public class EnumCVVResponse extends ValuedEnum { 


	private static final long serialVersionUID = 8856267207629465548L;
	public final static EnumCVVResponse MATCH		= new EnumCVVResponse(0, "M", "Match");
    public final static EnumCVVResponse NO_MATCH		= new EnumCVVResponse(1, "N", "No Match");
    public final static EnumCVVResponse CVV_NOT_INCLUDED_IN_REQUEST			= new EnumCVVResponse(2, "S", "Card verification number was not included in the request");
    public final static EnumCVVResponse VALIDATION_FAILURE	= new EnumCVVResponse(3, "I", "Card verification number failed processor's data validation check");
    public final static EnumCVVResponse NOT_SUPPORTED	= new EnumCVVResponse(4, "U", "Card verification is not supported by the issuing bank");
    public final static EnumCVVResponse SERVICE_UNAVAILABLE			= new EnumCVVResponse(5, "P", "Card verification number not processed by processor for unspecified reason");
    

    public final static EnumCVVResponse NO_RESPONSE	= new EnumCVVResponse(6, "X", "No Response");
    

    private String description;
		
	public static EnumCVVResponse getEnum(String code) {
		return (EnumCVVResponse) getEnum(EnumCVVResponse.class, code);
	}

	public static EnumCVVResponse getEnum(int id) {
		return (EnumCVVResponse) getEnum(EnumCVVResponse.class, id);
	}

	public static Map getEnumMap() {
		return getEnumMap(EnumCVVResponse.class);
	}

	public static List getEnumList() {
		return getEnumList(EnumCVVResponse.class);
	}

	public static Iterator<EnumCVVResponse> iterator() {
		return iterator(EnumCVVResponse.class);
	}
	
	public String getDescription(){
		return this.description;
	}

	public String toString() {
		return this.description;		
	}
    
    private EnumCVVResponse(int id, String name, String description) {
		super(name, id);
        this.description = description;
    }

    public String getCode() {
		return getName();
	}	
}
