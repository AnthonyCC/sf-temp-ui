package com.freshdirect.smartstore.dsl;

import junit.framework.TestCase;

import com.freshdirect.smartstore.dsl.Context.FunctionDef;
import com.freshdirect.smartstore.dsl.Context.MultiReturnTypeFunctionDef;

public class ParserTest extends TestCase {

    Parser parser;

    protected void setUp() throws Exception {
        parser = new Parser();
        parser.getContext().addVariable("variable", Expression.RET_FLOAT);
        parser.getContext().addVariable("b", Expression.RET_INT);
        parser.getContext().addVariable("c", Expression.RET_INT);
        parser.getContext().addFunctionDef("min", new MultiReturnTypeFunctionDef(2, -1));
        parser.getContext().addFunctionDef("max", new MultiReturnTypeFunctionDef(2, -1));
        parser.getContext().addFunctionDef("exp", new FunctionDef(1, 1, Expression.RET_FLOAT));
        parser.getContext().addFunctionDef("round", new FunctionDef(1, 1, Expression.RET_INT));
        parser.getContext().addFunctionDef("int", new FunctionDef(1, 1, Expression.RET_INT));
        parser.getContext().addFunctionDef("cms", new FunctionDef(2, 2, Expression.RET_STRING));

        parser.getContext().addVariable("set", Expression.RET_SET);
        parser.getContext().addVariable("setx", Expression.RET_SET);
        parser.getContext().addFunctionDef("atLeast", new FunctionDef(2, 2, Expression.RET_INT));

    }

    public void testToCodeFunctions() {
        Expression ex = new Operation(new NumberExp("2.0"), '+', new NumberExp("3"));
        assertEquals("basic", "2.0 + 3", ex.toCode());

        FunctionCall f = new FunctionCall("max");
        f.add(ex);
        f.add(new FunctionCall("rnd"));
        assertEquals("with function call", "max(2.0 + 3,rnd())", f.toCode());

        ex = new Operation(new NumberExp(5.1), '-', new NumberExp(5.00001));

        f.add(ex);
        assertEquals("with function call", "max(2.0 + 3,rnd(),5.1 - 5)", f.toCode());
    }
    
    
    public void testPriorities() throws CompileException {
        {        
            BlockExpression expression = parser.parse("5 + 3 * 2");
            assertNotNull("expression", expression);
            assertEquals("expression size", 1, expression.size());
            assertNotNull("expression", expression.get(0));
    
            Expression expr = expression.get(0);
            Operation oper = (Operation) expr;
            BinaryExpression xpr = oper.fixPrecedence();
            assertEquals("fixed precedence", "(5 + (3 * 2))", xpr.toCode());
            assertEquals("value correct", (5 + (3 * 2)), xpr.evaluateExpression().intValue());
        }

        {
            BlockExpression expression = parser.parse("5 + (3 - 1) * 2 + 3");
            assertNotNull("expression", expression);
            assertEquals("expression size", 1, expression.size());
            assertNotNull("expression", expression.get(0));
    
            Expression expr = expression.get(0);
            Operation oper = (Operation) expr;
            BinaryExpression xpr = oper.fixPrecedence();
            assertEquals("fixed precedence", "((5 + ((3 - 1) * 2)) + 3)", xpr.toCode());
            assertEquals("value correct", ((5 + ((3 - 1) * 2)) + 3), xpr.evaluateExpression().intValue());
        }
        
        {
            BlockExpression expression2 = parser.parse("set + setx : 3");
            assertNotNull("expression2", expression2);
            assertEquals("expression size", 1, expression2.size());
            assertNotNull("expression", expression2.get(0));
    
            Expression expr = expression2.get(0);
            Operation oper = (Operation) expr;
            BinaryExpression xpr = oper.fixPrecedence();
            assertEquals("fixed precedence", "($set + ($setx : 3))", xpr.toCode());
        }
        
        {
            BlockExpression expression2 = parser.parse("set + setx : 3 : 2");
            assertNotNull("expression3", expression2);
            assertEquals("expression size", 1, expression2.size());
            assertNotNull("expression", expression2.get(0));
    
            Expression expr = expression2.get(0);
            Operation oper = (Operation) expr;
            BinaryExpression xpr = oper.fixPrecedence();
            assertEquals("fixed precedence", "($set + (($setx : 3) : 2))", xpr.toCode());
        }
        
        
    }

    public void testParsing() throws CompileException {
        BlockExpression expression = parser.parse("2.3");

        assertEquals("parse number", "2.3", expression.toCode());

        assertEquals("simple variable", "$variable", parser.parse(" variable ").toCode());

        try {
            parser.parse("min");
            fail("min is not a variable, exception expected!");
        } catch (CompileException e) {

        }
        assertEquals("parse simple function", "max(2)", parser.parse("max(2.0)").toCode());
        assertEquals("parse simple function", "max(2,3.1)", parser.parse("max(2,3.1)").toCode());

        assertEquals("parse addition", "2 + 5", parser.parse("2+5").toCode());

        assertEquals("parse addition with function", "max(3,1) + 2", parser.parse("max(3.00,1)+2").toCode());
        assertEquals("parse addition with two function", "max(3,1) + round(2.3)", parser.parse("max(3.00,1)+round(2.3)").toCode());

        assertEquals("multiple statement", "max(1,3);int(2)", parser.parse("max(1.000,3.000),   int(2)").toCode());
    }

