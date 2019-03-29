// This class is a scanner for the program
// and programming language being interpreted.

import java.util.*;

/**
 * This class is a scanner for the program
 * and programming language being interpreted.
 * @author xieyi
 *
 */
public class Scanner {

    private String program;	// source program being interpreted
    private int pos;		// index of next char in program
    private Token token;	// last/current scanned token

    // sets of various characters and lexemes
    private Set<String> whitespace=new HashSet<String>();
    private Set<String> digits=new HashSet<String>();
    private Set<String> letters=new HashSet<String>();
    private Set<String> legits=new HashSet<String>();
    private Set<String> keywords=new HashSet<String>();
    private Set<String> operators=new HashSet<String>();
    private Set<String> comments = new HashSet<>();

    // initializers for previous sets

    /**
     * help method for initializing letters and digits
     * @param s
     * @param lo
     * @param hi
     */
    private void fill(Set<String> s, char lo, char hi) {
	for (char c=lo; c<=hi; c++)
	    s.add(c+"");
    }    

    /**
     * Initialize the whitespace
     * @param s
     */
    private void initWhitespace(Set<String> s) {
		s.add(" ");
		s.add("\n");
		s.add("\t");
    }

    /**
     * Initialize numbers including dot
     * @param s
     */
    private void initDigits(Set<String> s) {
		fill(s,'0','9');
		s.add(".");
    }

    /**
     * Initialize letters
     * @param s
     */
    private void initLetters(Set<String> s) {
		fill(s,'A','Z');
		fill(s,'a','z');
    }

    /**
     * Initialize legits
     * @param s
     */
    private void initLegits(Set<String> s) {
		s.addAll(letters);
		s.addAll(digits);
    }

    /**
     * Initialize all operators
     * @param s
     */
    private void initOperators(Set<String> s) {
		s.add("=");
		s.add("+");
		s.add("-");
		s.add("*");
		s.add("/");
		s.add("(");
		s.add(")");
		s.add(";");
		s.add("<");
		s.add("<=");
		s.add(">");
		s.add(">=");
		s.add("<>");
		s.add("==");
    }

    
    /**
     * Initialize comments
     * @param s
     */
    private void initComments(Set<String> s){
        s.add("#");
    }

    /**
     * Initialize keywords
     * @param s
     */
    private void initKeywords(Set<String> s) {
    	s.add("rd");
    	s.add("wr");
    	s.add("if");
    	s.add("then");
    	s.add("else");
    	s.add("while");
    	s.add("do");
    	s.add("begin");
    	s.add("end");
    }

    /**
     * constructor:- squirrel-away source program
     * - initialize sets
     * @param program
     */
    public Scanner(String program) {
		this.program=program;
		pos=0;
		token=null;
		initWhitespace(whitespace);
		initDigits(digits);
		initLetters(letters);
		initLegits(legits);
		initKeywords(keywords);
		initOperators(operators);
        initComments(comments);
    }

    // handy string-processing methods

    /**
     * To check if all characters of the string processed
     * @return
     */
    public boolean done() {
        return pos>=program.length();
    }

    /**
     * process keywords or numbers
     * @param s
     */
    private void many(Set<String> s) {
		while (!done() && s.contains(program.charAt(pos)+""))
			pos++;
    }
    
    private void past(char c) {
		while (!done() && c!=program.charAt(pos))
			pos++;
		if (!done() && c==program.charAt(pos))
			pos++;
    }

    // scan various kinds of lexeme

    /**
     * Scan numbers
     * @throws SyntaxException
     */
    private void nextNumber() throws SyntaxException {
		int old=pos;
        rmComments(comments);
		many(digits);
		if(checkPoints(old,pos)){
			token=new Token("num",program.substring(old,pos));
		}
		else
			throw new SyntaxException(pos,new Token("too many"),new Token("."));
    }

    /**
     * Check if there is more than one points for double number
     * @param begin
     * @param end
     * @return
     */
    private boolean checkPoints(int begin, int end){
		String numString = program.substring(begin,end);
		int counter = 0;
		for(int i=0; i <numString.length();i++){
			if(numString.charAt(i) =='.'){
				counter++;
			}
		}
		if(counter > 1){
			return false;
		}
		else
			return true;
	}

    /**
     * Scan keywords 
     */
    private void nextKwId() {
		int old=pos;
        rmComments(comments);
		many(letters);
		many(legits);
		String lexeme=program.substring(old,pos);
		token=new Token((keywords.contains(lexeme) ? lexeme : "id"),lexeme);
    }

    /**
     * Scan Operators 
     */
    private void nextOp() {
		int old=pos;
        rmComments(comments);
		pos=old+2;
		if (!done()) {
			String lexeme=program.substring(old,pos);
			if (operators.contains(lexeme)) {
			token=new Token(lexeme); // two-char operator
			return;
			}
		}
		pos=old+1;
		String lexeme=program.substring(old,pos);
		token=new Token(lexeme); // one-char operator
    }

    /**
     * his method determines the kind of the next token (e.g., "id"),
     * and calls a method to scan that token's lexeme 
     * @return
     * @throws SyntaxException
     */
    public boolean next() throws SyntaxException {
	if (done())
	    return false;
	many(whitespace);
    rmComments(comments);
    if(done())
        return false;
    many(whitespace);
	String c=program.charAt(pos)+"";
	if (digits.contains(c))
	    nextNumber();
	else if (letters.contains(c))
	    nextKwId();
	else if (operators.contains(c))
	    nextOp();
	else {
	    System.err.println("illegal character at position "+pos);
	    pos++;
	    return next();
	}
	return true;
    }

    /**
     * Skip all the comments
     * comments type:  #...#
     * @param s
     */
    private void rmComments(Set<String> s){
        if(!done() && s.contains(program.charAt(pos) + "")){
            do {
                pos++;
                if(s.contains(program.charAt(pos) + "")){
                    break;
                }
            } while(!done());
            pos++;
        }
    }

    /**
     * This method scans the next lexeme,
     * if the current token is the expected token.
     * @param t
     * @throws SyntaxException
     */
    public void match(Token t) throws SyntaxException {
	if (!t.equals(curr()))
	    throw new SyntaxException(pos,t,curr());
	next();
    }

    /**
     * Return current token
     * @return
     * @throws SyntaxException
     */
    public Token curr() throws SyntaxException {
	if (token==null)
	    throw new SyntaxException(pos,new Token("ANY"),new Token("EMPTY"));
	return token;
    }

    /**
     * Return current scanner position
     * @return
     */
    public int pos() { return pos; }

    /* for unit testing
    public static void main(String[] args) {
	try {
	    Scanner scanner=new Scanner(args[0]);
	    while (scanner.next())
		System.out.println(scanner.curr());
	} catch (SyntaxException e) {
	    System.err.println(e);
	}
    }
     */
}
