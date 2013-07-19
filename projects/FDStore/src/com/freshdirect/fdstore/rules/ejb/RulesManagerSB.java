package com.freshdirect.fdstore.rules.ejb;

import java.rmi.RemoteException;
import java.util.Map;

import javax.ejb.EJBObject;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.rules.Rule;
import com.freshdirect.rules.RulesConfig;

public interface RulesManagerSB extends EJBObject {

	Map<String, Rule> getRules(RulesConfig config) throws FDResourceException, RemoteException;

	Rule getRule(RulesConfig config,String ruleId) throws FDResourceException,RemoteException;

	void deleteRule(RulesConfig config,String ruleId) throws FDResourceException,RemoteException;

	void storeRule(RulesConfig config,Rule rule) throws FDResourceException,RemoteException;

}
