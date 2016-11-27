package com.freshdirect.dataloader.autoorder.create.command;

import java.util.Date;

public interface ITesterCommand {
	
	   
    public boolean isDone();
                 
    public long getElapsedTime();
    
    int getCount();

	public int getTotal();
	
	public String getStatusMessage();
    
    public void load();
    
    public void init(Date filename, String skuPath, String customerNo, String customerPrefix, String type);
}
