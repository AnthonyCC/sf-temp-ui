package com.freshdirect.mail;

/*
 * Created on Jun 2, 2005
 */

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.enums.Enum;

/**
 * @author jng
 *
 */
public class EnumTEmailProviderType extends Enum {
	//THE IDX FIELDS SO THAT CLASS CAN BE USED IN A JAVA 6 SWITCH STMT.
    public  final static int CHEETAH_IDX=1;
    public  final static int FDSTORE_IDX=2;
    public  final static int SILVERPOP_IDX=3;
	//DONT DUPLICATE VALUES IN  THE INDEX FIELD, WILL SCREW UP ITS USE IN SWITCH STMTS!!!!
	public static final EnumTEmailProviderType CHEETAH = new EnumTEmailProviderType("CHEETAH", "Cheetah Provider",CHEETAH_IDX);
	public static final EnumTEmailProviderType FDSTORE = new EnumTEmailProviderType("FDSTORE", "Internal Email provider",FDSTORE_IDX);
	public static final EnumTEmailProviderType SILVERPOP = new EnumTEmailProviderType("SILVERPOP", "IBM SilverPop Transact Email",SILVERPOP_IDX);

    private String description;
    private final int index;

    
    

    protected EnumTEmailProviderType(String name, String description, int idx) {
		super(name);
	    this.description = description;
	    this.index=idx;
	}
	
	public static EnumTEmailProviderType getEnum(String type) {
		return (EnumTEmailProviderType) getEnum(EnumTEmailProviderType.class, type);
	}
		
	public static Map getEnumMap() {
		return getEnumMap(EnumTEmailProviderType.class);
	}

	public static List getEnumList() {
		return getEnumList(EnumTEmailProviderType.class);
	}

	public static Iterator iterator() {
		return iterator(EnumTEmailProviderType.class);
	}

	public String getDescription(){
		return this.description;
	}

	public String toString() {
		return this.description;		
	}	
	public int getIndex(){
		return this.index;
	}
}
