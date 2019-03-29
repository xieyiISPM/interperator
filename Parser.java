// 



/**
 * This class is a recursive-descent parser,modeled after the programming language's grammar.
 * It constructs and has-a Scanner for the program being parsed.
 * @author xieyi
 *
 */
public class Parser {

    private Scanner scanner;

    /**
     * Check if the string matches keywords passed in
     * @param s
     * @throws SyntaxException
     */
    private void match(String s) throws SyntaxException {
	    scanner.match(new Token(s));
    }

    /**
     * return current token
     * @return
     * @throws SyntaxException
     */
    private Token curr() throws SyntaxException {
        return scanner.curr();
    }

    /**
     * return the scanner position
     * @return
     */
    private int pos() {
	return scanner.pos();
    }

    /**
     * Return multiple results
     * @return
     * @throws SyntaxException
     */
    private NodeMulop parseMulop() throws SyntaxException {
        if (curr().equals(new Token("*"))) {
            match("*");
            return new NodeMulop(pos(),"*");
        }
        if (curr().equals(new Token("/"))) {
            match("/");
            return new NodeMulop(pos(),"/");
        }
        return null;
    }
    
    /**
     * return boolean result
     * @return
     * @throws SyntaxException
     */
    private NodeRelop parseRelop() throws SyntaxException {
    	if(curr().equals(new Token ("<"))){
    		match("<");
    		return new NodeRelop(pos(),"<");
    	}
    	if(curr().equals(new Token ("<="))){
    		match("<=");
    		return new NodeRelop(pos(),"<=");
    	}
    	if(curr().equals(new Token (">"))){
    		match(">");
    		return new NodeRelop(pos(),">");
    	}
    	if(curr().equals(new Token (">="))){
    		match(">");
    		return new NodeRelop(pos(),">=");
    	}
    	if(curr().equals(new Token ("<>"))){
    		match("<>");
    		return new NodeRelop(pos(),"<>");
    	}
    	if(curr().equals(new Token ("=="))){
    		match("==");
    		return new NodeRelop(pos(),"==");
    	}
    	return null;
    }

    /**
     * Return add/minus results
     * @return
     * @throws SyntaxException
     */
    private NodeAddop parseAddop() throws SyntaxException {
        if (curr().equals(new Token("+"))) {
            match("+");
            return new NodeAddop(pos(),"+");
        }
        if (curr().equals(new Token("-"))) {
            match("-");
            return new NodeAddop(pos(),"-");
        }
        return null;
        }

    /**
     * Parse the Fact and create Fact node
     * @return
     * @throws SyntaxException
     */
    private NodeFact parseFact() throws SyntaxException {
        if (curr().equals(new Token("("))) {
            match("(");
            NodeExpr expr=parseExpr();
            match(")");
            return new NodeFactExpr(expr);
        }
        if (curr().equals(new Token("id"))) {
            Token id=curr();
            match("id");
            return new NodeFactId(pos(),id.lex());
        }
        if(curr().equals(new Token("-"))){
            match("-");
            Token negNum = curr();
            match("num");
            return new NodeFactNum("-" + negNum.lex());
        }

        Token num=curr();
        match("num");
        return new NodeFactNum(num.lex());
    }

    /**
     * Parse the Term and create term node
     * @return
     * @throws SyntaxException
     */
    private NodeTerm parseTerm() throws SyntaxException {
        NodeFact fact=parseFact();
        NodeMulop mulop=parseMulop();
        if (mulop==null)
            return new NodeTerm(fact,null,null);
        NodeTerm term=parseTerm();
        term.append(new NodeTerm(fact,mulop,null));
        return term;
    }

    /**
     * Parse Expression and return expression node
     * @return
     * @throws SyntaxException
     */
    private NodeExpr parseExpr() throws SyntaxException {
        NodeTerm term=parseTerm();
        NodeAddop addop=parseAddop();
        if (addop==null)
            return new NodeExpr(term,null,null);
        NodeExpr expr=parseExpr();
        expr.append(new NodeExpr(term,addop,null));
        return expr;
    }

    /**
     * Parsing assignment and return assignment node
     * @return
     * @throws SyntaxException
     */
    private NodeAssn parseAssn() throws SyntaxException {
        Token id=curr();
        match("id");
        match("=");
        NodeExpr expr=parseExpr();
        NodeAssn assn=new NodeAssn(id.lex(),expr);
        return assn;
    }
    
