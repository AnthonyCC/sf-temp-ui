package com.freshdirect.cms.si.dao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

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
	
	DataSource dataSource;

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
	 * 
	 * @throws SQLException
	 */
	public void flushCMSTables() {
		Connection conn = null;
		Statement stmt = null;
		
		try {
			conn = getConnection();
			
			conn.setAutoCommit(false);

			stmt = conn.createStatement();

			stmt.addBatch("delete /*+full(RELATIONSHIP)*/ from RELATIONSHIP");
			stmt.addBatch("delete /*+full(ATTRIBUTE)*/ from ATTRIBUTE");
			stmt.addBatch("delete /*+full(CONTENTNODE)*/ from CONTENTNODE");

			stmt.executeBatch();
			
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


	public void dropIndices() {
		Connection conn = null;
		Statement stmt = null;

		try {
			conn = getConnection();
			conn.setAutoCommit(true);
			
			// ATTRIBUTE
			safeExecute(conn, "alter table ATTRIBUTE drop constraint PK_ATTRIBUTE");
			safeExecute(conn, "alter table ATTRIBUTE drop constraint CNODE_ATTR_FK");
			
			// RELATIONSHIP
			safeExecute(conn, "alter table RELATIONSHIP drop constraint PK_RELATIONSHIP");
			safeExecute(conn, "alter table RELATIONSHIP drop constraint CNODE_RSHIPCHILD_FK");
			safeExecute(conn, "alter table RELATIONSHIP drop constraint CNODE_RSHIPPARENT_FK");
			
			// CONTENTNODE
			safeExecute(conn, "alter table contentnode drop constraint PK_CONTENTNODE");

			conn.close();
		} catch (SQLException e) {
			LOGGER.error(e);
		} finally {
			if (stmt != null) {
				try {
					stmt = conn.createStatement();
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

	public void createIndicesPhaseOne() {
		Connection conn = null;
		Statement stmt = null;

		try {
			conn = getConnection();
			conn.setAutoCommit(true);
			
			// CONTENTNODE
			safeExecute(conn, "alter table contentnode add constraint PK_CONTENTNODE PRIMARY KEY (\"ID\") ENABLE");

			// RELATIONSHIP
			safeExecute(conn, "alter table RELATIONSHIP add constraint PK_RELATIONSHIP PRIMARY KEY (\"ID\") ENABLE");
			// safeExecute(conn, "alter table RELATIONSHIP add constraint CNODE_RSHIPCHILD_FK FOREIGN KEY (\"CHILD_CONTENTNODE_ID\") REFERENCES \"CONTENTNODE\" (\"ID\") ENABLE");
			// safeExecute(conn, "alter table RELATIONSHIP add constraint CNODE_RSHIPPARENT_FK FOREIGN KEY (\"PARENT_CONTENTNODE_ID\") REFERENCES \"CONTENTNODE\" (\"ID\") ENABLE");
			
			// ATTRIBUTE
			safeExecute(conn, "alter table ATTRIBUTE add constraint PK_ATTRIBUTE primary key (\"ID\") enable");
			// safeExecute(conn, "alter table ATTRIBUTE add constraint CNODE_ATTR_FK FOREIGN KEY (\"CONTENTNODE_ID\") REFERENCES \"CONTENTNODE\" (\"ID\") ENABLE");

			conn.close();
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



	public void createIndicesPhaseTwo() {
		Connection conn = null;
		Statement stmt = null;

		try {
			conn = getConnection();
			conn.setAutoCommit(true);
			
			// CONTENTNODE
			// safeExecute(conn, "alter table contentnode add constraint PK_CONTENTNODE PRIMARY KEY (\"ID\") ENABLE");

			// RELATIONSHIP
			// safeExecute(conn, "alter table RELATIONSHIP add constraint PK_RELATIONSHIP PRIMARY KEY (\"ID\") ENABLE");
			safeExecute(conn, "alter table RELATIONSHIP add constraint CNODE_RSHIPCHILD_FK FOREIGN KEY (\"CHILD_CONTENTNODE_ID\") REFERENCES \"CONTENTNODE\" (\"ID\") ENABLE");
			safeExecute(conn, "alter table RELATIONSHIP add constraint CNODE_RSHIPPARENT_FK FOREIGN KEY (\"PARENT_CONTENTNODE_ID\") REFERENCES \"CONTENTNODE\" (\"ID\") ENABLE");
			
			// ATTRIBUTE
			// safeExecute(conn, "alter table ATTRIBUTE add constraint PK_ATTRIBUTE primary key (\"ID\") enable");
			safeExecute(conn, "alter table ATTRIBUTE add constraint CNODE_ATTR_FK FOREIGN KEY (\"CONTENTNODE_ID\") REFERENCES \"CONTENTNODE\" (\"ID\") ENABLE");

			conn.close();
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

	public void analyzeTables() {
		Connection conn = null;
		Statement stmt = null;

		try {
			conn = getConnection();
			conn.setAutoCommit(true);
			
			
			safeExecute(conn, "analyze table RELATIONSHIP compute statistics");
			safeExecute(conn, "analyze table CONTENTNODE compute statistics");
			safeExecute(conn, "analyze table ATTRIBUTE compute statistics");

			
			conn.close();
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



	protected void safeExecute(Connection conn, String cmd) {
		boolean retry = true;
		
		while (retry) {
			try {
				retry = false;

				Statement stmt = conn.createStatement();
				stmt.execute(cmd);
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
			}
		}
	}
	
	
	
	
	
	public void runScript(Reader aReader) {
		Connection conn = null;
		Statement stmt = null;

		try {
			conn = getConnection();
			conn.setAutoCommit(true);
			
			
			BufferedReader reader = new BufferedReader(aReader);
			
			String sqlLine;
			
			while ( (sqlLine = reader.readLine()) != null ) {
				if ( sqlLine.matches("\\s*") || sqlLine.matches("--.*") || sqlLine.matches("\\/\\/.*") ) {
					// skip empty or commented lines
					LOGGER.debug("Skip line " + sqlLine);
				} else {
					LOGGER.debug("Execute " + sqlLine);
					safeExecute(conn, sqlLine);
				}
			}
			
			conn.close();
		} catch (IOException e) {
			LOGGER.error(e);
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
