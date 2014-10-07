/* 
    Harshita Kodali, hkodali
    Amala Chirayil, achirayi
 
    Step3: BOOLEAN ERROR STRATEGY
 V2 CHANGED FROM EXTENDING THE ANTLRERROR STRATEGY FROM THE MAIN CLASS (STEP 2)
 */
import org.antlr.v4.runtime.DefaultErrorStrategy;
import org.antlr.v4.runtime.InputMismatchException;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Token;

public class BooleanErrorStrategy extends DefaultErrorStrategy {
    
	@Override
	public void reportError(Parser recognizer, RecognitionException re) {
		System.out.println("Not accepted");
		System.exit(1);
		
	}
	
	@Override
	public Token recoverInline(Parser recognizer) throws RuntimeException {
		throw new RuntimeException(new InputMismatchException(recognizer));
	}
	
	@Override
	public void sync(Parser recognizer) {
		
	}
    
}