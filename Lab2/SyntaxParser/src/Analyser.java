import java.util.ArrayList;
import java.util.List;

/**
 * Created by Seven on 2016/10/22.
 */
public class Analyser {

    //reserved words
    private String[] reservedWords={"void","class","public",
            "private","protected","for","if","else","while","do",
            "int","double","char","boolean","String","new","try",
            "catch","static","return","this","main"};
//    operators
//    private String[] operators={"+","-","*","/",">","<","==",
//            "=",">=","<=","+=","-=","*=","/=","&&","||","|",
//            "&","!","!="};
//    notes
//    private String[] notes={"/*","*/"};
//    punctuation
//    private String[] punctuation={"{","}",";",".","(",")","[","]",":","\"",","};

/*===============================================================================================*/

    //analyse the input string
    public List<Token> scanner(char[] input){
        List<Token> tokens=new ArrayList<>();
        int p_reader=0;
        Word word;
        //begin with letter
        while (input[p_reader]!='#') {
            word=new Word();
            if (isLetter(input[p_reader])) {
                word.add(input[p_reader]);
                p_reader++;
                //next with letter or number
                while (isLetter(input[p_reader]) || isNumber(input[p_reader])) {
                    //get an ID or ReservedWord
                    //ReservedWord has priority
                    String s=ch2s(word.getValue());
//                    if(isReservedWord(s)) {
//                        break;
//                    }
                    word.add(input[p_reader]);
                    p_reader++;
                }
                String s=ch2s(word.getValue());
                if(isReservedWord(s)){
//                    System.out.println(s);
                    Token token=new Token(Token.RESERVEDWORD,s);
                    tokens.add(token);
                }else{
//                    System.out.println(s);
                    Token token=new Token(Token.ID,s);
                    tokens.add(token);
                }
            }
            //begin with number
            else if(isNumber(input[p_reader])){
                word.add(input[p_reader]);
                p_reader++;
                //next with number
                while (isNumber(input[p_reader])){
                    word.add(input[p_reader]);
                    p_reader++;
                }
                if(input[p_reader]=='.'){
                    word.add(input[p_reader]);
                    p_reader++;
                    if(!isNumber(input[p_reader])){
                        Token token=new Token(Token.ERROR,ch2s(word.getValue()),"Fail to Recognize");
                        tokens.add(token);
                    }else {
                        while (isNumber(input[p_reader])) {
                            word.add(input[p_reader]);
                            p_reader++;
                        }
                        String s=ch2s(word.getValue());
                        Token token=new Token(Token.NUMBER,s);
                        tokens.add(token);
                    }
                }else{
                    String s=ch2s(word.getValue());
                    Token token=new Token(Token.NUMBER,s);
                    tokens.add(token);
                }

            }
            //begin with other character
            else{
                word.add(input[p_reader]);
                switch (input[p_reader]){
                    case '+':{
                        p_reader++;
                        if(input[p_reader]=='=') {
                            word.add(input[p_reader]);
                            p_reader++;
                            Token token = new Token(Token.ADD_EQUAL, ch2s(word.getValue()));
                            tokens.add(token);
                        }else {
                            Token token = new Token(Token.ADD, ch2s(word.getValue()));
                            tokens.add(token);
                        }
                        break;
                    }
                    case '-':{
                        p_reader++;
                        //negative value
                        if(isNumber(input[p_reader])){
                            word.add(input[p_reader]);
                            p_reader++;
                            while(isNumber(input[p_reader])){
                                word.add(input[p_reader]);
                                p_reader++;
                            }
                            if(input[p_reader]=='.'){
                                word.add(input[p_reader]);
                                p_reader++;
                                if(!isNumber(input[p_reader])){
                                    Token token=new Token(Token.ERROR,ch2s(word.getValue()),"Fail to Recognize!");
                                    tokens.add(token);
                                }else{
                                    word.add(input[p_reader]);
                                    p_reader++;
                                    while (isNumber(input[p_reader])){
                                        word.add(input[p_reader]);
                                        p_reader++;
                                    }
                                    Token token=new Token(Token.NUMBER,ch2s(word.getValue()));
                                    tokens.add(token);
                                }
                            }else{
                                Token token = new Token(Token.NUMBER, ch2s(word.getValue()));
                                tokens.add(token);
                            }
                        }
                        else{
                            // -=
                            if(input[p_reader]=='=') {
                                word.add(input[p_reader]);
                                p_reader++;
                                Token token = new Token(Token.MIN_EQUAL, ch2s(word.getValue()));
                                tokens.add(token);
                            }else {
                                Token token = new Token(Token.MIN, ch2s(word.getValue()));
                                tokens.add(token);
                            }
                        }

                        break;
                    }
                    case '*':{
                        p_reader++;
                        Token token;
                        //*=
                        if(input[p_reader]=='=') {
                            word.add(input[p_reader]);
                            p_reader++;
                            token=new Token(Token.MUL_EQUAL,ch2s(word.getValue()));
                        }else {
                            token = new Token(Token.MUL, ch2s(word.getValue()));
                        }
                        tokens.add(token);
                        break;
                    }
                    case '/':{
                        p_reader++;
                        Token token;
                        // notes
                        if(input[p_reader]=='*'){
                            word.add(input[p_reader]);
                            p_reader++;
                            boolean found=false;
                            do{
                                if((input[p_reader]!='\n')&&(input[p_reader]!='*')){
                                    word.add(input[p_reader]);
                                    p_reader++;
                                }else{
                                    if(input[p_reader]=='\n') {
                                        //can only handle notes in a single line
                                        p_reader++;
                                        token = new Token(Token.ERROR, ch2s(word.getValue()), "Fail to Recognize!");
                                        tokens.add(token);
                                        break;
                                    }else{
                                        word.add(input[p_reader]);
                                        p_reader++;
                                        //if next is '/', found it, else continue
                                        if(input[p_reader]=='/'){
                                            word.add(input[p_reader]);
                                            token=new Token(Token.SLASH_STAR,ch2s(word.getValue()));
                                            tokens.add(token);
                                            found=true;
                                            p_reader++;
                                        }
                                    }
                                }
                            }while (!found);
                        }
                        // /=
                        else if(input[p_reader]=='=') {
                            word.add(input[p_reader]);
                            p_reader++;
                            token=new Token(Token.DIV_EQUAL,ch2s(word.getValue()));
                            tokens.add(token);
                        }else {
                            token = new Token(Token.DIV, ch2s(word.getValue()));
                            tokens.add(token);
                        }
                        break;
                    }
                    case '=':{
                        p_reader++;
                        Token token;
                        //==
                        if(input[p_reader]=='=') {
                            word.add(input[p_reader]);
                            p_reader++;
                            token=new Token(Token.DOUBLE_EQUAL,ch2s(word.getValue()));
                        }else {
                            token = new Token(Token.EQUAL, ch2s(word.getValue()));
                        }
                        tokens.add(token);
                        break;
                    }
                    case '>':{
                        p_reader++;
                        Token token;
                        //>=
                        if(input[p_reader]=='=') {
                            word.add(input[p_reader]);
                            p_reader++;
                            token=new Token(Token.MORE_EQUAL,ch2s(word.getValue()));
                        }else {
                            token = new Token(Token.MORE, ch2s(word.getValue()));
                        }
                        tokens.add(token);
                        break;
                    }
                    case '<':{
                        p_reader++;
                        Token token;
                        //<=
                        if(input[p_reader]=='=') {
                            word.add(input[p_reader]);
                            p_reader++;
                            token=new Token(Token.MIN_EQUAL,ch2s(word.getValue()));
                        }else {
                            token = new Token(Token.MIN, ch2s(word.getValue()));
                        }
                        tokens.add(token);
                        break;
                    }
                    case '&':{
                        p_reader++;
                        Token token;
                        //&&
                        if(input[p_reader]=='&') {
                            word.add(input[p_reader]);
                            p_reader++;
                            token=new Token(Token.DOUBLE_AND,ch2s(word.getValue()));
                        }else {
                            token = new Token(Token.AND, ch2s(word.getValue()));
                        }
                        tokens.add(token);
                        break;
                    }
                    case '|':{
                        p_reader++;
                        Token token;
                        //||
                        if(input[p_reader]=='|') {
                            word.add(input[p_reader]);
                            p_reader++;
                            token=new Token(Token.DOUBLE_OR,ch2s(word.getValue()));
                        }else {
                            token = new Token(Token.OR, ch2s(word.getValue()));
                        }
                        tokens.add(token);
                        break;
                    }
                    case '!':{
                        p_reader++;
                        Token token;
                        //!=
                        if(input[p_reader]=='=') {
                            word.add(input[p_reader]);
                            p_reader++;
                            token=new Token(Token.NOT_EQUAL,ch2s(word.getValue()));
                        }else {
                            token = new Token(Token.NOT, ch2s(word.getValue()));
                        }
                        tokens.add(token);
                        break;
                    }
                    case ';':{
                        p_reader++;
                        Token token=new Token(Token.SEMICOLON,ch2s(word.getValue()));
                        tokens.add(token);
                        break;
                    }
                    case '\"':{
                        p_reader++;
                        boolean found=false;
                        Token token;
                        while (!found){
                            //can only handle string in a single line
                            if(input[p_reader]!='\n'){
                                word.add(input[p_reader]);
                                if(input[p_reader]=='\"'){
                                    p_reader++;
                                    found=true;
                                    token=new Token(Token.DOUBLE_QUOTE,ch2s(word.getValue()));
                                    tokens.add(token);
                                }else{
                                    p_reader++;
                                }
                            }else{
                                p_reader++;
                                token=new Token(Token.ERROR,ch2s(word.getValue()),"Fail to Recognize!");
                                tokens.add(token);
                                break;
                            }
                        }
                        break;
                    }
                    case ':':{
                        p_reader++;
                        Token token=new Token(Token.COLON,ch2s(word.getValue()));
                        tokens.add(token);
                        break;
                    }
                    case '{':{
                        p_reader++;
                        Token token=new Token(Token.LEFT_BRACE,ch2s(word.getValue()));
                        tokens.add(token);
                        break;
                    }
                    case '}':{
                        p_reader++;
                        Token token=new Token(Token.RIGHT_BRACE,ch2s(word.getValue()));
                        tokens.add(token);
                        break;
                    }
                    case '[':{
                        p_reader++;
                        Token token=new Token(Token.LEFT_BRACKET,ch2s(word.getValue()));
                        tokens.add(token);
                        break;
                    }
                    case ']':{
                        p_reader++;
                        Token token=new Token(Token.RIGHT_BRACKET,ch2s(word.getValue()));
                        tokens.add(token);
                        break;
                    }
                    case '(':{
                        p_reader++;
                        Token token=new Token(Token.LEFT_PARENTHESE,ch2s(word.getValue()));
                        tokens.add(token);
                        break;
                    }
                    case ')':{
                        p_reader++;
                        Token token=new Token(Token.RIGHT_PARENTHESE,ch2s(word.getValue()));
                        tokens.add(token);
                        break;
                    }
                    case '.':{
                        p_reader++;
                        Token token=new Token(Token.DOT,ch2s(word.getValue()));
                        tokens.add(token);
                        break;
                    }
                    case ',':{
                        p_reader++;
                        Token token=new Token(Token.COMMA,ch2s(word.getValue()));
                        tokens.add(token);
                        break;
                    }
                    default:{
                        p_reader++;
                        break;
                    }
                }
            }
        }
        return tokens;
    }


    /*==========================================================================================*/
    private boolean isLetter(char i){
        return ((i>='a'&&i<='z')||(i>='A')&&(i<='Z'));
    }

    private boolean isNumber(char i){
        return (i>='0'&&i<='9');
    }

    private String ch2s(char[] ch){
        String s;
        int i=0;
        while(ch[i]!='\0'){
            i++;
        }
        i--;
        s=String.valueOf(ch).substring(0,i+1);
        return s;
    }

    private boolean isReservedWord(String s){
        for (String reservedWord : reservedWords) {
            if (s.equals(reservedWord)) {
                return true;
            }
        }
        return false;
    }

}
