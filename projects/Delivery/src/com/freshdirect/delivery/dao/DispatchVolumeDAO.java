package com.freshdirect.delivery.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.freshdirect.analytics.DispatchVolumeModel;
import com.freshdirect.crm.CallLogModel;
import com.freshdirect.delivery.constants.EnumDeliveryMenuOption;
import com.freshdirect.crm.ejb.CriteriaBuilder;
import com.freshdirect.delivery.DlvResourceException;
import com.freshdirect.delivery.model.AirclicCartonInfo;
import com.freshdirect.delivery.model.AirclicCartonScanDetails;
import com.freshdirect.delivery.model.AirclicMessageVO;
import com.freshdirect.delivery.model.AirclicNextelVO;
import com.freshdirect.delivery.model.AirclicTextMessageVO;
import com.freshdirect.delivery.model.DeliveryExceptionModel;
import com.freshdirect.delivery.model.DeliveryManifestVO;
import com.freshdirect.delivery.model.DeliverySummaryModel;
import com.freshdirect.delivery.model.DispatchNextTelVO;
import com.freshdirect.delivery.model.RouteNextelVO;
import com.freshdirect.delivery.model.SignatureVO;
import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.routing.model.BuildingModel;
import com.freshdirect.routing.model.BuildingOperationDetails;
import com.freshdirect.routing.model.DeliveryModel;
import com.freshdirect.routing.model.GeographicLocation;
import com.freshdirect.routing.model.IBuildingModel;
import com.freshdirect.routing.model.IBuildingOperationDetails;
import com.freshdirect.routing.model.IDeliveryModel;
import com.freshdirect.routing.model.IGeographicLocation;
import com.freshdirect.routing.model.ILocationModel;
import com.freshdirect.routing.model.IZoneModel;
import com.freshdirect.routing.model.LocationModel;
import com.freshdirect.routing.model.ZoneModel;
import com.freshdirect.routing.util.RoutingTimeOfDay;


public class DispatchVolumeDAO implements IDispatchVolumeDAO{

	
	public void saveDispatch(Connection conn, Map<RoutingTimeOfDay, DispatchVolumeModel> dispatchMap) throws SQLException
	{
		PreparedStatement ps = null;
		try
		{
			ps = conn.prepareStatement("INSERT INTO MIS.DISPATCH_VOLUME(SNAPSHOT_TIME,DISPATCH_DATE,DISPATCH_TIME,PLANNED_CAPACITY,PLANT_CAPACITY,ORDERS,TRUCKS) VALUES(?,?,?,?,?,?,?)");
			
			for(Entry<RoutingTimeOfDay, DispatchVolumeModel> dispatchEntry: dispatchMap.entrySet())
			{
				ps.setTimestamp(1, new java.sql.Timestamp(dispatchEntry.getValue().getSnapshotTime().getTime()));
				ps.setDate(2, new java.sql.Date(dispatchEntry.getValue().getDispatchDate().getTime()));
				ps.setTimestamp(3, new java.sql.Timestamp(dispatchEntry.getKey().getAsDate().getTime()));
				ps.setInt(4, new BigDecimal(dispatchEntry.getValue().getPlannedCapacity()).intValue());
				ps.setInt(5, dispatchEntry.getValue().getPlantCapacity());
				ps.setInt(6, dispatchEntry.getValue().getOrderCount());
				ps.setInt(7, dispatchEntry.getValue().getNoOftrucks());
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