    public void testOperations() throws CompileException {

        assertEquals("test 1", "(2 + 3) * (1 - 3)", parser.parse("(2+3)*(1-3)").toCode());
        assertEquals("test 2", "(2 + $variable * 3) * (1 - 3)", parser.parse("(2+variable * 3)*(1-3)").toCode());

        assertEquals("test 3", "2 * -3 + 5", parser.parse("2 * -3 + 5").toCode());

        try {
            parser.parse("2 * + 3");
            fail("compile exception expected");
        } catch (CompileException e) {

        }

        assertEquals("test 4", "round(5.3 + 3) * 3", parser.parse("round(5.3+3)*3").toCode());
    }

    public void testVariableHandling() throws CompileException, VisitException {
        BlockExpression xpr = parser.parse("max(1,variable)+ b");

        VariableCollector v = new VariableCollector();
        xpr.visit(null, v);
        assertTrue("variable found", v.getVariables().contains("variable"));
        assertTrue("b found", v.getVariables().contains("b"));
        assertEquals("2 variable only", 2, v.getVariables().size());
    }

    public void testTypeValidating() throws CompileException {
        assertEquals("ret_int   ", Expression.RET_INT, compile("3 + 5").getReturnType());
        assertEquals("ret_float ", Expression.RET_FLOAT, compile("3 + 5.1").getReturnType());
        assertEquals("ret_float ", Expression.RET_FLOAT, compile("3 / 2").getReturnType());
        assertEquals("ret_int ", Expression.RET_INT, compile("3 * 2").getReturnType());
        assertEquals("ret_int ", Expression.RET_INT, compile("3 + (2 * b)").getReturnType());
        assertEquals("ret_float ", Expression.RET_FLOAT, compile("3 + (2 * variable)").getReturnType());
    }

    public void testFunctionParameters() throws CompileException {
        BlockExpression xpr = parser.parse("round(5,3)");
        try {
            xpr.validate();
            fail("round has at most 1 parameter");
        } catch (CompileException e) {

        }
        xpr = parser.parse("max(5)");
        try {
            xpr.validate();
            fail("max has at least 2 parameter");
        } catch (CompileException e) {

        }
        xpr = parser.parse("max(5,3,2)");
        xpr.validate();
    }

    public void testFunctionTypeValidating() throws CompileException {
        assertEquals("ret_int   ", Expression.RET_INT, compile("round(5.3)").getReturnType());
        assertEquals("ret_int 2 ", Expression.RET_INT, compile("round(5.3 + 3) +5").getReturnType());
        assertEquals("ret_float ", Expression.RET_FLOAT, compile("round(5.3 + 3) + exp(5)").getReturnType());

        assertEquals("ret_int 3 ", Expression.RET_INT, compile("round(variable) * 3").getReturnType());
        assertEquals("ret_float 3 ", Expression.RET_FLOAT, compile("exp(round(variable) * 3)").getReturnType());
    }
    
    public void testMinus() throws CompileException {
        compile("5 * - 3");
        //compile("-5");
    }

    private Expression compile(String line) throws CompileException {
        BlockExpression xpr = parser.parse(line);
        xpr.validate();
        return xpr.get(0);
    }

    public void testFilteringFunctions() throws CompileException {
        assertEquals("ret_set 1", Expression.RET_SET, compile("set").getReturnType());
        assertEquals("ret_set 2", Expression.RET_SET, compile("set + set").getReturnType());
        assertEquals("ret_set 3", Expression.RET_SET, compile("set:atLeast(variable,1)").getReturnType());
        assertEquals("ret_set 4", Expression.RET_SET, compile("(set + setx) * set").getReturnType());
    }

    public void testTypeSafety() {
        executeTestType("set + 3");
        executeTestType("set * 3");
        executeTestType("3 + set");
    }
    
    public void testStringParsing() throws CompileException {
        assertEquals("ret_string 0", Expression.RET_STRING, compile("\"node\"").getReturnType());
        assertEquals("ret_string 1", Expression.RET_STRING, compile("cms(\"node\",\"value bvalue\")").getReturnType());
        assertEquals("ret_string 2", Expression.RET_STRING, compile("\"node\" + \"value bvalue\"").getReturnType());
        assertEquals("ret_string 3", Expression.RET_STRING, compile("\"node\" + 3").getReturnType());
        assertEquals("ret_string 4", Expression.RET_STRING, compile("\"node\" + 3.6").getReturnType());

        assertEquals("to_code 0", "cms(\"node\",\"value bvalue\")", compile("cms(\"node\",     \"value bvalue\")").toCode());
        assertEquals("to_code 1", "\"node\" + \"value bvalue\"", compile("\"node\" + \"value bvalue\"").toCode());
        assertEquals("to_code 2", "\"node\" + 3", compile("\"node\" + 3").toCode());
        assertEquals("to_code 3", "\"node\" + 3.6", compile("\"node\" + 3.6").toCode());
    }


    void executeTestType(String s) {
        try {
            compile(s);
            fail("type error expected for '" + s + "'");
        } catch (CompileException c) {
            assertEquals("type error expected for '" + s + "'", CompileException.TYPE_ERROR, c.getCode());
        }
    }

}
