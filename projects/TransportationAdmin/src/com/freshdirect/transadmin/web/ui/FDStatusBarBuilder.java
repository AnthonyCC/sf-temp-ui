package com.freshdirect.transadmin.web.ui;

import org.extremecomponents.table.core.TableModel;
import org.extremecomponents.table.view.html.BuilderConstants;
import org.extremecomponents.table.view.html.BuilderUtils;
import org.extremecomponents.table.view.html.ToolbarBuilder;
import org.extremecomponents.table.view.html.toolbar.ImageItem;
import org.extremecomponents.table.view.html.toolbar.ToolbarItem;
import org.extremecomponents.util.HtmlBuilder;

public class FDStatusBarBuilder extends ToolbarBuilder {
	
	private final static String TOOLBAR_ADD_IMAGE = "add_new";
	private final static String TOOLBAR_DELETE_IMAGE = "delete";
	
	public FDStatusBarBuilder(TableModel model) {
        super(model);
    }
    
    public FDStatusBarBuilder(HtmlBuilder html, TableModel model) {
    	super(html, model);
    }
    
    public void addItemAsImage() {
        ImageItem item = new ImageItem();
        item.setTooltip(getMessages().getMessage(BuilderConstants.TOOLBAR_FILTER_TOOLTIP));
        item.setImage(BuilderUtils.getImage(getTableModel(), TOOLBAR_ADD_IMAGE));
        item.setAlt("Add");
        item.setStyle("border:0");
        buildAdd(getHtmlBuilder(), getTableModel(), item);
    }
    
    public void deleteItemAsImage() {
        ImageItem item = new ImageItem();
        item.setTooltip(getMessages().getMessage(BuilderConstants.TOOLBAR_FILTER_TOOLTIP));
        item.setImage(BuilderUtils.getImage(getTableModel(), TOOLBAR_DELETE_IMAGE));
        item.setAlt("Delete");
        item.setStyle("border:0");
        buildDelete(getHtmlBuilder(), getTableModel(), item);
    }
    
    public void buildAdd(HtmlBuilder html, TableModel model, ToolbarItem item) {
        item.setAction(getAddAction("edit",model));
        item.enabled(html, model);
    }

    public void buildDelete(HtmlBuilder html, TableModel model, ToolbarItem item) {
        item.setAction(getDeleteAction("delete",model));
        item.enabled(html, model);
    }
    
    
    public String getAddAction(String key, TableModel model) {
        StringBuffer result = new StringBuffer("javascript:");

        String action = model.getTableHandler().getTable().getAction();
        result.append("location.href = ").append("'").append(formatAction(action,key)).append("'");                       
        return result.toString();
    }
    
    public String getDeleteAction(String key, TableModel model) {
    	StringBuffer result = new StringBuffer("javascript:");

    	String action = model.getTableHandler().getTable().getAction();
        result.append("doDelete('").append(model.getTableHandler().getTable().getTableId())
        				.append("_table','").append(formatAction(action,key)).append("')");                       
        return result.toString();
    }
    
    private String formatAction(String action , String append) {
    	String formattedAction = action;
    	if(action != null) {
    		int intIndex = action.lastIndexOf("/");
    		if(intIndex > 0) {
    			formattedAction = action.substring(0, intIndex+1)
    								+append+action.substring(intIndex+1,action.length());
    		}
    	}
    	return formattedAction;
    }




}
