package com.freshdirect.fdstore.promotion;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Category;

import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.rules.Rule;
import com.freshdirect.rules.RulesEngineI;
import com.freshdirect.rules.RulesRegistry;

/**
 * @author knadeem Date Jun 1, 2005
 */
public class FDPromotionRulesEngine implements Serializable {
	
	private static Category LOGGER = LoggerFactory.getInstance(FDPromotionRulesEngine.class);
	
	public static List getEligiblePromotions(PromotionContextI ctx) {
		Map firedRules = getRulesEngine().evaluateRules(ctx);
		
		if(firedRules.isEmpty()) {
			return Collections.EMPTY_LIST;
		}
		
		List promoCodes = new ArrayList();
		for(Iterator i = firedRules.values().iterator(); i.hasNext(); ){
			Rule r = (Rule) i.next();
			if(r.validate()) {
				promoCodes.add(r.getOutcome());
			}
		}
		
		return promoCodes;
	}	
	
	private static RulesEngineI getRulesEngine() {
		return RulesRegistry.getRulesEngine("PROMOTION");
	}

}
