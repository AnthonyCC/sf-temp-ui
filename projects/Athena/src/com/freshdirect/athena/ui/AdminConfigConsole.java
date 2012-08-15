package com.freshdirect.athena.ui;

import java.util.Date;
import java.util.Map;

import com.freshdirect.athena.common.SystemMessage;
import com.freshdirect.athena.common.SystemMessageManager;
import com.freshdirect.athena.config.Api;
import com.freshdirect.athena.config.ConfigManager;
import com.freshdirect.athena.config.Datasource;
import com.vaadin.Application;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.Reindeer;

public class AdminConfigConsole extends Panel {

	private Table dataSourceList = new Table();
	private Table apiList = new Table();
	private Table systemMessages = new Table();
	
	private Label configDetails = new Label("", Label.CONTENT_XHTML);
	
	private Button refreshButton = new Button("Refresh");
	private Button testRawButton = new Button("Test Raw");
	private Button testDataButton = new Button("Test Data");
	
	private IndexedContainer dataSourceContainer;
	private IndexedContainer apiContainer;
	private IndexedContainer systemMessageContainer;
	
	private static String[] dataSourceCols = new String[] { "Datasource Name",
			"Datasource Type" };

	private static String[] apiCols = new String[] { "Api Name",
			"Datasource Name" };
	
	private static String[] systemMessageCols = new String[] { "Timestamp", "Message" };
		
	private Application application;
			
	private APIRawTestWindow testRawWindow = new APIRawTestWindow();
	private APIDataTestWindow testDataWindow = new APIDataTestWindow();
		
	public AdminConfigConsole() {
		
		super("<h1><center>Athena - FreshDirect Enterprise Data Services</center></h1>");
		addStyleName(Reindeer.LAYOUT_BLUE);
	}

	public void initUI(Application application) {
		
		HorizontalSplitPanel splitPanel = new HorizontalSplitPanel();
		splitPanel.setSizeFull();
		this.application = application;

		HorizontalLayout left = new HorizontalLayout();
		
		left.setSpacing(true);
		left.addComponent(dataSourceList);
		
		left.addComponent(apiList);
				
		splitPanel.addComponent(left);
		
		VerticalLayout right = new VerticalLayout();
		//right.setMargin(true);
		right.setSpacing(true);
		right.setSizeFull();
		
		HorizontalLayout controlButtons = new HorizontalLayout();
		controlButtons.setSizeFull();
		//controlButtons.setMargin(true);
		controlButtons.setSpacing(true);
				
		controlButtons.addComponent(refreshButton);
		controlButtons.addComponent(testRawButton);
		controlButtons.addComponent(testDataButton);
		
		controlButtons.setComponentAlignment(refreshButton, Alignment.MIDDLE_CENTER);
		controlButtons.setComponentAlignment(testRawButton, Alignment.MIDDLE_CENTER);
		controlButtons.setComponentAlignment(testDataButton, Alignment.MIDDLE_CENTER);
		
		right.addComponent(controlButtons);
						
		VerticalLayout configContainer = new VerticalLayout();
		configContainer.setSizeFull();
		configContainer.setMargin(true);
		configContainer.setSpacing(true);
		
		Label lineBreak = new Label("<hr/>", Label.CONTENT_XHTML);
		lineBreak.setSizeFull();
		configContainer.addComponent(lineBreak);
		configContainer.setComponentAlignment(lineBreak, Alignment.MIDDLE_CENTER);
		
		configContainer.addComponent(configDetails);
		configContainer.setComponentAlignment(configDetails, Alignment.MIDDLE_CENTER);
		
		right.addComponent(configContainer);
					
		splitPanel.addComponent(right); // Right Section
		
		this.addComponent(splitPanel);
		systemMessages.setSizeFull();
		
		this.addComponent(new Label("<hr/>", Label.CONTENT_XHTML));
		this.addComponent(systemMessages);
		
		initTables();
		initListeners();
	}
	
	public void initTables() {
		
		initDataSourceList();
		initApiList();
		initSystemMessageList();
	}
	
