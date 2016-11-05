import java.util.List;

/**
 * Created by Seven on 2016/11/4.
 */
public class Queue {
//  输入带
    private List<Token> input;

    public Queue(List<Token> tokens){
        this.input=tokens;
        this.input.add(new Token(Token.END,"$r"));
    }

    public Token get(){
        return input.get(0);
    }

    public Token dequeue(){
        Token token=input.get(0);
        this.input.remove(0);
        return token;
    }

    public void enqueue(Token token){
        this.input.add(token);
    }

}
