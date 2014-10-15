
public class IRnode {
    
    private IRopcodeType _opcode;
    private IRinterface _operand1;
    private IRinterface _operand2;
    private IRinterface _result;
    private IRnode _next = null;
    private IRnode _br_target = null;
    
    public IRnode(IRopcodeType opcode, IRinterface result){
        if (IRopcodeTypeVerify.verifyOpcode(opcode, 0)) {
            _opcode = opcode;
            _result = result;
        } else {
            throw new UnsupportedOperationException("Constructor does not match operand count for " + opcode.toString());
        }
    }
    
    public IRnode(IRopcodeType opcode, IRinterface operand1, IRinterface result) {
        if (IRopcodeTypeVerify.verifyOpcode(opcode,1)) {
            _opcode = opcode;
            _result = result;
            _operand1 = operand1;
        }else{
            throw new UnsupportedOperationException("Constructor does not match operand count for " + opcode.toString());
     
        }
    }
    public IRnode(IRopcodeType opcode, IRinterface operand1, IRinterface operand2, IRinterface result) {
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
    
    public IRopcodeType getOp() {
        return _opcode;
    }
    
    public IRinterface getResult() {
        return _result;
    }
    
    public IRinterface getOperand1() {
        return _operand1;
    }
    
    public IRinterface getOperand2() {
        return _operand2;
    }
    
    @Override
    public String toString() {
        switch(IRopcodeTypeVerify.numOperands(this._opcode)) {
            case 0:
                return _opcode.toString() + " " + _result.getName();
            case 1:
                return _opcode.toString() + " " + _operand1.getName() + " " + _result.getName();
            default:
                return _opcode.toString() + " " + _operand1.getName() + " " + _operand2.getName() + " " + _result.getName();
        }
    }
}