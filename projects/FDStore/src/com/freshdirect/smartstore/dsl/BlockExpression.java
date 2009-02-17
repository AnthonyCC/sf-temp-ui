/**
 * 
 */
package com.freshdirect.smartstore.dsl;

import java.util.ArrayList;
import java.util.List;

public class BlockExpression extends Expression {
    List expressions = new ArrayList();

    public BlockExpression() {
        this.type = Expression.RET_VOID;
    }

    public String toCode() {
        StringBuffer b = new StringBuffer();
        for (int i = 0; i < expressions.size(); i++) {
            if (i > 0) {
                b.append(';');
            }
            b.append(((Expression) expressions.get(i)).toCode());
        }
        return b.toString();
    }

    public void validate() throws CompileException {
        for (int i = 0; i < expressions.size(); i++) {
            ((Expression) expressions.get(i)).validate();
        }
    }

    public int size() {
        return expressions.size();
    }

    public Expression get(int i) {
        return (Expression) expressions.get(i);
    }

    public boolean add(Expression arg0) {
        expressions.add(arg0);
        return true;
    }

    public Expression lastExpression() {
        return (Expression) (expressions.size() > 0 ? expressions.get(expressions.size() - 1) : null);
    }

    public void removeLastExpression() {
        if (expressions.size() > 0) {
            expressions.remove(expressions.size() - 1);
        }
    }

    public void visit(ExpressionVisitor visitor) throws VisitException {
        visitor.visit(this);
        for (int i = 0; i < expressions.size(); i++) {
            ((Expression) expressions.get(i)).visit(visitor);
        }
    }

    public String toJavaCode() throws CompileException {
        StringBuffer b = new StringBuffer();
        for (int i = 0; i < expressions.size(); i++) {
            if (i > 0) {
                b.append(';');
            }
            b.append(((Expression) expressions.get(i)).toJavaCode());
        }
        return b.toString();
    }

}