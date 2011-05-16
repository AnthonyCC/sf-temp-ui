package com.freshdirect.truckassignment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import lpsolve.AbortListener;
import lpsolve.LpSolve;
import lpsolve.LpSolveException;
import lpsolve.VersionInfo;

public class TruckAssignmentSolver {
    
    
    private final static Map<Integer, String> CODES = new HashMap<Integer, String>();
    static {
        CODES.put(-2, "NO MEMORY");
        CODES.put(0, "OPTIMAL");
        CODES.put(1, "SUBOPTIMAL");
        CODES.put(2, "INFEASIBLE");
        CODES.put(3, "UNBOUNDED");
        CODES.put(4, "DEGENERATE");
        CODES.put(5, "NUMFAILURE");
        CODES.put(6, "USERABORT");
        CODES.put(7, "TIMEOUT");
        CODES.put(9, "PRESOLVED");
        CODES.put(10, "PROCFAIL");
        CODES.put(11, "PROCBREAK");
        CODES.put(12, "FEASFOUND");
        CODES.put(13, "NOFEASFOUND");
    }
    
    
	private final long timeout;
	private long startTime;

	private int maxPreferredTrucks;
	private double[] costs;
	private List<Route> routes;
	private int normalizer;
	private double compressionRate = 5.0; 

	private final static double NEARLY_ONE = 1. - (1e-10);

	private LpSolve solver;

	public TruckAssignmentSolver(int maxPrefferredTrucks, long timeout) {
		this.maxPreferredTrucks = maxPrefferredTrucks;
		this.timeout = timeout;
		this.costs = new double[maxPrefferredTrucks + 1];
		for (int i = 0; i < maxPrefferredTrucks; i++)
			this.costs[i] = 1.0;
		this.costs[maxPrefferredTrucks] = 100.0;
		this.routes = new ArrayList<Route>();
		this.normalizer = 1;
	}

	public void addRoute(Route r) {
		routes.add(r);
		for (int i = 0; i < maxPreferredTrucks; i++) {
			int measure = r.getPreferenceMeasure(i);
			if (measure > normalizer)
				normalizer = measure;
		}
	}

	public List<Route> getRoutes() {
		return routes;
	}

	public final void setupSolver() throws LpSolveException {
		int variableNumber = routes.size() * (maxPreferredTrucks + 1);
		System.out.println("routes :" + routes.size() + ", variables:" + variableNumber);
		solver = LpSolve.makeLp(0, variableNumber);
		int colNum = 1;
		double a = (compressionRate - 1) / Math.log(normalizer);
		for (int i = 0; i < routes.size(); i++) {
			for (int j = 0; j < maxPreferredTrucks + 1; j++) {
				solver.setColName(colNum, "x" + i + "_" + j);
				int measure = routes.get(i).getPreferenceMeasure(j);
				double weight = normalizer > 1 ? a * Math.log(measure) + 1. : 1.;
				solver.setObj(colNum, costs[j] / weight);
				if (getColNum(i, j) != colNum) {
					throw new RuntimeException("[" + i + ',' + j + "]=" + colNum + " was " + getColNum(i, j));
				}
				colNum++;
			}
		}
		for (int i = 0; i < variableNumber; i++) {
			solver.setBinary(i + 1, true);
		}

		{
			double[] row = new double[maxPreferredTrucks + 1];
			Arrays.fill(row, 1.0);
			int[] colNo = new int[maxPreferredTrucks + 1];
			rowCount = 1;
			for (int i = 0; i < routes.size(); i++) {
				for (int j = 0; j < maxPreferredTrucks + 1; j++) {
					colNo[j] = getColNum(i, j);
				}
				solver.addConstraintex(maxPreferredTrucks + 1, row, colNo, LpSolve.EQ, 1.0);
				// route i has assigned truck ...
				solver.setRowName(rowCount, "r"+i+"_truck");
				rowCount++;
			}
		}

		// detect trivial variables
		for (int i = 0 ; i < routes.size(); i++) {
		    setVariableToZeroWhereNoPreferredTruck(solver, i, routes.get(i));
		}

		// detect if two route has matching truck number, in that case we have to put constraint in the model
		for (int i = 0; i < routes.size(); i++) {
			for (int j = i + 1; j < routes.size(); j++) {
				Route r1 = routes.get(i);
				Route r2 = routes.get(j);
				if (r1.interlap(r2)) {
					calculateTruckCollisions(solver, i, r1, j, r2);
				}
			}
		}
	}

