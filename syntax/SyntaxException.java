package syntax;

import lexical.Token;

public class SyntaxException extends Exception {
    public SyntaxException(String expected, String expectedTerm, Token founded) { 
        super("Erro sintático, estava esperando [" + expected + "("+ expectedTerm + ")] e encontrei [" + founded.getType() + "(" +founded.getTerm()+")] na linha = " + founded.getLine() + " e coluna = " + founded.getColumn());
    }

    public SyntaxException(String expected, String founded) { 
        super("Erro sintático, estava esperando [" + expected + "] e encontrei [" + founded + "]");
    }
}
