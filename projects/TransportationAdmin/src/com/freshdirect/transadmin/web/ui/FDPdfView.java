package com.freshdirect.transadmin.web.ui;

import java.util.Iterator;
import java.util.List;

import org.extremecomponents.table.bean.Column;
import org.extremecomponents.table.bean.Export;
import org.extremecomponents.table.calc.CalcResult;
import org.extremecomponents.table.calc.CalcUtils;
import org.extremecomponents.table.core.TableModel;
import org.extremecomponents.table.view.PdfView;
import org.extremecomponents.util.ExtremeUtils;

public class FDPdfView extends PdfView {
	
	public String regionBefore(TableModel model) {
        StringBuffer sb = new StringBuffer();

        Export export = model.getExportHandler().getCurrentExport();

        String headerBackgroundColor = getDefaultHeaderBgColor(export);//export.getAttributeAsString(HEADER_BACKGROUND_COLOR);
        
        sb.append(" <fo:static-content flow-name=\"xsl-region-before\"> ");

        String title = export.getAttributeAsString(HEADER_TITLE);

        sb.append(" <fo:block space-after.optimum=\"15pt\" color=\"" + headerBackgroundColor + "\" font-size=\"17pt\" font-family=\"" + getHeadFont() + "'Times'\">" + title + "</fo:block> ");

        sb.append(" </fo:static-content> ");

        return sb.toString();
    }
	
	public String header(TableModel model) {
        StringBuffer sb = new StringBuffer();

        Export export = model.getExportHandler().getCurrentExport();
        String headerColor = getDefaultHeaderFgColor(export);//export.getAttributeAsString(HEADER_COLOR);
        String headerBackgroundColor = getDefaultHeaderBgColor(export);//export.getAttributeAsString(HEADER_BACKGROUND_COLOR);

        sb.append(" <fo:table-header background-color=\"" + headerBackgroundColor + "\" color=\"" + headerColor + "\"> ");

        sb.append(" <fo:table-row> ");

        List columns = model.getColumnHandler().getHeaderColumns();
        for (Iterator iter = columns.iterator(); iter.hasNext();) {
            Column column = (Column) iter.next();
            String title = column.getCellDisplay();
            sb.append(" <fo:table-cell border=\"solid silver .5px\" text-align=\"center\" display-align=\"center\" padding=\"3pt\"> ");
            sb.append(" <fo:block" + getFont() + ">" + title + "</fo:block> ");
            sb.append(" </fo:table-cell> ");
        }

        sb.append(" </fo:table-row> ");

        sb.append(" </fo:table-header> ");

        return sb.toString();
    }
	
	public StringBuffer totals(TableModel model) {

        StringBuffer sb = new StringBuffer();
        Export export = model.getExportHandler().getCurrentExport();
        String headerColor = getDefaultHeaderFgColor(export);//export.getAttributeAsString(HEADER_COLOR);
        String headerBackgroundColor = getDefaultHeaderBgColor(export);//export.getAttributeAsString(HEADER_BACKGROUND_COLOR);

        Column firstCalcColumn = model.getColumnHandler().getFirstCalcColumn();

        if (firstCalcColumn != null) {
            int rows = firstCalcColumn.getCalc().length;
            for (int i = 0; i < rows; i++) {
                sb.append("<fo:table-row>");
                for (Iterator iter = model.getColumnHandler().getColumns().iterator(); iter.hasNext();) {
                    Column column = (Column) iter.next();
                    if (column.isFirstColumn()) {
                        String calcTitle = CalcUtils.getFirstCalcColumnTitleByPosition(model, i);
                        sb.append(" <fo:table-cell border=\"solid silver .5px\" text-align=\"center\" display-align=\"center\" padding=\"3pt\" background-color=\"");
                        sb.append(headerBackgroundColor + "\" color=\"" + headerColor + "\">");
                        sb.append(" <fo:block " + getFont() + ">" + calcTitle);
                        sb.append(" </fo:block></fo:table-cell> ");
                        continue;
                    }
                    if (column.isCalculated()) {
                        sb.append(" <fo:table-cell border=\"solid silver .5px\" text-align=\"center\" display-align=\"center\" padding=\"3pt\" background-color=\"");
                        sb.append(headerBackgroundColor + "\" color=\"" + headerColor + "\"> ");
                        sb.append(" <fo:block " + getFont() + ">");
                        CalcResult calcResult = CalcUtils.getCalcResultsByPosition(model, column, i);
                        Number value = calcResult.getValue();
                        if (value != null) {
                            sb.append(ExtremeUtils.formatNumber(column.getFormat(), value, model.getLocale()));
                        } else {
                            sb.append("n/a");
                        }
                        sb.append("</fo:block> ");
                    } else {
                        sb.append(" <fo:table-cell border=\"solid silver .5px\" text-align=\"center\" display-align=\"center\" padding=\"3pt\" background-color=\"");
                        sb.append(headerBackgroundColor + "\" color=\"" + headerColor + "\"> ");
                        sb.append(" <fo:block " + getFont() + ">");
                        sb.append(" ");
                        sb.append("</fo:block> ");
                    }
                    sb.append(" </fo:table-cell> ");
                }
                sb.append("</fo:table-row>");

            }
        }
        return sb;
    }
	
	private String getDefaultHeaderBgColor(Export export) {
		String headerBackgroundColor = export.getAttributeAsString(HEADER_BACKGROUND_COLOR);
        
        if(headerBackgroundColor == null) {
        	headerBackgroundColor = "green";
        }
        return headerBackgroundColor;
	}
	
	private String getDefaultHeaderFgColor(Export export) {
		String headerColor = export.getAttributeAsString(HEADER_COLOR);
		if(headerColor == null) {
			headerColor = "black";
        }
        return headerColor;
	}
}
