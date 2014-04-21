package com.freshdirect.transadmin.datamanager.report;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFPrintSetup;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.Region;

import com.freshdirect.routing.constants.EnumHandOffDispatchStatus;
import com.freshdirect.routing.model.IHandOffBatch;
import com.freshdirect.routing.model.IHandOffBatchRoute;
import com.freshdirect.routing.model.IHandOffBatchTrailer;
import com.freshdirect.routing.model.IServiceTimeScenarioModel;
import com.freshdirect.routing.model.IWaveInstance;
import com.freshdirect.routing.service.proxy.RoutingInfoServiceProxy;
import com.freshdirect.routing.util.RoutingDateUtil;
import com.freshdirect.routing.util.RoutingServicesProperties;
import com.freshdirect.routing.util.RoutingTimeOfDay;
import com.freshdirect.transadmin.datamanager.RouteFileManager;
import com.freshdirect.transadmin.datamanager.model.OrderRouteInfoModel;
import com.freshdirect.transadmin.datamanager.model.TrailerRouteInfoModel;
import com.freshdirect.transadmin.datamanager.report.model.CutOffReportData;
import com.freshdirect.transadmin.datamanager.report.model.CutOffReportKey;
import com.freshdirect.transadmin.datamanager.report.model.TimeWindow;
import com.freshdirect.transadmin.util.TransStringUtil;

public class XlsCutOffReport extends BaseXlsReport implements ICutOffReport  {
	
	public static final short DEFAULT_FONT_HEIGHT = 8;
	public static final int WIDTH_MULT = 240; // width per char
	public static final int DEFAULT_WIDTH = 12; // width per char
    public static final int MIN_CHARS = 8; // minimum char width
    private short rownum;	
    private short cellnum;
    
    private boolean needsDistanceFactor = true;
    //private MathContext mc = new MathContext(2);
    
	public boolean isNeedsDistanceFactor() {
		return needsDistanceFactor;
	}

	public void setNeedsDistanceFactor(boolean needsDistanceFactor) {
		this.needsDistanceFactor = needsDistanceFactor;
	}

	@SuppressWarnings({ "rawtypes", "deprecation" })
	public void generateCutOffReport(String file, CutOffReportData reportData)
										throws ReportGenerationException, ParseException {

		HSSFWorkbook wb = new HSSFWorkbook();
		Map styles = initStyles(wb);
				
		if (reportData != null) {
			List cutOffKeys = getCutOffReportKeys(reportData.getReportData().keySet());
			Set timeSlots = (Set) cutOffKeys.get(0);
			Set routIds = (Set) cutOffKeys.get(1);
			Set orderDates = (Set) cutOffKeys.get(2);
			//System.out.println("timeSlots >>"+timeSlots);
			Iterator _orderDateItr = orderDates.iterator();
			int totalRoutes = 0;
			int totalOrders = 0;
			
			while (_orderDateItr.hasNext()) {
				Date orderDate = (Date) _orderDateItr.next();
				
				HSSFSheet sheet = wb.createSheet(getFormattedDate(orderDate));
				sheet.setDefaultColumnWidth((short)DEFAULT_WIDTH);	
				sheet.setPrintGridlines(true);
 			    
			    HSSFPrintSetup ps = sheet.getPrintSetup();
			    sheet.setAutobreaks(true);
		        ps.setFitHeight((short) 1);
		        ps.setFitWidth((short) 1);
		        
			    rownum = 0;	
			    cellnum = 0;
			    
			    HSSFRow row = sheet.createRow(rownum++);
			    HSSFCell hssfCell = row.createCell(cellnum);

		        hssfCell.setCellStyle((HSSFCellStyle) styles.get("titleStyle"));
		        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
		        hssfCell.setCellValue(new HSSFRichTextString(CUTOFFREPORT_TITLE));
		        //int valWidth = (CUTOFFREPORT_TITLE).length() * WIDTH_MULT;
		        
				sheet.addMergedRegion(new Region(0,(short)0,0,(short)8));
				row = sheet.createRow(rownum++);//blank Row
				
		        cellnum = 0;
		        row = sheet.createRow(rownum++);
		        
		        hssfCell = row.createCell(cellnum++);		        
		        hssfCell.setCellStyle((HSSFCellStyle) styles.get("boldStyle"));
		        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
		        hssfCell.setCellValue(new HSSFRichTextString(CUTOFFREPORT_DATETITLE));
		        		        
		        hssfCell = row.createCell(cellnum++);		        
		        hssfCell.setCellStyle((HSSFCellStyle) styles.get("boldStyle"));
		        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
		        hssfCell.setCellValue(new HSSFRichTextString(getFormattedDate(orderDate)));
		        
		        hssfCell = row.createCell(cellnum++);		        
		        hssfCell.setCellStyle((HSSFCellStyle) styles.get("boldStyle"));
		        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
		        hssfCell.setCellValue(new HSSFRichTextString(CUTOFFREPORT_CUTOFFTITLE));
		        
		        hssfCell = row.createCell(cellnum++);		        
		        hssfCell.setCellStyle((HSSFCellStyle) styles.get("boldStyle"));
		        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
		        hssfCell.setCellValue(new HSSFRichTextString(reportData.getCutOff()));
		        
		        
		        row = sheet.createRow(rownum++);//blank Row
		        
		        Iterator _colsItr = timeSlots.iterator();
		        
		        cellnum = 0;
		        row = sheet.createRow(rownum++);
		        hssfCell = row.createCell(cellnum++);		        
		        hssfCell.setCellStyle((HSSFCellStyle) styles.get("boldStyle"));
		        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
		        hssfCell.setCellValue(new HSSFRichTextString(CUTOFFREPORT_ROUTETITLE));
		        
		        hssfCell = row.createCell(cellnum++);		        
		        hssfCell.setCellStyle((HSSFCellStyle) styles.get("boldStyle"));
		        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
		        hssfCell.setCellValue(new HSSFRichTextString("Dispatch"));
		        		        
		        while (_colsItr.hasNext()) {

					TimeWindow timeWindow = (TimeWindow) _colsItr.next();
					String strWindow = TransStringUtil.formatTime(timeWindow
															.getTimeWindowStart());
					hssfCell = row.createCell(cellnum++);		        
				    hssfCell.setCellStyle((HSSFCellStyle) styles.get("boldRightAlignStyle"));
				    hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
				    hssfCell.setCellValue(new HSSFRichTextString(strWindow));	
				    //sheet.autoSizeColumn((short)(cellnum-1));				
				}
		        
		        hssfCell = row.createCell(cellnum++);		        
		        hssfCell.setCellStyle((HSSFCellStyle) styles.get("boldRightAlignStyle"));
		        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
		        hssfCell.setCellValue(new HSSFRichTextString(CUTOFFREPORT_STOPSTITLE));
		        
				Iterator _routeItr = routIds.iterator();
				String currentZone = null;
				boolean rowStyleSwitch = false;
				
				while (_routeItr.hasNext()) {

					String routeId = (String) _routeItr.next();
					Iterator _timeSlotItr = timeSlots.iterator();
					
					IHandOffBatchRoute routeModel = reportData.getRouteMapping() != null ? reportData.getRouteMapping().get(routeId)
																								: null;
					       			        
			        Date dispatchTime = null;
			        String zone = null;
			        
			        if(routeModel != null && routeModel.getDispatchTime() != null) {			              	
			        	dispatchTime = routeModel.getDispatchTime().getAsDate();
			        	zone = routeModel.getArea();
			        }
			        
			        if(currentZone == null || !currentZone.equals(zone)) {
						currentZone = zone;
						rowStyleSwitch = !rowStyleSwitch;
					}
			        
					cellnum = 0;
					row = sheet.createRow(rownum++);
			        hssfCell = row.createCell(cellnum++);		        
			        hssfCell.setCellStyle((HSSFCellStyle) styles.get(rowStyleSwitch ? "textStyle" : "textStyleHighlight"));
			        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
			        hssfCell.setCellValue(new HSSFRichTextString(routeId));			            
			        totalRoutes++;
			        
			        
			        
			        //Change to display the dispatch Time
			        hssfCell = row.createCell(cellnum++);		        
			        hssfCell.setCellStyle((HSSFCellStyle) styles.get(rowStyleSwitch ? "textStyle" : "textStyleHighlight"));
			        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
			        hssfCell.setCellValue(new HSSFRichTextString(dispatchTime != null 
			        											? TransStringUtil.formatTime(dispatchTime) : ""));
			        
			        int totalStops = 0;
					while (_timeSlotItr.hasNext()) {
												
						TimeWindow timeWindow = (TimeWindow) _timeSlotItr.next();						

						CutOffReportKey tmpKey = new CutOffReportKey(orderDate,
								timeWindow.getTimeWindowStart(), timeWindow
										.getTimeWindowStop(), routeId);
						
						int stopCnt = 0;
						List stopLst = (List) reportData.getReportData().get(
								tmpKey);
						if (stopLst != null) {
							stopCnt = stopLst.size();
						}
						hssfCell = row.createCell(cellnum++);		        
						hssfCell.setCellStyle((HSSFCellStyle) styles.get(rowStyleSwitch ? "numericStyle" : "numericStyleHighlight"));
				        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
				        if(stopCnt == 0) {
				        	hssfCell.setCellValue(new HSSFRichTextString(""));
				        } else {
				        	hssfCell.setCellValue(stopCnt);
				        }
				        totalStops = totalStops + stopCnt;
					}
					hssfCell = row.createCell(cellnum++);	
					hssfCell.setCellStyle((HSSFCellStyle) styles.get(rowStyleSwitch ? "numericStyleBold" : "numericStyleBoldHighlight"));			        
			        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
			        if(totalStops == 0) {
			        	hssfCell.setCellValue(new HSSFRichTextString(""));
			        } else {
			        	hssfCell.setCellValue(totalStops);
			        }
			        totalOrders = totalOrders + totalStops;
				}
				createTotalRows(sheet, rownum, totalRoutes, totalOrders, styles);
			}
			
			createCartonCaseSheet(wb, reportData, styles);
			createSummarySheet(wb, reportData, styles);
			createDispatchSummarySheet(wb, reportData, styles);
			createTripInfoSheet(wb, reportData, styles);
			createDetailInfoSheet(wb, reportData, styles);
			createDispatchStatusSheet(wb, reportData, styles);
			createTrailerRouteSummarySheet(wb, reportData, styles);
		}		 

		new RouteFileManager().generateReportFile(file, wb);
	}
	
