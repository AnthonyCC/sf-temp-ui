package com.freshdirect.smartstore.scoring;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.NotFoundException;

import com.freshdirect.smartstore.dsl.BlockExpression;
import com.freshdirect.smartstore.dsl.ClassCompileException;
import com.freshdirect.smartstore.dsl.CompileException;
import com.freshdirect.smartstore.dsl.Context;
import com.freshdirect.smartstore.dsl.Expression;
import com.freshdirect.smartstore.dsl.FunctionCall;
import com.freshdirect.smartstore.dsl.Operation;
import com.freshdirect.smartstore.dsl.Parser;
import com.freshdirect.smartstore.dsl.VariableCollector;
import com.freshdirect.smartstore.dsl.VariableExpression;
import com.freshdirect.smartstore.dsl.VisitException;

/**
 * This class is used to generate DataGenerator classes based on simple expressions.
 * 
 * @see DataGenerator
 * @author zsombor
 *
 */
public class DataGeneratorCompiler extends CompilerBase {

    private static final String CURRENT_NODE = "currentProduct";
    static final String TO_LIST = "toList";
    public static final String RECURSIVE_NODES_EXCEPT = "RecursiveNodesExcept";
    public static final String RECURSIVE_NODES = "RecursiveNodes";
    private static final String EXPLICIT_LIST = "explicitList";
    
    final static String NODE_TYPE="com.freshdirect.fdstore.content.ContentNodeModel";
    final static String SET_TYPE="List";
    
    boolean optimize = false;
    boolean caching = true;
    final static boolean CACHE_BY_CURRENT_NODE_ALSO = false;
    
    Set globalVariables;
    
    private class NodeFunction extends Context.FunctionDef {

        public NodeFunction() {
            super();
        }

        public NodeFunction(int min, int max, int retType) {
            super(min, max, retType);
        }

        public NodeFunction(int min, int max) {
            super(min, max);
        }

        public NodeFunction(int params) {
            super(params);
        }
        protected void validateParamCount(String name, String paramTypes) throws CompileException {
            super.validateParamCount(name, paramTypes);
            for (int i = 0; i < paramTypes.length(); i++) {
                int type = paramTypes.charAt(i);
                checkType(""+(i+1)+". parameter",type);
            }
        }
        
    }
    
    class RecursiveNodesFunction extends NodeFunction {
        public RecursiveNodesFunction() {
            super(1, Integer.MAX_VALUE, Expression.RET_SET);
        }


        public String toJavaCode(String name, List parameters) throws CompileException {
            if (parameters.size() == 1) {
                Expression param = (Expression) parameters.get(0);
                return handleOneParameteredFunction(param);
            } else {
                StringBuffer buffer = new StringBuffer();
                buffer.append("  HelperFunctions.recursiveNodes(new Object[] { ");
                for (int i = 0; i < parameters.size(); i++) {
                    if (i > 0) {
                        buffer.append(',');
                    }
                    buffer.append(((Expression) parameters.get(i)).toJavaCode());
                }
                buffer.append("})");
                return buffer.toString();
            }
        }


        String handleOneParameteredFunction(Expression param) throws CompileException {
            switch (param.getReturnType()) {
                case Expression.RET_NODE:
                case Expression.RET_STRING:
                case Expression.RET_SET:
                    return "  HelperFunctions.recursiveNodes(" + createScriptConvertToNodeOrSet(param) + ")";
                default:
                    checkType("first parameter",param.getReturnType());
            }
            return null;
        }
    }
    
    static String createScriptConvertToNode(Expression param) throws CompileException {
        if (param.getReturnType() == Expression.RET_NODE) {
            return "(" + NODE_TYPE + ")" + param.toJavaCode();
        }
        if (param.getReturnType() == Expression.RET_STRING) {
            return "HelperFunctions.lookup((String)" + param.toJavaCode() + ")";
        }
        throw new CompileException(CompileException.TYPE_ERROR, "Node or string parameter expected (" + param + ")!");
    }

