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

    @Override
    public String toCode() {
        return "(" + left.toCode() + ' ' + operator + ' ' + right.toCode() + ')';
    }

    @Override
    public String toJavaCode() throws CompileException {
        return "(" + left.toJavaCode() + ' ' + operator + ' ' + right.toJavaCode() + ')';
    }
    
    @Override
    public void validate() throws CompileException {
        left.validate();
        right.validate();
        this.type = Operation.calculateType(left.getReturnType(), operator, right.getReturnType());
    }

    @Override
    public void visit(ExpressionVisitor visitor) throws VisitException {
        super.visit(visitor);
        left.visit(visitor);
        right.visit(visitor);
    }
    
    @Override
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
    
    @Override
    public String getStringValue() {
        String s1 = left.getStringValue();
        String s2 = right.getStringValue();
        if (operator == '+') {
            return s1 + s2;
        }
        throw new RuntimeException("Operator " + operator + " not supported in string manipulations, between : '" + s1 + "' and '" + s2 + "' !");
    }
    
    @Override
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
    
    @Override
    public String getJavaInitializationCode() throws CompileException {
        return right.getJavaInitializationCode() + left.getJavaInitializationCode();
    }
    
}
