package com.freshdirect.routing.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.mbertoli.jfep.ExpressionNode;
import org.mbertoli.jfep.Parser;

import com.freshdirect.routing.model.IPackagingModel;

public class ServiceTimeUtil {

	public static double evaluateExpression(String expression, Map parameters) {

		Parser parser = new Parser(expression);
		ExpressionNode root = parser.getTree();
		for (Iterator i = parameters.keySet().iterator(); i.hasNext();) {
            Object name = i.next();
            if (!parser.getParsedVariables().contains(name))
                i.remove();
            else
                root.setVariable((String)name, ((Double)parameters.get(name)).doubleValue());
        }
		return root.getValue();
	}

	public static Map getServiceTimeFactorParams(IPackagingModel model) {
		Map tmpMap = new HashMap();
		tmpMap.put("x",new Double(model.getNoOfCartons()));
		tmpMap.put("y",new Double(model.getNoOfCases()));
		tmpMap.put("z",new Double(model.getNoOfFreezers()));

		return tmpMap;
	}

	public static Map getServiceTimeParams(double intFixedServiceTime, double intVariableServiceTime, double valX) {
		Map tmpMap = new HashMap();
		tmpMap.put("a",new Double(intFixedServiceTime));
		tmpMap.put("b",new Double(intVariableServiceTime));
		tmpMap.put("m",new Double(valX));
		return tmpMap;
	}

	public static boolean isValidExpression(String expression, List variableList) {
		try {
			Parser parser = new Parser(expression);
			ExpressionNode root = parser.getTree();
			for (Iterator i = parser.getParsedVariables().iterator(); i.hasNext();) {
	            Object name = i.next();
	            if (!variableList.contains(name)) {
	            	return false;
	            }
	        }
		} catch (Exception e) {
			return false;
		}

		return true;
	}

}
