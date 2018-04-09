package lexer;
import java.util.HashMap;

public class TablaSymbole {
    
    private HashMap<Token, Identificador> tS;

    public TablaSymbole() {
        tS = new HashMap();

        // Inserindo as palavras reservadas
        Token mot;
        mot = new Token(Tag.KW, "program", 0, 0);
        this.tS.put(mot, new Identificador());
        
        mot = new Token(Tag.KW, "if", 0, 0);
        this.tS.put(mot, new Identificador());
        
        mot = new Token(Tag.KW, "else", 0, 0);
        this.tS.put(mot, new Identificador());
        
        mot = new Token(Tag.KW, "while", 0, 0);
        this.tS.put(mot, new Identificador());
        
        mot = new Token(Tag.KW, "write", 0, 0);
        this.tS.put(mot, new Identificador());
        
        mot = new Token(Tag.KW, "read", 0, 0);
        this.tS.put(mot, new Identificador());
        
        mot = new Token(Tag.KW, "num", 0, 0);
        this.tS.put(mot, new Identificador());

        mot = new Token(Tag.KW, "char", 0, 0);
        this.tS.put(mot, new Identificador());

        mot = new Token(Tag.KW, "not", 0, 0);
        this.tS.put(mot, new Identificador());

        mot = new Token(Tag.KW, "or", 0, 0);
        this.tS.put(mot, new Identificador());
        
        mot = new Token(Tag.KW, "and", 0, 0);
        this.tS.put(mot, new Identificador());
    }
    
    public void put(Token w, Identificador i) {
        tS.put(w, i);
    }

    // Retorna um identificador de um determinado token
    public Identificador getIdentificador(Token w) {
        Identificador infoIdentificador = (Identificador) tS.get(w);
        return infoIdentificador;
    }

    // Pesquisa se o lexema já existe
    public Token retornaToken(String lexema) {
        for (Token token : tS.keySet()) {
            if (token.getLexema().equals(lexema)) {
                return token;
            }
        }
        return null;
    }
    
    @Override
    public String toString() {
        String saida = "\nTabela de Símbolo: \n";
        int i = 1;
        for (Token token : tS.keySet()){
            saida += ("" + token.toString()) + "\n";
            i++;
        }
        return saida;
    }
}