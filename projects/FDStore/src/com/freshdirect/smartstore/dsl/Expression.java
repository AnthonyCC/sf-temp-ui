/**
 * 
 */
package com.freshdirect.smartstore.dsl;


public class Expression {
    
    public static final int RET_UNKNOWN   = -1;
    public static final int RET_VOID      = 0;
    public static final int RET_INT       = 'i';
    public static final int RET_FLOAT     = 'f';
    public static final int RET_SET       = 's';
    public static final int RET_STRING    = 't';
    public static final int RET_NODE      = 'n';
    public static final int RET_SYMBOL    = 'y';
    
    Context context;
    int type = RET_VOID;

    public int getReturnType() {
        return type;
    }

    public void setReturnType(int type) {
        this.type = type;
    }

    public String toCode() {
        return "";
    }

    
    /**
     * Return a code fragment which initialize, and declares the necessary variables.
     * @return
     * @throws CompileException
     */
    public String getJavaInitializationCode() throws CompileException {
        return "";
    }
    
    public String toJavaCode() throws CompileException {
        throw new CompileException("Not implemented for "+this.getClass().getName());
    }
    
    /**
     * This method validates that the functions are valids with the given types, and calculates the returning types of every expression.
     * 
     * @throws CompileException
     */
    public void validate() throws CompileException {
    }

    public boolean add(Expression exp) throws CompileException {
        return false;
    }

    public Expression lastExpression() {
        return null;
    }

    public void removeLastExpression() {
    }
    
    public Number evaluateExpression() {
        throw new RuntimeException(this.getClass().getName()+".evaluateExpression not supported!");
    }
    
    public String getStringValue() {
        throw new RuntimeException(this.getClass().getName()+".getStringValue() not supported!");
    }
    
    public void visit(ExpressionVisitor visitor) throws VisitException {
        visitor.visit(this);
    }
    
    public boolean replace(Expression from,Expression to) {
        return false;
    }
    
    public static String getTypeName(int code) {
        switch (code) {
            case RET_INT : return "integer";
            case RET_FLOAT : return "float";
            case RET_SET : return "set";
            case RET_UNKNOWN : return "unknown";
            case RET_VOID : return "void";
            case RET_SYMBOL : return "symbol";
            case RET_STRING : return "string";
            case RET_NODE : return "node";
        }
        throw new RuntimeException("Unknown code:" + code + "('" + ((char) code) + "')");
    }

	public Context getContext() {
		return context;
	}
}
