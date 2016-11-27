package com.freshdirect.transadmin.datamanager.report;

import java.text.ParseException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFPrintSetup;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.Region;

import com.freshdirect.transadmin.datamanager.RouteFileManager;
import com.freshdirect.transadmin.model.Asset;
import com.freshdirect.transadmin.model.AssetAttribute;
import com.freshdirect.transadmin.util.TransStringUtil;

public class XlsAssetReport extends BaseXlsReport  {
	
	public static final short DEFAULT_FONT_HEIGHT = 8;
	public static final int WIDTH_MULT = 240; // width per char
	public static final int DEFAULT_WIDTH = 12; // width per char
    public static final int MIN_CHARS = 8; // minimum char width
    private short rownum;	
    private short cellnum;
    
	@SuppressWarnings({ "rawtypes" })
	public void generateAssetReport(String file, Collection assets)
										throws ReportGenerationException, ParseException {

		HSSFWorkbook wb = new HSSFWorkbook();
		Map styles = initStyles(wb);
				
		if (assets != null) {		        
			 rownum = 0;	
			 cellnum = 0;
			 createDetailInfoSheet(wb, assets, styles);
		}		 

		new RouteFileManager().generateReportFile(file, wb);
	}
		
	@SuppressWarnings({ "rawtypes", "deprecation", "unchecked" })
	private short createDetailInfoSheet(HSSFWorkbook wb, Collection assets, Map styles) {
		
		if(assets != null) {
						
			Iterator _assetItr = assets.iterator();
			
			short rownum = 0;	
		    short cellnum = 0;
		    
		    HSSFSheet sheet = wb.createSheet("Asset Details");
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
	        hssfCell.setCellValue(new HSSFRichTextString("Asset Details"));
	        			        
			sheet.addMergedRegion(new Region(0,(short)0,0,(short)6));			
			int count = 0;			
			List _detailData = null;
			while (_assetItr.hasNext()) {
				Asset _asset = (Asset) _assetItr.next();				
				_detailData = TransStringUtil.asSortedList(_asset.getAssetAttributes());
								
		        Iterator _colsTripItr = _detailData.iterator();
	       		
		        row = sheet.createRow(rownum++);//blank Row
		        sheet.addMergedRegion(new Region(rownum-1,(short)0,rownum-1,(short)6));
		        row = sheet.createRow(rownum++);//blank Row
		        sheet.addMergedRegion(new Region(rownum-1,(short)0,rownum-1,(short)6));
		        
		        if(count == 0) {
			        row = sheet.createRow(rownum++);
			        cellnum = 0;
			        
			        hssfCell = row.createCell(cellnum++);		        
			        hssfCell.setCellStyle((HSSFCellStyle) styles.get("boldStyle"));
			        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
			        hssfCell.setCellValue(new HSSFRichTextString("Asset Type"));
			        
			        hssfCell = row.createCell(cellnum++);		        
			        hssfCell.setCellStyle((HSSFCellStyle) styles.get("textStyle"));
			        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
			        hssfCell.setCellValue(new HSSFRichTextString(_asset.getAssetType().getCode()));
			 	}
		        count++;
		        row = sheet.createRow(rownum++);
		        cellnum = 0;
		        
		        hssfCell = row.createCell(cellnum++);		        
		        hssfCell.setCellStyle((HSSFCellStyle) styles.get("boldStyle"));
		        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
		        hssfCell.setCellValue(new HSSFRichTextString("Asset No"));
		        
		        hssfCell = row.createCell(cellnum++);		        
		        hssfCell.setCellStyle((HSSFCellStyle) styles.get("textStyle"));
		        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
		        hssfCell.setCellValue(new HSSFRichTextString(_asset.getAssetNo()));
		        
		        cellnum = (short)(cellnum+2);
		        hssfCell = row.createCell(cellnum++);		        
		        hssfCell.setCellStyle((HSSFCellStyle) styles.get("boldStyle"));
		        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
		        hssfCell.setCellValue(new HSSFRichTextString("Vendor Truck"));
		        		        
		        hssfCell = row.createCell(cellnum++);		        
		        hssfCell.setCellStyle((HSSFCellStyle) styles.get("textStyle"));
		        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
		        hssfCell.setCellValue(new HSSFRichTextString(_asset.getVendor()));
		        		        		             
		        row = sheet.createRow(rownum++);
		        cellnum = 0;		        
		        
		        hssfCell = row.createCell(cellnum++);		        
		        hssfCell.setCellStyle((HSSFCellStyle) styles.get("boldStyle"));
		        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
		        hssfCell.setCellValue(new HSSFRichTextString("Barcode"));
		        
		        hssfCell = row.createCell(cellnum++);		        
		        hssfCell.setCellStyle((HSSFCellStyle) styles.get("textStyle"));
		        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
		        hssfCell.setCellValue(new HSSFRichTextString(_asset.getBarcode()));
		        
		        cellnum = (short)(cellnum+2);
		        hssfCell = row.createCell(cellnum++);		        
		        hssfCell.setCellStyle((HSSFCellStyle) styles.get("boldStyle"));
		        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
		        hssfCell.setCellValue(new HSSFRichTextString("Domicile"));
		        		        
		        hssfCell = row.createCell(cellnum++);		        
		        hssfCell.setCellStyle((HSSFCellStyle) styles.get("textStyle"));
		        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
		        hssfCell.setCellValue(new HSSFRichTextString(_asset.getDomicile()));
		        
		        row = sheet.createRow(rownum++);
		        cellnum = 0;
		        
		        cellnum = (short)(cellnum++);
		        hssfCell = row.createCell(cellnum++);		        
		        hssfCell.setCellStyle((HSSFCellStyle) styles.get("boldStyle"));
		        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
		        hssfCell.setCellValue(new HSSFRichTextString("Status"));
		        		        
		        hssfCell = row.createCell(cellnum++);		        
		        hssfCell.setCellStyle((HSSFCellStyle) styles.get("textStyle"));
		        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
		        hssfCell.setCellValue(new HSSFRichTextString(_asset.getAssetStatus().getDescription()));
		        		        
		        cellnum = (short)(cellnum+2);
		        hssfCell = row.createCell(cellnum++);		        
		        hssfCell.setCellStyle((HSSFCellStyle) styles.get("boldStyle"));
		        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
		        hssfCell.setCellValue(new HSSFRichTextString("Body Length"));
		        		        
		        hssfCell = row.createCell(cellnum++);		        
		        hssfCell.setCellStyle((HSSFCellStyle) styles.get("textStyle"));
		        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
		        hssfCell.setCellValue(new HSSFRichTextString(_asset.getBodyLength()));
		        
		        row = sheet.createRow(rownum++);
		        cellnum = 1;
		        
		        sheet.addMergedRegion(new Region(rownum-1,(short)1,rownum-1,(short)5));
		        hssfCell = row.createCell(cellnum++);
		        
		        hssfCell.setCellStyle((HSSFCellStyle) styles.get("titlePlainStyle"));
		        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
		        hssfCell.setCellValue(new HSSFRichTextString("Asset Summary"));
		        
		        
		        row = sheet.createRow(rownum++);
		        cellnum = 1;
		        
		        hssfCell = row.createCell(cellnum++);		        
		        hssfCell.setCellStyle((HSSFCellStyle) styles.get("textStyleNoWrap"));
		        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
		        hssfCell.setCellValue(new HSSFRichTextString("Attributes"));
		        
		        hssfCell = row.createCell(cellnum++);		        
		        hssfCell.setCellStyle((HSSFCellStyle) styles.get("textStyle"));
		        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
		        hssfCell.setCellValue(new HSSFRichTextString(""+_asset.getAssetAttributes().size()));
		        
		        row = sheet.createRow(rownum++);//blank Row
		        
		        row = sheet.createRow(rownum++);
		        cellnum = 1;
		        
		        hssfCell = row.createCell(cellnum++);		        
		        hssfCell.setCellStyle((HSSFCellStyle) styles.get("boldStyle"));
		        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
		        hssfCell.setCellValue(new HSSFRichTextString("Attribute Type"));
	              	        	        
		        hssfCell = row.createCell(cellnum++);		        
		        hssfCell.setCellStyle((HSSFCellStyle) styles.get("boldStyle"));
		        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
		        hssfCell.setCellValue(new HSSFRichTextString("Attribute Value"));
		       
		        while (_colsTripItr.hasNext()) {
		        	cellnum = 1;
		        	AssetAttribute _model = (AssetAttribute)_colsTripItr.next();
		        			        	
		        	row = sheet.createRow(rownum++);
		        						   				    
				    hssfCell = row.createCell(cellnum++);		        
				    hssfCell.setCellStyle((HSSFCellStyle) styles.get("textStyle" ));
				    hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
				    hssfCell.setCellValue(new HSSFRichTextString(_model.getId().getAttributeType()));
				    				    
				    hssfCell = row.createCell(cellnum++);		        
				    hssfCell.setCellStyle((HSSFCellStyle) styles.get("textStyle"));
				    hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
				    hssfCell.setCellValue(new HSSFRichTextString(_model.getAttributeValue()));
				}
			}
		}		        
        return rownum;
	}
	
	protected DtlAttributeSummaryData getDtlSummaryData(Set attributeLst ) {
		
		DtlAttributeSummaryData result = new DtlAttributeSummaryData();			
		if(attributeLst != null) {
			int size = attributeLst.size();
			result.setNoOfAttributes(size);
			Iterator itr = attributeLst.iterator();
			AssetAttribute _attribute = null;
			while (itr.hasNext()) {
				_attribute = (AssetAttribute) itr.next();
				result.setAttrType(_attribute.getId().getAttributeType());					
				result.setAttrValue(_attribute.getAttributeValue());
			}
		}		
		return result;
	}
	
	class DtlAttributeSummaryData {
		
		String attrType;
		String attrValue;
		int noOfAttributes;
				
		public int getNoOfAttributes() {
			return noOfAttributes;
		}
		public void setNoOfAttributes(int noOfAttributes) {
			this.noOfAttributes = noOfAttributes;
		}
		public String getAttrType() {
			return attrType;
		}
		public void setAttrType(String attrType) {
			this.attrType = attrType;
		}
		public String getAttrValue() {
			return attrValue;
		}
		public void setAttrValue(String attrValue) {
			this.attrValue = attrValue;
		}	
	}
}
