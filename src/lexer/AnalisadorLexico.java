package lexer;
import java.io.*;
import java.util.ArrayList;

public class AnalisadorLexico {

	private static final int END_OF_FILE = -1; //Para identificar o final do arquivo
    private static int lookahead = 0; //Armazena o último caractere lido do arquivo	
    public static int n_line = 1; //Contador de linhas
    public static int n_column = 0; //Contador de colunas
    private RandomAccessFile instance_file; // Referência do arquivo
    private static TablaSymbole tablaSymbole; // A variavel para a Tabela de Símbolo
    
    public AnalisadorLexico(String caminho) {	
	    // Abre uma instância do arquivo solicitado
		try {
	            instance_file = new RandomAccessFile(caminho, "r"); // No modo leitura
		}
		catch(IOException e) {
	            System.out.println("Erro de abertura do arquivo " + caminho + "\n" + e);
	            System.exit(1);
		}
		catch(Exception e) {
	            System.out.println("Erro do programa ou falha da tabela de simbolos\n" + e);
	            System.exit(2);
		}
    }
    
    // Fecha a instância
    public void fechaArquivo() {
        try {
            instance_file.close();
        }
		catch (IOException errorFile) {
	            System.out.println ("Erro ao fechar arquivo\n" + errorFile);
	            System.exit(3);
		}
    }
    
    //Reporta erro para o usuário
    public void sinalizaErro(String mensagem) {
        System.out.println("[Erro Lexico]: " + mensagem + "");
    }
    
    //Volta uma posição do buffer de leitura
    public void retornaPonteiro(){
        try {
            // Não é preciso retornar o ponteiro em caso de Fim de Arquivo
            if (lookahead != END_OF_FILE) {
                instance_file.seek(instance_file.getFilePointer() - 1);
                n_column -= 1; //Já que está voltando uma "casa", tem que voltar uma posição no contador
            }    
        }
        catch(IOException e) {
            System.out.println("Falha ao retornar a leitura\n" + e);
            System.exit(4);
        }
    }

