package com.freshdirect.transadmin.datamanager.report;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFPrintSetup;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.Region;

import com.freshdirect.transadmin.datamanager.RouteFileManager;
import com.freshdirect.transadmin.datamanager.model.OrderRouteInfoModel;
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

	public void generateCutOffReport(String file, CutOffReportData reportData)
										throws ReportGenerationException {

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
				
				while (_routeItr.hasNext()) {

					String routeId = (String) _routeItr.next();
					Iterator _timeSlotItr = timeSlots.iterator();
					
					cellnum = 0;
					row = sheet.createRow(rownum++);
			        hssfCell = row.createCell(cellnum++);		        
			        hssfCell.setCellStyle((HSSFCellStyle) styles.get("textStyle"));
			        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
			        hssfCell.setCellValue(new HSSFRichTextString(routeId));
			        totalRoutes++;
			        
			        int totalStops = 0;
					while (_timeSlotItr.hasNext()) {

						TimeWindow timeWindow = (TimeWindow) _timeSlotItr
								.next();
						

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
				        hssfCell.setCellStyle((HSSFCellStyle) styles.get("numericStyle"));
				        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
				        if(stopCnt == 0) {
				        	hssfCell.setCellValue(new HSSFRichTextString(""));
				        } else {
				        	hssfCell.setCellValue(stopCnt);
				        }
				        totalStops = totalStops + stopCnt;
					}
					hssfCell = row.createCell(cellnum++);		        
			        hssfCell.setCellStyle((HSSFCellStyle) styles.get("numericStyleBold"));
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
			
			createSummarySheet(wb, reportData, styles);
			createTripInfoSheet(wb, reportData, styles);
			createDetailInfoSheet(wb, reportData, styles);
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
	
	private short createTripInfoSheet(HSSFWorkbook wb, CutOffReportData reportData, Map styles) {
		
		if(reportData.getTripReportData() != null) {
			Set tripKeys = reportData.getTripReportData().keySet();
			
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
	        hssfCell.setCellValue(new HSSFRichTextString("Stop No"));
	        
	        hssfCell = row.createCell(cellnum++);		        
	        hssfCell.setCellStyle((HSSFCellStyle) styles.get("boldStyle"));
	        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
	        hssfCell.setCellValue(new HSSFRichTextString("Order No"));
	        
	        hssfCell = row.createCell(cellnum++);		        
	        hssfCell.setCellStyle((HSSFCellStyle) styles.get("boldStyle"));
	        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
	        hssfCell.setCellValue(new HSSFRichTextString("Address"));
	        
			while (_tripKeyItr.hasNext()) {
				String _tripRouteId = (String) _tripKeyItr.next();
						        			        
		        Iterator _colsTripItr = ((List)reportData.getTripReportData().get(_tripRouteId)).iterator();
	       			        		        
		        while (_colsTripItr.hasNext()) {
		        	cellnum = 0;
		        	OrderRouteInfoModel _model = (OrderRouteInfoModel)_colsTripItr.next();
		        	
		        	row = sheet.createRow(rownum++);
		        	
					hssfCell = row.createCell(cellnum++);		        
				    hssfCell.setCellStyle((HSSFCellStyle) styles.get("textStyle"));
				    hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
				    hssfCell.setCellValue(new HSSFRichTextString(_tripRouteId));
				    
				    hssfCell = row.createCell(cellnum++);		        
				    hssfCell.setCellStyle((HSSFCellStyle) styles.get("textStyle"));
				    hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
				    hssfCell.setCellValue(new HSSFRichTextString(_model.getTripId()));
				    
				    hssfCell = row.createCell(cellnum++);		        
				    hssfCell.setCellStyle((HSSFCellStyle) styles.get("textStyle"));
				    hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
				    hssfCell.setCellValue(new HSSFRichTextString(_model.getStopNumber()));
				    
				    hssfCell = row.createCell(cellnum++);		        
				    hssfCell.setCellStyle((HSSFCellStyle) styles.get("textStyle"));
				    hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
				    hssfCell.setCellValue(new HSSFRichTextString(_model.getOrderNumber()));
				    
				    hssfCell = row.createCell(cellnum++);		        
				    hssfCell.setCellStyle((HSSFCellStyle) styles.get("textStyleNoWrap"));
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
		        hssfCell.setCellValue(new HSSFRichTextString(TransStringUtil.formatTime(addMinutes(_summaryData.getRouteStartTime(), -25))));
		        
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
				    hssfCell.setCellStyle((HSSFCellStyle) styles.get(rowStyleSwitch ? "textStyleNoWrap" : "textStyleHighlight"));
				    hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
				    hssfCell.setCellValue(new HSSFRichTextString(_model.getAddress()+","+_model.getZipcode()));
				    //sheet.autoSizeColumn((short)(cellnum-1));				
				}		        			        
				
			}
		}		        
        return rownum;
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
			Set summaryKeys = reportData.getSummaryData().keySet();
			
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
	        	        
			while (_itr.hasNext()) {
				String _routeId = (String) _itr.next();
				List _orders = 	(List)reportData.getSummaryData().get(_routeId);        			        
		        int totalStops =  _orders != null ? _orders.size():0;      
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
			    totalOrders = totalOrders+totalStops;
			}
			createTotalRows(sheet, rownum, totalRoutes, totalOrders, styles);
		}
		       
        return rownum;
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
