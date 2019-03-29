// This class provides a stubbed-out environment.
// You are expected to implement the methods.
// Accessing an undefined variable should throw an exception.
import java.util.HashMap;

/**
 * Environment class stores the variables used by the interpreter.
 * @author xieyi
 *
 */
public class Environment {
    private HashMap<String, Double> env;

    /**
     * Constructor
     */
    public Environment(){
        this.env = new HashMap<String, Double>();
    }

    /**
     * Store the variable string and double value to the hash map
     * @param var
     * @param val
     * @return
     */
    public double put(String var, double val) {
        this.env.put(var,val);
        return val;
    }

    /**
     * Get the variable string from the hash map and return the value
     * @param pos
     * @param var
     * @return
     * @throws EvalException
     */
    public double get(int pos, String var) throws EvalException {
        if(env.containsKey(var))
            new EvalException(pos, "value is missing");
        return env.get(var);
    }
}
