package com.freshdirect.truckassignment;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;

import lpsolve.LpSolveException;

public class TestRun {

    /**
     * @param args
     * @throws ParseException 
     * @throws LpSolveException 
     * @throws SQLException 
     * @throws IOException 
     */
    public static void main(String[] args) throws ParseException, IOException, SQLException, LpSolveException {
        if (args.length<3) {
            System.out.println("TestRun <trucks file> <assignment stats> <dispatch file>");
            System.exit(1);
        }
        
        TruckAssignmentProblem t = new TruckAssignmentProblem(null);
        t.setTrucksFile(args[0]);
        t.setAssignmentStatsFile(args[1]);
        t.setDispatchFile(args[2]);
        t.fullCalculation();
    }

}
