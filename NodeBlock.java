public class NodeBlock extends Node {

    private NodeStmt stmt;
    private NodeBlock block;

    public NodeBlock(NodeStmt stmt, NodeBlock block) {
	    this.stmt = stmt;
        this.block=block;
    }
    
    public double eval(Environment env) throws EvalException {
	    double temp = stmt.eval(env);
    	return block==null? temp: block.eval(env);
    }

}
