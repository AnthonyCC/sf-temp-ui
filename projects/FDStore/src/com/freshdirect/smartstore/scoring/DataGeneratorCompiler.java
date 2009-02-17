package com.freshdirect.smartstore.scoring;

import java.util.Iterator;
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
import com.freshdirect.smartstore.dsl.Operation;
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

    final static String LIST_TYPE="com.freshdirect.fdstore.content.ContentNodeModel";
    
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

    public DataGenerator createDataGenerator(String name, String expression) throws CompileException {
        BlockExpression expr = parse(expression);
        Class generated = compileAlgorithm(name, expr, expression);
        try {
            DataGenerator dg = (DataGenerator) generated.newInstance();
            
            VariableCollector vc = new VariableCollector();
            expr.visit(vc);

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

    protected Class compileAlgorithm(String name, BlockExpression ast, String toStringValue) throws CompileException {

        CtClass class1 = pool.makeClass(packageName + name);
        CtClass parent;
        try {
            parent = pool.get(DataGenerator.class.getName());
            class1.setSuperclass(parent);
            pool.importPackage("java.util");
            pool.importPackage("com.freshdirect.smartstore.scoring");

            CtMethod method = createGenerateMethod(class1, ast);
            if (method != null) {
                class1.addMethod(method);
            }
            method = createToStringMethod(class1, toStringValue);
            class1.addMethod(method);

            return class1.toClass();
        } catch (NotFoundException e) {
            throw new ClassCompileException("NotFound:" + e.getMessage(), e);
        } catch (CannotCompileException e) {
            throw new ClassCompileException("CannotCompile:" + e.getMessage(), e);
        }
    }


    private CtMethod createGenerateMethod(CtClass class1, BlockExpression ast) throws CannotCompileException, CompileException {
        CompileState c = new CompileState();

        Expression expression = ast.get(0);
        OperationCompileResult oc = compile(c, expression);
        StringBuffer buffer = new StringBuffer("public List generate(com.freshdirect.smartstore.SessionInput sessionInput, DataAccess input) {\n");
        buffer.append(" String userId = sessionInput.getCustomerId();\n");
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
        }
        return oc;
    }
    
    private OperationCompileResult compileVariable(CompileState c, VariableExpression expression) throws CompileException {
        if (expression.getReturnType() != Expression.RET_SET) {
            throw new CompileException(CompileException.TYPE_ERROR, "Collection expected for variable:" + expression.getVariableName());
        }
        String varName = "tmp" + c.lastTempVariable++;
        OperationCompileResult oc = new OperationCompileResult(varName, "  List " + varName + " = input.getDatasource(sessionInput, \"" + expression.getVariableName()
                + "\");\n");
        return oc;
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
                    buffer.append("  HelperFunctions.addAll(").append(varName).append(",").append(cc.tempVariableName).append(");\n");
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
                                ".iterator();iter.hasNext();) {\n     " + LIST_TYPE + " obj = (" + LIST_TYPE + ") iter.next();\n");
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


}
