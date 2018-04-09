package lexer;

public enum Tag {
    //Operadores
    OP_EQ,
    OP_NE,
    OP_GT,
    OP_LT,
    OP_GE,
    OP_LE,
    OP_AD,
    OP_MIN,
    OP_MUL,
    OP_DIV,
    OP_ASS,
    
    //Símbolos
    SMB_OBC,
    SMB_CBC,
    SMB_OPA,
    SMB_CPA,
    SMB_COM,
    SMB_SEM,
    
    //ID
    ID,
    
    //LITERAL
    LIT,
    
    //CONSTANTE
    CON_NUM,
    CON_CHAR,
    
    // Para as palavras reservada
    KW,
	
    // Final do arquivo
    EOF;
}
