
public class NodeWhileLoop extends Node {
	private NodeBoolExpr boolExpr;
	private NodeStmt stmt;
	
	public NodeWhileLoop(NodeBoolExpr boolExpr, NodeStmt stmt){
		this.boolExpr = boolExpr;
		this.stmt = stmt;
	}
	
	public double eval(Environment env) throws EvalException{
		while(boolExpr.eval(env) == 1){
			stmt.eval(env);
		}
		return 0;
	}

}
