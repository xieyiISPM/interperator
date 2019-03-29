
public class NodeStmtIfElse extends NodeStmt {
	private NodeIfElse ifElse;
	
	public NodeStmtIfElse(NodeIfElse ifElse) {
		this.ifElse = ifElse;
	}
	
	public double eval(Environment env) throws EvalException {
		return ifElse.eval(env);
	}
}