	private void createTotalRows(HSSFSheet sheet, int rowNum, int totalRoutes, int totalOrders, Map styles) {
		
		cellnum = 0;
		HSSFRow row = sheet.createRow(rowNum++);
		row = sheet.createRow(rowNum++);
        
		HSSFCell hssfCell = row.createCell(cellnum++);		        
        hssfCell.setCellStyle((HSSFCellStyle) styles.get("boldStyle"));
        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
        hssfCell.setCellValue(new HSSFRichTextString("Total Routes"));
        		        
        hssfCell = row.createCell(cellnum++);		        
        hssfCell.setCellStyle((HSSFCellStyle) styles.get("numericStyle"));
        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
        hssfCell.setCellValue(new HSSFRichTextString(totalRoutes+""));
        
        cellnum = 0;
		row = sheet.createRow(rowNum++);
        
		hssfCell = row.createCell(cellnum++);		        
        hssfCell.setCellStyle((HSSFCellStyle) styles.get("boldStyle"));
        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
        hssfCell.setCellValue(new HSSFRichTextString("Total Orders"));
        		        
        hssfCell = row.createCell(cellnum++);		        
        hssfCell.setCellStyle((HSSFCellStyle) styles.get("numericStyle"));
        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
        hssfCell.setCellValue(new HSSFRichTextString(totalOrders+""));
        
        
	}
	
	private short createCartonCaseSheet(HSSFWorkbook wb, CutOffReportData reportData, Map styles) {
		
		if(reportData.getReportData() != null) {
			List cutOffKeys = getCutOffReportKeys(reportData.getReportData().keySet());
			Set timeSlots = (Set) cutOffKeys.get(0);
			Set routeIds = (Set) cutOffKeys.get(1);
			Set orderDates = (Set) cutOffKeys.get(2);
			
			Iterator _orderDateItr = orderDates.iterator();
			int totalRoutes = 0;
			int totalOrderSize = 0;
			
			while (_orderDateItr.hasNext()) {
				Date orderDate = (Date) _orderDateItr.next();
				
				HSSFSheet sheet = wb.createSheet("CartonCase");
				sheet.setDefaultColumnWidth((short)DEFAULT_WIDTH);	
				sheet.setPrintGridlines(true);
 			    
			    HSSFPrintSetup ps = sheet.getPrintSetup();
			    sheet.setAutobreaks(true);
		        ps.setFitHeight((short) 1);
		        ps.setFitWidth((short) 1);
		        
			    rownum = 0;	
			    cellnum = 0;
			    
			    HSSFRow row = sheet.createRow(rownum++);
			    HSSFCell hssfCell = row.createCell(cellnum);

		        hssfCell.setCellStyle((HSSFCellStyle) styles.get("titleStyle"));
		        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
		        hssfCell.setCellValue(new HSSFRichTextString(CUTOFFREPORT_TITLE));
		        
				sheet.addMergedRegion(new Region(0,(short)0,0,(short)8));
				row = sheet.createRow(rownum++);//blank Row
				
		        cellnum = 0;
		        row = sheet.createRow(rownum++);
		        
		        hssfCell = row.createCell(cellnum++);		        
		        hssfCell.setCellStyle((HSSFCellStyle) styles.get("boldStyle"));
		        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
		        hssfCell.setCellValue(new HSSFRichTextString(CUTOFFREPORT_DATETITLE));
		        		        
		        hssfCell = row.createCell(cellnum++);		        
		        hssfCell.setCellStyle((HSSFCellStyle) styles.get("boldStyle"));
		        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
		        hssfCell.setCellValue(new HSSFRichTextString(getFormattedDate(orderDate)));
		        
		        hssfCell = row.createCell(cellnum++);		        
		        hssfCell.setCellStyle((HSSFCellStyle) styles.get("boldStyle"));
		        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
		        hssfCell.setCellValue(new HSSFRichTextString(CUTOFFREPORT_CUTOFFTITLE));
		        
		        hssfCell = row.createCell(cellnum++);		        
		        hssfCell.setCellStyle((HSSFCellStyle) styles.get("boldStyle"));
		        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
		        hssfCell.setCellValue(new HSSFRichTextString(reportData.getCutOff()));
		        
		        
		        row = sheet.createRow(rownum++);//blank Row
		        
		        Iterator _colsItr = timeSlots.iterator();
		        
		        cellnum = 0;
		        row = sheet.createRow(rownum++);
		        hssfCell = row.createCell(cellnum++);		        
		        hssfCell.setCellStyle((HSSFCellStyle) styles.get("boldStyle"));
		        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
		        hssfCell.setCellValue(new HSSFRichTextString(CUTOFFREPORT_ROUTETITLE));
		        
		        hssfCell = row.createCell(cellnum++);		        
		        hssfCell.setCellStyle((HSSFCellStyle) styles.get("boldStyle"));
		        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
		        hssfCell.setCellValue(new HSSFRichTextString("Dispatch"));
		        		        
		        while (_colsItr.hasNext()) {

					TimeWindow timeWindow = (TimeWindow) _colsItr.next();
					String strWindow = TransStringUtil.formatTime(timeWindow
															.getTimeWindowStart());
					hssfCell = row.createCell(cellnum++);		        
				    hssfCell.setCellStyle((HSSFCellStyle) styles.get("boldRightAlignStyle"));
				    hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
				    hssfCell.setCellValue(new HSSFRichTextString(strWindow));	
				    //sheet.autoSizeColumn((short)(cellnum-1));				
				}
		        
		        hssfCell = row.createCell(cellnum++);		        
		        hssfCell.setCellStyle((HSSFCellStyle) styles.get("boldRightAlignStyle"));
		        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
		        hssfCell.setCellValue(new HSSFRichTextString(CUTOFFREPORT_ROUTESIZETITLE));
		        
				Iterator _routeItr = routeIds.iterator();
				String currentZone = null;
				boolean rowStyleSwitch = false;
				
				while (_routeItr.hasNext()) {

					String routeId = (String) _routeItr.next();
					Iterator _timeSlotItr = timeSlots.iterator();
					
					IHandOffBatchRoute routeModel = reportData.getRouteMapping() != null ? reportData.getRouteMapping().get(routeId)
																								: null;
					       			        
			        Date dispatchTime = null;
			        String zone = null;
			        
			        if(routeModel != null && routeModel.getDispatchTime() != null) {			              	
			        	dispatchTime = routeModel.getDispatchTime().getAsDate();
			        	zone = routeModel.getArea();
			        }
			        
			        if(currentZone == null || !currentZone.equals(zone)) {
						currentZone = zone;
						rowStyleSwitch = !rowStyleSwitch;
					}
			        
					cellnum = 0;
					row = sheet.createRow(rownum++);
			        hssfCell = row.createCell(cellnum++);		        
			        hssfCell.setCellStyle((HSSFCellStyle) styles.get(rowStyleSwitch ? "textStyle" : "textStyleHighlight"));
			        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
			        hssfCell.setCellValue(new HSSFRichTextString(routeId));			            
			        totalRoutes++;
			        
			        
			        
			        //Change to display the dispatch Time
			        hssfCell = row.createCell(cellnum++);		        
			        hssfCell.setCellStyle((HSSFCellStyle) styles.get(rowStyleSwitch ? "textStyle" : "textStyleHighlight"));
			        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
			        hssfCell.setCellValue(new HSSFRichTextString(dispatchTime != null 
			        											? TransStringUtil.formatTime(dispatchTime) : ""));
			        
			        int totalRouteOrderSize = 0;
					while (_timeSlotItr.hasNext()) {
												
						TimeWindow timeWindow = (TimeWindow) _timeSlotItr.next();						

						CutOffReportKey tmpKey = new CutOffReportKey(orderDate,
								timeWindow.getTimeWindowStart(), timeWindow
										.getTimeWindowStop(), routeId);
						
						int orderSizeCnt = 0;
						List stopLst = (List) reportData.getReportData().get(tmpKey);
						if (stopLst != null) {
							Iterator _stopItr = stopLst.iterator();
							while(_stopItr.hasNext()){
								 OrderRouteInfoModel _stop = (OrderRouteInfoModel) _stopItr.next();
								 orderSizeCnt = orderSizeCnt + _stop.getOrderSize();
							}
						}
						hssfCell = row.createCell(cellnum++);		        
						hssfCell.setCellStyle((HSSFCellStyle) styles.get(rowStyleSwitch ? "numericStyle" : "numericStyleHighlight"));
				        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
				        if(orderSizeCnt == 0) {
				        	hssfCell.setCellValue(new HSSFRichTextString(""));
				        } else {
				        	hssfCell.setCellValue(orderSizeCnt);
				        }
				        totalRouteOrderSize = totalRouteOrderSize + orderSizeCnt;
					}
					hssfCell = row.createCell(cellnum++);	
					hssfCell.setCellStyle((HSSFCellStyle) styles.get(rowStyleSwitch ? "numericStyleBold" : "numericStyleBoldHighlight"));			        
			        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
			        if(totalRouteOrderSize == 0) {
			        	hssfCell.setCellValue(new HSSFRichTextString(""));
			        } else {
			        	hssfCell.setCellValue(totalRouteOrderSize);
			        }
			        totalOrderSize = totalOrderSize + totalRouteOrderSize;
				}
				
				cellnum = 0;
				row = sheet.createRow(rownum++);
				row = sheet.createRow(rownum++);
		        
				hssfCell = row.createCell(cellnum++);		        
		        hssfCell.setCellStyle((HSSFCellStyle) styles.get("boldStyle"));
		        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
		        hssfCell.setCellValue(new HSSFRichTextString("Total Routes"));
		        		        
		        hssfCell = row.createCell(cellnum++);		        
		        hssfCell.setCellStyle((HSSFCellStyle) styles.get("numericStyle"));
		        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
		        hssfCell.setCellValue(new HSSFRichTextString(totalRoutes+""));
		        
		        cellnum = 0;
				row = sheet.createRow(rownum++);
		        
				hssfCell = row.createCell(cellnum++);		        
		        hssfCell.setCellStyle((HSSFCellStyle) styles.get("boldStyle"));
		        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
		        hssfCell.setCellValue(new HSSFRichTextString("Total Order Size"));
		        		        
		        hssfCell = row.createCell(cellnum++);		        
		        hssfCell.setCellStyle((HSSFCellStyle) styles.get("numericStyle"));
		        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
		        hssfCell.setCellValue(new HSSFRichTextString(totalOrderSize+""));       
				
			}
		}
		
		return rownum;
	}
	
