package com.freshdirect.dataloader.notification.ejb;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Category;

import com.freshdirect.delivery.model.HandoffStatusNotification;
import com.freshdirect.framework.core.SessionBeanSupport;
import com.freshdirect.framework.util.log.LoggerFactory;

public class HandoffNotificationSessionBean extends SessionBeanSupport{
	
	private static final Category LOGGER = LoggerFactory.getInstance(HandoffNotificationSessionBean.class);

	private static final String GET_HANDOFF_COMPLETED = "select b.batch_id, b.delivery_date, b.cutoff_datetime, ba.action_datetime "+
				" from transp.handoff_batch b, transp.handoff_batchaction ba "+
				" where b.batch_id = ba.batch_id and b.delivery_date between trunc(sysdate) and trunc(sysdate+1) "+
				" and ba.action_type = 'COM' and b.batch_status = 'CPD' "+
				" order by b.cutoff_datetime"; 
	
	
	public List<HandoffStatusNotification> getHandoffStatus() throws RemoteException {
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<HandoffStatusNotification> completedBatchLst = new ArrayList<HandoffStatusNotification>();
		try {
			conn  = this.getConnection();
		   	ps = conn.prepareStatement(GET_HANDOFF_COMPLETED);
		  	rs = ps.executeQuery();
		  	HandoffStatusNotification model = null;
		  	while(rs.next()){
		  		model = new HandoffStatusNotification();
		  		completedBatchLst.add(model);
		  		model.setBatchId(rs.getString("BATCH_ID"));
		  		model.setDeliveryDate(rs.getDate("DELIVERY_DATE"));
		  		model.setCutoffTime(rs.getTimestamp("CUTOFF_DATETIME"));
		  		model.setCommitTime(rs.getTimestamp("ACTION_DATETIME"));
		  	}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (conn != null) {
					conn.close();
					conn = null;
				}
			} catch (SQLException se) {
				LOGGER.warn("SQLException while cleaning up", se);
			}
		}
		return completedBatchLst;
	} 
}