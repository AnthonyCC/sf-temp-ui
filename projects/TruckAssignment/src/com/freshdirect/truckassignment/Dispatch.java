package com.freshdirect.truckassignment;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.Map.Entry;

public class Dispatch extends Id implements Cloneable, Comparable<Dispatch> {
	public static String formatMinutesOffset(int minutes) {
		int hours = minutes / 60;
		StringBuilder buf = new StringBuilder();
		if (hours % 24 < 10)
			buf.append("0");
		buf.append(hours % 24);
		buf.append(":");
		if (minutes % 60 < 10)
			buf.append("0");
		buf.append(minutes % 60);
		if (hours >= 24)
			buf.append(" (next day)");
		return buf.toString();
	}

	private Date date;
	private String zone;
	private String area;
	private String route;
	private Truck truck;
	private int leaves;
	private int nextAvailable;
	private int plannedEnd;
	private Employee employee;

	public Dispatch(String id) {
		super(id);
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getZone() {
		return zone;
	}

	public void setZone(String zone) {
		this.zone = zone;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getRoute() {
		return route;
	}

	public void setRoute(String route) {
		this.route = route;
	}

	public Truck getTruck() {
		return truck;
	}

	public void setTruck(Truck truck) {
		this.truck = truck;
	}

	public int getLeaves() {
		return leaves;
	}

	public void setLeaves(int leaves) {
		this.leaves = leaves;
	}

	public int getNextAvailable() {
		return nextAvailable;
	}

	public void setNextAvailable(int nextAvailable) {
		this.nextAvailable = nextAvailable;
	}

	public int getPlannedEnd() {
		return plannedEnd;
	}

	public void setPlannedEnd(int plannedEnd) {
		this.plannedEnd = plannedEnd;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	private static int indexOf(LinkedHashMap<Truck, Integer> preferences, Truck truck) {
		int i = 0;
		for (Entry<Truck, Integer> entry : preferences.entrySet()) {
			if (entry.getKey().equals(truck))
				return i;
			i++;
		}
		return -1;
	}

	public double getPreference() {
		if (truck == null)
			return Double.NaN;
		if (truck.isVirtual())
			return Double.NEGATIVE_INFINITY;
		int index = indexOf(employee.getPreferences(), truck);
		if (index < 0)
			return Double.POSITIVE_INFINITY;
		return index;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Dispatch [id=");
		builder.append(getId());
		builder.append(", date=");
		builder.append(TruckAssignmentProblem.DATE_FORMAT.format(date));
		builder.append(", leaves=");
		builder.append(formatMinutesOffset(leaves));
		builder.append(", nextAvailable=");
		builder.append(formatMinutesOffset(nextAvailable));
		builder.append(", plannedEnd=");
		builder.append(formatMinutesOffset(plannedEnd));
		builder.append(", zone=");
		builder.append(zone);
		builder.append(", area=");
		builder.append(area);
		builder.append(", route=");
		builder.append(route);
		builder.append(", truck=");
		builder.append(truck == null ? truck : truck.getId());
		builder.append(", employee=");
		builder.append(employee.getId());
		if (truck != null) {
			builder.append(", employeesPreference=");
			double preference = getPreference();
			if (preference == Double.POSITIVE_INFINITY)
				builder.append("NONE");
			else if (preference == Double.NEGATIVE_INFINITY)
				builder.append("MISSING");
			else if (preference == 0.0)
				builder.append("FIRST");
			else {
				builder.append("SECOND (");
				builder.append(preference + 1);
				builder.append(")");
			}
		}
		builder.append("]");
		return builder.toString();
	}

	public boolean collide(Set<Dispatch> routes) {
		for (Dispatch route : routes)
			if (collide(route))
				return true;
		return false;
	}

	public boolean collide(Dispatch route) {
		return between(leaves, route.leaves, route.nextAvailable) || between(nextAvailable, route.leaves, route.nextAvailable);
	}
	
	public boolean collide(long tick) {
		return between(tick, leaves, nextAvailable);
	}

	private boolean between(long x, long a, long b) {
		return x >= a && x <= b;
	}

	public int compareTo(Dispatch o) {
		if (getId() == o.getId())
			return 0;

		int i = date.compareTo(o.date);
		if (i != 0)
			return i;

		i = (int) (leaves - o.leaves);
		if (i != 0)
			return i;

		return (int) (nextAvailable - o.nextAvailable);
	}

	@Override
	protected Dispatch clone() {
		Dispatch route = new Dispatch(getId());
		route.date = date;
		route.leaves = leaves;
		route.nextAvailable = nextAvailable;
		route.zone = zone;
		route.employee = employee;
		return route;
	}
}