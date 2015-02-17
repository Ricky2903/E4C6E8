public class LabelASTnode implements IRinterface {
    private String labelvalue;
    public LabelASTnode (String value) {
        labelvalue = value;
    }
    @Override
    public ASTtype getASTtype() {
        return ASTtype.LABEL;
    }
    
    @Override
    public SymbolType getType() {
        return SymbolType.NULL;
    }
    
    @Override
    public String getName() {
        return labelvalue;
    }
    
}