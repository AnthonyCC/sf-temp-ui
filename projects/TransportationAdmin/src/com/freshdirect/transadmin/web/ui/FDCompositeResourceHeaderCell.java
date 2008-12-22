package com.freshdirect.transadmin.web.ui;

import org.apache.commons.lang.StringUtils;
import org.extremecomponents.table.bean.Column;
import org.extremecomponents.table.cell.Cell;
import org.extremecomponents.table.core.MessagesConstants;
import org.extremecomponents.table.core.PreferencesConstants;
import org.extremecomponents.table.core.TableConstants;
import org.extremecomponents.table.core.TableModel;
import org.extremecomponents.table.core.TableModelUtils;
import org.extremecomponents.table.view.html.BuilderConstants;
import org.extremecomponents.table.view.html.BuilderUtils;
import org.extremecomponents.table.view.html.TableActions;
import org.extremecomponents.util.HtmlBuilder;

public class FDCompositeResourceHeaderCell  implements Cell{
	
	public String getExportDisplay(TableModel model, Column column) {
        return column.getTitle();
    }

    public String getHtmlDisplay(TableModel model, Column column) {
        HtmlBuilder html = new HtmlBuilder();

        String headerClass = null;
        String sortImage = null;
        String sortOrder = null;

        headerClass = column.getHeaderClass();
       

        buildHeaderHtml(html, model, column, headerClass, sortImage, sortOrder);

        return html.toString();
    }

    protected void buildHeaderHtml(HtmlBuilder html, TableModel model, Column column, String headerClass, String sortImage, String sortOrder) {
        html.td(2);

        if (StringUtils.isNotEmpty(headerClass)) {
            html.styleClass(headerClass);
        }

        if (StringUtils.isNotEmpty(column.getHeaderStyle())) {
            html.style(column.getHeaderStyle());
        }

        if (StringUtils.isNotEmpty(column.getWidth())) {
            html.width(column.getWidth());
        }
        

        html.close();
        
        html.table(0).close();
        html.tr(0).close();
        html.td(0).append(" colspan=\"2\" ").styleClass(headerClass).close().append(column.getTitle()).tdEnd();
        html.trEnd(0);
        html.tr(0).close();
        html.td(0).styleClass(headerClass).close().append("Max.").tdEnd();
        html.td(0).styleClass(headerClass).close().append("Req.").tdEnd();
        html.trEnd(0);
        html.tableEnd(0);
        //html.append(column.getTitle());
        

        html.tdEnd();
    }
}
