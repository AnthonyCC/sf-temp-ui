package com.freshdirect.truckassignment;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import lpsolve.LpSolveException;

import com.freshdirect.analysis.db.util.AbstractQueryCallback;
import com.freshdirect.analysis.db.util.ConnectionInfo;
import com.freshdirect.analysis.db.util.DataRetrieval;
import com.freshdirect.analysis.db.util.QueryParam;

public class TruckAssignmentProblem implements Comparable<TruckAssignmentProblem> {
	static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

	static final int PREF_LIST_SIZE = 3;

	private static final long SOLVER_TIMEOUT = 120000l;

	private static class DispatchTruckFrequency {
		Dispatch dispatch;
		Truck truck;
		int frequency;

		public DispatchTruckFrequency(Dispatch dispatch, Truck truck, int frequency) {
			super();
			this.dispatch = dispatch;
			this.truck = truck;
			this.frequency = frequency;
		}
	}

	private static int toMinutes(Date date) {
		if (date == null)
			return 0;
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int minutes = 0;
		minutes += cal.get(Calendar.MINUTE);
		minutes += 60 * cal.get(Calendar.HOUR_OF_DAY);
		return minutes;
	}

	public static <K extends Comparable<K>, V extends Comparable<V>> List<K> sortByValue(final Map<K, V> m) {
		List<K> keys = new ArrayList<K>();
		keys.addAll(m.keySet());
		Collections.sort(keys, new Comparator<K>() {
			public int compare(K o1, K o2) {
				V v1 = m.get(o1);
				V v2 = m.get(o2);
				if (v1 == null) {
					return (v2 == null) ? 0 : 1;
				} else if (v2 == null) {
					return -1;
				} else {
					int i = v1.compareTo(v2);
					if (i == 0)
						return o1.compareTo(o2);
					else
						return i;
				}
			}
		});
		return keys;
	}

