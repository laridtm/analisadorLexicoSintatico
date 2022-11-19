package lexico;

public class LexicalException extends Exception {
    public LexicalException(String term, int line, int column) {
        super("Erro lexico no termo " + term + " linha = " + line + " coluna = " + column);
    }
}