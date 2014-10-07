/*
 Harshita Kodali, hkodali
 Amala Chirayil, achirayi
 
 Step4: AST node interface for a literal
 */

public class LiteralASTnode implements IRinterface {
    
    private final SymbolType _type;
    private final float _fvalue;
    private final int _ivalue;
    private final String _svalue;
    
    public LiteralASTnode(float value) {
        _fvalue = value;
        _ivalue = 0;
        _svalue = null;
        _type = SymbolType.FLOAT;
    }
    
    public LiteralASTnode(int value) {
        _fvalue = 0;
        _ivalue = value;
        _svalue = null;
        _type = SymbolType.INT;
    }
    
    public LiteralASTnode(String value) {
        _fvalue = 0;
        _ivalue = 0;
        _svalue = value;
        _type = SymbolType.STRING;
    }
    
    @Override
    public ASTtype getASTtype() {
        return ASTtype.LITERAL;
    }
    
    @Override
    public SymbolType getType() {
        return _type;
    }
    
    @Override
    public String getName() {
        if (_type == SymbolType.FLOAT) {
            return Float.toString(_fvalue);
        } else if (_type == SymbolType.INT){
            return Integer.toString(_ivalue);
        } else if (_type == SymbolType.STRING) {
            return _svalue;
        } else {
            return null;
        }
    }
    
    public Object value() {
        if (_type == SymbolType.FLOAT) {
            return _fvalue;
        } else if (_type == SymbolType.INT){
            return _ivalue;
        } else if (_type == SymbolType.STRING) {
            return _svalue;
        } else {
            return null;
        }
    }
    
}