	public static void main(String[] args) throws ParseException, SQLException, IOException, LpSolveException {
		Date start = DATE_FORMAT.parse("2010-09-06");
		for (int i = 0; i < 70; i++) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(start);
			cal.add(Calendar.DAY_OF_YEAR, i);
			TruckAssignmentProblem tap = new TruckAssignmentProblem(cal.getTime());
			tap.loadTrucks();
			tap.dumpTrucks();
			tap.retrieveDispatches();
			tap.dumpEmployees();
			tap.dumpDispatches();
			tap.loadAssignmentStats();
			tap.assignByPreference();
			tap.eval();
			tap.assignAutomatically();
			tap.saveAssignmentStats();
			tap.eval();
			tap.saveStats();
			// tap.dumpDispatches();
		}
	}

	private Date today;
	private List<Truck> trucks;
	private Map<String, Truck> truckMap;
	private Map<String, Collection<TruckAssignmentStat>> truckAssignmentStats;
	private int nPlusOne;
	private List<Employee> employees;
	private Map<String, Employee> employeeMap;
	private List<Dispatch> dispatches;
	private Set<String> dispatchIds;
	private TAPStatistics statistics;

	public TruckAssignmentProblem(Date today) {
		this.today = today;
		trucks = new ArrayList<Truck>();
		truckMap = new LinkedHashMap<String, Truck>();
		truckAssignmentStats = new HashMap<String, Collection<TruckAssignmentStat>>();
		employees = new ArrayList<Employee>();
		employeeMap = new HashMap<String, Employee>();
		dispatches = new ArrayList<Dispatch>();
		dispatchIds = new HashSet<String>();
		statistics = new TAPStatistics();
	}

	@Override
	public int compareTo(TruckAssignmentProblem o) {
		if (statistics.getTotal() != o.statistics.getTotal())
			throw new IllegalStateException("two different truck assignment cannot be compared to each other");
		int i = statistics.getFirst() - o.statistics.getFirst();
		if (i != 0)
			return i;
		i = statistics.getSecond() - o.statistics.getSecond();
		if (i != 0)
			return i;
		i = o.statistics.getNotAssigned() - statistics.getNotAssigned();
		return i;
	}

	private void loadTrucks() throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader("trucks.txt"));
		// skip header
		if (reader.readLine() != null) {
			String line;
			while ((line = reader.readLine()) != null) {
				String[] split = line.split(",");
				if (split.length > 1) {
					Truck truck = Truck.newUnknownTruck(split[0]);
					truck.setInService(Boolean.parseBoolean(split[1]));
					trucks.add(truck);
					truckMap.put(truck.getId(), truck);
				}
			}
		}
	}

	private void retrieveDispatches() throws ParseException, SQLException {
		DataRetrieval retrieval = new DataRetrieval(ConnectionInfo.STANDBY);
		retrieval.setFetchSize(1000);
		retrieval.setQuery("SELECT d.dispatch_id, d.dispatch_date, d.zone, d.route, d.start_time, "
				+ "d.checkedin_time, dr.resource_id, p.max_time "
				+ "FROM TRANSP.dispatch d INNER JOIN TRANSP.dispatch_resource dr ON dr.dispatch_id = d.dispatch_id "
				+ "LEFT JOIN TRANSP.plan p ON d.plan_id = p.plan_id " + "WHERE dr.role = '001' AND d.dispatch_date = ?",
				new QueryParam[] { new QueryParam(Types.DATE, today) });
		retrieval.executeQuery(new AbstractQueryCallback() {
			@Override
			protected void rowImpl(ResultSet row) throws SQLException {
				String id = row.getString(1);
				Date date = row.getDate(2);
				String zone = row.getString(3);
				String area;
				if (zone != null && zone.length() > 0)
					area = zone.substring(0, 1);
				else
					area = "_";
				String route = row.getString(4);
				int start = toMinutes(row.getTimestamp(5));
				int end = toMinutes(row.getTimestamp(6));
				if (end < start)
					end += 24 * 60;
				String employeeId = row.getString(7);
				int plannedEnd = toMinutes(row.getTimestamp(8));
				if (plannedEnd == 0)
					plannedEnd = 24 * 60;
				else
					plannedEnd += start + 59;
				String baseId = id;
				int idSequence = 2;
				while (dispatchIds.contains(id)) {
					id = baseId + "-" + idSequence++;
				}
				Dispatch dispatch = new Dispatch(id);
				dispatch.setDate(date);
				dispatch.setRoute(route);
				dispatch.setZone(zone);
				dispatch.setArea(area);
				dispatch.setLeaves(start);
				dispatch.setNextAvailable(end);
				dispatch.setPlannedEnd(plannedEnd);
				dispatches.add(dispatch);
				dispatchIds.add(id);
				Employee employee;
				if (!employeeMap.containsKey(employeeId)) {
					employee = new Employee(employeeId);
					employeeMap.put(employeeId, employee);
					employees.add(employee);
				} else {
					employee = employeeMap.get(employeeId);
				}
				dispatch.setEmployee(employee);
			}

			@Override
			public void done() {
				super.done();
				Collections.sort(dispatches);
			}
		});
	}

	private void assignByPreference() throws LpSolveException {
		// determine current preferences based on assignment stats
		System.out.println("Determining truck preferences based on stats...");
		for (Employee employee : employees) {
			Collection<TruckAssignmentStat> stats = truckAssignmentStats.get(employee.getId());
			List<Truck> prefs = new ArrayList<Truck>();
			HashMap<Truck, Integer> coll = new HashMap<Truck, Integer>();
			if (stats != null) {
				for (TruckAssignmentStat stat : stats) {
					Truck truck = truckMap.get(stat.getTruckId());
					if (truck != null) {
						if (!coll.containsKey(truck))
							coll.put(truck, 0);
						coll.put(truck, coll.get(truck) + 1);
					}
				}
				prefs = sortByValue(coll);
				Collections.reverse(prefs);
				if (prefs.size() > PREF_LIST_SIZE)
					prefs = prefs.subList(0, PREF_LIST_SIZE);
			}
			LinkedHashMap<Truck, Integer> sorted = new LinkedHashMap<Truck, Integer>();
			for (Truck truck : prefs)
				sorted.put(truck, coll.get(truck));
			employee.setPreferences(sorted);
		}

		// set up index <--> id transformation
		System.out.println("Preparing truck ID index...");
		Map<String, Integer> toIndex = new HashMap<String, Integer>();
		Map<Integer, String> toId = new HashMap<Integer, String>();
		int index = 1;
		for (Truck truck : trucks) {
			toIndex.put(truck.getId(), index);
			toId.put(index, truck.getId());
			index++;
		}

		// use the simplex algorithm to find assignments based on preferences
		System.out.println("Setting up solver...");
		TruckAssignmentSolver solver = new TruckAssignmentSolver(PREF_LIST_SIZE, SOLVER_TIMEOUT);
		for (Dispatch dispatch : dispatches) {
			int start = dispatch.getLeaves();
			int end = dispatch.getNextAvailable();
			LinkedHashMap<Truck, Integer> preferences = dispatch.getEmployee().getPreferences();
			LinkedHashMap<Integer, Integer> indexed = new LinkedHashMap<Integer, Integer>();
			for (Entry<Truck, Integer> entry : preferences.entrySet())
				indexed.put(toIndex.get(entry.getKey().getId()), entry.getValue());
			solver.addRoute(new Route(dispatch, start, end, indexed, PREF_LIST_SIZE));
		}
		System.out.println("Starting solver...");
		long start = System.currentTimeMillis();
		solver.solve();
		System.out.println("Solution completed (" + (System.currentTimeMillis() - start) + "ms).");

		System.out.println("Writing back solution into the model...");
		for (Route route : solver.getRoutes()) {
			if (route.getSolution() != Route.NOT_FOUND) {
				int truckIndex = route.getPreferredTruck(route.getSolution());
				String truckId = toId.get(truckIndex);
				if (truckId != null)
					route.getDispatch().setTruck(truckMap.get(truckId));
			}
		}
		System.out.println("Model has been updated.");
		solver = null;
	}

	private void assignAutomatically() {
		System.out.println("Auto-assignment started...");
		long start = System.currentTimeMillis();

		Set<Dispatch> engaged = new HashSet<Dispatch>();
		Set<Dispatch> free = new HashSet<Dispatch>();
		Set<Truck> unused = new HashSet<Truck>(trucks);
		for (Dispatch dispatch : dispatches)
			if (dispatch.getTruck() != null) {
				engaged.add(dispatch);
				unused.remove(dispatch.getTruck());
			} else
				free.add(dispatch);

		List<DispatchTruckFrequency> tuples = new ArrayList<DispatchTruckFrequency>();
		while (!free.isEmpty()) {
			tuples.clear();
			for (Dispatch dispatch : free) {
				LinkedHashMap<Truck, Integer> preferences = dispatch.getEmployee().getPreferences();
				TRUCK: for (Truck truck : preferences.keySet()) {
					for (Dispatch engDispatch : engaged)
						if (engDispatch.getTruck().equals(truck) && engDispatch.collide(dispatch))
							continue TRUCK;

					tuples.add(new DispatchTruckFrequency(dispatch, truck, preferences.get(truck)));
					break TRUCK;
				}
			}
			// reverse sort
			Collections.sort(tuples, new Comparator<DispatchTruckFrequency>() {
				@Override
				public int compare(DispatchTruckFrequency o1, DispatchTruckFrequency o2) {
					// !!! reverse sort !!!
					int i = o2.frequency - o1.frequency;
					if (i != 0)
						return i;
					i = o2.truck.compareTo(o1.truck);
					if (i != 0)
						return i;
					return o2.dispatch.getEmployee().getId().compareTo(o1.dispatch.getEmployee().getId());
				}
			});
			Dispatch dispatch;
			Truck truck;
			SELECT: {
				if (!tuples.isEmpty()) {
					DispatchTruckFrequency dtf = tuples.get(0);
					dispatch = dtf.dispatch;
					truck = dtf.truck;
				} else {
					List<Dispatch> freeDispatches = new ArrayList<Dispatch>(free);
					Collections.sort(freeDispatches, new Comparator<Dispatch>() {
						@Override
						public int compare(Dispatch o1, Dispatch o2) {
							return o1.getEmployee().getId().compareTo(o2.getEmployee().getId());
						}
					});
					// find the first not colliding
					for (Dispatch d : freeDispatches)
						COLLISION: for (Dispatch e : engaged)
							if (!d.collide(e)) {
								for (Dispatch f : engaged)
									if (!f.getId().equals(e.getId()) && e.getTruck().equals(f.getTruck()) &&
											d.collide(f))
										continue COLLISION;
								dispatch = d;
								truck = e.getTruck();
								break SELECT;
							}

					dispatch = freeDispatches.get(0);
					if (!unused.isEmpty()) {
						truck = unused.iterator().next(); // practically random
						unused.remove(truck);
					} else {
						truck = Truck.newVirtualTruck();
						trucks.add(truck);
						truckMap.put(truck.getId(), truck);
					}
				}
			}
			dispatch.setTruck(truck);
			engaged.add(dispatch);
			free.remove(dispatch);
		}

		System.out.println("Auto-assignment took " + (System.currentTimeMillis() - start) + "ms");
	}

	@SuppressWarnings("unused")
	private void assignAutomaticallyOld() {
		System.out.println("Auto-assignment started...");
		long start = System.currentTimeMillis();
		Set<String> engaged = new HashSet<String>();

		for (int tick = 0; tick < 24 * 60; tick++) {
			engaged.clear();
			for (Dispatch dispatch : dispatches) {
				if (dispatch.getTruck() != null && dispatch.collide(tick))
					engaged.add(dispatch.getTruck().getId());
			}

			OUTER: for (Dispatch dispatch : dispatches) {
				if (!dispatch.collide(tick))
					continue OUTER;
				if (dispatch.getTruck() != null)
					continue OUTER;
				for (Truck truck : trucks) {
					if (truck.isInService() && !engaged.contains(truck.getId())) {
						dispatch.setTruck(truck);
						engaged.add(truck.getId());
						continue OUTER;
					}
				}

				// not enough trucks available constructing virtual trucks
				Truck truck = Truck.newVirtualTruck();
				trucks.add(truck);
				truckMap.put(truck.getId(), truck);
				dispatch.setTruck(truck);
				engaged.add(truck.getId());
			}
		}
		System.out.println("Auto-assignment took " + (System.currentTimeMillis() - start) + "ms");
	}

	private void saveAssignmentStats() throws FileNotFoundException {
		// delete all n + 1 items
		for (String employeeId : truckAssignmentStats.keySet()) {
			Iterator<TruckAssignmentStat> it = truckAssignmentStats.get(employeeId).iterator();
			while (it.hasNext()) {
				TruckAssignmentStat stat = it.next();
				if (stat.getN() == nPlusOne)
					it.remove();
			}
		}

		for (Dispatch dispatch : dispatches) {
			String employeeId = dispatch.getEmployee().getId();
			String truckId = dispatch.getTruck().getId();
			if (!truckAssignmentStats.containsKey(employeeId))
				truckAssignmentStats.put(employeeId, new ArrayList<TruckAssignmentStat>());
			Collection<TruckAssignmentStat> stats = truckAssignmentStats.get(employeeId);
			stats.add(new TruckAssignmentStat(nPlusOne, employeeId, truckId, today));
		}

		FileOutputStream fos = new FileOutputStream("assignmentStats.txt");
		PrintWriter writer = new PrintWriter(fos);
		writer.println("N,EMPLOYEE_ID,TRUCK_ID,DATE");
		for (Collection<TruckAssignmentStat> stats : truckAssignmentStats.values()) {
			for (TruckAssignmentStat stat : stats)
				writer.println(stat.format());
		}
		writer.flush();
		writer.close();
		writer = null;
		fos = null;
	}

	private void loadAssignmentStats() throws IOException, ParseException {
		BufferedReader reader = new BufferedReader(new FileReader("assignmentStats.txt"));
		Date latest = DATE_FORMAT.parse("1970-01-01");
		int n = 0;
		// skip header
		if (reader.readLine() != null) {
			String line;
			while ((line = reader.readLine()) != null) {
				TruckAssignmentStat stat = TruckAssignmentStat.parse(line);
				String employeeId = stat.getEmployeeId();
				if (!truckAssignmentStats.containsKey(employeeId))
					truckAssignmentStats.put(employeeId, new ArrayList<TruckAssignmentStat>());
				Collection<TruckAssignmentStat> stats = truckAssignmentStats.get(employeeId);
				stats.add(stat);
				if (stat.getDate().after(latest)) {
					latest = stat.getDate();
					n = stat.getN();
				}
			}
		}
		nPlusOne = n % 100 + 1;
	}

	private void eval() {
		statistics = new TAPStatistics();
		for (Dispatch dispatch : dispatches) {
			statistics.collect(dispatch);
		}
		statistics.dump();
	}

	private void saveStats() throws IOException {
		FileWriter writer = new FileWriter("stats.txt", true);
		PrintWriter print = new PrintWriter(writer);
		print.println(statistics.format());
		print.close();
	}

	private void dumpDispatches() {
		for (Dispatch route : dispatches) {
			System.out.println(route);
		}
	}

	private void dumpEmployees() {
		System.out.println("Employees: " + employees.size());
		for (Employee employee : employees)
			System.out.println(employee);
	}

	private void dumpTrucks() {
		System.out.println("Trucks: " + trucks.size());
		for (Truck truck : trucks)
			System.out.println(truck);
	}
}
