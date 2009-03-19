package com.freshdirect.smartstore.scoring;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import junit.framework.TestCase;

import com.freshdirect.smartstore.dsl.ClassCompileException;
import com.freshdirect.smartstore.dsl.CompileException;
import com.freshdirect.smartstore.dsl.Expression;

public class CompilerTest extends TestCase {
    ScoringAlgorithmCompiler comp;

    protected void setUp() throws Exception {
        comp = new ScoringAlgorithmCompiler();
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
        
        Map var = new HashMap();
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

        Map var = new HashMap();
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

            Map var = new HashMap();
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

}
