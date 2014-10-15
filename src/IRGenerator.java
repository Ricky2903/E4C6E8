/*
 Harshita Kodali, hkodali
 Amala Chirayil, achirayi
 
 Step4 Version 1.0: IR Generator class file 
 */
public class IRGenerator {
    private IRnode head = null;
    private IRnode root = null;
    public IRGenerator() {
    }
    public IRinterface generateExpr(MicroParser.ExprContext exprContextObject, SymbolTable tableObject) {
        IRinterface expr = generateFactor(exprContextObject.factor(0), tableObject);
        for (int i = 1; i<exprContextObject.factor().size(); i++) {
            
            IRinterface temp = generateFactor(exprContextObject.factor(i), tableObject);
            
            SymbolType type = SymbolType.NULL;
            IRopcodeType op;
            
            if (expr.getType() == SymbolType.FLOAT && temp.getType() == SymbolType.FLOAT) type = SymbolType.FLOAT;
            else if (expr.getType() == SymbolType.INT && temp.getType() == SymbolType.INT) type = SymbolType.INT;
            else throw new RuntimeException("ERROR: Operator " + exprContextObject.addop(i-1).getText() + " not valid for types " + expr.getType() + " and " + temp.getType());
            
            if (exprContextObject.addop(i-1).ADD() != null) {
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
    private IRinterface generateFactor(MicroParser.FactorContext exprContextObject, SymbolTable tableObject) {
        IRinterface expr = generatePostfix(exprContextObject.postfix_expr(0), tableObject);
        for (int i = 1; i<exprContextObject.postfix_expr().size(); i++) {
            IRinterface temp = generatePostfix(exprContextObject.postfix_expr(i), tableObject);
            SymbolType type = SymbolType.NULL;
            IRopcodeType op;
            if (expr.getType() == SymbolType.FLOAT && temp.getType() == SymbolType.FLOAT) type = SymbolType.FLOAT;
            else if (expr.getType() == SymbolType.INT && temp.getType() == SymbolType.INT) type = SymbolType.INT;
            else throw new RuntimeException("ERROR: Operator " + exprContextObject.mulop(i-1).getText() + " not valid for types " + expr.getType() + " and " + temp.getType());
            
            if (exprContextObject.mulop(i-1).MULTIPLY() != null) {
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
    private IRinterface generatePostfix(MicroParser.Postfix_exprContext exprContextObject, SymbolTable tableObject) {
        if (exprContextObject.primary() != null) {
            //handle the primary postfix
            if (exprContextObject.primary().expr() != null) {
                return generateExpr(exprContextObject.primary().expr(), tableObject);
            } else if (exprContextObject.primary().id() != null)
            {
                String id = exprContextObject.primary().id().getText();
                Symbol s = tableObject.findSymbol(id);
                if (s == null) throw new RuntimeException("ERROR: Symbol " + id + " not found.");
                TempASTnode tmpreg = new TempASTnode (s.getType(), RegFile.getNext());
                if (s.getType() == SymbolType.FLOAT) addNode(new IRnode(IRopcodeType.STOREF, new TermASTnode(s.getType(), s.getID()), tmpreg));
                else if (s.getType() == SymbolType.INT) addNode(new IRnode(IRopcodeType.STOREI, new TermASTnode(s.getType(),s.getID()), tmpreg));
                else throw new RuntimeException("Cannot create postfix for type " + s.getType());
                return tmpreg;
            } else if (exprContextObject.primary().FLOATLITERAL() != null) {
                return new LiteralASTnode(Float.parseFloat(exprContextObject.primary().FLOATLITERAL().toString()));
            } else if (exprContextObject.primary().INTLITERAL() != null) {
                return new LiteralASTnode(Integer.parseInt(exprContextObject.primary().INTLITERAL().toString()));
            } else return null;
        }else {
            throw new RuntimeException("Cannot handle call expressions yet");
        }    }

    
    public void generateAssignment(MicroParser.Assign_stmtContext exprContextObject, SymbolTable tableObject) {
        IRinterface tempreg = generateExpr(exprContextObject.expr(), tableObject);
        Symbol s = tableObject.findSymbol(exprContextObject.id().getText());
        if (s == null) throw new RuntimeException("ERROR: Could not find symbol " + exprContextObject.id().getText());
        if (s.getType() != tempreg.getType()) throw new RuntimeException("ERROR: type mismatch");
        if (tempreg.getType() == SymbolType.FLOAT) addNode(new IRnode(IRopcodeType.STOREF, tempreg, new TermASTnode(s.getType(), s.getID())));
        else if (tempreg.getType() == SymbolType.INT) addNode(new IRnode(IRopcodeType.STOREI, tempreg, new TermASTnode(s.getType(), s.getID())));
        else throw new RuntimeException("ERROR: Assignment not valid for type " + tempreg.getType());
    }
    
    public void generateRead(MicroParser.Read_stmtContext exprContextObject, SymbolTable tableObject) {
        for (MicroParser.IdContext id : exprContextObject.id_list().id()) {
            Symbol s = tableObject.findSymbol(id.getText());
            if (s == null) throw new RuntimeException("ERROR: Symbol " + id + " not found.");
            if (s.getType() == SymbolType.FLOAT) addNode(new IRnode(IRopcodeType.READF, new TermASTnode(s.getType(), s.getID())));
            else if (s.getType() == SymbolType.INT) addNode(new IRnode(IRopcodeType.READI, new TermASTnode(s.getType(), s.getID())));
            else throw new RuntimeException("ERROR: Read statement not valid for type " + s.getType());
        }
    }
    public void generateWrite(MicroParser.Write_stmtContext exprContextObject, SymbolTable tableObject) {
        for (MicroParser.IdContext id : exprContextObject.id_list().id()) {
            Symbol s = tableObject.findSymbol(id.getText());
            if (s == null) throw new RuntimeException("ERROR: Symbol " + id + " not found.");
            if (s.getType() == SymbolType.FLOAT) addNode(new IRnode(IRopcodeType.WRITEF, new TermASTnode(s.getType(), s.getID())));
            else if (s.getType() == SymbolType.INT) addNode(new IRnode(IRopcodeType.WRITEI, new TermASTnode(s.getType(), s.getID())));
            else if (s.getType() == SymbolType.STRING) addNode(new IRnode(IRopcodeType.WRITES, new TermASTnode(s.getType(), s.getID())));
            else throw new RuntimeException("ERROR: Write statement not valid for type " + s.getType());
        }    }
    
    private void addNode(IRnode node) {
        if (head == null) {
            head = node;
            root = node;
        }
        else {
            head.setNext(node);
            head = node;
        }
    }
    public void print() {
        IRnode h = root;
        while (h != null) {
            System.out.println(h.toString());
            h = h.getNext();
        }
    }
    
    public void printTiny(SymbolTable s) {
        System.out.println(TinyUtility.generateTiny(root, s));
    }
}