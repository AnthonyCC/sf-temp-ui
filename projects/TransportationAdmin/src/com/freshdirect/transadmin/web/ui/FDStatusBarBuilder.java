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
	private final static String TOOLBAR_CONFIRM_IMAGE = "confirm-unconfirm";
	private final static String TOOLBAR_COPY_IMAGE = "copy";
	
	private final static String TOOLBAR_GEOCODE_IMAGE = "geocode";	
	private final static String TOOLBAR_UPDATE_IMAGE = "update";
	private final static String TOOLBAR_SEND_IMAGE = "send";
	
	public FDStatusBarBuilder(TableModel model) {
        super(model);
    }
    
    public FDStatusBarBuilder(HtmlBuilder html, TableModel model) {
    	super(html, model);
    }
    
    public void addItemAsImage() {               
    	ImageItem item = itemAsImage(TOOLBAR_ADD_IMAGE, "Add");
        buildAdd(getHtmlBuilder(), getTableModel(), item);
    }
       
    public void deleteItemAsImage() {            
        
        ImageItem item = itemAsImage(TOOLBAR_DELETE_IMAGE, "Delete");
        buildDelete(getHtmlBuilder(), getTableModel(), item);
    }
    
    public void confirmItemAsImage() {        
        ImageItem item = itemAsImage(TOOLBAR_CONFIRM_IMAGE, "Confirm");
        buildConfirm(getHtmlBuilder(), getTableModel(), item);
    }
    
    public void copyItemAsImage() {
    	ImageItem item = itemAsImage(TOOLBAR_COPY_IMAGE, "Copy");
        buildCopy(getHtmlBuilder(), getTableModel(), item);
    }
    
    public void geocodeItemAsImage() {
    	ImageItem item = itemAsImage(TOOLBAR_GEOCODE_IMAGE, "Geocode");
        buildGeocode(getHtmlBuilder(), getTableModel(), item);
    }
    
    public void updateItemAsImage() {
    	ImageItem item = itemAsImage(TOOLBAR_UPDATE_IMAGE, "Update");
        buildUpdate(getHtmlBuilder(), getTableModel(), item);
    }
    
    public void sendItemAsImage() {
    	ImageItem item = itemAsImage(TOOLBAR_SEND_IMAGE, "Send");
        buildSend(getHtmlBuilder(), getTableModel(), item);
    }
    
    private ImageItem itemAsImage(String image, String altText) {
    	
    	ImageItem item = new ImageItem();
        item.setTooltip(getMessages().getMessage(BuilderConstants.TOOLBAR_FILTER_TOOLTIP));
        item.setImage(BuilderUtils.getImage(getTableModel(), image));
        item.setAlt(altText);
        item.setStyle("border:0");
        return item;
    }
            
    public void buildAdd(HtmlBuilder html, TableModel model, ToolbarItem item) {
        item.setAction(getAddAction("edit",model));
        item.enabled(html, model);
    }

    public void buildDelete(HtmlBuilder html, TableModel model, ToolbarItem item) {
        item.setAction(getDeleteAction("delete",model));
        item.enabled(html, model);
    }
    
    public void buildConfirm(HtmlBuilder html, TableModel model, ToolbarItem item) {
        item.setAction(getConfirmAction("confirm",model));
        item.enabled(html, model);
    }
    
    public void buildCopy(HtmlBuilder html, TableModel model, ToolbarItem item) {
        item.setAction(getAddAction("copy",model));
        item.enabled(html, model);
    }
    
    public void buildGeocode(HtmlBuilder html, TableModel model, ToolbarItem item) {
        item.setAction(getGeocodeAction("geocode",model));
        item.enabled(html, model);
    }
    
    public void buildUpdate(HtmlBuilder html, TableModel model, ToolbarItem item) {
        item.setAction(getUpdateAction("update",model));
        item.enabled(html, model);
    }
    
    public void buildSend(HtmlBuilder html, TableModel model, ToolbarItem item) {
        item.setAction(getSendAction("send",model));
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
    
    public String getGeocodeAction(String key, TableModel model) {
    	StringBuffer result = new StringBuffer("javascript:");

    	String action = model.getTableHandler().getTable().getAction();
        result.append("doGeocode('").append(model.getTableHandler().getTable().getTableId())
        				.append("_table','").append(formatAction(action,key)).append("')");                       
        return result.toString();
    }
    
    public String getUpdateAction(String key, TableModel model) {
    	StringBuffer result = new StringBuffer("javascript:");

    	String action = model.getTableHandler().getTable().getAction();
        result.append("doUpdate('").append(model.getTableHandler().getTable().getTableId())
        				.append("_table','").append(formatAction(action,key)).append("')");                       
        return result.toString();
    }
    
    public String getSendAction(String key, TableModel model) {
    	StringBuffer result = new StringBuffer("javascript:");

    	String action = model.getTableHandler().getTable().getAction();
        result.append("doSend('").append(model.getTableHandler().getTable().getTableId())
        				.append("_table','").append(formatAction(action,key)).append("')");                       
        return result.toString();
    }
    
    public String getConfirmAction(String key, TableModel model) {
    	StringBuffer result = new StringBuffer("javascript:");

    	String action = model.getTableHandler().getTable().getAction();
        result.append("doConfirm('").append(model.getTableHandler().getTable().getTableId())
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
