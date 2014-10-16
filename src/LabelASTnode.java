
public class LabelASTnode implements IRinterface {
    private String labelvalue;
    public Label (String value) {
        labelvalue = value;
    }
    @Override
    public AccessMethod getAccessMethod() {
        return AccessMethod.LABEL;
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