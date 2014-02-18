package com.freshdirect.delivery.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;
import java.util.Map.Entry;

import com.freshdirect.analytics.DispatchVolumeModel;
import com.freshdirect.routing.util.RoutingTimeOfDay;


public class DispatchVolumeDAO implements IDispatchVolumeDAO{

	
	public void saveDispatch(Connection conn, Map<RoutingTimeOfDay, DispatchVolumeModel> dispatchMap) throws SQLException
	{
		PreparedStatement ps = null;
		try
		{
			ps = conn.prepareStatement("INSERT INTO MIS.DISPATCH_VOLUME(SNAPSHOT_TIME,DISPATCH_DATE,DISPATCH_TIME,PLANNED_CAPACITY,PLANT_CAPACITY,ORDERS,TRUCKS,ALLOCATED_ORDERS) VALUES(?,?,?,?,?,?,?,?)");
			
			for(Entry<RoutingTimeOfDay, DispatchVolumeModel> dispatchEntry: dispatchMap.entrySet())
			{
				ps.setTimestamp(1, new java.sql.Timestamp(dispatchEntry.getValue().getSnapshotTime().getTime()));
				ps.setDate(2, new java.sql.Date(dispatchEntry.getValue().getDispatchDate().getTime()));
				ps.setTimestamp(3, new java.sql.Timestamp(dispatchEntry.getKey().getAsDate().getTime()));
				ps.setInt(4, new BigDecimal(dispatchEntry.getValue().getPlannedCapacity()).intValue());
				ps.setInt(5, dispatchEntry.getValue().getPlantCapacity());
				ps.setInt(6, dispatchEntry.getValue().getOrderCount());
				ps.setInt(7, dispatchEntry.getValue().getNoOftrucks());
				ps.setInt(8, dispatchEntry.getValue().getAllocatedOrderCnt());
				ps.addBatch();
			}
			ps.executeBatch();
				
		}
		
		finally
		{
			try {
				if(ps!=null)
					ps.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		
	}
}
