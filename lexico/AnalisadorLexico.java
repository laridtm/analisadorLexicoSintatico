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

public class AnalisadorLexico {
    private BufferedReader buffer;
    private int coluna = 1, linha = 1;

    public AnalisadorLexico(String nomeArquivo) {
        try {
            // Cria o arquivo e salva no buffer
            File file = new File(nomeArquivo);
            InputStream in = new FileInputStream(file);
            Reader reader = new InputStreamReader(in, Charset.defaultCharset());
            this.buffer = new BufferedReader(reader);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Token> analisar() {
        List<Token> tokens = new ArrayList<Token>();
        try {
            Token token = proximoToken();
            while (token != null) {
                tokens.add(token);
                token = proximoToken();
            }
        } catch (LexicoException e) {
            System.err.println(e.getMessage());
        }
        return tokens;
    }

    private Token proximoToken() throws LexicoException {
        try {
            String termo = "";
            int estado = 0;
            int caracterCorrente = buffer.read();

            while (caracterCorrente != -1) {
                if (eNovaLinha(caracterCorrente)) {
                    coluna = 1;
                    linha++;
                }

                // Transforma o numero da tabela ASCII em char
                char caracter = (char) caracterCorrente;
                switch (estado) {
                    case 0:
                        if (eCaracter(caracter)) {
                            termo += caracter;
                            estado = 1;
                        } else if (eCaracterEspecial(caracter)) {
                            termo += caracter;
                            estado = 2;
                        } else if (eDigito(caracter)) {
                            termo += caracter;
                            estado = 3;
                        } else if (caracter == '"') {
                            termo += caracter;
                            estado = 4;
                        }
                        break;
                    case 1:
                        if (eCaracter(caracter) || eDigito(caracter)) {
                            termo += caracter;
                        } else if (ePalavraReservada(termo)) {
                            return new Token(Token.PALAVRA_RESERVADA, termo, linha, coluna++);
                        } else if (eConstanteBooleana(termo)) {
                            return new Token(Token.CONSTANTE_LOGICA, termo, linha, coluna++);
                        } else {
                            return new Token(Token.IDENTIFICADOR, termo, linha, coluna++);
                        }
                        break;
                    case 2:
                        termo += caracter;
                        if (!eCaracterEspecial(caracter)) {
                            return converteCaracterEspecial(termo);
                        }
                        break;
                    case 3:
                        if (eDigito(caracter)) {
                            termo += caracter;
                        } else if (eDelimitadorDeToken(caracter)){
                            return new Token(Token.CONSTANTE_INTEIRA, termo, linha, coluna++);
                        } else {
                            termo += caracter;
                            throw new LexicoException(termo, linha, coluna);
                        }
                        break;
                    case 4:
                        termo += caracter;
                        if (caracter == '"') {
                            return new Token(Token.CONSTANTE_LITERAL, termo, linha, coluna++); 
                        }
                        break;
                }

                coluna++;
                caracterCorrente = buffer.read();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private boolean eConstanteBooleana(String termo) {
        return termo.equals("verdadeiro") || termo.equals("falso");
    }

    private Token converteCaracterEspecial(String termo) throws LexicoException {
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
                return new Token(Token.OPERADOR, termo, linha, coluna);
            case "=":
                return new Token(Token.ATRIBUICAO, termo, linha, coluna);
            case "{":
                return new Token(Token.ABRE_CHAVE, termo, linha, coluna);
            case "}":
                return new Token(Token.FECHA_CHAVE, termo, linha, coluna);
            case ";":
                return new Token(Token.DELIMITADOR, termo, linha, coluna);
            default: 
                throw new LexicoException(termo, linha, coluna);
        }
    }

    private boolean eEspaco(char c) {
        return c == ' ';
    }
    
    private boolean eDelimitadorDeToken(char c) {
        return c == ' ' || c == ';';
    }

    private boolean eNovaLinha(int caracter) {
        if (caracter == 13 || caracter == 10) {
            return true;
        }
        return false;
    }

    private boolean eCaracter(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
    }

    private boolean eDigito(char c) {
        return c >= '0' && c <= '9';
    }

    private boolean eCaracterEspecial(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/' || c == '<' || c == '>' ||
                c == '=' || c == '!' || c == ';' || c == '{' || c == '}';
    }

    private boolean ePalavraReservada(String termo) {
        if (termo.equals("var") || termo.equals("se") || termo.equals("senao") ||
                termo.equals("classe") || termo.equals("enquanto")) {
            return true;
        }
        return false;
    }
}
