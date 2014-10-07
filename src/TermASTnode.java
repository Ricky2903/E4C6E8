/*
 Harshita Kodali, hkodali
 Amala Chirayil, achirayi
 
 Step4: Utility File with
 */

public class TermASTnode implements IRinterface {
    
    private final SymbolType _type;
    private final String _name;
    
    public TermASTnode(SymbolType type, String varID) {
        _type = type;
        _name = varID;
    }
    @Override
    public ASTtype getASTtype() {
        return ASTtype.TERM;
    }
    
    @Override
    public SymbolType getType() {
        return _type;
    }
    
    @Override
    public String getName() {
        return _name;
    }
    
}