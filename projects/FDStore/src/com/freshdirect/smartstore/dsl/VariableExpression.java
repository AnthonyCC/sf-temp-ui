package com.freshdirect.smartstore.dsl;

public class VariableExpression extends Expression {

    String name;

    public VariableExpression(String name) {
        this.name = name;
    }

    public String getVariableName() {
        return name;
    }

    public int getReturnType() {
        return context.getVariableType(name);
    }

    public String toCode() {
        return context != null ? context.formatVariable(name) : name;
    }
    
    public String toJavaCode() throws CompileException {
        return name;
    }

    public String toString() {
        return "Variable[" + name + (context != null ? "," + getReturnType() : "") + "]";
    }
    
    public Number evaluateExpression() {
        return (Number) context.getVariableValue(name);
    }
}
