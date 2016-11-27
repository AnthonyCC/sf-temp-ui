package com.freshdirect.transadmin.datamanager.report;

import java.text.ParseException;
import java.util.Iterator;
import java.util.Map;


import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFPrintSetup;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.Region;

import com.freshdirect.transadmin.constants.EnumCrisisMngBatchType;
import com.freshdirect.transadmin.datamanager.RouteFileManager;
import com.freshdirect.transadmin.datamanager.model.ICancelOrderInfo;
import com.freshdirect.transadmin.datamanager.report.model.CrisisManagerReportData;
import com.freshdirect.transadmin.model.ICrisisManagerBatchDeliverySlot;
import com.freshdirect.transadmin.util.TransStringUtil;

public class XlsCrisisManagerReport extends BaseXlsReport implements ICrisisManagerReport  {
	
	public static final short DEFAULT_FONT_HEIGHT = 8;
	public static final int WIDTH_MULT = 240; // width per char
	public static final int DEFAULT_WIDTH = 16; // width per char
    public static final int MIN_CHARS = 8; // minimum char width
    private short rownum;
    
	@SuppressWarnings("unchecked")
	public void generateMarketingReport(String file, CrisisManagerReportData reportData)
										throws ReportGenerationException {
		HSSFWorkbook wb = new HSSFWorkbook();
		Map styles = initStyles(wb);		
		if (reportData != null && EnumCrisisMngBatchType.REGULARORDER.equals(reportData.getBatch().getBatchType())) {
			createMarketingRegularOrderSheet(wb, reportData, styles);				
		} else {
			createMarketingStandingOrderSheet(wb, reportData, styles);
		}
		new RouteFileManager().generateReportFile(file, wb);
	}
	
	public void generateVoiceShotReport(String file, CrisisManagerReportData reportData)
										throws ReportGenerationException {
	
		HSSFWorkbook wb = new HSSFWorkbook();
		Map styles = initStyles(wb);
		if (reportData != null && EnumCrisisMngBatchType.REGULARORDER.equals(reportData.getBatch().getBatchType())) {
			createVoiceshotRegularOrderSheet(wb, reportData, styles);				
		} else {
			createVoiceshotStandingOrderSheet(wb, reportData, styles);
		}
		new RouteFileManager().generateReportFile(file, wb);
	}
	
	public void generateTimeSlotExceptionReport(String file, CrisisManagerReportData reportData)
										throws ReportGenerationException, ParseException {

		HSSFWorkbook wb = new HSSFWorkbook();
		Map styles = initStyles(wb);
		createTimeSlotExceptionSheet(wb, reportData, styles);
		new RouteFileManager().generateReportFile(file, wb);				
	}
	
	public void generateSOSimulationReport(String file, CrisisManagerReportData reportData)
										throws ReportGenerationException, ParseException {

		HSSFWorkbook wb = new HSSFWorkbook();
		Map styles = initStyles(wb);	
		createStandingOrderSimulationSheet(wb, reportData, styles);
		new RouteFileManager().generateReportFile(file, wb);				
	}
	
	public void generateSOFailureReport(String file, CrisisManagerReportData reportData)
										throws ReportGenerationException, ParseException {

		HSSFWorkbook wb = new HSSFWorkbook();
		Map styles = initStyles(wb);	
		createStandingOrderFailureSheet(wb, reportData, styles);
		new RouteFileManager().generateReportFile(file, wb);				
	}
	
	@SuppressWarnings("deprecation")
	private void createTotalRows(HSSFRow row, int totalOrders, Map styles) {
				      
		HSSFCell hssfCell = row.createCell(cellnum++);		        
        cellnum = 0;
		        
		hssfCell = row.createCell(cellnum++);		        
        hssfCell.setCellStyle((HSSFCellStyle) styles.get("boldStyle"));
        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
        hssfCell.setCellValue(new HSSFRichTextString("Total Records"));
        		        
        hssfCell = row.createCell(cellnum++);		        
        hssfCell.setCellStyle((HSSFCellStyle) styles.get("numericStyle"));
        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
        hssfCell.setCellValue(new HSSFRichTextString(totalOrders+""));
        
	}
	
