package syntax;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lexical.LexicalAnalyzer;
import lexical.Token;

public class SyntaxAnalyzer {
    private LexicalAnalyzer lexical;
    private List<Token> tokenList = new ArrayList<Token>();
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
        nextToken();

        while (currentToken != null && !tokenIs(Token.CLOSE_BRACE)) {
            switch (currentToken.getType()) {
                case Token.RESERVED_KEYWORD:
                    if(currentToken.getTerm().equals("var")) {
                        variableDeclaration();
                    } else if(currentToken.getTerm().equals("se")) {
                        ifStatement();
                    } else if(currentToken.getTerm().equals("enquanto")) {
                        whileStatement();
                    } else {
                        throw new SyntaxException(Token.RESERVED_KEYWORD, "var, se, enquanto", currentToken);
                    }
                    break;
                case Token.IDENTIFIER:
                    valuation();
                    break;
                default:
                throw new SyntaxException(Token.RESERVED_KEYWORD + ", " + Token.IDENTIFIER, currentToken.getType());
            }

            nextToken();
        }

        saveTokenInList();
    }

    private void ifStatement() throws Exception {
        condition();
        assertNextTokenIs(Token.OPEN_BRACE);
        codeBlock();
        assertNextTokenIs(Token.CLOSE_BRACE);
        elseStatement();
    }

    private void whileStatement() throws Exception {
        condition();
        assertNextTokenIs(Token.OPEN_BRACE);
        codeBlock();
        assertNextTokenIs(Token.CLOSE_BRACE);
    }

    private void elseStatement() throws Exception {
        nextToken();
        if(tokenIs(Token.RESERVED_KEYWORD) && currentToken.getTerm().equals("senao")) {
            assertNextTokenIs(Token.OPEN_BRACE);
            codeBlock();
            assertNextTokenIs(Token.CLOSE_BRACE);
        } else {
            saveTokenInList();
        }
    }

    private void condition() throws Exception {
        nextToken();

        if(!tokenIs(Token.CONST_BOOL)) {
            expression();
        }
    }

    private void expression() throws Exception {
        term();
        assertNextTokenIs(Token.OPERATOR);
        nextToken();
        term();
    }

    private void term() throws Exception {
        String[] allowedTokens = new String[]{Token.CONST_INTEGER, Token.CONST_LITERAL, Token.CONST_BOOL, Token.IDENTIFIER};
        if(!Arrays.asList(allowedTokens).contains(currentToken.getType())) {
            throw new SyntaxException(String.join(", ", allowedTokens), currentToken.getType());
        }
    }

    private void variableDeclaration() throws Exception {
        assertNextTokenIs(Token.IDENTIFIER);
        valuation();
    }

    private void valuation() throws Exception {
        assertNextTokenIs(Token.ASSIGN);
        nextToken();
        term();
        assertNextTokenIs(Token.DELIMITER);
    }

    private void reservedKeyWord(String keyword) throws Exception {
        nextToken();
        if(!tokenIs(Token.RESERVED_KEYWORD) || !currentToken.getTerm().equals(keyword)) {
            throw new SyntaxException(Token.RESERVED_KEYWORD, keyword, currentToken);
        }
    }

    private void assertNextTokenIs(String type) throws Exception {
        nextToken();
        assertTokenIs(type);
    }

    private void assertTokenIs(String type) throws Exception {
        if (currentToken == null) {
            throw new SyntaxException(type, "null");
        }

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

    private void saveTokenInList() {
        if (currentToken != null) { 
            tokenList.add(currentToken);
        }
    }

    private void nextToken() throws Exception {
        if (!tokenList.isEmpty()){
            currentToken = tokenList.remove(0);
            return;
        }
        currentToken = lexical.nextToken();
    }
}
