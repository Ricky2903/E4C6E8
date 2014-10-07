/* 
    Harshita Kodali, hkodali
    Amala Chirayil, achirayi
 
    Step3: Symbol Class File
 */

public class Symbol {
	private String _ident;
	private SymbolType _type;
	private Object _value;
	
	public Symbol (String id, SymbolType type, Object value) throws IllegalArgumentException{
		_ident = id;
		_type = type;
		if (value == null) {
			_value = null;
			return;
		}
		try {
			switch(type){
                case FLOAT:
                    _value = Float.valueOf((String)value);
                    break;
                case INT:
                    _value = Integer.valueOf((String)value);
                    break;
                case STRING:
                    _value = value.toString();
                    break;
                default:
                    throw new Exception();
			}
		} catch (Exception e) {
			throw new IllegalArgumentException("Type " + value + " cannot be cast to type" + type.toString());
		}
	}
	
	public String getID () {
		return _ident;
	}
	
	public SymbolType getType() {
		return _type;
	}
	
	public Object getValue() {
		return _value;
	}
	
	@Override public String toString() {
		return "name " + _ident + " type " + _type.toString() +  (_value == null ? "" : " value " + _value.toString());
	}
	
	
	@Override public int hashCode() {
		return _ident.hashCode();
	}
	
	@Override public boolean equals(Object ObjectObject) {
		return ObjectObject != null && ObjectObject instanceof Symbol && this._ident.equals(((Symbol) ObjectObject)._ident);
	}
	
	
	
}