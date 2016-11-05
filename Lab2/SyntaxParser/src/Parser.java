import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Seven on 2016/11/3.
 */
public class Parser {
    Token begin=new Token(Token.END,"$l");
    Token end=new Token(Token.END,"$r");
    PPT ppt=new PPT();
    List<String> derivation;

    //deal with the input part
    private char[] readFromTXT() {
        char[] input = new char[500];
        String inputFile = "input.txt";
        try {
            BufferedReader bf = new BufferedReader(new InputStreamReader(new FileInputStream(new File(inputFile))));
            String line;
            int index = 0;
            while ((line = bf.readLine()) != null) {
                //delete blanks
                char[] tmp = line.toCharArray();
                for (char a : tmp) {
                    //delete all the blank characters
                    input[index++] = a;
                }
                input[index++] = '\n';
            }
            input[index] = '#';
            bf.close();
        } catch (FileNotFoundException e) {
            System.out.println("Oops...Open File Failed");
        } catch (IOException e) {
            System.out.println("Oops...Read File Failed");
        }
        return input;
    }
//        parser token sequence
//        维护一个状态栈，初始状态为$r,S为栈顶
//        令X=栈顶符号
//        栈非空时，若X与输入带指向的符号相同，则pop(X),输入带后移->匹配
//        若X是终结符->报错
//        若M[X,a]为空->报错
//        若M[X,a]=某产生式，则 输出产生式，弹出栈顶符号，讲产生式右部从右至左压入栈中

    public List<String> parser(List<Token> tokens){
        Stack stack=new Stack();
        Queue queue=new Queue(tokens);
        Token reader=queue.get();
        derivation=new ArrayList<>();
        int productionNum;

        Token current=stack.get();
            while (!(current.getType() == Token.END && reader.getType() == Token.END)) {
                if (reader.getType() == current.getType()) {
                    //与读头相匹配
                    if (reader.getType() == Token.RESERVEDWORD) {
                        if (reader.getCode().equals(current.getCode())) {
//                            System.out.println("  "+reader.getCode());
                            stack.pop();
                            queue.dequeue();
                            reader = queue.get();
                        }
                    } else {
//                        System.out.println("  "+reader.getCode());
                        stack.pop();
                        queue.dequeue();
                        reader = queue.get();
                    }
                } else if (isVt(current)) {
                    //error
                    derivation.add("WRONG1");
                    break;
                } else if (lookUp(reader, current) == -1) {
                    //error
                    derivation.add("WRONG2");
                    break;
                } else if ((productionNum = lookUp(reader, current)) >= 0) {
                    //输出产生式
                    derivation.add(ppt.production.get(productionNum));
                    //弹出栈顶符号
                    stack.pop();
                    //将产生式入栈
                    switch (productionNum) {
                        case 0: {
                            stack.push(new Token(Token.SEMICOLON, ";"));
                            stack.push(new Token(Token.E, "E"));
                            stack.push(new Token(Token.EQUAL, "="));
                            stack.push(new Token(Token.ID, "id"));
                            break;
                        }
                        case 1: {
                            stack.push(new Token(Token.RIGHT_BRACE, "}"));
                            stack.push(new Token(Token.S, "S"));
                            stack.push(new Token(Token.LEFT_BRACE, "{"));
                            stack.push(new Token(Token.RESERVEDWORD, "else"));
                            stack.push(new Token(Token.RIGHT_BRACE, "}"));
                            stack.push(new Token(Token.S, "S"));
                            stack.push(new Token(Token.LEFT_BRACE, "{"));
                            stack.push(new Token(Token.RIGHT_PARENTHESE, ")"));
                            stack.push(new Token(Token.C, "C"));
                            stack.push(new Token(Token.LEFT_PARENTHESE, "("));
                            stack.push(new Token(Token.RESERVEDWORD, "if"));
                            break;
                        }
                        case 2: {
                            stack.push(new Token(Token.RIGHT_BRACE, "}"));
                            stack.push(new Token(Token.S, "S"));
                            stack.push(new Token(Token.LEFT_BRACE, "{"));
                            stack.push(new Token(Token.RIGHT_PARENTHESE, ")"));
                            stack.push(new Token(Token.C, "C"));
                            stack.push(new Token(Token.LEFT_PARENTHESE, "("));
                            stack.push(new Token(Token.RESERVEDWORD, "while"));
                            break;
                        }
                        case 3: {
                            stack.push(new Token(Token.E0, "E0"));
                            stack.push(new Token(Token.T, "T"));
                            break;
                        }
                        case 4: {
                            stack.push(new Token(Token.E0, "E0"));
                            stack.push(new Token(Token.T, "T"));
                            stack.push(new Token(Token.ADD, "+"));
                            break;
                        }
                        case 5: {
                            break;
                        }
                        case 6: {
                            stack.push(new Token(Token.T0, "T0"));
                            stack.push(new Token(Token.F, "F"));
                            break;
                        }
                        case 7: {
                            stack.push(new Token(Token.T0, "T0"));
                            stack.push(new Token(Token.F, "F"));
                            stack.push(new Token(Token.MUL, "*"));
                            break;
                        }
                        case 8: {
                            break;
                        }
                        case 9: {
                            stack.push(new Token(Token.RIGHT_PARENTHESE, ")"));
                            stack.push(new Token(Token.E, "E"));
                            stack.push(new Token(Token.LEFT_PARENTHESE, "("));
                            break;
                        }
                        case 10: {
                            stack.push(new Token(Token.ID, "id"));
                            break;
                        }
                        case 11: {
                            stack.push(new Token(Token.NUMBER, "number"));
                            break;
                        }
                        case 12: {
                            stack.push(new Token(Token.C0, "C0"));
                            stack.push(new Token(Token.D, "D"));
                            break;
                        }
                        case 13: {
                            stack.push(new Token(Token.C0, "C0"));
                            stack.push(new Token(Token.D, "D"));
                            stack.push(new Token(Token.DOUBLE_OR, "||"));
                            break;
                        }
                        case 14: {
                            break;
                        }
                        case 15: {
                            stack.push(new Token(Token.RIGHT_PARENTHESE, ")"));
                            stack.push(new Token(Token.C, "C"));
                            stack.push(new Token(Token.LEFT_PARENTHESE, "("));
                            break;
                        }
                        case 16: {
                            stack.push(new Token(Token.NUMBER, "number"));
                            stack.push(new Token(Token.DOUBLE_EQUAL, "=="));
                            stack.push(new Token(Token.ID, "id"));
                            break;
                        }
                        default:
                            break;
                    }
                }
                current = stack.get();
            }
        return derivation;
    }

