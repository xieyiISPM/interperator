import java.util.Scanner;

public class NodeRd extends Node {
	private String id;
	private static Scanner sc;
	public NodeRd(String id){
		this.id = id;
	}
	
	public double eval(Environment env) throws EvalException {
		if (sc == null) sc = new Scanner(System.in);
		System.out.println("please input a number:");
		double inputVal = sc.nextDouble();
		env.put(id, inputVal);
		return inputVal;
	}


}
