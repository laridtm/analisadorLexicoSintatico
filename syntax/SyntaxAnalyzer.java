package syntax;

import lexical.LexicalAnalyzer;
import lexical.Token;

public class SyntaxAnalyzer {
    private LexicalAnalyzer lexical;
    private Token currentToken;

    public SyntaxAnalyzer(LexicalAnalyzer lexical) {
        this.lexical = lexical;
    }

    public void analyze() {
        try {
            program();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    private void program() throws Exception {
        reservedKeyWord("classe");
        assertNextTokenIs(Token.IDENTIFIER);
        assertNextTokenIs(Token.OPEN_BRACE);
        codeBlock();
        assertNextTokenIs(Token.CLOSE_BRACE);
    }

    private void codeBlock() throws Exception {
        
    }

    private void reservedKeyWord(String keyword) throws Exception {
        nextToken();
        if(!tokenIs(Token.RESERVED_KEYWORD) || !currentToken.getTerm().equals(keyword)) {
            throw new SyntaxException(Token.RESERVED_KEYWORD, keyword, currentToken);
        }
    }

    private void assertNextTokenIs(String type) throws Exception {
        nextToken();
        if(!tokenIs(type)) {
            throw new SyntaxException(type, currentToken.getType());
        }
    }

    private boolean tokenIs(String type) {
        if (currentToken == null) {
            return false;
        }

        return currentToken.getType().equals(type);
    }

    private void nextToken() throws Exception {
        currentToken = lexical.nextToken();
    }
}