    /**
     * Parsing boolean expression and return boolean expression node
     * @return
     * @throws SyntaxException
     */
    private NodeBoolExpr parseBoolExpr() throws SyntaxException {
    	NodeExpr expr0 = parseExpr();
    	NodeRelop relop = parseRelop();
    	NodeExpr expr1 = parseExpr();
    	return new NodeBoolExpr(expr0, relop, expr1);
    }

    /**
     * Parsing read and return read node
     * @return
     * @throws SyntaxException
     */
    private NodeRd parseRd() throws SyntaxException {
    	match("rd");
    	Token id = curr();
    	match("id");
    	return new NodeRd(id.lex());
    }

    /**
     * Parsing write and return write node
     * @return
     * @throws SyntaxException
     */
    private NodeWr parseWr() throws SyntaxException {
        match("wr");
        NodeExpr expr = parseExpr();
        return new NodeWr(expr);
    }
    
    /**
     * Parsing if.. then.. else..  statement return if.. then.. else.. node
     * @return
     * @throws SyntaxException
     */
    private NodeIfElse parseIfElse() throws SyntaxException{
    	match("if");
    	NodeBoolExpr boolExpr = parseBoolExpr();
    	match("then");
    	NodeStmt thenStmt = parseStmt();
    	if(curr().equals(new Token("else"))) {
    		match("else");
    		NodeStmt elseStmt = parseStmt();
    		return new NodeIfElse(boolExpr, thenStmt, elseStmt);
    	}
    	else {
    		return new NodeIfElse(boolExpr, thenStmt, null);
    	}
    }
    
    /**
     * Parsing while do loop and return while..do .. node
     * @return
     * @throws SyntaxException
     */
    private NodeWhileLoop parseWhileLoop()throws SyntaxException {
    	match("while");
    	NodeBoolExpr boolExpr = parseBoolExpr();
    	match("do");
    	NodeStmt stmt = parseStmt();
    	return new NodeWhileLoop(boolExpr, stmt);
    }
    
    /**
     * Parsing being.. end and return begin..end node
     * @return
     * @throws SyntaxException
     */
    private NodeBegin parseBegin() throws SyntaxException {
    	match("begin");
    	NodeBlock block =parseBlock();
    	match("end");
    	return new NodeBegin(block);
    }

    /**
     * Parsing statement and return the statement node;
     * @return
     * @throws SyntaxException
     */
    private NodeStmt parseStmt() throws SyntaxException {

        if(curr().equals(new Token("rd"))) {
            NodeRd rd = parseRd();
            return new NodeStmtRd(rd);
        }
       
        else  if(curr().equals(new Token("wr"))){
        	NodeWr wr = parseWr();
            return new NodeStmtWr(wr);
        }
        
        else if(curr().equals(new Token("while"))){
        	NodeWhileLoop whileLoop = parseWhileLoop();
        	return new NodeStmtWhileLoop(whileLoop);
        }
        
        else if(curr().equals(new Token("if"))){
        	NodeIfElse ifElse = parseIfElse();
        	return new NodeStmtIfElse(ifElse);
        }
        
        else if(curr().equals(new Token("begin"))){
        	NodeBegin begin = parseBegin();
        	return new NodeStmtBegin(begin);
        }
        else {
        NodeAssn assn = parseAssn();
        return new NodeStmt(assn);
        }
    }

    /**
     * Parsing Block and return block node
     * @return
     * @throws SyntaxException
     */
    private NodeBlock parseBlock() throws SyntaxException {
        NodeStmt stmt = parseStmt();
        NodeBlock block = null;
        if(curr().equals(new Token(";"))){
            match(";");
            block = parseBlock(); 
        }
        block = new NodeBlock(stmt, block);
        return block;
    }

    /**
     * Parsing the program and return program node
     * @return
     * @throws SyntaxException
     */
    private NodeProg parseProg() throws SyntaxException {
        NodeBlock block = parseBlock();
        NodeProg prog = new NodeProg(block);
        return prog;
    }

    /**
     * Starting building Parsing tree
     * @param program
     * @return
     * @throws SyntaxException
     */
    public Node parse(String program) throws SyntaxException {
        scanner=new Scanner(program);
        scanner.next();
        return parseProg();
    }

}
