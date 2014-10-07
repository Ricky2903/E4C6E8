/* 
    Harshita Kodali, hkodali
    Amala Chirayil, achirayi
 
    Step3: SymbolTable File
 */
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.LinkedHashMap;
import java.util.List;

//DOUBLY LINKED TREE => ACCESS IN BOTH THE DIRECTIONS
//0(1) LOOKUP IMPLEMENTED USING LINKED HASHSET
//ITEM ORDER PRESERVED

public class SymbolTable {
    
	private String _id;
	private SymbolTable _parent;
	private List<SymbolTable> _children;
    
	private LinkedHashMap<String, Symbol> _lookup;
	
	public SymbolTable (String id) {
		_parent= null;
		_lookup = new LinkedHashMap<String, Symbol>();
		_children = new ArrayList<SymbolTable>();
		_id = id;
	}
	
	
	public void pushSymbol(Symbol SymbolObject) throws IllegalArgumentException{
		if (_lookup.containsValue(SymbolObject)) throw new IllegalArgumentException("DECLARATION ERROR " + SymbolObject.getID());
		_lookup.put(SymbolObject.getID(), SymbolObject);
	}
	
	public void addParent(SymbolTable SymbolTableObject) {
		_parent = SymbolTableObject;
	}
	
	public SymbolTable getParent() {
		return _parent;
	}
	
	public void addChild(SymbolTable SymbolTableObject) {
		_children.add(SymbolTableObject);
	}
    
    public void delChild(SymbolTable SymbolTableObject) {
        _children.remove(SymbolTableObject);
    }
	
	public List<SymbolTable> getChildren() {
		return _children;
	}
	
	
	public void printRcsv() {
		System.out.print("Symbol table " + _id);
		for (Symbol SymbolObject : _lookup.values()) {
			System.out.print("\n" + SymbolObject.toString());
		}
		System.out.println();
		for (SymbolTable SymbolTableObject : _children) {
			SymbolTableObject.printRcsv();
		}
	}
	
	public static SymbolTable createLeaf(SymbolTable parent, String id) {
		SymbolTable newroot = new SymbolTable(id);
		newroot.addParent(parent);
		parent.addChild(newroot);
		return newroot;
	}
    
    public LinkedHashMap<String, Symbol> getSymbols() {
        return _lookup;
    }
    
    public Symbol findSymbol(String name) {
        SymbolTable current = this;
        while (current != null) {
            if (current._lookup.containsKey(name)) return current._lookup.get(name);
            current = current._parent;
        }
        return null;
    }
    
}