    static String createScriptConvertToNodeOrSet(Expression param) throws CompileException {
        if (param.getReturnType() == Expression.RET_NODE) {
            return "(" + NODE_TYPE + ")" + param.toJavaCode();
        }
        if (param.getReturnType() == Expression.RET_STRING) {
            return "HelperFunctions.lookup((String)" + param.toJavaCode() + ")";
        }
        if (param.getReturnType() == Expression.RET_SET) {
            return "(" + SET_TYPE + ")" + param.toJavaCode();
        }
        throw new CompileException(CompileException.TYPE_ERROR, "Node or string parameter expected (" + param + ")!");
    }
    
    
    class RecursiveNodesExceptFunction extends RecursiveNodesFunction {
        public String toJavaCode(String name, List parameters) throws CompileException {
            if (parameters.size() == 1) {
                Expression param = (Expression) parameters.get(0);
                return handleOneParameteredFunction(param);
            } else {
                Expression param = (Expression) parameters.get(0);
                if (parameters.size() == 2) {
                    Expression exp = (Expression) parameters.get(1);
                    return "  HelperFunctions.recursiveNodesExcept(" + createScriptConvertToNodeOrSet(param) + ",(Object) " + exp.toJavaCode() + ")";
                } else {
                    StringBuffer buffer = new StringBuffer();
                    buffer.append("  HelperFunctions.recursiveNodesExcept("+ createScriptConvertToNodeOrSet(param)+ ",new Object[] { ");
                    for (int i = 1; i < parameters.size(); i++) {
                        if (i > 1) {
                            buffer.append(',');
                        }
                        buffer.append(((Expression) parameters.get(i)).toJavaCode());
                    }
                    buffer.append("})");
                    return buffer.toString();
                }
                
            }
        }
        
    }

    static class OperationCompileResult {
        String tempVariableName;
        String codeFragment;

        public OperationCompileResult() {
            tempVariableName = "null";
        }

        public OperationCompileResult(String variableName, String codeFragment) {
            this.tempVariableName = variableName;
            this.codeFragment = codeFragment;
        }
    }

    static class CompileState {
        int lastTempVariable = 0;

        public CompileState() {

        }
    }
    
    protected  void setupParser(Parser parser) {
        super.setupParser(parser);
        parser.getContext().addFunctionDef(RECURSIVE_NODES, new RecursiveNodesFunction());
        parser.getContext().addFunctionDef(RECURSIVE_NODES_EXCEPT, new RecursiveNodesExceptFunction());
        parser.getContext().addFunctionDef(TO_LIST, new NodeFunction(1, Integer.MAX_VALUE, Expression.RET_SET) {

            public String toJavaCode(String name, List parameters) throws CompileException {
                if (parameters.size()==1) {
                    Expression param = (Expression) parameters.get(0);
                    switch (param.getReturnType()) {
                        case Expression.RET_NODE:
                            return "  HelperFunctions.toList((" + NODE_TYPE + ")" + param.toJavaCode() + ")";
                        case Expression.RET_STRING:
                            return "  HelperFunctions.toList((String)" + param.toJavaCode() + ")";
                        case Expression.RET_SET:
                            return "  " + param.toJavaCode() + "";
                    }
                    checkType("first parameter",param.getReturnType());
                } else {
                    StringBuffer buffer = new StringBuffer();
                    buffer.append("  HelperFunctions.toList(new Object[] { ");
                    for (int i = 0; i < parameters.size(); i++) {
                        if (i > 0) {
                            buffer.append(',');
                        }
                        buffer.append(((Expression) parameters.get(i)).toJavaCode());
                    }
                    buffer.append("})");
                    return buffer.toString();
                }
                return null;
            }
        });
        parser.getContext().addFunctionDef("ParentCategory", new Context.FunctionDef(1, 1, Expression.RET_NODE) {
            public String toJavaCode(String name, List parameters) throws CompileException {
                Expression param = (Expression) parameters.get(0);
                return "  HelperFunctions.getParentCategory(" + createScriptConvertToNode(param) + ")";
            }
        });
        parser.getContext().addFunctionDef("TopLevelCategory", new Context.FunctionDef(1, 1, Expression.RET_NODE) {
            public String toJavaCode(String name, List parameters) throws CompileException {
                Expression param = (Expression) parameters.get(0);
                return "  HelperFunctions.getToplevelCategory(" + createScriptConvertToNode(param) + ")";
            }
        });

        parser.getContext().addFunctionDef("Department", new Context.FunctionDef(1, 1, Expression.RET_NODE) {
            public String toJavaCode(String name, List parameters) throws CompileException {
                Expression param = (Expression) parameters.get(0);
                return "  HelperFunctions.getParentDepartment(" + createScriptConvertToNode(param) + ")";
            }
        });
        
        parser.getContext().addVariable(CURRENT_NODE, Expression.RET_NODE);
        parser.getContext().addVariable(EXPLICIT_LIST, Expression.RET_SET);
        
    }

