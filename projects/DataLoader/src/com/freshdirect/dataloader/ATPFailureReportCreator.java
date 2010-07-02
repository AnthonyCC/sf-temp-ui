package com.freshdirect.dataloader;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ATPFailureReportCreator extends DBReportCreator {

	public static void main(String[] args) throws Exception {

		if (args.length != 2) {
			System.out.println("USAGE: java ATPFailureReportCreator MM-dd-yyyy /path/where/to/store/files");
			System.exit(0);
		}

		Date day = SF.parse(args[0]);
		File f = new File(args[1]);
		ATPFailureReportCreator rc = new ATPFailureReportCreator();
		rc.createReport(day, f);
	}

	private void createReport(Date day, File f) throws SQLException, IOException {
		Connection conn = this.getConnection();
		try {
			new ATPFailureReport(day, new File(f, "ATP_Material_Unavailability_" + SF.format(day) + ".tsv")).run(conn);
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
	}

	private static class ATPFailureReport extends Report {

		public ATPFailureReport(Date day, File f) {
			super(day, f);
		}

		@Override
        protected void processRow(ResultSet rs) throws SQLException {

			sb.append(rs.getDate("DELIVERY_DATE")).append("\t");
			sb.append(rs.getTimestamp("FIRST_CHECK")).append("\t");
			sb.append(rs.getTimestamp("LAST_CHECK")).append("\t");
			sb.append(rs.getInt("TOTAL_CHECKS")).append("\t");
			sb.append(rs.getString("MATERIAL_NUMBER")).append("\t");
			sb.append(rs.getString("DESCRIPTION")).append("\t");
			sb.append("\n");

		}

		@Override
        protected String getQuery() {
			return "select af.requested_date as delivery_date, min(af.timestamp) first_check, max(af.timestamp) last_check, sum(1) total_checks, af.material_sap_id as material_number, m.description "
				+ "from erps.atp_failure af, erps.material m "
				+ "where trunc(af.timestamp) = ? "
				+ "and m.sap_id=af.material_sap_id and m.version=(select max(version) from erps.material where sap_id=af.material_sap_id) "
				+ "and af.quantity_avail = 0 "
				+ "group by af.requested_date, af.material_sap_id, m.description "
				+ "order by max(af.timestamp) ";
		}

		@Override
        protected Object[] getParams() {
			Object[] params = new Object[1];
			params[0] = new java.sql.Date(day.getTime());
			return params;
		}

		@Override
        protected List<String> getHeaders() {
			List<String> lst = new ArrayList<String>();

			lst.add("delivery_date");
			lst.add("time_of_first_check");
			lst.add("time_of_last_check");
			lst.add("total_checks");
			lst.add("material_number");
			lst.add("description");

			return lst;
		}
	}
}
