package com.freshdirect.cms._import.dao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.sql.DataSource;

import org.apache.log4j.Logger;


/**
 * Class provides API for basic CMS infrastructure related operations
 * such as creating / deleting / flushing tables, etc.
 * 
 * @author segabor
 *
 */
public class CMSInfrastructureDao {
	private static final Logger LOGGER = Logger.getLogger(CMSInfrastructureDao.class);
	
	private static final Pattern RE_EMPTY_LINE = Pattern.compile("^\\s*$");
	private static final Pattern RE_COMMENT_LINE = Pattern.compile("^\\s*--");
	private static final Pattern RE_STATEMENT_CLOSE = Pattern.compile(";\\s*$");

	private DataSource dataSource;

	public CMSInfrastructureDao(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	public DataSource getDataSource() {
		return dataSource;
	}

	public Connection getConnection() throws SQLException {
		return dataSource.getConnection();
	}

	/**
	 * Drop contents of the following tables
	 * 
	 * CONTENTNODE
	 * ATTRIBUTE
	 * RELATIONSHIP
	 */
	public void flushStoreTables() {
		List<String> cmds = new ArrayList<String>();

		cmds.add("delete from RELATIONSHIP");
		cmds.add("delete from ATTRIBUTE");
		cmds.add("delete from CONTENTNODE");
		
		safeExecuteCommands(cmds, false);
	}


	public void dropStoreIndices() {
		List<String> cmds = new ArrayList<String>();

		// ATTRIBUTE
		cmds.add("alter table ATTRIBUTE drop constraint PK_ATTRIBUTE");
		cmds.add("alter table ATTRIBUTE drop constraint CNODE_ATTR_FK");
		
		// RELATIONSHIP
		cmds.add("alter table RELATIONSHIP drop constraint PK_RELATIONSHIP");
		cmds.add("alter table RELATIONSHIP drop constraint CNODE_RSHIPCHILD_FK");
		cmds.add("alter table RELATIONSHIP drop constraint CNODE_RSHIPPARENT_FK");
		
		// CONTENTNODE
		cmds.add("alter table contentnode drop constraint PK_CONTENTNODE");
		
		safeExecuteCommands(cmds, true);
	}

	public void createStoreIndicesPhaseOne() {
		List<String> cmds = new ArrayList<String>();

		// CONTENTNODE
		cmds.add("alter table contentnode add constraint PK_CONTENTNODE PRIMARY KEY (\"ID\") ENABLE");

		// RELATIONSHIP
		cmds.add("alter table RELATIONSHIP add constraint PK_RELATIONSHIP PRIMARY KEY (\"ID\") ENABLE");
		// cmds.add("alter table RELATIONSHIP add constraint CNODE_RSHIPCHILD_FK FOREIGN KEY (\"CHILD_CONTENTNODE_ID\") REFERENCES \"CONTENTNODE\" (\"ID\") ENABLE");
		// cmds.add("alter table RELATIONSHIP add constraint CNODE_RSHIPPARENT_FK FOREIGN KEY (\"PARENT_CONTENTNODE_ID\") REFERENCES \"CONTENTNODE\" (\"ID\") ENABLE");
		
		// ATTRIBUTE
		cmds.add("alter table ATTRIBUTE add constraint PK_ATTRIBUTE primary key (\"ID\") enable");
		// cmds.add("alter table ATTRIBUTE add constraint CNODE_ATTR_FK FOREIGN KEY (\"CONTENTNODE_ID\") REFERENCES \"CONTENTNODE\" (\"ID\") ENABLE");
		
		safeExecuteCommands(cmds, true);
	}



	public void createStoreIndicesPhaseTwo() {
		List<String> cmds = new ArrayList<String>();

		// CONTENTNODE
		// cmds.add("alter table contentnode add constraint PK_CONTENTNODE PRIMARY KEY (\"ID\") ENABLE");

		// RELATIONSHIP
		// cmds.add("alter table RELATIONSHIP add constraint PK_RELATIONSHIP PRIMARY KEY (\"ID\") ENABLE");
		cmds.add("alter table RELATIONSHIP add constraint CNODE_RSHIPCHILD_FK FOREIGN KEY (\"CHILD_CONTENTNODE_ID\") REFERENCES \"CONTENTNODE\" (\"ID\") ENABLE");
		cmds.add("alter table RELATIONSHIP add constraint CNODE_RSHIPPARENT_FK FOREIGN KEY (\"PARENT_CONTENTNODE_ID\") REFERENCES \"CONTENTNODE\" (\"ID\") ENABLE");
		
		// ATTRIBUTE
		// cmds.add(conn, "alter table ATTRIBUTE add constraint PK_ATTRIBUTE primary key (\"ID\") enable");
		cmds.add("alter table ATTRIBUTE add constraint CNODE_ATTR_FK FOREIGN KEY (\"CONTENTNODE_ID\") REFERENCES \"CONTENTNODE\" (\"ID\") ENABLE");

		
		safeExecuteCommands(cmds, true);
	}



	public void analyzeStoreTables() {
		List<String> cmds = new ArrayList<String>();
		cmds.add("analyze table RELATIONSHIP compute statistics");
		cmds.add("analyze table CONTENTNODE compute statistics");
		cmds.add("analyze table ATTRIBUTE compute statistics");

		safeExecuteCommands(cmds, true);
	}



	private boolean safeExecute(Connection conn, String cmd) {
		boolean result = false;
		boolean retry = true;
		
		while (retry) {
			Statement stmt = null;
			try {
				retry = false;

				if (cmd.startsWith("call")) {
					LOGGER.debug("Calling stored proc: " + cmd);
					stmt = conn.prepareCall(cmd);
					((CallableStatement)stmt).execute();
				} else {
					stmt = conn.createStatement();
					stmt.execute(cmd);
				}
				
				result = true;
			} catch (SQLException e) {
				if (e.getMessage().startsWith("ORA-00054")) {
					retry = true;
					LOGGER.debug("Resource is busy, retrying op " + cmd);
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e1) {
					}
				} else {
					LOGGER.error(e);
				}
			} finally {
				if (stmt != null) {
					try {
						stmt.close();
					} catch (SQLException e) {
						LOGGER.error(e);
					}
				}
			}
		}
		
		return result;
	}



