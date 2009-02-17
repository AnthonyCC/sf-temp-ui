package com.freshdirect.smartstore.dsl;

public class BinaryExpression extends Expression {

    Expression left;
    Expression right;
    char       operator;

    public BinaryExpression(Expression left, char operator, Expression right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    public Expression getLeft() {
        return left;
    }

    public Expression getRight() {
        return right;
    }

    public char getOperator() {
        return operator;
    }

    public String toCode() {
        return "(" + left.toCode() + ' ' + operator + ' ' + right.toCode() + ')';
    }

    public String toJavaCode() throws CompileException {
        return "(" + left.toJavaCode() + ' ' + operator + ' ' + right.toJavaCode() + ')';
    }
    
    public void validate() throws CompileException {
        left.validate();
        right.validate();
        this.type = Operation.calculateType(left.getReturnType(), operator, right.getReturnType());
    }

    public void visit(ExpressionVisitor visitor) throws VisitException {
        super.visit(visitor);
        left.visit(visitor);
        right.visit(visitor);
    }
    
    public Number evaluateExpression() {
        Number lv = left.evaluateExpression();
        Number rv = right.evaluateExpression();
        switch (operator) {
            case '+' : return new Double(lv.doubleValue() + rv.doubleValue());
            case '-' : return new Double(lv.doubleValue() - rv.doubleValue());
            case '*' : return new Double(lv.doubleValue() * rv.doubleValue());
            case '/' : return new Double(lv.doubleValue() / rv.doubleValue());
        }
        return super.evaluateExpression();
    }
    
    public boolean replace(Expression from,Expression to) {
        if (right == from) {
            right = to;
            return true;
        }
        if (left == from) {
            left = to;
            return true;
        }
        return false;
    }
    
    public String toString() {
        StringBuffer b = new StringBuffer();
        b.append("Binary[").append(left).append(',').append(operator).append(',').append(right).append("]");
        return b.toString();
    }
    
}
