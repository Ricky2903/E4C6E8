/*
 Harshita Kodali, hkodali
 Amala Chirayil, achirayi
 
 Step4, version 1.0: IRopcodeTypeVerify
 */
public class IRopcodeTypeVerify {
    public static boolean verifyOpcode(IRopcodeType IRopcodeObject, int ID) {
        return numOperands(IRopcodeObject) == ID;
    }
    public static int numOperands(IRopcodeType IRopcodeObject) {
        switch (IRopcodeObject) {
            case JUMP: case LABEL:
            case READI: case READF:
            case WRITEI: case WRITEF:
                return 0;
            case STOREI: case STOREF:
                return 1;
            default:
                return 2;
        }
    }
}