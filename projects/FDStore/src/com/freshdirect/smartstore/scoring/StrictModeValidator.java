package com.freshdirect.smartstore.scoring;


import com.freshdirect.smartstore.dsl.Expression;
import com.freshdirect.smartstore.dsl.Operation;
import com.freshdirect.smartstore.dsl.ExpressionVisitor;
import com.freshdirect.smartstore.dsl.VisitException;


public class StrictModeValidator implements ExpressionVisitor {

    public StrictModeValidator() {
    }

    public void visit(Expression expression) throws VisitException {
        if (expression instanceof Operation) {
            Operation oper = (Operation) expression;
            
        }

    }

}
