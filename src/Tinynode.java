

public class Tinynode {
    private Tinynode next;
    private TinyOp op;
    private IRinterface opnd1;
    private IRinterface opnd2;
    public Tinynode(TinyOp op){
        if (TinyHelp.opcount(op) != 0) throw new UnsupportedOperationException("Constructor does not match operand count for " + op.toString());
        else {
            next = null;
            this.op = op;
            opnd1 = null;
            opnd2 = null;
        }
    }
    public Tinynode(TinyOp op, IRinterface opnd1) {
        if (TinyHelp.opcount(op) != 1) throw new UnsupportedOperationException("Constructor does not match operand count for " + op.toString());
        else {
            next = null;
            this.op = op;
            this.opnd1 = opnd1;
            opnd2 = null;
        }
    }
    public Tinynode(TinyOp op, IRinterface opnd1, IRinterface opnd2) {
        if (TinyHelp.opcount(op)!= 1) throw new UnsupportedOperationException("Constructor does not match operand count for " + op.toString());
        else {
            next = null;
            this.op = op;
            this.opnd1 = opnd1;
            this.opnd2 = opnd2;
        }
    }
    
    public void setNext(Tinynode node) {
        this.next = node;
    }
    
    public Tinynode getNext() {
        return this.next;
    }
    
    
    @Override
    public String toString() {
        switch(TinyHelp.opcount(this.op)) {
            case 0:
                return TinyHelp.getName(op);
            case 1:
                return TinyHelp.getName(op) + " " + opnd1.getName();
            default:
                return TinyHelp.getName(op) + " " + opnd1.getName() + " " + opnd2.getName();
        }
    }
}