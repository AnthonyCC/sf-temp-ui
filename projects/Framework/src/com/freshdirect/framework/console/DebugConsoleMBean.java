package com.freshdirect.framework.console;

public interface DebugConsoleMBean {

	public boolean isConsoleStarted();
	
	public void startConsole(int port);
	
	public void stopConsole();
	
	public void defineUser(String username, String password);
		
	public String[] getUsers();
}
