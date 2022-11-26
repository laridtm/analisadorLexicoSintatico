package syntax;

import lexical.Token;

public class SyntaxException extends Exception {
    public SyntaxException(String expected, Token founded) { 
        super("Erro sintático, estava esperando ["+ expected +"] e encontrei " + founded);
    }
}
