package com.freshdirect.framework.webapp;

public interface ActionI {
    
    public final static int SUCCESS = 0;
    public final static int ERROR = 1;
    public final static int INPUT = 2;
    
	public void setActionResult(ActionResult result);
    
    public int execute() throws Exception;

}

