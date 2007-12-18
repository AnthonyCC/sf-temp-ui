/*
 * EnumZipCheckResponses.java
 *
 * Created on January 22, 2002, 3:51 PM
 */

package com.freshdirect.delivery;

/**
 *
 * @author  knadeem
 * @version 
 */
public class EnumZipCheckResponses implements java.io.Serializable{
	
	public final static EnumZipCheckResponses NEED_MOREINFO = new EnumZipCheckResponses(1, "NMF", "Need more address information");
	public final static EnumZipCheckResponses DELIVER = new EnumZipCheckResponses(2, "DLR", "Deliver to this zipcode");
	public final static EnumZipCheckResponses DONOT_DELIVER = new EnumZipCheckResponses(3, "DDR", "Donot deliver to this zipcode");
	
	protected final int id;
	private final String code;
	private final String name;
	
	/** Creates new EnumZipCheckResponses */
    private EnumZipCheckResponses(int id, String code, String name) {
		this.id = id;
		this.code = code;
		this.name = name;
    }
	
	public String getCode(){
		return this.code;
	}
	
	public String getName(){
		return this.name;
	}
	
	public boolean equals(Object o) {
		if (o instanceof EnumZipCheckResponses) {
			return this.id == ((EnumZipCheckResponses)o).id;
		}
		return false;
    }

}
