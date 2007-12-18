package com.freshdirect.fdstore.promotion;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CompositeStrategy implements PromotionStrategyI {

	private List strategies = new ArrayList();
	private int operator;
	
	public static final int OR = 0;
	public static final int AND = 1;

	public CompositeStrategy(int operator) {
		this.operator = operator;
	}
	
	public void addStrategy(PromotionStrategyI strategy){
		strategies.add(strategy);
	}
	
	public void addStrategies(List strategies) {
		strategies.addAll(strategies);
	}
	
	
	public int evaluate(String promotionCode, PromotionContextI context) {
		int result = -1;
		for (Iterator i = this.strategies.iterator(); i.hasNext();){
			PromotionStrategyI strategy = (PromotionStrategyI) i.next();
			int response = strategy.evaluate(promotionCode, context);
			switch(operator) {
				case OR: result = (result == -1) ? response : result | response;
						 break;
				case AND: result = (result == -1) ? response : result & response;
					     break;
				default: result = DENY;
			}
		} 
		return result;
	}

/*	switch(operator){
	case OR:
		switch (response) {
			case PromotionStrategyI.ALLOW:
				//eligible, Allow
				return ALLOW;
				
			default:
				// not eligible, go to next
				result = DENY;
				continue;
			}
	case AND:
		switch (response) {
			case PromotionStrategyI.ALLOW:
				// eligible, go to next
				result = ALLOW;
				continue;
				
			default:
				// not eligible, Deny
				return DENY;
		}
	default:
		//Invalid Operator
		return DENY;		
}*/
	public int getPrecedence() {
		return 1100;
	}

	
	public String toString() {
		return "CompositeAttributeStrategy[...]";
	}

}