	private class HandOffBatchOrderComparator implements Comparator<OrderRouteInfoModel> {
		public int compare(OrderRouteInfoModel order1, OrderRouteInfoModel order2) {
			if(order1.getTripNo() != 0 && order2.getTripNo() != 0) {
				int windowCmp = order1.getTimeWindowStart().compareTo(order2.getTimeWindowStart());
				if(windowCmp != 0) {
					return windowCmp;						
				}
				return (order1.getTripNo() < order2.getTripNo() ? -1 :
	                (order1.getTripNo() == order2.getTripNo() ? 0 : 1));
			}
			return 0;
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes", "deprecation" })
	private short createTripInfoSheet(HSSFWorkbook wb, CutOffReportData reportData, Map styles) {
		
		if(reportData.getTripReportData() != null) {

			List<Integer> tripKeys = reportData.getTripSummaryKeys();
			Iterator _tripKeyItr = tripKeys.iterator();

			short rownum = 0;
			short cellnum = 0;

		    HSSFSheet sheet = wb.createSheet("Trip Summary");
		    sheet.setDefaultColumnWidth((short)DEFAULT_WIDTH);	
			sheet.setPrintGridlines(true);
		    
		    HSSFPrintSetup ps = sheet.getPrintSetup();
		    sheet.setAutobreaks(true);
	        ps.setFitHeight((short) 1);
	        ps.setFitWidth((short) 1);
	       		    
		    HSSFRow row = sheet.createRow(rownum++);
		    HSSFCell hssfCell = row.createCell(cellnum);

	        hssfCell.setCellStyle((HSSFCellStyle) styles.get("titleStyle"));
	        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
	        hssfCell.setCellValue(new HSSFRichTextString("Trip Summary Info"));
	        			        
			sheet.addMergedRegion(new Region(0,(short)0,0,(short)8));
			row = sheet.createRow(rownum++);//blank Row
			
	        row = sheet.createRow(rownum++);
	        
	        hssfCell = row.createCell(cellnum++);		        
	        hssfCell.setCellStyle((HSSFCellStyle) styles.get("boldStyle"));
	        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
	        hssfCell.setCellValue(new HSSFRichTextString("Route Id"));
	        		        
	        hssfCell = row.createCell(cellnum++);		        
	        hssfCell.setCellStyle((HSSFCellStyle) styles.get("boldStyle"));
	        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
	        hssfCell.setCellValue(new HSSFRichTextString("Trip Id"));
	        
	        hssfCell = row.createCell(cellnum++);		        
	        hssfCell.setCellStyle((HSSFCellStyle) styles.get("boldStyle"));
	        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
	        hssfCell.setCellValue(new HSSFRichTextString("Trip No"));
	        
	        hssfCell = row.createCell(cellnum++);		        
	        hssfCell.setCellStyle((HSSFCellStyle) styles.get("boldStyle"));
	        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
	        hssfCell.setCellValue(new HSSFRichTextString("Time Window"));
	        
	        hssfCell = row.createCell(cellnum++);		        
	        hssfCell.setCellStyle((HSSFCellStyle) styles.get("boldStyle"));
	        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
	        hssfCell.setCellValue(new HSSFRichTextString("ETA Window"));
	        	        
	        hssfCell = row.createCell(cellnum++);		        
	        hssfCell.setCellStyle((HSSFCellStyle) styles.get("boldStyle"));
	        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
	        hssfCell.setCellValue(new HSSFRichTextString("Stop No"));
	        
	        hssfCell = row.createCell(cellnum++);		        
	        hssfCell.setCellStyle((HSSFCellStyle) styles.get("boldStyle"));
	        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
	        hssfCell.setCellValue(new HSSFRichTextString("Order No"));
	        
	        hssfCell = row.createCell(cellnum++);		        
	        hssfCell.setCellStyle((HSSFCellStyle) styles.get("boldStyle"));
	        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
	        hssfCell.setCellValue(new HSSFRichTextString("Address"));
	        
	        boolean rowStyleSwitch = false;
	        int currentripNo = 0;
			while (_tripKeyItr.hasNext()) {
				
				String _tripNo = (String) _tripKeyItr.next();
				Collections.sort((List) reportData.getTripReportData().get(_tripNo), new HandOffBatchOrderComparator());
				
				Iterator _colsTripItr = ((List) reportData.getTripReportData().get(_tripNo)).iterator();

		        while (_colsTripItr.hasNext()) {
		        	cellnum = 0;
		        	OrderRouteInfoModel _model = (OrderRouteInfoModel)_colsTripItr.next();
		        	
		        	if(_model.getTripNo() != currentripNo) {
		        		currentripNo = _model.getTripNo();
		        		rowStyleSwitch = !rowStyleSwitch;
		        	}

		        	row = sheet.createRow(rownum++);
		        	
					hssfCell = row.createCell(cellnum++);		        
					hssfCell.setCellStyle((HSSFCellStyle) styles.get(rowStyleSwitch ? "textStyle" : "textStyleHighlight"));
				    hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
				    hssfCell.setCellValue(new HSSFRichTextString(_model.getRouteId()));
				    
				    hssfCell = row.createCell(cellnum++);		        
				    hssfCell.setCellStyle((HSSFCellStyle) styles.get(rowStyleSwitch ? "textStyle" : "textStyleHighlight"));				    
				    hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
				    hssfCell.setCellValue(new HSSFRichTextString(_model.getTripId()));
				    
				    hssfCell = row.createCell(cellnum++);		        
				    hssfCell.setCellStyle((HSSFCellStyle) styles.get(rowStyleSwitch ? "textStyle" : "textStyleHighlight"));				    
				    hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
				    hssfCell.setCellValue(new HSSFRichTextString(Integer.toString(_model.getTripNo())));
				    				    
				    hssfCell = row.createCell(cellnum++);		        
				    hssfCell.setCellStyle((HSSFCellStyle) styles.get(rowStyleSwitch ? "textStyle" : "textStyleHighlight"));
				    hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
				    hssfCell.setCellValue(new HSSFRichTextString(TransStringUtil.formatTimeRange
				    												(_model.getTimeWindowStart(), _model.getTimeWindowStop())));

				    hssfCell = row.createCell(cellnum++);		        
				    hssfCell.setCellStyle((HSSFCellStyle) styles.get(rowStyleSwitch ? "textStyle" : "textStyleHighlight"));
				    hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
				    if(_model.getDlvETAWindowStart() != null && _model.getDlvETAWindowStop() != null) {
				    	hssfCell.setCellValue(new HSSFRichTextString(TransStringUtil.formatTimeRange
				    												(_model.getDlvETAWindowStart(), _model.getDlvETAWindowStop())));
				    } else {
				    	hssfCell.setCellValue(new HSSFRichTextString(""));
				    }
				    				    			    
				    hssfCell = row.createCell(cellnum++);		        
				    hssfCell.setCellStyle((HSSFCellStyle) styles.get(rowStyleSwitch ? "textStyle" : "textStyleHighlight"));
				    hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
				    hssfCell.setCellValue(new HSSFRichTextString(_model.getStopNumber()));
				    
				    hssfCell = row.createCell(cellnum++);		        
				    hssfCell.setCellStyle((HSSFCellStyle) styles.get(rowStyleSwitch ? "textStyle" : "textStyleHighlight"));
				    hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
				    hssfCell.setCellValue(new HSSFRichTextString(_model.getOrderNumber()));
				    
				    hssfCell = row.createCell(cellnum++);
				    hssfCell.setCellStyle((HSSFCellStyle) styles.get(rowStyleSwitch ? "textStyleNoWrap" : "textStyleHighlight"));
				    hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
				    hssfCell.setCellValue(new HSSFRichTextString(_model.getAddress()+","+_model.getZipcode()));
				    //sheet.autoSizeColumn((short)(cellnum-1));				
				}		        			        
				
			}
		}
		
        
        return rownum;
	}
	
	private short createDetailInfoSheet(HSSFWorkbook wb, CutOffReportData reportData, Map styles) {
		
		if(reportData.getDetailData() != null) {
			Set routeKeys = reportData.getDetailData().keySet();
			
			Iterator _routeKeyItr = routeKeys.iterator();
			
			short rownum = 0;	
		    short cellnum = 0;
		    
		    HSSFSheet sheet = wb.createSheet("Route Details");
		    sheet.setDefaultColumnWidth((short)DEFAULT_WIDTH);	
					    
		    HSSFPrintSetup ps = sheet.getPrintSetup();
		    //sheet.setAutobreaks(true);		    
	        //ps.setFitWidth((short)1);
	        //ps.setFitHeight((short)1);	
	        ps.setScale((short)100);
	        sheet.setGridsPrinted(false);
	        
	        ps.setLandscape(true);
	        
	        /*sheet.setHorizontallyCenter(true);
	        ps.setPaperSize(HSSFPrintSetup.LETTER_PAPERSIZE);*/
	        
	        ps.setHeaderMargin((double) .25);
	        ps.setFooterMargin((double) .25);
	        sheet.setMargin(HSSFSheet.TopMargin, (double) .25);
	        sheet.setMargin(HSSFSheet.BottomMargin, (double) .25);
	        sheet.setMargin(HSSFSheet.LeftMargin, (double) .25);
	        sheet.setMargin(HSSFSheet.RightMargin, (double) .25);
		    
		    HSSFRow row = sheet.createRow(rownum++);
		    HSSFCell hssfCell = row.createCell(cellnum);

	        hssfCell.setCellStyle((HSSFCellStyle) styles.get("titleStyle"));
	        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
	        hssfCell.setCellValue(new HSSFRichTextString("Route Details"));
	        			        
			sheet.addMergedRegion(new Region(0,(short)0,0,(short)8));
			
			
			List _detailData = null;
			while (_routeKeyItr.hasNext()) {
				String _routeId = (String) _routeKeyItr.next();
				
				_detailData = ((List)reportData.getDetailData().get(_routeId));
				
				DtlSummaryData _summaryData = getDtlSummaryData(_detailData);
				
		        Iterator _colsTripItr = _detailData.iterator();
	       		
		        row = sheet.createRow(rownum++);//blank Row
		        sheet.addMergedRegion(new Region(rownum-1,(short)0,rownum-1,(short)8));
		        row = sheet.createRow(rownum++);//blank Row
		        sheet.addMergedRegion(new Region(rownum-1,(short)0,rownum-1,(short)8));
		        
		        row = sheet.createRow(rownum++);
		        cellnum = 0;
		        
		        hssfCell = row.createCell(cellnum++);		        
		        hssfCell.setCellStyle((HSSFCellStyle) styles.get("boldStyle"));
		        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
		        hssfCell.setCellValue(new HSSFRichTextString("Zone"));
		        
		        hssfCell = row.createCell(cellnum++);		        
		        hssfCell.setCellStyle((HSSFCellStyle) styles.get("textStyle"));
		        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
		        hssfCell.setCellValue(new HSSFRichTextString(TransStringUtil.getZoneNumber(_routeId)));
		        
		        cellnum = (short)(cellnum+4);
		        hssfCell = row.createCell(cellnum++);		        
		        hssfCell.setCellStyle((HSSFCellStyle) styles.get("boldStyle"));
		        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
		        hssfCell.setCellValue(new HSSFRichTextString(CUTOFFREPORT_DATETITLE));
		        		        
		        hssfCell = row.createCell(cellnum++);		        
		        hssfCell.setCellStyle((HSSFCellStyle) styles.get("textStyle"));
		        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
		        hssfCell.setCellValue(new HSSFRichTextString(getFormattedDate(_summaryData.getDeliveryDate())));
		        		        		             
		        row = sheet.createRow(rownum++);
		        cellnum = 0;		        
		        
		        hssfCell = row.createCell(cellnum++);		        
		        hssfCell.setCellStyle((HSSFCellStyle) styles.get("boldStyle"));
		        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
		        hssfCell.setCellValue(new HSSFRichTextString("Route"));
		        
		        hssfCell = row.createCell(cellnum++);		        
		        hssfCell.setCellStyle((HSSFCellStyle) styles.get("textStyle"));
		        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
		        hssfCell.setCellValue(new HSSFRichTextString(_routeId));
		        
		        row = sheet.createRow(rownum++);
		        cellnum = 1;
		        
		        sheet.addMergedRegion(new Region(rownum-1,(short)1,rownum-1,(short)7));
		        hssfCell = row.createCell(cellnum++);
		        
		        hssfCell.setCellStyle((HSSFCellStyle) styles.get("titlePlainStyle"));
		        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
		        hssfCell.setCellValue(new HSSFRichTextString("Route Summary"));
		        
		        
		        row = sheet.createRow(rownum++);
		        cellnum = 1;
		        
		        hssfCell = row.createCell(cellnum++);		        
		        hssfCell.setCellStyle((HSSFCellStyle) styles.get("textStyleNoWrap"));
		        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
		        hssfCell.setCellValue(new HSSFRichTextString("Stops"));
		        
		        hssfCell = row.createCell(cellnum++);		        
		        hssfCell.setCellStyle((HSSFCellStyle) styles.get("textStyle"));
		        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
		        hssfCell.setCellValue(new HSSFRichTextString(""+_summaryData.noOfStops));
		        
		        cellnum = (short)(cellnum+3);
		        
		        hssfCell = row.createCell(cellnum++);		        
		        hssfCell.setCellStyle((HSSFCellStyle) styles.get("textStyleNoWrap"));
		        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
		        hssfCell.setCellValue(new HSSFRichTextString("Miles"));
		        		        
		        hssfCell = row.createCell(cellnum++);		        
		        hssfCell.setCellStyle((HSSFCellStyle) styles.get("textStyle"));
		        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
		        hssfCell.setCellValue(new HSSFRichTextString(_summaryData.getTotalDistance()+" Miles"));
		        
		        
		        row = sheet.createRow(rownum++);
		        cellnum = 1;
		        
		        hssfCell = row.createCell(cellnum++);		        
		        hssfCell.setCellStyle((HSSFCellStyle) styles.get("textStyleNoWrap"));
		        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
		        //hssfCell.setCellValue(new HSSFRichTextString("Cartons/Cases"));
		        hssfCell.setCellValue(new HSSFRichTextString(""));
		        
		        hssfCell = row.createCell(cellnum++);		        
		        hssfCell.setCellStyle((HSSFCellStyle) styles.get("textStyle"));
		        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
		        //hssfCell.setCellValue(new HSSFRichTextString(""+(int)_summaryData.getNoOfCartons()));
		        hssfCell.setCellValue(new HSSFRichTextString(""));
		        		                
		        cellnum = (short)(cellnum+3);
		        
		        hssfCell = row.createCell(cellnum++);		        
		        hssfCell.setCellStyle((HSSFCellStyle) styles.get("textStyleNoWrap"));
		        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
		        hssfCell.setCellValue(new HSSFRichTextString("Drive Time"));
		        
		        hssfCell = row.createCell(cellnum++);		        
		        hssfCell.setCellStyle((HSSFCellStyle) styles.get("textStyle"));
		        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
		        hssfCell.setCellValue(new HSSFRichTextString(""+TransStringUtil.formatTime(_summaryData.getTotalTravelTime())));
		        
		        row = sheet.createRow(rownum++);
		        cellnum = 6;
		        
		        hssfCell = row.createCell(cellnum++);		        
		        hssfCell.setCellStyle((HSSFCellStyle) styles.get("textStyleNoWrap"));
		        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
		        hssfCell.setCellValue(new HSSFRichTextString("Service Time"));
		        
		        hssfCell = row.createCell(cellnum++);		        
		        hssfCell.setCellStyle((HSSFCellStyle) styles.get("textStyle"));
		        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
		        hssfCell.setCellValue(new HSSFRichTextString(""+TransStringUtil.formatTime(_summaryData.getTotalServiceTime())));
		        
		        
		        row = sheet.createRow(rownum++);
		        cellnum = 1;
		        
		        sheet.addMergedRegion(new Region(rownum-1,(short)1,rownum-1,(short)7));
		        hssfCell = row.createCell(cellnum++);
		        
		        hssfCell.setCellStyle((HSSFCellStyle) styles.get("titlePlainStyle"));
		        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
		        hssfCell.setCellValue(new HSSFRichTextString("Route Time ETAs"));
		        
		        
		        row = sheet.createRow(rownum++);
		        cellnum = 1;
		        
		        hssfCell = row.createCell(cellnum++);		        
		        hssfCell.setCellStyle((HSSFCellStyle) styles.get("textStyleNoWrap"));
		        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
		        hssfCell.setCellValue(new HSSFRichTextString("Dispatch Time"));
		        
		        hssfCell = row.createCell(cellnum++);		        
		        hssfCell.setCellStyle((HSSFCellStyle) styles.get("textStyle"));
		        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
		        //hssfCell.setCellValue(new HSSFRichTextString(TransStringUtil.formatTime(addMinutes(_summaryData.getRouteStartTime(), -25))));
		        hssfCell.setCellValue(new HSSFRichTextString(_summaryData.getDispatchTime() != null 
		        											? TransStringUtil.formatTime(_summaryData.getDispatchTime()) : ""));
		        
		        cellnum = (short)(cellnum+3);
		        
		        hssfCell = row.createCell(cellnum++);		        
		        hssfCell.setCellStyle((HSSFCellStyle) styles.get("textStyleNoWrap"));
		        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
		        hssfCell.setCellValue(new HSSFRichTextString("Last Stop Completed"));
		        
		        hssfCell = row.createCell(cellnum++);		        
		        hssfCell.setCellStyle((HSSFCellStyle) styles.get("textStyle"));
		        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
		        hssfCell.setCellValue(new HSSFRichTextString(TransStringUtil.formatTime(_summaryData.getLastDepartureTime())));
		        
		        
		        row = sheet.createRow(rownum++);
		        cellnum = 1;
		        
		        hssfCell = row.createCell(cellnum++);		        
		        hssfCell.setCellStyle((HSSFCellStyle) styles.get("textStyleNoWrap"));
		        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
		        hssfCell.setCellValue(new HSSFRichTextString("Truck Depart Time"));
		        
		        hssfCell = row.createCell(cellnum++);		        
		        hssfCell.setCellStyle((HSSFCellStyle) styles.get("textStyle"));
		        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
		        hssfCell.setCellValue(new HSSFRichTextString(TransStringUtil.formatTime(_summaryData.getRouteStartTime())));
		        		                
		        cellnum = (short)(cellnum+3);
		        
		        hssfCell = row.createCell(cellnum++);		        
		        hssfCell.setCellStyle((HSSFCellStyle) styles.get("textStyleNoWrap"));
		        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
		        hssfCell.setCellValue(new HSSFRichTextString("Return to Bldg Time"));
		        
		        hssfCell = row.createCell(cellnum++);		        
		        hssfCell.setCellStyle((HSSFCellStyle) styles.get("textStyle"));
		        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
		        hssfCell.setCellValue(new HSSFRichTextString(TransStringUtil.formatTime(_summaryData.getRouteCompleteTime())));
		        
		        row = sheet.createRow(rownum++);
		        cellnum = 1;
		        
		        hssfCell = row.createCell(cellnum++);		        
		        hssfCell.setCellStyle((HSSFCellStyle) styles.get("textStyleNoWrap"));
		        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
		        hssfCell.setCellValue(new HSSFRichTextString("1st Stop Start"));
		        
		        hssfCell = row.createCell(cellnum++);		        
		        hssfCell.setCellStyle((HSSFCellStyle) styles.get("textStyle"));
		        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
		        hssfCell.setCellValue(new HSSFRichTextString(TransStringUtil.formatTime(_summaryData.getFirstDepartureTime())));
		        		                
		        cellnum = (short)(cellnum+3);
		        
		        hssfCell = row.createCell(cellnum++);		        
		        hssfCell.setCellStyle((HSSFCellStyle) styles.get("textStyleNoWrap"));
		        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
		        hssfCell.setCellValue(new HSSFRichTextString("Check in"));
		        
		        hssfCell = row.createCell(cellnum++);		        
		        hssfCell.setCellStyle((HSSFCellStyle) styles.get("textStyle"));
		        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
		        hssfCell.setCellValue(new HSSFRichTextString(TransStringUtil.formatTime(addMinutes(_summaryData.getRouteCompleteTime(), 25))));
		        
		        row = sheet.createRow(rownum++);//blank Row
		        
		        row = sheet.createRow(rownum++);
		        cellnum = 1;
		        
		        hssfCell = row.createCell(cellnum++);		        
		        hssfCell.setCellStyle((HSSFCellStyle) styles.get("boldStyle"));
		        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
		        hssfCell.setCellValue(new HSSFRichTextString("Window"));
	              	        	        
		        hssfCell = row.createCell(cellnum++);		        
		        hssfCell.setCellStyle((HSSFCellStyle) styles.get("boldStyle"));
		        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
		        hssfCell.setCellValue(new HSSFRichTextString("Stop No"));
		        
		        hssfCell = row.createCell(cellnum++);		        
		        hssfCell.setCellStyle((HSSFCellStyle) styles.get("boldStyle"));
		        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
		        hssfCell.setCellValue(new HSSFRichTextString("Order No"));
		        
		        hssfCell = row.createCell(cellnum++);		        
		        hssfCell.setCellStyle((HSSFCellStyle) styles.get("boldStyle"));
		        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
		        hssfCell.setCellValue(new HSSFRichTextString("ETA Window"));
		        
		        hssfCell = row.createCell(cellnum++);		        
		        hssfCell.setCellStyle((HSSFCellStyle) styles.get("boldStyle"));
		        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
		        hssfCell.setCellValue(new HSSFRichTextString("Address"));
		        
		        boolean rowStyleSwitch = true;
		        Date currentWindow = null;
		        while (_colsTripItr.hasNext()) {
		        	cellnum = 1;
		        	OrderRouteInfoModel _model = (OrderRouteInfoModel)_colsTripItr.next();
		        	
		        	if(!_model.getTimeWindowStart().equals(currentWindow)) {
		        		currentWindow = _model.getTimeWindowStart();
		        		rowStyleSwitch = !rowStyleSwitch;
		        	}
		        	
		        	row = sheet.createRow(rownum++);
		        	
					hssfCell = row.createCell(cellnum++);		        
				    hssfCell.setCellStyle((HSSFCellStyle) styles.get(rowStyleSwitch ? "textStyle" : "textStyleHighlight"));
				    hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
				    hssfCell.setCellValue(new HSSFRichTextString(TransStringUtil.formatTimeRange
				    												(_model.getTimeWindowStart(), _model.getTimeWindowStop())));
			    	    				    
				    hssfCell = row.createCell(cellnum++);		        
				    hssfCell.setCellStyle((HSSFCellStyle) styles.get(rowStyleSwitch ? "textStyle" : "textStyleHighlight"));
				    hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
				    hssfCell.setCellValue(new HSSFRichTextString(_model.getStopNumber()));
				    				    
				    hssfCell = row.createCell(cellnum++);		        
				    hssfCell.setCellStyle((HSSFCellStyle) styles.get(rowStyleSwitch ? "textStyle" : "textStyleHighlight"));
				    hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
				    hssfCell.setCellValue(new HSSFRichTextString(_model.getOrderNumber()));
				    
				    hssfCell = row.createCell(cellnum++);		        
				    hssfCell.setCellStyle((HSSFCellStyle) styles.get(rowStyleSwitch ? "textStyle" : "textStyleHighlight"));
				    hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
				    if(_model.getDlvETAWindowStart() != null && _model.getDlvETAWindowStop() != null) {
				    	hssfCell.setCellValue(new HSSFRichTextString(TransStringUtil.formatTimeRange
																	(_model.getDlvETAWindowStart(), _model.getDlvETAWindowStop())));
				    } else {
				    	hssfCell.setCellValue(new HSSFRichTextString(""));
				    }
				    
				    hssfCell = row.createCell(cellnum++);		        
				    hssfCell.setCellStyle((HSSFCellStyle) styles.get(rowStyleSwitch ? "textStyleNoWrap" : "textStyleHighlight"));
				    hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
				    hssfCell.setCellValue(new HSSFRichTextString(_model.getAddress()+","+_model.getZipcode()));
				    //sheet.autoSizeColumn((short)(cellnum-1));				
				}		        			        
				
			}
		}		        
        return rownum;
	}
	
	protected DtlTrailerSummaryData getDtlTrailerSummaryData(IHandOffBatchTrailer trailer, List routeLst, IHandOffBatch batch, IServiceTimeScenarioModel scenarioModel ) throws ParseException {
		
		DtlTrailerSummaryData result = new DtlTrailerSummaryData();
		if(routeLst != null) {
			int size = routeLst.size();
			result.setTrailerId(trailer.getTrailerId());
			result.setNoOfRoutes(size);
			result.setCrossDockId(trailer.getCrossDockId());
			result.setDeliveryDate(batch.getDeliveryDate());
			result.setDispatchTime(trailer.getDispatchTime().getAsDate());
			
			result.setTotalTravelTime(TransStringUtil.getTime(TransStringUtil.calcHMS(TransStringUtil.getDiffInSeconds(trailer.getCompletionTime(), trailer.getDispatchTime().getAsDate()))));
			
			int maxCartonsPerPallet = 0;
			int maxPalletPerTrailer = 0;
			if(scenarioModel != null && scenarioModel.getDefaultContainerCartonCount() != 0 &&
					scenarioModel.getDefaultTrailerContainerCount() != 0){
				 maxCartonsPerPallet = scenarioModel.getDefaultContainerCartonCount();
				 maxPalletPerTrailer = scenarioModel.getDefaultTrailerContainerCount();
			} else {
				maxCartonsPerPallet = RoutingServicesProperties.getMaxTrailerCartonSize();
				maxPalletPerTrailer = RoutingServicesProperties.getMaxTrailerContainerSize();
			}		
			
			double routePalletCnt = 0.0;			
			double totalPalletCnt = 0.0;
					
			TrailerRouteInfoModel model = null;
			for(int i=0; i < size; i++){
				model = (TrailerRouteInfoModel)routeLst.get(i);				
				routePalletCnt = model.getNoOfCartons()/maxCartonsPerPallet;
				routePalletCnt = Math.ceil(routePalletCnt);
				totalPalletCnt += routePalletCnt;
			}	
			result.setNoOfConts(totalPalletCnt);
			result.setMaxContainers(maxPalletPerTrailer);	
		}
		return result;
	}
	
	class DtlTrailerSummaryData {
		
		Date deliveryDate;
		Date dispatchTime;
		Date travelTime;
		int noOfRoutes;
		double noOfConts;
		String crossDockId;
		String trailerType;
		double totalConts;
		double totalCartons;
		Date totalTravelTime;
		String trailerId;
		int maxContainers;
		
		public String getTrailerId() {
			return trailerId;
		}
		public void setTrailerId(String trailerId) {
			this.trailerId = trailerId;
		}
		public Date getDeliveryDate() {
			return deliveryDate;
		}
		public void setDeliveryDate(Date deliveryDate) {
			this.deliveryDate = deliveryDate;
		}
		public Date getDispatchTime() {
			return dispatchTime;
		}
		public void setDispatchTime(Date dispatchTime) {
			this.dispatchTime = dispatchTime;
		}
		public int getNoOfRoutes() {
			return noOfRoutes;
		}
		public void setNoOfRoutes(int noOfRoutes) {
			this.noOfRoutes = noOfRoutes;
		}
		public String getCrossDockId() {
			return crossDockId;
		}
		public void setCrossDockId(String crossDockId) {
			this.crossDockId = crossDockId;
		}
		public String getTrailerType() {
			return trailerType;
		}
		public void setTrailerType(String trailerType) {
			this.trailerType = trailerType;
		}
		public double getTotalConts() {
			return totalConts;
		}
		public void setTotalConts(double totalConts) {
			this.totalConts = totalConts;
		}
		public double getTotalCartons() {
			return totalCartons;
		}
		public void setTotalCartons(double totalCartons) {
			this.totalCartons = totalCartons;
		}
		public Date getTotalTravelTime() {
			return totalTravelTime;
		}
		public void setTotalTravelTime(Date totalTravelTime) {
			this.totalTravelTime = totalTravelTime;
		}
		public double getNoOfConts() {
			return noOfConts;
		}
		public void setNoOfConts(double noOfConts) {
			this.noOfConts = noOfConts;
		}
		public Date getTravelTime() {
			return travelTime;
		}
		public void setTravelTime(Date travelTime) {
			this.travelTime = travelTime;
		}
		public int getMaxContainers() {
			return maxContainers;
		}
		public void setMaxContainers(int maxContainers) {
			this.maxContainers = maxContainers;
		}
	}
	protected DtlSummaryData getDtlSummaryData(List rnOrderLst ) {
		
		DtlSummaryData result = new DtlSummaryData();
		double 	noOfCartons = 0.0;	
		if(rnOrderLst != null) {
			int size = rnOrderLst.size();
			result.setNoOfStops(size);
			OrderRouteInfoModel _model = null;
			for (int i = 0; i < size; i++) {
				_model = (OrderRouteInfoModel)rnOrderLst.get(i);
				if(i == 0) {
					result.setDeliveryDate(_model.getDeliveryDate());					
					result.setTotalDistance(getFormattedDistance(_model.getTotalDistance()));
					result.setTotalServiceTime(_model.getTotalServiceTime());
					result.setTotalTravelTime(_model.getTotalTravelTime());
					result.setRouteStartTime(_model.getRouteStartTime());
					result.setFirstDepartureTime(_model.getStopArrivalTime());	
					result.setDispatchTime(_model.getDispatchTime());
					result.setDispatchSequence(_model.getDispatchSequence());
				} else if(i == (size-1)) {
					result.setRouteCompleteTime(_model.getRouteCompleteTime());
					result.setLastDepartureTime(_model.getStopDepartureTime());
				}
				//noOfCartons += getDoubleVal(_model.getTotalSize1())+getDoubleVal(_model.getTotalSize2());
			}
		}
		result.setNoOfCartons(noOfCartons);		
		return result;
	}
	
	class DtlSummaryData {
		
		int noOfStops;
		double noOfCartons;
		double totalDistance;
		Date totalTravelTime;
		Date totalServiceTime;
		Date routeCompleteTime;
		
		Date routeStartTime;
		Date firstDepartureTime;
		Date lastDepartureTime;
		Date deliveryDate;
		Date dispatchTime;
		int dispatchSequence;
		
		public Date getDeliveryDate() {
			return deliveryDate;
		}
		public void setDeliveryDate(Date deliveryDate) {
			this.deliveryDate = deliveryDate;
		}
		public int getNoOfStops() {
			return noOfStops;
		}
		public void setNoOfStops(int noOfStops) {
			this.noOfStops = noOfStops;
		}
		public double getNoOfCartons() {
			return noOfCartons;
		}
		public void setNoOfCartons(double noOfCartons) {
			this.noOfCartons = noOfCartons;
		}
		public double getTotalDistance() {
			return totalDistance;
		}
		public void setTotalDistance(double totalDistance) {
			this.totalDistance = totalDistance;
		}
		public Date getTotalTravelTime() {
			return totalTravelTime;
		}
		public void setTotalTravelTime(Date totalTravelTime) {
			this.totalTravelTime = totalTravelTime;
		}
		public Date getTotalServiceTime() {
			return totalServiceTime;
		}
		public void setTotalServiceTime(Date totalServiceTime) {
			this.totalServiceTime = totalServiceTime;
		}
		public Date getRouteCompleteTime() {
			return routeCompleteTime;
		}
		public void setRouteCompleteTime(Date routeCompleteTime) {
			this.routeCompleteTime = routeCompleteTime;
		}
		public Date getRouteStartTime() {
			return routeStartTime;
		}
		public void setRouteStartTime(Date routeStartTime) {
			this.routeStartTime = routeStartTime;
		}
		public Date getFirstDepartureTime() {
			return firstDepartureTime;
		}
		public void setFirstDepartureTime(Date firstDepartureTime) {
			this.firstDepartureTime = firstDepartureTime;
		}
		public Date getLastDepartureTime() {
			return lastDepartureTime;
		}
		public void setLastDepartureTime(Date lastDepartureTime) {
			this.lastDepartureTime = lastDepartureTime;
		}
		public Date getDispatchTime() {
			return dispatchTime;
		}
		public int getDispatchSequence() {
			return dispatchSequence;
		}
		public void setDispatchTime(Date dispatchTime) {
			this.dispatchTime = dispatchTime;
		}
		public void setDispatchSequence(int dispatchSequence) {
			this.dispatchSequence = dispatchSequence;
		}		
	}
		
	private double getFormattedDistance(String distance) {
		
		double result = 0;
		try {			
			result = isNeedsDistanceFactor() ? Double.parseDouble(distance)/10 : Double.parseDouble(distance);
			//result = new BigDecimal(result).round(mc).doubleValue();
		} catch (Exception e) {
			//Do Nothing
		}
		return result;
	}
	
	private Date addMinutes(Date source, int minutes) {
		
		Date result = null;
		try {
			result = TransStringUtil.addSeconds(source, minutes*60);
		} catch (Exception e) {
			//Do Nothing
		}
		return result;
	}
	
	private short createSummarySheet(HSSFWorkbook wb, CutOffReportData reportData, Map styles) {
		
		if(reportData.getSummaryData() != null) {
			List<String> summaryKeys = reportData.getSummaryKeys();
						
			Iterator _itr = summaryKeys.iterator();
			
			short rownum = 0;	
		    short cellnum = 0;
		    
		    int totalRoutes = 0;
			int totalOrders = 0;
		    
		    HSSFSheet sheet = wb.createSheet("Route Summary");
		    sheet.setDefaultColumnWidth((short)DEFAULT_WIDTH);	
			sheet.setPrintGridlines(true);
		    
		    HSSFPrintSetup ps = sheet.getPrintSetup();
		    sheet.setAutobreaks(true);
	        ps.setFitHeight((short) 1);
	        ps.setFitWidth((short) 1);
	       		    
		    HSSFRow row = sheet.createRow(rownum++);
		    HSSFCell hssfCell = row.createCell(cellnum);

	        hssfCell.setCellStyle((HSSFCellStyle) styles.get("titleStyle"));
	        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
	        hssfCell.setCellValue(new HSSFRichTextString("Route Summary Report"));
	        			        
			sheet.addMergedRegion(new Region(0,(short)0,0,(short)10));
			row = sheet.createRow(rownum++);//blank Row
			
	        row = sheet.createRow(rownum++);
	        
	        hssfCell = row.createCell(cellnum++);		        
	        hssfCell.setCellStyle((HSSFCellStyle) styles.get("boldStyle"));
	        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
	        hssfCell.setCellValue(new HSSFRichTextString("Route Id"));
	       
	        		        
	        hssfCell = row.createCell(cellnum++);		        
	        hssfCell.setCellStyle((HSSFCellStyle) styles.get("boldStyle"));
	        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
	        hssfCell.setCellValue(new HSSFRichTextString("No of Orders"));
	        
	        hssfCell = row.createCell(cellnum++);		        
	        hssfCell.setCellStyle((HSSFCellStyle) styles.get("boldStyle"));
	        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
	        hssfCell.setCellValue(new HSSFRichTextString("Dispatch Time"));
	        
	        hssfCell = row.createCell(cellnum++);		        
	        hssfCell.setCellStyle((HSSFCellStyle) styles.get("boldStyle"));
	        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
	        hssfCell.setCellValue(new HSSFRichTextString("Dispatch Sequence"));
	        	        
			while (_itr.hasNext()) {
				String _routeId = (String) _itr.next();
				List _orders = 	(List)reportData.getSummaryData().get(_routeId);        			        
		        int totalStops =  0;   
		        Date dispatchTime = null;
		        int dispatchSequence = 0;
		        if(_orders != null) {
		        	totalStops = _orders.size();
		        	if(_orders.size() > 0) {
		        		dispatchTime = ((OrderRouteInfoModel)_orders.get(0)).getDispatchTime();
		        		dispatchSequence = ((OrderRouteInfoModel)_orders.get(0)).getDispatchSequence();
		        	}
		        }
		        cellnum = 0;
	        	        	
	        	row = sheet.createRow(rownum++);
	        	
				hssfCell = row.createCell(cellnum++);		        
			    hssfCell.setCellStyle((HSSFCellStyle) styles.get("textStyle"));
			    hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
			    hssfCell.setCellValue(new HSSFRichTextString(_routeId));
			    totalRoutes++;
			    
			    hssfCell = row.createCell(cellnum++);		        
			    hssfCell.setCellStyle((HSSFCellStyle) styles.get("textStyle"));
			    hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
			    hssfCell.setCellValue(new HSSFRichTextString(totalStops+""));
			    
			    hssfCell = row.createCell(cellnum++);		        
			    hssfCell.setCellStyle((HSSFCellStyle) styles.get("textStyle"));
			    hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
			    hssfCell.setCellValue(new HSSFRichTextString(dispatchTime != null 
												? TransStringUtil.formatTime(dispatchTime) : ""));
			    
			    hssfCell = row.createCell(cellnum++);		        
			    hssfCell.setCellStyle((HSSFCellStyle) styles.get("textStyle"));
			    hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
			    hssfCell.setCellValue(new HSSFRichTextString(dispatchSequence+""));
			    
			    totalOrders = totalOrders+totalStops;
			}
			createTotalRows(sheet, rownum, totalRoutes, totalOrders, styles);
		}
		       
        return rownum;
	}
	
	private short createDispatchSummarySheet(HSSFWorkbook wb, CutOffReportData reportData, Map styles) {
		
		Map<String, DispatchSummaryInfo> dispatchSummaryInfo = getDispatchSummaryInfo(reportData);
		short rownum = 0;	
	    short cellnum = 0;
	   		    
	    HSSFSheet sheet = wb.createSheet("Dispatch Summary");
	    sheet.setDefaultColumnWidth((short)DEFAULT_WIDTH);	
		sheet.setPrintGridlines(true);
	    
	    HSSFPrintSetup ps = sheet.getPrintSetup();
	    sheet.setAutobreaks(true);
        ps.setFitHeight((short) 1);
        ps.setFitWidth((short) 1);
       		    
	    HSSFRow row = sheet.createRow(rownum++);
	    HSSFCell hssfCell = row.createCell(cellnum);

        hssfCell.setCellStyle((HSSFCellStyle) styles.get("titleStyle"));
        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
        hssfCell.setCellValue(new HSSFRichTextString("Dispatch Summary Report"));
        			        
		sheet.addMergedRegion(new Region(0,(short)0,0,(short)10));
		row = sheet.createRow(rownum++);//blank Row
		
        row = sheet.createRow(rownum++);
        
        hssfCell = row.createCell(cellnum++);		        
        hssfCell.setCellStyle((HSSFCellStyle) styles.get("boldStyle"));
        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
        hssfCell.setCellValue(new HSSFRichTextString("Zone"));
       
        		        
        hssfCell = row.createCell(cellnum++);		        
        hssfCell.setCellStyle((HSSFCellStyle) styles.get("boldStyle"));
        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
        hssfCell.setCellValue(new HSSFRichTextString("Store Orders"));
        
        hssfCell = row.createCell(cellnum++);		        
        hssfCell.setCellStyle((HSSFCellStyle) styles.get("boldStyle"));
        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
        hssfCell.setCellValue(new HSSFRichTextString("Routing Orders"));
        
        hssfCell = row.createCell(cellnum++);		        
        hssfCell.setCellStyle((HSSFCellStyle) styles.get("boldStyle"));
        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
        hssfCell.setCellValue(new HSSFRichTextString("Exception"));
        
		for(Map.Entry<String, DispatchSummaryInfo> zoneEntry : dispatchSummaryInfo.entrySet()) {
			cellnum = 0;
        	
        	row = sheet.createRow(rownum++);
        	
			hssfCell = row.createCell(cellnum++);		        
		    hssfCell.setCellStyle((HSSFCellStyle) styles.get("textStyle"));
		    hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
		    hssfCell.setCellValue(new HSSFRichTextString(zoneEntry.getKey()));
		   		    
		    hssfCell = row.createCell(cellnum++);		        
		    hssfCell.setCellStyle((HSSFCellStyle) styles.get("numericStyle"));
		    hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
		    hssfCell.setCellValue(new HSSFRichTextString(zoneEntry.getValue().getStoreOrderCnt()+""));
		    
		    hssfCell = row.createCell(cellnum++);		        
		    hssfCell.setCellStyle((HSSFCellStyle) styles.get("numericStyle"));
		    hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
		    hssfCell.setCellValue(new HSSFRichTextString(zoneEntry.getValue().getRoutingOrderCnt()+""));
		    
		    hssfCell = row.createCell(cellnum++);		        
		    hssfCell.setCellStyle((HSSFCellStyle) styles.get("textStyle"));
		    hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
		    hssfCell.setCellValue(new HSSFRichTextString( (zoneEntry.getValue().getStoreOrderCnt() != zoneEntry.getValue().getRoutingOrderCnt() ?
		    		                "X" : "")));
		}
				       
        return rownum;
	}
	
	private short createDispatchStatusSheet(HSSFWorkbook wb, CutOffReportData reportData, Map styles) {
		
		Map<RoutingTimeOfDay, DispatchStatusInfo> dispatchStatusInfo = getDispatchStatusInfo(reportData);
		short rownum = 0;	
	    short cellnum = 0;
	   		    
	    HSSFSheet sheet = wb.createSheet("Dispatch Status");
	    sheet.setDefaultColumnWidth((short)DEFAULT_WIDTH);	
		sheet.setPrintGridlines(true);
	    
	    HSSFPrintSetup ps = sheet.getPrintSetup();
	    sheet.setAutobreaks(true);
        ps.setFitHeight((short) 1);
        ps.setFitWidth((short) 1);
       		    
	    HSSFRow row = sheet.createRow(rownum++);
	    HSSFCell hssfCell = row.createCell(cellnum);

        hssfCell.setCellStyle((HSSFCellStyle) styles.get("titleStyle"));
        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
        hssfCell.setCellValue(new HSSFRichTextString("Dispatch Status Report"));
        			        
		sheet.addMergedRegion(new Region(0,(short)0,0,(short)10));
		row = sheet.createRow(rownum++);//blank Row
		
        row = sheet.createRow(rownum++);
        
        hssfCell = row.createCell(cellnum++);		        
        hssfCell.setCellStyle((HSSFCellStyle) styles.get("boldStyle"));
        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
        hssfCell.setCellValue(new HSSFRichTextString("Dispatch Time"));
       
        		        
        hssfCell = row.createCell(cellnum++);		        
        hssfCell.setCellStyle((HSSFCellStyle) styles.get("boldStyle"));
        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
        hssfCell.setCellValue(new HSSFRichTextString("Status"));
        
        hssfCell = row.createCell(cellnum++);		        
        hssfCell.setCellStyle((HSSFCellStyle) styles.get("boldStyle"));
        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
        hssfCell.setCellValue(new HSSFRichTextString("Pending Zone"));
        
        hssfCell = row.createCell(cellnum++);		        
        hssfCell.setCellStyle((HSSFCellStyle) styles.get("boldStyle"));
        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
        hssfCell.setCellValue(new HSSFRichTextString("Pending HandOff"));
        
		for(Map.Entry<RoutingTimeOfDay, DispatchStatusInfo> dispatchEntry : dispatchStatusInfo.entrySet()) {
			cellnum = 0;
        	
        	row = sheet.createRow(rownum++);
        	
			hssfCell = row.createCell(cellnum++);		        
		    hssfCell.setCellStyle((HSSFCellStyle) styles.get(dispatchEntry.getValue().getStatus().equals(EnumHandOffDispatchStatus.COMPLETE) 
										? "textStyle" : "textStyleHighlight"));
		    hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
		    hssfCell.setCellValue(new HSSFRichTextString(dispatchEntry.getKey() != null 
								? TransStringUtil.formatTime(dispatchEntry.getKey().getAsDate()) : ""));
		   		    
		    hssfCell = row.createCell(cellnum++);		        
		    hssfCell.setCellStyle((HSSFCellStyle) styles.get(dispatchEntry.getValue().getStatus().equals(EnumHandOffDispatchStatus.COMPLETE) 
		    							? "textStyle" : "textStyleHighlight"));
		    hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
		    hssfCell.setCellValue(new HSSFRichTextString(dispatchEntry.getValue().getStatus().name()));
		    
		    if(dispatchEntry.getValue().getPendingDetails() != null && 
		    		dispatchEntry.getValue().getPendingDetails().keySet().size() > 0) {
		    	for(Map.Entry<String, List<RoutingTimeOfDay>> zoneEntry : dispatchEntry.getValue().getPendingDetails().entrySet()) {
		    		for(RoutingTimeOfDay cutOff : zoneEntry.getValue()) {
		    			cellnum = 2;
		    			row = sheet.createRow(rownum++);
		    			hssfCell = row.createCell(cellnum++);		        
		    		    hssfCell.setCellStyle((HSSFCellStyle) styles.get("textStyle"));
		    		    hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
		    		    hssfCell.setCellValue(new HSSFRichTextString(zoneEntry.getKey()));
		    		    
		    		    hssfCell = row.createCell(cellnum++);		        
		    		    hssfCell.setCellStyle((HSSFCellStyle) styles.get("textStyle"));
		    		    hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
		    		    hssfCell.setCellValue(new HSSFRichTextString(cutOff != null 
		    					? TransStringUtil.formatTime(cutOff.getAsDate()) : ""));
		    		}
		    	}
		    }
		    
		}
				       
        return rownum;
	}
	
	private short createTrailerRouteSummarySheet(HSSFWorkbook wb, CutOffReportData reportData, Map styles) throws ParseException {
		if(reportData.getTrailerDetailData() != null) {
			Set trailerKeys = reportData.getTrailerDetailData().keySet();
			
			Iterator _trailerKeyItr = trailerKeys.iterator();
			
			short rownum = 0;	
		    short cellnum = 0;
		    
		    HSSFSheet sheet = wb.createSheet("Trailer Details");
		    sheet.setDefaultColumnWidth((short)DEFAULT_WIDTH);	
					    
		    HSSFPrintSetup ps = sheet.getPrintSetup();
		    ps.setScale((short)100);
	        sheet.setGridsPrinted(false);
	        
	        ps.setLandscape(true);
	         
	        ps.setHeaderMargin((double) .25);
	        ps.setFooterMargin((double) .25);
	        sheet.setMargin(HSSFSheet.TopMargin, (double) .25);
	        sheet.setMargin(HSSFSheet.BottomMargin, (double) .25);
	        sheet.setMargin(HSSFSheet.LeftMargin, (double) .25);
	        sheet.setMargin(HSSFSheet.RightMargin, (double) .25);
		    
		    HSSFRow row = sheet.createRow(rownum++);
		    HSSFCell hssfCell = row.createCell(cellnum);

	        hssfCell.setCellStyle((HSSFCellStyle) styles.get("titleStyle"));
	        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
	        hssfCell.setCellValue(new HSSFRichTextString("Trailer-Route Details"));
	        			        
			sheet.addMergedRegion(new Region(0,(short)0,0,(short)8));
			int totalRoutes =  0;   
			List _detailData = null;
			while (_trailerKeyItr.hasNext()) {
				String _trailerId = (String) _trailerKeyItr.next();
				
				_detailData = ((List)reportData.getTrailerDetailData().get(_trailerId));
				totalRoutes = _detailData.size();
				
				DtlTrailerSummaryData _summaryData = getDtlTrailerSummaryData(reportData.getTrailerMapping().get(_trailerId)
						, _detailData, reportData.getBatch(), reportData.getScenarioModel());
				
		        Iterator _colsRouteItr = _detailData.iterator();
	       		
		        row = sheet.createRow(rownum++);//blank Row
		        sheet.addMergedRegion(new Region(rownum-1,(short)0,rownum-1,(short)8));
		        row = sheet.createRow(rownum++);//blank Row
		        sheet.addMergedRegion(new Region(rownum-1,(short)0,rownum-1,(short)8));
		        
		        row = sheet.createRow(rownum++);
		        cellnum = 0;
		        
		        hssfCell = row.createCell(cellnum++);		        
		        hssfCell.setCellStyle((HSSFCellStyle) styles.get("boldStyle"));
		        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
		        hssfCell.setCellValue(new HSSFRichTextString("CrossDock"));
		        
		        hssfCell = row.createCell(cellnum++);		        
		        hssfCell.setCellStyle((HSSFCellStyle) styles.get("textStyle"));
		        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
		        hssfCell.setCellValue(new HSSFRichTextString(_summaryData.getCrossDockId()));
		        
		        cellnum = (short)(cellnum+4);
		        hssfCell = row.createCell(cellnum++);		        
		        hssfCell.setCellStyle((HSSFCellStyle) styles.get("boldStyle"));
		        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
		        hssfCell.setCellValue(new HSSFRichTextString(CUTOFFREPORT_DATETITLE));
		        		        
		        hssfCell = row.createCell(cellnum++);		        
		        hssfCell.setCellStyle((HSSFCellStyle) styles.get("textStyle"));
		        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
		        hssfCell.setCellValue(new HSSFRichTextString(getFormattedDate(_summaryData.getDeliveryDate())));
		        		        		             
		        row = sheet.createRow(rownum++);
		        cellnum = 0;		        
		        
		        hssfCell = row.createCell(cellnum++);		        
		        hssfCell.setCellStyle((HSSFCellStyle) styles.get("boldStyle"));
		        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
		        hssfCell.setCellValue(new HSSFRichTextString("Trailer"));
		        
		        hssfCell = row.createCell(cellnum++);		        
		        hssfCell.setCellStyle((HSSFCellStyle) styles.get("textStyle"));
		        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
		        hssfCell.setCellValue(new HSSFRichTextString(_trailerId));
		        
		        row = sheet.createRow(rownum++);
		        cellnum = 1;
		        
		        sheet.addMergedRegion(new Region(rownum-1,(short)1,rownum-1,(short)7));
		        hssfCell = row.createCell(cellnum++);
		        
		        hssfCell.setCellStyle((HSSFCellStyle) styles.get("titlePlainStyle"));
		        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
		        hssfCell.setCellValue(new HSSFRichTextString("Trailer Summary"));
		        		        
		        row = sheet.createRow(rownum++);
		        cellnum = 1; 	        
		        
		        hssfCell = row.createCell(cellnum++);		        
		        hssfCell.setCellStyle((HSSFCellStyle) styles.get("textStyleNoWrap"));
		        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
		        hssfCell.setCellValue(new HSSFRichTextString("Routes"));
		        
		        hssfCell = row.createCell(cellnum++);		        
		        hssfCell.setCellStyle((HSSFCellStyle) styles.get("textStyle"));
		        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
		        hssfCell.setCellValue(new HSSFRichTextString(""+_summaryData.getNoOfRoutes()));
		        
		        cellnum = (short)(cellnum+3);
		        
		        hssfCell = row.createCell(cellnum++);		        
		        hssfCell.setCellStyle((HSSFCellStyle) styles.get("textStyleNoWrap"));
		        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
		        hssfCell.setCellValue(new HSSFRichTextString("Dispatch Time"));
		        
		        hssfCell = row.createCell(cellnum++);		        
		        hssfCell.setCellStyle((HSSFCellStyle) styles.get("textStyle"));
		        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
		        hssfCell.setCellValue(new HSSFRichTextString(_summaryData.getDispatchTime() != null 
						? TransStringUtil.formatTime(_summaryData.getDispatchTime()) : ""));
		        
		        row = sheet.createRow(rownum++);
		        cellnum = 1;
		        
		        hssfCell = row.createCell(cellnum++);		        
		        hssfCell.setCellStyle((HSSFCellStyle) styles.get("textStyleNoWrap"));
		        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
		        hssfCell.setCellValue(new HSSFRichTextString("Total Pallets"));
		        
		        hssfCell = row.createCell(cellnum++);		        
		        hssfCell.setCellStyle((HSSFCellStyle) styles.get("textStyle"));
		        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
		        hssfCell.setCellValue(new HSSFRichTextString(""+_summaryData.getNoOfConts()));
		        
		        cellnum = (short)(cellnum+3);
		        
		        hssfCell = row.createCell(cellnum++);		        
		        hssfCell.setCellStyle((HSSFCellStyle) styles.get("textStyleNoWrap"));
		        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
		        hssfCell.setCellValue(new HSSFRichTextString("Drive Time"));
		        
		        hssfCell = row.createCell(cellnum++);		        
		        hssfCell.setCellStyle((HSSFCellStyle) styles.get("textStyle"));
		        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
		        hssfCell.setCellValue(new HSSFRichTextString(""+TransStringUtil.formatTime(_summaryData.getTotalTravelTime())));
		        
		        row = sheet.createRow(rownum++);
		        cellnum = 1;
		        
		        hssfCell = row.createCell(cellnum++);		        
		        hssfCell.setCellStyle((HSSFCellStyle) styles.get("textStyleNoWrap"));
		        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
		        hssfCell.setCellValue(new HSSFRichTextString("Max Cont(s)"));
		        
		        hssfCell = row.createCell(cellnum++);		        
		        hssfCell.setCellStyle((HSSFCellStyle) styles.get("textStyle"));
		        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
		        hssfCell.setCellValue(new HSSFRichTextString(""+_summaryData.getMaxContainers()));
		       		         
		        row = sheet.createRow(rownum++);
		        cellnum = 1;
		        
		        sheet.addMergedRegion(new Region(rownum-1,(short)1,rownum-1,(short)7));
		        hssfCell = row.createCell(cellnum++);
		        
		        hssfCell.setCellStyle((HSSFCellStyle) styles.get("titlePlainStyle"));
		        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
		        hssfCell.setCellValue(new HSSFRichTextString("Trailer Routes"));
		        
		        row = sheet.createRow(rownum++);//blank Row
		        
		        row = sheet.createRow(rownum++);
		        cellnum = 1;
		        
		        hssfCell = row.createCell(cellnum++);		        
		        hssfCell.setCellStyle((HSSFCellStyle) styles.get("boldStyle"));
		        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
		        hssfCell.setCellValue(new HSSFRichTextString("Route No"));
	              	        	        
		        hssfCell = row.createCell(cellnum++);		        
		        hssfCell.setCellStyle((HSSFCellStyle) styles.get("boldStyle"));
		        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
		        hssfCell.setCellValue(new HSSFRichTextString("No of Stops"));
		        
		        hssfCell = row.createCell(cellnum++);		        
		        hssfCell.setCellStyle((HSSFCellStyle) styles.get("boldStyle"));
		        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
		        hssfCell.setCellValue(new HSSFRichTextString("Dispatch Time"));
		        
		        hssfCell = row.createCell(cellnum++);		        
		        hssfCell.setCellStyle((HSSFCellStyle) styles.get("boldStyle"));
		        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
		        hssfCell.setCellValue(new HSSFRichTextString("Dispatch Seq"));
		        
		        boolean rowStyleSwitch = true;
		        Date currentDispatch = null;
		        while (_colsRouteItr.hasNext()) {
		        	cellnum = 1;
		        	TrailerRouteInfoModel _model = (TrailerRouteInfoModel)_colsRouteItr.next();
		        	
		        	if(!_model.getDispatchTime().equals(currentDispatch)) {
		        		currentDispatch = _model.getDispatchTime();
		        		rowStyleSwitch = !rowStyleSwitch;
		        	}
		        	
		        	row = sheet.createRow(rownum++);
		        	
					hssfCell = row.createCell(cellnum++);		        
				    hssfCell.setCellStyle((HSSFCellStyle) styles.get(rowStyleSwitch ? "textStyle" : "textStyleHighlight"));
				    hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
				    hssfCell.setCellValue(new HSSFRichTextString(_model.getRouteId()));
			    	    				    
				    hssfCell = row.createCell(cellnum++);		        
				    hssfCell.setCellStyle((HSSFCellStyle) styles.get(rowStyleSwitch ? "textStyle" : "textStyleHighlight"));
				    hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
				    hssfCell.setCellValue(new HSSFRichTextString(_model.getNoOfStops()+""));
				    				    
				    hssfCell = row.createCell(cellnum++);		        
				    hssfCell.setCellStyle((HSSFCellStyle) styles.get(rowStyleSwitch ? "textStyle" : "textStyleHighlight"));
				    hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
				    hssfCell.setCellValue(new HSSFRichTextString(_model.getDispatchTime() != null 
							? TransStringUtil.formatTime(_model.getDispatchTime()) : ""));
				    
				    hssfCell = row.createCell(cellnum++);		        
				    hssfCell.setCellStyle((HSSFCellStyle) styles.get(rowStyleSwitch ? "textStyleNoWrap" : "textStyleHighlight"));
				    hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
				    hssfCell.setCellValue(new HSSFRichTextString(_model.getDispatchSequence()+""));
				}		        			        
				
			}
		}		        
        return rownum;		
	}
	private Map<RoutingTimeOfDay, DispatchStatusInfo> getDispatchStatusInfo(CutOffReportData reportData) {
		
		Map<RoutingTimeOfDay, DispatchStatusInfo> result = new TreeMap<RoutingTimeOfDay, DispatchStatusInfo>();
		Map<String, List<RoutingTimeOfDay>> pendingDetails = null;
		String area = null;
		RoutingInfoServiceProxy routingInfoProxy = new RoutingInfoServiceProxy();

		Map<RoutingTimeOfDay, Integer> cutOffSeqMap = routingInfoProxy.getCutoffSequence();
		
		if(reportData.getPlannedDispatchTree() != null && reportData.getBatch() != null) {
			RoutingTimeOfDay rCutOff = new RoutingTimeOfDay(reportData.getBatch().getCutOffDateTime());
			for(Map.Entry<String, Map<RoutingTimeOfDay, Map<RoutingTimeOfDay, List<IWaveInstance>>>> areaEntry : reportData.getPlannedDispatchTree().entrySet()) {
				for(Map.Entry<RoutingTimeOfDay, Map<RoutingTimeOfDay, List<IWaveInstance>>> dispEntry : areaEntry.getValue().entrySet()) {
					for(Map.Entry<RoutingTimeOfDay, List<IWaveInstance>> cutOffEntry : dispEntry.getValue().entrySet()) {
						if(!result.containsKey(dispEntry.getKey())) {
							result.put(dispEntry.getKey(), new DispatchStatusInfo());
						}
						if (RoutingDateUtil.isLaterCutoff(cutOffSeqMap, cutOffEntry.getKey(), rCutOff)) {
							result.get(dispEntry.getKey()).setStatus(EnumHandOffDispatchStatus.PENDING);
							pendingDetails = result.get(dispEntry.getKey()).getPendingDetails();
							if(cutOffEntry.getValue().size() > 0) { 
								area = cutOffEntry.getValue().get(0).getArea().getAreaCode();
								if(!pendingDetails.containsKey(area)) {
									pendingDetails.put(area, new ArrayList<RoutingTimeOfDay>());
								}
								pendingDetails.get(area).add(cutOffEntry.getKey());
							}
						} else {
							if(result.get(dispEntry.getKey()).getStatus() == null) {
								result.get(dispEntry.getKey()).setStatus(EnumHandOffDispatchStatus.COMPLETE);
							}
						}
					}
				}
			}
		}		
		return result;
	}
	
	private Map<String, DispatchSummaryInfo> getDispatchSummaryInfo(CutOffReportData reportData) {
		Map<String, DispatchSummaryInfo> result = new HashMap<String, DispatchSummaryInfo>();
		if(reportData.getSummaryData() != null) {
			List<String> summaryKeys = reportData.getSummaryKeys();
			Iterator _itr = summaryKeys.iterator();
			IHandOffBatchRoute routeInfo = null;
			String _routeId = null;
			
			while (_itr.hasNext()) {
				_routeId = (String) _itr.next();
				List<OrderRouteInfoModel> _orders = 	(List<OrderRouteInfoModel>)reportData.getSummaryData().get(_routeId); 
				for(OrderRouteInfoModel info : _orders) {
					if(info.getDeliveryZone() != null) {
						if(!result.containsKey(info.getDeliveryZone())) {
							result.put(info.getDeliveryZone(), new DispatchSummaryInfo());
						}
						result.get(info.getDeliveryZone()).setStoreOrderCnt(result.get(info.getDeliveryZone()).getStoreOrderCnt()+1);
						routeInfo = reportData.getRouteMapping() != null ? reportData.getRouteMapping().get(_routeId) : null; 
						if(routeInfo != null) {
							if(!result.containsKey(routeInfo.getArea())) {
								result.put(routeInfo.getArea(), new DispatchSummaryInfo());
							}
							result.get(routeInfo.getArea()).setRoutingOrderCnt(result.get(routeInfo.getArea()).getRoutingOrderCnt()+1);
						}
					}
				}
			}
		}
		return result;
	}
	
	class DispatchSummaryInfo {
		private int storeOrderCnt;
		private int routingOrderCnt;
		public int getStoreOrderCnt() {
			return storeOrderCnt;
		}
		public void setStoreOrderCnt(int storeOrderCnt) {
			this.storeOrderCnt = storeOrderCnt;
		}
		public int getRoutingOrderCnt() {
			return routingOrderCnt;
		}
		public void setRoutingOrderCnt(int routingOrderCnt) {
			this.routingOrderCnt = routingOrderCnt;
		}
			
	}
	
	class DispatchStatusInfo {
		private EnumHandOffDispatchStatus status;
		private Map<String, List<RoutingTimeOfDay>> pendingDetails = new HashMap<String, List<RoutingTimeOfDay>>();
		
		public EnumHandOffDispatchStatus getStatus() {
			return status;
		}
		public void setStatus(EnumHandOffDispatchStatus status) {
			this.status = status;
		}
		public Map<String, List<RoutingTimeOfDay>> getPendingDetails() {
			return pendingDetails;
		}
		public void setPendingDetails(Map<String, List<RoutingTimeOfDay>> pendingDetails) {
			this.pendingDetails = pendingDetails;
		}					
	}

	private List getCutOffReportKeys(Set keys) {

		List result = new ArrayList();
		Set timeSlots = new TreeSet();
		Set routIds = new TreeSet();
		Set orderDates = new TreeSet();
		result.add(timeSlots);
		result.add(routIds);
		result.add(orderDates);

		if (keys != null) {
			Iterator _iterator = keys.iterator();
			CutOffReportKey _key = null;
			while (_iterator.hasNext()) {
				_key = (CutOffReportKey) _iterator.next();
				timeSlots.add(new TimeWindow(_key.getTimeWindowStart(), _key
						.getTimeWindowStop()));
				routIds.add(_key.getRouteId());
				orderDates.add(_key.getOrderDate());
			}
		}
		return result;
	}
	
}
