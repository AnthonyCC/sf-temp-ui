package com.freshdirect.transadmin.datamanager.report;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.freshdirect.transadmin.datamanager.RouteFileManager;
import com.freshdirect.transadmin.datamanager.report.model.CutOffReportData;
import com.freshdirect.transadmin.datamanager.report.model.CutOffReportKey;
import com.freshdirect.transadmin.datamanager.report.model.TimeWindow;
import com.freshdirect.transadmin.util.TransStringUtil;
import com.freshdirect.transadmin.util.TransportationAdminProperties;

public class CsvCutOffReport implements ICutOffReport {

	public void generateCutOffReport(String file, CutOffReportData reportData ) 
											throws ReportGenerationException {

		StringBuffer colHeader = new StringBuffer();
		StringBuffer rows = new StringBuffer();
		StringBuffer report = new StringBuffer();

		report.append(CUTOFFREPORT_TITLE).append(CSV_SEPARATOR)
		.append(TransportationAdminProperties.getFileSeparator())
		.append(TransportationAdminProperties.getFileSeparator());

		if(reportData != null) {
			List cutOffKeys = getCutOffReportKeys(reportData.getReportData().keySet());
			Set timeSlots = (Set)cutOffKeys.get(0);
			Set routIds = (Set)cutOffKeys.get(1); 
			Set orderDates = (Set)cutOffKeys.get(2); 

			Iterator _orderDateItr = orderDates.iterator();
			while(_orderDateItr.hasNext()) {

				Date orderDate = (Date)_orderDateItr.next();
				report.append(CUTOFFREPORT_DATETITLE).append(CSV_SEPARATOR);
				try {
					report.append(TransStringUtil.getServerDate(orderDate)).append(CSV_SEPARATOR);
				} catch(ParseException exp) {
					// Do Nothing Corrupted Data
				}
				report.append(CUTOFFREPORT_CUTOFFTITLE).append(CSV_SEPARATOR)
				.append(reportData.getCutOff()).append(CSV_SEPARATOR)
				.append(TransportationAdminProperties.getFileSeparator())
				.append(TransportationAdminProperties.getFileSeparator());

				Iterator _routeItr = routIds.iterator();
				boolean headerDone = false;

				while(_routeItr.hasNext()) {

					String routeId = (String)_routeItr.next();
					Iterator _timeSlotItr = timeSlots.iterator();
					rows.append(routeId).append(CSV_SEPARATOR);

					while(_timeSlotItr.hasNext()) {

						TimeWindow timeWindow = (TimeWindow)_timeSlotItr.next();
						if(!headerDone) {
							if(colHeader.length() ==0) {
								colHeader.append(CUTOFFREPORT_ROUTETITLE).append(CSV_SEPARATOR);
							}
							colHeader.append(TransStringUtil.formatTimeRange(timeWindow.getTimeWindowStart()
									, timeWindow.getTimeWindowStop())).append(CSV_SEPARATOR);						
						}	

						CutOffReportKey tmpKey = new CutOffReportKey(orderDate
								, timeWindow.getTimeWindowStart()
								, timeWindow.getTimeWindowStop()
								, routeId);
						int stopCnt = 0;
						List stopLst = (List)reportData.getReportData().get(tmpKey);
						if(stopLst != null) {
							stopCnt = stopLst.size();
						}
						rows.append(stopCnt).append(CSV_SEPARATOR);
					}
					rows.append(TransportationAdminProperties.getFileSeparator());

					headerDone = true;
				}
				report.append(colHeader).append(TransportationAdminProperties.getFileSeparator());
				report.append(rows);
				colHeader = new StringBuffer();
				rows = new StringBuffer();
			}
		}
		new RouteFileManager().generateReportFile(file, report.toString());
	}

	private List getCutOffReportKeys(Set keys) {

		List result = new ArrayList();
		Set timeSlots = new TreeSet();
		Set routIds = new TreeSet();
		Set orderDates = new TreeSet();
		result.add(timeSlots);
		result.add(routIds);
		result.add(orderDates);

		if(keys != null) {
			Iterator _iterator = keys.iterator();
			CutOffReportKey _key = null;
			while(_iterator.hasNext()) {
				_key = (CutOffReportKey)_iterator.next();
				timeSlots.add(new TimeWindow(_key.getTimeWindowStart(), _key.getTimeWindowStop()));
				routIds.add(_key.getRouteId());
				orderDates.add(_key.getOrderDate());
			}
		}
		return result;
	}
}
