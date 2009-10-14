package com.freshdirect.smartstore.scoring;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import junit.framework.TestCase;

import com.freshdirect.cms.core.MockContentNodeModel;
import com.freshdirect.cms.fdstore.FDContentTypes;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.smartstore.dsl.ClassCompileException;
import com.freshdirect.smartstore.dsl.CompileException;
import com.freshdirect.smartstore.dsl.Expression;
import com.freshdirect.smartstore.sampling.RankedContent;

public class CompilerTest extends TestCase {
    ScoringAlgorithmCompiler comp;

    ContentNodeModel[] model = new ContentNodeModel[4];
    
    protected void setUp() throws Exception {
        comp = new ScoringAlgorithmCompiler();
        for (int i=0;i<model.length;i++) {
            model[i] = new MockContentNodeModel(FDContentTypes.PRODUCT, "a"+i);
        }
    }

    public void testSimpleCompilation() throws CompileException {
        comp.getParser().getContext().addVariable("ia", Expression.RET_INT);
        comp.getParser().getContext().addVariable("fb", Expression.RET_FLOAT);

        ScoringAlgorithm sa = comp.createScoringAlgorithm("test1", "ia * fb");

        assertNotNull("scoring algorithm", sa);

        String[] variableNames = sa.getVariableNames();
        assertNotNull("variableNames", variableNames);
        assertEquals("two variable", 2, variableNames.length);
        
        int fbPos = -1;
        int iaPos = -1;
        for (int i=0;i<variableNames.length;i++) {
            if ("fb".equals(variableNames[i])) {
                fbPos = i;
            }
            if ("ia".equals(variableNames[i])) {
                iaPos = i;
            }
        }
        
        assertTrue("fb variable found", fbPos!=-1);
        assertTrue("ia variable found", iaPos!=-1);

        assertEquals("output variable", 1, sa.getReturnSize());
        
        Map<String, Number> var = new HashMap<String, Number>();
        var.put("ia", new Integer(31));
        var.put("fb", new Double(7));

        Score scores = sa.getScores(var);

        assertNotNull("score", scores);
        assertEquals("values 1", 1, scores.size());
        assertEquals("31 * 7", 31 * 7.0, scores.get(0), 0.001);

        Random rnd = new Random();
        for (int i = 0; i < 10; i++) {
            int ia = rnd.nextInt(1000);
            double fb = ((double)rnd.nextInt(1000)) / 100.0;
            var.put("ia", new Integer(ia));
            var.put("fb", new Double(fb));

            scores = sa.getScores(var);

            assertNotNull("score", scores);
            assertEquals("values 1", 1, scores.size());

            assertEquals("ia:" + ia + " * " + fb, ia * fb, scores.get(0), 0.001);
        }
        for (int i = 0; i<10; i++) {
            int ia = rnd.nextInt(1000);
            double fb = (double) rnd.nextInt(1000) / 100.0;

            double[] values = new double[2];
            values[fbPos] = fb;
            values[iaPos] = ia;
            
            double[] sss = sa.getScores(values);
            assertNotNull("score", sss);
            assertEquals("values 1", 1, sss.length);

            assertEquals("ia:" + ia + " * " + fb, ia * fb, sss[0], 0.001);
        }
    }

    public void testCompoundCompilation() throws CompileException {
        comp.getParser().getContext().addVariable("ia", Expression.RET_INT);
        comp.getParser().getContext().addVariable("ic", Expression.RET_INT);
        comp.getParser().getContext().addVariable("fb", Expression.RET_FLOAT);

        ScoringAlgorithm sa = comp.createScoringAlgorithm("testCompound", "ia * ic; ia - fb");
        assertEquals("output variable", 2, sa.getReturnSize());

        String[] exps = sa.getExpressions();
        assertEquals("expressions", 2, exps.length);
        assertEquals("expression[0]", "$ia * $ic", exps[0]);
        assertEquals("expression[1]", "$ia - $fb", exps[1]);
        
        Map<String, Number> var = new HashMap<String, Number>();
        var.put("ia", new Integer(79));
        var.put("ic", new Integer(19));
        var.put("fb", new Double(42.1));

        Score scores = sa.getScores(var);

        assertNotNull("score", scores);
        assertEquals("values 2", 2, scores.size());
        assertEquals("79 * 19", 79 * 19, scores.get(0), 0.001);
        assertEquals("79 - 42", 79 - 42.1, scores.get(1), 0.001);
    }

    public void testFunctionCompilation() throws CompileException {
        comp.getParser().getContext().addVariable("fa", Expression.RET_FLOAT);
        comp.getParser().getContext().addVariable("fb", Expression.RET_FLOAT);

        ScoringAlgorithm sa = comp.createScoringAlgorithm("testFunction", "between(fa,0,0.5); between(fa*fb,0,0.5), atLeast(fa,0.5), atMost(fb,fa)");
        assertEquals("output variable", 4, sa.getReturnSize());

        Random rnd = new Random();
        for (int i = 0; i < 100; i++) {
            double fa = rnd.nextDouble();
            double fb = rnd.nextDouble();

            Map<String, Number> var = new HashMap<String, Number>();
            var.put("fa", new Double(fa));
            var.put("fb", new Double(fb));

            Score scores = sa.getScores(var);

            assertNotNull("score", scores);
            assertEquals("values 4", 4, scores.size());
            if (0 <= fa && fa <= 0.5) {
                assertEquals("fa in range ", 1, scores.get(0), 0.0001);
            } else {
                assertEquals("fa not in range ", 0, scores.get(0), 0.0001);
            }
            if (0 <= fa * fb && fa * fb <= 0.5) {
                assertEquals("fa*fb in range ", 1, scores.get(1), 0.0001);
            } else {
                assertEquals("fa*fb not in range ", 0, scores.get(1), 0.0001);
            }

            assertEquals("fa less than 0.5", (fa >= 0.5 ? 1.0 : 0.0), scores.get(2), 0.00001);
            assertEquals("fb greater than fa", (fa >= fb ? 1.0 : 0.0), scores.get(3), 0.00001);
        }
    }

