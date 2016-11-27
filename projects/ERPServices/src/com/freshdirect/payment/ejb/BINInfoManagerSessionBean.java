package com.freshdirect.payment.ejb;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;

import org.apache.log4j.Logger;

import com.freshdirect.common.ERPSessionBeanSupport;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.payment.BINInfo;

public class BINInfoManagerSessionBean extends ERPSessionBeanSupport {
	
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getInstance(BINInfoManagerSessionBean.class);
	public void saveBINInfo( List<List<BINInfo>> binInfo) throws FDResourceException {
		
		Connection conn = null;
		
		try {
			
			conn = getConnection();
			BINInfoDAO.storeBINInfo(conn, binInfo);
		} catch (SQLException e) {
			LOGGER.info("Exception in saveBINInfo(): "+e);
			this.getSessionContext().setRollbackOnly();
			throw new FDResourceException(e);
		} finally {
            close(conn);
		}
		
	}
	public NavigableMap<Long, BINInfo> getActiveBINs() throws FDResourceException {
		Connection conn = null;
				
				try {
					
					conn = getConnection();
					return BINInfoDAO.getLatestBINInfo(conn);
				} catch (SQLException e) {
					LOGGER.info("Exception in saveBINInfo(): "+e);
					this.getSessionContext().setRollbackOnly();
					throw new FDResourceException(e);
				} finally {
		            close(conn);
				}
				
	}

}
