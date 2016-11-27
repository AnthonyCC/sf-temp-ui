package com.freshdirect.athena.ui;

import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;

import com.freshdirect.athena.config.Api;
import com.vaadin.terminal.ExternalResource;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class APIRawTestWindow extends Window {
    
	private Embedded emContainer = new Embedded("");
    
	public APIRawTestWindow() {
		
    	super("API Raw Tester");
    	setWidth("95%");
        setHeight("95%");
        
        VerticalLayout configContainer = new VerticalLayout();
		configContainer.setSizeFull();
		
		emContainer.setSizeFull();
		emContainer.setHeight("500px");
		
		configContainer.addComponent(emContainer);
		addComponent(configContainer);
		
        center();
    }
	
	public void setTestData(HttpServletRequest request, Api api) {
	
		String basePath = request.getScheme()+"://"+request.getServerName()
					+":"+request.getServerPort()+ request.getContextPath() +"/"+"api"+"/"+api.getEndpoint()+"/";

		URL url;
		try {
			url = new URL(basePath);
			System.out.println(basePath);
	        ExternalResource source = new ExternalResource(url);
	        emContainer.setType(Embedded.TYPE_BROWSER);
	        emContainer.setSource(source);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
