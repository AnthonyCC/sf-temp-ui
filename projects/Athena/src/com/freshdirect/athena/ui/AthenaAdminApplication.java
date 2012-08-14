package com.freshdirect.athena.ui;

import javax.servlet.http.HttpServletRequest;

import com.vaadin.Application;
import com.vaadin.service.ApplicationContext.TransactionListener;
import com.vaadin.ui.Window;

public class AthenaAdminApplication extends Application implements TransactionListener {
	
	AdminConfigConsole rootPanel = new AdminConfigConsole();
	
	HttpServletRequest lastRequest;
	
	@Override
	public void init() {
		initLayout();
		getContext().addTransactionListener(this);
	}

	private void initLayout() {
		
		setMainWindow(new Window("Athena Admin Console", rootPanel));
		rootPanel.initUI(this);
			
	}

	@Override
	public void transactionStart(Application application, Object transactionData) {
		// TODO Auto-generated method stub
		lastRequest = (HttpServletRequest) transactionData;
	}

	@Override
	public void transactionEnd(Application application, Object transactionData) {
		// TODO Auto-generated method stub
		lastRequest = null;
	}

	public HttpServletRequest getLastRequest() {
		return lastRequest;
	}
		
}
