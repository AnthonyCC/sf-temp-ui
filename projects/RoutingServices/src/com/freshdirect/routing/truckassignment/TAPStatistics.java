package com.freshdirect.routing.truckassignment;

public class TAPStatistics {
	private int first;
	private int second;
	private int nonPreferred;
	private int missing;
	private int notAssigned;
	private int total;

	public TAPStatistics() {
		super();
		first = second = nonPreferred = missing = notAssigned = total = 0;
	}

	public int getFirst() {
		return first;
	}

	public int getSecond() {
		return second;
	}

	public int getNonPreferred() {
		return nonPreferred;
	}

	public int getMissing() {
		return missing;
	}

	public int getNotAssigned() {
		return notAssigned;
	}

	public int getTotal() {
		return total;
	}

	public void collect(Dispatch route) {
		total++;
		double preference = route.getPreference();
		if (Double.isNaN(preference))
			notAssigned++;
		else if (preference == Double.NEGATIVE_INFINITY)
			missing++;
		else if (preference == Double.POSITIVE_INFINITY)
			nonPreferred++;
		else if (preference == 0.)
			first++;
		else
			second++;
	}

	public String format() {
		return first + "," + second + "," + nonPreferred + "," + notAssigned + "," + missing + "," + total;
	}

	public void dump() {
		System.out.println("first choice: " + first);
		System.out.println("second choice: " + second);
		System.out.println("non-preferred: " + nonPreferred);
		System.out.println("missing truck: " + missing);
		System.out.println("not assigned route: " + notAssigned);
		System.out.println("total: " + total);
	}
}