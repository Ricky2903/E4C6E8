/* 
    Harshita Kodali, hkodali
    Amala Chirayil, achirayi
 
    Step3: Symbol Table Class File
 */

import org.antlr.v4.runtime.misc.NotNull;


public class MicroExtendedListener extends MicroBaseListener {
	
	private int CONT_INC = 1;
	private String AUTOI() {
		return "BLOCK " + String.valueOf(CONT_INC++);
	}
	private SymbolTable tableObject;
    private IRGenerator generator;
	public MicroExtendedListener() {
        generator = new IRGenerator ();
	}
	
	@Override public void enterProgram(@NotNull MicroParser.ProgramContext contextObject) {
		tableObject = new SymbolTable("GLOBAL");
	}
	@Override public void enterFunc_decl(@NotNull MicroParser.Func_declContext contextObject) {
		
        //RECORDING SYMBOLS IN THE ORDER THEY ARE SEEN
        tableObject = SymbolTable.createLeaf(tableObject, contextObject.id().getText());
	}
	@Override public void exitFunc_decl(@NotNull MicroParser.Func_declContext contextObject) {
		if (contextObject.getChildCount() == 9) {
			tableObject = tableObject.getParent();
		}else {
			//BAD FUNCTION GENERATION
		}
	}
	@Override public void exitParam_decl(@NotNull MicroParser.Param_declContext contextObject) {
		SymbolType t = SymbolType.NULL;
		if (contextObject.var_type().INT() != null) {
			t = SymbolType.INT;
		} else if (contextObject.var_type().FLOAT() != null) {
			t = SymbolType.FLOAT;
		}
		if (t==SymbolType.NULL) return;
		tableObject.pushSymbol(new Symbol(contextObject.id().IDENTIFIER().getText(), t, null));
	}
	@Override public void enterIf_stmt(@NotNull MicroParser.If_stmtContext contextObject) {
		tableObject = SymbolTable.createLeaf(tableObject, AUTOI());
	}
	@Override public void exitIf_stmt(@NotNull MicroParser.If_stmtContext contextObject) {
		tableObject = tableObject.getParent();
	}
	
	@Override public void enterElse_part(@NotNull MicroParser.Else_partContext contextObject) {
		tableObject = SymbolTable.createLeaf(tableObject, AUTOI());
	}
    
	@Override public void exitElse_part(@NotNull MicroParser.Else_partContext contextObject) {
        if (contextObject.ELSE() == null)
        {
            tableObject.getParent().delChild(tableObject);
            CONT_INC--;
        }
		tableObject = tableObject.getParent();
	}
	
	@Override public void enterWhile_stmt(@NotNull MicroParser.While_stmtContext contextObject) {
		tableObject = SymbolTable.createLeaf(tableObject, AUTOI());
	}
    
	@Override public void exitWhile_stmt(@NotNull MicroParser.While_stmtContext contextObject) {
		tableObject = tableObject.getParent();
	}
	
	@Override public void exitVar_decl(@NotNull MicroParser.Var_declContext contextObject) {
		SymbolType SymbolTypeObject = SymbolType.NULL;
		if (contextObject.var_type().INT() != null) {
			SymbolTypeObject = SymbolType.INT;
		} else if (contextObject.var_type().FLOAT() != null) {
			SymbolTypeObject = SymbolType.FLOAT;
		}
		if (SymbolTypeObject==SymbolType.NULL) return;
		for (MicroParser.IdContext IdContextObject : contextObject.id_list().id()) {
			tableObject.pushSymbol(new Symbol(IdContextObject.IDENTIFIER().getText(), SymbolTypeObject, null));
		}
	}
	
	@Override public void exitString_decl(@NotNull MicroParser.String_declContext contextObject) {
		tableObject.pushSymbol(new Symbol(contextObject.id().IDENTIFIER().getText(), SymbolType.STRING, contextObject.STRINGLITERAL().getText()));
	}
	
	public void printSymbolTable() {
		tableObject.printRcsv();
	}
    
    @Override public void exitAssign_stmt(@NotNull MicroParser.Assign_stmtContext contextObject) {
        generator.generateAssignment(contextObject, tableObject);
    }
    
    @Override public void exitRead_stmt(@NotNull MicroParser.Read_stmtContext contextObject) {
        generator.generateRead(contextObject, tableObject);
    }
    
    @Override public void exitWrite_stmt(@NotNull MicroParser.Write_stmtContext contextObject) {
        generator.generateWrite(contextObject, tableObject);
    }
    public void printIR() {
        generator.print();
    }
    
    public void printTiny() {
        generator.printTiny(tableObject);
    }
}