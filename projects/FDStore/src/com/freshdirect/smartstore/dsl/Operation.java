/**
 * 
 */
package com.freshdirect.smartstore.dsl;

import java.util.ArrayList;
import java.util.List;

public class Operation extends Expression {
    /**
     * List<Expression>
     */
    List<Expression>    params         = new ArrayList<Expression>();

    /**
     * List<Character>
     */
    List<Character>    operators      = new ArrayList<Character>();

    boolean includeParent  = false;

    boolean expectOperator = true;

    public Operation() {
    }

    public Operation(Expression expr) {
        params.add(expr);
    }

    public Operation(Expression expr, char operand, Expression expr2) {
        params.add(expr);
        operators.add(new Character(operand));
        params.add(expr2);
    }

    public void addOperand(char operand) throws CompileException {
        if (!expectOperator) {
            throw new CompileException(CompileException.SYNTAX_ERROR, "Two operator, without an operand : " + operators + " -> " + operand);
        }
        operators.add(new Character(operand));
        expectOperator = false;
    }

    public void setIncludeParent(boolean includeParent) {
        this.includeParent = includeParent;
    }

    public boolean add(Expression expr) throws CompileException {
        if (params.size() > 0 && expectOperator) {
            throw new CompileException(CompileException.SYNTAX_ERROR, "Operator expected, instead of " + expr);
        }
        params.add(expr);
        expectOperator = true;
        return true;
    }
    
    public int size() {
        return params.size();
    }
    
    public Expression get(int index) {
        return params.get(index);
    }
    
    /**
     * Return the index-th operator. The first operator between the first and second operand is the 0th operator.
     * @param index
     * @return
     */
    public char getOperator(int index) {
        return (operators.get(index)).charValue();
    }

    public String toCode() {
        StringBuilder b = new StringBuilder();
        if (includeParent) {
            b.append('(');
        }
        b.append(params.get(0).toCode());
        for (int i = 1; i < params.size(); i++) {
            if (operators.size() > i - 1) {
                b.append(' ').append(operators.get(i - 1)).append(' ');
            } else {
                b.append(" --- ");
            }
            Expression exp = params.get(i);
            b.append(exp.toCode());
        }
        if (includeParent) {
            b.append(')');
        }
        return b.toString();
    }

    public String toJavaCode() throws CompileException {
        StringBuilder b = new StringBuilder();
        b.append('(');
        b.append(((Expression) params.get(0)).toJavaCode());
        for (int i = 1; i < params.size(); i++) {
            b.append(' ').append(operators.get(i - 1)).append(' ');
            Expression exp = params.get(i);
            b.append(exp.toJavaCode());
        }
        b.append(')');
        return b.toString();
    }

    public String toString() {
        StringBuilder b = new StringBuilder();
        b.append("Oper[").append(operators).append(',').append(params).append("]");
        return b.toString();
    }

    public void validate() throws CompileException {
        for (int i = 0; i < params.size(); i++) {
            Expression exp = params.get(i);
            exp.validate();
        }
        int currentType = params.get(0).getReturnType();
        for (int i = 1; i < params.size(); i++) {
            Expression exp = params.get(i);
            int type = exp.getReturnType();
            Character operator = operators.get(i - 1);
            currentType = calculateType(currentType, operator.charValue(), type);
        }
        this.type = currentType;
    }
    
    
    public boolean replace(Expression from,Expression to) {
        int index = params.indexOf(from);
        if (index!=-1) {
            params.remove(index);
            params.add(index, to);
            return true;
        }
        return false;
    }
    

