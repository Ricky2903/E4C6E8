
public class IRnode {
    
    private IRopcodeType _opcode;
    private String _operand1;
    private String _operand2;
    private String _result;
    private IRnode _next = null;
    private IRnode _br_target = null;
    
    public IRnode(IRopcodeType opcode, String result){
        if (IRopcodeTypeVerify.verifyOpcode(opcode, 0)) {
            _opcode = opcode;
            _result = result;
        } else {
            throw new UnsupportedOperationException("Constructor does not match operand count for " + opcode.toString());
        }
    }
    public IRnode(IRopcodeType opcode, String operand1, String result) {
        if (IRopcodeTypeVerify.verifyOpcode(opcode,1)) {
            _opcode = opcode;
            _result = result;
            _operand1 = operand1;
        }else{
            throw new UnsupportedOperationException("Constructor does not match operand count for " + opcode.toString());
        }
    }
    public IRnode(IRopcodeType opcode, String operand1, String operand2, String result) {
        if (IRopcodeTypeVerify.verifyOpcode(opcode,2)) {
            _opcode = opcode;
            _result = result;
            _operand1 = operand1;
            _operand2 = operand2;
        } else {
            throw new UnsupportedOperationException("Constructor does not match operand count for " + opcode.toString());
        }
    }
    
    public void setBranchTarget (IRnode target) {
        _br_target = target;
    }
    
    public IRnode getBranchTarget() {
        return _br_target;
    }
    
    public void setNext(IRnode next) {
        _next = next;
    }
    
    public IRnode getNext() {
        return _next;
    }
    
    @Override
    public String toString() {
        switch(IRopcodeTypeVerify.numOperands(this._opcode)) {
            case 0:
                return _opcode.toString() + " " + _result;
            case 1:
                return _opcode.toString() + " " + _operand1 + " " + _result;
            default:
                return _opcode.toString() + " " + _operand1 + " " + _operand2 + " " + _result;
        }
    }
}