/*
 Harshita Kodali, hkodali
 Amala Chirayil, achirayi
 
 Step4 Version 1.0: IR Generator class file 
 */

import java.util.HashMap;
import java.util.Stack;

public class IRGenerator {
    private IRnode head = null;
    private IRnode root = null;
    
    private HashMap<String, IRnode> label_table = new HashMap<String, IRnode>();
    private Stack<LabelASTnode> conditional_label = new Stack<LabelASTnode> ();
    
    public IRGenerator() {
    }
    
    public IRinterface generateExpr(MicroParser.ExprContext ContextObject, SymbolTable tableObject) {
        IRinterface expr = generateFactor(ContextObject.factor(0), tableObject);
        for (int i = 1; i<ContextObject.factor().size(); i++) {
            
            IRinterface temp = generateFactor(ContextObject.factor(i), tableObject);
            
            SymbolType type = SymbolType.NULL;
            IRopcodeType op;
            
            if (expr.getType() == SymbolType.FLOAT && temp.getType() == SymbolType.FLOAT) type = SymbolType.FLOAT;
            else if (expr.getType() == SymbolType.INT && temp.getType() == SymbolType.INT) type = SymbolType.INT;
            else throw new RuntimeException("ERROR: Operator " + ContextObject.addop(i-1).getText() + " not valid for types " + expr.getType() + " and " + temp.getType());
            
            if (ContextObject.addop(i-1).ADD() != null) {
                if (type == SymbolType.FLOAT) op = IRopcodeType.ADDF;
                else op = IRopcodeType.ADDI;
            } else {
                if (type == SymbolType.FLOAT) op = IRopcodeType.SUBF;
                else op = IRopcodeType.SUBI;
            }
            
            TempASTnode tempnode = new TempASTnode(type, RegFile.getNext());
            addNode(new IRnode(op, expr, temp, tempnode));
            expr = tempnode;
        }
        return expr;
    }
    private IRinterface generateFactor(MicroParser.FactorContext factorContextObject, SymbolTable tableObject) {
        IRinterface expr = generatePostfix(factorContextObject.postfix_expr(0), tableObject);
        for (int i = 1; i<factorContextObject.postfix_expr().size(); i++) {
            IRinterface temp = generatePostfix(factorContextObject.postfix_expr(i), tableObject);
            SymbolType type = SymbolType.NULL;
            IRopcodeType op;
            if (expr.getType() == SymbolType.FLOAT && temp.getType() == SymbolType.FLOAT) type = SymbolType.FLOAT;
            else if (expr.getType() == SymbolType.INT && temp.getType() == SymbolType.INT) type = SymbolType.INT;
            else throw new RuntimeException("ERROR: Operator " + factorContextObject.mulop(i-1).getText() + " not valid for types " + expr.getType() + " and " + temp.getType());
            
            if (factorContextObject.mulop(i-1).MULTIPLY() != null) {
                if (type == SymbolType.FLOAT) op = IRopcodeType.MULTF;
                else op = IRopcodeType.MULTI;
            } else {
                if (type == SymbolType.FLOAT) op = IRopcodeType.DIVF;
                else op = IRopcodeType.DIVI;
            }
            TempASTnode tempnode = new TempASTnode(type, RegFile.getNext());
            addNode(new IRnode(op, expr, temp, tempnode));
            expr = tempnode;
        }
        return expr;
    }
    private IRinterface generatePostfix(MicroParser.Postfix_exprContext ContextObject, SymbolTable tableObject) {
        if (ContextObject.primary() != null) {
            //handle the primary postfix
            if (ContextObject.primary().expr() != null) {
                return generateExpr(ContextObject.primary().expr(), tableObject);
            } else if (ContextObject.primary().id() != null)
            {
                String id = ContextObject.primary().id().getText();
                Symbol s = tableObject.findSymbol(id);
                if (s == null) throw new RuntimeException("ERROR: Symbol " + id + " not found.");
                return new TermASTnode (s.getType(), s.getID());
            } else if (ContextObject.primary().FLOATLITERAL() != null) {
                return new LiteralASTnode(Float.parseFloat(ContextObject.primary().FLOATLITERAL().toString()));
            } else if (ContextObject.primary().INTLITERAL() != null) {
                return new LiteralASTnode(Integer.parseInt(ContextObject.primary().INTLITERAL().toString()));
            } else return null;
        }else {
            throw new RuntimeException("Cannot handle call expressions yet");
        }    }

    
    public void generateAssignment(MicroParser.Assign_stmtContext ContextObject, SymbolTable tableObject) {
        IRinterface tempreg = generateExpr(ContextObject.expr(), tableObject);
        Symbol s = tableObject.findSymbol(ContextObject.id().getText());
        
        if (s == null) throw new RuntimeException("ERROR: Could not find symbol " + ContextObject.id().getText());
        if (s.getType() != tempreg.getType()) throw new RuntimeException("ERROR: type mismatch");
        
        if (tempreg.getASTtype() == ASTtype.TERM) {
            TempASTnode r = new TempASTnode(tempreg.getType(), RegFile.getNext());
            if (tempreg.getType() == SymbolType.FLOAT) addNode(new IRnode(IRopcodeType.STOREF, tempreg, r));
            else if (tempreg.getType() == SymbolType.INT) addNode(new IRnode(IRopcodeType.STOREI, tempreg, r));
            tempreg = r;
        }
        if (tempreg.getType() == SymbolType.FLOAT) addNode(new IRnode(IRopcodeType.STOREF, tempreg, new TermASTnode(s.getType(), s.getID())));
        else if (tempreg.getType() == SymbolType.INT) addNode(new IRnode(IRopcodeType.STOREI, tempreg, new TermASTnode(s.getType(), s.getID())));
        else throw new RuntimeException("ERROR: Assignment not valid for type " + tempreg.getType());
    }
    
