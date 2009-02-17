package com.freshdirect.smartstore.dsl;

public interface ExpressionVisitor {
    public void visit(Expression expression) throws VisitException;

}
