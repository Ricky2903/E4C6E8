/*
 Harshita Kodali, hkodali
 Amala Chirayil, achirayi
 
 Step4: Implementing the tiny simulator
 */

public class TinyUtility {
    
    private static Tinynode head = null;
    private static Tinynode root = null;
    public static String generateTiny(IRnode head, SymbolTable rootTable) {
        String varDecl = createDecl(rootTable);
        String code = "";
        IRnode curr = head;
        while (curr != null) {
            code += Simulate(curr);
            curr = curr.getNext();
        }
        return varDecl.trim() + "\n" + code.trim() + "\n" + "sys halt";
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
        String jmpstr = "";
        boolean performMove = false;
        
        switch(op) {
            case MULTI:
                opstr = "muli ";
                performMove = true;
                break;
            case MULTF:
                opstr = "mulr ";
                performMove = true;
                break;
            case ADDI:
                opstr = "addi ";
                performMove = true;
                break;
            case ADDF:
                opstr = "addr ";
                performMove = true;
                break;
            case DIVI:
                opstr = "divi ";
                performMove = true;
                break;
            case DIVF:
                opstr = "divr ";
                performMove = true;
                break;
            case SUBI:
                opstr = "subi ";
                performMove = true;
                break;
            case SUBF:
                opstr = "subr ";
                performMove = true;
                break;
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
            case LABEL:
                opstr = "label "; break;
            case JUMP:
                opstr = "jmp "; break;
            case LE: case GE: case LT: case GT: case NE: case EQ:
                if (node.getOperand1().getType() == SymbolType.INT) opstr = "cmpi ";
                else if (node.getOperand1().getType() == SymbolType.FLOAT) opstr = "cmpr ";
                switch (op) {
                    case LE: jmpstr = "jle ";break;
                    case GE: jmpstr = "jge ";break;
                    case LT: jmpstr = "jlt "; break;
                    case GT: jmpstr = "jgt "; break;
                    case NE: jmpstr = "jne "; break;
                    case EQ: jmpstr = "jeq "; break;
                }
            System.out.println(op);
        }
        
        switch(IRopcodeTypeVerify.numOperands(op)) {
            case 0:
                opstr += fixReg(node.getResult()); break;
            case 1:
                opstr += fixReg(node.getOperand1()) + " " + fixReg(node.getResult()); break;
            case 2:
                if (performMove)
                {
                    opstr = "move " + fixReg(node.getOperand1()) + " " + fixReg(node.getResult()) + "\n" + opstr;
                    opstr += fixReg(node.getOperand2()) + " " + fixReg(node.getResult());
                }else {
                    opstr +=  fixReg(node.getOperand1()) + " " + fixReg(node.getOperand2());
                    jmpstr += fixReg(node.getResult()) + "\n";
                }
        }
        return opstr + "\n" + jmpstr;
    }
    
    private void addNode(Tinynode node) {
        if (head == null) {
            head = root = node;
        } else {
            head.setNext(node);
            head = node;
        }
    }
    
    private static String fixReg(IRinterface val) {
        if (val.getASTtype() == ASTtype.TEMPORARY) {
            return "r" + val.getName().substring(2);
        }
        return val.getName();
    }
}