	private void initListeners() {

		testRawWindow.addListener(new Window.CloseListener() {
            public void windowClose(CloseEvent e) {
            	testRawButton.setEnabled(true);
            	testDataButton.setEnabled(true);
            }
        });
		
		testDataWindow.addListener(new Window.CloseListener() {
            public void windowClose(CloseEvent e) {
            	testRawButton.setEnabled(true);
            	testDataButton.setEnabled(true);
            }
        });
		
		testRawButton.addListener(new Button.ClickListener() {
			public void buttonClick(ClickEvent event) {	
							
				testRawButton.setEnabled(false);
				testDataButton.setEnabled(false);
				if(testRawButton.getData() != null) {
					testRawWindow.setTestData(getApplication().getLastRequest(), (Api) testRawButton.getData());
					getWindow().addWindow(testRawWindow);
				}
			}
		});
		
		testDataButton.addListener(new Button.ClickListener() {
			public void buttonClick(ClickEvent event) {	
							
				testRawButton.setEnabled(false);
				testDataButton.setEnabled(false);
				if(testDataButton.getData() != null) {
					testDataWindow.setTestData(getApplication().getLastRequest(), (Api) testDataButton.getData());
					getWindow().addWindow(testDataWindow);
				}
			}
		});
	
		
		refreshButton.addListener(new Button.ClickListener() {
			public void buttonClick(ClickEvent event) {
				ConfigManager.getInstance().init();
				initTables();
			}
		});
		
		dataSourceList.addListener(new Property.ValueChangeListener() {
			public void valueChange(ValueChangeEvent event) {
				Object id = dataSourceList.getValue();
				if(dataSourceContainer != null && id != null) {
					Property col = dataSourceContainer.getContainerProperty(id, "Details");
					if(col != null) {
						testRawButton.setEnabled(false);
						testDataButton.setEnabled(false);
						//initConfigDetailsTable(col.getValue());
						configDetails.setValue(col.getValue().toString());
					}
				}
			}
		});
		
		apiList.addListener(new Property.ValueChangeListener() {
			public void valueChange(ValueChangeEvent event) {
				Object id = apiList.getValue();
				if(apiContainer != null && id != null) {
					Property col = apiContainer.getContainerProperty(id, "Details");
					if(col != null) {
						testRawButton.setData(col.getValue());
						testRawButton.setEnabled(true);
						
						testDataButton.setData(col.getValue());
						testDataButton.setEnabled(true);

						configDetails.setValue(col.getValue().toString());
					}
				}
			}
		});		
		

	}

	private void initDataSourceList() {

		dataSourceList.setContainerDataSource(getDataSourceTableData());
		dataSourceList.setVisibleColumns(dataSourceCols);
		dataSourceList.setSelectable(true);
		dataSourceList.setImmediate(true);
		
	}

	private IndexedContainer getDataSourceTableData() {

		dataSourceContainer = new IndexedContainer();
		dataSourceContainer.addContainerProperty("Datasource Name", String.class, "");
		dataSourceContainer.addContainerProperty("Datasource Type", String.class, "");
		dataSourceContainer.addContainerProperty("Details", Object.class, "");

		for (Map.Entry<String, Datasource> dbPoolEntry : ConfigManager
				.getInstance().getDataSourceMapping().entrySet()) {
			Object id = dataSourceContainer.addItem();
			dataSourceContainer.getContainerProperty(id, "Datasource Name").setValue(
					dbPoolEntry.getKey());
			dataSourceContainer.getContainerProperty(id, "Datasource Type").setValue(
					dbPoolEntry.getValue().getConnectionType());
			dataSourceContainer.getContainerProperty(id, "Details").setValue(
					dbPoolEntry.getValue());

		}
		return dataSourceContainer;
	}

	private void initApiList() {

		apiList.setContainerDataSource(getApiTableData());
		apiList.setVisibleColumns(apiCols);
		apiList.setSelectable(true);
		apiList.setImmediate(true);
	}

	private IndexedContainer getApiTableData() {

		apiContainer = new IndexedContainer();
		apiContainer.addContainerProperty("Api Name", String.class, "");
		apiContainer.addContainerProperty("Datasource Name", String.class, "");
		apiContainer.addContainerProperty("Details", Object.class, "");

		for (Map.Entry<String, Api> apiEntry : ConfigManager.getInstance()
				.getServiceMapping().entrySet()) {
			
			Object id = apiContainer.addItem();
			apiContainer.getContainerProperty(id, "Api Name").setValue(apiEntry.getKey());
			apiContainer.getContainerProperty(id, "Datasource Name").setValue(
					apiEntry.getValue().getDatasource());
			apiContainer.getContainerProperty(id, "Details").setValue(
					apiEntry.getValue());

		}
		return apiContainer;
	}
	
	private void initSystemMessageList() {

		systemMessages.setContainerDataSource(getSystemMessageData());
		systemMessages.setVisibleColumns(systemMessageCols);
		
		systemMessages.setSelectable(true);
		systemMessages.setImmediate(true);
	}

	private IndexedContainer getSystemMessageData() {

		systemMessageContainer = new IndexedContainer();
		systemMessageContainer.addContainerProperty("Timestamp", Date.class, "");
		systemMessageContainer.addContainerProperty("Message", String.class, "");
		
		for (SystemMessage message : SystemMessageManager.getInstance().getMessages()) {
			Object id = systemMessageContainer.addItem();
			systemMessageContainer.getContainerProperty(id, "Timestamp").setValue(message.getTimeStamp());
			systemMessageContainer.getContainerProperty(id, "Message").setValue(message.getMessage());

		}
		return systemMessageContainer;
	}
	
	public AthenaAdminApplication getApplication() {
		return (AthenaAdminApplication)application;
	}
	
}
