package com.freshdirect.delivery.restriction;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.enums.Enum;

public class EnumDlvRestrictionReason extends Enum {

	public static final EnumDlvRestrictionReason BEER = new EnumDlvRestrictionReason("BER", "Beer");
	public static final EnumDlvRestrictionReason WINE = new EnumDlvRestrictionReason("WIN", "Wine");
	public static final EnumDlvRestrictionReason ALCOHOL = new EnumDlvRestrictionReason("ACL", "Alcohol");
	public static final EnumDlvRestrictionReason KOSHER = new EnumDlvRestrictionReason("KHR", "special kosher item");
	public static final EnumDlvRestrictionReason THANKSGIVING = new EnumDlvRestrictionReason("TKG", "Thanksgiving", true);
	public static final EnumDlvRestrictionReason VALENTINES = new EnumDlvRestrictionReason("VAL", "Valentine's Day", true);
	public static final EnumDlvRestrictionReason EASTER = new EnumDlvRestrictionReason("EAS", "Easter", true);
	public static final EnumDlvRestrictionReason PLATTER = new EnumDlvRestrictionReason("PLT", "Platter");
	public static final EnumDlvRestrictionReason CLOSED = new EnumDlvRestrictionReason("CLD", "Close");
	public static final EnumDlvRestrictionReason TEMP_A = new EnumDlvRestrictionReason("TMPA", "Temporary A");
	public static final EnumDlvRestrictionReason TEMP_B = new EnumDlvRestrictionReason("TMPB", "Temporary B");
	public static final EnumDlvRestrictionReason TEMP_C = new EnumDlvRestrictionReason("TMPC", "Temporary C");
	public static final EnumDlvRestrictionReason TEMP_D = new EnumDlvRestrictionReason("TMPD", "Temporary D");
	public static final EnumDlvRestrictionReason TEMP_E = new EnumDlvRestrictionReason("TMPE", "Temporary E");
	public static final EnumDlvRestrictionReason TEMP_F = new EnumDlvRestrictionReason("TMPF", "Temporary F");
	public static final EnumDlvRestrictionReason TEMP_G = new EnumDlvRestrictionReason("TMPG", "Temporary G");
	public static final EnumDlvRestrictionReason TEMP_H = new EnumDlvRestrictionReason("TMPH", "Temporary H");
	public static final EnumDlvRestrictionReason TEMP_I = new EnumDlvRestrictionReason("TMPI", "Temporary I");
	public static final EnumDlvRestrictionReason TEMP_J = new EnumDlvRestrictionReason("TMPJ", "Temporary J");
	public static final EnumDlvRestrictionReason TEMP_K = new EnumDlvRestrictionReason("TMPK", "Temporary K");
	public static final EnumDlvRestrictionReason TEMP_L = new EnumDlvRestrictionReason("TMPL", "Temporary L");
	public static final EnumDlvRestrictionReason TEMP_M = new EnumDlvRestrictionReason("TMPM", "Temporary M");

	public static final EnumDlvRestrictionReason BLOCK_SUNDAY = new EnumDlvRestrictionReason("B_SUN", "Block Sunday");
	public static final EnumDlvRestrictionReason BLOCK_MONDAY = new EnumDlvRestrictionReason("B_MON", "Block Monday");
	public static final EnumDlvRestrictionReason BLOCK_TUESDAY = new EnumDlvRestrictionReason("B_TUE", "Block Tuesday");
	public static final EnumDlvRestrictionReason BLOCK_WEDNESDAY = new EnumDlvRestrictionReason("B_WED", "Block Wednesday");
	public static final EnumDlvRestrictionReason BLOCK_THURSDAY = new EnumDlvRestrictionReason("B_THU", "Block Thursday");
	public static final EnumDlvRestrictionReason BLOCK_FRIDAY = new EnumDlvRestrictionReason("B_FRI", "Block Friday");
	public static final EnumDlvRestrictionReason BLOCK_SATURDAY = new EnumDlvRestrictionReason("B_SAT", "Block Saturday");
	
	// new thanks giving deals
	public static final EnumDlvRestrictionReason THANKSGIVING_MEALS = new EnumDlvRestrictionReason("TKGML", "Thanksgiving Meals", true);
	
	// test easter/passover meals (meals == advanced order)
	public static final EnumDlvRestrictionReason EASTER_MEALS = new EnumDlvRestrictionReason("EASML", "Easter Meals", true);
	
	public static final EnumDlvRestrictionReason EBT_PAYMENT = new EnumDlvRestrictionReason("EBT_PMNT", "EBT Payment");
	
	//Day Part Selling 
	public static final EnumDlvRestrictionReason DAY_PART_RESTRICTION_1 = new EnumDlvRestrictionReason("DPR1", "Day Part Selling Restriction Type 1");
	public static final EnumDlvRestrictionReason DAY_PART_RESTRICTION_2 = new EnumDlvRestrictionReason("DPR2", "Day Part Selling Restriction Type 2");
	public static final EnumDlvRestrictionReason DAY_PART_RESTRICTION_3 = new EnumDlvRestrictionReason("DPR3", "Day Part Selling Restriction Type 3");
	public static final EnumDlvRestrictionReason DAY_PART_RESTRICTION_4 = new EnumDlvRestrictionReason("DPR4", "Day Part Selling Restriction Type 4");
	public static final EnumDlvRestrictionReason DAY_PART_RESTRICTION_5 = new EnumDlvRestrictionReason("DPR5", "Day Part Selling Restriction Type 5");
	
