package com.freshdirect.transadmin.datamanager.report;

import java.util.Iterator;
import java.util.List;
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
import com.freshdirect.transadmin.util.TransStringUtil;
import com.freshdirect.transadmin.web.model.SpatialBoundary;

public class XlsCommunityReport extends BaseXlsReport implements ICommunityReport  {
	
	public static final short DEFAULT_FONT_HEIGHT = 8;
	public static final int WIDTH_MULT = 240; // width per char
	public static final int DEFAULT_WIDTH = 12; // width per char
    public static final int MIN_CHARS = 8; // minimum char width
    private short rownum;	
    private short cellnum;
    
        
	public void generateCommunityReport(String file, Map reportData, Map stopCount, String routeDate, String cutOff)
										throws ReportGenerationException {

		HSSFWorkbook wb = new HSSFWorkbook();
		Map styles = initStyles(wb);
		
				
		HSSFSheet sheet = wb.createSheet("Community Report");
		sheet.setDefaultColumnWidth((short)DEFAULT_WIDTH);	
		sheet.setPrintGridlines(true);
		    
	    HSSFPrintSetup ps = sheet.getPrintSetup();
	    sheet.setAutobreaks(true);
        ps.setFitHeight((short) 1);
        ps.setFitWidth((short) 1);
				
		if (reportData != null && stopCount != null) {
			
		    rownum = 0;	
		    cellnum = 0;
		    
		    HSSFRow row = sheet.createRow(rownum++);
		    HSSFCell hssfCell = row.createCell(cellnum);

	        setCellEncoding(hssfCell);

	        hssfCell.setCellStyle((HSSFCellStyle) styles.get("titleStyle"));
	        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
	        hssfCell.setCellValue(new HSSFRichTextString(REPORT_TITLE));
	        //int valWidth = (CUTOFFREPORT_TITLE).length() * WIDTH_MULT;
	        
			sheet.addMergedRegion(new Region(0,(short)0,0,(short)8));
			row = sheet.createRow(rownum++);//blank Row
			
	        cellnum = 0;
	        row = sheet.createRow(rownum++);
	        
	        hssfCell = row.createCell(cellnum++);		        
	        hssfCell.setCellStyle((HSSFCellStyle) styles.get("boldStyle"));
	        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
	        hssfCell.setCellValue(new HSSFRichTextString(REPORT_DATETITLE));
	        		        
	        hssfCell = row.createCell(cellnum++);		        
	        hssfCell.setCellStyle((HSSFCellStyle) styles.get("boldStyle"));
	        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
	        hssfCell.setCellValue(new HSSFRichTextString(routeDate));
	        
	        hssfCell = row.createCell(cellnum++);		        
	        hssfCell.setCellStyle((HSSFCellStyle) styles.get("boldStyle"));
	        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
	        hssfCell.setCellValue(new HSSFRichTextString(REPORT_CUTOFFTITLE));
	        
	        hssfCell = row.createCell(cellnum++);		        
	        hssfCell.setCellStyle((HSSFCellStyle) styles.get("boldStyle"));
	        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
	        hssfCell.setCellValue(new HSSFRichTextString(cutOff));
			
	        row = sheet.createRow(rownum++);//blank Row
	        row = sheet.createRow(rownum++);//blank Row
	        
	        cellnum = 0;
	        row = sheet.createRow(rownum++);
	        
	        hssfCell = row.createCell(cellnum++);		        
	        hssfCell.setCellStyle((HSSFCellStyle) styles.get("boldStyle"));
	        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
	        hssfCell.setCellValue(new HSSFRichTextString("Route #"));
	        		        
	        hssfCell = row.createCell(cellnum++);		        
	        hssfCell.setCellStyle((HSSFCellStyle) styles.get("boldStyle"));
	        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
	        hssfCell.setCellValue(new HSSFRichTextString("Community"));
	        
	        hssfCell = row.createCell(cellnum++);		        
	        hssfCell.setCellStyle((HSSFCellStyle) styles.get("boldStyle"));
	        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
	        hssfCell.setCellValue(new HSSFRichTextString("% of Community"));
	        
			Iterator _routeItr = reportData.keySet().iterator();
			Map routeData = null;
			String routeId = null;
			double totalStop = 0;
			
			while(_routeItr.hasNext()) {
				routeId  = (String)_routeItr.next();
				routeData = (Map)reportData.get(routeId);
				
				if(stopCount.containsKey(routeId)) {
					totalStop = ((Integer)stopCount.get(routeId)).intValue();
				}
				if(routeData != null) {
					
					Iterator _routeDataItr = routeData.keySet().iterator();
					List routeStopData = null;
					SpatialBoundary communityId = null;					
					
					boolean isFirst = true;
					while(_routeDataItr.hasNext()) {
						communityId  = (SpatialBoundary)_routeDataItr.next();
						routeStopData = (List)routeData.get(communityId);
						
						cellnum = 0;
				        row = sheet.createRow(rownum++);
				        
				        hssfCell = row.createCell(cellnum++);		        
				        hssfCell.setCellStyle((HSSFCellStyle) styles.get("textStyle"));
				        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
				        hssfCell.setCellValue(new HSSFRichTextString((isFirst?routeId:" ")));
				        		        
				        hssfCell = row.createCell(cellnum++);		        
				        hssfCell.setCellStyle((HSSFCellStyle) styles.get("textStyle"));
				        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
				        hssfCell.setCellValue(new HSSFRichTextString(communityId.getName()));
				        
				        hssfCell = row.createCell(cellnum++);		        
				        hssfCell.setCellStyle((HSSFCellStyle) styles.get("numericStyleBold"));
				        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
				        hssfCell.setCellValue(TransStringUtil.formatSingleDigitNumber((routeStopData.size()/totalStop)*100));
				        isFirst = false;
					}
				}				
			}
		}		 

		new RouteFileManager().generateReportFile(file, wb);
	}
	
	
	
}