	@SuppressWarnings("deprecation")
	private short createTimeSlotExceptionSheet(HSSFWorkbook wb, CrisisManagerReportData reportData, Map styles) throws ParseException {
		
		if(reportData.getTimeslots() != null) {			
						
			short rownum = 0;	
		    short cellnum = 0;
		    
		    HSSFSheet sheet = wb.createSheet("TimeSlot Exception Summary");
		    sheet.setDefaultColumnWidth((short)20);	
			sheet.setPrintGridlines(true);
		    
		    HSSFPrintSetup ps = sheet.getPrintSetup();
		    sheet.setAutobreaks(true);
	        ps.setFitHeight((short) 1);
	        ps.setFitWidth((short) 1);
	       		    
		    HSSFRow row = sheet.createRow(rownum++);
		    HSSFCell hssfCell = row.createCell(cellnum);

	        hssfCell.setCellStyle((HSSFCellStyle) styles.get("titleStyle"));
	        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
	        hssfCell.setCellValue(new HSSFRichTextString("Timeslot Exception Summary"));
	        			        
			sheet.addMergedRegion(new Region(0,(short)0,0,(short)8));
			row = sheet.createRow(rownum++);//blank Row
			row = sheet.createRow(rownum++);//blank Row
			createTotalRows(row, reportData.getTimeslots().size(), styles);		       
		    row = sheet.createRow(rownum++);//blank Row
		    cellnum = 0;
	        row = sheet.createRow(rownum++);
	        
	        hssfCell = row.createCell(cellnum++);		        
	        hssfCell.setCellStyle((HSSFCellStyle) styles.get("boldStyle"));
	        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
	        hssfCell.setCellValue(new HSSFRichTextString("AREA"));
	        		        
	        hssfCell = row.createCell(cellnum++);		        
	        hssfCell.setCellStyle((HSSFCellStyle) styles.get("boldStyle"));
	        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
	        hssfCell.setCellValue(new HSSFRichTextString("Source Window"));
	       
	        hssfCell = row.createCell(cellnum++);		        
	        hssfCell.setCellStyle((HSSFCellStyle) styles.get("boldStyle"));
	        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
	        hssfCell.setCellValue(new HSSFRichTextString("Destination Window"));
	           	        
	        Iterator<ICrisisManagerBatchDeliverySlot> _slotItr = reportData.getTimeslots().iterator();
	        while (_slotItr.hasNext()) {
	        	cellnum = 0;
	        	ICrisisManagerBatchDeliverySlot _model = _slotItr.next();
	        	
	        	row = sheet.createRow(rownum++);
	        	
				hssfCell = row.createCell(cellnum++);		        
			    hssfCell.setCellStyle((HSSFCellStyle) styles.get("textStyle"));
			    hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
			    hssfCell.setCellValue(new HSSFRichTextString(_model.getArea()));
			    
			    hssfCell = row.createCell(cellnum++);		        
			    hssfCell.setCellStyle((HSSFCellStyle) styles.get("textStyleNoWrap"));
			    hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);			    
			    hssfCell.setCellValue(new HSSFRichTextString(TransStringUtil.getServerTime(_model.getStartTime())+" - "+TransStringUtil.getServerTime(_model.getEndTime())));
			    
			    hssfCell = row.createCell(cellnum++);		        
			    hssfCell.setCellStyle((HSSFCellStyle) styles.get("textStyleNoWrap"));
			    hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
			    hssfCell.setCellValue(new HSSFRichTextString((_model.getDestStartTime() != null && _model.getDestEndTime() != null) ? TransStringUtil.getServerTime(_model.getDestStartTime())+" - "+TransStringUtil.getServerTime(_model.getDestEndTime()):""));
			    		
			}
		}
		return rownum;
	}
	
	@SuppressWarnings("deprecation")
	private short createMarketingRegularOrderSheet(HSSFWorkbook wb, CrisisManagerReportData reportData, Map styles) {
		
		if(reportData.getOrders() != null) {			
						
			short rownum = 0;	
		    short cellnum = 0;
		    
		    HSSFSheet sheet = wb.createSheet("RegularOrder Marketing Report");
		    sheet.setDefaultColumnWidth((short)DEFAULT_WIDTH);	
			sheet.setPrintGridlines(true);
		    
		    HSSFPrintSetup ps = sheet.getPrintSetup();
		    sheet.setAutobreaks(true);
	        ps.setFitHeight((short) 1);
	        ps.setFitWidth((short) 1);
	       		    
		    HSSFRow row = sheet.createRow(rownum++);
		    HSSFCell hssfCell = row.createCell(cellnum);

	      /*  hssfCell.setCellStyle((HSSFCellStyle) styles.get("titleStyle"));
	        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
	        hssfCell.setCellValue(new HSSFRichTextString("Regular Order Info"));
	        			        
			sheet.addMergedRegion(new Region(0,(short)0,0,(short)8));
			row = sheet.createRow(rownum++);//blank Row
			row = sheet.createRow(rownum++);//blank Row
			createTotalRows(row, reportData.getOrders().size(), styles);	       
		    row = sheet.createRow(rownum++);//blank Row*/	
		    cellnum = 0;
	        	        
	        hssfCell = row.createCell(cellnum++);		        
	        hssfCell.setCellStyle((HSSFCellStyle) styles.get("boldStyle"));
	        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
	        hssfCell.setCellValue(new HSSFRichTextString("Customer #"));
	        		        
	        hssfCell = row.createCell(cellnum++);		        
	        hssfCell.setCellStyle((HSSFCellStyle) styles.get("boldStyle"));
	        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
	        hssfCell.setCellValue(new HSSFRichTextString("Email Address"));
	        
	        hssfCell = row.createCell(cellnum++);		        
	        hssfCell.setCellStyle((HSSFCellStyle) styles.get("boldStyle"));
	        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
	        hssfCell.setCellValue(new HSSFRichTextString("First Name"));
	        
	        hssfCell = row.createCell(cellnum++);		        
	        hssfCell.setCellStyle((HSSFCellStyle) styles.get("boldStyle"));
	        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
	        hssfCell.setCellValue(new HSSFRichTextString("Last Name"));
	        
	        Iterator<ICancelOrderInfo> _orderItr = reportData.getOrders().iterator();
	        while (_orderItr.hasNext()) {
	        	cellnum = 0;
	        	ICancelOrderInfo _model = _orderItr.next();
	        	
	        	row = sheet.createRow(rownum++);
	        	
				hssfCell = row.createCell(cellnum++);		        
			    hssfCell.setCellStyle((HSSFCellStyle) styles.get("textStyle"));
			    hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
			    hssfCell.setCellValue(new HSSFRichTextString(_model.getCustomerId()));
			    
			    hssfCell = row.createCell(cellnum++);		        
			    hssfCell.setCellStyle((HSSFCellStyle) styles.get("textStyleNoWrap"));
			    hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
			    hssfCell.setCellValue(new HSSFRichTextString(_model.getEmail()));
			    
			    hssfCell = row.createCell(cellnum++);		        
			    hssfCell.setCellStyle((HSSFCellStyle) styles.get("textStyle"));
			    hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
			    hssfCell.setCellValue(new HSSFRichTextString(_model.getFirstName()));
			    
			    hssfCell = row.createCell(cellnum++);		        
			    hssfCell.setCellStyle((HSSFCellStyle) styles.get("textStyle"));
			    hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
			    hssfCell.setCellValue(new HSSFRichTextString(_model.getLastName()));
			    		
			}	        
		}
		return rownum;
	}
	
	@SuppressWarnings("deprecation")
	private short createMarketingStandingOrderSheet(HSSFWorkbook wb, CrisisManagerReportData reportData, Map styles) {
		if(reportData.getOrders() != null) {
			
			short rownum = 0;	
		    short cellnum = 0;
		    
		    HSSFSheet sheet = wb.createSheet("Standing Order Marketing Report");
		    sheet.setDefaultColumnWidth((short)DEFAULT_WIDTH);	
			sheet.setPrintGridlines(true);
		    
		    HSSFPrintSetup ps = sheet.getPrintSetup();
		    sheet.setAutobreaks(true);
	        ps.setFitHeight((short) 1);
	        ps.setFitWidth((short) 1);
	       		    
		    HSSFRow row = sheet.createRow(rownum++);
		    HSSFCell hssfCell = row.createCell(cellnum);

	        /*hssfCell.setCellStyle((HSSFCellStyle) styles.get("titleStyle"));
	        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
	        hssfCell.setCellValue(new HSSFRichTextString("Standing Order Info"));
	        			        
			sheet.addMergedRegion(new Region(0,(short)0,0,(short)8));
			row = sheet.createRow(rownum++);//blank Row
			row = sheet.createRow(rownum++);//blank Row
			createTotalRows(row, reportData.getOrders().size(), styles);		       
		    row = sheet.createRow(rownum++);//blank Row
*/		    cellnum = 0;
	       
	        
	        hssfCell = row.createCell(cellnum++);		        
	        hssfCell.setCellStyle((HSSFCellStyle) styles.get("boldStyle"));
	        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
	        hssfCell.setCellValue(new HSSFRichTextString("Company Name"));
	        
	        hssfCell = row.createCell(cellnum++);		        
	        hssfCell.setCellStyle((HSSFCellStyle) styles.get("boldStyle"));
	        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
	        hssfCell.setCellValue(new HSSFRichTextString("Customer #"));
	        		        
	        hssfCell = row.createCell(cellnum++);		        
	        hssfCell.setCellStyle((HSSFCellStyle) styles.get("boldStyle"));
	        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
	        hssfCell.setCellValue(new HSSFRichTextString("Email Address"));
	        
	        hssfCell = row.createCell(cellnum++);		        
	        hssfCell.setCellStyle((HSSFCellStyle) styles.get("boldStyle"));
	        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
	        hssfCell.setCellValue(new HSSFRichTextString("StandingOrder #"));
	        
	        hssfCell = row.createCell(cellnum++);		        
	        hssfCell.setCellStyle((HSSFCellStyle) styles.get("boldStyle"));
	        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
	        hssfCell.setCellValue(new HSSFRichTextString("Order #"));
	        
	        hssfCell = row.createCell(cellnum++);		        
	        hssfCell.setCellStyle((HSSFCellStyle) styles.get("boldStyle"));
	        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
	        hssfCell.setCellValue(new HSSFRichTextString("Order Status"));
	        
	        hssfCell = row.createCell(cellnum++);		        
	        hssfCell.setCellStyle((HSSFCellStyle) styles.get("boldStyle"));
	        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
	        hssfCell.setCellValue(new HSSFRichTextString("Delivery Window"));
	        
	        Iterator<ICancelOrderInfo> _orderItr = reportData.getOrders().iterator();
	        while (_orderItr.hasNext()) {
	        	cellnum = 0;
	        	ICancelOrderInfo _model = _orderItr.next();
	        	
	        	row = sheet.createRow(rownum++);
	        	
	        	hssfCell = row.createCell(cellnum++);		        
			    hssfCell.setCellStyle((HSSFCellStyle) styles.get("textStyleNoWrap"));
			    hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
			    hssfCell.setCellValue(new HSSFRichTextString(_model.getCompanyName() != null ? _model.getCompanyName() : ""));
				
	        	hssfCell = row.createCell(cellnum++);		        
			    hssfCell.setCellStyle((HSSFCellStyle) styles.get("textStyle"));
			    hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
			    hssfCell.setCellValue(new HSSFRichTextString(_model.getCustomerId()));
			    
			    hssfCell = row.createCell(cellnum++);		        
			    hssfCell.setCellStyle((HSSFCellStyle) styles.get("textStyleNoWrap"));
			    hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
			    hssfCell.setCellValue(new HSSFRichTextString(_model.getEmail()));
			    
			    hssfCell = row.createCell(cellnum++);		        
			    hssfCell.setCellStyle((HSSFCellStyle) styles.get("textStyle"));
			    hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
			    hssfCell.setCellValue(new HSSFRichTextString(_model.getStandingOrderId()));
			    
			    hssfCell = row.createCell(cellnum++);		        
			    hssfCell.setCellStyle((HSSFCellStyle) styles.get("textStyle"));
			    hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
			    hssfCell.setCellValue(new HSSFRichTextString(_model.getOrderNumber()));
			    
			    hssfCell = row.createCell(cellnum++);		        
			    hssfCell.setCellStyle((HSSFCellStyle) styles.get("textStyle"));
			    hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
			    hssfCell.setCellValue(new HSSFRichTextString(_model.getOrderStatus()));
			    
			    hssfCell = row.createCell(cellnum++);		        
			    hssfCell.setCellStyle((HSSFCellStyle) styles.get("textStyleNoWrap"));
			    hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
			    hssfCell.setCellValue(new HSSFRichTextString(_model.getDeliveryWindow()));	    
			    		
			}
		}
		return rownum;
	}
	
	@SuppressWarnings("deprecation")
	private short createVoiceshotRegularOrderSheet(HSSFWorkbook wb, CrisisManagerReportData reportData, Map styles) {
		
		if(reportData.getOrders() != null) {			
						
			short rownum = 0;	
		    short cellnum = 0;
		    
		    HSSFSheet sheet = wb.createSheet("RegularOrder Voiceshot Report");
		    sheet.setDefaultColumnWidth((short)DEFAULT_WIDTH);	
			sheet.setPrintGridlines(true);
		    
		    HSSFPrintSetup ps = sheet.getPrintSetup();
		    sheet.setAutobreaks(true);
	        ps.setFitHeight((short) 1);
	        ps.setFitWidth((short) 1);
	       		    
		    HSSFRow row = sheet.createRow(rownum++);
		    HSSFCell hssfCell = row.createCell(cellnum);

/*	        hssfCell.setCellStyle((HSSFCellStyle) styles.get("titleStyle"));
	        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
	        hssfCell.setCellValue(new HSSFRichTextString("Regular Order Voiceshot Report"));
	        			        
			sheet.addMergedRegion(new Region(0,(short)0,0,(short)8));
			row = sheet.createRow(rownum++);//blank Row
			row = sheet.createRow(rownum++);//blank Row
			createTotalRows(row, reportData.getOrders().size(), styles);		       
		    row = sheet.createRow(rownum++);//blank Row
*/		    cellnum = 0;
	        	        
	        hssfCell = row.createCell(cellnum++);		        
	        hssfCell.setCellStyle((HSSFCellStyle) styles.get("boldStyle"));
	        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
	        hssfCell.setCellValue(new HSSFRichTextString("Customer #"));
	        		        
	        hssfCell = row.createCell(cellnum++);		        
	        hssfCell.setCellStyle((HSSFCellStyle) styles.get("boldStyle"));
	        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
	        hssfCell.setCellValue(new HSSFRichTextString("Name"));
	        
	        hssfCell = row.createCell(cellnum++);		        
	        hssfCell.setCellStyle((HSSFCellStyle) styles.get("boldStyle"));
	        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
	        hssfCell.setCellValue(new HSSFRichTextString("Phone Number"));
	        	        
	        Iterator<ICancelOrderInfo> _orderItr = reportData.getOrders().iterator();
	        while (_orderItr.hasNext()) {
	        	cellnum = 0;
	        	ICancelOrderInfo _model = _orderItr.next();
	        	
	        	row = sheet.createRow(rownum++);
	        	
				hssfCell = row.createCell(cellnum++);		        
			    hssfCell.setCellStyle((HSSFCellStyle) styles.get("textStyle"));
			    hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
			    hssfCell.setCellValue(new HSSFRichTextString(_model.getCustomerId()));
			    
			    hssfCell = row.createCell(cellnum++);		        
			    hssfCell.setCellStyle((HSSFCellStyle) styles.get("textStyle"));
			    hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
			    hssfCell.setCellValue(new HSSFRichTextString(_model.getFullName()));
			    
			    hssfCell = row.createCell(cellnum++);		        
			    hssfCell.setCellStyle((HSSFCellStyle) styles.get("textStyle"));
			    hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
			    hssfCell.setCellValue(new HSSFRichTextString(_model.getPhoneNumber()));    		
			}	        
		}
		return rownum;
	}

	
	@SuppressWarnings("deprecation")
	private short createVoiceshotStandingOrderSheet(HSSFWorkbook wb, CrisisManagerReportData reportData, Map styles) {
		if(reportData.getOrders() != null) {
			
			short rownum = 0;	
		    short cellnum = 0;
		    
		    HSSFSheet sheet = wb.createSheet("StandingOrder Voiceshot Report");
		    sheet.setDefaultColumnWidth((short)DEFAULT_WIDTH);	
			sheet.setPrintGridlines(true);
		    
		    HSSFPrintSetup ps = sheet.getPrintSetup();
		    sheet.setAutobreaks(true);
	        ps.setFitHeight((short) 1);
	        ps.setFitWidth((short) 1);
	       		    
		    HSSFRow row = sheet.createRow(rownum++);
		    HSSFCell hssfCell = row.createCell(cellnum);

	     /*   hssfCell.setCellStyle((HSSFCellStyle) styles.get("titleStyle"));
	        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
	        hssfCell.setCellValue(new HSSFRichTextString("Standing Order Voiceshot Report"));
	        			        
			sheet.addMergedRegion(new Region(0,(short)0,0,(short)8));
			row = sheet.createRow(rownum++);//blank Row
			row = sheet.createRow(rownum++);//blank Row
			createTotalRows(row, reportData.getOrders().size(), styles);		       
		    row = sheet.createRow(rownum++);//blank Row*/
		    cellnum = 0;
	       	        
	        hssfCell = row.createCell(cellnum++);		        
	        hssfCell.setCellStyle((HSSFCellStyle) styles.get("boldStyle"));
	        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
	        hssfCell.setCellValue(new HSSFRichTextString("Company Name"));
	       
	        hssfCell = row.createCell(cellnum++);		        
	        hssfCell.setCellStyle((HSSFCellStyle) styles.get("boldStyle"));
	        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
	        hssfCell.setCellValue(new HSSFRichTextString("Customer #"));
	             
	        hssfCell = row.createCell(cellnum++);		        
	        hssfCell.setCellStyle((HSSFCellStyle) styles.get("boldStyle"));
	        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
	        hssfCell.setCellValue(new HSSFRichTextString("StandingOrder #"));
	       
	        hssfCell = row.createCell(cellnum++);		        
	        hssfCell.setCellStyle((HSSFCellStyle) styles.get("boldStyle"));
	        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
	        hssfCell.setCellValue(new HSSFRichTextString("Order #"));
	        
	        hssfCell = row.createCell(cellnum++);		        
	        hssfCell.setCellStyle((HSSFCellStyle) styles.get("boldStyle"));
	        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
	        hssfCell.setCellValue(new HSSFRichTextString("Order Status"));
	       	        
	        hssfCell = row.createCell(cellnum++);		        
	        hssfCell.setCellStyle((HSSFCellStyle) styles.get("boldStyle"));
	        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
	        hssfCell.setCellValue(new HSSFRichTextString("Delivery Window"));
	        
	        hssfCell = row.createCell(cellnum++);		        
	        hssfCell.setCellStyle((HSSFCellStyle) styles.get("boldStyle"));
	        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
	        hssfCell.setCellValue(new HSSFRichTextString("Business Phone"));
	        
	        hssfCell = row.createCell(cellnum++);		        
	        hssfCell.setCellStyle((HSSFCellStyle) styles.get("boldStyle"));
	        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
	        hssfCell.setCellValue(new HSSFRichTextString("Call Phone"));
	        
	        Iterator<ICancelOrderInfo> _orderItr = reportData.getOrders().iterator();
	        while (_orderItr.hasNext()) {
	        	cellnum = 0;
	        	ICancelOrderInfo _model = _orderItr.next();
	        	
	        	row = sheet.createRow(rownum++);
	        	
	        	hssfCell = row.createCell(cellnum++);		        
			    hssfCell.setCellStyle((HSSFCellStyle) styles.get("textStyleNoWrap"));
			    hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
			    hssfCell.setCellValue(new HSSFRichTextString(_model.getCompanyName() != null ? _model.getCompanyName() : ""));
				
	        	hssfCell = row.createCell(cellnum++);		        
			    hssfCell.setCellStyle((HSSFCellStyle) styles.get("textStyle"));
			    hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
			    hssfCell.setCellValue(new HSSFRichTextString(_model.getCustomerId()));
			    
			    hssfCell = row.createCell(cellnum++);		        
			    hssfCell.setCellStyle((HSSFCellStyle) styles.get("textStyle"));
			    hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
			    hssfCell.setCellValue(new HSSFRichTextString(_model.getStandingOrderId()));
			    
			    hssfCell = row.createCell(cellnum++);		        
			    hssfCell.setCellStyle((HSSFCellStyle) styles.get("textStyle"));
			    hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
			    hssfCell.setCellValue(new HSSFRichTextString(_model.getOrderNumber()));
			    
			    hssfCell = row.createCell(cellnum++);		        
			    hssfCell.setCellStyle((HSSFCellStyle) styles.get("textStyle"));
			    hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
			    hssfCell.setCellValue(new HSSFRichTextString(_model.getOrderStatus()));
			    
			    hssfCell = row.createCell(cellnum++);		        
			    hssfCell.setCellStyle((HSSFCellStyle) styles.get("textStyleNoWrap"));
			    hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
			    hssfCell.setCellValue(new HSSFRichTextString(_model.getDeliveryWindow()));
			    
			    hssfCell = row.createCell(cellnum++);		        
			    hssfCell.setCellStyle((HSSFCellStyle) styles.get("textStyle"));
			    hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
			    hssfCell.setCellValue(new HSSFRichTextString(_model.getBusinessPhone()));
			    
			    hssfCell = row.createCell(cellnum++);		        
			    hssfCell.setCellStyle((HSSFCellStyle) styles.get("textStyle"));
			    hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
			    hssfCell.setCellValue(new HSSFRichTextString(_model.getCellPhone()));
			}
		}
		return rownum;
	}
	
	@SuppressWarnings("deprecation")
	private short createStandingOrderSimulationSheet(HSSFWorkbook wb, CrisisManagerReportData reportData, Map styles) throws ParseException {
		if(reportData.getOrders() != null) {
			
			short rownum = 0;	
		    short cellnum = 0;
		    
		    HSSFSheet sheet = wb.createSheet("Standing Order Simulation Summary");
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
	        hssfCell.setCellValue(new HSSFRichTextString("Standing Order Simulation Report"));
	        			        
			sheet.addMergedRegion(new Region(0,(short)0,0,(short)8));
			row = sheet.createRow(rownum++);//blank Row
			row = sheet.createRow(rownum++);//blank Row
	        createTotalRows(row, reportData.getOrders().size(), styles);
	        
	        row = sheet.createRow(rownum++);//blank Row
	        cellnum = 0;
	        row = sheet.createRow(rownum++);	        
	        
	        hssfCell = row.createCell(cellnum++);		        
	        hssfCell.setCellStyle((HSSFCellStyle) styles.get("boldStyle"));
	        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
	        hssfCell.setCellValue(new HSSFRichTextString("Delivery Date"));
	       
	        hssfCell = row.createCell(cellnum++);		        
	        hssfCell.setCellStyle((HSSFCellStyle) styles.get("boldStyle"));
	        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
	        hssfCell.setCellValue(new HSSFRichTextString("Company Name"));
	       
	        hssfCell = row.createCell(cellnum++);		        
	        hssfCell.setCellStyle((HSSFCellStyle) styles.get("boldStyle"));
	        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
	        hssfCell.setCellValue(new HSSFRichTextString("Customer #"));
	             
	        hssfCell = row.createCell(cellnum++);		        
	        hssfCell.setCellStyle((HSSFCellStyle) styles.get("boldStyle"));
	        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
	        hssfCell.setCellValue(new HSSFRichTextString("StandingOrder #"));
	        
	        hssfCell = row.createCell(cellnum++);		        
	        hssfCell.setCellStyle((HSSFCellStyle) styles.get("boldStyle"));
	        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
	        hssfCell.setCellValue(new HSSFRichTextString("Order #"));
	        
	        hssfCell = row.createCell(cellnum++);		        
	        hssfCell.setCellStyle((HSSFCellStyle) styles.get("boldStyle"));
	        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
	        hssfCell.setCellValue(new HSSFRichTextString("Order Status"));
	       	        
	        hssfCell = row.createCell(cellnum++);		        
	        hssfCell.setCellStyle((HSSFCellStyle) styles.get("boldStyle"));
	        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
	        hssfCell.setCellValue(new HSSFRichTextString("Delivery Window"));
	        
	        hssfCell = row.createCell(cellnum++);		        
	        hssfCell.setCellStyle((HSSFCellStyle) styles.get("boldStyle"));
	        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
	        hssfCell.setCellValue(new HSSFRichTextString("Business Phone"));
	        
	        hssfCell = row.createCell(cellnum++);		        
	        hssfCell.setCellStyle((HSSFCellStyle) styles.get("boldStyle"));
	        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
	        hssfCell.setCellValue(new HSSFRichTextString("Cell Phone"));
	        
	        hssfCell = row.createCell(cellnum++);		        
	        hssfCell.setCellStyle((HSSFCellStyle) styles.get("boldStyle"));
	        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
	        hssfCell.setCellValue(new HSSFRichTextString("Order LineItem Count"));
	        
	        hssfCell = row.createCell(cellnum++);		        
	        hssfCell.setCellStyle((HSSFCellStyle) styles.get("boldStyle"));
	        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
	        hssfCell.setCellValue(new HSSFRichTextString("Template LineItem Count"));
	        
	        hssfCell = row.createCell(cellnum++);		        
	        hssfCell.setCellStyle((HSSFCellStyle) styles.get("boldStyle"));
	        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
	        hssfCell.setCellValue(new HSSFRichTextString("LineItem Change"));
	        
	        
	        Iterator<ICancelOrderInfo> _orderItr = reportData.getOrders().iterator();
	        while (_orderItr.hasNext()) {
	        	cellnum = 0;
	        	ICancelOrderInfo _model = _orderItr.next();
	        	
	        	row = sheet.createRow(rownum++);
	        	
	        	hssfCell = row.createCell(cellnum++);		        
			    hssfCell.setCellStyle((HSSFCellStyle) styles.get("textStyleNoWrap"));
			    hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
			    hssfCell.setCellValue(new HSSFRichTextString(TransStringUtil.getDate(reportData.getBatch().getDestinationDate())));
					        	
	        	hssfCell = row.createCell(cellnum++);		        
			    hssfCell.setCellStyle((HSSFCellStyle) styles.get("textStyleNoWrap"));
			    hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
			    hssfCell.setCellValue(new HSSFRichTextString(_model.getCompanyName() != null ? _model.getCompanyName() : ""));
				
	        	hssfCell = row.createCell(cellnum++);		        
			    hssfCell.setCellStyle((HSSFCellStyle) styles.get("textStyle"));
			    hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
			    hssfCell.setCellValue(new HSSFRichTextString(_model.getCustomerId()));
			    
			    hssfCell = row.createCell(cellnum++);		        
			    hssfCell.setCellStyle((HSSFCellStyle) styles.get("textStyle"));
			    hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
			    hssfCell.setCellValue(new HSSFRichTextString(_model.getStandingOrderId()));
			   
			    hssfCell = row.createCell(cellnum++);		        
			    hssfCell.setCellStyle((HSSFCellStyle) styles.get("textStyle"));
			    hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
			    hssfCell.setCellValue(new HSSFRichTextString(_model.getOrderNumber()));
			    
			    hssfCell = row.createCell(cellnum++);		        
			    hssfCell.setCellStyle((HSSFCellStyle) styles.get("textStyle"));
			    hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
			    hssfCell.setCellValue(new HSSFRichTextString(_model.getOrderStatus()));
			    
			    hssfCell = row.createCell(cellnum++);		        
			    hssfCell.setCellStyle((HSSFCellStyle) styles.get("textStyleNoWrap"));
			    hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
			    hssfCell.setCellValue(new HSSFRichTextString(_model.getDeliveryWindow()));
			    
			    hssfCell = row.createCell(cellnum++);		        
			    hssfCell.setCellStyle((HSSFCellStyle) styles.get("textStyle"));
			    hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
			    hssfCell.setCellValue(new HSSFRichTextString(_model.getBusinessPhone()));
			    
			    hssfCell = row.createCell(cellnum++);		        
			    hssfCell.setCellStyle((HSSFCellStyle) styles.get("textStyle"));
			    hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
			    hssfCell.setCellValue(new HSSFRichTextString(_model.getCellPhone()));
			    
			    hssfCell = row.createCell(cellnum++);		        
			    hssfCell.setCellStyle((HSSFCellStyle) styles.get("textStyleNoWrap"));
			    hssfCell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
			    hssfCell.setCellValue(_model.getLineItemCount());
			    
			    hssfCell = row.createCell(cellnum++);		        
			    hssfCell.setCellStyle((HSSFCellStyle) styles.get("textStyle"));
			    hssfCell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
			    hssfCell.setCellValue(_model.getTempLineItemCount());
			    
			    hssfCell = row.createCell(cellnum++);		        
			    hssfCell.setCellStyle((HSSFCellStyle) styles.get("textStyle"));
			    hssfCell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
			    hssfCell.setCellValue(_model.getLineItemChangeCount());
			    		
			}
		}
		return rownum;
	}
	
	@SuppressWarnings("deprecation")
	private short createStandingOrderFailureSheet(HSSFWorkbook wb, CrisisManagerReportData reportData, Map styles) {
		if(reportData.getOrders() != null) {
			
			short rownum = 0;	
		    short cellnum = 0;
		    
		    HSSFSheet sheet = wb.createSheet("Standing Order Failure Rpt");
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
	        hssfCell.setCellValue(new HSSFRichTextString("Standing Order Failure Report"));
	        			        
			sheet.addMergedRegion(new Region(0,(short)0,0,(short)8));
			row = sheet.createRow(rownum++);//blank Row
			row = sheet.createRow(rownum++);//blank Row
	        createTotalRows(row, reportData.getOrders().size(), styles);
	        
	        row = sheet.createRow(rownum++);//blank Row
	        cellnum = 0;
	        row = sheet.createRow(rownum++);	        
	        hssfCell = row.createCell(cellnum++);		        
	        hssfCell.setCellStyle((HSSFCellStyle) styles.get("boldStyle"));
	        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
	        hssfCell.setCellValue(new HSSFRichTextString("Company Name"));
	       
	        hssfCell = row.createCell(cellnum++);		        
	        hssfCell.setCellStyle((HSSFCellStyle) styles.get("boldStyle"));
	        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
	        hssfCell.setCellValue(new HSSFRichTextString("Customer #"));
	             
	        hssfCell = row.createCell(cellnum++);		        
	        hssfCell.setCellStyle((HSSFCellStyle) styles.get("boldStyle"));
	        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
	        hssfCell.setCellValue(new HSSFRichTextString("StandingOrder #"));
	       	              
	        hssfCell = row.createCell(cellnum++);		        
	        hssfCell.setCellStyle((HSSFCellStyle) styles.get("boldStyle"));
	        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
	        hssfCell.setCellValue(new HSSFRichTextString("Delivery Window"));
	        
	        hssfCell = row.createCell(cellnum++);		        
	        hssfCell.setCellStyle((HSSFCellStyle) styles.get("boldStyle"));
	        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
	        hssfCell.setCellValue(new HSSFRichTextString("Business Phone"));
	        
	        hssfCell = row.createCell(cellnum++);		        
	        hssfCell.setCellStyle((HSSFCellStyle) styles.get("boldStyle"));
	        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
	        hssfCell.setCellValue(new HSSFRichTextString("Cell Phone"));
	        
	        hssfCell = row.createCell(cellnum++);		        
	        hssfCell.setCellStyle((HSSFCellStyle) styles.get("boldStyle"));
	        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
	        hssfCell.setCellValue(new HSSFRichTextString("Error Detail"));
	                        
	        Iterator<ICancelOrderInfo> _orderItr = reportData.getOrders().iterator();
	        while (_orderItr.hasNext()) {
	        	cellnum = 0;
	        	ICancelOrderInfo _model = _orderItr.next();
	        	
	        	row = sheet.createRow(rownum++);
	        	
	        	hssfCell = row.createCell(cellnum++);		        
			    hssfCell.setCellStyle((HSSFCellStyle) styles.get("textStyleNoWrap"));
			    hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
			    hssfCell.setCellValue(new HSSFRichTextString(_model.getCompanyName() != null ? _model.getCompanyName() : ""));
				
	        	hssfCell = row.createCell(cellnum++);		        
			    hssfCell.setCellStyle((HSSFCellStyle) styles.get("textStyle"));
			    hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
			    hssfCell.setCellValue(new HSSFRichTextString(_model.getCustomerId()));
			    
			    hssfCell = row.createCell(cellnum++);		        
			    hssfCell.setCellStyle((HSSFCellStyle) styles.get("textStyle"));
			    hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
			    hssfCell.setCellValue(new HSSFRichTextString(_model.getStandingOrderId()));
			   			    
			    hssfCell = row.createCell(cellnum++);		        
			    hssfCell.setCellStyle((HSSFCellStyle) styles.get("textStyleNoWrap"));
			    hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
			    hssfCell.setCellValue(new HSSFRichTextString(_model.getDeliveryWindow()));
			    
			    hssfCell = row.createCell(cellnum++);		        
			    hssfCell.setCellStyle((HSSFCellStyle) styles.get("textStyle"));
			    hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
			    hssfCell.setCellValue(new HSSFRichTextString(_model.getBusinessPhone()));
			    
			    hssfCell = row.createCell(cellnum++);		        
			    hssfCell.setCellStyle((HSSFCellStyle) styles.get("textStyle"));
			    hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
			    hssfCell.setCellValue(new HSSFRichTextString(_model.getCellPhone()));
			    
			    hssfCell = row.createCell(cellnum++);		        
			    hssfCell.setCellStyle((HSSFCellStyle) styles.get("textStyleNoWrap"));
			    hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
			    hssfCell.setCellValue(new HSSFRichTextString(_model.getErrorDetail()));
		    			    		
			}
		}
		return rownum;
	}
}
