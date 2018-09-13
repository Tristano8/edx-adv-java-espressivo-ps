package expressivo;

import java.util.Map;

/**
 * An immutable data type representing a variable (case-sensitive nonempty strings of letters.
 * 
 */
public class Variable implements Expression {
    
    // Rep: A variable used in mathematical expressions
    private final String name;
    
    // Abstraction Function: Represents a variable, eg. x  in  'x + 3'
    //                       
    // Rep Invariant: Variables are case sensitive, non-empty and do not contain numeric chars.
    //
    // Safety from rep exposure argument: String field is final, no mutator methods               
    
    public Variable(String name) {
        this.name = name;
        checkRep();
    }
    
    private void checkRep() {
        assert this.name != null;
    }
    
    /**
     * @param a variable which this Expression object will be differentiated with respect to
     * @return a new differentiated Expression object
     */
    public Expression differentiate(Variable v) {
        checkRep();
        if (v.name.equals(this.name)) {
            return new Number(1);
        } else return new Number(0);
    }
    
    /**
     * Simplify an expression.  
     * @param environment A mapping of Variables to numeric (double) values.
     * @return A new Expression object containing the simplified expression. If variable matches
     * a mapping in environemtn, returns a new Number object containing the mapped value.
     */
    public Expression simplify(Map<String,Double> environment) {
        checkRep();
        if (environment.containsKey(this.name)) {
            return new Number(environment.get(this.name));
            }
        else 
            return this;
    }
    
    /**
     * @return a parsable representation of this expression, such that
     * for all e:Expression, e.equals(Expression.parse(e.toString())).
     */
    @Override 
    public String toString() {
        checkRep();
        return this.name; 
    }

    /**
     * @param thatObject any object
     * @return true if and only if this and thatObject are structurally-equal
     * Expressions, as defined in the PS1 handout.
     */
    @Override
    public boolean equals(Object thatObject) {
        checkRep();
        if (!(thatObject instanceof Variable)) return false;
        Variable thatVar = (Variable) thatObject;
        return this.name.equals(thatVar.name);
    }
    
    /**
     * @return hash code value consistent with the equals() definition of structural
     * equality, such that for all e1,e2:Expression,
     *     e1.equals(e2) implies e1.hashCode() == e2.hashCode()
     */
    @Override
    public int hashCode() {
        checkRep();
        return this.name.hashCode();
    }
    
    public double getValue() {
        checkRep();
        throw new UnsupportedOperationException("Code should not reach here");
    }
    
    /**
     * @return True if the current expression object contains an integer value.
     */
    public boolean hasValue() {
        checkRep();
        return false;
    }
    

}
