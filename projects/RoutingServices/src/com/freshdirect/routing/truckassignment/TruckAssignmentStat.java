package com.freshdirect.routing.truckassignment;

import java.text.ParseException;
import java.util.Date;

public class TruckAssignmentStat implements Comparable<TruckAssignmentStat> {
	private int n;
	private String truckId;
	private String employeeId;
	private Date date;

	public TruckAssignmentStat(int n, String employeeId, String truckId, Date date) {
		super();
		this.n = n;
		this.truckId = truckId;
		this.employeeId = employeeId;
		this.date = date;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + ((employeeId == null) ? 0 : employeeId.hashCode());
		result = prime * result + n;
		result = prime * result + ((truckId == null) ? 0 : truckId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TruckAssignmentStat other = (TruckAssignmentStat) obj;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (employeeId == null) {
			if (other.employeeId != null)
				return false;
		} else if (!employeeId.equals(other.employeeId))
			return false;
		if (n != other.n)
			return false;
		if (truckId == null) {
			if (other.truckId != null)
				return false;
		} else if (!truckId.equals(other.truckId))
			return false;
		return true;
	}

	@Override
	public int compareTo(TruckAssignmentStat o) {
		return date.compareTo(o.date);
	}

	@Override
	public String toString() {
		return "TruckAssignmentStat[N=" + n + ", date=" + date + ", employeeId=" + employeeId + ", truckId=" + truckId + "]";
	}

	public int getN() {
		return n;
	}

	public void setN(int n) {
		this.n = n;
	}

	public String getTruckId() {
		return truckId;
	}

	public void setTruckId(String truckId) {
		this.truckId = truckId;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String format() {
		return n + "," + employeeId + "," + truckId + "," + TruckAssignmentProblem.DATE_FORMAT.format(date);
	}

	public static TruckAssignmentStat parse(String line) throws ParseException {
		String[] split = line.split(",");
		if (split.length < 4)
			throw new ParseException("missing column(s) in line: " + line, line.length());
		int n = Integer.parseInt(split[0]);
		Date date = TruckAssignmentProblem.DATE_FORMAT.parse(split[3]);
		return new TruckAssignmentStat(n, split[1], split[2], date);
	}
}
