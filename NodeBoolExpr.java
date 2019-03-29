
public class NodeBoolExpr extends Node{
	private NodeExpr expr0, expr1;
	private NodeRelop relop;
	
	public NodeBoolExpr(NodeExpr expr0, NodeRelop relop, NodeExpr expr1) {
		this.expr0 = expr0;
		this.expr1 = expr1;
		this.relop = relop;
	}
	
	public double eval(Environment evn) throws EvalException {
		return relop.op(expr0.eval(evn), expr1.eval(evn));
	}

}
