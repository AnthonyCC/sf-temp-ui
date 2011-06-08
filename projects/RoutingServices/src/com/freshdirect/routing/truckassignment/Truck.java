package com.freshdirect.routing.truckassignment;

public class Truck extends Id implements Comparable<Truck> {
	private static int virtualId = 0;

	private synchronized static int getVirtualId() {
		return ++virtualId;
	}

	public static Truck newUnknownTruck(String id) {
		Truck truck = new Truck(id);
		truck.inService = false;
		truck.vendor = "Unknown";
		truck.length = "Unknown";
		truck.rental = "Unknown";
		truck.virtual = false;
		return truck;
	}

	public static Truck newVirtualTruck() {
		Truck truck = new Truck("V" + getVirtualId());
		truck.inService = true;
		truck.vendor = "Unknown";
		truck.length = "Unknown";
		truck.rental = "Unknown";
		truck.virtual = true;
		return truck;
	}

	private String vendor;
	private String length;
	private String rental;
	private boolean inService;
	private boolean virtual;

	public Truck(String id) {
		super(id);
	}

	public String getVendor() {
		return vendor;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	public String getLength() {
		return length;
	}

	public void setLength(String length) {
		this.length = length;
	}

	public String getRental() {
		return rental;
	}

	public void setRental(String rental) {
		this.rental = rental;
	}

	public boolean isInService() {
		return inService;
	}

	public void setInService(boolean inService) {
		this.inService = inService;
	}

	public boolean isVirtual() {
		return virtual;
	}

	public void setVirtual(boolean virtual) {
		this.virtual = virtual;
	}

	@Override
	public int compareTo(Truck t) {
		return getId().compareTo(t.getId());
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Truck [id=");
		builder.append(getId());
		builder.append(", vendor=");
		builder.append(vendor);
		builder.append(", length=");
		builder.append(length);
		builder.append(", rental=");
		builder.append(rental);
		builder.append(", inService=");
		builder.append(inService);
		builder.append(", virtual=");
		builder.append(virtual);
		builder.append("]");
		return builder.toString();
	}
}