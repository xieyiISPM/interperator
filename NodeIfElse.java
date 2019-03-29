
public class NodeIfElse extends Node {
	private NodeBoolExpr boolExpr;
	private NodeStmt thenStmt;
	private NodeStmt elseStmt;
	
	public NodeIfElse(NodeBoolExpr boolExpr, NodeStmt thenStmt, NodeStmt elseStmt) {
		this.boolExpr = boolExpr;
		this.thenStmt = thenStmt;
		this.elseStmt = elseStmt;
	}
	
	
	public double eval(Environment env) throws EvalException {
		return boolExpr.eval(env) == 1? thenStmt.eval(env) :
			elseStmt ==null? 0: elseStmt.eval(env);
	}

}
