public class TempASTnode implements IRinterface {
    
    private final SymbolType _type;
    private final String _regName;
    
    public TempASTnode(SymbolType type, String regName) {
        _type = type;
        _regName = regName;
    }
    @Override
    public ASTtype getASTtype() {
        return ASTtype.TEMPORARY;
    }
    
    @Override
    public SymbolType getType() {
        return _type;
    }
    
    @Override
    public String getName() {
        return _regName;
    }
    
}