    public void testErrorCodes() {
        comp.getParser().getContext().addVariable("ia", Expression.RET_INT);
        comp.getParser().getContext().addVariable("fb", Expression.RET_FLOAT);

        try {
            comp.createScoringAlgorithm("t1", "3 * + 5");
            fail("Syntax error expected");
        } catch (ClassCompileException e) {
            e.printStackTrace();
            fail("class compile exception:" + e.getMessage());
        } catch (CompileException e) {
            assertEquals("syntax error expected", CompileException.SYNTAX_ERROR, e.getCode());
        }
        try {
            comp.createScoringAlgorithm("t1", "3 * ( 5 + 3");
            fail("Syntax error expected");
        } catch (ClassCompileException e) {
            e.printStackTrace();
            fail("class compile exception:" + e.getMessage());
        } catch (CompileException e) {
            assertEquals("syntax error expected", CompileException.SYNTAX_ERROR, e.getCode());
        }
        try {
            comp.createScoringAlgorithm("t1", " 3 * x");
            fail("unknown variable exception expected");
        } catch (ClassCompileException e) {
            e.printStackTrace();
            fail("class compile exception:" + e.getMessage());
        } catch (CompileException e) {
            assertEquals("unknown variable exception expected", CompileException.UNKNOWN_VARIABLE, e.getCode());
        }

        try {
            comp.createScoringAlgorithm("t1", " cos(0) * 4");
            fail("unknown function exception expected");
        } catch (ClassCompileException e) {
            e.printStackTrace();
            fail("class compile exception:" + e.getMessage());
        } catch (CompileException e) {
            assertEquals("unknown function exception expected", CompileException.UNKNOWN_FUNCTION, e.getCode());
        }
    }

    public void testTopLimitCompilation() throws CompileException {
        comp.getParser().getContext().addVariable("fa", Expression.RET_FLOAT);
        comp.getParser().getContext().addVariable("fb", Expression.RET_FLOAT);

        ScoringAlgorithm sa = comp.createScoringAlgorithm("testTopLimitFunction", "fa:top; fb; fa*fb");
        assertEquals("output variable", 3, sa.getReturnSize());

        String[] exps = sa.getExpressions();
        assertEquals("expressions", 3, exps.length);
        assertEquals("expression[0]", "$fa", exps[0]);
        assertEquals("expression[1]", "$fb", exps[1]);
        assertEquals("expression[2]", "$fa * $fb", exps[2]);

        List<String> result = calculateScores(sa, true);

        assertEquals("1. element", result.get(0), "a2");
        assertEquals("2. element", result.get(1), "a0");
        assertEquals("3. element", result.get(2), "a1");
        assertEquals("4. element", result.get(3), "a3");
    }

    public void testNoTopLimitCompilation() throws CompileException {
        comp.getParser().getContext().addVariable("fa", Expression.RET_FLOAT);
        comp.getParser().getContext().addVariable("fb", Expression.RET_FLOAT);

        ScoringAlgorithm sa = comp.createScoringAlgorithm("testNoTopLimitFunction", "fa; fb; fa*fb");
        assertEquals("output variable", 3, sa.getReturnSize());
        String[] exps = sa.getExpressions();
        assertEquals("expressions", 3, exps.length);
        assertEquals("expression[0]", "$fa", exps[0]);
        assertEquals("expression[1]", "$fb", exps[1]);
        assertEquals("expression[2]", "$fa * $fb", exps[2]);
        List<String> result = calculateScores(sa, false);

        assertEquals("1. element", result.get(0), "a2");
        assertEquals("2. element", result.get(1), "a1");
        assertEquals("3. element", result.get(2), "a0");
        assertEquals("4. element", result.get(3), "a3");
    }
    
    
    int[] fa = { 1, 2, 3, 0};
    int[] fb = { 5, 3, 1, 0};

    private List<String> calculateScores(ScoringAlgorithm sa, boolean topLimitAdded) {
        String[] variableNames = sa.getVariableNames();
        
        assertNotNull("variableNames", variableNames);
        assertEquals("two variable", 2, variableNames.length);

        int fbPos = -1;
        int faPos = -1;
        for (int i=0;i<variableNames.length;i++) {
            if ("fa".equals(variableNames[i])) {
                faPos = i;
            }
            if ("fb".equals(variableNames[i])) {
                fbPos = i;
            }
        }
        assertTrue("fa variable found", faPos!=-1);
        assertTrue("fb variable found", fbPos!=-1);
        
        OrderingFunction orderingFunction = sa.createOrderingFunction();
        if (topLimitAdded) {
            assertTrue("ordering function is a top limiting", orderingFunction instanceof TopLimitOrderingFunction);
        } else {
            assertTrue("ordering function is NOT a top limiting", !(orderingFunction instanceof TopLimitOrderingFunction));
        }

        double[] vars = new double[2];
        for (int i=0;i<model.length;i++) {
            vars[faPos] = fa[i];
            vars[fbPos] = fb[i];
            
            double[] result = sa.getScores(vars);
            orderingFunction.addScore(model[i], result);
        }
        List<RankedContent.Single> result = orderingFunction.getRankedContents();
        List<String> strings = new ArrayList<String>(result.size());
        for (RankedContent.Single r : result) {
            strings.add(r.getId());
        }
        return strings;
    }
    
}