	protected void safeExecuteCommands(List<String> commands, boolean autoCommit) {
		Connection conn = null;

		try {
			conn = getConnection();
			conn.setAutoCommit(autoCommit);

			for (String cmd : commands) {
				if (!safeExecute(conn, cmd)) {
					LOGGER.error("Failed to execute " + cmd);
					break;
				}
			}

			if (!autoCommit)
				conn.commit();
		} catch (SQLException e) {
			LOGGER.error(e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					LOGGER.error(e);
				}
			}
		}
	}
	
	
	
	public boolean runScript(Reader aReader) {
		Connection conn = null;
		try {
			conn = getConnection();
			conn.setAutoCommit(true);
			
			
			BufferedReader reader = new BufferedReader(aReader);
			
			String line;
			StringBuilder command = new StringBuilder(512);
			
			while ( (line = reader.readLine()) != null ) {
				if (RE_EMPTY_LINE.matcher(line).find() || RE_COMMENT_LINE.matcher(line).find()) {
					// skip empty or commented lines
					LOGGER.debug("Skipped line: " + line);
				} else {
					if (command.length() != 0)
						command.append(' ');
					Matcher matcher = RE_STATEMENT_CLOSE.matcher(line);
					if (matcher.find()) {
						line = matcher.replaceFirst("");
						command.append(line);						
						LOGGER.info("Executing: " + command);
						boolean success = safeExecute(conn, command.toString());
						if (!success)
							return false;
						command = new StringBuilder(512);
					} else
						command.append(line);						
				}
			}
		} catch (IOException e) {
			LOGGER.error(e);
			return false;
		} catch (SQLException e) {
			LOGGER.error(e);
			return false;
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					LOGGER.error(e);
				}
			}
		}
		return true;
	}
	
	
	
	public void flushMediaTable() {
		List<String> cmds = new ArrayList<String>();
		cmds.add("delete from media");

		safeExecuteCommands(cmds, true);
	}


	
	public void dropMediaIndex() {
		List<String> cmds = new ArrayList<String>();
		cmds.add("alter table MEDIA drop constraint \"UNQ_MEDIA_URI\"");

		safeExecuteCommands(cmds, true);
	}

	public void addMediaIndex() {
		List<String> cmds = new ArrayList<String>();
		cmds.add("alter table MEDIA add constraint \"UNQ_MEDIA_URI\" UNIQUE (\"URI\") ENABLE");

		safeExecuteCommands(cmds, true);
	}



	public void fixSystemSequenceValue() {
		Connection conn = null;
		java.sql.PreparedStatement stmt = null;

		int curval = 0;
		int maxval = 0;
		
		try {
			conn = getConnection();
			conn.setAutoCommit(true);

			// get nextval
			stmt = conn.prepareStatement("select system_seq.nextval from dual");
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				curval = rs.getInt(1);
			}
			stmt.close();


			// look for max ID value
			final String selectMaxIdValueQuery = 
				"select max(ID) from ( \n"
				+ "		  SELECT MAX(ID) AS ID FROM ATTRIBUTE \n"
				+ "		  UNION \n"
				+ "		  SELECT MAX(ID) AS ID FROM RELATIONSHIP \n"
				+ "		  UNION \n"
				+ "		  SELECT MAX(ID) AS ID FROM MEDIA WHERE ID NOT LIKE '/%' \n"
				+ "		)";
			stmt = conn.prepareStatement(selectMaxIdValueQuery);
			rs = stmt.executeQuery();
			if (rs.next()) {
				maxval = rs.getInt(1);
			}
			stmt.close();

			
			// update system seq
			conn.setAutoCommit(false);
			Statement stmt2 = conn.createStatement();
			stmt2.addBatch("alter sequence system_seq increment by "+ (maxval-curval));
			stmt2.addBatch("select system_seq.nextval from dual");
			stmt2.addBatch("alter sequence system_seq increment by 1");
			conn.commit();
		} catch (SQLException e) {
			LOGGER.error(e);
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					LOGGER.error(e);
				}
			}

			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					LOGGER.error(e);
				}
			}
		}
	}
}