    //look up the table return the number of production
    private int lookUp(Token reader,Token current){
        List<Token> Vn=ppt.Vn;
        for (int i=0;i<Vn.size();i++){
            //确定对应的非终结符
            if(current.getType()==Vn.get(i).getType()){
                Map<Token,Integer> item=ppt.table.get(i);
                for(Token key:item.keySet()){
                    if(reader.getType()==key.getType()){
                        //找到对应的产生式
                        //如果是保留字，则需要进一步比较code是否相等
                        if(reader.getType()==Token.RESERVEDWORD){
                            if(reader.getCode().equals(key.getCode())){
                                return item.get(key);
                            }
                        }else {
                            return item.get(key);
                        }
                    }
                }
            }
        }
        return -1;
    }

    //deal with the output part
    private void writeToTXT(List<String> result){
        File outputFile=new File("output.txt");
        try {
            if(!outputFile.exists()){
                outputFile.createNewFile();
            }
            BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile)));
            for(int i=0;i<result.size();i++){
                bw.write(result.get(i));
                bw.write('\n');
            }
            bw.flush();
            bw.close();
        } catch (FileNotFoundException e) {
            System.out.println("Oops...open file failed");
        } catch (IOException e) {
            System.out.println("Oops...write to file failed");
        }
    }

    //check if the token is in Vt sequence
    private boolean isVt(Token token){
        List<Token> Vn=ppt.Vn;
        for (int i=0;i<Vn.size();i++){
            if(token.getType()==Vn.get(i).getType()){
                return false;
            }
        }
        if(token.getType()==Token.END){
            return false;
        }else {
            return true;
        }
    }

    public static void main(String[] args){
        Parser parser=new Parser();
        char[] input=parser.readFromTXT();
        Analyser analyser=new Analyser();
        List<Token> tokens=analyser.scanner(input);
        List<String> result=parser.parser(tokens);
        parser.writeToTXT(result);
    }
}
