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
 * Tests for the Expression abstract data type.
 */
public class ExpressionTest {

    // Testing strategy
    //
    // Each implementation of the Expression interface should be tested
    //
    // Partitions:
    //
    //
    // Expression
    //
    // parser()
    // Expression using one number
    // Expression using two numbers
    // Expression using no operations
    // Expression using one operation
    // Expression using parens
    // Expression using nested parens
    // Expression using multiple operations
    // Expression using variables
    // Expression using both variables and numbers
    //
    // Variable
    // 
    // toString()
    //
    // equals()
    // variables with same name, different cases 
    //
    // hashCode()
    //
    //
    // Number
    //
    // toString()
    //
    // equals()
    // hashCode()
    //
    //
    // Plus
    //
    // toString()
    // equals()
    // hashCode()
    //
    //
    // Times
    //
    // toString()
    // equals()
    // hashCode()
    //
    //
    // 
    //
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    @Test
    public void parserTestSingleDigit() throws IllegalArgumentException, IOException {
      Expression tree = Expression.parse("3");
      Expression tree2 = Expression.parse("345890");
      assertEquals(tree.toString(),"3.0");
      assertEquals(tree2.toString(),"345890.0");
    }
    
    @Test
    public void parserTestDecimal() throws IllegalArgumentException, IOException {
      Expression tree = Expression.parse("3.8");
      Expression tree2 = Expression.parse("3.2*x");
      assertEquals(tree.toString(),"3.8");
      assertEquals(tree2.toString(),"3.2*x");
    }
    
    @Test
    public void parserTestSingleVar() throws IllegalArgumentException, IOException {
        Expression tree = Expression.parse("x");
        Expression tree2 = Expression.parse("Foo");
        assertEquals(tree.toString(),"x");
        assertEquals(tree2.toString(),"Foo");
    }
    
    @Test
    public void parserTestSinglePlus() throws IllegalArgumentException, IOException {
        Expression tree = Expression.parse("3+5");
        Expression tree2 = Expression.parse("3+x");
        assertEquals(tree.toString(),"(3.0+5.0)");
        assertEquals(tree2.toString(),"(3.0+x)");
    }
    
    @Test
    public void parserTestSingleTimes() throws IllegalArgumentException, IOException {
        Expression tree = Expression.parse("4*5");
        assertEquals(tree.toString(),"4.0*5.0");
    }
    
    @Test
    public void parserTestWhiteSpace() throws IllegalArgumentException, IOException {
        Expression tree = Expression.parse(" 3  +  4");
        assertEquals(tree.toString(),"(3.0+4.0)");
    }
    
    @Test
    public void parserTestSingleParens() throws IllegalArgumentException, IOException {
        Expression tree = Expression.parse("(3+5)");
        assertEquals(tree.toString(),"(3.0+5.0)");
    }
    
    @Test
    public void parserTestNestedParens() throws IllegalArgumentException, IOException {
        Expression tree = Expression.parse("((3))");
        assertEquals(tree.toString(),"3.0");
    }
    
    @Test
    public void parserTestParenOperations() throws IllegalArgumentException, IOException {
        Expression tree = Expression.parse("(3)*(4)");
        assertEquals(tree.toString(),"3.0*4.0");
    }
    
