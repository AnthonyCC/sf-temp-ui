package com.freshdirect.routing.manager;

import com.freshdirect.routing.service.exception.RoutingProcessException;

public interface IProcessManager {
	
	void setSuccessor(IProcessManager successor);

    void process(ProcessContext request) throws RoutingProcessException;
    
    void startProcess(ProcessContext request) throws RoutingProcessException;
    void endProcess(ProcessContext request) throws RoutingProcessException;
}
