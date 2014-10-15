/*
 Harshita Kodali, hkodali
 Amala Chirayil, achirayi
 
 Step4: Implementing the tiny simulator
 */

public class TinyUtility {
    public static String generateTiny(IRnode head, SymbolTable rootTable) {
        String varDecl = createDecl(rootTable);
        String code = "";
        IRnode curr = head;
        while (curr != null) {
            code += Simulate(curr);
            curr = curr.getNext();
        }
        return varDecl.trim() + "\n" + code.trim() + "\n" + "end";
    }
    
    private static String createDecl(SymbolTable s) {
        String retv = "";
        for (Symbol sym : s.getSymbols().values()) {
            if (sym.getType() == SymbolType.STRING) retv += "str " + sym.getID() + " " + (String)sym.getValue();
            else if (sym.getType() == SymbolType.INT || sym.getType() == SymbolType.FLOAT) retv += "var " + sym.getID() + "\n";
        }
        for (SymbolTable st : s.getChildren()) {
            retv += createDecl(st);
        }
        return retv;
    }
    
    private static String Simulate(IRnode node) {
        IRopcodeType op = node.getOp();
        String opstr = "";
        switch(op) {
            case MULTI:
                opstr = "muli ";
                break;
            case MULTF:
                opstr = "mulr ";
                break;
            case ADDI:
                opstr = "addi ";
                break;
            case ADDF:
                opstr = "addr ";
                break;
            case DIVI:
                opstr = "divi ";
                break;
            case DIVF:
                opstr = "divr ";
                break;
            case SUBI:
                opstr = "subi "; break;
            case SUBF:
                opstr = "subr "; break;
            case READI:
                opstr = "sys readi "; break;
            case READF:
                opstr = "sys readr "; break;
            case WRITEF:
                opstr = "sys writer "; break;
            case WRITEI:
                opstr = "sys writei "; break;
            case WRITES:
                opstr = "sys writes "; break;
            case STOREI: case STOREF:
                opstr = "move "; break;
        }
        
        switch(IRopcodeTypeVerify.numOperands(op)) {
            case 0:
                opstr += fixReg(node.getResult()); break;
            case 1:
                opstr += fixReg(node.getOperand1()) + " " + fixReg(node.getResult()); break;
            case 2:
                opstr = "move " + fixReg(node.getOperand1()) + " " + fixReg(node.getResult()) + "\n" + opstr;
                opstr += fixReg(node.getOperand2()) + " " + fixReg(node.getResult());
        }
        return opstr + "\n";
    }
    private static String fixReg(IRinterface val) {
        if (val.getASTtype() == ASTtype.TEMPORARY) {
            return "r" + val.getName().substring(2);
        }
        return val.getName();
    }
}