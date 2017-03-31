package com.freshdirect.cms.changecontrol;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.Category;

import com.freshdirect.cms.CmsRuntimeException;
import com.freshdirect.cms.ContentKey;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.util.log.LoggerFactory;

/**
 * Database-backed implementation of {@link com.freshdirect.cms.changecontrol.ChangeLogServiceI}.
 * 
 * @see com.freshdirect.cms.changecontrol.ChangeSetDao
 */
public class DbChangeLogService implements ChangeLogServiceI {

    private final Category LOGGER = LoggerFactory.getInstance(DbChangeLogService.class);

	private final DataSource dataSource;

	public DbChangeLogService(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	private Connection getConnection() throws SQLException {
		return dataSource.getConnection();
	}

	public PrimaryKey storeChangeSet(ChangeSet changeSet) {
		Connection conn = null;
		try {
			conn = getConnection();
			ChangeSetDao dao = new ChangeSetDao();
			String id = dao.store(conn, changeSet);
			return new PrimaryKey(id);
		} catch (SQLException e) {
            LOGGER.error(MessageFormat.format("Error persisting changeset {0}", changeSet), e);
			throw new CmsRuntimeException(e);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e1) {
				throw new CmsRuntimeException(e1);
			}
		}

	}

	public List<ChangeSet> getChangeHistory(ContentKey cKey) {
		Connection conn = null;
		try {
			conn = getConnection();
			ChangeSetDao dao = new ChangeSetDao();
			return dao.getChangeHistory(conn, cKey);
		} catch (SQLException e) {
            LOGGER.error(MessageFormat.format("Error getting change history for content key {0}", cKey), e);
			throw new CmsRuntimeException(e);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e1) {
				throw new CmsRuntimeException(e1);
			}
		}
	}

	public ChangeSet getChangeSet(PrimaryKey pk) {
		Connection conn = null;
		try {
			conn = getConnection();
			ChangeSetDao dao = new ChangeSetDao();
			return dao.retrieve(conn, pk.getId());
		} catch (SQLException e) {
            LOGGER.error(MessageFormat.format("Error getting change set for primary key {0}", pk), e);
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

	public List<ChangeSet> getChangesBetween(Date startDate, Date endDate) {
		Connection conn = null;
		try {
			conn = getConnection();
			ChangeSetDao dao = new ChangeSetDao();
			return dao.getChangesBetween(conn, startDate, endDate);
		} catch (SQLException e) {
            LOGGER.error(MessageFormat.format("Error getting changes between startDate {0} and endDate {1}", startDate, endDate), e);
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

	public List<ChangeSet> getChangesByUser( String userId ) {
		Connection conn = null;
		try {
			conn = getConnection();
			ChangeSetDao dao = new ChangeSetDao();
			return dao.getChangesByUser( conn, userId );
		} catch (SQLException e) {
            LOGGER.error(MessageFormat.format("Error getting changes by user {0}", userId), e);
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
	
	@Override
	public List<ChangeSet> getChangeSets( ContentKey contentKey, String userId, Date startDate, Date endDate ) {
		Connection conn = null;
		try {
			conn = getConnection();
			ChangeSetDao dao = new ChangeSetDao();
			return dao.getChangeSets( conn, contentKey, userId, startDate, endDate );
		} catch (SQLException e) {
            LOGGER.error(MessageFormat.format("Error getting changes by user {0} content key {1} between startDate {2} and endDate {3}", userId, contentKey, startDate, endDate), e);
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
}