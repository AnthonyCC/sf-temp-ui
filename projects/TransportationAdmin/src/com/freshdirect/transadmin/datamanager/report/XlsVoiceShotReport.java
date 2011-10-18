package com.freshdirect.transadmin.datamanager.report;

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

import com.freshdirect.transadmin.datamanager.RouteFileManager;
import com.freshdirect.transadmin.datamanager.model.ICancelOrderInfo;
import com.freshdirect.transadmin.datamanager.report.model.CrisisManagerReportData;

public class XlsVoiceShotReport extends BaseXlsReport implements IVoiceShotReport  {
	
	public static final short DEFAULT_FONT_HEIGHT = 8;
	public static final int WIDTH_MULT = 240; // width per char
	public static final int DEFAULT_WIDTH = 12; // width per char
    public static final int MIN_CHARS = 8; // minimum char width
    private short rownum;	
    private short cellnum;
    
   

	public void generateVoiceShotReport(String file, CrisisManagerReportData reportData)
										throws ReportGenerationException {

		HSSFWorkbook wb = new HSSFWorkbook();
		Map styles = initStyles(wb);
		
		if (reportData != null) {			
			createRegularOrderSheet(wb, reportData, styles);
			createStandingOrderSheet(wb, reportData, styles);	
		}
		
		new RouteFileManager().generateReportFile(file, wb);
	}

	private short createRegularOrderSheet(HSSFWorkbook wb, CrisisManagerReportData reportData, Map styles) {
		
		if(reportData.getRegularOrders() != null) {			
						
			short rownum = 0;	
		    short cellnum = 0;
		    
		    HSSFSheet sheet = wb.createSheet("Regular Orders");
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
	        hssfCell.setCellValue(new HSSFRichTextString("Regular Order Info"));
	        			        
			sheet.addMergedRegion(new Region(0,(short)0,0,(short)8));
			row = sheet.createRow(rownum++);//blank Row
			
	        row = sheet.createRow(rownum++);
	        
	        hssfCell = row.createCell(cellnum++);		        
	        hssfCell.setCellStyle((HSSFCellStyle) styles.get("boldStyle"));
	        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
	        hssfCell.setCellValue(new HSSFRichTextString("CUSTOMER_ID"));
	        		        
	        hssfCell = row.createCell(cellnum++);		        
	        hssfCell.setCellStyle((HSSFCellStyle) styles.get("boldStyle"));
	        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
	        hssfCell.setCellValue(new HSSFRichTextString("NAME"));
	        
	        hssfCell = row.createCell(cellnum++);		        
	        hssfCell.setCellStyle((HSSFCellStyle) styles.get("boldStyle"));
	        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
	        hssfCell.setCellValue(new HSSFRichTextString("PHONE NUMBER"));
	        	        
	        Iterator<ICancelOrderInfo> _orderItr = reportData.getRegularOrders().iterator();
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

	
	private short createStandingOrderSheet(HSSFWorkbook wb, CrisisManagerReportData reportData, Map styles) {
		if(reportData.getStandingOrders() != null) {
			
			short rownum = 0;	
		    short cellnum = 0;
		    
		    HSSFSheet sheet = wb.createSheet("Standing Order Summary");
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
	        hssfCell.setCellValue(new HSSFRichTextString("Standing Order Info"));
	        			        
			sheet.addMergedRegion(new Region(0,(short)0,0,(short)8));
			row = sheet.createRow(rownum++);//blank Row
			
	        row = sheet.createRow(rownum++);
	        
	        hssfCell = row.createCell(cellnum++);		        
	        hssfCell.setCellStyle((HSSFCellStyle) styles.get("boldStyle"));
	        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
	        hssfCell.setCellValue(new HSSFRichTextString(""));
	        
	        hssfCell = row.createCell(cellnum++);		        
	        hssfCell.setCellStyle((HSSFCellStyle) styles.get("boldStyle"));
	        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
	        hssfCell.setCellValue(new HSSFRichTextString("CUSTOMER_ID"));
	             
	        hssfCell = row.createCell(cellnum++);		        
	        hssfCell.setCellStyle((HSSFCellStyle) styles.get("boldStyle"));
	        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
	        hssfCell.setCellValue(new HSSFRichTextString("ORDER_ID"));
	        
	        hssfCell = row.createCell(cellnum++);		        
	        hssfCell.setCellStyle((HSSFCellStyle) styles.get("boldStyle"));
	        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
	        hssfCell.setCellValue(new HSSFRichTextString("DELIVERY_WINDOW"));
	        
	        hssfCell = row.createCell(cellnum++);		        
	        hssfCell.setCellStyle((HSSFCellStyle) styles.get("boldStyle"));
	        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
	        hssfCell.setCellValue(new HSSFRichTextString("BUSINESS PHONE"));
	        
	        hssfCell = row.createCell(cellnum++);		        
	        hssfCell.setCellStyle((HSSFCellStyle) styles.get("boldStyle"));
	        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
	        hssfCell.setCellValue(new HSSFRichTextString("CELL PHONE"));
	        
	        Iterator<ICancelOrderInfo> _orderItr = reportData.getStandingOrders().iterator();
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
			    hssfCell.setCellValue(new HSSFRichTextString(_model.getOrderNumber()));
			    
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
	
}
