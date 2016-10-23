import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Seven on 2016/10/22.
 */
public class Analyser {

    //reserved words
    private String[] reservedWords={"void","class","public",
            "private","protected","for","if","else","while","do",
            "int","double","char","boolean","string","new","try",
            "catch","static","return","this"};
    //operators
    private String[] operators={"+","-","*","/",">","<","==",
            "=",">=","<=","+=","-=","*=","/=","&&","||","|",
            "&","!","!="};
    //notes
    private String[] notes={"/*","*/"};
    //punctuation
    private String[] punctuation={"{","}",";",".","(",")","[","]",":","\""};

/*===============================================================================================*/

    //deal with the input part
    public char[] readFromTXT(){
        char[] input=new char[200];
        String inputFile="input.txt";
        try {
            BufferedReader bf=new BufferedReader(new InputStreamReader(new FileInputStream(new File(inputFile))));
            String line="";
            int index=0;
            while((line=bf.readLine())!=null){
                //delete blanks
                char[] tmp=line.toCharArray();
                for(int i=0;i<tmp.length;i++){
                    //delete all the blank characters
                    if((tmp[i]!=' ')&&(tmp[i]!='\t')){
                        input[index++]=tmp[i];
                    }
                }
                input[index++]='\n';
            }
            input[index]='#';
            bf.close();
        } catch (FileNotFoundException e) {
            System.out.println("Oops...Open File Failed");
        } catch (IOException e) {
            System.out.println("Oops...Read File Failed");
        }
        return input;
    }

