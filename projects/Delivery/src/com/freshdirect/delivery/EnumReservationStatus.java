package com.freshdirect.delivery;

/**
 *
 * @author  knadeem
 * @version 
 */
public class EnumReservationStatus implements java.io.Serializable {

	private static final long	serialVersionUID	= 5002191229427433704L;
	
	public final static EnumReservationStatus RESERVED = new EnumReservationStatus(0, 5, "Reserved");
	public final static EnumReservationStatus COMMITTED = new EnumReservationStatus(1, 10, "Committed");
	public final static EnumReservationStatus CANCELED = new EnumReservationStatus(2, 15, "Canceled");
	public final static EnumReservationStatus EXPIRED = new EnumReservationStatus(3, 20, "Expired");

	protected final int id;
	private final int code;
	private final String name;
	
	private EnumReservationStatus(int id, int code, String name) {
		this.id = id;
		this.code = code;
		this.name = name;
	}

	public static EnumReservationStatus getReservationStatus(int status) {
		switch (status) {
			case 5 :
				return RESERVED;
			case 10 :
				return COMMITTED;
			case 15 :
				return CANCELED;
			case 20 :
				return EXPIRED;
			default :
				return null;
		}
	}

	public int getCode() {
		return this.code;
	}

	public String getName() {
		return this.name;
	}

	public boolean equals(Object o) {
		if (o instanceof EnumReservationStatus) {
			return this.id == ((EnumReservationStatus) o).id;
		}
		return false;
	}

}
