import java.util.ArrayList;
import java.util.List;

/**
 * Created by Seven on 2016/11/4.
 */
public class Stack {
    //  输入带
    private List<Token> stack;

    public Stack(){
        stack=new ArrayList<Token>();
        this.stack.add(new Token(Token.END,"$l"));
        this.stack.add(new Token(Token.S,"s"));
    }

    public Token get(){
        return stack.get(stack.size()-1);
    }

    public Token pop(){
        Token token=stack.get(stack.size()-1);
        this.stack.remove(stack.size()-1);
        return token;
    }

    public void push(Token token){
        this.stack.add(token);
    }

}