    //analyse the input string
    public List<Token> scanner(char[] input){
        List<Token> tokens=new ArrayList<>();
        int p_reader=0;
        //begin with letter
        while (input[p_reader]!='#') {
            Word word=new Word();

            if (isLetter(input[p_reader])) {
                word.add(input[p_reader]);
                p_reader++;
                //next with letter or number
                while (isLetter(input[p_reader]) || isNumber(input[p_reader])) {
                    //get an ID or ReservedWord
                    //ReservedWord has priority
                    String s=ch2s(word.getValue());
                    Token token;
                    if(isReservedWord(s)) {
                        token = new Token("ReservedWord", s);
                        tokens.add(token);
                        break;
                    }

                    word.add(input[p_reader]);
                    p_reader++;


                }
                String s=ch2s(word.getValue());
                if(!isReservedWord(s)){
                    Token token=new Token("ID",s);
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
                        Token token=new Token("",ch2s(word.getValue()),"Fail to Recognize");
                        tokens.add(token);
                    }else {
                        while (isNumber(input[p_reader])) {
                            word.add(input[p_reader]);
                            p_reader++;
                        }
                        String s=ch2s(word.getValue());
                        Token token=new Token("Float",s);
                        tokens.add(token);
                    }
                }else{
                    String s=ch2s(word.getValue());
                    Token token=new Token("Number",s);
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
                            Token token = new Token("Operator+=", ch2s(word.getValue()));
                        }else {
                            Token token = new Token("Operator+", ch2s(word.getValue()));
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
                                    Token token=new Token("",ch2s(word.getValue()),"Fail to Recognize!");
                                    tokens.add(token);
                                }else{
                                    word.add(input[p_reader]);
                                    p_reader++;
                                    while (isNumber(input[p_reader])){
                                        word.add(input[p_reader]);
                                        p_reader++;
                                    }
                                    Token token=new Token("Negative Float",ch2s(word.getValue()));
                                    tokens.add(token);
                                }
                            }else{
                                Token token = new Token("Negative Number", ch2s(word.getValue()));
                                tokens.add(token);
                            }
                        }
                        else{
                            // -=
                            if(input[p_reader]=='=') {
                                word.add(input[p_reader]);
                                p_reader++;
                                Token token = new Token("Operator-=", ch2s(word.getValue()));
                                tokens.add(token);
                            }else {
                                Token token = new Token("Operator-", ch2s(word.getValue()));
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
                            token=new Token("Operator*=",ch2s(word.getValue()));
                        }else {
                            token = new Token("Operator*", ch2s(word.getValue()));
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
                                        token = new Token("", ch2s(word.getValue()), "Fail to Recognize!");
                                        tokens.add(token);
                                        break;
                                    }else{
                                        word.add(input[p_reader]);
                                        p_reader++;
                                        //if next is '/', found it, else continue
                                        if(input[p_reader]=='/'){
                                            found=true;
                                            token=new Token("Notes",ch2s(word.getValue()));
                                            tokens.add(token);
                                        }
                                    }
                                }
                            }while (!found);
                        }
                        // /=
                        else if(input[p_reader]=='=') {
                            word.add(input[p_reader]);
                            p_reader++;
                            token=new Token("Operator/=",ch2s(word.getValue()));
                            tokens.add(token);
                        }else {
                            token = new Token("Operator/", ch2s(word.getValue()));
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
                            token=new Token("Operator==",ch2s(word.getValue()));
                        }else {
                            token = new Token("Operator=", ch2s(word.getValue()));
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
                            token=new Token("Operator>=",ch2s(word.getValue()));
                        }else {
                            token = new Token("Operator>", ch2s(word.getValue()));
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
                            token=new Token("Operator<=",ch2s(word.getValue()));
                        }else {
                            token = new Token("Operator<", ch2s(word.getValue()));
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
                            token=new Token("Operator&&",ch2s(word.getValue()));
                        }else {
                            token = new Token("Operator&", ch2s(word.getValue()));
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
                            token=new Token("Operator||",ch2s(word.getValue()));
                        }else {
                            token = new Token("Operator|", ch2s(word.getValue()));
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
                            token=new Token("Operator!=",ch2s(word.getValue()));
                        }else {
                            token = new Token("Operator!", ch2s(word.getValue()));
                        }
                        tokens.add(token);
                        break;
                    }
                    case ';':{
                        p_reader++;
                        Token token=new Token("Operator;",ch2s(word.getValue()));
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
                                    token=new Token("Punctuation\"\"",ch2s(word.getValue()));
                                    tokens.add(token);
                                }else{
                                    p_reader++;
                                }
                            }else{
                                p_reader++;
                                token=new Token("",ch2s(word.getValue()),"Fail to Recognize!");
                                tokens.add(token);
                                break;
                            }
                        }
                        break;
                    }
                    case ':':{
                        p_reader++;
                        Token token=new Token("Punctuation:",ch2s(word.getValue()));
                        break;
                    }
                    case '{':{
                        p_reader++;
                        Token token=new Token("Punctuation{",ch2s(word.getValue()));
                        break;
                    }
                    case '}':{
                        p_reader++;
                        Token token=new Token("Punctuation}",ch2s(word.getValue()));
                        break;
                    }
                    case '[':{
                        p_reader++;
                        Token token=new Token("Punctuation[",ch2s(word.getValue()));
                        break;
                    }
                    case ']':{
                        p_reader++;
                        Token token=new Token("Punctuation]",ch2s(word.getValue()));
                        break;
                    }
                    case '(':{
                        p_reader++;
                        Token token=new Token("Punctuation(",ch2s(word.getValue()));
                        break;
                    }
                    case ')':{
                        p_reader++;
                        Token token=new Token("Punctuation)",ch2s(word.getValue()));
                        break;
                    }
                    case '.':{
                        p_reader++;
                        Token token=new Token("Punctuation.",ch2s(word.getValue()));
                        break;
                    }
                }
            }
        }
        return tokens;
    }

    //deal with the output part
    public void writeToTXT(List<Token> tokens){
        try {
            BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("output.txt"))));
            for(int i=0;i<tokens.size();i++){
                bw.write(tokens.get(i).toString());
            }
            bw.close();
        } catch (FileNotFoundException e) {
            System.out.println("Oops...open file failed");
        } catch (IOException e) {
            System.out.println("Oops...write to file failed");
        }
    }


    public static void main(String[] args){
        Analyser analyser=new Analyser();
        char[] input=analyser.readFromTXT();
        List<Token> tokens=analyser.scanner(input);
        analyser.writeToTXT(tokens);
    }
/*==========================================================================================*/
    private boolean isLetter(char i){
        if((i>='a'&&i<='z')||(i>='A')||(i<='Z')){
            return true;
        }
        return false;
    }

    private boolean isNumber(char i){
        if(i>='0'&&i<='9'){
            return true;
        }
        return false;
    }

    private String ch2s(char[] ch){
        String s="";
        int i=0;
        while(ch[i]!='\0'){
            i++;
        }
        i--;
        s=String.valueOf(ch).substring(0,i);
        return s;
    }

    private boolean isReservedWord(String s){
        for(int i=0;i<reservedWords.length;i++){
            if(s.equals(reservedWords[i])){
                return true;
            }
        }
        return false;
    }

}
