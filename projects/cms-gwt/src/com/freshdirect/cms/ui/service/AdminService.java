package com.freshdirect.cms.ui.service;

import com.freshdirect.cms.ui.model.AdminProcStatus;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath( "adminService" )
public interface AdminService extends RemoteService {

    public AdminProcStatus rebuildIndexes();
    
    public AdminProcStatus validateEditors();
    
    public AdminProcStatus getBuildIndexStatus();
    
}