    public DataGenerator createDataGenerator(String name, String expression) throws CompileException {
        BlockExpression expr = parse(expression);
        try {
            if (expr.size()>1) {
                throw new CompileException("One expression expected instead of "+expr.size()+" in '"+expr.toCode()+"'!");
            }
            
            if (optimize) {
                RecursiveNodesCallOptimizer optimizer = new RecursiveNodesCallOptimizer();
                expr.visit(optimizer);
                if (optimizer.isOptimizationOccured()) {
                    expression += " -> optimized to : "+expr.toCode();
                }
            }
            VariableCollector vc = new VariableCollector();
            expr.visit(vc);

            Class generated = compileAlgorithm(name, expr, expression, vc);
            
            DataGenerator dg = (DataGenerator) generated.newInstance();
            

            Context context = getParser().getContext();
            
            Set vars = vc.getVariables();
            for (Iterator iter = vars.iterator(); iter.hasNext();) {
                String varName = (String) iter.next();
                int type = context.getVariableType(varName);
                if (type!=Expression.RET_FLOAT && type!=Expression.RET_INT) {
                    iter.remove();
                }
            }
            dg.setFactors(vars);
            return dg;
        } catch (InstantiationException e) {
            throw new ClassCompileException("InstantiationException:" + e.getMessage(), e);
        } catch (IllegalAccessException e) {
            throw new ClassCompileException("IllegalAccessException:" + e.getMessage(), e);
        } catch (VisitException e) {
            throw new ClassCompileException("VisitException:" + e.getMessage(), e);
        }
    }

    protected Class compileAlgorithm(String name, BlockExpression ast, String toStringValue, VariableCollector vc) throws CompileException {

        CtClass class1 = pool.makeClass(packageName + name);
        CtClass parent;
        
        boolean doCaching = caching && isCacheable(vc.getVariables());
        
        try {
            parent = doCaching ? pool.get(CachingDataGenerator.class.getName()) : pool.get(DataGenerator.class.getName());
            class1.setSuperclass(parent);
            pool.importPackage("java.util");
            pool.importPackage("com.freshdirect.smartstore.scoring");
            pool.importPackage("com.freshdirect.smartstore");

            CtMethod method = createGenerateMethod(class1, ast, doCaching, vc);
            if (method != null) {
                class1.addMethod(method);
            }
            if (doCaching) {
                method = createKeyCreatorMethod(class1, toStringValue, vc);
                class1.addMethod(method);
            }
            
            method = createToStringMethod(class1, toStringValue);
            class1.addMethod(method);

            return class1.toClass();
        } catch (NotFoundException e) {
            throw new ClassCompileException("NotFound:" + e.getMessage(), e);
        } catch (CannotCompileException e) {
            throw new ClassCompileException("CannotCompile:" + e.getMessage(), e);
        } catch (VisitException e) {
            throw new ClassCompileException("VisitException:" + e.getMessage(), e);
        }
    }


    private CtMethod createKeyCreatorMethod(CtClass class1, String toStringValue, VariableCollector vc) throws CannotCompileException {
        Set keys = this.getCachingKeys(vc.getVariables());
        StringBuffer b = new StringBuffer("public String getKey(SessionInput input) {\n");
        b.append("   return \""+toStringValue.replace('"', '\'')+"\"");
        if (keys.contains(CURRENT_NODE)) {
            b.append("+ '$' + HelperFunctions.getCurrentNodeCacheKey(input)");
        }
        if (keys.contains(EXPLICIT_LIST)) {
            b.append("+ '$' + HelperFunctions.getExplicitListCacheKey(input)");
        }
        b.append(";\n");
        b.append(" } ");
        return CtNewMethod.make(b.toString(), class1);
    }

    private CtMethod createGenerateMethod(CtClass class1, BlockExpression ast, boolean doCaching,VariableCollector vc) throws CannotCompileException, CompileException, VisitException {
        CompileState c = new CompileState();

        Expression expression = ast.get(0);
        expression.validate();
        
        OperationCompileResult oc = compile(c, expression);
        StringBuffer buffer = new StringBuffer("public List ")
            .append(doCaching? "generateImpl" : "generate" )
            .append("(com.freshdirect.smartstore.SessionInput sessionInput, DataAccess input) {\n");
        
        buffer.append(" String userId = sessionInput.getCustomerId();\n");
        if (vc.getVariables().contains(CURRENT_NODE)) {
            buffer.append(" "+NODE_TYPE+ " currentProduct = sessionInput.getCurrentNode();\n");
        }
        if (vc.getVariables().contains(EXPLICIT_LIST)) {
            buffer.append(" "+SET_TYPE+ ' ' + EXPLICIT_LIST+ "  = sessionInput.getExplicitList();\n");
        }
        
        
        buffer.append(oc.codeFragment);
        buffer.append("  return ").append(oc.tempVariableName).append(";\n");
        buffer.append("}");
        return CtNewMethod.make(buffer.toString(), class1);
    }

