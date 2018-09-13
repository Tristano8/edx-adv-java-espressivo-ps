package expressivo;

import java.util.Map;

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
public class Plus implements Expression {
    
    // Rep: An addition operator expression
    private final String plusOp = "+";
    private final Expression left;
    private final Expression right;
    
    // Abstraction Function: Represents a mathematical operator expression of the form x + 3
    //                       where x is the left expression branch, 3 is the right branch
    //                       
    // Rep Invariant: 
    //
    // Safety from rep exposure argument: Datatype is immutable                
    
    public Plus(Expression left, Expression right) {
        this.left = left;
        this.right = right;
        checkRep();
    }
    
    private void checkRep() {
        assert this.left != null;
        assert this.right != null;
    }
    
    /**
     * @param a variable which this Expression object will be differentiated with respect to
     * @return a new differentiated Expression object
     */
    public Expression differentiate(Variable v) {
        checkRep();
        Expression leftDifferential = this.left.differentiate(v);
        Expression rightDifferential = this.right.differentiate(v);
        return new Plus(leftDifferential, rightDifferential);
    }
    
    /**
     * Simplify an expression.  
     * @param environment A mapping of Variables to numeric (double) values.
     * @return A new Expression object containing the simplified expression. Constant expressions will be reduced to a single number, with
     * no remaining operators. Variable expressions will remain in their current form unless the
     * environment allows for substitution.
     */
    public Expression simplify(Map<String,Double> environment) {
        checkRep();
        Expression leftExpression = this.left.simplify(environment);
        Expression rightExpression = this.right.simplify(environment);
        return combineTerms(leftExpression, rightExpression);
        
    }

    /**
     * @return the left factor of this expression.
     */
    public Expression getLeft() {
        checkRep();
        return this.left;
    }
    
    /**
     * @return the right factor of this expression.
     */
    public Expression getRight() {
        checkRep();
        return this.right;
    }
    
    /**
     * @return a parsable representation of this expression, such that
     * for all e:Expression, e.equals(Expression.parse(e.toString())).
     */
    @Override 
    public String toString() {
        checkRep();
        return "(" + this.left.toString() + plusOp + this.right.toString() + ")"; 
    }

    /**
     * @param thatObject any object
     * @return true if and only if this and thatObject are structurally-equal
     * Expressions, as defined in the PS1 handout.
     */
    @Override
    public boolean equals(Object thatObject) {
        checkRep();
        if (!(thatObject instanceof Plus)) return false;
        Plus thatExp = (Plus) thatObject; 
            return (this.left.equals(thatExp.left)) && (this.right.equals(thatExp.right));
    }
    
    /**
     * @return hash code value consistent with the equals() definition of structural
     * equality, such that for all e1,e2:Expression,
     *     e1.equals(e2) implies e1.hashCode() == e2.hashCode()
     */
    @Override
    public int hashCode() {
        checkRep();
        return left.hashCode() + right.hashCode();
    }
    
    /**
     * @param exp1 First expression to be added
     * @param exp2 Second expression to be added
     * @return Number object with sum of first and second expression if both have values, otherwise returns a Plus
     * object containing both expressions.
     */
    public Expression combineTerms(Expression exp1, Expression exp2) {
        checkRep();
        if (exp1.hasValue() && exp2.hasValue()) {
            return new Number(exp1.getValue() + exp2.getValue());
        } else return new Plus(exp1, exp2);
    }
    
    public double getValue() {
        checkRep();
        throw new UnsupportedOperationException("Code should never reach here");
    }
    
    /**
     * @return True if the current expression object contains an integer value.
     */
    public boolean hasValue() {
        checkRep();
        return this.left.hasValue() && this.right.hasValue();
    }
    

}
