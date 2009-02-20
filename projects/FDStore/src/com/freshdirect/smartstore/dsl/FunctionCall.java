/**
 * 
 */
package com.freshdirect.smartstore.dsl;

import java.util.ArrayList;
import java.util.List;

public class FunctionCall extends Expression {

    String name;
    /**
     * List<Expression>
     */
    List   params = new ArrayList();

    public int getReturnType() {
        return type;
    }

    public FunctionCall(String name) {
        this.name = name;
        this.type = RET_UNKNOWN;
    }

    public FunctionCall(String name, int type) {
        this.name = name;
        this.type = type;
    }

    public boolean add(Expression exp) {
        params.add(exp);
        return true;
    }

    public Expression lastExpression() {
        return (Expression) (params.size() > 0 ? params.get(params.size() - 1) : null);
    }

    public void removeLastExpression() {
        if (params.size() > 0) {
            params.remove(params.size() - 1);
        }
    }

    public String toCode() {
        StringBuffer buf = new StringBuffer();
        buf.append(name).append('(');
        for (int i = 0; i < params.size(); i++) {
            if (i > 0) {
                buf.append(',');
            }
            buf.append(((Expression) params.get(i)).toCode());
        }
        buf.append(')');
        return buf.toString();
    }
    
    public String toJavaCode() throws CompileException {
        return context.getJavaCode(name, new ArrayList(params));
    }

    public void validate() throws CompileException {
        StringBuffer paramTypes = new StringBuffer();
        for (int i = 0; i < params.size(); i++) {
            Expression exp = (Expression) params.get(i);
            exp.validate();
            int rtype = exp.getReturnType();
            if (rtype == RET_FLOAT || rtype == RET_INT || rtype == RET_STRING) {
                paramTypes.append((char) rtype);
            } else {
                throw new CompileException(CompileException.TYPE_ERROR, "Return type of '" + exp.toCode() + "' is neither float, nor int, nor string ! (" + rtype + ")");
            }
        }
        this.type = context.getFunctionReturnType(name, paramTypes.toString());
        if (this.type == RET_UNKNOWN) {
            throw new CompileException(CompileException.UNKNOWN_FUNCTION, "Not a valid function : " + name + "(" + paramTypes + ")!");
        }
    }

    public String getName() {
        return name;
    }

    public Expression getParam(int index) {
        return (Expression) params.get(index);
    }

    public void visit(ExpressionVisitor visitor) throws VisitException {
        visitor.visit(this);
        for (int i = 0; i < params.size(); i++) {
            ((Expression) params.get(i)).visit(visitor);
        }
    }

    public String toString() {
        return "Function[" + name + ',' + params + "]";
    }

}