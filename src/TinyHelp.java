public class TinyHelp {
    public static int opcount(TinyOp op) {
        switch(op) {
            case RET: case UNLNK: case HALT:
                return 0;
            case VAR: case LABEL: case INCI: case DECI: case PUSH: case POP: case JSR: case LINK:
            case JMP: case JGT: case JLT: case JLE: case JGE: case JNE: case JEQ: case READI: case READR:
            case WRITEI: case WRITER: case WRITES:
                return 1;
        }
        return 2;
    }
    public static String getName(TinyOp op) {
        switch(op) {
            case READI: return "sys readi";
            case READR: return "sys readr";
            case WRITEI: return "sys writei";
            case WRITER: return "sys writer";
            case WRITES: return "sys writes";
            case HALT: return "sys halt";
            default: return op.toString().toLowerCase();
        }
    }
}