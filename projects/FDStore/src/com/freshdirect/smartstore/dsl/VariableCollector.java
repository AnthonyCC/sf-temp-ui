package com.freshdirect.smartstore.dsl;

import java.util.HashSet;
import java.util.Set;

public class VariableCollector implements ExpressionVisitor {

    Set names = new HashSet();
    
    public void visit(Expression expression) {
        if (expression instanceof VariableExpression) {
            names.add( ((VariableExpression)expression).name);
        }
    }

    public Set getVariables() {
        return names;
    }
}
