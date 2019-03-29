
public class NodeStmtWr extends NodeStmt {
	NodeWr wr;
	public NodeStmtWr(NodeWr wr) {
		this.wr = wr;
	}
	
	public double eval(Environment env) throws EvalException {
		return wr.eval(env);
	}
}