    public void generateRead(MicroParser.Read_stmtContext ContextObject, SymbolTable tableObject) {
        for (MicroParser.IdContext id : ContextObject.id_list().id()) {
            Symbol s = tableObject.findSymbol(id.getText());
            if (s == null) throw new RuntimeException("ERROR: Symbol " + id + " not found.");
            if (s.getType() == SymbolType.FLOAT) addNode(new IRnode(IRopcodeType.READF, new TermASTnode(s.getType(), s.getID())));
            else if (s.getType() == SymbolType.INT) addNode(new IRnode(IRopcodeType.READI, new TermASTnode(s.getType(), s.getID())));
            else throw new RuntimeException("ERROR: Read statement not valid for type " + s.getType());
        }
    }
    public void generateWrite(MicroParser.Write_stmtContext ContextObject, SymbolTable tableObject) {
        for (MicroParser.IdContext id : ContextObject.id_list().id()) {
            Symbol s = tableObject.findSymbol(id.getText());
            if (s == null) throw new RuntimeException("ERROR: Symbol " + id + " not found.");
            if (s.getType() == SymbolType.FLOAT) addNode(new IRnode(IRopcodeType.WRITEF, new TermASTnode(s.getType(), s.getID())));
            else if (s.getType() == SymbolType.INT) addNode(new IRnode(IRopcodeType.WRITEI, new TermASTnode(s.getType(), s.getID())));
            else if (s.getType() == SymbolType.STRING) addNode(new IRnode(IRopcodeType.WRITES, new TermASTnode(s.getType(), s.getID())));
            else throw new RuntimeException("ERROR: Write statement not valid for type " + s.getType());
        }
    }
    
    public void generateIf(MicroParser.If_stmtContext ContextObject, SymbolTable tableObject) {
        generateIf(ContextObject.cond(), tableObject);
    }
    
    private void generateIf(MicroParser.CondContext conditionalObject, SymbolTable tableObject) {
        IRinterface conditionalExpr1 = generateExpr(conditionalObject.expr(0), tableObject);
        IRinterface conditionalExpr2 = generateExpr(conditionalObject.expr(1), tableObject);
        IRopcodeType comp = Compare.invert(Compare.generateComparisonOp(conditionalObject.compop()));
        //Generate the inverse comparison.  The code will skip over any statements and jump to another label.
        //Move expression 2 to a register if it is a variable, since tiny cannot deal with 2nd argument non-registers/literals
        if (conditionalExpr2.getASTtype() == ASTtype.TERM || conditionalExpr2.getASTtype() == ASTtype.LITERAL) {
            TempASTnode r = new TempASTnode(conditionalExpr2.getType(), RegFile.getNext());
            if (conditionalExpr1.getType() == SymbolType.FLOAT)
                addNode(new IRnode(IRopcodeType.STOREF, conditionalExpr2, r));
            else
                addNode(new IRnode(IRopcodeType.STOREI, conditionalExpr2, r));
            conditionalExpr2 = r;
        }
        
        LabelASTnode condlabel =  new LabelASTnode(LabelFile.getCondLabel());
        addNode(new IRnode(comp, conditionalExpr1, conditionalExpr2, condlabel));
        conditional_label.push(condlabel);
    }
    
    public void generateElse(MicroParser.Else_partContext ContextObject) {
        if (ContextObject.ELSE() != null) {
            //If there is an else part, insert the label so that the original branch skips to the else statement
            //list instead of the end of the entire if expression.
            LabelASTnode endLabel = new LabelASTnode(LabelFile.getCondLabel());
            addNode(new IRnode(IRopcodeType.JUMP, endLabel));
            addNode(new IRnode(IRopcodeType.LABEL, conditional_label.pop()));
            conditional_label.push(endLabel);
        }
        //If there isn't an else part, we are at the end. Do nothing.  The end if generator will supply the label.
    }
    
    public void generateEndIf() {
        addNode(new IRnode(IRopcodeType.LABEL, conditional_label.pop()));
    }
    
    public void generateWhile(MicroParser.While_stmtContext ContextObject, SymbolTable tableObject) {
        //Drop a label at the beginning so we can come back here
        LabelASTnode labelObject = new LabelASTnode(LabelFile.getLoopLabel());
        addNode(new IRnode(IRopcodeType.LABEL, labelObject));
        //Evaluate the expression and jump to the end if it's false
        generateIf(ContextObject.cond(), tableObject);
        conditional_label.push(labelObject);
    }
    
    public void generateEndWhile() {
        addNode(new IRnode(IRopcodeType.JUMP, conditional_label.pop()));
        addNode(new IRnode(IRopcodeType.LABEL, conditional_label.pop()));
        
    }
    
    private void addNode(IRnode node) {
        if (head == null) {
            head = node;
            root = node;
        }
        else {
            head.setNext(node);
            head = node;
        }
        switch (node.getOp()){
            case LABEL:
                label_table.put(node.getResult().getName(), node);
                break;
            case JUMP: case GE: case GT: case LT: case LE: case EQ: case NE:
                node.setBranchTarget(label_table.get(node.getResult().getName()));
                break;
            default:
        }
    }
    public void print() {
        IRnode h = root;
        System.out.println(";" + "IR code");
        System.out.println(";" + "LABEL main");
        System.out.println(";" + "LINK");
        while (h != null) {
            System.out.println(h.toString());
            h = h.getNext();
        }
        System.out.println(";" + "RET");
        System.out.println(";" + "tiny code");
    }
    
    public void printTiny(SymbolTable s) {
        System.out.println(TinyUtility.generateTiny(root, s));
    }
}