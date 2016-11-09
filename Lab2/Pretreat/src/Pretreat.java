import java.util.ArrayList;
import java.util.List;

/**
 * Created by Seven on 2016/11/7.
 */
public class Pretreat {
    List<Character> Vn;
    List<Character> Vt;

    public void calFirstAndFollow(List<Production> productions){
        this.splitVnVt(productions);
        //计算first
        //若非终结符对应的产生式右部 以终结符开头，则将其加入first集
        //                       以非终结符开头，则计算该非终结符的first()
        //若first集中包含e，则计算该非终结符的follow集
        //找到产生式右部包含该非终结符的下一字符
        //若为非终结符，则计算其first()集，若first集中含有e，则计算该产生式左部的非终结符的follow集
    }

    public void calFollow(){

    }

    public void splitVnVt(List<Production> productions){
        //将所有非终结符和终结符分离
        Vn=new ArrayList<>();
        Vt=new ArrayList<>();

        for(Production production:productions){
            if(!Vn.contains(production.leftPart)) {
                Vn.add(production.leftPart);
            }
        }
        int index=0;
        for (Production production:productions) {
            char[] right = production.rightPart.toCharArray();
            for(char c:right){
                //查找c是否在非终结符集合中，若在则为非终结符
                int flag=0;
                for(char vn:Vn){
                    if (vn==c){
                        flag=1;
                    }
                }
                if(flag==0){
                    //c为终结符
                    if(!Vt.contains(c)) {
                        Vt.add(c);
                    }
                }
            }
        }

        for(char c:Vt){
            System.out.println(c);
        }
    }
}
