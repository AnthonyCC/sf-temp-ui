package com.freshdirect.truckassignment;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class Route {
	public final static int NOT_FOUND = -1;

	private Dispatch dispatch;
	private int start;
	private int end;
	private LinkedHashMap<Integer, Integer> truckPreferences;
	private List<Integer> trucks;
	private final int maxPreferredTrucks;

	private int solution = NOT_FOUND;

	public Route(Dispatch dispatch, int start, int end, LinkedHashMap<Integer, Integer> truckPreferences, int maxPreferredTrucks) {
		this.dispatch = dispatch;
		this.start = start;
		this.end = end;
		this.truckPreferences = truckPreferences;
		this.trucks = new ArrayList<Integer>(truckPreferences.keySet());
		this.maxPreferredTrucks = maxPreferredTrucks;
	}

	public Dispatch getDispatch() {
		return dispatch;
	}

	public boolean interlap(Route other) {
		return (other.start <= start && start <= other.end) || (other.start <= end && end <= other.end);
	}

	public int getPreferredTruck(int pos) {
		if (pos < maxPreferredTrucks && pos < trucks.size())
			return trucks.get(pos);
		else
			return 0;
	}
	
	public int getPreferenceMeasure(int pos) {
		int truck = getPreferredTruck(pos);
		if (truck > 0)
			return truckPreferences.get(truck);
		else
			return 1;
	}

	public int getPreferredTruckPosition(int truckNumber) {
		for (int i = 0; i < Math.min(trucks.size(), maxPreferredTrucks); i++) {
			if (trucks.get(i) == truckNumber) {
				return i;
			}
		}
		return -1;
	}

	public int getEnd() {
		return end;
	}

	public int getStart() {
		return start;
	}

	public int getSolution() {
		return solution;
	}

	public void setSolution(int solution) {
		this.solution = solution;
	}

	@Override
	public String toString() {
		return "Route [start=" + start + ", end=" + end + ", dispatchId=" + dispatch.getId() + ", truckPreferences="
				+ truckPreferences + ", solution=" + solution + "]";
	}

	public String getSolutionAsString() {
		if (solution == NOT_FOUND) {
			return "Unknown";
		}
		if (0 <= solution && solution < maxPreferredTrucks && solution < trucks.size()) {
			return " " + solution + ". truck[" + getPreferredTruck(solution) + ']';
		}
		return "Not preferred truck";
	}
}
