/*
 Harshita Kodali, hkodali
 Amala Chirayil, achirayi
 
 Step5 Version 1.0: Compare Class File
*/

public class Compare {
    public static IRopcodeType generateComparisonOp(MicroParser.CompopContext compareObject) {
        if (compareObject.LTHAN()!= null) {
            return IRopcodeType.LT;
        } else if (compareObject.LTHANEQ() != null) {
            return IRopcodeType.LE;
        } else if (compareObject.GTHAN() != null) {
            return IRopcodeType.GT;
        } else if (compareObject.GTHANEQ() != null) {
            return IRopcodeType.GE;
        } else if (compareObject.EQUAL() != null) {
            return IRopcodeType.EQ;
        } else if (compareObject.NEQUAL() != null) {
            return IRopcodeType.NE;
        }
        return null;
    }
    public static IRopcodeType invert(IRopcodeType op) {
        if (op == IRopcodeType.LT) return IRopcodeType.GE;
        else if (op == IRopcodeType.GT) return IRopcodeType.LE;
        else if (op == IRopcodeType.LE) return IRopcodeType.GT;
        else if (op == IRopcodeType.GE) return IRopcodeType.LT;
        else if (op == IRopcodeType.EQ) return IRopcodeType.NE;
        else if (op == IRopcodeType.NE) return IRopcodeType.EQ;
        return op;
    }
}