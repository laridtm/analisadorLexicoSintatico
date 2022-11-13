//java Principal.java program_sample.txt
import java.util.List;

import lexico.AnalisadorLexico;
import lexico.Token;

public class Principal {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("É esperado o envio do path do arquivo a ser analisado como argumento do programa!");
            return;
        }

        AnalisadorLexico lexico = new AnalisadorLexico(args[0]);
        List<Token> tokens = lexico.analisar();

        for (Token token : tokens) {
            System.out.println(token.toString());
        }
    }
}