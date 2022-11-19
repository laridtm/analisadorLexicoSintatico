package lexico;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class LexicalAnalyzer {
    private BufferedReader buffer;
    private int column = 1, line = 1;

    public LexicalAnalyzer(String fileName) {
        try {
            // Cria o arquivo e salva no buffer
            File file = new File(fileName);
            InputStream in = new FileInputStream(file);
            Reader reader = new InputStreamReader(in, Charset.defaultCharset());
            this.buffer = new BufferedReader(reader);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Token> analyze() {
        List<Token> tokens = new ArrayList<Token>();
        try {
            Token token = nextToken();
            while (token != null) {
                tokens.add(token);
                token = nextToken();
            }
        } catch (LexicalException e) {
            System.err.println(e.getMessage());
        }
        return tokens;
    }

    private Token nextToken() throws LexicalException {
        try {
            String term = "";
            int state = 0;
            int currentChar = buffer.read();

            while (currentChar != -1) {
                if (isNewLine(currentChar)) {
                    column = 1;
                    line++;
                }

                // Transforma o numero da tabela ASCII em char
                char character = (char) currentChar;
                switch (state) {
                    case 0:
                        if (isCharacter(character)) {
                            term += character;
                            state = 1;
                        } else if (isSpecialCharacter(character)) {
                            term += character;
                            state = 2;
                        } else if (isDigit(character)) {
                            term += character;
                            state = 3;
                        } else if (character == '"') {
                            term += character;
                            state = 4;
                        }
                        break;
                    case 1:
                        if (isCharacter(character) || isDigit(character)) {
                            term += character;
                        } else if (isReservedKeyword(term)) {
                            return new Token(Token.RESERVED_KEYWORD, term, line, column++);
                        } else if (isBooleanConst(term)) {
                            return new Token(Token.CONST_BOOL, term, line, column++);
                        } else {
                            return new Token(Token.IDENTIFIER, term, line, column++);
                        }
                        break;
                    case 2:
                        term += character;
                        if (!isSpecialCharacter(character)) {
                            return specialCharacterToToken(term);
                        }
                        break;
                    case 3:
                        if (isDigit(character)) {
                            term += character;
                        } else if (isTokenDelimiter(character)){
                            return new Token(Token.CONST_INTEGER, term, line, column++);
                        } else {
                            term += character;
                            throw new LexicalException(term, line, column);
                        }
                        break;
                    case 4:
                        term += character;
                        if (character == '"') {
                            return new Token(Token.CONST_LITERAL, term, line, column++); 
                        }
                        break;
                }

                column++;
                currentChar = buffer.read();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private Token specialCharacterToToken(String termo) throws LexicalException {
        //Remove espaco em branco do termo
        switch (termo.replaceAll("\\s+", "")) {
            case "+":
            case "*":
            case "-":
            case "/":
            case "<":
            case ">":
            case "<=":
            case ">=":
            case "==":
            case "!=":
                return new Token(Token.OPERATOR, termo, line, column);
            case "=":
                return new Token(Token.ASSIGN, termo, line, column);
            case "{":
                return new Token(Token.OPEN_BRACE, termo, line, column);
            case "}":
                return new Token(Token.CLOSE_BRACE, termo, line, column);
            case ";":
                return new Token(Token.DELIMITER, termo, line, column);
            default: 
                throw new LexicalException(termo, line, column);
        }
    }

    private boolean isBooleanConst(String termo) {
        return termo.equals("verdadeiro") || termo.equals("falso");
    }

    private boolean isSpace(char c) {
        return c == ' ';
    }
    
    private boolean isTokenDelimiter(char c) {
        return c == ' ' || c == ';';
    }

    private boolean isNewLine(int caracter) {
        if (caracter == 13 || caracter == 10) {
            return true;
        }
        return false;
    }

    private boolean isCharacter(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
    }

    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    private boolean isSpecialCharacter(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/' || c == '<' || c == '>' ||
                c == '=' || c == '!' || c == ';' || c == '{' || c == '}';
    }

    private boolean isReservedKeyword(String term) {
        if (term.equals("var") || term.equals("se") || term.equals("senao") ||
                term.equals("classe") || term.equals("enquanto")) {
            return true;
        }
        return false;
    }
}
