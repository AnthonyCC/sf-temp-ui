package com.freshdirect.transadmin.web.ui;

import org.apache.commons.lang.StringUtils;
import org.extremecomponents.table.bean.Column;
import org.extremecomponents.table.cell.FilterCell;
import org.extremecomponents.table.core.TableConstants;
import org.extremecomponents.table.core.TableModel;
import org.extremecomponents.table.view.html.TableActions;
import org.extremecomponents.util.HtmlBuilder;

public class FDFilterCell extends FilterCell {
	
	    public String getHtmlDisplay(TableModel model, Column column) {
	        HtmlBuilder html = new HtmlBuilder();

	        html.td(2);

	        if (StringUtils.isNotEmpty(column.getFilterClass())) {
	            html.styleClass(column.getFilterClass());
	        }

	        if (StringUtils.isNotEmpty(column.getFilterStyle())) {
	            html.style(column.getFilterStyle());
	        }

	        if (StringUtils.isNotEmpty(column.getWidth())) {
	            html.width(column.getWidth());
	        }

	        html.close();

	        if (!column.isFilterable()) {
	            html.append("");
	        } else {
	            html.append(input(model, column));
	        }

	        html.tdEnd();

	        return html.toString();
	    }

	    /**
	     * If the filter is specified the default is to use a <input type=text> tag.
	     */
	    private String input(TableModel model, Column column) {
	        HtmlBuilder html = new HtmlBuilder();

	        html.input("text");
	        html.name(model.getTableHandler().prefixWithTableId() + TableConstants.FILTER + column.getAlias());
	        html.size(column.getWidth());

	        String value = column.getValueAsString();
	        if (StringUtils.isNotBlank(value)) {
	            html.value(value);
	        }

	        StringBuffer onkeypress = new StringBuffer();
	        onkeypress.append("if (event.keyCode == 13) {");
	        onkeypress.append(new TableActions(model).getFilterAction());
	        onkeypress.append("}");
	        html.onkeypress(onkeypress.toString());

	        html.xclose();

	        return html.toString();
	    }

}
