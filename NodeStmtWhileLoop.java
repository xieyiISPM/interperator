
public class NodeStmtWhileLoop extends NodeStmt {
	NodeWhileLoop whileLoop;
	
	public NodeStmtWhileLoop(NodeWhileLoop whileLoop){
		this.whileLoop = whileLoop;
	}
	
	public double eval(Environment env) throws EvalException {
		return whileLoop.eval(env);
	}
}