    // Pegar próximo token do arquivo
    public Token proxToken() {
		StringBuilder lexema = new StringBuilder();
		int estado = 0;
		char c;
			
		while(true) {
			c = '\u0000'; // null char
	            
            // Vai para o próximo caractere
            try {
                lookahead = instance_file.read(); 
                if(lookahead != END_OF_FILE) {
                    c = (char) lookahead;
                }
                n_column += 1;
            }
            catch(IOException e) {
                System.out.println("Erro na leitura do arquivo");
                System.exit(3);
            }
            
            // Aqui analisaremos o estado e modificações do automato
            switch(estado) {
                case 0:
                    if (lookahead == END_OF_FILE)
                        return new Token(Tag.EOF, "EOF", n_line, n_column);
                    else if (c == ' '){
                    }
                    else if (c == '\r') {
                    	n_column = 0;
                    }else if (c == '\n'){
                    	n_line += 1;
                    	n_column = 0;
                    }else if (c == '\t'){
                    	n_column += 2;
                    }
                    else if(c == '='){
                    	estado = 1;
                    }else if(c == '!'){
                    	estado = 2;
                    }else if(c == '>'){
                    	estado = 3;
                    }else if(c == '<'){
                    	estado = 4;
                    }else if(c == '+'){
                    	estado = 5;
                    	return new Token(Tag.OP_AD, "+", n_line, n_column);
                    }else if(c == '-'){
                    	estado = 6;
                    	return new Token(Tag.OP_MIN, "-", n_line, n_column);
                    }else if(c == '*'){
                    	estado = 7;
                    	return new Token(Tag.OP_MUL, "*", n_line, n_column);
                    }else if(c == '/'){
                    	estado = 25;
                    }else if(Character.isDigit(c)){
                    	estado = 9;
                    	lexema.append(c);
                    }else if (Character.isLetter(c)){
                    	estado = 10;
                        lexema.append(c);
                    }else if(c == '{'){
                    	estado = 11;
                    	return new Token(Tag.SMB_OBC, "{", n_line, n_column);
                    }else if(c == '}'){
                    	estado = 12;
                    	return new Token(Tag.SMB_CBC, "}", n_line, n_column);
                    }else if(c == '('){
                    	estado = 13;
                    	return new Token(Tag.SMB_OPA, "(", n_line, n_column);
                    }else if(c == ')'){
                    	estado = 14;
                    	return new Token(Tag.SMB_CPA, ")", n_line, n_column);
                    }else if(c == ','){
                    	estado = 15;
                    	return new Token(Tag.SMB_COM, ",", n_line, n_column);
                    }else if(c == ';'){
                    	estado = 16;
                    	return new Token(Tag.SMB_SEM, ";", n_line, n_column);
                    }else if(c == (char)39){ // Se é igual a aspas simples
                    	estado = 22;
                    }else if(c == '"'){
                    	estado = 23;
                    }
                    else {
                        sinalizaErro("Caractere invalido " + c + " na linha " + n_line + " e coluna " + n_column);
                        return null;
                    }
                    break;
                    
                case 1:
                	if(c == '='){
                		estado = 17;
                		return new Token(Tag.OP_EQ, "==", n_line, n_column);
                	}else {
                        retornaPonteiro();
                        return new Token(Tag.OP_ASS, "=", n_line, n_column);
                    }
                case 2:
                	if(c == '='){
                		estado = 18;
                		return new Token(Tag.OP_NE, "!=", n_line, n_column);
                	}else {
                        sinalizaErro("Caractere invalido (!) ou está faltando um (=) na linha " + n_line + " e coluna " + n_column);
                        return null;
                    }
                case 3:
                	if(c == '='){
                		estado = 19;
                		return new Token(Tag.OP_GE, ">=", n_line, n_column);
                	}else {
                        retornaPonteiro();
                        return new Token(Tag.OP_GT, ">", n_line, n_column);
                    }
				case 4:
                	if(c == '='){
                		estado = 20;
                		return new Token(Tag.OP_LE, "<=", n_line, n_column);
                	}else {
                        retornaPonteiro();
                        return new Token(Tag.OP_LT, "<", n_line, n_column);
                    }
				case 9:
                	if(Character.isDigit(c)){
                    	estado = 9;
                    	lexema.append(c);
                    }else if (c == '.'){
                    	lexema.append(c);
                    	estado = 21;
                    }
                    else{
                    	retornaPonteiro();						
            			return new Token(Tag.CON_NUM, lexema.toString(), n_line, n_column);
                    }
                    break;
                case 10:
                	if (Character.isLetterOrDigit(c) || c == '_') {
                		estado = 10;
                		lexema.append(c);
                    }
                    else {
                        retornaPonteiro();  
                        Token token = tablaSymbole.retornaToken(lexema.toString());
                        
                        if (token == null) {
                            Token novoToken = new Token(Tag.ID, lexema.toString(), n_line, n_column);
                            tablaSymbole.put(novoToken, new Identificador());
                            return novoToken;
                        }
                        return token;
                    }
                    break;
                case 21:
                	if (Character.isDigit(c)) {
                        lexema.append(c);
                        estado = 24;
                    }
                    else {
                        sinalizaErro("Padrao para número invalido na linha " + n_line + " coluna " + n_column);
                        return null;
                    }
                    break;
                case 22:
                	if (c == (char)39) {
                        return new Token(Tag.CON_CHAR, lexema.toString(), n_line, n_column);
                    }
                    else if (lookahead == END_OF_FILE) {
                        sinalizaErro("Conjunto de char deve ser fechado com ' antes do fim de arquivo");
                        return null;
                    }
                    else {
                        lexema.append(c);
                    }
                	break;
                case 23:
                	if (c == '"') {
                        return new Token(Tag.LIT, lexema.toString(), n_line, n_column);
                    }
                    else if (lookahead == END_OF_FILE) {
                        sinalizaErro("String deve ser fechada com \" antes do fim de arquivo");
                        return null;
                    }
                    else { // Se vier outro, permanece no estado 24
                        lexema.append(c);
                    }
                	break;	
                case 24:
                    if (Character.isDigit(c)) {
                        lexema.append(c);
                    }
                    else {
                        retornaPonteiro();						
                        return new Token(Tag.CON_NUM, lexema.toString(), n_line, n_column);
                    }
                    break;
                case 25:
                	if(c == '*'){
                		estado = 26;
                	}else if(c == '/'){
                		estado = 27;
                	}else{
                		retornaPonteiro();
                		return new Token(Tag.OP_DIV, "/", n_line, n_column);
                	}
                	
                	break;
                case 26:
                	if(c == '*'){
                		estado = 28; 
                	}else if (lookahead == END_OF_FILE){
                		sinalizaErro("Comentário deve ser fechado antes do fim do arquivo com */ ");
                		estado = 0;
                		n_column = 0;
                		n_line += 1;
                	}else{
                		estado = 26;
                	}
                	break;
                case 27:
                	if(c == '\n'){ //Acabou a linha e consequentemente o comentário
                		estado = 0;
                		n_column = 0;
                		n_line += 1;
                	}else{
                		estado = 27;
                	}
                	break;
                case 28:
                	if(c == '/'){
                		estado = 0;
                	}else if (lookahead == END_OF_FILE){
                		sinalizaErro("Comentário deve ser fechado antes do fim do arquivo com */ ");
                		estado = 0;
                		n_column = 0;
                		n_line += 1;
                	}else{
                		estado = 26;
                	}
                	break;
            }
		}
    }
                   
    public static void main(String[] args) {
    	//AnalisadorLexico analisador = new AnalisadorLexico("PrimeiroCerto.txt");
    	//AnalisadorLexico analisador = new AnalisadorLexico("SegundoCerto.txt");
    	//AnalisadorLexico analisador = new AnalisadorLexico("TerceiroCerto.txt");
    	//AnalisadorLexico analisador = new AnalisadorLexico("PrimeiroErrado.txt");
    	//AnalisadorLexico analisador = new AnalisadorLexico("SegundoErrado.txt");
    	AnalisadorLexico analisador = new AnalisadorLexico("TerceiroErrado.txt");
    	Token token;
        tablaSymbole = new TablaSymbole();
        ArrayList<Token> listaTokens = new ArrayList<Token>();

        // Iremos percorrer o arquivo até encontrarmos o final do mesmo
        do {
            token = analisador.proxToken();
            
            // Mostrar o token no console, e armazena no vetor
            if(token != null) {
                System.out.println("Token: <"+token.getClasse()+", \""+token.getLexema()+"\">\t Linha: " + n_line + "\t Coluna: " + n_column);
                listaTokens.add(token);
            }
		} while(token == null || token.getClasse() != Tag.EOF);
        
        System.out.println(tablaSymbole.toString());
        analisador.fechaArquivo();
    }
}