    private OperationCompileResult compile(CompileState c, Expression expression) throws CompileException {
        OperationCompileResult oc = new OperationCompileResult();
        if (expression instanceof VariableExpression) {
            oc = compileVariable(c, (VariableExpression) expression);
        } else if (expression instanceof Operation) {
            oc = compileOperation(c, (Operation) expression);
        } else if (expression instanceof FunctionCall) {
            oc = compileFunctionCall(c, (FunctionCall) expression);
        }
        return oc;
    }
    
    private OperationCompileResult compileFunctionCall(CompileState c, FunctionCall expression) throws CompileException {
        String varName = "tmp" + c.lastTempVariable++;
        int retType = expression.getReturnType();
        StringBuffer buffer = new StringBuffer();
        buffer.append("  ").append(getType(retType)).append(' ').append(varName).append(" = ").append(expression.toJavaCode()).append(";\n");
        return new OperationCompileResult(varName, buffer.toString());
    }

    private OperationCompileResult compileVariable(CompileState c, VariableExpression expression) throws CompileException {
        if (expression.getReturnType() != Expression.RET_SET) {
            throw new CompileException(CompileException.TYPE_ERROR, "Collection expected for variable:" + expression.getVariableName());
        }
        String varName = "tmp" + c.lastTempVariable++;
        if (isVariableFromDatasource(expression.getVariableName())) {
            OperationCompileResult oc = new OperationCompileResult(varName, "  List " + varName + " = input.getDatasource(sessionInput, \"" + expression.getVariableName()
                    + "\");\n");
            return oc;
        } else {
            OperationCompileResult oc = new OperationCompileResult(varName, "  List " + varName + " = " + expression.getVariableName() + ";\n");
            return oc;
        }
            
    }
    
    protected boolean isVariableFromDatasource(String name) {
        return !EXPLICIT_LIST.equals(name);
    }
    

    private OperationCompileResult compileOperation(CompileState c, Operation expression) throws CompileException {
        String arrayName = "arr"+c.lastTempVariable;
        String varName = "tmp" + c.lastTempVariable++;
        
        StringBuffer buffer = new StringBuffer();
        buffer.append("  List ").append(varName).append(" = new ArrayList();\n");
        buffer.append("  String[] ").append(arrayName).append(";\n");
        char operation = '+';
        
        for (int i=0;i<expression.size();i++) {
            Expression xpr = expression.get(i);
            switch (operation) {
                case '+' : {
                    OperationCompileResult cc = compile(c, xpr);
                    buffer.append(cc.codeFragment);
                    buffer.append("  ").append(varName).append(".addAll(").append(cc.tempVariableName).append(");\n");
                    //buffer.append("  HelperFunctions.addAll(").append(varName).append(",").append(cc.tempVariableName).append(");\n");
                    break;
                }
                case '*' : {
                    OperationCompileResult cc = compile(c, xpr);
                    buffer.append(cc.codeFragment);
                    buffer.append("  ").append(varName).append(".retainAll(").append(cc.tempVariableName).append(");\n");
                    break;
                }
                case '-' : {
                    OperationCompileResult cc = compile(c, xpr);
                    buffer.append(cc.codeFragment);
                    buffer.append("  ").append(varName).append(".removeAll(").append(cc.tempVariableName).append(");\n");
                    break;
                }
                case ':' : {
                    try {
                        /* 
                         * The expected function is something like this for filtering:
                         * 
                         * for (Iterator iter=tmp0.iterator();iter.hasNext();) {
                         *   TypeX obj = (TypeX) iter.next();
                         *   Map variables = input.getVariables(obj);
                         *   double popularity = variables.get("popularity").doubleValue();
                         *   int weight = variables.get("weight").doubleValue();
                         *   
                         *   if (someFunc(popularity, weight)==0) {
                         *      iter.remove();
                         *   }
                         *  }
                         * 
                         * 
                         */
                        
                        VariableCollector vc = new VariableCollector();
                        xpr.visit(vc);
                        
                        boolean constantExpression = vc.getVariables().isEmpty();
                        if (!constantExpression) {
                            buffer.append("  ").append(arrayName).append(" = new String[] {\n");
                            boolean first = true;
                            for (Iterator iter=vc.getVariables().iterator();iter.hasNext();) {
                                if (first) {
                                    first = false;
                                } else {
                                    buffer.append(",\n");
                                }
                                buffer.append("      \"").append(iter.next()).append('"');
                            }
                            buffer.append("   };\n");
                        }
                        buffer.append("  for (Iterator iter=").append(varName).append(
                                ".iterator();iter.hasNext();) {\n     " + NODE_TYPE + " obj = (" + NODE_TYPE + ") iter.next();\n");
                        Context context = getParser().getContext();
                        if (!constantExpression) {
                            buffer.append("     double[] vars = input.getVariables(userId, obj,").append(arrayName).append(");\n");
                            int index = 0;
                            for (Iterator iter=vc.getVariables().iterator();iter.hasNext();) {
                                buffer.append("   ").append(declareVariable(context, (String) iter.next(), "vars["+index+']'));
                            }
                        }
                        buffer.append("     if ((").append(xpr.toJavaCode()).append(") == 0) {\n"
                                + "        iter.remove();\n"
                                + "     }\n"
                                + "   }");
                        break;
                    } catch (VisitException e) {
                        e.printStackTrace();
                    }
                    
                }
            }
            operation = i<expression.size()-1 ? expression.getOperator(i) : ' ';
        }
        
        return new OperationCompileResult(varName, buffer.toString());
    }

