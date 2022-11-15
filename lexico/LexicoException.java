package lexico;

public class LexicoException extends Exception {
    public LexicoException(String termo, int linha, int coluna) {
        super("Erro lexico no termo " + termo + " linha = " + linha + " coluna = " + coluna);
    }
}