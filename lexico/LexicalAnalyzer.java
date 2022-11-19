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
    public static final int S0 = 0; // Initial State
    public static final int S1 = 1; // Character
    public static final int S2 = 2; // Special Character
    public static final int S3 = 3; // Cont Int
    public static final int S4 = 4; // Cont Literal
    public static final int S5 = 5; // Final State

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
            int state = S0;
            int currentChar = buffer.read();

            while (!isEOF(currentChar)) {
                if (isNewLine(currentChar)) {
                    column = 1;
                    line++;
                }

                // Transforma o numero da tabela ASCII em char
                char character = (char) currentChar;
                switch (state) {
                    case S0:
                        term += character;
                        if (isCharacter(character)) {
                            state = S1;
                            break;
                        } else if (isSpecialCharacter(character)) {
                            state = S2;
                            break;
                        } else if (isDigit(character)) {
                            state = S3;
                            break;
                        } else if (character == '"') {
                            state = S4;
                            break;
                        }
                        throw new LexicalException(term, line, column);
                    case S1:
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
                    case S2:
                        term += character;
                        if (!isSpecialCharacter(character)) {
                            return specialCharacterToToken(term);
                        }
                        break;
                    case S3:
                        if (isDigit(character)) {
                            term += character;
                        } else if (isTokenDelimiter(character)){
                            return new Token(Token.CONST_INTEGER, term, line, column++);
                        } else {
                            term += character;
                            throw new LexicalException(term, line, column);
                        }
                        break;
                    case S4:
                        term += character;
                        if (character == '"') {
                            return new Token(Token.CONST_LITERAL, term, line, column++); 
                        }
                        break;
                }

                column++;
                currentChar = buffer.read();
            }

            // Ultimo char do programa é um caracter especial
            if (state == S2) {
                return specialCharacterToToken(term);
            }

            if (state != S0) {
                throw new LexicalException(term, line, column);
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

    private boolean isEOF(int character) {
        return character == -1;
    }

    private boolean isBooleanConst(String termo) {
        return termo.equals("verdadeiro") || termo.equals("falso");
    }

    private boolean isTokenDelimiter(char c) {
        return c == ' ' || c == ';';
    }

    private boolean isNewLine(int character) {
        if (character == 13 || character == 10) {
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