	private final String description;
	private final boolean specialHoliday;

	public EnumDlvRestrictionReason(String name, String description) {
		this(name, description, false);
	}

	private EnumDlvRestrictionReason(String name, String description, boolean holiday) {
		super(name);
		this.description = description;
		this.specialHoliday = holiday;
	}

	public String getDescription() {
		return this.description;
	}

	public boolean isSpecialHoliday() {
		return this.specialHoliday;
	}

	public static EnumDlvRestrictionReason getEnum(String name) {
		return (EnumDlvRestrictionReason) getEnum(EnumDlvRestrictionReason.class, name);
	}

	public static Map getEnumMap() {
		return getEnumMap(EnumDlvRestrictionReason.class);
	}

	public static List getEnumList() {
		return getEnumList(EnumDlvRestrictionReason.class);
	}
	public static List<EnumDlvRestrictionReason> getAlcoholEnumList() {
		List<EnumDlvRestrictionReason> aList = new ArrayList<EnumDlvRestrictionReason>();
		aList.add(EnumDlvRestrictionReason.ALCOHOL);
		aList.add(EnumDlvRestrictionReason.BEER);
		aList.add(EnumDlvRestrictionReason.WINE);
		return aList;
	}
	
	public static List<EnumDlvRestrictionReason> getNonAlcoholEnumList() {
		List<EnumDlvRestrictionReason> aList = new ArrayList<EnumDlvRestrictionReason>();
		aList.add(EnumDlvRestrictionReason.KOSHER);
		aList.add(EnumDlvRestrictionReason.THANKSGIVING);
		aList.add(EnumDlvRestrictionReason.VALENTINES);
		aList.add(EnumDlvRestrictionReason.EASTER);
		aList.add(EnumDlvRestrictionReason.PLATTER);
		aList.add(EnumDlvRestrictionReason.CLOSED);
		aList.add(EnumDlvRestrictionReason.TEMP_A);
		aList.add(EnumDlvRestrictionReason.TEMP_B);
		aList.add(EnumDlvRestrictionReason.TEMP_C);
		aList.add(EnumDlvRestrictionReason.TEMP_D);
		aList.add(EnumDlvRestrictionReason.TEMP_E);
		aList.add(EnumDlvRestrictionReason.TEMP_F);
		aList.add(EnumDlvRestrictionReason.TEMP_G);
		aList.add(EnumDlvRestrictionReason.TEMP_H);
		aList.add(EnumDlvRestrictionReason.TEMP_I);
		aList.add(EnumDlvRestrictionReason.TEMP_J);
		aList.add(EnumDlvRestrictionReason.TEMP_K);
		aList.add(EnumDlvRestrictionReason.TEMP_L);
		aList.add(EnumDlvRestrictionReason.TEMP_M);
		aList.add(EnumDlvRestrictionReason.BLOCK_SUNDAY);
		aList.add(EnumDlvRestrictionReason.BLOCK_MONDAY);
		aList.add(EnumDlvRestrictionReason.BLOCK_TUESDAY);
		aList.add(EnumDlvRestrictionReason.BLOCK_WEDNESDAY);
		aList.add(EnumDlvRestrictionReason.BLOCK_THURSDAY);
		aList.add(EnumDlvRestrictionReason.BLOCK_FRIDAY);
		aList.add(EnumDlvRestrictionReason.BLOCK_SATURDAY);
		aList.add(EnumDlvRestrictionReason.THANKSGIVING_MEALS);
		aList.add(EnumDlvRestrictionReason.EASTER_MEALS);
		aList.add(EnumDlvRestrictionReason.DAY_PART_RESTRICTION_1);
		aList.add(EnumDlvRestrictionReason.DAY_PART_RESTRICTION_2);
		aList.add(EnumDlvRestrictionReason.DAY_PART_RESTRICTION_3);
		aList.add(EnumDlvRestrictionReason.DAY_PART_RESTRICTION_4);
		aList.add(EnumDlvRestrictionReason.DAY_PART_RESTRICTION_5);
		return aList;
	}
	public static Iterator iterator() {
		return iterator(EnumDlvRestrictionReason.class);
	}

	public static List<EnumDlvRestrictionReason> getDayPartRestrictionsEumList() {
		List<EnumDlvRestrictionReason> aList = new ArrayList<EnumDlvRestrictionReason>();
		aList.add(EnumDlvRestrictionReason.DAY_PART_RESTRICTION_1);
		aList.add(EnumDlvRestrictionReason.DAY_PART_RESTRICTION_2);
		aList.add(EnumDlvRestrictionReason.DAY_PART_RESTRICTION_3);
		aList.add(EnumDlvRestrictionReason.DAY_PART_RESTRICTION_4);
		aList.add(EnumDlvRestrictionReason.DAY_PART_RESTRICTION_5);
		return aList;
	}
}