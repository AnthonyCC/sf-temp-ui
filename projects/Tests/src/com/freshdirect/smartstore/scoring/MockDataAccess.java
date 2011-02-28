package com.freshdirect.smartstore.scoring;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.freshdirect.common.pricing.PricingContext;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.smartstore.SessionInput;

public abstract class MockDataAccess implements DataAccess {
    private final static Logger LOGGER = Logger.getLogger(MockDataAccess.class);
    List<ContentNodeModel> nodes = new ArrayList<ContentNodeModel>();
	List<ContentNodeModel> posteriorNodes = new ArrayList<ContentNodeModel>();

    public List<ContentNodeModel> fetchContentNodes(SessionInput input, String name) {
        LOGGER.info("getDatasource called with name: '"+name+"'");
        return null;
    }

    public double[] getVariables(String userId, PricingContext pricingContext, ContentNodeModel contentNode, String[] variables) {
        LOGGER.info("getVariables called with name: '"+variables+"'");
        Map varMap = getVariables(contentNode.getContentKey().getId());
        double[] result = new double[variables.length];
        
        for (int i=0;i<variables.length;i++) {
            String var = variables[i];
            Number number = (Number) varMap.get(var);
            if (number!=null){ 
                result[i] = number.doubleValue();
            }
        }
        
        return result;
    }

    protected abstract Map getVariables(String id);

    @Override
    public boolean addPrioritizedNode(ContentNodeModel model) {
    	return nodes.add(model);
    }
    
    @Override
    public List<ContentNodeModel> getPrioritizedNodes() {
    	return nodes;
    }
    
    @Override
    public boolean addPosteriorNode(ContentNodeModel model) {
    	return posteriorNodes.add(model);
    }
    
    @Override
    public List<ContentNodeModel> getPosteriorNodes() {
    	return posteriorNodes;
    }
    
    public void reset() {
    	nodes.clear();
    }
}
