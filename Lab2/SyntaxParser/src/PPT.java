import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * LL（1）分析表
 * Created by Seven on 2016/11/4.
 */
public class PPT {
    List<String> production;
    List<Map<Token,Integer>> table;
    List<Token> Vn;
    List<Token> Vt;

    public PPT(){
        production=new ArrayList<>();
        table=new ArrayList<>();
        Vn=new ArrayList<>();
        Vt=new ArrayList<>();
        this.initial();
    }

    private void initial(){
//        添加所有非终结符
        Vn.add(new Token(Token.S,"S"));
        Vn.add(new Token(Token.E,"E"));
        Vn.add(new Token(Token.E0,"E0"));
        Vn.add(new Token(Token.T,"T"));
        Vn.add(new Token(Token.T0,"T0"));
        Vn.add(new Token(Token.F,"F"));
        Vn.add(new Token(Token.C,"C"));
        Vn.add(new Token(Token.C0,"C0"));
        Vn.add(new Token(Token.D,"D"));
//        添加所有终结符
//        Vt.add(new Token(Token.END,"$l"));//0
        Vt.add(new Token(Token.ID,"id"));//0
        Vt.add(new Token(Token.RESERVEDWORD,"if"));//1
        Vt.add(new Token(Token.RESERVEDWORD,"while"));//2
        Vt.add(new Token(Token.ADD,"+"));//3
        Vt.add(new Token(Token.MUL,"*"));//4
        Vt.add(new Token(Token.SEMICOLON,";"));//5
        Vt.add(new Token(Token.DOUBLE_OR,"||"));//6
        Vt.add(new Token(Token.LEFT_PARENTHESE,"("));//7
        Vt.add(new Token(Token.RIGHT_PARENTHESE,")"));//8
        Vt.add(new Token(Token.NUMBER,"number"));//9
        Vt.add(new Token(Token.LEFT_BRACE,"{"));//10
        Vt.add(new Token(Token.RIGHT_BRACE,"}"));//11
        Vt.add(new Token(Token.RESERVEDWORD,"else"));//12
        Vt.add(new Token(Token.END,"$r"));//13
        Vt.add(new Token(Token.EQUAL,"="));//14
        Vt.add(new Token(Token.DOUBLE_EQUAL,"=="));//15

//        添加所有产生式
        production.add("S->id=E;");//0
        production.add("if(C){S}else{S}");//1
        production.add("S->while(C){S}");//2
        production.add("E->TE0");//3
        production.add("E0->+TE0");//4
        production.add("E0->e");//5
        production.add("T->FT0");//6
        production.add("T0->*FT0");//7
        production.add("T0->e");//8
        production.add("F->(E)");//9
        production.add("F->id");//10
        production.add("F->number");//11
        production.add("C->DC0");//12
        production.add("C0->||DC0");//13
        production.add("C0->e");//14
        production.add("D->(C)");//15
        production.add("D->id==number");//16
//        构造LL(1)分析表
        Map<Token ,Integer> item;
        //S
        item=new HashMap<>();
        item.put(Vt.get(0),0);
        item.put(Vt.get(1),1);
        item.put(Vt.get(2),2);
        table.add(item);
        //E
        item=new HashMap<>();
        item.put(Vt.get(0),3);
        item.put(Vt.get(7),3);
        item.put(Vt.get(9),3);
        table.add(item);
        //E0
        item=new HashMap<>();
        item.put(Vt.get(3),4);
        item.put(Vt.get(5),5);
        item.put(Vt.get(8),5);
        table.add(item);
        //T
        item=new HashMap<>();
        item.put(Vt.get(0),6);
        item.put(Vt.get(7),6);
        item.put(Vt.get(9),6);
        table.add(item);
        //T0
        item=new HashMap<>();
        item.put(Vt.get(3),8);
        item.put(Vt.get(4),7);
        item.put(Vt.get(5),8);
        item.put(Vt.get(8),8);
        table.add(item);
        //F
        item=new HashMap<>();
        item.put(Vt.get(0),10);
        item.put(Vt.get(7),9);
        item.put(Vt.get(9),11);
        table.add(item);
        //C
        item=new HashMap<>();
        item.put(Vt.get(0),12);
        item.put(Vt.get(7),12);
        table.add(item);
        //C0
        item=new HashMap<>();
        item.put(Vt.get(6),13);
        item.put(Vt.get(8),14);
        table.add(item);
        //D
        item=new HashMap<>();
        item.put(Vt.get(0),16);
        item.put(Vt.get(7),15);
        table.add(item);
    }
}
