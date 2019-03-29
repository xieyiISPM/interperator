// This class, and its subclasses,
// .
// Each kind of node can be eval()-uated.

/**
 * Collectively model parse-tree nodes
 * @author professor
 *
 */
public abstract class Node {

    protected int pos=0;

    public double eval(Environment env) throws EvalException {
	throw new EvalException(pos,"cannot eval() node!");
    }

}
