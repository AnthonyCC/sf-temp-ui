package com.freshdirect.athena.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import javax.servlet.http.HttpServletRequest;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import com.freshdirect.athena.config.Api;
import com.freshdirect.athena.data.Data;
import com.freshdirect.athena.data.Row;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.ui.Table;
import com.vaadin.ui.Window;

public class APIDataTestWindow extends Window {
    
	private Table emContainer = new Table();
		    
	public APIDataTestWindow() {
		
    	super("API Data Tester");
    	setWidth("95%");
        setHeight("95%");
       
        addComponent(emContainer);
        emContainer.setSizeFull();
		emContainer.setHeight("500px");
		
		center();
    }
	
	public void setTestData(HttpServletRequest request, Api api) {
	
		String basePath = request.getScheme()+"://"+request.getServerName()
					+":"+request.getServerPort()+ request.getContextPath() +"/"+"api"+"/"+api.getEndpoint()+"/";

		URL url = null;
		BufferedReader in = null;
		try {
			url = new URL(basePath);
	        URLConnection yc = url.openConnection();
	        in = new BufferedReader(new InputStreamReader(
	                                yc.getInputStream()));
	        String inputLine;
	        
	        StringBuffer strBuf = new StringBuffer();
	        while ((inputLine = in.readLine()) != null) {
	        	strBuf.append(inputLine);
	        }
	        
	        Serializer serializer = new Persister();
	        
	        if(serializer != null) {
	        	Data data = serializer.read(Data.class, strBuf.toString());
	        	initTable(data, api.getEndpoint());
	        }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if(in != null) {
				try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	private void initTable(Data data, String endPoint) {
		if(data != null && data.getVariables() != null && data.getVariable(endPoint)!=null
					&&  data.getVariable(endPoint).getRow() != null
					&&  data.getVariable(endPoint).getRow().size() > 0) {
			int totalCols =  data.getVariable(endPoint).getRow().get(0).getColumn().size();
			String[] column = new String[totalCols];
			for(int col=0; col < totalCols ; col++) {
				column[col] = "Column_"+col;
			}
			IndexedContainer icData = getTableData(data, column, endPoint);
			emContainer.setContainerDataSource(icData);
			emContainer.setVisibleColumns(column);		
			emContainer.setImmediate(true);
		}
				
	}

	private IndexedContainer getTableData(Data rawData, String[] column,String endPoint) {

		IndexedContainer data = new IndexedContainer();
		for(String colVal : column) {
			data.addContainerProperty(colVal, String.class, "");
		}
		
		for(Row row : rawData.getVariable(endPoint).getRow()) {
			Object id = data.addItem();
			int colIndex = 0;
			for(String col : row.getColumn()) {
				data.getContainerProperty(id, column[colIndex++]).setValue(col);
			}
		}
		return data;
	}

}
