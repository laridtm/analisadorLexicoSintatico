package lexico;

public class Token {
    public static final String PALAVRA_RESERVADA = "palavra_reservada";
    public static final String IDENTIFICADOR = "identificador";
    public static final String ATRIBUICAO = "atribuicao";
    public static final String CONSTANTE_INTEIRA = "constante_inteira";
    public static final String CONSTANTE_LITERAL = "constante_literal";
    public static final String CONSTANTE_LOGICA = "constante_logica";
    public static final String DELIMITADOR = "delimitador";
    public static final String OPERADOR = "operador";
    public static final String ABRE_CHAVE = "abre_chave";
    public static final String FECHA_CHAVE = "fecha_chave";

    private String tipo;
    private String termo;
    private int linha;
    private int coluna;
    
    public Token(String tipo, String termo, int linha, int coluna) {
        this.tipo = tipo;
        this.termo = termo;
        this.linha = linha;
        this.coluna = coluna;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getTermo() {
        return termo;
    }

    public void setTermo(String termo) {
        this.termo = termo;
    }

    public int getLinha() {
        return linha;
    }

    public void setLinha(int linha) {
        this.linha = linha;
    }

    public int getColuna() {
        return coluna;
    }

    public void setColuna(int coluna) {
        this.coluna = coluna;
    }

    @Override
    public String toString() {
        return "Token [tipo = " + tipo + " termo = " + termo + " linha = "+ linha + " coluna = " + coluna +"]";
    }
}