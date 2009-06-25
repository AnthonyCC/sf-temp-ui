package com.freshdirect.transadmin.datamanager.report;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

import com.freshdirect.transadmin.util.TransStringUtil;

public class BaseXlsReport {
	
	public static final short DEFAULT_FONT_HEIGHT = 8;
	public static final int WIDTH_MULT = 240; // width per char
	public static final int DEFAULT_WIDTH = 12; // width per char
    public static final int MIN_CHARS = 8; // minimum char width
    protected short rownum;	
    protected short cellnum;
    
    protected Map initStyles(HSSFWorkbook wb) {
        return initStyles(wb, DEFAULT_FONT_HEIGHT);
    }

    protected Map initStyles(HSSFWorkbook wb, short fontHeight) {
        Map result = new HashMap();
        HSSFCellStyle titleStyle = wb.createCellStyle();
        HSSFCellStyle textStyle = wb.createCellStyle();
        HSSFCellStyle boldStyle = wb.createCellStyle();
        HSSFCellStyle boldRightAlignStyle = wb.createCellStyle();
        HSSFCellStyle textStyleNoWrap = wb.createCellStyle();
        HSSFCellStyle numericStyle = wb.createCellStyle();
        HSSFCellStyle numericStyleBold = wb.createCellStyle();
        
        HSSFCellStyle numericStyle_Totals = wb.createCellStyle();
        HSSFCellStyle percentStyle_Totals = wb.createCellStyle();
        HSSFCellStyle textStyle_Totals = wb.createCellStyle();

        result.put("titleStyle", titleStyle);
        result.put("textStyle", textStyle);
        result.put("boldStyle", boldStyle);
        result.put("boldRightAlignStyle", boldRightAlignStyle);
        result.put("textStyleNoWrap", textStyleNoWrap);
        
        result.put("numericStyle", numericStyle);
        result.put("numericStyleBold", numericStyleBold);
        result.put("numericStyle_Totals", numericStyle_Totals);
        result.put("percentStyle_Totals", percentStyle_Totals);
        result.put("textStyle_Totals", textStyle_Totals);

        // Global fonts
        HSSFFont font = wb.createFont();
        font.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
        font.setColor(HSSFColor.BLACK.index);
        font.setFontName(HSSFFont.FONT_ARIAL);
        font.setFontHeightInPoints(fontHeight);
        
        HSSFFont titleFont = wb.createFont();
        titleFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        titleFont.setColor(HSSFColor.BLACK.index);
        titleFont.setFontName(HSSFFont.FONT_ARIAL);
        titleFont.setFontHeightInPoints((short)(fontHeight+4));
        

        HSSFFont fontBold = wb.createFont();
        fontBold.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        fontBold.setColor(HSSFColor.BLACK.index);
        fontBold.setFontName(HSSFFont.FONT_ARIAL);
        font.setFontHeightInPoints(fontHeight);
     

        // Standard Numeric Style
        numericStyle.setFont(font);
        numericStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);

        // Standard Numeric Style Bold
        numericStyleBold.setFont(fontBold);
        numericStyleBold.setAlignment(HSSFCellStyle.ALIGN_RIGHT);

        // Title Style
        titleStyle.setFont(titleFont);
        titleStyle.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        titleStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        titleStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        titleStyle.setBottomBorderColor(HSSFColor.BLACK.index);
        titleStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        titleStyle.setLeftBorderColor(HSSFColor.BLACK.index);
        titleStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        titleStyle.setRightBorderColor(HSSFColor.BLACK.index);
        titleStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        titleStyle.setTopBorderColor(HSSFColor.BLACK.index);
        titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        titleStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

        // Standard Text Style
        textStyle.setFont(font);
        textStyle.setWrapText(true);

        // Standard Text Style
        boldStyle.setFont(fontBold);
        boldStyle.setWrapText(true);
        
        boldRightAlignStyle.setFont(fontBold);
        boldRightAlignStyle.setWrapText(true);
        boldRightAlignStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        
        textStyleNoWrap.setFont(font);
        textStyleNoWrap.setWrapText(false);
          
        
        
       
        // Numeric Style Total
        numericStyle_Totals.setFont(fontBold);
        numericStyle_Totals.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        numericStyle_Totals.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        numericStyle_Totals.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        numericStyle_Totals.setBottomBorderColor(HSSFColor.BLACK.index);
        numericStyle_Totals.setBorderTop(HSSFCellStyle.BORDER_THIN);
        numericStyle_Totals.setTopBorderColor(HSSFColor.BLACK.index);
        numericStyle_Totals.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        numericStyle_Totals.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

        
        // Text Style Total
        textStyle_Totals.setFont(fontBold);
        textStyle_Totals.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        textStyle_Totals.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        textStyle_Totals.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        textStyle_Totals.setBottomBorderColor(HSSFColor.BLACK.index);
        textStyle_Totals.setBorderTop(HSSFCellStyle.BORDER_THIN);
        textStyle_Totals.setTopBorderColor(HSSFColor.BLACK.index);
        textStyle_Totals.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        textStyle_Totals.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

        return result;
    }
    
    //  add to set Cell encoding
    protected void setCellEncoding(HSSFCell cell) {
    	cell.setEncoding(HSSFCell.ENCODING_UTF_16);
    }
    
    protected String getFormattedDate(Date date) {
    	try {
			return TransStringUtil.getServerDate(date);
		} catch (ParseException exp) {
			return "Error Formatting Date";
		}
    }
}
