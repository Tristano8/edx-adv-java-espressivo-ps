package expressivo;

import java.util.Map;

/**
 * An immutable data type representing a nonnegative integer or floating-point number
 *   
 */
public class Number implements Expression {
    
    // Rep: A non-negative number or floating-point number
    private final double n;
    
    // Abstraction Function: Represents a floating point number n, such that n >= 0.0
    //                       
    // Rep Invariant: n >= 0
    //
    // Safety from rep exposure argument: n field is final, no mutator methods               
    
    public Number(double n) {
        this.n = n;
        checkRep();
    }
    
    private void checkRep() {
        assert this.n >=0;
    }
    
    /**
     * @param a variable which this Expression object will be differentiated with respect to
     * @return a new differentiated Expression object
     */
    public Expression differentiate(Variable x) {
        checkRep();
        return new Number(0);
    }
    
    /**
     * Simplify an expression.  
     * @param environment A mapping of Variables to numeric (double) values.
     * @return A new Expression object containing the simplified expression. Constant expressions will be reduced to a single number, with
     * no remaining operators. Variable expressions will remain in their current form unless the
     * environment allows for substitution.
     */
    public Expression simplify(Map<String,Double> environemnt) {
        checkRep();
        return this;
    }
    

    /**
     * @return a parsable representation of this expression, such that
     * for all e:Expression, e.equals(Expression.parse(e.toString())).
     */
    @Override 
    public String toString() {
        checkRep();
        return String.valueOf(this.n); 
    }

    /**
     * @param thatObject any object
     * @return true if and only if this and thatObject are structurally-equal
     * Expressions, as defined in the PS1 handout.
     */
    @Override
    public boolean equals(Object thatObject) {
        checkRep();
        if (!(thatObject instanceof Number)) return false;
        Number thatVar = (Number) thatObject;
        return this.n == thatVar.n;
    }
    
    /**
     * @return hash code value consistent with the equals() definition of structural
     * equality, such that for all e1,e2:Expression,
     *     e1.equals(e2) implies e1.hashCode() == e2.hashCode()
     */
    @Override
    public int hashCode() {
        checkRep();
        return Double.hashCode(this.n);
    }
    
    /**
     * @return True if the current expression object contains an integer value.
     */
    public boolean hasValue() {
        checkRep();
        return true;
    }
    
    /**
     * @return The numeric (double) value of this expression object
     */
    public double getValue() {
        checkRep();
        return this.n;
    }
    

}
