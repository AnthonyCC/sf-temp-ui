package com.freshdirect.smartstore.dsl;

public class StringExp extends Expression {

    String value;
    
    public StringExp(String value) {
        this.value = value;
    }
    
    
    public int getReturnType() {
        return Expression.RET_STRING;
    }
    
    public String getValue() {
        return value;
    }
    

    public String toCode() {
        return "\""+value+"\"";
    }
    
    public String toJavaCode() throws CompileException {
        return toCode();
    }

    public String toString() {
        return "String['"+value+"']";
    }
    
}
