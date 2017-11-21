package com.freshdirect.fdstore.promotion;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Category;

import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.rules.Rule;
import com.freshdirect.storeapi.StoreServiceLocator;
import com.freshdirect.storeapi.rules.RulesEngineI;

/**
 * @author knadeem Date Jun 1, 2005
 */
public class FDPromotionRulesEngine implements Serializable {

	private static Category LOGGER = LoggerFactory.getInstance(FDPromotionRulesEngine.class);

	public static List<String> getEligiblePromotions(PromotionContextI ctx) {
		Map<String, Rule> firedRules = getRulesEngine().evaluateRules(ctx);

		if(firedRules.isEmpty()) {
			return Collections.emptyList();
		}

		List<String> promoCodes = new ArrayList<String>();
		for (Rule r : firedRules.values()) {
			if(r.validate()) {
				promoCodes.add((String) r.getOutcome());
			}
		}

		return promoCodes;
	}

	private static RulesEngineI getRulesEngine() {
		return StoreServiceLocator.rulesRegistry().getRulesEngine("PROMOTION");
	}

}
