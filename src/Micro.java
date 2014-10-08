/* 
    Harshita Kodali, hkodali
    Amala Chirayil, achirayi
 
    Step2: MicroParser main class file
 */

import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.*;

public class Micro {
    public static void main(String[] args) throws IOException {
        if(args.length != 1) {
            throw new IOException("Not enough arguments provided");
        }
        MicroLexer lexerObject = new MicroLexer (new ANTLRFileStream(args[0]));
        CommonTokenStream CommonTokenStreamObject = new CommonTokenStream(lexerObject);
        CommonTokenStreamObject.fill();
        
        MicroParser parserObject = new MicroParser(CommonTokenStreamObject);
        parserObject.setErrorHandler(new BooleanErrorStrategy());
        MicroExtendedListener listenerObject = new MicroExtendedListener();
        
        //SYMBOL TABLE COMPONENTS
        ParseTree tree = null;
        try {
        	tree = parserObject.program();
        } catch (Exception e) {
        	System.exit(0);
        }
        //System.out.println("Accepted");
        
        ParseTreeWalker walkerObject = new ParseTreeWalker();
        try {
            walkerObject.walk(listenerObject, tree);
        } catch (IllegalArgumentException e) {
        	System.out.println(e.getMessage());
        	System.exit(0);
        }
        //listenerObject.printSymbolTable();
        listenerObject.printIR();
        listenerObject.printTiny();
        
        
    }
}