    public static int calculateType(int type0, char operator, int type1) throws CompileException {
        if (type0 == RET_FLOAT || type0 == RET_INT) {
            if (type1 == RET_FLOAT || type1 == RET_INT) {
                switch (operator) {
                    case '+':
                    case '-':
                    case '*':
                    case '%': {
                        return type0 == RET_INT && type1 == RET_INT ? RET_INT : RET_FLOAT;
                    }
                    case '/': {
                        return RET_FLOAT;
                    }
                }
            } else if ((type1==RET_SYMBOL) && operator==':') {
                return type0;
            } else {
                throw new CompileException(CompileException.TYPE_ERROR, "Operation between "+Expression.getTypeName(type0) + " "+operator + " "+Expression.getTypeName(type1));
            }
        }
        if (type0 == RET_SET) {
            if (operator == ':') {
                if (type1 != RET_INT) {
                    throw new CompileException(CompileException.TYPE_ERROR, "Filtering function must return integer value!");
                }
                return RET_SET;
            }
            if (type1==RET_SET) {
                switch (operator) {
                    case '+':
                    case '-':
                    case '*': {
                        return RET_SET;
                    }
                }
            }
            throw new CompileException(CompileException.TYPE_ERROR, "Operation between "+Expression.getTypeName(type0) + " "+operator + " "+Expression.getTypeName(type1));
        }
        if (type0==RET_STRING) {
            if (operator == '+') {
                switch (type1) { 
                    case RET_INT:
                    case RET_FLOAT:
                    case RET_STRING : 
                        return RET_STRING;
                    default : 
                        throw new CompileException(CompileException.TYPE_ERROR, "Operation between "+Expression.getTypeName(type0) + " "+operator + " "+Expression.getTypeName(type1));
                }
            }
            throw new CompileException(CompileException.TYPE_ERROR, "Operation between "+Expression.getTypeName(type0) + " "+operator + " "+Expression.getTypeName(type1));
        }
        throw new CompileException(CompileException.SYNTAX_ERROR, "Unknown operator : '" + operator+"'");
    }
    
    private static int getPrecedence(char code) {
        switch (code) {
            case ':' : return 5;
            case '*' : return 4;
            case '/' : return 3;
            case '+' : return 2;
            case '-' : return 1;
        }
        return 0;
    }
    

    public void visit(ExpressionVisitor visitor) throws VisitException {
        visitor.visit(this);
        for (int i = 0; i < params.size(); i++) {
            params.get(i).visit(visitor);
        }
    }
    
    public Expression getUniqueExpression() {
        return params.size() == 1 ? params.get(0) : null;
    }
    
    /**
     * Return a fixed binary tree. It can be null, when there is only one operand.
     * @return
     */
    public BinaryExpression fixPrecedence() {
        if (params.size() <= 1) {
            // rare case, when some tree manipulation removed one or more operand ..
            return null;
        }
        List<Expression> tParams = new ArrayList<Expression>(params);
        List<Character> tOps = new ArrayList<Character>(operators);
        int max = 0;
        do {
            max = 0;
            for (int i = 0; i < tOps.size(); i++) {
                int curr = getPrecedence(tOps.get(i).charValue());
                max = curr > max ? curr : max;
            }
            if (max>0) {
                for (int i=0;i<tOps.size();i++) {
                    char charValue = tOps.get(i).charValue();
                    int curr = getPrecedence(charValue);
                    if (curr == max) {
                        Expression left = tParams.remove(i);
                        Expression right = tParams.remove(i);
                        tOps.remove(i);
                        if (left instanceof Operation) {
                            left = ((Operation) left).fixPrecedence();
                        }
                        if (right instanceof Operation) {
                            right = ((Operation) right).fixPrecedence();
                        }
                        BinaryExpression b = new BinaryExpression(left, charValue, right);
                        tParams.add(i, b);
                        i = 0;
                    }
                    
                }
            }
            
        } while(max>0 && tParams.size()>1);
        return (BinaryExpression) tParams.get(0);
    }

    public void removeOperator(int i) {
        params.remove(i);
        if (i>0) {
            operators.remove(i-1);
        }
    }

    
    @Override
    public String getJavaInitializationCode() throws CompileException {
       StringBuilder buf = new StringBuilder();
       for (Expression exp : params) {
           buf.append(exp.getJavaInitializationCode());
       }
       return buf.toString();
    }
}