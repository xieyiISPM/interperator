public class NodeStmtRd extends NodeStmt {
	String id;
	NodeRd rd;
	
	public NodeStmtRd(NodeRd rd) {
		super();
		this.rd = rd;
	}
	
	 public double eval(Environment env) throws EvalException {
	    	return rd.eval(env);
	    }
}
