//java Principal.java program_sample.txt
import java.util.List;

import lexical.LexicalAnalyzer;
import lexical.Token;

public class Main {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("É esperado o envio do path do arquivo a ser analisado como argumento do programa!");
            return;
        }

        LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer(args[0]);
        List<Token> tokens = lexicalAnalyzer.analyze();

        for (Token token : tokens) {
            System.out.println(token.toString());
        }
    }
}