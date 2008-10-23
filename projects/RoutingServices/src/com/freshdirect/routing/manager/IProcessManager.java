package com.freshdirect.routing.manager;

import com.freshdirect.routing.service.exception.RoutingProcessException;

public interface IProcessManager {
	
	void setSuccessor(IProcessManager successor);

    Object process(ProcessContext request) throws RoutingProcessException;
    
    Object startProcess(ProcessContext request) throws RoutingProcessException;
    Object endProcess(ProcessContext request) throws RoutingProcessException;
}
