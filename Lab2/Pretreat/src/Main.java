import java.util.ArrayList;
import java.util.List;

/**
 * Created by Seven on 2016/11/7.
 */
public class Main {

    public static void main(String[] args){
        List<Production> productions=new ArrayList<>();

        //generate productions without left-recursion
        Production p0=new Production('S',"AaAb");
        Production p1=new Production('S',"BbBa");
        Production p2=new Production('A',"e");
        Production p3=new Production('B',"e");

        productions.add(p0);
        productions.add(p1);
        productions.add(p2);
        productions.add(p3);

        Pretreat pretreat=new Pretreat();
        pretreat.calFirstAndFollow(productions);
    }

}
