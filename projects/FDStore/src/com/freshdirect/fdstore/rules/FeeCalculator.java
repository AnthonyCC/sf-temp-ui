package com.freshdirect.fdstore.rules;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.freshdirect.framework.util.MathUtil;
import com.freshdirect.rules.Rule;
import com.freshdirect.rules.RulesEngineI;
import com.freshdirect.rules.RulesRegistry;

/**
 * Generic rule-based fee calculator that operates with a single base price and adjustments.
 */
public class FeeCalculator implements Serializable {

	private String subsystem;

	public FeeCalculator(String subsystem) {
		this.subsystem = subsystem;
	}

	private static Set resolveConflicts(Collection rules) {

		Set applicableRules = filterBasePriceRulesWithLowPriority(rules);

		Set finalRules = new HashSet();

		Rule baseRule = null;
		for (Iterator i = applicableRules.iterator(); i.hasNext();) {
			Rule r = (Rule) i.next();
			if (r.getOutcome() == null) {
				continue;
			}

			if (r.getOutcome() instanceof BasePrice) {
				BasePrice currPrice = (BasePrice) r.getOutcome();
				BasePrice basePrice = (baseRule == null ? null : (BasePrice) baseRule.getOutcome());
				if (basePrice == null || basePrice.getPrice() > currPrice.getPrice()) {
					baseRule = r;
				}
			} else {
				finalRules.add(r);
			}
		}
		if (baseRule != null) {
			finalRules.add(baseRule);
		}
		return finalRules;
	}

	private static Set filterBasePriceRulesWithLowPriority(Collection rules) {
		Set filteredRules = new HashSet();
		int hPriority = Integer.MIN_VALUE;

		for (Iterator i = rules.iterator(); i.hasNext();) {
			Rule r = (Rule) i.next();
			if (r.getOutcome() == null) {
				continue;
			}

			if (r.getOutcome() instanceof BasePrice) {
				if (hPriority < r.getPriority()) {
					hPriority = r.getPriority();
				}
			}
		}

		for (Iterator i = rules.iterator(); i.hasNext();) {
			Rule r = (Rule) i.next();
			if (r.getOutcome() == null) {
				continue;
			}
			if (r.getOutcome() instanceof BasePrice) {
				if (r.getPriority() == hPriority) {
					filteredRules.add(r);
				}
			} else {
				filteredRules.add(r);
			}
		}

		return filteredRules;
	}

	private RulesEngineI getRulesEngine() {
		return RulesRegistry.getRulesEngine(this.subsystem);
	}

	public double calculateFee(FDRuleContextI ctx) {
		Map firedRules = getRulesEngine().evaluateRules(ctx);

		//System.out.println("FIRED RULES: " + firedRules);

		Set rules = resolveConflicts(firedRules.values());

		//System.out.println("AFTER RESOLVE CONFLICT: " + rules);

		double value = 0.0;
		for (Iterator i = rules.iterator(); i.hasNext();) {
			Rule r = (Rule) i.next();
			Object outcome = r.getOutcome();
			if (outcome instanceof BasePrice) {
				value += ((BasePrice) r.getOutcome()).getPrice();
			} else if (outcome instanceof Adjustment) {
				value -= ((Adjustment) r.getOutcome()).getValue();
			}
		}

		return value < 0 ? 0 : MathUtil.roundDecimal(value);
	}

}