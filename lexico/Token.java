package lexico;

public class Token {
    public static final String RESERVED_KEYWORD = "palavra_reservada";
    public static final String IDENTIFIER = "identificador";
    public static final String ASSIGN = "atribuicao";
    public static final String CONST_INTEGER = "constante_inteira";
    public static final String CONST_LITERAL = "constante_literal";
    public static final String CONST_BOOL = "constante_logica";
    public static final String DELIMITER = "delimitador";
    public static final String MATH_OPERATOR = "operador_matematico";
    public static final String OPERATOR = "operador";
    public static final String OPEN_BRACE = "abre_chave";
    public static final String CLOSE_BRACE = "fecha_chave";

    private String type;
    private String term;
    private int line;
    private int column;
    
    public Token(String tipo, String termo, int linha, int coluna) {
        this.type = tipo;
        this.term = termo;
        this.line = linha;
        this.column = coluna;
    }

    public String getType() {
        return type;
    }

    public void setType(String typo) {
        this.type = typo;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    @Override
    public String toString() {
        return "Token [tipo = " + type + " termo = " + term + " linha = "+ line + " coluna = " + column +"]";
    }
}