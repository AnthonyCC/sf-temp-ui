/**
 * @author ekracoff
 * Created on Jun 7, 2005*/

package com.freshdirect.cms.publish;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.freshdirect.cms.CmsRuntimeException;

/**
 * Publish task that performs a SQL query and stores the results in a
 * delimited file. The first row of the file will have the field names
 * (based on JDBC meta-data).
 * 
 * @TODO Change class name to something better (ExportQueryTask?)
 */
public class StoreExportTask implements PublishTask {

	private final DataSource dataSource;
	private final String query;
	private final String destinationPath;
	private final String delimiter;

	/**
	 * @param dataSource data source to perform query on
	 * @param query SQL query to run
	 * @param destinationPath relative path of file
	 * @param delimiter field delimiter
	 */
	public StoreExportTask(DataSource dataSource, String query, String destinationPath, String delimiter) {
		this.dataSource = dataSource;
		this.query = query;
		this.destinationPath = destinationPath;
		this.delimiter = delimiter;
	}

	public void execute(Publish publish) {
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			String contents = executeQuery(this.query, this.delimiter, conn);

			File file = new File(publish.getPath(), destinationPath);
			createParentDirectory(file);

			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			writer.write(contents);
			writer.flush();
			writer.close();

		} catch (SQLException e) {
			throw new CmsRuntimeException(e);
		} catch (IOException e) {
			throw new CmsRuntimeException(e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e1) {
					throw new CmsRuntimeException(e1);
				}
			}
		}
	}

	public String getComment() {
		return "Generating store export file: "+this.destinationPath;
	}

	private String executeQuery(String query, String delimiter, Connection conn) throws SQLException {
		StringBuffer buffer = new StringBuffer();
		PreparedStatement ps = conn.prepareStatement(query);
		ResultSet rs = ps.executeQuery();

		ResultSetMetaData meta = rs.getMetaData();
		int cols = meta.getColumnCount();

		for (int c = 1; c <= cols; c++) {
			buffer.append(meta.getColumnName(c));
			if (c != cols) {
				buffer.append(delimiter);
			}
		}
		buffer.append("\n");

		while (rs.next()) {
			for (int c = 1; c <= cols; c++) {
				buffer.append(rs.getString(c));
				if (c != cols) {
					buffer.append(delimiter);
				}
			}
			buffer.append("\n");
		}

		rs.close();
		ps.close();

		return buffer.toString();
	}

	private void createParentDirectory(File file) {
		File directory = file.getParentFile();
		if (directory != null && !directory.exists()) {
			directory.mkdirs();
		}
	}

}