    String getType(int type) throws CompileException {
        switch (type) {
            case Expression.RET_NODE : return NODE_TYPE;
            case Expression.RET_SET : return "List";
            case Expression.RET_STRING : return "String";
            case Expression.RET_FLOAT : return "double";
            case Expression.RET_INT : return "int";
            default : throw new CompileException(CompileException.TYPE_ERROR, "Java type not found for:"+Expression.getTypeName(type));
        }
    }

    void checkType(String name, int type) throws CompileException {
        if (type != Expression.RET_NODE && type != Expression.RET_STRING && type != Expression.RET_SET) {
            throw new CompileException(CompileException.TYPE_ERROR, "The "+name+" type is " + Expression.getTypeName(type)
                    + " instead of the expected node/set/string!");
        }
    }

    void checkNodeStringType(String name, int type) throws CompileException {
        if (type != Expression.RET_NODE && type != Expression.RET_STRING) {
            throw new CompileException(CompileException.TYPE_ERROR, "The "+name+" type is " + Expression.getTypeName(type)
                    + " instead of the expected node/string!");
        }
    }

    public void setOptimize(boolean optimize) {
        this.optimize = optimize;
    }
    
    public boolean isOptimize() {
        return optimize;
    }
    
    public void setCaching(boolean caching) {
        this.caching = caching;
    }
    
    public boolean isCaching() {
        return caching;
    }

    /**
     * 
     * @param globalVariables
     */
    public void setGlobalVariables(Set globalVariables) {
        this.globalVariables = globalVariables;
    }
    
    /**
     * return the cache key for the given variable, or null, if it's not possible to cache. For example 'PurchaseHistory' is specific to user, 
     * so it shouldn't be cached. 'explicitList', 'currentProduct', 'FeaturedItems' and 'CandidateList' is global, so it can be cached.
     *   
     * @param variable
     * @return
     */
    private String getCacheKeyForVariable(String variable) {
        if (EXPLICIT_LIST.equals(variable)) {
            return EXPLICIT_LIST;
        }
        if (CACHE_BY_CURRENT_NODE_ALSO) {
            if (CURRENT_NODE.equals(variable)) {
                return CURRENT_NODE;
            }
            if ("FeaturedItems".equals(variable)) {
                return CURRENT_NODE;
            }
            if ("CandidateLists".equals(variable)) {
                return CURRENT_NODE;
            }
        }
        return null;
    }
    
    private boolean isCacheable(Collection usedVariables) {
        for (Iterator iter=usedVariables.iterator();iter.hasNext();) {
            String varName = (String) iter.next();
            if (!globalVariables.contains(varName)) {
                if (getCacheKeyForVariable(varName)==null) {
                    return false;
                }
            }
        }
        return true;
    }
    
    private Set getCachingKeys(Collection usedVariables) {
        Set result = new HashSet();
        for (Iterator iter=usedVariables.iterator();iter.hasNext();) {
            String varName = (String) iter.next();
            if (!globalVariables.contains(varName)) {
                String key = getCacheKeyForVariable(varName);
                if (key==null) {
                    return null;
                } else {
                    result.add(key);
                }
            }
        }
        return result;
    }
    
    
    
}
