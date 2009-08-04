package com.freshdirect.transadmin.web.ui;

import org.extremecomponents.table.bean.Column;
import org.extremecomponents.table.core.TableModel;
import org.extremecomponents.util.HtmlBuilder;

import com.freshdirect.transadmin.util.EnumStatus;
import com.freshdirect.transadmin.web.model.DispatchCommand;


public class DispatchDBTableView extends FlatTableView {
	
	private boolean isOpenTBody = false;
	
	private boolean hadReady = false;
	
	private boolean closedReady = false;
	
	
	protected void beforeBodyInternal(TableModel model) {
        toolbar(getHtmlBuilder(), getTableModel());
        
        getTableBuilder().tableStart();

        getTableBuilder().theadStart();
        
        statusBar(getHtmlBuilder(), getTableModel());
        
        getTableBuilder().filterRow();

        getTableBuilder().headerRow();

        getTableBuilder().theadEnd();

        getTableBuilder().tbodyStart();
        
        isOpenTBody = true;
    }
	
	public void body(TableModel model, Column column) {
		
		DispatchCommand command = (DispatchCommand) model.getCurrentRowBean();
		
		if(isReady(command)) {
			hadReady = true;
		}
		
        if (column.isFirstColumn()) {
        	
        	if(hadReady && !closedReady && !isReady(command)) 
        	{
        		//line between ready and non-ready state
        		HtmlBuilder html =getHtmlBuilder();
        		html.tr(1);
        		html.styleClass("red");                
                html.close();
                html.td(1).colSpan("10");                
                html.close();
                html.append("<hr>");
                html.tdEnd();
        		this.getRowBuilder().rowEnd();
        		
        		
        		
        		getTableBuilder().tbodyEnd();
        		isOpenTBody = false;
        		closedReady = true;
        	}
        	if(!isOpenTBody) {
        		
        		getTableBuilder().tbodyStart();                
                isOpenTBody = true;
        	}
            this.getRowBuilder().rowStart();
            
        }
        
        getHtmlBuilder().append(column.getCellDisplay());

        if (column.isLastColumn()) {
        	this.getRowBuilder().rowEnd();
        	/*if(hadReady && !closedReady && !isReady(command)) {
        		getTableBuilder().tbodyEnd();
        		isOpenTBody = false;
        		closedReady = true;
        	} */       	
        }
    }
	
	private boolean isReady(DispatchCommand command) {
			return command.getDispatchStatus() != null && command.getDispatchStatus().equals(EnumStatus.Ready);
				//DispatchPlanUtil.categorizeDispatch(command) < 0;
				//(!(command.getDispatchTime()!= null && command.getDispatchTime().trim().length()>0) && !TransStringUtil.isEmpty(command.getZoneName()));
	}


    protected void afterBodyInternal(TableModel model) {
       
    	if(isOpenTBody) {
    		getTableBuilder().tbodyEnd();
    	}
    	
        getTableBuilder().tableEnd();
    }

}