    @Test
    public void parserTestNestedOperations() throws IllegalArgumentException, IOException {
        Expression tree = Expression.parse("(3+5.0)*7");
        assertEquals(tree.toString(),"(3.0+5.0)*7.0");
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void parserTestUnbalancedParens() throws IllegalArgumentException, IOException {
        Expression tree = Expression.parse("(3");
        
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void parserTestUnbalancedOperations() throws IllegalArgumentException, IOException {
        Expression tree = Expression.parse("5*");
    }
    
    Variable x = new Variable("x");
    Variable y = new Variable("x");
    Variable z = new Variable("z");
    
    @Test
    public void testVariableToString() {
        assertTrue(x.toString() == "x");
    }
    
    @Test
    public void testVariableEquals() {
        assertTrue(x.equals(y));
        assertFalse(x.equals(z));
    }
    
    @Test
    public void testVariableHashCode() {
        assertTrue(x.hashCode() == y.hashCode());
        assertTrue(x.hashCode() != z.hashCode());
    }
    
    @Test
    public void testVariableDifferential() {
        Expression diff = x.differentiate(y);
        assertTrue(diff.equals(new Number(1)));
    }
    
    @Test
    public void testDyDxDifferential() {
        Expression diff = x.differentiate(z);
        assertTrue(diff.equals(new Number(0)));
    }
    
    Number n = new Number(5);
    Number n2 = new Number(5.0);
    
    @Test
    public void testNumberToString() {
        assertEquals(n2.toString(),"5.0");
        assertEquals(n.toString(),n2.toString());
    }
    
    @Test
    public void testNumberHashCode() {
        assertEquals(n.hashCode(),n2.hashCode());
    }
    
    @Test
    public void testNumberEquals() {
        assertTrue(n.equals(n2));
    }
    
    @Test
    public void testNumberDifferential() {
        Variable x = new Variable("x");
        Expression diff = n.differentiate(x);
        assertTrue(diff.equals(new Number(0)));
    }
    
    Plus op = new Plus(n, n2);
    Plus op2 = new Plus(n, n2);
    
    @Test
    public void testPlusToString() {
        assertEquals(op.toString(),"(5.0+5.0)");
    }

    @Test
    public void testPlusHashCode() {
        assertEquals(op.hashCode(), op2.hashCode());
    }
    
    @Test
    public void testPlusEquals() {
        assertTrue(op.equals(op2));
    }
    
    @Test
    public void testPlusDifferential() {
        Variable x = new Variable("x");
        Plus sum = new Plus(x, new Number(8));
        Plus diff = (Plus) sum.differentiate(x);
        assertEquals(diff.getLeft(), new Number(1));
        assertEquals(diff.getRight(),new Number(0));
    }
    
    @Test
    public void testPlusDifferentialTwoVars() {
        Variable x = new Variable("x");
        Plus sum = new Plus(x, x);
        Plus diff = (Plus) sum.differentiate(x);
        assertEquals(diff.getLeft(), new Number(1));
        assertEquals(diff.getRight(),new Number(1));
    }
    
    @Test
    public void testPlusDifferentialXandY() {
        Variable x = new Variable("x");
        Variable y = new Variable("y");
        Plus sum = new Plus(x, y);
        Plus diff = (Plus) sum.differentiate(x);
        assertEquals(diff.getLeft(), new Number(1));
        assertEquals(diff.getRight(),new Number(0));
    }
    
    Times op3 = new Times(n,n2);
    Times op4 = new Times(n,n2);
    
    @Test
    public void testTimesToString() {
        assertEquals(op3.toString(),"5.0*5.0");
    }

    @Test
    public void testTimesHashCode() {
        assertEquals(op3.hashCode(), op4.hashCode());
    }
    
    @Test
    public void testTimesEquals() {
        assertTrue(op3.equals(op4));
    }
    
    @Test 
    public void testSumVarNumber() throws IOException {
        Variable x = new Variable("x");
        Plus y = new Plus(new Variable("x"),x);
        assertEquals(y.getLeft(),y.getRight());
        Expression expr = Expression.parse("x+9");
        assertEquals(expr.differentiate(x).toString(),"(1.0+0.0)");
    }
    
    @Test
    public void testTimesDifferential() {
        Variable x = new Variable("x");
        Expression expr = new Times(x,x);
        Expression diff = expr.differentiate(x);
        Times halfAnswer = new Times(x, new Number(1));
        Plus fullAnswer = new Plus(halfAnswer, halfAnswer);
        assertEquals(diff.toString(),fullAnswer.toString());
    }
    
    @Test
    public void testTimesNumAndVarDifferential() {
        Variable x = new Variable("x");
        Expression expr = new Times(new Number(8), x);
        Expression diff = expr.differentiate(x);
        assertEquals(diff.toString(),"(8.0*1.0+x*0.0)");
    }
    
    //Tests simplify()
    
    @Test
    public void testSimplifyVarSubsitution () throws IllegalArgumentException, IOException {
        Map<String, Double> environment = new HashMap<String, Double>();
        environment.put(("x"),1.0);
        Expression expression = Expression.parse("x");
        assertEquals(expression.simplify(environment),new Number(1.0));
    }
    
    @Test
    public void testSimplifyIntegerExpression () throws IllegalArgumentException, IOException {
        Map<String, Double> environment = new HashMap<String, Double>();
        Expression expression = Expression.parse("1.0+1.0"); 
        assertEquals(expression.simplify(environment).toString(), "2.0");
    }
    
    @Test
    public void testSimplifyOneVar () throws IllegalArgumentException, IOException {
        Map<String, Double> environment = new HashMap<String, Double>();
        environment.put("x", 10.0);
        
        Expression expression = Expression.parse("x+5");
        assertEquals(expression.simplify(environment), new Number(15.0));
        
        Expression expression2 = Expression.parse("x*10.0");
        assertEquals(expression2.simplify(environment), new Number(100.0));
    }
    
    @Test
    public void testSimplifyTwoVar () throws IllegalArgumentException, IOException {
        Map<String, Double> environment = new HashMap<String, Double>();
        environment.put(("x"), 10.0);
        environment.put(("y"), 20.0);
        Expression expression = Expression.parse("y+6+x");
        Expression expression2 = Expression.parse("2*x+y");
        assertEquals(expression.simplify(environment), new Number(36.0));
        assertEquals(expression2.simplify(environment), new Number(40.0));
    }
    
    @Test
    public void testSimplifyVarNotInEnvironment() throws IllegalArgumentException, IOException {
        Map<String, Double> environment = new HashMap<String, Double>();
        environment.put(("x"), 3.0);
        Expression expression = Expression.parse("y + 8.0");
        Expression expression2 = Expression.parse("y + 8.0 + x");
        assertEquals(expression.simplify(environment).toString(), "(y+8.0)");
        assertEquals(expression2.simplify(environment).toString(), "((y+8.0)+3.0)");
    }
    
    @Test
    public void testSimplifyMultipleOfSameVar () throws IllegalArgumentException, IOException {
        Map<String, Double> environment = new HashMap<String, Double>();
        environment.put(("x"), 5.0);
        Expression expression = Expression.parse("(x+5)*(5+x)");
        Expression expression2 = Expression.parse("(x*5)+(5*x)");
        assertEquals(expression.simplify(environment),new Number(100.0));
        assertEquals(expression2.simplify(environment),new Number(50.0));
    }
    
}
