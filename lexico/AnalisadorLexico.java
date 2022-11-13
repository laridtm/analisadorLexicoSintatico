package lexico;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
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

        Token token = proximoToken();
        while (token != null) {
            tokens.add(token);
            token = proximoToken();
        }
        return tokens;
    }

    private Token proximoToken() {
        try {
            String termo = "";
            int estado = 0;
            int caracterCorrente = buffer.read();

            while(caracterCorrente != -1) {
                if (eNovaLinha(caracterCorrente)){
                    coluna = 1;
                    linha++;
                }

                // Transforma o numero da tabela ASCII em char
                char caracter = (char)caracterCorrente;
                switch (estado) {
                    case 0:
                        if (eCaracter(caracter)) {
                            termo += caracter;
                            estado = 1;
                        }
                        break;
                    case 1:
                        if (eCaracter(caracter) || eDigito(caracter)) {
                            termo += caracter;
                        } else if(ePalavraReservada(termo)){
                            return new Token(Token.PALAVRA_RESERVADA, termo, linha, coluna++);
                        } else {
                            return new Token(Token.IDENTIFICADOR, termo, linha, coluna++);
                        }
                        break;
                    default:
                        break;
                }
                
                coluna++;
                caracterCorrente = buffer.read();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private boolean eNovaLinha(int caracter) {
        if(caracter == 13 || caracter == 10){
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

    private boolean ePalavraReservada(String termo) {
        if (termo.equals("var") || termo.equals("se") || termo.equals("senao") ||
                termo.equals("classe") || termo.equals("enquanto")) {
            return true;
        }
        return false;
    }
}
