package com.freshdirect.dataloader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public abstract class DBReportCreator {
	
	protected static final SimpleDateFormat SF = new SimpleDateFormat("MM-dd-yyyy");

	protected Connection getConnection() throws SQLException {
		DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
		//		PRD DB URL: db1.nyc2.freshdirect.com:1521:DBSTO01
		//		INT DB URL: db1.dev.nyc1.freshdirect.com:1521:DBINT01
		
		Connection c =
			DriverManager.getConnection("Jdbc:oracle:thin:@(DESCRIPTION =(ADDRESS_LIST =(ADDRESS = (PROTOCOL = TCP)(HOST = nyc1dbcl01-vip02.nyc1.freshdirect.com)(PORT = 1521))(LOAD_BALANCE = yes))(CONNECT_DATA =(SERVICE_NAME = devint)(SRVR = DEDICATED)))", "fdstore_prda", "fdstore_prda");
			//DriverManager.getConnection("Jdbc:oracle:thin:@(DESCRIPTION=(ADDRESS=(PROTOCOL=TCP)(HOST=nyc2stdb01-vip.nyc2.freshdirect.com)(PORT=1521))(ADDRESS=(PROTOCOL=TCP)(HOST=nyc2stdb02-vip.nyc2.freshdirect.com)(PORT=1521))(CONNECT_DATA=(SERVER=DEDICATED)(SERVICE_NAME=dbsto_prod)(failover_mode=(type=session)(method=basic)(retries=20))))", "appdev", "readn0wrt");
		return c;
	}

	protected abstract static class Report {
		protected final Date day;
		private final File file;
		protected final StringBuffer sb = new StringBuffer(32768);

		public Report(Date day, File file) {
			this.day = day;
			this.file = file;
			//append the header row
			for (Iterator i = this.getHeaders().iterator(); i.hasNext();) {
				sb.append(i.next()).append("\t");
			}
			sb.append("\n");
		}

		public void run(Connection conn) throws SQLException, IOException {
			PreparedStatement ps = conn.prepareStatement(this.getQuery());
			Object[] params = this.getParams();
			for (int i = 0; i < params.length; i++) {
				ps.setObject(i + 1, params[i]);
			}
			ResultSet rs = ps.executeQuery();
			//now take this result set and convert it to a tab delimited file
			while (rs.next()) {
				this.processRow(rs);
			}
			rs.close();
			ps.close();
			if(getFooter() != null && getFooter().length() > 0) {
				sb.append(getFooter());
				sb.append("\n");
			}
			this.write();
		}

		private void write() throws IOException {
			FileOutputStream ofs = new FileOutputStream(file);
			BufferedWriter bfw = new BufferedWriter(new OutputStreamWriter(ofs));
			bfw.write(sb.toString());
			bfw.flush();
			bfw.close();
		}

		protected abstract void processRow(ResultSet rs) throws SQLException;
		protected abstract String getQuery();
		protected abstract Object[] getParams();
		protected abstract List getHeaders();
		protected String getFooter(){
			return null;
		}

	}

}
