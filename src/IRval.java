/*
 Harshita Kodali, hkodali
 Amala Chirayil, achirayi
 
 Step4, version 1.0: IRval
 
 contains
 */

public class IRval {
    private final byte type;
    /*
     * 0 - float literal
     * 1 - int literal
     * 2 - float register/var
     * 3 - int register/var
     */
    private float val_1;
    private int val_2;
    private String val_3;
    
    public IRval(float FloatLit) {
        type = 0;
        val_1 = FloatLit;
    }
    
    public IRval(int IntLit) {
        type = 1;
        val_2 = IntLit;
    }
    
    public IRval(String reg, boolean isFloat) {
        if (isFloat) type = 2;
        else type = 3;
        val_3 = reg;
    }
    
    public Object val() {
        if (type == 0) return val_1;
        else if (type == 1) return val_2;
        else return val_3;
    }
    
    public boolean isFloat() {
        return type == 0 || type == 2;
    }
    
    public boolean isInt() {
        return type == 1 || type == 3;
    }
    
    public boolean isLit() {
        return type == 0 || type == 1;
    }
    
    public boolean isReg() {
        return type == 2 || type == 3;
    }
    
    @Override
    public String toString() {
        if (type == 0) return String.valueOf(val_1);
        else if (type == 1) return String.valueOf(val_2);
        else return val_3;
    }
}