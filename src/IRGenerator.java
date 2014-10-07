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
    public IRval generateExpr(MicroParser.ExprContext exprContextObject, SymbolTable tableObject) {
        IRval res = generateFactor(exprContextObject.factor(0), tableObject);
        for (int i = 1; i<exprContextObject.factor().size(); i++) {
            IRval temp = generateFactor(exprContextObject.factor(i), tableObject);
            
            if (exprContextObject.addop(i-1).ADD() != null) {
                if (res.isFloat()) {
                    addNode(new IRnode(IRopcodeType.ADDF, res.toString(), temp.toString(), RegFile.getNext()));
                    res = new IRval(RegFile.getCurrent(), true);
                }else {
                    addNode(new IRnode(IRopcodeType.ADDI, res.toString(), temp.toString(), RegFile.getNext()));
                    res = new IRval(RegFile.getCurrent(),false);
                }
            } else {
                if (res.isFloat()) {
                    addNode(new IRnode(IRopcodeType.SUBF, res.toString(), temp.toString(), RegFile.getNext()));
                    res = new IRval(RegFile.getCurrent(), true);
                } else {
                    addNode(new IRnode(IRopcodeType.SUBI, res.toString(), temp.toString(), RegFile.getNext()));
                    res = new IRval(RegFile.getCurrent(), false);
                }
            }
        }
        return res;
    }
    private IRval generateFactor(MicroParser.FactorContext exprContextObject, SymbolTable tableObject) {
        IRval res = generatePostfix(exprContextObject.postfix_expr(0), tableObject);
        for (int i = 1; i<exprContextObject.postfix_expr().size(); i++) {
            IRval temp = generatePostfix(exprContextObject.postfix_expr(i), tableObject);
            
            if (exprContextObject.mulop(i-1).MULTIPLY() != null) {
                if (res.isFloat()) {
                    addNode(new IRnode(IRopcodeType.MULTF, res.toString(), temp.toString(), RegFile.getNext()));
                    res = new IRval(RegFile.getCurrent(), true);
                }else {
                    addNode(new IRnode(IRopcodeType.MULTI, res.toString(), temp.toString(), RegFile.getNext()));
                    res = new IRval(RegFile.getCurrent(),false);
                }
            } else {
                if (res.isFloat()) {
                    addNode(new IRnode(IRopcodeType.DIVF, res.toString(), temp.toString(), RegFile.getNext()));
                    res = new IRval(RegFile.getCurrent(), true);
                } else {
                    addNode(new IRnode(IRopcodeType.DIVI, res.toString(), temp.toString(), RegFile.getNext()));
                    res = new IRval(RegFile.getCurrent(), false);
                }
            }
        }
        return res;
    }
    private IRval generatePostfix(MicroParser.Postfix_exprContext exprContextObject, SymbolTable tableObject) {
        if (exprContextObject.primary() != null) {
            //handle the primary postfix
            if (exprContextObject.primary().expr() != null) {
                return generateExpr(exprContextObject.primary().expr(), tableObject);
            } else if (exprContextObject.primary().id() != null) {
                String id = exprContextObject.primary().id().getText();
                Symbol s = tableObject.findSymbol(id);
                if (s == null) throw new RuntimeException("ERROR: Symbol " + id + " not found.");
                return new IRval(id, s.getType()==SymbolType.FLOAT ? true : false);
            } else if (exprContextObject.primary().FLOATLITERAL() != null) {
                return new IRval(Float.parseFloat(exprContextObject.primary().FLOATLITERAL().toString()));
            } else if (exprContextObject.primary().INTLITERAL() != null) {
                return new IRval(Integer.parseInt(exprContextObject.primary().INTLITERAL().toString()));
            } else return null;
        }else {
            throw new RuntimeException("Cannot handle call expressions yet");
        }
    }
    
    public void generateAssignment(MicroParser.Assign_stmtContext exprContextObject, SymbolTable tableObject) {
        IRval tempreg = generateExpr(exprContextObject.expr(), tableObject);
        if (tempreg.isFloat()) {
            addNode(new IRnode(IRopcodeType.STOREF, tempreg.toString(), exprContextObject.id().getText()));
        }
        else {
            addNode(new IRnode(IRopcodeType.STOREI, tempreg.toString(), RegFile.getNext()));
            tempreg = new IRval(RegFile.getCurrent(),false);
            addNode(new IRnode(IRopcodeType.STOREI, tempreg.toString(), exprContextObject.id().getText()));
        }
    }
    
    public void generateRead(MicroParser.Read_stmtContext exprContextObject, SymbolTable tableObject) {
        for (MicroParser.IdContext id : exprContextObject.id_list().id()) {
            Symbol s = tableObject.findSymbol(id.getText());
            if (s == null) throw new RuntimeException("ERROR: Symbol " + id + " not found.");
            if (s.getType() == SymbolType.FLOAT) addNode(new IRnode(IRopcodeType.READF, s.getID()));
            else addNode(new IRnode(IRopcodeType.READI, s.getID()));
        }
    }
    public void generateWrite(MicroParser.Write_stmtContext exprContextObject, SymbolTable tableObject) {
        for (MicroParser.IdContext id : exprContextObject.id_list().id()) {
            Symbol s = tableObject.findSymbol(id.getText());
            if (s == null) throw new RuntimeException("ERROR: Symbol " + id + " not found.");
            if (s.getType() == SymbolType.FLOAT) addNode(new IRnode(IRopcodeType.WRITEF, s.getID()));
            else addNode(new IRnode(IRopcodeType.WRITEI, s.getID()));
        }
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
    }
    public void print() {
        IRnode h = root;
        while (h != null) {
            System.out.println(h.toString());
            h = h.getNext();
        }
    }
}