	public void solve() throws LpSolveException {
		VersionInfo lpSolveVersion = LpSolve.lpSolveVersion();
		System.out.println("lp solve " + lpSolveVersion.getMajorversion() + '.' + lpSolveVersion.getMinorversion() + '.'
				+ lpSolveVersion.getRelease() + '.' + lpSolveVersion.getBuild());

		if (solver == null) {
			setupSolver();
		}

		AbortListener abortfunc = new AbortListener() {
			int i = 0;

			public boolean abortfunc(LpSolve problem, Object handle) {
				if (++i % 100 == 0)
					System.out.print(".");
				if (i % 8000 == 0)
					System.out.println();
				if ((System.currentTimeMillis() - startTime) > timeout) {
					if (i % 8000 != 0)
						System.out.println();
					System.out.println("Timed out");
					return true;
				}
				return false;
			}
		};
		solver.putAbortfunc(abortfunc, 1);

		{
			startTime = System.currentTimeMillis();
			int solution = solver.solve();
			long elapsedTime = System.currentTimeMillis() - startTime;
			System.out.println("solution found:" + CODES.get(solution)+'('+solution+')' + ", in " + elapsedTime + " ms");
			solver.printLp();
		}
		double[] var = solver.getPtrVariables();
		for (int i = 0; i < routes.size(); i++) {
			for (int j = 0; j < maxPreferredTrucks + 1; j++) {
				int pos = getColNum(i, j) - 1;
				if (var[pos] > NEARLY_ONE) {
					routes.get(i).setSolution(j);
				}
			}
		}
		// for (int i = 0; i < var.length; i++) {
		// System.out.println("x[" + i + "]=" + var[i]);
		// }

		for (Route r : routes) {
			System.out.println(r + " -> " + r.getSolutionAsString());
		}

	}

	int rowCount ;
	
	private int getColNum(int route, int pref) {
		return 1 + (route * (maxPreferredTrucks + 1)) + pref;
	}
	
	private void setVariableToZeroWhereNoPreferredTruck(LpSolve solver, int routeId, Route r) throws LpSolveException {
	    for (int i = 0; i < maxPreferredTrucks; i++) {
                int t1 = r.getPreferredTruck(i);
                if (t1 == 0) {
                    solver.addConstraintex(1, new double[] { 1.0 }, new int[] { getColNum(routeId, i) }, LpSolve.EQ, 0.0);
                    solver.setRowName(rowCount, "z_"+routeId+'_'+i);
                    rowCount++;
                }
	    }
	}
	
        private void calculateTruckCollisions(LpSolve solver, int route1, Route r1, int route2, Route r2) throws LpSolveException {
            for (int i = 0; i < maxPreferredTrucks; i++) {
                int t1 = r1.getPreferredTruck(i);
                if (t1 != 0) {
                    // there is a preferred truck
                    int pos = r2.getPreferredTruckPosition(t1);
                    if (pos != -1) {
                        // x(route1, i) and x(route2, pos) both cant be 1 in the
                        // same time
                        int[] cols = { getColNum(route1, i), getColNum(route2, pos) };
                        double[] row = { 1.0, 1.0 };
                        solver.addConstraintex(2, row, cols, LpSolve.LE, 1.0);
                        solver.setRowName(rowCount, "o_" + route1 + '_' + i + '_' + route2 + '_' + pos);
                        rowCount++;
                    }
                }
            }
        }
}
