package expressivo;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import lib6005.parser.*;

/**
 * An immutable data type representing a polynomial expression of:
 *   + and *
 *   nonnegative integers and floating-point numbers
 *   variables (case-sensitive nonempty strings of letters)
 * 
 * <p>PS1 instructions: this is a required ADT interface.
 * You MUST NOT change its name or package or the names or type signatures of existing methods.
 * You may, however, add additional methods, or strengthen the specs of existing methods.
 * Declare concrete variants of Expression in their own Java source files.
 */
public interface Expression {
    
    // Datatype definition
    //
    // Expression = Number(n: Float)
    //                     + Variable(name: String)
    //                     + Plus(left: Expression, right: Expression)
    //                     + Times(left: Expression, right: Expression)
    //
    // Abstraction Function: Represents a polynomial expession of the form x * 3 + 7.8 where
    //                       x is a string Variable object
    //                       3 and 7.8 are Number objects (Int and Float are overloaded)
    //                       *  and +  are Plus and Times objects, respectively
    //
    // Rep invariant: 
    //                
    //
    // Safety from rep exposure argument: Datatype is immutable
    
    public enum ExpressionGrammar {ROOT, FACTOR, 
                                      SUM, TIMES, NUMBER, VARIABLE, WHITESPACE};
    
    /**
     * Function converts a ParseTree to an Expression.
     * @param p
     *  ParseTree<ExpressionGrammar> that is assumed to have been constructed by 
     *  the grammar in Expression.g
     * @return an Abstract Syntax Tree representing the parsed grammar
     */
    public static Expression buildAST(ParseTree<ExpressionGrammar> p) {
        switch(p.getName()) {
        
        case NUMBER:
            /*
             * A number will be a terminal containing a number.
             */
            return new Number(Double.parseDouble((p.getContents())));
        case VARIABLE:
            /*
             * A variable will be a terminal containing a variable.
             */
            return new Variable(p.getContents());
        case FACTOR:
            /*
             * A factor will have either a number, variable or sum as child (in addition to some
             * whitespace)
             */
            if (!p.childrenByName(ExpressionGrammar.NUMBER).isEmpty()) {
                return buildAST(p.childrenByName(ExpressionGrammar.NUMBER).get(0));
            }
            else if(!p.childrenByName(ExpressionGrammar.VARIABLE).isEmpty()) {
                return buildAST(p.childrenByName(ExpressionGrammar.VARIABLE).get(0));
                }
            else return buildAST(p.childrenByName(ExpressionGrammar.SUM).get(0));
        case TIMES:
            /*
             * A times will have one or more children that need to be multiplied together.
             */
            boolean timesFirst = true;
            Expression timesResult = null;
            for(ParseTree<ExpressionGrammar> child : p.childrenByName(ExpressionGrammar.FACTOR)) {
                if(timesFirst){
                    timesResult = buildAST(child);
                    timesFirst = false;
                } else{
                    timesResult = new Times(timesResult, buildAST(child));
                }
            }
            if (timesFirst) {
                throw new RuntimeException("times must have a non whitespace child:" + p); 
            }
            return timesResult;
        case SUM:
            boolean sumFirst = true;
            Expression sumResult = null;
            for(ParseTree<ExpressionGrammar> child : p.childrenByName(ExpressionGrammar.TIMES)) {
                if(sumFirst){
                    sumResult = buildAST(child);
                    sumFirst = false;
                } else{
                    sumResult = new Plus(sumResult, buildAST(child));
                }
            }
            if (sumFirst) {
                throw new RuntimeException("sum must have a non whitespace child:" + p); 
            }
            return sumResult;

        case ROOT:
            /*
             * The root has a single sum child, in addition to potentially some whitespace.
             */
            return buildAST(p.childrenByName(ExpressionGrammar.SUM).get(0));
        case WHITESPACE:
            /*
             * We are always avoiding calling buildAST with whitespace, so the code should
             * never make it here.
             */
            throw new RuntimeException("You should never reach here:" + p);
        }
        throw new RuntimeException("You should never reach here:" + p);
    }
    /**
     * Parse an expression.
     * @param input expression to parse, as defined in the PS1 handout.
     * @return expression AST for the input
     * @throws IllegalArgumentException if the expression is invalid
     * 
     */
    public static Expression parse(String input) throws IllegalArgumentException {
        Parser<ExpressionGrammar> parser;
        try {
            parser = GrammarCompiler.compile(new File("src/expressivo/Expression.g"), ExpressionGrammar.ROOT);
            Expression parsedExpression = buildAST(parser.parse(input));
            return parsedExpression;
        } catch (IllegalArgumentException i) {
            throw new IllegalArgumentException("invalid expression: unable to parse");
        } catch (UnableToParseException e) {
            throw new IllegalArgumentException("invalid expression: unable to parse");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }



    /**
     * Differentiate an expression
     * @param v Variable the expression will differentiate in relation to.
     * @return A new Expression object containing the differentiated expression.
     */
    public Expression differentiate(Variable v);
    
    /**
     * Simplify an expression.  
     * @param environment A mapping of Variables to numeric (double) values.
     * @return A new Expression object containing the simplified expression. Constant expressions will be reduced to a single number, with
     * no remaining operators. Variable expressions will remain in their current form unless the
     * environment allows for substitution.
     */
    public Expression simplify(Map<String,Double> environment);
    
    /**
     * @return a parsable representation of this expression, such that
     * for all e:Expression, e.equals(Expression.parse(e.toString())).
     */
    @Override 
    public String toString();

    /**
     * @param thatObject any object
     * @return true if and only if this and thatObject are structurally-equal
     * Expressions, as defined in the PS1 handout.
     */
    @Override
    public boolean equals(Object thatObject);
    
    /**
     * @return hash code value consistent with the equals() definition of structural
     * equality, such that for all e1,e2:Expression,
     *     e1.equals(e2) implies e1.hashCode() == e2.hashCode()
     */
    @Override
    public int hashCode();
    
    public double getValue();
    
    /**
     * @return True if the current expression object contains an integer value.
     */
    public boolean hasValue();
    
    /* Copyright (c) 2015-2017 MIT 6.005 course staff, all rights reserved.
     * Redistribution of original or derived work requires permission of course staff.
     */
}
