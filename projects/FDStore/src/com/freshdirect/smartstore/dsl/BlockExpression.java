/**
 * 
 */
package com.freshdirect.smartstore.dsl;

import java.util.ArrayList;
import java.util.List;

public class BlockExpression extends Expression {
    List<Expression> expressions = new ArrayList<Expression>();

    public BlockExpression() {
        this.type = Expression.RET_VOID;
    }

    @Override
    public String toCode() {
        StringBuffer b = new StringBuffer();
        for (int i = 0; i < expressions.size(); i++) {
            if (i > 0) {
                b.append(';');
            }
            b.append(expressions.get(i).toCode());
        }
        return b.toString();
    }

    @Override
    public void validate() throws CompileException {
        for (Expression exp : expressions) {
            exp.validate();
        }
    }

    public int size() {
        return expressions.size();
    }

    public Expression get(int i) {
        return expressions.get(i);
    }

    public boolean add(Expression arg0) {
        expressions.add(arg0);
        return true;
    }

    public Expression lastExpression() {
        return (expressions.size() > 0 ? expressions.get(expressions.size() - 1) : null);
    }

    public void removeLastExpression() {
        if (expressions.size() > 0) {
            expressions.remove(expressions.size() - 1);
        }
    }

    @Override
    public void visit(ExpressionVisitor visitor) throws VisitException {
        visitor.visit(this);
        for (Expression exp : expressions) {
            exp.visit(visitor);
        }
    }

    @Override
    public String toJavaCode() throws CompileException {
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < expressions.size(); i++) {
            if (i > 0) {
                b.append(';');
            }
            b.append(expressions.get(i).toJavaCode());
        }
        return b.toString();
    }
 
    @Override
    public String getJavaInitializationCode() throws CompileException {
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < expressions.size(); i++) {
            b.append(expressions.get(i).toJavaCode());
        }
        return b.toString();
    }

}