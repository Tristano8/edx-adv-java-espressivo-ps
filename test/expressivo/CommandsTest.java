/* Copyright (c) 2015-2017 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package expressivo;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

/**
 * Tests for the static methods of Commands.
 */
public class CommandsTest {

    // Testing strategy
    //
    // String expression
    // Constant
    // Variable expression
    // Sum expression
    // Times expression
    // Multiple expressions
    // 
    //
    // Variable
    // Variable contained in expression
    // Variable not contained in expression
    // More than one variable in expression
    //
    // Partitions
    //
    // differentiate()
    // 
    // Constant expression
    // Variable expression with respect to same variable
    // Sum expression with more than one variable
    // Times expression with more than one variable
    //
    // simplify()
    //
    // environment has 0 variables
    // environment has 1 variable
    // environment has 2 or more variables
    //
    // variables occur in middle of expression
    // variables occur at ends of expression
    //
    // expression involves addition
    // expression involves multiplication
    // expression involves parentheses
    //
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    @Test
    public void testDifferentiateConstant() throws IllegalArgumentException, IOException {
        String differential = Commands.differentiate("8","x").toString();
        assertEquals(differential,"0.0");
    }
    
    @Test
    public void testDifferentiateSingleVar() throws IllegalArgumentException, IOException {
        String differential = Commands.differentiate("x", "x").toString();
        assertEquals(differential,"1.0");
    }
    
    @Test
    public void testDifferentiateDifferentVar() throws IllegalArgumentException, IOException {
        String differential = Commands.differentiate("x", "y").toString();
        assertEquals(differential,"0.0");
    }
    
    @Test
    public void testVarPlusNumberDifferential() throws IllegalArgumentException, IOException {
        String differential = Commands.differentiate("x+9.0","x");
        assertEquals(differential,"(1.0+0.0)");
    }
    
    @Test
    public void testDifferentiateSum() throws IllegalArgumentException, IOException {
        String differential = Commands.differentiate("x+y+x", "x").toString();
        assertEquals(differential,"((1.0+0.0)+1.0)");
    }
    
    @Test
    public void testDifferentiateTimes() throws IllegalArgumentException, IOException {
        String differential = Commands.differentiate("x*x", "x").toString();
        assertEquals(differential,"(x*1.0+x*1.0)");
    }
    
    @Test
    public void testDifferentiateIntTimesVar() throws IllegalArgumentException, IOException {
        String differential = Commands.differentiate("3*x", "x").toString();
        System.out.println(differential);
        assertEquals(differential,"(3.0*1.0+x*0.0)");
    }
    
    @Test
    public void testDifferentiateMixedExpression () throws IllegalArgumentException, IOException {
        String differential = Commands.differentiate("x*x+3*x+9", "x");
        System.out.println(differential.toString());
        assertEquals(differential,"(((x*1.0+x*1.0)+(3.0*1.0+x*0.0))+0.0)");
    }
    
    //tests for Commands.simplify()
    
    @Test
    public void testSimplifyIntegerExpression () throws IllegalArgumentException, IOException {
        Map<String, Double> environment = new HashMap<String, Double>();
        String expression = "1.0+1.0"; 
        assertEquals(Commands.simplify(expression, environment), "2.0");
    }
    
    @Test
    public void testSimplifyOneVar () throws IllegalArgumentException, IOException {
        Map<String, Double> environment = new HashMap<String, Double>();
        environment.put("x", 10.0);
        String expression = "x+5";
        String expression2 = "x*10.0";
        assertEquals(Commands.simplify(expression, environment), "15.0");
        assertEquals(Commands.simplify(expression2, environment), "100.0");
    }
    
    @Test
    public void testSimplifyTwoVar () throws IllegalArgumentException, IOException {
        Map<String, Double> environment = new HashMap<String, Double>();
        environment.put("x", 10.0);
        environment.put("y", 20.0);
        String expression = "y+6+x";
        String expression2 = "2*x+y";
        assertEquals(Commands.simplify(expression, environment), "36.0");
        assertEquals(Commands.simplify(expression2, environment), "40.0");
    }
    
    @Test
    public void testSimplifyVarNotInEnvironment() throws IllegalArgumentException, IOException {
        Map<String, Double> environment = new HashMap<String, Double>();
        environment.put("x", 3.0);
        String expression = "y + 8.0";
        String expression2 = "y + 8.0 + x";
        assertEquals(Commands.simplify(expression, environment), "(y+8.0)");
        assertEquals(Commands.simplify(expression2, environment), "((y+8.0)+3.0)");
    }
    
    @Test
    public void testSimplifyMultipleOfSameVar () throws IllegalArgumentException, IOException {
        Map<String, Double> environment = new HashMap<String, Double>();
        environment.put("x", 5.0);
        String expression = "(x+5)*(5+x)";
        String expression2 = "(x*5)+(5*x)";
        assertEquals(Commands.simplify(expression, environment),"100.0");
        assertEquals(Commands.simplify(expression2, environment),"50.0");
    }
    
}
